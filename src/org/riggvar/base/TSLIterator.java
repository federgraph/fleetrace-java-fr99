package org.riggvar.base;

public class TSLIterator {
    private int i = -1;
    private int c;
    private TStrings SL;

    public TSLIterator(TStrings aSL) {
        SL = aSL;
        c = SL.getCount();
    }

    public int NextI() {
        i++;
        if (i < c) {
            return Utils.StrToIntDef(SL.getString(i), -1);
        } else {
            return -1;
        }
    }

    public String NextS() {
        i++;
        if (i < c) {
            return SL.getString(i);
        } else {
            return "";
        }
    }

    public char NextC() {
        i++;
        if ((i < c) && (SL.getString(i).length() == 1)) {
            return SL.getString(i).charAt(0);
        } else {
            return TBaseEntry.SpaceChar;
        }
    }
}
