package org.riggvar.base;

public class TInputActionDelegate implements IMsgEvent {
    private IMsgEvent target;

    public TInputActionDelegate(IMsgEvent aTarget) {
        target = aTarget;
    }

    public void HandleMsg(Object sender, String msg) {
        target.HandleMsg(sender, msg);
    }
}
