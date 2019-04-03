package org.riggvar.base;

import org.riggvar.bo.TMain;
import org.riggvar.conn.TAdapterInputNCP;
import org.riggvar.conn.TAdapterOutputNCP;
import org.riggvar.conn.TServerFactory;
import org.riggvar.conn.TServerFunction;

public class TAdapterBO implements IHandleContextMsg {
    public TAdapterInputNCP InputServer;
    public TAdapterOutputNCP OutputServer;
    public IOutput Output;

    public IConnection AdapterInputConnection;
    public IConnection AdapterOutputConnection;

    public TAdapterBO(TAdapterParams aParams) {
        TMain.AdapterBO = this;

        IServer ts;
        try {
            ts = TServerFactory.CreateServer(TMain.IniImage.PortIn, TServerFunction.Input);
            InputServer = new TAdapterInputNCP(ts);

            ts = TServerFactory.CreateServer(TMain.IniImage.PortOut, TServerFunction.Output);
            OutputServer = new TAdapterOutputNCP(ts);
        } catch (Exception ex) {
            InputServer = null;
            OutputServer = null;
        }

        Output = new TAdapterOutput();
    }

    public TAdapterWatches getWatches() {
        return TGlobalWatches.Instance;
    }

    public void HandleMsg(TContextMsg cm) {
        if (this.AdapterOutputConnection != null) {
            // while processing a switch message,
            // when multicasting to internally connected clients,
            // block the message from going out
            if (!cm.getIsSwitchMsg()) {
                // OutputServer.SendMsg(TBaseIniImage.DefaultEventType, cm);
                TGlobalWatches.Instance.setMsgOut(cm.msg);
                cm.MsgType = TPeerController.MsgTypeInput;
                OutputServer.Server.SendMsg(0, cm); // broadcast
            }
        }
    }

    public void Dispose() {
        if (TMain.BO != null)
            TMain.BO.Disconnect();
        InputServer.Server.Dispose();
        OutputServer.Server.Dispose();
    }

}
