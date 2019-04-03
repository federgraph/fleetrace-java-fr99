package org.riggvar.conn;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.util.*;

//manages an accepted connection in form of a SocketChannel instance
public class TSocketConnections extends Thread {
    public TSocketServer server;

    public Selector readSelector;
    int count = 0;
    boolean disposed = false;
    Vector<TSocketConnection> Items = new Vector<TSocketConnection>();

    Vector<SocketChannel> newChannels = new Vector<SocketChannel>();

    private SocketChannel NextChannel() {
        if (newChannels.size() > 0) {
            return newChannels.remove(0);
        }
        return null;
    }

    public int getConnectionCount() {
        return Items.size();
    }

    public TSocketConnections() throws IOException {
        setName("TSocketConnections");
        // setDaemon(true);
        readSelector = SelectorProvider.provider().openSelector();
    }

    public void dispose() {
        disposed = true;

        for (int i = 0; i < Items.size(); i++) {
            TSocketConnection sc = Items.get(i);
            if (sc == null) {
                continue;
            }
            sc.close();
        }
        readSelector.wakeup();
    }

    synchronized void addChannel(SocketChannel channel) {
        newChannels.add(channel);
        if (count == 0) {
            registerChannel();
            start();
        } else {
            // call to register would block while select on read is pending
            readSelector.wakeup();
        }
    }

    private void registerChannel() {
        SocketChannel newChannel;
        while ((newChannel = NextChannel()) != null) {
            try {
                if (!newChannel.isOpen()) {
                    return; // channel.open();
                }
                if (!newChannel.isConnected()) {
                    return; // channel.finishConnect();
                }
                if (newChannel.isBlocking()) {
                    newChannel.configureBlocking(false);
                }
                SelectionKey sk = newChannel.register(readSelector, SelectionKey.OP_READ);
                TSocketConnection connection = new TSocketConnection(this, newChannel);
                sk.attach(connection);
                Items.add(connection);
                connection.ID = count;
            } catch (Exception ex) {
                System.err.println("Exception in registerChannel: " + ex.getMessage());
            } finally {
                count++;
            }
        }
    }

    private void removeChannel(SelectionKey sk) {
        TSocketConnection c = (TSocketConnection) sk.attachment();
        if (Items.remove(c)) {
            sk.attach(null);
            System.out.println("removed connection " + c.ID);
            sk.cancel();
            try {
                sk.channel().close();
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public void run() {
        System.out.println("enter run method TSocketConnections");
        while (!disposed) {
            try {
                read();
            } catch (InterruptedException ex) {
                disposed = true;
                System.out.println("TSocketConnections: InterruptedException.");
            } catch (Exception ex) {
                System.err.println("Exception in TSocketConnections.Run (readDataFromSockets): " + ex.getMessage());
                disposed = true;
            }
        }
        System.out.println("exit run method TSocketConnections");
    }

    private void read() throws Exception {
        if (!readSelector.isOpen()) {
            return;
        }

        // if (Items.size() == 0) return;
        int selectedCount = readSelector.select();
        if (selectedCount > 0) {
            Set<SelectionKey> readyKeys = readSelector.selectedKeys();
            Iterator<SelectionKey> i = readyKeys.iterator();
            while (i.hasNext()) {
                SelectionKey sk = i.next();
                i.remove();

                TSocketConnection c = (TSocketConnection) sk.attachment();
                if (c == null) {
                    return;
                }

                if (!sk.isValid()) {
                    removeChannel(sk);
                    return;
                }

                if (c.read() == 0) {
                    removeChannel(sk);
                }
            }
        }
        registerChannel();
    }

    public void write(String s) {
        if (Items.size() == 0) {
            return;
        }

        String ss = (char) 2 + s + (char) 3;
        ByteBuffer bb = ByteBuffer.wrap(ss.getBytes());
        for (int i = 0; i < Items.size(); i++) {
            TSocketConnection sc = Items.get(i);
            if (sc == null) {
                continue;
            }
            sc.write(bb);
        }
    }

    public void sendMsg(int KatID, String s) {
        if (Items.size() == 0) {
            return;
        }

        String ss = (char) 2 + s + (char) 3;
        ByteBuffer bb = ByteBuffer.wrap(ss.getBytes());

        TSocketConnection sc;
        for (int i = 0; i < Items.size(); i++) {
            sc = Items.get(i);
            if ((KatID == 0 || sc.getKatID() == 0) || sc.getKatID() == KatID) {
                sc.write(bb);
            }
        }
    }

    public void Reply(Object Connection, String s) {
        if (Items.size() == 0) {
            return;
        }
        for (int i = 0; i < Items.size(); i++) {
            TSocketConnection sc = Items.get(i);
            if (sc == Connection) {
                String ss = (char) 2 + s + (char) 3;
                ByteBuffer bb = ByteBuffer.wrap(ss.getBytes());
                sc.write(bb);
                break;
            }
        }
    }

}
