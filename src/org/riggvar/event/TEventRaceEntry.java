package org.riggvar.event;

import org.riggvar.penalty.*;

import java.text.*;
import java.util.*;
import org.riggvar.base.*;

public class TEventRaceEntry extends TBOPersistent {
    private static NumberFormat pointsFormat1;

    private TEventNode FNode;
    private TPenaltyISAF FPenalty = new TPenaltyISAF();
    public int CTime; // Points

    public boolean IsRacing = true;
    public int Fleet;
    public boolean Drop;
    public int DG;
    public int OTime; // ORank
    public int Rank;
    public int PosR;
    public int PLZ;
    public FREnumSet FinishErrors = new FREnumSet(TFinishError.Count);

    public TEventRaceEntry(TEventNode ru) {
        FNode = ru;
        pointsFormat1 = NumberFormat.getInstance(Locale.US);
        pointsFormat1.setMinimumFractionDigits(1);
    }

    public void Clear() {
        IsRacing = true;
        Fleet = 0;
        Drop = false;
        CTime = 0;
        Rank = 0;
        PosR = 0;
        FPenalty.Clear();
        PLZ = 0;
        OTime = 0;
        FinishErrors.Clear();
    }

    @Override
    public void Assign(Object source) {
        if (source instanceof TEventRaceEntry) {
            TEventRaceEntry e = (TEventRaceEntry) source;
            //
            IsRacing = e.IsRacing;
            Fleet = e.Fleet;
            Drop = e.Drop;
            FPenalty.Assign(e.getPenalty()); // QU := e.QU;
            DG = e.DG;
            OTime = e.OTime;
            CTime = e.CTime;
            Rank = e.Rank;
            PosR = e.PosR;
            PLZ = e.PLZ;
        } else {
            super.Assign(source);
        }
    }

    public String getRaceValue() {
        String sPoints;
        switch (getLayout()) {
        case 0:
            sPoints = getPoints(); // Default
            break;
        case 1:
            sPoints = Integer.toString(OTime); // FinishPos
            break;
        case 2:
            sPoints = getPoints(); // Points
            break;
        // case 3:
        // sPoints = QU.ToString();
        // break;
        // case 4:
        // sPoints = Rank.ToString();
        // break;
        // case 5:
        // sPoints = PosR.ToString();
        // break;
        // case 6:
        // sPoints = PLZ.ToString();
        // break;
        default:
            sPoints = Integer.toString(CTime);
            break;
        }
        String result = sPoints;

        String sQU = FPenalty.toString();
        if (sQU != "") {
            result = result + "/" + sQU;

            // { Parenthesis, runde Klammer }
            // if ((CTime != OTime) && (Ttime l!= 0))
            // result = result + " (" + OTime.ToString() + ")";

            // { Bracket, eckige Klammer }
        }
        if (Drop) {
            result = "[" + result + "]";

        }
        if (!IsRacing)
            result = '(' + result + ')';

        return result;
    }

    public void setRaceValue(String value) {
        /*
         * use special value 0 to delete a FinishPosition, instead of -1, so that the
         * sum of points is not affected
         */
        if (value.equals("0")) {
            OTime = 0;
        } else if (Utils.StrToIntDef(value, -1) > 0) {
            OTime = Utils.StrToInt(value);
        } else if (value.length() > 1 && value.startsWith("F")) {
            Fleet = ParseFleet(value);
        } else if (value.equals("x"))
            IsRacing = false;
        else if (value.equals("-x"))
            IsRacing = true;
        else {
            FPenalty.Parse(value);
        }
    }

    public int getQU() {
        return FPenalty.getAsInteger();
    }

    public void setQU(int value) {
        FPenalty.setAsInteger(value);
    }

    public TPenaltyISAF getPenalty() {
        return FPenalty;
    }

    public String getPoints() {
        return Double.toString(getCPoints());
    }

    public String getDecimalPoints() {
        return pointsFormat1.format(getCPoints());
    }

    public double getCPoints() {
        return (double) CTime / 100; // Round(CTime / 100);
    }

    public void setCPoints(double value) {
        // CTime = System.Convert.ToInt32(value*100); //Convert.ToInt32(value * 100);
        double d = value * 100;
        float f = (float) d;
        CTime = Math.round(f);
    }

    public int getCTime1() {
        return CTime / 100; // CTime div 100;
    }

    public void setCTime1(int value) {
        CTime = value * 100;
    }

    public int getLayout() {
        if (FNode != null) {
            return FNode.getShowPoints();
        } else {
            return 0;
        }
    }

    public int ParseFleet(String value) {
        if (value.length() >= 2 && value.startsWith("F")) {
            String s = value.toUpperCase().substring(1);
            char c = s.charAt(0);
            switch (c) {
            case 'Y':
                return 1;
            case 'B':
                return 2;
            case 'R':
                return 3;
            case 'G':
                return 4;
            case 'M':
                return 0;
            default:
                int i = Utils.StrToIntDef(s, Fleet);
                if (i < 0)
                    i = Fleet;
                return i;
            }
        }
        return Fleet;
    }

}
