package org.riggvar.scoring;

import java.io.*;
import java.net.*;

public class ScoringServerThread extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private StringBuffer sb = new StringBuffer();
    private int ThreadNo;

    private void SetMsgBrutto(Object sender, char ch) {
        if (ch == (char) 2) {
            sb = new StringBuffer();
        } else if (ch == (char) 3) {
            SetMsg(sender, sb.toString());
        } else {
            sb.append(ch);
        }
    }

    private void SetMsg(Object sender, String s) {
        int i = s.indexOf("<?xml ");
        if (i == 3 || s.startsWith("<?xml ")) {
            System.out.print("message " + ThreadNo + "(" + s.length() + ")");

            // read xml into object
            TFRProxy p = new TFRProxy();
            if (i == 3)
                p.ReadFromString(s.substring(3));
            else
                p.ReadFromString(s);

            // load new regatta from data in p,
            // calc (scoreRegatta in javascore),
            // and retrieve (unload) calculated data into p

            // make sure real ProxyLoader is set up via injection
            TProxyLoader.getInstance().Calc(p);

            // create new xml msg (fill StringBuffer inside XmlWriter)
            // form data in p
            XmlWriter xw = new XmlWriter();
            p.WriteXml(xw);
            p = null;

            // log new xml to console when debugging
            // System.out.println(xw.toString());

            // send back answer over network
            out.println((char) 2 + xw.toString() + (char) 3);
            xw = null;
        } else {
            System.err.println(s);
        }
        out.flush();
    }

    public ScoringServerThread(Socket s) throws IOException {
        ThreadNo = ThreadCountContainer.threadCount;
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Enable auto-flush:
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        // If any of the above calls throw an
        // exception, the caller is responsible for
        // closing the socket. Otherwise the thread
        // will close it.
        start(); // Calls run()
    }

    @Override
    public void run() {
        try {
            char[] cb = new char[1];
            while (ThreadCountContainer.threadCount == ThreadNo) {
                int i = in.read(cb);
                if (i == 1) {
                    SetMsgBrutto(null, cb[0]);
                    if (cb[0] == (char) 3) {
                        break;
                    }
                }
            }
            // System.out.println("closing...");
        } catch (IOException e) {
            System.err.println("IO Exception" + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket not closed");
            }
        }
    }

}

class ThreadCountContainer {
    public static int threadCount;
}
