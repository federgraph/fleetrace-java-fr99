package org.riggvar.namefields;

import org.riggvar.stammdaten.*;

public class NameFieldMap {
    private int[] fieldIndex = new int[JS_NameFieldMax];

    private static NameFieldMap instance;

    public static NameFieldMap Instance() {
        if (instance == null)
            instance = new NameFieldMap();
        return instance;
    }

    public static final int JS_SkipperF = 1;
    public static final int JS_SkipperL = 2;
    public static final int JS_SkipperID = 3;
    public static final int JS_Club = 4;
    public static final int JS_CBYRA = 5;
    public static final int JS_USSA = 6;
    public static final int JS_Crew1F = 7;
    public static final int JS_Crew1L = 8;
    public static final int JS_Crew1ID = 9;
    public static final int JS_Crew2F = 10;
    public static final int JS_Crew2L = 11;
    public static final int JS_Crew2ID = 12;

    public static final int JS_NameFieldMax = 12;

    public String FieldName(int FieldID) {
        switch (FieldID) {
        case JS_SkipperF:
            return "Skipper First";
        case JS_SkipperL:
            return "Skipper Last";
        case JS_SkipperID:
            return "Skipper ID";

        case JS_Crew1F:
            return "Crew 1 First";
        case JS_Crew1L:
            return "Crew 1 Last";
        case JS_Crew1ID:
            return "Crew 1 ID";

        case JS_Crew2F:
            return "Crew 2 First";
        case JS_Crew2L:
            return "Crew 2 Last";
        case JS_Crew2ID:
            return "Crew 2 ID";

        case JS_Club:
            return "Club";
        case JS_CBYRA:
            return "CBYRA";
        case JS_USSA:
            return "USSA";
        default:
            return "";
        }
    }

    public String FieldValue(int FieldID, TStammdatenRowCollectionItem cr) {
        if (FieldID > 0 && FieldID <= JS_NameFieldMax && cr != null) {
            int m = fieldIndex[FieldID - 1];
            return cr.getFieldValue(m);
        }
        return "";
    }

    public int getFieldIndex(int FieldID) {
        if (FieldID > 0 && FieldID <= JS_NameFieldMax) {
            return fieldIndex[FieldID - 1];
        }
        return 0;
    }

    public void setFieldIndex(int FieldID, int value) {
        if (FieldID > 0 && FieldID <= JS_NameFieldMax) {
            fieldIndex[FieldID - 1] = value;
        }
    }

}
