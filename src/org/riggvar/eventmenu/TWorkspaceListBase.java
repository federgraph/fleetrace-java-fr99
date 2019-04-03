package org.riggvar.eventmenu;

import org.riggvar.base.TStringList;
import org.riggvar.base.TStrings;

public class TWorkspaceListBase {

    public enum UrlScheme {
        Http, File, App
    };

    public class TWorkspaceUrl {
        public String Value;

        public UrlScheme GetScheme() {
            if (isHttpScheme())
                return UrlScheme.Http;
            if (isAppScheme())
                return UrlScheme.App;
            return UrlScheme.File;
        }

        public boolean isAppScheme() {
            return Value.startsWith("app");
        }

        public boolean isHttpScheme() {
            return Value.startsWith("http");
        }
    }

    public String u0 = "app://DocManager/EventMenu.xml";

    protected static String u1 = "http://www.fleetrace.org/EventMenu.txt";
    protected static String u2 = "http://data.riggvar.de/EventMenu.txt";

    protected static String u3 = "data.riggvar.de/xml=http://data.riggvar.de/EventMenu.xml";
    protected static String u4 = "www.riggvar.de/xml=http://www.riggvar.de/results/EventMenu.xml";
    protected static String u5 = "www.fleetrace.org/demo=http://www.fleetrace.org/DemoIndex.xml";
    protected static String u6 = "www.riggvar.de/html=http://www.riggvar.de/results/EventMenuHtml.xml";

    protected TStringList TL;
    protected TStringList VL;

    public TWorkspaceListBase() {
        TL = new TStringList();
        VL = new TStringList();
    }

    public void Init(boolean FromMemo) {
        if (VL.getCount() == 0) {
            LoadDefault();
            VL.setText(TL.getText());
        }
    }

    public void LoadDefault() {
        TL.Clear();

        boolean WantXml = true; // CheckWin32Version(5,1);

        if (WantXml) {
            AddUrl(u3);
            AddUrl(u4);
            AddUrl(u5);
            AddUrl(u6);
        } else {
            AddUrl(u1);
            AddUrl(u2);
        }
    }

    protected void AddUrl(String s) {
        int i;
        String u;

        i = s.indexOf("="); // , StringComparison.OrdinalIgnoreCase);
        if (i > 0)
            u = s.substring(i + 1).trim(); // Trim(Copy(s, i+1, Length(s)))
        else
            u = s.trim();
        TL.Add(u);
    }

    public void Load() {

    }

    public void Save() {

    }

    public String GetName(int i) {
        return "";
    }

    public String GetUrl(int i) {
        if (i >= 0 && i < VL.getCount())
            return VL.getString(i);
        return "";
    }

    public String GetTemp() {
        return TL.getText();
    }

    public String GetText() {
        return VL.getText();
    }

    public void SetText(String value) {
        // not implemented
    }

    public boolean IsWritable(int i) {
        return false;
    }

    public void FillCombo(TStrings Combo) {
        Combo.Clear();
        for (int i = 0; i < VL.getCount(); i++)
            Combo.Add(VL.getString(i));
    }

}
