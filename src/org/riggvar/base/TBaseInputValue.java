package org.riggvar.base;

public class TBaseInputValue extends TBaseToken {
    public TBaseInputValue() {
        super();
    }

    public TBaseInputValue(TBaseToken aOwner, String aNameID) {
        super(aOwner, aNameID);
    }

    public void SendMsg(boolean IsValid, String aCommand, String aValue) {
        String sKey;
        if (IsValid) {
            MsgID++;
            sKey = "";
        } else {
            sKey = "//";

        }
        sKey += NamePath() + "." + aCommand;
        if (getInputAction() != null)
            getInputAction().Send(sKey, aValue);

    }

    protected boolean IsPositiveInteger(String s) {
        return true;
    }

    public TInputAction getInputAction() {
        if (getActionID() == 0)
            return TInputActionManager.DynamicActionRef;
        else
            return TInputActionManager.UndoActionRef;
    }

}
