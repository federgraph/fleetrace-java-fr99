package org.riggvar.conn;

import org.riggvar.base.IInjectMsgEvent;
import org.riggvar.base.IServer;
import org.riggvar.base.TContextMsg;
import org.riggvar.base.TMsgSource;

public class TAdapterBaseNCP implements IInjectMsgEvent {
    public IServer Server;

    public TAdapterBaseNCP(IServer ts) {
        Server = ts;
        Server.setOnHandleMsg(this);
    }

    public void InjectMsg(Object sender, TMsgSource ms, String s) {
        TContextMsg cm = new TContextMsg();
        cm.MsgSource = ms;
        cm.Sender = sender;
        cm.IsAdapterMsg = true;
        cm.msg = s;
        HandleMsg(cm);
    }

    protected void HandleMsg(TContextMsg cm) {
        // virtual;
    }

}
