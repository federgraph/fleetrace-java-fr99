package org.riggvar.base;

public class TBaseMsgTree extends TBaseToken {
    public boolean UseMsgID = false;
    public boolean UsePrefix = true;

    public TBaseMsgTree(String aNameID) {
        super(null, aNameID);
    }

    @Override
    public String NamePath() {
        String result = "";
        if (UseMsgID) {
            if (Owner != null) {
                result = Owner.NamePath() + "." + NameID();
            } else {
                result = NameID() + ".Msg" + Utils.IntToStr(MsgID);
            }
        } else if (UsePrefix) {
            result = super.NamePath();
        }
        return result;
    }

    public boolean LongNames;

}
