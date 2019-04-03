package org.riggvar.scoring;

public class FR50 {
    public static void main(String[] args) {
        String fn = "D:\\RiggVar\\Delphi\\FRResult\\Regatta X.xml";
        TFRProxy p = new TFRProxy();
        p.ReadFromFile(fn);
        XmlWriter xw = new XmlWriter();
        p.WriteXml(xw);
        p = null;
        System.out.println(xw.toString());
        xw = null;
    }
}
