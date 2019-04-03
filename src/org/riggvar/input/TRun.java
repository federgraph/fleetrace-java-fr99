package org.riggvar.input;

import org.riggvar.base.*;

public class TRun extends TInputValue {
    private TTokenList FBibStore;

    public TRun() {
        super();
        FBibStore = new TTokenList(this, "Bib", new TBib());
    }

    public TRun(TBaseToken aOwner, String aNameID) {
        super(aOwner, aNameID);
        FBibStore = new TTokenList(this, "Bib", new TBib());
    }

    public void IsRacing(String Value) {
        SendMsg(IsValidBoolean(Value), "IsRacing", Value);
    }

    public TBib Bib(int index) {
        return (TBib) FBibStore.Token(index);
    }

}
