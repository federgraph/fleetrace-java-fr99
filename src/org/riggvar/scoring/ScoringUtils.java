package org.riggvar.scoring;

public class ScoringUtils {

    public static String BoolStr(boolean b) {
        if (b == false) {
            return "False";
        } else {
            return "True";
        }
    }

    public static int StrToInt(String s) {
        return Integer.parseInt(s);
    }

    public static int StrToIntDef(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    public static String FloatToStr(double d, String format) {
        java.text.DecimalFormat f = new java.text.DecimalFormat("0.00");
        return f.format(d);
    }

    public static double StrToFloatDef(String s, double def) {
        try {
            return Double.parseDouble(s.replace(',', '.'));
        } catch (Exception ex) {
            return def;
        }
    }

    public static String Format(int i, String format) {
        // ToDo: implement Format!
        return IntToStr(i);
    }

    public static int ToInt32(double d) {
        Double D = d;
        return D.intValue();
    }

    public static String FloatToStr(double d) {
        return FloatToStr(d, "");
    }

    public static String IntToStr(int i) {
        Integer result = i;
        return result.toString();
    }

    public static String CopyRest(String s, int startpos) {
        return Copy(s, startpos, s.length());
    }

    public static String Copy(String s, int startpos, int len) {
        try {
            // this function is One-based! as in Delphi-VCL-Copy-RTL-Funktion
            if (len < 1) {
                return "";
            }
            if (startpos < 1) {
                startpos = 1;

            }
            int l = s.length();

            if (startpos > l) {
                return "";
            } else if (startpos - 1 + len >= l) {
                String result = s.substring(startpos - 1);
                return result;
            } else {
                String result = s.substring(startpos - 1, startpos - 1 + len);
                return result;
            }
        } catch (Exception ex) // System.ArgumentOutOfRangeException
        {
            return "";
        }
    }

    public static int Pos(String subs, String s) {
        return s.indexOf(subs) + 1;
    }

    public static boolean IsTrue(String Value) {
        boolean result = false;
        if (Value.equalsIgnoreCase("TRUE") || Value.equalsIgnoreCase("T")) {
            result = true;
        }
        return result;
    }

    public static String PadLeft(String s, int Count, char c) {
        StringBuffer sb = new StringBuffer(Count);
        for (int i = 0; i < Count - s.length(); i++) {
            sb.append(c);
        }
        sb.append(s);
        return sb.toString();
    }
}
