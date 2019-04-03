package org.riggvar.base;

public class TAdapterWatches {
    protected String FMsgIn;
    public int FMsgInCount;
    protected String FMsgOut;
    public int FMsgOutCount;
    protected int FMsgOffset;

    public TAdapterWatches() {
    }

    public void Clear() {
        FMsgIn = "";
        FMsgInCount = 0;
        FMsgOut = "";
        FMsgOutCount = 0;
    }

    public void Update(int LabelID) {
    }

    public String getMsgIn() {
        return FMsgIn;
    }

    public void setMsgIn(String value) {
        FMsgIn = value;
        FMsgInCount++;
        if (FMsgInCount == -1)
            FMsgInCount = 1;
        Update(FMsgOffset + 3);

    }

    public int getMsgInCount() {
        return FMsgInCount;
    }

    public String getMsgOut() {
        return FMsgOut;
    }

    public void setMsgOut(String value) {
        FMsgOut = value;
        FMsgOutCount++;
        Update(FMsgOffset + 5);
    }

    public void Show(String EventName) {
        // virtual
    }

    public void setUndo(String value) {
        // virtual, overridden in TLocalWatches
    }

    public void setRedo(String value) {
        // virtual, overridden in TLocalWatches
    }

}
