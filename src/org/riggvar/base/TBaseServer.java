package org.riggvar.base;

import org.riggvar.conn.TServerFunction;

public class TBaseServer implements IServer {
    public static int Status_Active = 1;

    private IInjectMsgEvent FOnHandleMsg;
    private TServerFunction FServerFunction;
    private int FPort;
    private boolean FActive;
    public TMsgSource MsgSource;

    public TBaseServer(int aPort, TServerFunction aServerFunction) {
        FPort = aPort;
        FServerFunction = aServerFunction;
        MsgSource = TMsgSource.Unknown;
    }

    public void Dispose() {
        // virtual
    }

    public int Port() {
        return FPort;
    }

    public void SendMsg(int KatID, TContextMsg cm) {
        // virtual
    }

    public void Reply(Object Connection, String s) {
        // virtual
    }

    /**
     * must be implemented for connections, not implemented for socket connections
     */
    public IConnection Connect() {
        // virtual
        return null;
    }

    public int Status() {
        if (getActive()) {
            return Status_Active;
        } else {
            return 0;
        }
    }

    public int ConnectionCount() {
        return -1;
    }

    public IInjectMsgEvent getOnHandleMsg() {
        return FOnHandleMsg;
    }

    public void setOnHandleMsg(IInjectMsgEvent value) {
        FOnHandleMsg = value;
    }

    public TServerFunction getServerFunction() {
        return FServerFunction;
    }

    public boolean getIsBridge() {
        return FServerFunction == TServerFunction.Bridge;
    }

    public boolean getIsOutput() {
        return FServerFunction == TServerFunction.Output;
    }

    public void setIsOutput(boolean value) {
        FServerFunction = TServerFunction.Output;
    }

    public boolean getActive() {
        return FActive;
    }

    public void setActive(boolean value) {
        FActive = value;
    }

    public void setMsg(Object sender, String s) {
        if (FOnHandleMsg != null) {
            FOnHandleMsg.InjectMsg(sender, MsgSource, s);
        }
    }

    public void Trace(String s) {
        FRTrace.Trace(s);
    }

}
