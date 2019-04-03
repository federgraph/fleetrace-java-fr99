package org.riggvar.bo;

import org.riggvar.base.*;
import org.riggvar.stammdaten.*;

public class TMsgParser extends TBaseMsgParser {
    public TBO BO;

    public String sAthlete;
    public String sPos;
    public String sMsgID;

    public String sDivision;
    public String sRunID;
    public String sBib;
    public String sCommand;
    public String sValue;

    public TMsgParser() {
        BO = TMain.BO;
    }

    @Override
    public String LongToken(String t) {
        return FRLongToken(t);
    }

    public static String FRLongToken(String t) {
        String result;
        // Command
        if (t.equals("QU")) {
            result = "Status";
        } else if (t.equals("FM")) {
            result = "Fleet"; // FleetMembership
        } else if (t.equals("ST")) {
            result = "StartTime";
        } else if (Utils.Copy(t, 1, 2).equals("IT")) {
            result = "IntermediateTime" + Utils.Copy(t, 3, t.length());
        } else if (t.equals("FT")) {
            result = "FinishTime";
        } else if (t.equals("XX")) {
            result = "TestMessage";
        } else if (t.equals("DG")) {
            result = "DSQGate";
        } else if (t.equals("STL")) {
            result = "Startlist";
        } else if (t.equals("Bib")) {
            result = "Bib";
        } else if (t.equals("SNR")) {
            result = "AthleteID";
        } else if (t.equals("FN")) {
            result = "FirstName";
        } else if (t.equals("LN")) {
            result = "LastName";
        } else if (t.equals("SN")) {
            result = "ShortName";
            // else if (t.equals("NOC")) result = "NOC";
            // else if (t.equals("Gender")) result = "Gender";
        } else if (t.equals("PB")) {
            result = "PersonalBest";
        } else {
            result = t;
        }
        return result;
    }

    private boolean ParseDivision() {
        NextToken();
        SLCompare.Clear();
        SLCompare.Add(BO.cTokenDivision);
        SLCompare.Add("*");
        sDivision = CompareToken(sToken);
        boolean result = (!sDivision.equals(""));
        if (!result) {
            FLastError = Error_Division;
        }
        return result;
    }

    private boolean ParseRunID() {
        NextToken();
        SLCompare.Clear();
        SLCompare.Add(BO.cTokenRace + 1);
        sRunID = CompareToken(sToken);
        boolean result = (!sRunID.equals(""));
        if (!result) {
            FLastError = Error_RunID;
        }
        return result;
    }

    @Override
    protected boolean ParseCommand() {
        SLCompare.Clear();

        SLCompare.Add("XX");
        SLCompare.Add("IsRacing");

        SLCompare.Add("FM");
        SLCompare.Add("QU");
        SLCompare.Add("ST");
        if (BO != null) {
            for (int i = 0; i <= BO.BOParams.ITCount; i++) {
                SLCompare.Add("IT" + Utils.IntToStr(i));
            }
        }
        SLCompare.Add("FT");
        SLCompare.Add("DG");
        SLCompare.Add("Rank");
        SLCompare.Add("RV");

        if (!sPos.equals("")) {
            SLCompare.Add("SNR");
            SLCompare.Add("Bib");
        }

        if (!sAthlete.equals("")) {
            SLCompare.Add("SNR");

            SLCompare.Add("FN");
            SLCompare.Add("LN");
            SLCompare.Add("SN");
            SLCompare.Add("NC");
            SLCompare.Add("GR");
            SLCompare.Add("PB");

            SLCompare.Add(FieldNames.FN);
            SLCompare.Add(FieldNames.LN);
            SLCompare.Add(FieldNames.SN);
            SLCompare.Add(FieldNames.NC);
            SLCompare.Add(FieldNames.GR);
            SLCompare.Add(FieldNames.PB);

            for (int i = 1; i <= BO.AdapterParams.FieldCount; i++) {
                SLCompare.Add("N" + i);
            }
        }

        SLCompare.Add("Count");

        sCommand = CompareToken(sRest);
        return (!sCommand.equals(""));
    }

    private boolean ParseAthlete() {
        if (NextTokenX(BO.cTokenAthlete) > -1) {
            sAthlete = sToken;
            return true;
        } else {
            sAthlete = "";
            FLastError = Error_Athlete;
            return false;
        }
    }

    private boolean ParseRace() {
        if (NextTokenX(BO.cTokenRace) > -1) {
            sRunID = "W" + sToken;
            return true;
        } else {
            sRunID = "";
            FLastError = Error_Race;
            return false;
        }
    }

    private boolean ParseBib() {
        if (NextTokenX("Bib") > -1) {
            sBib = sToken;
            return true;
        } else {
            sBib = "";
            FLastError = Error_Bib;
            return false;
        }
    }

    private boolean ParsePos() {
        if (NextTokenX("Pos") > -1) {
            sPos = sToken;
            return true;
        } else {
            sPos = "";
            FLastError = Error_Pos;
            return false;
        }
    }

    private boolean ParseMsgID() {
        if (NextTokenX("Msg") > -1) {
            sMsgID = sToken;
            return true;
        } else {
            sMsgID = "";
            FLastError = Error_MsgID;
            return false;
        }
    }

    @Override
    protected boolean ParseValue() {
        sValue = "";
        boolean result = false;

        if (sCommand.equals("XX")) {
            result = true;

        } else if (sCommand.equals("QU")) {
            result = ParseStatusValue();

        } else if (IsTimeCommand()) {
            result = ParseTimeValue();

        } else if ((sCommand.equals("DG")) || (sCommand.equals("Bib")) || (sCommand.equals("SNR"))
                || (sCommand.equals("Count")) || (sCommand.equals("Rank")) || (sCommand.equals("FM"))) {
            result = ParsePositiveIntegerValue();

        } else if (IsAthleteCommand()) {
            result = true;

        } else if (sCommand.equals("IsRacing")) {
            result = ParseBooleanValue();

        } else if (sCommand.equals("RV")) {
            result = true; // ParseRaceValue()

        }
        if (result) {
            sValue = FValue;

        }
        return result;
    }

    private boolean ParseTimeValue() {
        return true;
    }

    private boolean ParseStatusValue() {
        return true;
        // ###
        /*
         * return ((FValue.equals("dnf")) || (FValue.equals("dsq")) ||
         * (FValue.equals("dns")) || (FValue.equals("ok")) || (FValue.equals("*")));
         */
    }

    private boolean IsTimeCommand() {
        return ((sCommand.equals("ST")) || (Utils.Copy(sCommand, 1, 2).equals("IT")) || (sCommand.equals("FT")));
    }

    private boolean IsRunID() {
        int RaceCount;
        if (BO != null) {
            RaceCount = BO.BOParams.RaceCount;
        } else {
            RaceCount = 1;

        }
        String s = Utils.Copy(sRunID, 1, 1);
        int i = Utils.StrToIntDef(Utils.Copy(sRunID, 2, sRunID.length()), -1);
        return ((s.equals("W")) && (i > 0) && (i <= RaceCount));
    }

    private boolean IsStartlistCommand() {
        return ((sCommand.equals("Bib")) || (sCommand.equals("SNR")) || (sCommand.equals("Count")));
    }

    private boolean IsAthleteCommand() {
        return ((sCommand.equals(FieldNames.FN)) || (sCommand.equals(FieldNames.LN)) || (sCommand.equals(FieldNames.SN))
                || (sCommand.equals(FieldNames.NC)) || (sCommand.equals(FieldNames.GR))
                || (sCommand.equals(FieldNames.PB))

                || (sCommand.equals("FN")) || (sCommand.equals("LN")) || (sCommand.equals("SN"))
                || (sCommand.equals("NC")) || (sCommand.equals("GR")) || (sCommand.equals("PB"))

                || IsProp(sCommand) || IsNameCommand(sCommand));
    }

    @Override
    public void Clear() {
        super.Clear();
        sAthlete = "";
        sPos = "";
        //
        sDivision = "";
        sRunID = "";
        sBib = "";
        sCommand = "";
        sValue = "";
    }

    @Override
    protected boolean ParseKeyValue(String sKey, String sValue) {
        Clear();
        FKey = sKey;
        FValue = sValue;
        FInput = sKey + "=" + sValue;

        Tokenizer = new java.util.StringTokenizer(sKey, ".");
        sToken = "";
        sRest = sKey;

        if (TestTokenName(BO.cTokenFleetRace)) {

            // consume Token
            NextToken();
        }
        if ((sToken.equals(BO.cTokenFleetRace)) && TestTokenName("Input")) {

            // consume Token Input
            NextToken();

        }
        if (TestTokenName("Msg")) {
            if (!ParseMsgID()) {
                return false;
            }
        }

        if (!ParseDivision()) {
            return false;
        }

        if (ParseLeaf()) {
            return true;
        }

        if (TestTokenName(BO.cTokenAthlete)) {
            if (!ParseAthlete()) {
                return false;
            }
        } else {

            if (TestTokenName(BO.cTokenRace)) {
                if (!ParseRace()) {
                    return false;
                }

                if (ParseLeaf()) {
                    return true;
                }
            }

            else if (!ParseRunID()) {
                return false;
            }

            if (TestTokenName("STL")) {
                NextToken();

                if (ParseLeaf()) {
                    return true;
                }

                if (!ParsePos()) {
                    return false;
                }
            }

            else if (IsRunID()) {
                if (TestTokenName("Pos")) {
                    if (!ParsePos()) {
                        return false;
                    }
                } else {
                    if (!ParseBib()) {
                        return false;
                    }
                }
            }
        }

        return ParseLeaf();
    }

    public boolean IsValid() {
        return FIsValid;
    }

    public int ErrorVal() {
        return (int) FLastError; // ###
    }

    public void Report(TStrings Memo) {
        Memo.Add("Input: " + FInput);
        Memo.Add("Divsion: " + sDivision);
        Memo.Add("RunID: " + sRunID);
        Memo.Add("Bib: " + sBib);
        if (IsStartlistCommand()) {
            if (sCommand.equals("Count")) {
                Memo.Add("Startlist.Count: " + sValue);
            } else {
                Memo.Add("Pos: " + sPos);
            }
        }
        if (IsAthleteCommand()) {
            Memo.Add("Athlete: " + sAthlete);
        }
        Memo.Add("Command: " + sCommand);
        Memo.Add("Value: " + sValue);
        if (!IsValid()) {
            Memo.Add("Error: " + getString(FLastError) + "(" + Utils.IntToStr(ErrorVal()) + ")");
        }
    }

    public String ReportProt() {
        // not implemented
        return "";
    }

    public String ReportLongProt() {
        // not implemented
        return "";
    }
}
