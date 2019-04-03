package org.riggvar.bo;

import java.net.*;
import java.io.*;
import org.riggvar.base.*;

public class TStatusFeedback extends TLineParser {
    private InetAddress address;
    private DatagramSocket socket;
    private TBO BO;

    public boolean Enabled;
    public String Host;
    public int Port;

    public TStatusFeedback(TBO abo) {
        BO = abo;
    }

    public void Dispose() {
        if (socket != null) {
            socket.close();
            socket = null;
        }
    }

    @Override
    protected boolean ParseKeyValue(String Key, String Value) {
        if (Utils.Pos("Manage.", Key) == 1) {
            Key = Utils.Copy(Key, ("Manage.").length() + 1, Key.length());
        }

        if (Key.equals("StatusTrigger")) {
            BO.OnIdle();
            SendStatus(BO.getHashString());
        } else if (Key.equalsIgnoreCase("Clear")) {
            BO.ClearCommand();
        } else if (Key.equalsIgnoreCase("Feedback.Host"))
            Host = Value;
        else if (Key.equalsIgnoreCase("Feedback.Port"))
            Port = Utils.StrToIntDef(Value, Port);
        else
            return false;

        return true;
    }

    public void SendStatus(String msg) {
        if (Enabled) {
            try {

                if (socket == null)
                    socket = new DatagramSocket();
                if (address == null)
                    address = InetAddress.getByName(Host);
                byte[] buf = msg.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Port);
                socket.send(packet);
            } catch (SocketException ex) {
                Enabled = false;
            } catch (UnknownHostException ex) {
                Enabled = false;
            } catch (IOException ex) {
                Enabled = false;
            } catch (Exception ex) {
                Enabled = false;
            }

        }

    }
}
