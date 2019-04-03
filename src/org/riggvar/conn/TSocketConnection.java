package org.riggvar.conn;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

import org.riggvar.base.*;
import java.nio.charset.*;

public class TSocketConnection {
    public static String RegisterClientString = "RiggVar.RegisterClient.";

    public static int RegisteredKatID = -1; // must be set for every connection
    public int ID;

    private TSocketConnections collection;
    private SocketChannel channel;
    private StringBuffer sb = new StringBuffer();
    private ByteBuffer byteBuffer;

    private boolean useUnicode = false;
    private Charset charset = Charset.forName("UTF-8");
    private CharsetEncoder encoder = charset.newEncoder();
    private CharsetDecoder decoder = charset.newDecoder();

    public TSocketConnection(TSocketConnections aCollection, SocketChannel achannel) throws IOException {
        collection = aCollection;
        channel = achannel;
        byteBuffer = ByteBuffer.allocate(16);
    }

    public void setKatID(int value) {
        TSocketConnection.RegisteredKatID = value;
    }

    public int getKatID() {
        return TSocketConnection.RegisteredKatID;
    }

    public void Dispose() {
        // do nothing - used with internal connections
    }

    public TSocketServer getServer() {
        return collection.server;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void close() {
        try {
            if (channel.isOpen()) {
                channel.close();
            }
        } catch (IOException ex) {
            System.out.println("IOException in TSocketConnection.close()");
        }
    }

    public void write(String s) {
        ByteBuffer bb;
        if (useUnicode) {
            try {
                // use UTF-8 encoding
                bb = encoder.encode(CharBuffer.wrap(s));
            } catch (CharacterCodingException ex) {
                // use default encoding
                bb = ByteBuffer.wrap(s.getBytes());
            }
        } else {
            // use default encoding
            bb = ByteBuffer.wrap(s.getBytes());
        }
        write(bb);
    }

    public void write(ByteBuffer bb) {
        if (!channel.isConnected()) {
            return;
        }
        try {
            channel.write(bb);
        } catch (IOException ex) {
            System.out.println("cannot write String to SocketChannel");
        }
    }

    public int read() {
        int br = 0;
        try {
            if (!channel.isOpen()) {
                System.out.println("channel closed in TSocketConnection.read()");
                return br;
            }
            byteBuffer.clear();
            int bytesRead = channel.read(byteBuffer);
            while (bytesRead > 0) {
                if (useUnicode) {
                    String request = decoder.decode(byteBuffer).toString();
                    for (int i = 0; i < request.length(); i++) {
                        char c = request.charAt(i);
                        setMsgChar(c);
                        br++;
                    }
                } else {
                    for (int i = 0; i < bytesRead; i++) {
                        byte b = byteBuffer.get(i);
                        char c = (char) b;
                        setMsgChar(c);
                        br++;
                    }
                }
                byteBuffer.clear();
                bytesRead = channel.read(byteBuffer);
            }
        } catch (AsynchronousCloseException ex) {
            System.err.println("AsynchronousCloseException in TSocketConnection.read()");
        } catch (IOException ex) {
            System.err.println("IOException in TSocketConnection.read()");
        }
        return br;
    }

    private void setMsgChar(char ch) {
        // 24. January 2007
        // attention: Switch always send 'switch connect'
        // instead of RiggVar.RegisterClient.*
        // therefore KatID must be assigned.
        if (ch == (char) 2) {
            sb = new StringBuffer();
        } else if (ch == (char) 3) {
            String s = sb.toString();
            int tempKatID = getKatID();
            if (s.startsWith(RegisterClientString)) {
                String t = s.substring(RegisterClientString.length() - 1);
                if (t.startsWith(".SKK"))
                    tempKatID = LookupKatID.SKK;
                else if (t.startsWith(".Rgg"))
                    tempKatID = LookupKatID.Rgg;
                else if (t.startsWith(".FR"))
                    tempKatID = LookupKatID.FR;
                else if (t.startsWith("t.SBPGS"))
                    tempKatID = LookupKatID.SBPGS;
                else if (t.length() > 1) {
                    String u = t.substring(1);
                    int i = Utils.StrToIntDef(u, tempKatID);
                    setKatID(i);
                }
            } else
                getServer().OnMsgReceived(this, s);
        } else {
            sb.append(ch);
        }
    }
}
