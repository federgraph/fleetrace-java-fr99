package org.riggvar.base;

public class TBaseMsgParser extends TBaseConst {

    protected String sToken;
    protected String sRest;
    protected java.util.StringTokenizer Tokenizer;
    protected TStringList SL = new TStringList();
    protected boolean FIsValid;
    protected int FLastError;
    protected String FInput;
    protected String FKey;
    protected String FValue;
    protected TStringList SLCompare = new TStringList();

    public static final int Error_None = 0;
    public static final int Error_Division = 1;
    public static final int Error_RunID = 2;
    public static final int Error_Bib = 3;
    public static final int Error_Command = 4;
    public static final int Error_Value = 5;
    public static final int Error_Pos = 6;
    public static final int Error_Athlete = 7;
    public static final int Error_MsgID = 8;
    public static final int Error_Race = 9;

    public static String getString(int e) {
        switch (e) {
        case Error_Athlete:
            return "Error_Athlete";
        case Error_Bib:
            return "Error_Bib";
        case Error_Command:
            return "Error_Command";
        case Error_Division:
            return "Error_Division";
        case Error_MsgID:
            return "Error_MsgID";
        case Error_None:
            return "Error_None";
        case Error_Pos:
            return "Error_Pos";
        case Error_Race:
            return "Error_Race";
        case Error_RunID:
            return "Error_RunID";
        case Error_Value:
            return "Error_Value";
        default:
            return "";
        }
    }

    public int MsgType;

    public String getMsgKey() {
        return FKey;
    }

    public String getMsgValue() {
        return FValue;
    }

    protected void Clear() {
        FLastError = Error_None;
        FIsValid = false;
        FInput = "";
        FKey = "";
        FValue = "";
    }

    public static String MsgTypeToString(int value) {
        return String.valueOf(MsgTypeToChar(value));
    }

    public static char MsgTypeToChar(int value) {
        switch (value) {
        case MsgType_Input:
            return 'I';
        case MsgType_Param:
            return 'P';
        case MsgType_Option:
            return 'O';
        case MsgType_Prop:
            return 'E';
        case MsgType_Cmd:
            return 'C';
        case MsgType_Request:
            return 'R';
        case MsgType_Comment:
            return 'K';
        case MsgType_Test:
            return 'T';
        default:
            return 'N';
        }
    }

    public static int ParseMsgTypeAsEnumDef(String value, int def) {
        int result = ParseMsgTypeAsEnum(value);
        if (result == MsgType_None) {
            return def;
        } else {
            return result;
        }
    }

    public static int ParseMsgTypeAsEnum(String value) {
        if (value == null) {
            return MsgType_None;
        } else if (value.equals("")) {
            return MsgType_None;
        } else {
            return ParseMsgTypeAsEnum(value.charAt(0));
        }
    }

    public static int ParseMsgTypeAsEnum(char value) {
        switch (value) {
        case 'I':
            return MsgType_Input;
        case 'P':
            return MsgType_Param;
        case 'O':
            return MsgType_Option;
        case 'E':
            return MsgType_Prop;
        case 'C':
            return MsgType_Cmd;
        case 'R':
            return MsgType_Request;
        case 'K':
            return MsgType_Comment;
        case 'T':
            return MsgType_Test;
        default:
            return MsgType_None;
        }
    }

    protected void NextToken() {
        if (Tokenizer.hasMoreTokens()) {
            sToken = Tokenizer.nextToken();
            if (sToken.length() < sRest.length())
                sRest = sRest.substring(sToken.length() + 1);
            else
                sRest = "";
        } else {
            sToken = sRest;
            sRest = "";
        }
    }

    protected int NextTokenX(String TokenName) {
        NextToken();
        int result = -1;
        int l = TokenName.length();
        if (Utils.Copy(sToken, 1, l).equals(TokenName)) {
            sToken = Utils.Copy(sToken, l + 1, sToken.length() - l);
            result = Utils.StrToIntDef(sToken, -1);
        }
        return result;
    }

    protected boolean TestTokenName(String TokenName) {
        String LongTokenName = LongToken(TokenName);
        return (Utils.Copy(sRest, 1, TokenName.length()).equals(TokenName))
                || (Utils.Copy(sRest, 1, LongTokenName.length()).equals(LongTokenName));
    }

    protected String CompareToken(String t) {
        for (int i = 0; i < SLCompare.getCount(); i++) {
            String s = SLCompare.getString(i);
            if ((t.equals(s)) || (t.equals(LongToken(s)))) {
                return s;
            }
        }
        if (IsProp(t)) {
            return t;
        }
        return "";
    }

    protected boolean ParseLeaf() {
        FIsValid = false;

        // Command
        if (!ParseCommand()) {
            FLastError = Error_Command;
            return false;
        }
        // Value
        if (!ParseValue()) {
            FLastError = Error_Value;
            return false;
        }

        FIsValid = true;
        return true;
    }

    protected boolean ParseCommand() {
        // virtual
        return false;
    }

    protected boolean ParseValue() {
        // virtual
        return false;
    }

    public boolean Parse(String s) {
        SL.Clear();

        String temp;
        int i = Utils.Pos("=", s);
        if (i > 0) {
            String s1 = Utils.Copy(s, 1, i - 1);
            s1 = s1.trim();
            String s2 = Utils.Copy(s, i + 1, s.length());
            s2 = s2.trim();
            temp = s1 + "=" + s2;
            // temp = Trim(Copy(s, 1, i-1)) + '=' + Trim(Copy(s, i+1, Length(s)));
        } else {
            temp = s.replaceAll(" ", "");

        }
        if (Utils.Pos("=", temp) == 0) {
            temp = temp + "=";
        }
        SL.Add(temp);
        String sK = SL.getName(0);
        String sV = SL.getValue(sK);
        return ParseKeyValue(sK, sV);
    }

    protected boolean ParseKeyValue(String aKey, String aValue) {
        Clear();
        FKey = aKey;
        FValue = aValue;
        FInput = aKey + "=" + aValue;
        sRest = aKey;

//		if (IsProp(aKey))
//			MsgType = TMsgType.Prop;
//		else
//			MsgType = TMsgType.Input;

        return true;
    }

    protected boolean ParsePositiveIntegerValue() {
        return Utils.StrToIntDef(FValue, -1) > -1;
    }

    protected boolean ParseBooleanValue() {
        String s = FValue.toLowerCase();
        return ((s.equals("false")) || (s.equals("true")));
    }

    protected boolean IsProp(String Token) {
        return (Utils.Copy(Token, 1, 5).equals("Prop_"));
    }

    protected boolean IsNameCommand(String Token) {
        if (Token == null) {
            return false;
        }
        if (Token.length() < 2) {
            return false;
        }
        if (!Token.startsWith("N")) {
            return false;
        }
        if (Utils.StrToIntDef(Token.substring(1), -1) == -1) {
            return false;
        }
        return true;
    }

    public String LongToken(String t) {
        // virtual
        return t;
    }

    public static String FRLongToken(String t) {
        return t;
    }

}
