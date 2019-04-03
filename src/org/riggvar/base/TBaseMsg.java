package org.riggvar.base;

public class TBaseMsg extends TReplayMsg {
    public int MsgResult;
    public int KatID;
    public String Prot;

    public boolean DispatchProt() {
        return false;
    }
}
