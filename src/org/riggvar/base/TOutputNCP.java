package org.riggvar.base;

import org.riggvar.bo.TMain;

public class TOutputNCP extends TBaseNCP {

    public TOutputNCP(IServer ts) {
        super(ts);
    }

    @Override
    protected void HandleMsg(TContextMsg cm) {
        // answer directly to requests on output (without calculation)
        Server.Reply(cm.Sender, TMain.BO.Output.getMsg(cm.msg));
    }

    @Override
    public void InjectMsg(Object sender, TMsgSource ms, String s) {
        TContextMsg cm = new TContextMsg();
        cm.MsgSource = ms;
        cm.msg = s;
        SendMsg(cm.KatID, cm);
    }

    public void SendMsg(int KatID, TContextMsg cm) {
        TMain.BO.getWatches().setMsgOut(cm.msg);
        Server.SendMsg(KatID, cm); // broadcast
    }

}
