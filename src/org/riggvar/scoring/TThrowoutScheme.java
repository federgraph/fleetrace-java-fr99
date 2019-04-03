package org.riggvar.scoring;

public class TThrowoutScheme {
    public static final int throwoutDefault = 0;
    public static final int throwoutBYNUMRACES = 1;
    public static final int throwoutPERXRACES = 2;
    public static final int throwoutBESTXRACES = 3;
    public static final int throwoutNONE = 4;

    public static String getString(int e) {
        switch (e) {
        case throwoutDefault:
            return "ByNumRaces";
        case throwoutBYNUMRACES:
            return "ByNumRaces";
        case throwoutPERXRACES:
            return "PerXRaces";
        case throwoutBESTXRACES:
            return "BestXRaces";
        case throwoutNONE:
            return "None";
        }
        return "";
    }

    public static int ParseDef(String value, int def) {
        if (value.equals("ByNumRaces")) {
            return TThrowoutScheme.throwoutBYNUMRACES;
        } else if (value.equals("ByBestXRaces")) {
            return TThrowoutScheme.throwoutBESTXRACES;
        } else if (value.equals("PerXRaces")) {
            return TThrowoutScheme.throwoutPERXRACES;
        } else {
            return def;
        }
    }
}
