package org.riggvar.base;

public interface IServer {
    public int Port();

    public void SendMsg(int KatID, TContextMsg cm);

    public void Reply(Object Connection, String s);

    public IConnection Connect();

    public int Status();

    public int ConnectionCount();

    public void Dispose();

    public void setOnHandleMsg(IInjectMsgEvent MsgEvent);
}
