package org.riggvar.conn;

import org.riggvar.base.IServer;
import org.riggvar.base.TContextMsg;
import org.riggvar.bo.TMain;

public class TAdapterOutputNCP extends TAdapterBaseNCP {

    public TAdapterOutputNCP(IServer ts) {
        super(ts);
    }

    @Override
    protected void HandleMsg(TContextMsg cm) {
        // answer directly to requests on output (without calculation)
        Server.Reply(cm.Sender, TMain.BO.Output.getMsg(cm.msg));
    }

}
