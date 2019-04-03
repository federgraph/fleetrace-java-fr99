package org.riggvar.web;

public class XmlGenerator {
    public static String GetXml(String EventData) {
        return "eventxml not implemented";
    }

    public static String HtmlEncode(String s) {
        char[] ch = s.toCharArray();
        int start = 0;
        int length = ch.length;
        char[] dest = new char[length * 8];
        int newlen = escape(ch, start, length, dest);
        StringBuffer sb = new StringBuffer();
        sb.append(dest, 0, newlen);
        return sb.toString();
    }

    private static int escape(char ch[], int start, int length, char[] out) {
        int o = 0;

        for (int i = start; i < start + length; i++) {
            if (ch[i] == '<') {
                ("&lt;").getChars(0, 4, out, o);
                o += 4;
            } else if (ch[i] == '>') {
                ("&gt;").getChars(0, 4, out, o);
                o += 4;
            } else if (ch[i] == '&') {
                ("&amp;").getChars(0, 5, out, o);
                o += 5;
            } else if (ch[i] == '\"') {
                ("&#34;").getChars(0, 5, out, o);
                o += 5;
            } else if (ch[i] == '\'') {
                ("&#39;").getChars(0, 5, out, o);
                o += 5;
            } else if (ch[i] < 127) {
                out[o++] = ch[i];
            } else {
                // output character reference
                out[o++] = '&';
                out[o++] = '#';
                String code = Integer.toString(ch[i]);
                int len = code.length();
                code.getChars(0, len, out, o);
                o += len;
                out[o++] = ';';
            }
        }

        return o;
    }

}
