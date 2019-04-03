package org.riggvar.base;

/**
 * Base class for TInputServer and TOutputServer
 */
public class TBaseNCP implements IInjectMsgEvent {
    public IServer Server;

    public TBaseNCP(IServer ts) {
        Server = ts;
        Server.setOnHandleMsg(this);
    }

    public void InjectMsg(Object sender, TMsgSource ms, String s) {
        // virtual
    }

    protected void HandleMsg(TContextMsg cm) {
        // virtual;
    }

    public void ProcessQueue() {
        // virtual;
    }

}
