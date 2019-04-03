package org.riggvar.web;

public class WebUtils {

    public static int StrToIntDef(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    public static String IntToStr(int i) {
        Integer result = Integer.valueOf(i);
        return result.toString();
    }

    public static String BoolStr(boolean b) {
        if (b == false) {
            return "False";
        } else {
            return "True";
        }
    }

}
