package org.riggvar.base;

public interface IConnection {
    public int GetPort();

    public void InjectMsg(String s);

    public String HandleMsg(String s);

    public void SendMsg(TContextMsg cm);

    public void SetOnSendMsg(IHandleContextMsg Value);

    public void Delete();
}
