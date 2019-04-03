package org.riggvar.bo;

import org.riggvar.base.*;
import org.riggvar.col.*;

public class TExcelImporter {

    public static final int TableID_NameList = 1;
    public static final int TableID_FinishList = 2;
    public static final int TableID_StartList = 3;
    public static final int TableID_ResultList = 4;
    public static final int TableID_TimeList = 5;
    public static final int TableID_FleetList = 6;
    public static final int TableID_CaptionList = 7;

    public static final String NameListStartToken = "NameList.Begin";
    public static final String NameListEndToken = "NameList.End";

    public static final String StartListStartToken = "StartList.Begin";
    public static final String StartListEndToken = "StartList.End";

    public static final String FinishListStartToken = "FinishList.Begin";
    public static final String FinishListEndToken = "FinishList.End";

    public static final String TimeListStartToken = "TimeList.Begin"; // with Param: TimeList.Begin.R1
    public static final String TimeListEndToken = "TimeList.End";

    public static final String ResultListStartToken = "ResultList.Begin";
    public static final String ResultListEndToken = "ResultList.End";

    public static final String FleetListStartToken = "FleetList.Begin";
    public static final String FleetListEndToken = "FleetList.End";

    public static final String CaptionListStartToken = "CaptionList.Begin";
    public static final String CaptionListEndToken = "CaptionList.End";

    private TStringList SL;
    private TStringList SLToken;
    private TStringList SLField;
    private TStringList SLFilter;

    private int SNR;
    private int Bib;
//private String Division;
    private int PosID;
    private int N;
    private int W;
    private int IT;
    public char Delimiter;
    private int TableID;

    public TExcelImporter() {
        Delimiter = ';';
        SL = new TStringList();
        SLToken = new TStringList();
        SLToken.setDelimiter(Delimiter);
        // SLToken.StrictDelimiter = true;
        SLField = new TStringList();
        SLFilter = new TStringList();
        SLToken.setQuoteChar('"');
    }

    public String Go(String command, String data) {
        TStringList Memo = new TStringList();
        Memo.setText(data);
        if (command.equals("Convert"))
            Convert(Memo, TableID_ResultList);
        if (command.equals("ShowTabs"))
            ShowTabs(Memo);
        if (command.equals("GetTestData"))
            GetTestData(Memo);

        return Memo.getText();
    }

    public void SetTableID(String Value) {
        if (Value.contains("StartList"))
            TableID = TableID_StartList;
        else if (Value.contains("FleetList"))
            TableID = TableID_FleetList;
        else if (Value.contains("FinishList"))
            TableID = TableID_FinishList;
        else if (Value.contains("NameList"))
            TableID = TableID_NameList;
        else if (Value.contains("ResultList"))
            TableID = TableID_ResultList;
        else if (Value.contains("TimeList")) {
            TableID = TableID_TimeList;
            W = ExtractRaceParam(Value);
        } else if (Value.contains("CaptionList"))
            TableID = TableID_CaptionList;
        else
            TableID = 0;
    }

    private int ExtractRaceParam(String Value) {
        TTokenParser tp = new TTokenParser(Value);
        tp.NextToken(); // TimeList
        tp.NextToken(); // Begin
        return tp.NextTokenX("R"); // RX
    }

    protected String TrimAroundEqual(String s) {
        String result = s;
        int i = s.indexOf('=');
        if (i > 0) {
            // substring(beginIndex, endIndex) ends at endIndex-1 !
            result = s.substring(0, i).trim() + '=' + s.substring(i + 1).trim();
        }
        return result;
    }

    public void GetTestData(TStrings Memo) {
        Memo.Clear();
        Memo.Add("Bib;SNR;FN;LN;NOC;R1;R2");
        Memo.Add("1;1000;A;a;FRA;1;2");
        Memo.Add("2;1001;B;b;GER;2;3");
        Memo.Add("3;1002;C;b;ITA;3;1");
    }

    public void ShowTabs(TStrings Memo) {
        SL.setText(Memo.getText());
        Memo.Clear();
        char delim = '\t';
        String s;
        for (int i = 0; i < SL.getCount(); i++) {
            s = SL.getString(i);
            s = s.replace(delim, ';');
            Memo.Add(s);
        }
    }

    public String Convert(String MemoText, int tableID) {
        TStringList Memo = new TStringList();
        Memo.setText(MemoText);
        Convert(Memo, tableID);
        return Memo.getText();
    }

    public void Convert(TStrings Memo, int tableID) {
        SL.Clear();
        TableID = tableID;
        Transpose(Memo);
        Memo.setText(SL.getText());
        TableID = 0;
        SL.Clear();
    }

    private void SetValue_StartList(String f, String v) {
        String s;

        if (f.equals("SNR")) {
            SNR = Utils.StrToIntDef(v, 0);
            if (SNR != 0) {
                s = "FR.*.W1.STL.Pos" + PosID + ".SNR=" + SNR;
                SL.Add(s);
            }
        } else if (f.equals("Bib")) {
            Bib = Utils.StrToIntDef(v, 0);
            if (SNR != 0 && Bib != 0) {
                s = "FR.*.W1.STL.Pos" + PosID + ".Bib=" + Bib;
                SL.Add(s);
            }
        }
    }

    private void SetValue_NameList(String f, String v) {

        String s;
        if (f.equals("FN") || f.equals("LN") || f.equals("SN") || f.equals("NC") || f.equals("GR") || f.equals("PB")) {
            String v1 = v.trim();
            s = "FR.*.SNR" + SNR + "." + f + "=" + v1;
            SL.Add(s);
        }

        else if (f.length() > 1 && f.startsWith("N")) {
            N = Utils.StrToIntDef(f.substring(1), 0);
            if (N > 0 && SNR > 0) {
                s = "FR.*.SNR" + SNR + ".N" + N + "=" + v;
                SL.Add(s);
            }
        }
    }

    private void SetValue_FinishList(String f, String v) {
        String s;
        if (f.length() > 1 && f.startsWith("R")) {
            W = Utils.StrToIntDef(f.substring(1), 0);
            if (W > 0 && Bib > 0) {
                if (Utils.StrToIntDef(v, 0) > 0) {
                    s = "FR.*.W" + W + ".Bib" + Bib + ".Rank=" + v;
                    SL.Add(s);
                }
            }
        }
    }

    private void SetValue_FleetList(String f, String v) {
        String s;
        if (f.length() > 1 && f.startsWith("R")) {
            W = Utils.StrToIntDef(f.substring(1), 0);
            if (W > 0 && Bib > 0) {
                if (Utils.StrToIntDef(v, -1) >= 0) {
                    // s = "FR.*.W" + W + ".Bib" + Bib + ".RV=F" + v; //alternative
                    s = "FR.*.W" + W + ".Bib" + Bib + ".FM=" + v;
                    SL.Add(s);
                }
            }
        }
    }

    private void SetValue_TimeList(String f, String v) {
        String s;
        if (f.length() > 2 && f.startsWith("I")) {
            IT = Utils.StrToIntDef(f.substring(2), -1);
            if (IT > 0 && Bib > 0) {
                s = "FR.*.W" + W + ".Bib" + Bib + ".IT" + IT + "=" + v;
                SL.Add(s);
            }
            if (IT == 0 && Bib > 0) {
                s = "FR.*.W" + W + ".Bib" + Bib + ".FT=" + v;
                SL.Add(s);
            }
        } else if (f.equals("FT")) {
            s = "FR.*.W" + W + ".Bib" + Bib + ".FT=" + v;
            SL.Add(s);
        }
    }

    public void SetValue_ResultList(String f, String v) {
        String s;

        if (f.equals("SNR")) {
            SNR = Utils.StrToIntDef(v, 0);
            if (SNR != 0) {
                s = "FR.*.W1.STL.Pos" + PosID + ".SNR=" + SNR;
                SL.Add(s);
            }
        } else if (f.equals("Bib")) {
            Bib = Utils.StrToIntDef(v, 0);
            if (SNR != 0 && Bib != 0) {
                s = "FR.*.W1.STL.Pos" + PosID + ".Bib=" + Bib;
                SL.Add(s);
            }
        }

        else if (f.equals("FN") || f.equals("LN") || f.equals("SN") || f.equals("NC") || f.equals("GR")
                || f.equals("PB")) {
            SetValue_NameList(f, v);
        }

        else if (f.length() > 1 && f.startsWith("N")) {
            SetValue_NameList(f, v);
        }

        else if (f.length() > 1 && f.startsWith("R")) {
            W = Utils.StrToIntDef(f.substring(1), 0);
            if (W > 0 && Bib > 0) {
                if (Utils.StrToIntDef(v, -1) > -1)
                    s = "FR.*.W" + W + ".Bib" + Bib + ".RV=" + v;
                else
                    s = "FR.*.W" + W + ".Bib" + Bib + ".QU=" + v;
                SL.Add(s);
            }
        }
    }

    public void SetValue_CaptionList(String f, String v) {
        TBaseColProps.ColCaptionBag.setCaption(f, v);
    }

    private void SetValue(String f, String v) {
        if (TableID == TableID_StartList)
            SetValue_StartList(f, v);
        else if (TableID == TableID_NameList)
            SetValue_NameList(f, v);
        else if (TableID == TableID_FinishList)
            SetValue_FinishList(f, v);
        else if (TableID == TableID_TimeList)
            SetValue_TimeList(f, v);
        else if (TableID == TableID_FleetList)
            SetValue_FleetList(f, v);
        else if (TableID == TableID_ResultList)
            SetValue_ResultList(f, v);
        else if (TableID == TableID_CaptionList)
            SetValue_CaptionList(f, v);
    }

    private void Transpose(TStrings Memo) {
        String s;
        String f;
        String v;
        int snrIndex;
        String snrString;
        int bibIndex;
        String bibString;

        PosID = -1;
        SLField.Clear();
        snrIndex = -1;
        bibIndex = -1;
        for (int i = 0; i < Memo.getCount(); i++) {
            s = Memo.getString(i);
            if (s.trim().equals(""))
                continue;
            PosID++;
            Bib = 0 + PosID;
            SLToken.setDelimiter(Delimiter);
            SLToken.setDelimitedText(s);
            SNR = 999 + PosID;

            if (i > 0) {
                if (SLToken.getCount() != SLField.getCount()) {
                    // SL.Add('');
                    // SL.Add('//line skipped - SLToken.Count <> SLField.Count');
                    // SL.Add('');
                    continue;
                }
            }

            // get real SNR
            if (i == 0) {
                snrIndex = SLToken.IndexOf("SNR");
            }
            if (i > 0) {
                if (snrIndex > -1) {
                    snrString = SLToken.getString(snrIndex).trim();
                    SNR = Utils.StrToIntDef(snrString, SNR);
                }
            }

            // get real Bib
            if (i == 0) {
                bibIndex = SLToken.IndexOf("Bib");
            }
            if (i > 0) {
                if (bibIndex > -1) {
                    bibString = SLToken.getString(bibIndex).trim();
                    Bib = Utils.StrToIntDef(bibString, Bib);
                }
            }

            for (int j = 0; j < SLToken.getCount(); j++) {
                v = SLToken.getString(j);
                if (i == 0)
                    SLField.Add(v);
                else {
                    if (v.trim().equals(""))
                        continue;
                    f = SLField.getString(j);
                    SetValue(f, v);
                }
            }
        }

    }

    private void TransposePropList(TStrings Memo) {
        String s, temp, sK, sV;

        for (int l = 0; l < Memo.getCount(); l++) {
            s = Memo.getString(l);
            if (s.trim().equals(""))
                continue;

            SLToken.Clear();
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
            SLToken.Add(temp);
            sK = SLToken.getName(0);
            sV = SLToken.getValue(sK);

            SetValue(sK, sV);
        }
    }

    public void RunImportFilter(String Data, TStrings m) {
        SL.Clear();

        String s;
        boolean FilterMode;

        TableID = 0;
        FilterMode = false;
        m.setText(Data);
        for (int i = 0; i < m.getCount(); i++) {
            s = m.getString(i);

            // Comment Lines
            if (s.equals(""))
                continue;
            if (s.startsWith("//"))
                continue;
            if (s.startsWith("#"))
                continue;

            // TableEndToken for key=value property list
            if (s.equals(CaptionListEndToken)) {
                TransposePropList(SLFilter);
                SLFilter.Clear();
                FilterMode = false;
                TableID = 0;
            }

            // TableEndToken for delimited Tables
            else if (s.equals(NameListEndToken) || s.equals(StartListEndToken) || s.equals(FinishListEndToken)
                    || s.equals(TimeListEndToken) || s.equals(FleetListEndToken)) {
                Transpose(SLFilter);
                SLFilter.Clear();
                FilterMode = false;
                TableID = 0;
            }

            // TableStartToken, may include Parameters
            else if (s.equals(NameListStartToken) || s.equals(StartListStartToken) || s.equals(FinishListStartToken)
                    || s.equals(FleetListStartToken) || s.contains(TimeListStartToken)
                    || s.equals(CaptionListStartToken)) {
                SLFilter.Clear();
                FilterMode = true;
                SetTableID(s);
            }

            // DataLines, normal Message or delimited Line
            else {
                if (FilterMode)
                    SLFilter.Add(s);
                else {
                    s = TrimAroundEqual(s);
                    SL.Add(s);
                }
            }
        }
        if (SLFilter.getCount() != 0) {
            System.out.println("FilterError");
        }
        m.setText(SL.getText());
    }

    public static String Expand(String EventData) {
        TStringList t = new TStringList();
        TExcelImporter o = new TExcelImporter();
        o.RunImportFilter(EventData, t);
        return t.getText();
    }

}
