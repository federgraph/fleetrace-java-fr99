package org.riggvar.base;

public class TInputAction {
    public IMsgEvent OnSend = null;

    public TInputAction() {
    }

    public void Send(String sKey, String sValue) {
        if (OnSend != null) {
            OnSend.HandleMsg(this, sKey + "=" + sValue);
        }
    }

}
