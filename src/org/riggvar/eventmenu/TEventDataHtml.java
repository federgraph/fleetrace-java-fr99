package org.riggvar.eventmenu;

import java.util.List;

import org.riggvar.base.TStringList;
import org.riggvar.base.TStrings;
import org.riggvar.base.Utils;
import org.w3c.dom.Node;
import java.util.regex.*;

public class TEventDataHtml {

    class X extends TXmlDocumentParser {
    }

    boolean FOK;
    String FError;

    String upchar;
    String downchar;

    Node Xml;

    List<Node> PropertyRows;
    List<Node> HeaderFields;
    List<Node> BodyRows;

    StringBuilder sb;

    TStrings SL; // not owned, do not free

    TStrings FieldList;
    TStrings Props;
    TStringList FLA;

    int StartListCount;
    int RaceCount;
    int TargetFleetSize;
    boolean UseFleets;

    int R1Col;
    int EndCol;
    int BibCol;

    int Bib;
    int Race;

    boolean WantPreTags;

    public TEventDataHtml() {
        SL = new TStringList();
        FieldList = new TStringList();
        Props = new TStringList();
        FLA = new TStringList();
        FLA.setDelimiter(';');
    }

    public String TransformEventData(String EventData) {
        String result;
        try {
            SL.Clear();
            String t;

            t = "<?xml version=\"1.0\"?>" + "<!DOCTYPE html [" + "<!ENTITY nbsp \"&#160;\">" +
            // "<!ENTITY ndash \"&#8211;\">" +
            // "<!ENTITY mdash \"&#8212;\">" +
                    "]>";

//            t = "<!DOCTYPE documentElement[\n" +
//                    "<!ENTITY nbsp \"\"&#160;\"\">\n" +
//                    "<!ENTITY ndash \"\"&#8211;\"\">\n" +
//                    "<!ENTITY mdash \"\"&#8212;\"\">\n" + 
//                    "]>\n" + EventData;

            Xml = X.GetNode(t + EventData);
            Transform();
            result = SL.getText();
            Xml = null;
            SL.Clear();
            FOK = true;
        } catch (Exception e) {
            FOK = false;
            FError = e.getMessage();
            result = "";
        }
        return result;
    }

    public String getEventName() {
        return Props.getValue("EventName");
    }

    void WriteNameListHeaderLine(int StartIndex, int StopIndex) {
        int n;
        String fl;
        n = 0;
        FLA.Clear();
        FLA.Add("ID");
        FLA.Add("SNR");
        FLA.Add("Bib");
        for (int i = StartIndex; i < StopIndex; i++) {
            n++;
            FLA.Add("N" + n);
        }
        fl = FLA.getDelimitedText();
        SL.Add(fl);
        FLA.Clear();
    }

    void WriteFirstLine(TStrings FL, int StartIndex, int StopIndex, int RaceIndex, int EndIndex) {
        String fl;
        String s;
        if (FL.getCount() == 0) {
            FError = "FieldList.Count == 0";
            return;
        }

        FLA.Clear();
        for (int i = 0; i < EndIndex; i++) {
            if ((i >= StartIndex && i <= StopIndex) || (i >= RaceIndex)) {
                if (i > FL.getCount() - 1) {
                    FError = "WriteFirstLine: FieldList.Count < Index";
                    return;
                }
                s = FL.getString(i);
                if (s != "") {
                    s = s.replace(upchar, "");
                    s = s.replace(downchar, "");
                    FLA.Add(s);
                }
            }
        }
        fl = FLA.getDelimitedText();
        SL.Add(fl);
    }

    String GenQU(String value) {
        return "FR.*.W" + Race + ".Bib" + Bib + ".QU=" + value;
    }

    int CalcFleetSize(int rCol, String gClass) {
        List<Node> tds;
        int i;
        int c;

        c = 0;
        for (Node tr : BodyRows) {
            tds = X.Descendants(tr, "td");
            i = 0;
            for (Node td : tds) {
                if (i == rCol) {
                    if (!X.ReadAttribute(td, gClass).equals(""))
                        c++;
                }
                i++;
            }
        }
        return c;
    }

    int GetRaceIndex(int r) {
        return r + R1Col - 1;
    }

    int CountGroupElements(int Group) {
        List<Node> tds;
        String cls;
        int c;
        String gn;

        c = 0;
        gn = "g" + Group;
        for (Node tr : BodyRows) {
            tds = X.Descendants(tr, "td");
            for (Node td : tds) {
                if (!X.ReadAttribute(td, "class").equals("")) {
                    cls = X.ReadAttribute(td, "class");
                    if (cls.contains(gn))
                        c++;
                }
            }
        }
        return c;
    }

    String RetrieveGroupMatch(String input, Matcher mc) {
        String s = "";
        String t;
        boolean matchFound = mc.find();
        if (matchFound) {
            for (int i = 0; i <= mc.groupCount(); i++) {
                t = mc.group(i);
                if (i == 1)
                    s = t;
            }
        }
        return s;
    }

    boolean IsRaceCol(String th) {
        Pattern regex;
        Matcher mc;
        String t;

        // var pattern = /R(\d+)/;
        // return pattern.test($(th).text());

        regex = Pattern.compile("R(\\d+)");
        mc = regex.matcher(th);
        t = RetrieveGroupMatch(th, mc);
        if (!t.equals(""))
            return true;
        return false;
    }

    void ExtractParams() {
        String s;
        int i;
        int g0, g1;

        if (HeaderFields == null) {
            FError = "HeaderFields = nil in ExtractParams";
            FOK = false;
            return;
        }

        i = 0;
        for (Node th : HeaderFields) {
            s = th.getTextContent();
            if ((R1Col > 0 && EndCol == 0) && !IsRaceCol(s)) {
                RaceCount = i - R1Col;
                EndCol = i;
            } else if ((i > 0 && R1Col == 0) && IsRaceCol(s)) {
                R1Col = i;
            }
            if (EndCol == 0) {
                FieldList.Add(s);
            }
            i++;
        }

        StartListCount = BodyRows.size();

        UseFleets = CountGroupElements(0) > 0;
        UseFleets = UseFleets || (CountGroupElements(1) > 0);

        g0 = CalcFleetSize(GetRaceIndex(1), "g0");
        g1 = CalcFleetSize(GetRaceIndex(1), "g1");
        TargetFleetSize = Math.max(g0, g1);
    }

    int ExtractGroup(Node td) {
        String cl = X.ReadAttribute(td, "class");
        if (cl.equals("g0"))
            return 0;
        else if (cl.equals("g1"))
            return 1;
        else if (cl.equals("g2"))
            return 2;
        else if (cl.equals("g3"))
            return 3;
        else if (cl.equals("g4"))
            return 4;
        else
            return 0;
    }

    String ExtractFinishPosition(String s) {
        Pattern regex;
        Matcher mc;
        String t;

        // '12' --> 12
        regex = Pattern.compile("^(\\d+)$");
        if (regex.matcher(s).matches())
            return s;

        // '[35]' --> 35
        regex = Pattern.compile("^\\[(\\d+)\\]$");
        mc = regex.matcher(s);
        t = RetrieveGroupMatch(s, mc);
        if (!t.equals(""))
            return t;

        // '[0/DNF]' --> 0
        regex = Pattern.compile("^\\[(\\d+)\\/\\S+\\]");
        mc = regex.matcher(s);
        t = RetrieveGroupMatch(s, mc);
        if (!t.equals(""))
            return t;

        // '5/DSQ' --> 5
        regex = Pattern.compile("^(\\d+)\\/\\S+");
        mc = regex.matcher(s);
        t = RetrieveGroupMatch(s, mc);
        if (!t.equals(""))
            return t;

        return "0";
    }

    String ExtractQU(String s) {
        Pattern regex;
        Matcher mc;
        int c;
        String t;

        // '12' --> 12
        regex = Pattern.compile("^(\\d+)$");
        if (regex.matcher(s).matches())
            return "";

        // '[35]' --> 35
        regex = Pattern.compile("^\\[(\\d+)\\]$");
        if (regex.matcher(s).matches())
            return "";

        // '[0/DNF]' --> 0
        regex = Pattern.compile("^\\[\\d+\\/(\\S+)\\]");
        mc = regex.matcher(s);
        t = RetrieveGroupMatch(s, mc);
        if (!t.equals(""))
            return GenQU(t);

        // '5/DSQ' --> 5
        regex = Pattern.compile("^\\d+\\/(\\S+)");
        mc = regex.matcher(s);
        t = RetrieveGroupMatch(s, mc);
        if (!t.equals(""))
            return GenQU(t);

        // 'dnf' --> dnf
        regex = Pattern.compile("dns|dnf|dsq", Pattern.CASE_INSENSITIVE);

        mc = regex.matcher(s);
        c = mc.groupCount();
        if (c >= 1) {
            t = mc.group(0);
            return GenQU(t);
        }

        return "";
    }

    String ExtractNoRace(String s) {
        // '(0)'
        Pattern regex = Pattern.compile("^\\(0\\)$");
        if (regex.matcher(s).matches())
            return "FR.*.W" + Race + ".Bib" + Bib + ".RV=x";
        return "";
    }

    void MakeNameList() {
        List<Node> tds;
        String r, s;
        int i;

        WriteNameListHeaderLine(3, R1Col);
        s = "";
        i = 0;
        for (Node tr : BodyRows) {
            tds = X.Descendants(tr, "td");
            for (Node td : tds) {
                r = td.getTextContent();
                if (r.equals("&nbsp;"))
                    r = "";
                if (i >= 0 && i < R1Col) {
                    if (s != "")
                        s = s + ';';
                    s = s + r;
                }
                i++;
            }
            SL.Add(s);
            s = "";
            i = 0;
        }
    }

    void MakeStartList() {
        List<Node> tds;
        String r, s;
        int i;
        int e;

        e = 3;
        WriteFirstLine(FieldList, 0, e, e, e);
        s = "";
        i = 0;
        for (Node tr : BodyRows) {
            tds = X.Descendants(tr, "td");
            for (Node td : tds) {
                r = td.getTextContent();
                if (i < e) {
                    if (!s.equals(""))
                        s = s + ';';
                    s = s + r;
                }
                i++;
            }
            SL.Add(s);
            s = "";
            i = 0;
        }
    }

    void MakeFleetList() {
        List<Node> tds;
        String r, s;
        int i;

        WriteFirstLine(FieldList, 1, 2, R1Col, EndCol);
        s = "";
        i = 0;
        for (Node tr : BodyRows) {
            tds = X.Descendants(tr, "td");
            for (Node td : tds) {
                r = td.getTextContent();
                if (i > 0 && i < 3) {
                    if (!s.equals(""))
                        s = s + ';';
                    s = s + r;
                } else if (i >= R1Col && i < EndCol) {
                    if (!s.equals(""))
                        s = s + ';';
                    s = s + ExtractGroup(td);
                }
                i++;
            }
            SL.Add(s);
            s = "";
            i = 0;
        }
    }

    void MakeFinishList() {
        List<Node> tds;
        String r, s;
        int i;

        WriteFirstLine(FieldList, 0, 2, R1Col, EndCol);
        s = "";
        i = 0;
        for (Node tr : BodyRows) {
            tds = X.Descendants(tr, "td");
            for (Node td : tds) {
                r = td.getTextContent();
                if ((i >= 0 && i <= 2) || (i >= R1Col && i < EndCol)) {
                    if (!s.equals(""))
                        s = s + ';';
                    s = s + ExtractFinishPosition(r);
                }
                i++;
            }
            SL.Add(s);
            s = "";
            i = 0;
        }
    }

    void MakeMsgList() {
        List<Node> tds;
        String r, s;
        int i;

        s = "";
        i = 0;
        for (Node tr : BodyRows) {
            tds = X.Descendants(tr, "td");
            for (Node td : tds) {
                r = td.getTextContent();
                if (i < EndCol) {
                    if (i == BibCol)
                        Bib = Utils.StrToIntDef(r, 0);
                    if (i >= R1Col) {
                        Race = i - R1Col + 1;
                        s = ExtractQU(r);
                        if (!s.equals("")) {
                            SL.Add(s);
                            s = "";
                        }
                        s = ExtractNoRace(r);
                        if (!s.equals("")) {
                            SL.Add(s);
                            s = "";
                        }
                    }
                }
                i++;
            }
            i = 0;
        }
    }

    void ReadTables() {
        String s;
        Node body = X.ReadElement(Xml, "body");
        if (body != null) {
            // find the tables as direct descendants of body tag
            FindTables(body);

            if (BodyRows == null) {
                // find tables inside the results div
                List<Node> divs = X.Descendants(body, "div");
                for (Node dive : divs) {
                    s = X.ReadAttribute(dive, "id");
                    if (s.equals("results"))
                        FindTables(dive);
                }
            }
        }
    }

    void ReadProperties(Node table) {
        List<Node> trs, tds;
        String s;
        int i;
        String k, v;

        trs = X.Descendants(table, "tr");
        for (Node tr : trs) {
            tds = X.Descendants(tr, "td");
            i = 1;
            k = "";
            v = "";
            for (Node td : tds) {
                s = td.getTextContent();
                if (i == 1)
                    k = s;
                else if (i == 2)
                    v = s;
                i++;
                if (i == 3) {
                    Props.setValue(k, v);
                    break;
                }
            }
        }
        PropertyRows = trs;
        // remember for now and free later in Reset method
    }

    void ReadResults(Node table) {
        if (HeaderFields != null) {
            FError = "HeaderFields null in ReadResults";
            return;
        }

        Node nl = X.ReadElement(table, "thead");
        if (nl != null) {
            List<Node> trs = X.Descendants(nl, "tr");
            if (trs.size() == 1) {
                Node tr = X.ReadElement(nl, "tr");
                HeaderFields = X.Descendants(tr, "th");
            }
        }
        BodyRows = X.Descendants(table, "tr");
    }

    void Reset() {
        FError = "";
        FOK = false;

        HeaderFields = null;
        BodyRows = null;
        PropertyRows = null;

        upchar = String.valueOf((char) 9650);
        downchar = String.valueOf((char) 9660);

        StartListCount = 0;
        R1Col = 0;
        RaceCount = 0;
        EndCol = 0;
        BibCol = 2;
        Bib = 0;

        FieldList.Clear();
        Props.Clear();
        FLA.Clear();

        Props.setValue("EP.UseFleets", "false");
        Props.setValue("EP.TargetFleetSize", "20");
        Props.setValue("EP.FirstFinalRace", "0");
        Props.setValue("EP.NameFieldCount", "0");
        Props.setValue("EP.NameFieldOrder", "");
        Props.setValue("EP.FieldCaptions", "");
        Props.setValue("EP.FieldCount", "0");
        Props.setValue("EP.ScoringSystem", "Low Point System");
        Props.setValue("EP.Throwouts", "1");
        Props.setValue("EP.ReorderRAF", "false");
        Props.setValue("EP.ColorMode", "N");
    }

    void Transform() {
        Reset();

        ReadTables();

        // HeaderFields = $('table.results thead tr th');
        if (HeaderFields == null) {
            FError = "HeaderFields = null";
            FOK = false;
            return;
        }

        // BodyRows = $('table.results tbody tr');
        if (BodyRows == null) {
            FError = "BodyRows = null";
            FOK = false;
            return;
        }

        // PropertyRows = $('table.properties tr');
        if (PropertyRows == null) {
            FError = "PropertyRows = null";
            FOK = false;
            return;
        }

        ExtractParams();

        if (WantPreTags)
            SL.Add("<pre>");

        SL.Add("#Params");

        SL.Add("");

        SL.Add("DP.StartlistCount =" + StartListCount);
        SL.Add("DP.ITCount = 0");
        SL.Add("DP.RaceCount = " + RaceCount);

        SL.Add("");

        SL.Add("#Event Properties");

        SL.Add("");

        SL.Add("EP.Name = " + Props.getValue("EP.Name"));
        SL.Add("EP.ScoringSystem = " + Props.getValue("EP.ScoringSystem"));
        SL.Add("EP.Throwouts = " + Props.getValue("EP.Throwouts"));
        SL.Add("EP.ReorderRAF = " + Props.getValue("EP.ReorderRAF"));
        SL.Add("EP.DivisionName = *");
        SL.Add("EP.InputMode = Strict");
        SL.Add("EP.RaceLayout = Finish");
        SL.Add("EP.NameSchema = NX");
        // t.Add("EP.FieldMap = " + Props.Values["EP.FieldMap"]);
        SL.Add("EP.FieldCaptions = " + Props.getValue("EP.FieldCaptions"));
        SL.Add("EP.FieldCount = " + Props.getValue("EP.FieldCount"));
        SL.Add("EP.NameFieldCount = " + Props.getValue("EP.NameFieldCount"));
        SL.Add("EP.NameFieldOrder = " + Props.getValue("EP.NameFieldOrder"));

        SL.Add("EP.ColorMode = " + Props.getValue("EP.ColorMode"));
        SL.Add("EP.UseFleets = " + Props.getValue("EP.UseFleets"));
        SL.Add("EP.TargetFleetSize = " + Props.getValue("EP.TargetFleetSize"));
        SL.Add("EP.FirstFinalRace = " + Props.getValue("EP.FirstFinalRace"));
        SL.Add("EP.IsTimed = false");
        SL.Add("EP.UseCompactFormat = true");

        SL.Add("");

        SL.Add("NameList.Begin");
        MakeNameList();
        SL.Add("NameList.End");

        SL.Add("");

        SL.Add("StartList.Begin");
        MakeStartList();
        SL.Add("StartList.End");

        if (UseFleets) {
            SL.Add("");

            SL.Add("FleetList.Begin");
            MakeFleetList();
            SL.Add("FleetList.End");
        }

        SL.Add("");

        SL.Add("FinishList.Begin");
        MakeFinishList();
        SL.Add("FinishList.End");

        SL.Add("");

        // t.Add("MsgList.Begin");
        MakeMsgList();
        // t.Add("MsgList.End");

        if (WantPreTags)
            SL.Add("</pre>");
    }

    void ReadTable(Node table) {
        String s = X.ReadAttribute(table, "class");
        if (s.equals("fr properties"))
            ReadProperties(table);
        else if (s.equals("sortable fr results"))
            ReadResults(table);
    }

    void FindTables(Node node) {
        List<Node> tables = X.Descendants(node, "table");
        if (tables != null)
            for (Node table : tables)
                ReadTable(table);
    }

}
