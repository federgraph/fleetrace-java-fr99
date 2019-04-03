package org.riggvar.conn;

import org.riggvar.base.IServer;

public class TServerFactory {
    public static IServer CreateServer(int Port, TServerFunction ServerFunction) {
        // if (Main.IniImage.SearchForUsablePorts)
        // {
        // Port = FindUsablePort(Port);
        // }

        try {
            return new TSocketServer(Port, ServerFunction);
        } catch (Exception ex) {
            return new TServerIntern(Port, ServerFunction);
        }

    }
}
