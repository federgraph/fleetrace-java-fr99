package org.riggvar.event;

import org.riggvar.col.*;
import org.riggvar.stammdaten.*;

import java.util.*;
import org.riggvar.base.*;
import org.riggvar.bo.*;

public class TEventBO extends
        TBaseColBO<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp> {
    private List<TEventRowCollectionItem> FL;
    private TBO BO;
    private boolean FRelaxedInputMode;
    public TUndoAgent UndoAgent;
    public int NameFieldCount;
    public String NameFieldOrder;
    public boolean WantDiffCols = false;

    public TEventBO() {
        super();
        FL = new ArrayList<TEventRowCollectionItem>();
        BO = TMain.BO;
        UndoAgent = TMain.BO.UndoAgent;
        NameFieldCount = 2;
        NameFieldOrder = "041256";
    }

    private int RaceCount() {
        return BO.BOParams.RaceCount;
    }

    public boolean getRelaxedInputMode() {
        return FRelaxedInputMode;
    }

    public void setRelaxedInputMode(boolean value) {
        if (value) {
            FRelaxedInputMode = true;
        } else {
            TEventNode ev = BO.EventNode;
            if (ev.ErrorList.IsPreconditionForStrictInputMode(ev)) {
                FRelaxedInputMode = false; // strict mode on
            }
        }
    }

    @Override
    public void InitColsActiveLayout(TEventColGrid g, int aLayout) {
        TEventColProp cp;
        g.getColsActive().clear();
        g.AddColumn("col_BaseID");

        cp = g.AddColumn("col_SNR");
        cp.setOnFinishEdit(new TEventDelegate(this, TEventDelegate.commandEditSNR));
        cp.ReadOnly = false;

        cp = g.AddColumn("col_Bib");
        cp.setOnFinishEdit(new TEventDelegate(this, TEventDelegate.commandEditBib));
        cp.ReadOnly = false;

        String s;
        for (int i = 1; i <= NameFieldCount; i++) {
            s = getNameFieldID(i);
            if (!s.equals("")) {
                cp = g.AddColumn(s);
                if (s.equals("col_NC")) {
                    cp.setOnFinishEdit(new TEventDelegate(this, TEventDelegate.commandEditNOC));
                    cp.ReadOnly = false;
                }
            }
        }

        int rc = this.RaceCount();
        for (int i = 1; i <= rc; i++) {
            cp = g.AddColumn("col_R" + Utils.IntToStr(i));
            cp.setOnFinishEdit2(new TEventDelegate(this, TEventDelegate.commandEditRaceValue));
            cp.ReadOnly = false;
        }

        g.AddColumn("col_GPoints");
        g.AddColumn("col_GRank");
        if (BO.EventProps.getShowPosRColumn())
            g.AddColumn("col_GPosR");
        if (BO.EventProps.getShowPLZColumn())
            g.AddColumn("col_PLZ");
        if (this.BO.EventProps.ShowCupColumn) // if (aLayout > 0)
            g.AddColumn("col_Cup");
    }

    private String getNameFieldID(int Index) {
        char c;

        if (Index < 1 || Index > 6)
            return "";

        if (Index <= NameFieldOrder.length())
            c = NameFieldOrder.charAt(Index - 1);
        else {
            String s = "" + Index;
            c = s.charAt(0);
        }

        switch (c) {
        case '0':
            return "col_DN";
        case '1':
            return "col_FN";
        case '2':
            return "col_LN";
        case '3':
            return "col_SN";
        case '4':
            return "col_NC";
        case '5':
            return "col_GR";
        case '6':
            return "col_PB";
        }

        return "";
    }

    public String EditSNR(TEventRowCollectionItem cr, String Value) {
        if (cr != null) {
            cr.SNR = Utils.StrToIntDef(Value, cr.SNR);
            Value = Utils.IntToStr(cr.SNR);
            BO.setSNR(cr.getIndex(), cr.SNR);
        }
        return Value;
    }

    public String EditBib(TEventRowCollectionItem cr, String Value) {
        if (cr != null) {
            int oldBib = cr.Bib;
            cr.Bib = Utils.StrToIntDef(Value, cr.Bib);
            Value = Utils.IntToStr(cr.Bib);
            BO.setBib(cr.getIndex(), cr.Bib);

            if (oldBib != cr.Bib) {
                UndoAgent.UndoFlag = true;
                UndoAgent.MsgTree.Division.Race1.Startlist.Pos(cr.BaseID).Bib("" + oldBib);
                UndoAgent.MsgTree.Division.Race1.Startlist.Pos(cr.BaseID).Bib("" + cr.Bib);
            }
        }
        return Value;
    }

    public String EditNOC(TEventRowCollectionItem cr, String Value) {
        if (cr != null) {
            TStammdatenRowCollection cl = BO.StammdatenNode.getBaseRowCollection();
            TStammdatenRowCollectionItem crs = cl.FindKey(cr.SNR);
            BO.StammdatenBO.editNC(crs, Value);
        }
        return Value;
    }

    public String EditRaceValue(TEventRowCollectionItem cr, String Value, String ColName) {
        if (cr != null) {
            int i;
            // i = Util.StrToIntDef(Utils.Copy(ColName, 6, ColName.Length), -1);
            try {
                i = Integer.parseInt(ColName.substring(5));
            } catch (Exception ex) {
                i = -1;
            }

            if ((i < 1) || (i > this.RaceCount())) {
                return "";
            }

            if (Value.equals("$")) {
                boolean oldIsRacing = BO.RNode[i].IsRacing;
                BO.RNode[i].IsRacing = !BO.RNode[i].IsRacing;
                if (!UndoAgent.getUndoLock()) {
                    UndoAgent.UndoFlag = true;
                    UndoAgent.MsgTree.Division.Race(i).IsRacing(Utils.BoolStr(oldIsRacing)); // undoMsg
                    UndoAgent.MsgTree.Division.Race(i).IsRacing(Utils.BoolStr(!oldIsRacing)); // redoMsg
                    UndoAgent.setUndoLock(false);
                }
            }

            if (Utils.StrToIntDef(Value, -1) > -1) {
                Value = EditOTime(cr, Value, i);
            }

            // Fleet Assignment, easy edit
            else if (Value.length() == 1 && IsFleetInputChar(Value.charAt(0))) {
                TEventRaceEntry re = cr.Race[i];
                char c = Value.charAt(0);
                switch (c) {
                case 'y':
                    re.Fleet = 1;
                    break; // yellow
                case 'b':
                    re.Fleet = 2;
                    break; // blue
                case 'r':
                    re.Fleet = 3;
                    break; // red
                case 'g':
                    re.Fleet = 4;
                    break; // green
                case 'm':
                    re.Fleet = 0;
                    break; // medal
                }
            }

            // Fleet Assignment, general method
            else if (Value.length() > 1 && Value.startsWith("F")) {
                TEventRaceEntry re = cr.Race[i];
                re.setRaceValue(Value); // do not broadcast Fleet Assignments
                // cr.Modified = true;
                // Value = re.RaceValue;
            }

            else if (Value.equals("x")) {
                cr.Race[i].IsRacing = false;
                // cr.setModified(true); //use CalcBtn
            }

            else if (Value.equals("-x")) {
                cr.Race[i].IsRacing = true;
                // cr.setModified(true); //use CalcBtn
            }

            else {
                int oldQU = cr.Race[i].getQU();
                String oldQUString = cr.Race[i].getPenalty().toString();

                String undoValue = cr.Race[i].getPenalty().Invert(Value);
                String redoValue = Value;

                cr.Race[i].setRaceValue(Value);

                Value = cr.Race[i].getRaceValue();
                cr.setModified();

                int newQU = cr.Race[i].getQU();
                String newQUString = cr.Race[i].getPenalty().toString();

                if ((oldQU != newQU) || (!oldQUString.equals(newQUString))) {
                    BO.setPenalty(i, cr.getIndex(), cr.Race[i].getPenalty());
                    if (!UndoAgent.getUndoLock()) {
                        UndoAgent.UndoFlag = true;
                        UndoAgent.MsgTree.Division.Race(i).Bib(cr.Bib).RV(undoValue);
                        UndoAgent.MsgTree.Division.Race(i).Bib(cr.Bib).RV(redoValue);
                        UndoAgent.setUndoLock(false);
                    }
                }
            }
        }
        return Value;
    }

    public String EditOTime(TEventRowCollectionItem cr, String Value, int RaceIndex) {
        TEventRowCollection cl;

        if (cr != null) {
            cl = cr.ru().getBaseRowCollection();
        } else {
            return "";
        }

        TEventRaceEntry re = cr.Race[RaceIndex];

        int oldRank;
        int newRank;

        // mode a: direct input, minimal restriction
        if (getRelaxedInputMode()) {
            oldRank = re.OTime;
            newRank = Utils.StrToIntDef(Value, oldRank);
            if ((newRank >= 0) && (newRank <= cl.getCount()) && (newRank != oldRank)) {
                re.OTime = newRank;
//                BO.RNode[RaceIndex].getBaseRowCollection().get(cr.getIndex()).MRank = newRank;
                cr.setModified();
            }

            if (!UndoAgent.getUndoLock()) {
                UndoAgent.UndoFlag = true;
                UndoAgent.MsgTree.Division.Race(RaceIndex).Bib(cr.Bib).RV("" + oldRank);
                UndoAgent.MsgTree.Division.Race(RaceIndex).Bib(cr.Bib).RV("" + newRank);
                UndoAgent.setUndoLock(false);
            }

            Value = Utils.IntToStr(cr.Race[RaceIndex].OTime);
        }

        // mode b: maintain contiguous rank from 1 to max rank
        else {
            oldRank = re.OTime;
            Value = CheckOTime(cl, cr, RaceIndex, Value);
            newRank = re.OTime;
            if (oldRank != newRank) {
//                BO.CopyOTimes(RaceIndex);
                cr.setModified();

                if ((!UndoAgent.getUndoLock()) && (oldRank != newRank)) {
                    UndoAgent.UndoFlag = true;
                    UndoAgent.MsgTree.Division.Race(RaceIndex).Bib(cr.Bib).RV("" + oldRank);
                    UndoAgent.MsgTree.Division.Race(RaceIndex).Bib(cr.Bib).RV("" + newRank);
                    UndoAgent.setUndoLock(false);
                }
            }
        }
        return Value;
    }

    private String CheckOTime(TEventRowCollection cl, TEventRowCollectionItem cr, int r, String Value) {
        if (BO.EventNode.UseFleets) {
            int f = cr.Race[r].Fleet;
            cl.FillFleetList(FL, r, f);
            Value = CheckOTimeForFleet(FL, cr, r, Value);
            FL.clear();
        } else {
            CheckOTimeForAll(cl, cr, r, Value);
        }
        return Value;
    }

    private String CheckOTimeForFleet(List<TEventRowCollectionItem> cl, TEventRowCollectionItem cr, int r,
            String Value) {
        TEventRaceEntry er;

        TEventRowCollectionItem cr1;
        TEventRaceEntry er1;

        int oldRank;
        int newRank;
        int maxRank;

        er = cr.Race[r];
        oldRank = er.OTime;
        newRank = Utils.StrToIntDef(Value, er.OTime);
        maxRank = 0;
        for (int i = 0; i < cl.size(); i++) {
            cr1 = cl.get(i);
            er1 = cr1.Race[r];
            if (cr == cr1)
                continue;
            else if (er1.OTime > 0)
                maxRank++;
        }

        // limit new value
        if (newRank < 0)
            newRank = 0;
        if (newRank > maxRank + 1)
            newRank = maxRank + 1;
        if (newRank > cl.size())
            newRank = cl.size();

        if (oldRank == newRank)
            Value = Utils.IntToStr(er.OTime);
        else {
            for (int i = 0; i < cl.size(); i++) {
                cr1 = cl.get(i);
                er1 = cr1.Race[r];
                if (cr1 == cr)
                    continue;
                int temp = er1.OTime;
                // remove
                if ((oldRank > 0) && (oldRank < temp))
                    er1.OTime = temp - 1;
                // insert
                if ((newRank > 0) && (newRank <= er1.OTime))
                    er1.OTime = er1.OTime + 1;
            }
            cr.Race[r].OTime = newRank;
            Value = Utils.IntToStr(er.OTime);
        }

        return Value;
    }

    private String CheckOTimeForAll(TEventRowCollection cl, TEventRowCollectionItem cr, int r, String Value) {
        TEventRaceEntry er;

        TEventRowCollectionItem cr1;
        TEventRaceEntry er1;

        int oldRank;
        int newRank;
        int maxRank;

        er = cr.Race[r];
        oldRank = er.OTime;
        newRank = Utils.StrToIntDef(Value, er.OTime);
        maxRank = 0;
        for (int i = 0; i < cl.getCount(); i++) {
            cr1 = cl.get(i);
            er1 = cr1.Race[r];
            if (cr == cr1)
                continue;
            else if (er1.OTime > 0)
                maxRank++;
        }

        // limit new value
        if (newRank < 0)
            newRank = 0;
        if (newRank > maxRank + 1)
            newRank = maxRank + 1;
        if (newRank > cl.getCount())
            newRank = cl.getCount();

        if (oldRank == newRank)
            Value = Utils.IntToStr(er.OTime);
        else {
            for (int i = 0; i < cl.getCount(); i++) {
                cr1 = cl.get(i);
                er1 = cr1.Race[r];
                if (cr1 == cr)
                    continue;
                int temp = er1.OTime;
                // remove
                if ((oldRank > 0) && (oldRank < temp))
                    er1.OTime = temp - 1;
                // insert
                if ((newRank > 0) && (newRank <= er1.OTime))
                    er1.OTime = er1.OTime + 1;
            }
            cr.Race[r].OTime = newRank;
            Value = Utils.IntToStr(er.OTime);
        }

        return Value;
    }

    private boolean IsFleetInputChar(char c) {
        switch (c) {
        case 'y':
            return true;
        case 'b':
            return true;
        case 'r':
            return true;
        case 'g':
            return true;
        case 'm':
            return true;
        default:
            return false;
        }
    }

}
