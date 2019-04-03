package org.riggvar.base;

import java.util.Properties;

public class TIniFile {

    Properties p;
    String s;
    int i;

    public TIniFile(Properties ap) {
        p = ap;
    }

    public boolean ReadBool(String section, String prop, boolean def) {
        s = p.getProperty(prop, Utils.BoolStr(def));
        return Utils.IsTrue(s);
    }

    public String ReadString(String section, String prop, String def) {
        s = p.getProperty(prop, def);
        return s;
    }

    public int ReadInteger(String section, String prop, int def) {
        s = p.getProperty(prop, "" + def);
        return Utils.StrToIntDef(s, def);
    }

    public void WriteBool(String section, String prop, boolean val) {
        p.setProperty(prop, Utils.BoolStr(val));
    }

    public void WriteString(String section, String prop, String val) {
        p.setProperty(prop, val);
    }

    public void WriteInteger(String section, String prop, int val) {
        p.setProperty(prop, "" + val);
    }
}
