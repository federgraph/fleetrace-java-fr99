package org.riggvar.conn;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.util.*;

public class TSocketChannelThread extends Thread {

    private int port;
    TSocketConnections connections;
    private SelectionKey acceptKey;
    private Selector acceptSelector;
    private boolean disposed = false;
    public String ServerName;

    public TSocketChannelThread(int aport) throws IOException {
        port = aport;
        ServerName = "SocketChannelThread"; // will be updated to a more specific value
        setName("SocketChannelThread");
        // setDaemon(true);
        connections = new TSocketConnections();
    }

    public void dispose() {
        // System.out.println(ServerName + ".dispose()");
        acceptKey.cancel();
        connections.dispose();
        try {
            disposed = true;
            if (acceptSelector.isOpen()) {
                acceptSelector.wakeup();
                if (acceptSelector.isOpen()) {
                    acceptSelector.close();
                    if (acceptSelector.isOpen()) {
                        System.out.println("acceptSelector still open in " + ServerName);
                    }
                    // acceptSelector = null; //no side effect
                    // connections can be established after Selector.close
                    // only the selector does not respond any more
                }
            }
        } catch (IOException ex) {
            System.out.println("IOException in " + ServerName + ".Dispose()");
        }
    }

    @Override
    public void run() {
        System.out.println("enter run method " + ServerName);
        try {
            if (!disposed) {
                acceptConnections(port);
            }
        } catch (Exception ex) {
            System.err.println("Exception" + ex.getMessage());
            disposed = true;
        } finally {
        }
        System.out.println("exit run method " + ServerName);
    }

    // Accept connections. Lazy Exception thrown.
    private void acceptConnections(int port) throws Exception {
        acceptSelector = SelectorProvider.provider().openSelector();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        // Bind the channel's server socket to the local host and port
        InetAddress lh = InetAddress.getLocalHost();
        InetSocketAddress isa = new InetSocketAddress(lh, port);
        ssc.socket().bind(isa);

        acceptKey = ssc.register(acceptSelector, SelectionKey.OP_ACCEPT);

        int keysAdded = 0;
        // The select method will return when any operations registered above have
        // occurred,
        // the thread has been interrupted, etc.
        while ((keysAdded = acceptSelector.select()) > 0) {
            Set<SelectionKey> readyKeys = acceptSelector.selectedKeys();
            Iterator<SelectionKey> i = readyKeys.iterator();

            // Walk through the ready keys collection and process requests.
            while (i.hasNext()) {
                SelectionKey sk = i.next();
                i.remove();
                ServerSocketChannel nextReady = (ServerSocketChannel) sk.channel();
                SocketChannel sc = nextReady.accept();
                Socket s = sc.socket();
                System.out.println("connection accepted from " + s.getInetAddress().getHostAddress());
                connections.addChannel(sc);
            }
            if (disposed) {
                break;
            }
        }
        if (keysAdded == 0) {
            // System.out.println("acceptSelector.select() returned with 0 in " +
            // ServerName);
        }
    }

}
