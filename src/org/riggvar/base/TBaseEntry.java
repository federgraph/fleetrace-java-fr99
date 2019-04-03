package org.riggvar.base;

public class TBaseEntry extends TBOPersistent {
    public static char SpaceChar = ' ';
    public static char EmptyMark = '-';

    protected String sep = ";";

    public int SO;
    public String sOutput;

    public TBaseEntry() {
    }

    private boolean Need_CSV_Escape(String aTagValue) {
        return aTagValue.contains(" ") || (sep.equals(",") && aTagValue.contains(sep));
    }

    private void SLADD_CSV_LAST(String aTagName, String aTagValue) {
        if (aTagValue.equals(""))
            sOutput = sOutput + aTagValue;
        else if (Need_CSV_Escape(aTagValue))
            sOutput = sOutput + "\"" + aTagValue + "\"";
        else
            sOutput = sOutput + aTagValue;
    }

    private void SLADD_CSV(String aTagName, String aTagValue) {
        if (aTagValue.equals(""))
            sOutput = sOutput + aTagValue + sep;
        else if (Need_CSV_Escape(aTagValue))
            sOutput = sOutput + "\"" + aTagValue + "\"" + sep;
        else
            sOutput = sOutput + aTagValue + sep;
    }

    private void SLADD_CSV_Header_LAST(String aTagName, String aTagValue) {
        sOutput = sOutput + aTagName;
    }

    private void SLADD_CSV_Header(String aTagName, String aTagValue) {
        sOutput = sOutput + aTagName + sep;
    }

    private void SLADD_HTM(String aTagName, String aTagValue) {
        if (aTagValue == null || aTagValue.equals("")) {
            aTagValue = "&nbsp";
        }
        sOutput = sOutput + "<td>" + aTagValue + "</td>";
    }

    private void SLADD_HTM_Header(String aTagName, String aTagValue) {
        if (aTagValue == null || aTagValue.equals("")) {
            aTagValue = "&nbsp";
        }
        sOutput = sOutput + "<th>" + aTagName + "</th>";
    }

    private void SLADD_XML(String aTagName, String aTagValue) {
        sOutput = sOutput + aTagName + "=\"" + aTagValue + "\" ";
    }

    protected void SLADD(String aTagName, String aTagValue) {
        switch (OutputType) {
        case TOutputType.otCSV:
            SLADD_CSV(aTagName, aTagValue);
            break;
        case TOutputType.otHTM:
            SLADD_HTM(aTagName, aTagValue);
            break;
        case TOutputType.otXML:
            SLADD_XML(aTagName, aTagValue);
            break;
        case TOutputType.otCSV_Header:
            SLADD_CSV_Header(aTagName, aTagValue);
            break;
        case TOutputType.otHTM_Header:
            SLADD_HTM_Header(aTagName, aTagValue);
            break;
        default:
            break;
        }
    }

    protected void SLADDLAST(String aTagName, String aTagValue) {
        switch (OutputType) {
        case TOutputType.otCSV:
            SLADD_CSV_LAST(aTagName, aTagValue);
            break;
        case TOutputType.otHTM:
            SLADD_HTM(aTagName, aTagValue);
            break;
        case TOutputType.otXML:
            SLADD_XML(aTagName, aTagValue);
            break;
        case TOutputType.otCSV_Header:
            SLADD_CSV_Header_LAST(aTagName, aTagValue);
            break;
        case TOutputType.otHTM_Header:
            SLADD_HTM_Header(aTagName, aTagValue);
            break;
        default:
            break;
        }
    }

    protected void GetOutput() {
        // overrides should make calls to SLADD here
    }

    public void GetFooter(TStrings SL, int ot, String aName, boolean XMLSection) {
        switch (ot) {
        case TOutputType.otCSV:
            break;
        case TOutputType.otHTM:
            SL.Add("</table>");
            break;
        case TOutputType.otXML:
            if (XMLSection) {
                SL.Add("</" + aName + ">");
            }
            break;
        default:
            break;
        }
    }

    public void GetHeader(TStrings SL, int ot, String aName, boolean XMLSection) {
        switch (ot) {
        case TOutputType.otCSV:
            SL.Add(GetCSV_Header());
            break;
        case TOutputType.otHTM:

            // SL.Add("<H3>" + aName + "</H3>");
            SL.Add("<table border=\"1\" cellspacing=\"0\" cellpadding=\"1\">");
            SL.Add("<caption>" + aName + "</caption>");
            SL.Add(GetHTM_Header());
            break;
        case TOutputType.otXML:
            if (XMLSection) {
                SL.Add("<" + aName + ">");
                // SL.Add("<" + aName + " xmlns=\"http://tempuri.org/" + aName + ".xsd\">");
            }
            break;
        }
    }

    public String GetCommaText(TStrings SL) {
        return "";
    }

    public void SetCommaText(TStrings SL) {
    }

    public String GetCSV() {
        OutputType = TOutputType.otCSV;
        sOutput = "";
        GetOutput();
        return sOutput;
    }

    public String GetHTM() {
        OutputType = TOutputType.otHTM;
        sOutput = "<tr>";
        GetOutput();
        sOutput = sOutput + "</tr>";
        return sOutput;
    }

    public String GetXML(String aTagName) {
        OutputType = TOutputType.otXML;
        sOutput = "";
        GetOutput();
        return "<" + aTagName + " " + sOutput + "/>";
    }

    public String GetCSV_Header() {
        return "";
    }

    public String GetHTM_Header() {
        OutputType = TOutputType.otHTM_Header;
        sOutput = "<tr align=\"left\">";
        GetOutput();
        sOutput = sOutput + "</tr>";
        return sOutput;
    }

    public int OutputType;

    public INewIDFunction NewID;
}
