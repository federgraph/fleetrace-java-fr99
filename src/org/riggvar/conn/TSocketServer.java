package org.riggvar.conn;

import java.io.*;
import org.riggvar.base.*;
import org.riggvar.bo.TMain;

public class TSocketServer extends TBaseServer {
    TSocketChannelThread channelThread = null;

    public TSocketServer(int aPort, TServerFunction aServerFunction) {
        super(aPort, aServerFunction);
        MsgSource = TMsgSource.TCP;
        open();
    }

    @Override
    public int ConnectionCount() {
        if (channelThread != null)
            if (channelThread.connections != null)
                return channelThread.connections.getConnectionCount();
        return -1;
    }

    @Override
    public void Dispose() {
        if (channelThread == null)
            return;
        try {
            if (channelThread.isAlive()) {
                channelThread.dispose();
                channelThread.join(1000);
                if (channelThread.isAlive())
                    System.out.println(getServerName() + " thread is still alive");
                else
                    System.out.println(getServerName() + " thread is dead");
            }
        } catch (InterruptedException ex) {
            System.out.println("InterruptedException in Test01.Dispose");
        }
    }

    public String getServerName() {
        switch (getServerFunction()) {
        case Output:
            return "OutputServer";
        case Input:
            return "InputServer";
        case Bridge:
            return "BridgeServer";
        default:
            return "SocketServer";
        }
    }

    @Override
    public boolean getActive() {
        if (channelThread == null)
            return false;
        return super.getActive() && channelThread.isAlive();
    }

    /**
     * broadcast
     * 
     * @param cm contains msg to send
     */
    @Override
    public void SendMsg(int KatID, TContextMsg cm) {
        if (channelThread == null)
            return;
        if (this.getIsOutput() || this.getIsBridge()) {
            // if (KatID == 0 || cm.KatID == KatID)
            // {
            if (getIsOutput()) {
                // handled as Adapter Messages at destination
                this.channelThread.connections.sendMsg(KatID, cm.getEncodedMsg());
                // header is extracted only for Adapter messages
            } else {
                // no Header used for Bridge messages
                this.channelThread.connections.sendMsg(KatID, cm.msg);
            }
            // }
        }
    }

    @Override
    public void Reply(Object Connection, String s) {
        if (s.isEmpty() || s == null)
            s = "empty";

        if (channelThread == null)
            return;
        this.channelThread.connections.Reply(Connection, s);
    }

    synchronized public void OnMsgReceived(TSocketConnection sender, String s) {
        if (!getIsOutput()) {
            setMsg(sender, s);
            TMain.BO.OnIdle(); // --> Calc, ProcessQueue --> SetAnswer
        } else {
            setMsg(sender, s);
        }
    }

    public void open() {
        if (channelThread != null)
            return;

        try {
            channelThread = new TSocketChannelThread(this.Port());
            channelThread.connections.server = this;
            channelThread.ServerName = getServerName();
            channelThread.start();
            setActive(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        if (channelThread == null)
            return;

        setActive(false);
        channelThread.dispose();
        try {
            if (channelThread.isAlive()) {
                channelThread.join(5000);
                if (channelThread.isAlive()) {
                    System.out.println(getServerName() + " still alive");
                } else {
                    System.out.println(getServerName() + " thread dead");
                }
            }
        } catch (InterruptedException ex) {
            System.out.println("InterruptedException in TSocketServer");
        }
    }

    // TODO: check Broadcast function in TSocketServer
    public void Broadcast(Object Sender, String s) {
        TContextMsg cm = new TContextMsg();
        cm.msg = s;
        this.SendMsg(LookupKatID.FR, cm);
    }

}
