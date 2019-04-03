package org.riggvar.base;

public class TBaseOutput extends TOutputType implements IOutput {
    public int OutputType;
    public TStrings SL = new TStringList();
    public int MsgID;
    public boolean XMLSection;

    public boolean WantHTMEscape;
    public boolean WantPageHeader;

    public TBaseOutput() {
    }

    protected void PageHeader() {
        switch (OutputType) {
        case otCSV:
            break;
        case otHTM:
            SL.Add("<HTML>");
            SL.Add("<HEAD>");
            SL.Add("<TITLE>FR Output</TITLE>");
            SL.Add("<style><!-- pre {color=\"maroon\"} --> </style>");
            SL.Add("</HEAD>");
            SL.Add("<BODY>");
            break;
        case otXML:

            // SL.Add("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
            SL.Add("<?xml version=\"1.0\" ?>"); // standalone=\"yes\"
            break;
        }
    }

    protected void PageFooter() {
        switch (OutputType) {
        case otCSV:
            break;
        case otHTM:
            SL.Add("</BODY>");
            SL.Add("</HTML>");
            break;
        case otXML:
            break;
        }
    }

    protected void SectionHeader(String s) {
        switch (OutputType) {
        case otCSV:
            break;
        case otHTM:
            SL.Add("");
            SL.Add("#" + s);
            SL.Add("");
            break;
        case otXML:
            break;
        }
    }

    protected void EscapeHTM() {
        for (int i = 0; i < SL.getCount(); i++) {
            // SL[i] = System.Web.HttpUtility.HtmlEncode(SL[i]);
        }
        SL.Insert(0, "<pre>");
        SL.Add("</pre>");
    }

    public String getMsg(String sRequest) {
        return "";
    }

    public String getAll(TStrings OutputRequestList) {
        String sRequest;
        TStringList sl = new TStringList();
        // SL.Add("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
        // SL.Add("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
        sl.Add("<?xml version=\"1.0\" ?>");
        sl.Add("<e>");
        for (int i = 0; i < OutputRequestList.getCount(); i++) {
            sRequest = OutputRequestList.getString(i);
            sl.Add("<answer request=\"" + sRequest + "\">");
            sl.Add("<![CDATA[");
            sl.Add(getMsg(sRequest));
            sl.Add("]]>");
            sl.Add("</answer>");
            sl.Add("");
        }
        sl.Add("</e>");
        return sl.getText();
    }

}
