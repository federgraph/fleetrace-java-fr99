package org.riggvar.base;

public class TLineParser {

    // SL = helper object, Commatext used to parse line when loading
    private TStringList SL = new TStringList();

    protected boolean ParseKeyValue(String Key, String Value) {
        // virtual, this version only used in unit test
        return Key.equals("Key") && Value.equals("Value");
    }

    public boolean ParseLine(String s) {
        String temp;

        SL.Clear();
        int i = Utils.Pos("=", s);
        if (i > 0) {
            temp = Utils.Copy(s, 1, i - 1).trim();
            temp += "=";
            temp += Utils.Copy(s, i + 1, s.length()).trim();
        } else {
            temp = s.trim();
            temp = temp.replace(' ', '_');
            // temp = StringReplace(Trim(s), ' ', '_', [rfReplaceAll]);
        }

        if (Utils.Pos("=", temp) == 0) {
            temp = temp + "=";
        }
        SL.Add(temp);
        String sK = SL.getName(0);
        String sV = SL.getValue(sK);
        // StringReplace(sV, '_', ' ', [rfReplaceAll]);
        return ParseKeyValue(sK, sV);
    }

}
