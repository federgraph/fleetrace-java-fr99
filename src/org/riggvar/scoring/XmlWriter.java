package org.riggvar.scoring;

import java.text.*;

public class XmlWriter {
    private boolean hasAttributes;
    private String a;
    public StringBuffer sb = new StringBuffer();
    public DecimalFormat dfmt;

    public XmlWriter() {
        sb.append("<?xml version=\"1.0\" ?>");
        dfmt = new DecimalFormat("0.####");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        dfmt.setDecimalFormatSymbols(dfs);

    }

    @Override
    public String toString() {
        return sb.toString();
    }

    public void WriteStartElement(String s) {
        if (hasAttributes) {
            a = a + ">";
            sb.append(a);
            hasAttributes = false;
        }
        sb.append("\n<" + s);
        a = " ";
    }

    public void WriteEndElement(String s) {
        if (hasAttributes) {
            a = a + "/>";
            sb.append(a);
            hasAttributes = false;
        } else {
            sb.append("\n</" + s + ">");
        }
    }

    public void WriteAttributeString(String key, String value) {
        a = a + key + "=\"" + value + "\" ";
        hasAttributes = true;
    }

    public void WriteAttributeString(String key, int value) {
        WriteAttributeString(key, Integer.toString(value));
    }

    public void WriteAttributeString(String key, long value) {
        WriteAttributeString(key, Long.toString(value));
    }

    public void WriteAttributeString(String key, boolean value) {
        if (value)
            WriteAttributeString(key, "True");
        else
            WriteAttributeString(key, "False");
    }

    public void WriteAttributeString(String key, double value) {
        WriteAttributeString(key, dfmt.format(value));
    }

}
