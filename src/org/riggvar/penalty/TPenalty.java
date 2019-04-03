package org.riggvar.penalty;

import org.riggvar.base.*;

public class TPenalty extends TBOPersistent {
    public TPenalty() {
    }

    public boolean getIsDSQPending() {
        return false;
    }

    public boolean getIsOK() {
        return true;
    }

    public boolean getIsOut() {
        return false;
    }

    protected void setIsDSQPending(boolean Value) {
    }

    public int getAsInteger() {
        return 0;
    }

    public void setAsInteger(int Value) {
    }

    public void Clear() {
    }

    public boolean Parse(String Value) {
        return false;
    }

    public boolean FromString(String Value) {
        return false;
    }

}
