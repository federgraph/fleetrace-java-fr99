package org.riggvar.scoring;

import java.io.*;
import java.net.*;

public class FR51 {
    public static int Port = 3037;

    public static void main(String[] args) {
        org.riggvar.js08.ScoringManager.setTrace(false);

        try {
            int c = args.length;
            if (c > 0) {
                Port = ScoringUtils.StrToIntDef(args[0], Port);
            }
            FR51 scoringServer = new FR51();
            scoringServer.runServer();
        } catch (Exception ex) {
            System.out.println("exception-message:");
            System.out.println(ex.getMessage());
            System.out.println("exception-cause:");
            System.out.println(ex.getCause());
            System.out.println("exception-stacktrace:");
            System.out.println(ex.getStackTrace());
        }
    }

    public void runServer() throws IOException {
        System.out.println("running main in FR51...");
        ServerSocket s = new ServerSocket(Port);
        System.out.println("ScoringServer listening on Port " + Port);
        try {
            while (true) {
                // Blocks until a connection occurs:
                Socket socket = s.accept();
                try {
                    ThreadCountContainer.threadCount++;
                    new ScoringServerThread(socket);

                } catch (IOException e) {
                    // If it fails, close the socket,
                    // otherwise the thread will close it:
                    socket.close();
                }
            }
        } finally {
            s.close();
        }
    }

}
