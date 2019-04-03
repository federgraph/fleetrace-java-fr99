package org.riggvar.bo;

import org.riggvar.base.*;
import org.riggvar.event.*;

public class TBOMsg extends TBaseMsg {
    public TBO BO;
    public TMsgParser msgParser;
    public int ItemPos;
    public int AthleteID;

    public TBOMsg() {
        super();
        BO = TMain.BO;
        msgParser = new TMsgParser();
        KatID = org.riggvar.base.LookupKatID.FR;
    }

    private TEventRowCollectionItem FindCR() {
        TEventNode qn = BO.EventNode;
        if (qn != null) {
            if (ItemPos > 0) {
                return qn.getBaseRowCollection().get(ItemPos - 1);
            } else {
                return qn.FindBib(getBib());
            }
        } else {
            return null;
        }
    }

    private String GetColName() {
        if (!FRunID.startsWith("W")) {
            return "";
        }
        String s = Utils.Copy(FRunID, 2, FRunID.length());
        int i = Utils.StrToIntDef(s, -1);
        if ((i < 1) || (i > BO.BOParams.RaceCount)) {
            return "";
        }
        return "col_R" + Utils.IntToStr(i);
    }

    @Override
    public void ClearResult() {
        super.ClearResult();
        ItemPos = -1;
        AthleteID = -1;
    }

    @Override
    public boolean DispatchProt() {
        ClearResult();

        // Comments-----------------------------------
        if ((Prot.equals("")) || Prot.startsWith("//") || Prot.startsWith("#")) {
            this.setMsgType(TBaseConst.MsgType_Comment);
            return true;
        }

        // Management Commands------------------------
        if (Prot.startsWith("Manage.")) {
            BO.StatusFeedback.ParseLine(Prot);
            return true;
        }

        // Properties---------------------------------
        if (Prot.startsWith("EP.") || Prot.startsWith("Event.Prop_")) {
            BO.EventProps.ParseLine(Prot);
            this.setMsgType(TBaseConst.MsgType_Prop);
            return true;
        }

        // ignore params------------------------------
        if (Prot.startsWith("DP.") || Prot.startsWith("Event.")) {
            return true;
        }

        // Data---------------------------------------
        if (Prot.startsWith(BO.cTokenModul)) {
            return ParseProt();
        }

        return false;
    }

    private boolean ParseProt() {
        this.setMsgType(TBaseConst.MsgType_Input);

        boolean result = msgParser.Parse(Prot);
        if (result) {
            this.setMsgType(msgParser.MsgType);
            this.setMsgKey(msgParser.getMsgKey());
            this.setMsgValue(msgParser.sValue);
            //
            this.setDivision(msgParser.sDivision);
            this.setRunID(msgParser.sRunID);
            this.setBib(Utils.StrToIntDefEmpty(msgParser.sBib, -1));
            this.setCmd(msgParser.sCommand);
            this.ItemPos = Utils.StrToIntDefEmpty(msgParser.sPos, -1);
            this.AthleteID = Utils.StrToIntDefEmpty(msgParser.sAthlete, -1);
            this.DBID = Utils.StrToIntDefEmpty(msgParser.sMsgID, -1);
            //
            HandleProt();
        }
        return result;
    }

    public void HandleProt() {

        MsgResult = 1;
        boolean MsgHandled = false;

        // test message
        if (FCmd.equals("XX")) {
            // if Verbose then Trace('HandleProt: test message');
        } else if (FCmd.equals("Count")) {
            MsgHandled = BO.UpdateStartlistCount( /* Division + */FRunID, Utils.StrToIntDef(FMsgValue, -1));

        } else if (AthleteID > 0) {
            MsgHandled = BO.UpdateAthlete(AthleteID, FCmd, FMsgValue);

        } else if (FCmd.equals("IsRacing")) {
            if (BO.FindNode(FRunID) != null) {
                BO.EventNode.getBaseRowCollection().get(0).Race[0].IsRacing = (getMsgValue()
                        .equals(Utils.BoolStr(true)));
            }
        }

        else {
            String temp = FMsgValue.toLowerCase();
            if ((temp.equals("empty")) || (temp.equals("null")) || (temp.equals("99:99:99.99"))) {
                FMsgValue = "-1";
            }
            TEventRowCollectionItem cr = FindCR();
            if (cr != null) {
                MsgHandled = HandleMsg(cr);
            }
        }

        if (MsgHandled) {
            BO.CounterMsgHandled++;
            MsgResult = 0;
        }
    }

    public boolean HandleMsg(TEventRowCollectionItem cr) {
        String Cmd = getCmd();
        TEventBO o = TMain.BO.EventBO;
        String s = getMsgValue();
        int r = GetRaceIndex();

        if (Cmd.equals("QU")) {
            BO.RaceBO.EditQU(r, cr.getIndex(), s);
        } else if (Cmd.equals("DG")) {
            BO.RaceBO.EditDG(r, cr.getIndex(), s);
        } else if (Cmd.equals("Rank")) {
            BO.RaceBO.EditOTime(r, cr.getIndex(), s);
        } else if (Cmd.equals("RV")) {
            TEventRowCollectionItem crev = BO.EventNode.getBaseRowCollection().get(cr.getIndex());
            if (crev != null) {
                BO.EventNode.BO.EditRaceValue(crev, s, GetColName());
            }
        } else if (Cmd.equals("FM")) {
            if (r != -1) {
                TEventRowCollectionItem crev = BO.EventNode.getBaseRowCollection().get(cr.getIndex());
                crev.Race[r].Fleet = Utils.StrToIntDef(s, crev.Race[r].Fleet);
            }
        } else if (Cmd.equals("Bib")) {
            o.EditBib(cr, s);
        } else if (Cmd.equals("SNR")) {
            o.EditSNR(cr, s);
        }
        return true;
    }

    private int GetRaceIndex() {
        if (!FRunID.startsWith("W"))
            return -1;
        String s = FRunID.substring(1);
        int i = Utils.StrToIntDef(s, -1);
        if (i < 1 || i > BO.BOParams.RaceCount)
            i = -1;
        return i;
    }

}
