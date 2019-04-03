package org.riggvar.output;

import org.riggvar.base.*;
import org.riggvar.bo.*;

public class TOutput extends TBaseOutput {

    private TBO BO;

    private TOutput01 output01;
    private TOutput02 output02;

    public TOutput() {
        super();
        BO = TMain.BO;

        // defer creation of output helpers until needed
    }

    protected TOutput01 Output01() {
        if (output01 == null)
            output01 = new TOutput01();
        return output01;
    }

    protected TOutput02 Output02() {
        if (output02 == null)
            output02 = new TOutput02();
        return output02;
    }

    @Override
    public String getMsg(String sRequest) {
        XMLSection = true;
        SL.Clear();
        MsgID++;

        if (sRequest.equals(BO.cTokenOutput + "PageHeaderOn")) {
            WantPageHeader = true;
            return "PageHeader On";
        } else if (sRequest.equals(BO.cTokenOutput + "PageHeaderOff")) {
            this.WantPageHeader = true;
            return "PageHeader Off";
        }

        if (Utils.Pos(BO.cTokenOutputXML, sRequest) > 0) {
            WantPageHeader = true;
        }

        int c;
        String temp;
        c = Utils.Pos(BO.cTokenAnonymousOutput, sRequest);
        if (c == 1) {
            temp = Utils.Copy(sRequest, BO.cTokenAnonymousOutput.length() + 1, sRequest.length());
        } else {
            c = Utils.Pos(BO.cTokenOutput, sRequest);
            if (c == 1)
                temp = Utils.Copy(sRequest, BO.cTokenOutput.length() + 1, sRequest.length());
            else
                return "";
        }

        // CSV,HTM,XML
        WantHTMEscape = false;
        OutputType = TOutputType.otCSV;
        if (Utils.Pos("CSV", temp) == 1) {
            OutputType = TOutputType.otCSV;
            temp = Utils.Copy(temp, 5, temp.length());
        } else if (Utils.Pos("HTM", temp) == 1) {
            OutputType = TOutputType.otHTM;
            temp = Utils.Copy(temp, 5, temp.length());
        } else if (Utils.Pos("XML", temp) == 1) {
            OutputType = TOutputType.otXML;
            temp = Utils.Copy(temp, 5, temp.length());
        } else if (Utils.Pos("CSM", temp) == 1) {
            OutputType = TOutputType.otCSV;
            WantHTMEscape = true;
            temp = Utils.Copy(temp, 5, temp.length());
        } else if (Utils.Pos("XMM", temp) == 1) {
            OutputType = TOutputType.otXML;
            WantHTMEscape = true;
            temp = Utils.Copy(temp, 5, temp.length());
        }

        if (WantPageHeader) {
            PageHeader();
        }

        else if (Utils.Copy(temp, 1, 7).equals("Report.")) {
            if (Utils.Copy(temp, 1, 16).equals("Report.IndexData")) {
                Output01().IndexReport(SL);
            } else if (Utils.Copy(temp, 1, 15).equals("Report.CssTable")) {
                Output01().CssReport(SL);
            } else if (Utils.Copy(temp, 1, 18).equals("Report.FinishTable")) {
                Output01().FinishTable(SL);
            } else if (Utils.Copy(temp, 1, 18).equals("Report.PointsTable")) {
                Output01().PointsTable(SL);
            } else if (Utils.Copy(temp, 1, 19).equals("Report.FinishReport")) {
                Output01().FinishReport(SL);
            } else if (Utils.Copy(temp, 1, 19).equals("Report.PointsReport")) {
                Output01().PointsReport(SL);
            } else if (Utils.Copy(temp, 1, 22).equals("Report.TimePointReport")) {
                SL.Add("<TimePointReport>not implemented</TimePointReport>");
            }
        }

        else if (Utils.Copy(temp, 1, 7).equals("ASPNET.")) {
            if (Utils.Copy(temp, 1, 14).equals("ASPNET.Default"))
                BO.EventNode.getBaseRowCollection().GetXML(SL);
            else if (Utils.Copy(temp, 1, 14).equals("ASPNET.Entries")) {
                BO.EventNode.StammdatenRowCollection.GetXML(SL);
            }
        }

        else if (Utils.Copy(temp, 1, 4).equals("Web.")) {
            if (Utils.Copy(temp, 1, 9).equals("Web.Event"))
                Output02().GetMsg(SL, Utils.Copy(temp, 5, temp.length()));
        }

        if (WantPageHeader)
            PageFooter();
        if (WantHTMEscape)
            EscapeHTM();

        BO.MsgCounter++;

        return SL.getText();
    }

}
