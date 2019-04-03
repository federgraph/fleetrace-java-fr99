package org.riggvar.stammdaten;

public class FieldNames {

    // NameSchema_N0
    public static String N0_FN = "FN";
    public static String N0_LN = "LN";
    public static String N0_SN = "SN";
    public static String N0_NC = "NC";
    public static String N0_GR = "GR";
    public static String N0_PB = "PB";

    // NameSchema_N1
    public static String N1_FN = "FirstName";
    public static String N1_LN = "LastName";
    public static String N1_SN = "ShortName";
    public static String N1_NC = "NOC";
    public static String N1_GR = "Gender";
    public static String N1_PB = "PersonalBest";

    // NameSchema_N2
    public static String N2_FN = "N1";
    public static String N2_LN = "N2";
    public static String N2_SN = "N3";
    public static String N2_NC = "N4";
    public static String N2_GR = "N5";
    public static String N2_PB = "N6";

    // actual mapping
    public static String FN = N0_FN;
    public static String LN = N0_LN;
    public static String SN = N0_SN;
    public static String NC = N0_NC;
    public static String GR = N0_GR;
    public static String PB = N0_PB;

    public static int nameSchema = 0;

    public static int getSchemaCode() {
        return nameSchema;
    }

    public static void setSchemaCode(int value) {
        switch (value) {
        case 0:
            nameSchema = 0;
            FN = FieldNames.N0_FN;
            LN = FieldNames.N0_LN;
            SN = FieldNames.N0_SN;
            NC = FieldNames.N0_NC;
            GR = FieldNames.N0_GR;
            PB = FieldNames.N0_PB;
            break;
        case 1:
            nameSchema = 1;
            FN = FieldNames.N1_FN;
            LN = FieldNames.N1_LN;
            SN = FieldNames.N1_SN;
            NC = FieldNames.N1_NC;
            GR = FieldNames.N1_GR;
            PB = FieldNames.N1_PB;
            break;
        case 2:
            nameSchema = 2;
            FN = FieldNames.N2_FN;
            LN = FieldNames.N2_LN;
            SN = FieldNames.N2_SN;
            NC = FieldNames.N2_NC;
            GR = FieldNames.N2_GR;
            PB = FieldNames.N2_PB;
            break;
        }
    }

    public static String GetStandardFieldCaption(int index, int NameSchema) {
        String result = "";

        if (index == 0)
            result = "SNR";

        switch (NameSchema) {
        case 0:
            switch (index) {
            case 1:
                result = N0_FN;
                break;
            case 2:
                result = N0_LN;
                break;
            case 3:
                result = N0_SN;
                break;
            case 4:
                result = N0_NC;
                break;
            case 5:
                result = N0_GR;
                break;
            case 6:
                result = N0_PB;
                break;
            }
            break;

        case 1:
            switch (index) {
            case 1:
                result = N1_FN;
                break;
            case 2:
                result = N1_LN;
                break;
            case 3:
                result = N1_SN;
                break;
            case 4:
                result = N1_NC;
                break;
            case 5:
                result = N1_GR;
                break;
            case 6:
                result = N1_PB;
                break;
            }
            break;

        case 2:
            switch (index) {
            case 1:
                result = N2_FN;
                break;
            case 2:
                result = N2_LN;
                break;
            case 3:
                result = N2_SN;
                break;
            case 4:
                result = N2_NC;
                break;
            case 5:
                result = N2_GR;
                break;
            case 6:
                result = N2_PB;
                break;
            }
            break;
        }

        if (result.equals(""))
            result = "N" + index;
        return result;
    }

}
