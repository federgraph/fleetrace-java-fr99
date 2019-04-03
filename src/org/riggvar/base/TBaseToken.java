package org.riggvar.base;

public abstract class TBaseToken {
    public static int NewActionID = 0; // used only during construction of MsgTree
    public static int MsgID = 0;
    public static boolean UseLongNames = false;

    private Integer FActionID;
    protected TBaseToken Owner;
    private String FNameID;
    int TokenID = -1;

    public TBaseToken() {
        FActionID = NewActionID;
    }

    public TBaseToken(TBaseToken aOwner, String aNameID) {
        Owner = aOwner;
        FNameID = aNameID;
        FActionID = NewActionID;
    }

    public Integer getActionID() {
        return FActionID;
    }

    public String NameID() {
        if (TokenID > -1) {
            if (UseLongNames) {
                return TBaseMsgParser.FRLongToken(FNameID) + TokenID;
            } else {
                return FNameID + TokenID;
            }
        } else {
            if (UseLongNames) {
                return TBaseMsgParser.FRLongToken(FNameID);
            } else {
                return FNameID;
            }
        }
    }

    public void setNameID(String value) {
        FNameID = value;
    }

    public String NamePath() {
        String result = NameID();
        if (Owner != null) {
            String s = Owner.NamePath();
            if (!s.equals("")) {
                result = s + "." + NameID();
            }
        }
        return result;
    }
}
