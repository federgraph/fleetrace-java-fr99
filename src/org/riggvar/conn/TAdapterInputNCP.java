package org.riggvar.conn;

import org.riggvar.base.ConnectionNotifierEventArgs;
import org.riggvar.base.DrawNotifierEventArgs;
import org.riggvar.base.IServer;
import org.riggvar.base.MsgContext;
import org.riggvar.base.TContextMsg;
import org.riggvar.base.TGlobalWatches;
import org.riggvar.base.TMsgSource;
import org.riggvar.bo.TMain;

public class TAdapterInputNCP extends TAdapterBaseNCP {

    public TAdapterInputNCP(IServer ts) {
        super(ts);
    }

    @Override
    public void HandleMsg(TContextMsg cm) {
        cm.DecodeHeader();

        if (cm.MsgType == 'W') {
            String s = ""; // TMain.GuiManager.WebReceiver.Receive(cm.msg);
            if (!s.isEmpty())
                Server.Reply(cm.Sender, s);
            return;
        }

        if (cm.msg.equals("switch connect")) {
            MsgContext.SwitchSender = cm.Sender;
            if (TMain.ConnectionNotifier != null) {
                TMain.ConnectionNotifier.ConnectionStatusChanged(this,
                        new ConnectionNotifierEventArgs(ConnectionNotifierEventArgs.SenderTypeExternalPlug,
                                ConnectionNotifierEventArgs.ConnectStatusConnect));
            }
            return;
        } else if (cm.msg.equals("switch disconnect")) {
            MsgContext.SwitchSender = null;
            // notify main form ?
            return;
        }
        if (cm.Sender == MsgContext.SwitchSender && MsgContext.SwitchSender != null)
            cm.MsgSource = TMsgSource.Switch;

        if (TMain.AdapterBO.AdapterInputConnection != null) {
            // answer to multiline request (with request)
            if (cm.msg.indexOf(".Request.") > -1) {
                TMain.BO.InputServer.HandleMsg(cm);
                Server.Reply(cm.Sender, cm.Answer);
            } else {
                TGlobalWatches.Instance.setMsgIn(cm.msg);
                MsgContext.SwitchLocked = true; // lock adapter output for generated messages (Redo)
                TMain.BO.InputServer.HandleMsg(cm);
                TMain.SoundManager.PlaySound(1);
            }
            TMain.DrawNotifier.ScheduleFullUpdate(this,
                    new DrawNotifierEventArgs(DrawNotifierEventArgs.DrawTargetEvent));
        }

    }

}
