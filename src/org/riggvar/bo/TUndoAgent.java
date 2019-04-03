package org.riggvar.bo;

import org.riggvar.base.*;
import org.riggvar.input.TMsgTree;

public class TUndoAgent implements IMsgEvent {
    public boolean UndoFlag;
    private boolean FUndoLock;

    protected String UndoMsg;
    protected String RedoMsg;

    public TMsgTree MsgTree;

    public TUndoAgent() {
        super();
        TInputAction InputAction = new TInputAction();
        InputAction.OnSend = this;
        TInputActionManager.UndoActionRef = InputAction;
        MsgTree = new TMsgTree(TMain.BO.cTokenFleetRace, TInputActionManager.UndoActionID);
    }

    public void HandleMsg(Object sender, String msg) {
        if (UndoFlag) {
            UndoMsg = msg;
            UndoFlag = false;
        } else {
            RedoMsg = msg;
            TMain.BO.UndoManager.AddMsg(UndoMsg, RedoMsg);
        }
    }

    public boolean getUndoLock() {
        return FUndoLock || TMain.BO.isLoading();
    }

    public void setUndoLock(boolean value) {
        FUndoLock = value;
    }

}
