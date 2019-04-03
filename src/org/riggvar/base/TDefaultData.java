package org.riggvar.base;

public class TDefaultData {
    private StringBuffer sb = new StringBuffer();
    private String crlf = "\r\n";

    public TDefaultData() {
    }

    private void a(String s) {
        sb.append(s);
        sb.append(crlf);
    }

    public String FR() {
        sb = new StringBuffer();
        // a("FR.420.Request.XML.ASPNET.Default");
        // a("");
        a("#Params");
        a("");
        a("DP.StartlistCount = 8");
        a("DP.ITCount = 2");
        a("DP.RaceCount = 2");
        a("");
        a("#Event Properties");
        a("");
        a("EP.Name = Test-Regatta");
        a("EP.Dates = 1.09.2003 - 2.09.2003");
        a("EP.HostClub = Segelverein Y");
        a("EP.PRO");
        a("EP.JuryHead");
        a("EP.ScoringSystem = Low Point System");
        a("EP.Throwouts = 1");
        a("EP.ThrowoutScheme = ByNumRaces");
        a("EP.DivisionName = 420");
        a("EP.InputMode = Strict");
        a("EP.Uniqua.Enabled = True");
        a("EP.Uniqua.Gesegelt = 2");
        a("EP.Uniqua.Gemeldet = 8");
        a("EP.Uniqua.Gezeitet = 8");
        a("EP.Uniqua.Faktor = 1.10");
        a("");
        a("#Athletes");
        a("");
        a("FR.420.SNR1000.SN=Boot Nr.8");
        a("FR.420.SNR1001.SN=Boot Nr.7");
        a("FR.420.SNR1002.SN=Boot Nr.6");
        a("FR.420.SNR1003.SN=Boot Nr.5");
        a("FR.420.SNR1004.SN=Boot Nr.4");
        a("FR.420.SNR1005.SN=Boot Nr.3");
        a("FR.420.SNR1006.SN=Boot Nr.2");
        a("FR.420.SNR1007.SN=Boot Nr.1");
        a("");
        a("FR.420.SNR1000.LN=Boot Nr.8");
        a("FR.420.SNR1001.LN=Boot Nr.7");
        a("FR.420.SNR1002.LN=Boot Nr.6");
        a("FR.420.SNR1003.LN=Boot Nr.5");
        a("FR.420.SNR1004.LN=Boot Nr.4");
        a("FR.420.SNR1005.LN=Boot Nr.3");
        a("FR.420.SNR1006.LN=Boot Nr.2");
        a("FR.420.SNR1007.LN=Boot Nr.1");
        a("");
        a("FR.420.SNR1000.NOC=GER");
        a("FR.420.SNR1001.NOC=GER");
        a("FR.420.SNR1002.NOC=USA");
        a("FR.420.SNR1003.NOC=FRA");
        a("FR.420.SNR1004.NOC=RUS");
        a("FR.420.SNR1005.NOC=GER");
        a("FR.420.SNR1006.NOC=GER");
        a("FR.420.SNR1007.NOC=SWE");
        a("");
        a("#Startlist");
        a("");
        a("#W1");
        a("");
        a("FR.420.W1.Bib1.Rank=2");
        a("FR.420.W1.Bib2.Rank=7");
        a("FR.420.W1.Bib3.Rank=5");
        a("FR.420.W1.Bib4.Rank=1");
        a("FR.420.W1.Bib5.Rank=6");
        a("FR.420.W1.Bib6.Rank=8");
        a("FR.420.W1.Bib7.Rank=4");
        a("FR.420.W1.Bib8.Rank=3");
        a("");
        a("#W2");
        a("");
        a("FR.420.W2.Bib1.Rank=4");
        a("FR.420.W2.Bib2.Rank=5");
        a("FR.420.W2.Bib3.Rank=1");
        a("FR.420.W2.Bib4.Rank=8");
        a("FR.420.W2.Bib5.Rank=6");
        a("FR.420.W2.Bib6.Rank=7");
        a("FR.420.W2.Bib7.Rank=3");
        a("FR.420.W2.Bib8.Rank=2");
        return sb.toString();
    }

    public String SKK() {
        sb = new StringBuffer();
        a("DP.StartlistCount = 2");
        a("SKK.Kreise.W1.STL.Count=2");
        a("");
        a("SKK.Kreise.W1.Bib1.R1=58");
        a("SKK.Kreise.W1.Bib1.R2=58");
        a("SKK.Kreise.W1.Bib1.M1X=104");
        a("SKK.Kreise.W1.Bib1.M1Y=0");
        a("SKK.Kreise.W1.Bib1.M2X=146");
        a("SKK.Kreise.W1.Bib1.M2Y=82");
        a("");
        a("SKK.Kreise.W1.Bib2.R1=41");
        a("SKK.Kreise.W1.Bib2.R2=100");
        a("SKK.Kreise.W1.Bib2.M1Y=120");
        return sb.toString();
    }

    public String FR_IDM2000() {
        return ""; // IDM2000.TestData;
    }
}
