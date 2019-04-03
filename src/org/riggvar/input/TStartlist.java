package org.riggvar.input;

import org.riggvar.base.*;

public class TStartlist extends TInputValue {
    private TTokenList FPosStore;

    public TStartlist(TBaseToken aOwner, String aNameID) {
        super(aOwner, aNameID);
        FPosStore = new TTokenList(this, "Pos", new TPos());
    }

    public TPos Pos(int index) {
        return (TPos) FPosStore.Token(index);
    }

    public void Count(String Value) {
        SendMsg(IsPositiveInteger(Value), "Count", Value);
    }
}
