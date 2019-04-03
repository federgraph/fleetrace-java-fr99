package org.riggvar.input;

import org.riggvar.base.*;

public class TInputValue extends TBaseInputValue {
    public TInputValue() {
        super();
    }

    public TInputValue(TBaseToken aOwner, String aNameID) {
        super(aOwner, aNameID);
    }

    protected boolean IsValidBoolean(String s) {
        return true;
    }

    protected boolean IsValidCount(String s) {
        return true;
    }

    protected boolean IsValidTime(String s) {
        return true;
    }

    protected boolean IsValidStatus(String s) {
        return true;

//        boolean b = false;
//        if (s.equals("ok"))
//        {
//            b = true;
//        }
//        else if (s.equals("dnf"))
//        {
//            b = true;
//        }
//        else if (s.equals("dsq"))
//        {
//            b = true;
//        }
//        else if (s.equals("dns"))
//        {
//            b = true;
//        }
//        else if (s.equals("*"))
//        {
//            b = true;
//        }
//        return b;
    }

    protected boolean IsValidBib(String s) {
        return true;
    }

    protected boolean IsValidCourse(String s) {
        return true;
    }

    protected boolean IsValidSNR(String s) {
        return true;
    }

    protected boolean IsValidNC(String s) {
        return true;
    }

    protected boolean IsValidName(String s) {
        return true;
    }

    protected boolean IsValidDSQGate(String s) {
        return true;
    }

    protected boolean IsValidGR(String s) {
        return true;
    }

    protected boolean IsValidPB(String s) {
        return true;
    }

    protected boolean IsValidRace(String s) {
        if (s != null)
            return (s.length() < 13);
        else
            return false;
    }

    protected boolean IsValidRadius(String s) {
        return true;
    }

    protected boolean IsValidKoord(String s) {
        return true;
    }

    protected boolean IsValidProp(String Key, String Value) {
        return true;
    }
}
