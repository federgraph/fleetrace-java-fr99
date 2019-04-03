package org.riggvar.conn;

import org.riggvar.base.*;
import org.riggvar.bo.TMain;

public class TConnection extends TCollectionItem<TConnections, TConnection> implements IConnection {
    private String FAnswer;
    private IHandleContextMsg OnSendMsg;

    public TConnection() {
        super();
    }

    public int GetPort() {
        return this.getID();
    }

    public void InjectMsg(String s) {
        getServer().setMsg(this, s);
    }

    public String HandleMsg(String s) {
        FAnswer = "";
        getServer().setMsg(this, s);
        if (!isOutput())
            TMain.BO.OnIdle(); // --> calculate, process queue --> FAnswer
        return FAnswer;
    }

    public void HandleContextMsg(TContextMsg cm) {
        getServer().HandleMsg(cm);
    }

    public void SendMsg(TContextMsg cm) {
        if (isOutput() && (OnSendMsg != null)) {
            OnSendMsg.HandleMsg(cm);
        }
    }

    public void SetAnswer(String s) {
        FAnswer = s;
    }

    public TServerIntern getServer() {
        return Collection.Server;
    }

    public int getPort() {
        return this.getID();
    }

    public boolean isOutput() {
        return getServer().getIsOutput();
    }

    public void SetOnSendMsg(IHandleContextMsg Value) {
        OnSendMsg = Value;
    }
}

//public class TConnection extends TBOCollectionItem implements IConnection
//{
//    private String FAnswer;
//    private IHandleContextMsg OnSendMsg;
//
//    public TConnection(TBOCollection aCollection)
//    {
//        super(aCollection);
//    }
//
//    public int GetPort()
//    {
//        return this.getId();
//    }
//
//    public void InjectMsg(String s)
//    {
//        getServer().setMsg(this, s);
//    }
//
//    public String HandleMsg(String s)
//    {
//        FAnswer = "";
//        getServer().setMsg(this, s);
//        if (!isOutput())
//        	TMain.BO.OnIdle(); //--> calculate, process queue --> FAnswer
//        return FAnswer;
//    }
//
//    public void HandleContextMsg(TContextMsg cm)
//    {
//    	getServer().HandleMsg(cm);
//    }
//    
//    public void SendMsg(TContextMsg cm)
//    {
//        if (isOutput() && (OnSendMsg != null))
//        {
//            OnSendMsg.HandleMsg(cm);
//        }
//    }
//
//    public void Dispose() 
//    {
//        this.getCollection().Delete(this.getIndex());
//    }
//    
//    public void SetAnswer(String s)
//    {
//        FAnswer = s;
//    }
//
//    public TServerIntern getServer()
//    {
//        return ( (TConnections) getCollection()).Server;
//    }
//
//    public int getPort()
//    {
//        return this.getId();
//    }
//
//    public boolean isOutput()
//    {
//        return getServer().getIsOutput();
//    }
//
//    public void SetOnSendMsg(IHandleContextMsg Value)
//    {
//        OnSendMsg = Value;
//    }
//}
