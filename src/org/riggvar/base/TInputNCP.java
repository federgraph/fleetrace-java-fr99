package org.riggvar.base;

import org.riggvar.bo.*;

public class TInputNCP extends TBaseNCP {
    public TBaseMsgQueue MsgQueue;

    public TInputNCP(IServer ts) {
        super(ts);
        MsgQueue = new TBaseMsgQueue();
    }

    @Override
    public void InjectMsg(Object sender, TMsgSource ms, String s) {
        TContextMsg cm = new TContextMsg();
        cm.Sender = sender;
        cm.MsgSource = ms;
        cm.msg = s;
        HandleMsg(cm);
    }

    @Override
    public void HandleMsg(TContextMsg cm) {
        TBaseMsgList msg = TMain.MsgListFactory.CreateMsg();
        msg.Prot = cm.msg;
        if (msg.DispatchProt()) {
//            if (TMain.WantAutoSync)
//                TMain.GuiManager.CacheMotor.SynchronizeIfNotActive();

            // send receipt
            if ((msg.MsgResult == 0) && (msg.DBID > 0))
                Server.Reply(cm.Sender, Utils.IntToStr(msg.DBID));

            if (cm.getIsSwitchMsg() && msg.OutputRequestList != null) {
//                //do not pass on what originated from Switch
//                //may trigger calculation and painting as appropriate (e.g. SKK)
//                if (msg.KatID == LookupKatID.SKK)
//                {
//                    TMain.BO.Calc();
//                    TMain.BO.OutputServer.SendMsg(cm.KatID, cm); //send on internally only
//                }
            } else {
                cm.IsQueued = true;
                if (msg.getCmd().equals(TMain.BO.cTokenOutput) || msg.getCmd().equals(TMain.BO.cTokenAnonymousOutput)) {
                    cm.HasRequest = true;
                    if (msg.OutputRequestList.getCount() > 1) {
                        cm.OutputRequestList = new TStringList();
                        cm.OutputRequestList.Assign(msg.OutputRequestList);
                    } else {
                        cm.msg = msg.getMsgValue();
                    }
                }
                // send message on only after calculation
                MsgQueue.Enqueue(cm);
            }

        }
        // unlock adapter output
        MsgContext.SwitchLocked = false;
    }

    @Override
    public void ProcessQueue() {
        TContextMsg cm;
        while (MsgQueue.getCount() > 0) {
            cm = MsgQueue.Dequeue();
            if (cm != null) {

                if (cm.HasRequest) {
                    if (cm.OutputRequestList != null) {
                        cm.Answer = TMain.BO.Output.getAll(cm.OutputRequestList);
                    } else {
                        cm.Answer = TMain.BO.Output.getMsg(cm.msg);
                    }
                    if (!cm.IsAdapterMsg)
                        Server.Reply(cm.Sender, cm.Answer);
                    // else if (cm.IsAdapterMsg) //see AdapterInputNCP
                    // TMain.AdapterBO.InputServer.Server.Reply(cm.Sender, cm.Answer);
                } else if (TMain.BO.OutputServer != null) {
                    TMain.BO.OutputServer.SendMsg(cm.KatID, cm);
                    TMain.DrawNotifier.ScheduleFullUpdate(this,
                            new DrawNotifierEventArgs(DrawNotifierEventArgs.DrawTargetEvent));
                }
            }
        }
    }

}
