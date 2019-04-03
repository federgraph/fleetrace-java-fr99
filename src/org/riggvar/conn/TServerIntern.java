package org.riggvar.conn;

import org.riggvar.base.*;

public class TServerIntern extends TBaseServer {
    private TConnections FConnections;

    public TServerIntern(int aPort, TServerFunction aServerFunction) {
        super(aPort, aServerFunction);

        if (aServerFunction == TServerFunction.Input)
            MsgSource = TMsgSource.InternalInput;
        else
            MsgSource = TMsgSource.InternalOutput;

        FConnections = new TConnections();
        FConnections.Server = this;
        this.setActive(true);
    }

    @Override
    public void SendMsg(int KatID, TContextMsg cm) {
        for (int i = 0; i < FConnections.size(); i++) {
            FConnections.getItem(i).SendMsg(cm);
        }
    }

    @Override
    public void Reply(Object Connection, String s) {
        for (int i = 0; i < FConnections.size(); i++) {
            TConnection so = FConnections.getItem(i);
            if (so == Connection) {
                so.SetAnswer(s); // selective
            }
        }
    }

    @Override
    public IConnection Connect() {
        return Connections().AddRow();
    }

    public TConnections Connections() {
        return FConnections;
    }

    @Override
    public int ConnectionCount() {
        return FConnections.size();
    }

    public void HandleMsg(TContextMsg cm) {
    }

}
