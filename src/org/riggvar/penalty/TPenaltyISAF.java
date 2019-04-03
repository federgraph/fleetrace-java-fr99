package org.riggvar.penalty;

import org.riggvar.base.*;

/**
 * TPenaltyISAF translates TPenalty to javascore.Penalty and back
 */
public class TPenaltyISAF extends TPenalty {
    /*
     * General:
     *
     * Penalty values are/should be used only internally to the program and NOT
     * written out to persistent storage (that is done by string name). Therefore it
     * should be safe to reset the values, provided the orders are not changed and
     * the types of penalties keep their bit-boundaries straight.
     */

    /*
     * DSQ Penalties:
     *
     * Disqualification penalties are the various ways a boat can be disqualified.
     * These are not bitwise, as a boat can only be disqualified once. But a boat
     * can be disqualified with or without a valid finish. So a boat can carry both
     * a non-finish penalty and a disqualification penalty. See Penalty class.
     */
    public static final int ISAF_DSQ = 0x0001; // Disqualification
    public static final int ISAF_DNE = 0x0002; // Disqualification not excludable under rule 88.3(b)
    public static final int ISAF_RAF = 0x0003; // Retired after finishing
    public static final int ISAF_OCS = 0x0004; // Did not start; on the course side of the starting line and broke rule
                                               // 29.1 or 30.1
    public static final int ISAF_BFD = 0x0005; // Disqualification under rule 30.3 - black flag
    public static final int ISAF_DGM = 0x0006; //

    /*
     * Other scoring penalties:
     *
     * are the various other ways a boat can be "dinged". This includes check-in
     * penalties, redress, percentage penalties, etc. These ARE Bit-wise penalties,
     * and a boat can have more than one of them. Also, a boat CAN have a non-finish
     * penalty and an other penalty, but a boat may not have a disqualification
     * penalty and "other" penalty. See Penalty class.
     */

    // public const int available = 0x0008;
    public static final int ISAF_TIM = 0x0010; // time limit
    public static final int ISAF_ZFP = 0x0020; // 20% penalty under rule 30. 2
    public static final int ISAF_AVG = 0x0040; // average
    public static final int ISAF_SCP = 0x0080; // Took a scoring penalty under rule 44.3
    public static final int ISAF_RDG = 0x0100; // Redress given
    public static final int ISAF_MAN = 0x0200; // manual
    public static final int ISAF_CNF = 0x0400; // check-in failure
    public static final int ISAF_TMP = 0x0800; // scoring time penalty, pct (percent) of time
    public static final int ISAF_DPI = 0x1000; // descretionary penalty imposed

    // highest possible real finish
    public static final int ISAF_HIGHEST_FINISH = 0x1FFF; // 8191

    /*
     * Non-finishing penalties:
     *
     * show up in the finish order column and can get set as Finish "Positions" -
     * means no finish recorded yet. Non-Finish penalty values are for boats that do
     * not have a valid Finish. these are used in BOTH the FinishPosition class and
     * in the Penalty class These are not-bitwise penalties and a boat cannot have
     * more than one of these at a time. See FinishPosition class
     */

    // available = 0x2000; //8192
    // = 0010000000000000
    // available = 0x4000;
    // = 0100000000000000
    public static final int ISAF_TLE = 0x6000; // an amount of time applied to elapsed time?

    // = 0110000000000000
    public static final int ISAF_DNF = 0x8000; // Did not finish

    // = 0100000000000000
    public static final int ISAF_DNS = 0xA000; // Did not start (other than DNC and OCS)

    // = 1010000000000000
    public static final int ISAF_DNC = 0xC000; // Did not start; did not come to the starting area

    // = 1100000000000000
    public static final int ISAF_NOFINISH = 0xE000; //

    // = 1110000000000000 //57344

    // These masks break up the integer into the portions reserved for each penalty
    // type.
    public static final int ISAF_NO_PENALTY = 0x0000;
    public static final int ISAF_DSQ_MASK = 0x0007;
    public static final int ISAF_OTHER_MASK = 0x1FF8;
    public static final int ISAF_NOFINISH_MASK = 0xE000; // 57344

    public int PenaltyDSQ; // enum TISAFPenaltyDSQ
    public int PenaltyNoFinish; // enum TISAFPenaltyNoFinish
    public FREnumSet PenaltyOther = new FREnumSet(TISAFPenaltyOther.Count);

    public boolean DSQPending;

    public double Points;
    public int Percent;
    public long TimePenalty;
    public String Note;
    public static TStringList SLPenalty = new TStringList();

    public TPenaltyISAF() {
        SLPenalty.setQuoteChar('#');
    }

    @Override
    public void Assign(Object source) {
        if (source.equals(this)) {
            return;
        }
        if (source instanceof TPenaltyISAF) {
            TPenaltyISAF o = (TPenaltyISAF) source;
            PenaltyDSQ = o.PenaltyDSQ;
            PenaltyNoFinish = o.PenaltyNoFinish;
            PenaltyOther.Assign(o.PenaltyOther);
            DSQPending = o.DSQPending;
            //
            Points = o.Points;
            Percent = o.Percent;
            TimePenalty = o.TimePenalty;
            Note = o.Note;
        } else {
            super.Assign(source);
        }
    }

    @Override
    public void Clear() {
        PenaltyDSQ = TISAFPenaltyDSQ.NoDSQ;
        PenaltyNoFinish = TISAFPenaltyNoFinish.NoFinishBlank;
        PenaltyOther.Clear();
        DSQPending = false;
        //
        Points = 0;
        Percent = 0;
        TimePenalty = 0;
        Note = "";
    }

    @Override
    public String toString() {
        int po; // TPenaltyOther po
        String s;
        boolean showPts = true;

        SLPenalty.Clear();

        if (PenaltyDSQ != TISAFPenaltyDSQ.NoDSQ) {
            SLPenalty.Add(TISAFPenaltyDSQ.getString(PenaltyDSQ));
        }
        if (PenaltyNoFinish != TISAFPenaltyNoFinish.NoFinishBlank) {
            SLPenalty.Add(TISAFPenaltyNoFinish.getString(PenaltyNoFinish));
        }
        for (int i = PenaltyOther.Low(); i <= PenaltyOther.High(); i++) {
            po = i; // (TISAFPenaltyOther) i;
            if (PenaltyOther.IsMember(i)) {
                s = TISAFPenaltyOther.getString(po);
                if ((po == TISAFPenaltyOther.MAN) && showPts) {
                    s += '/' + Utils.FloatToStr(Points);
                } else if ((po == TISAFPenaltyOther.RDG) && showPts) {
                    s += '/' + Utils.FloatToStr(Points);
                } else if ((po == TISAFPenaltyOther.DPI) && showPts) {
                    s += '/' + Utils.FloatToStr(Points);
                } else if ((po == TISAFPenaltyOther.TIM) && showPts) {
                    s += '/' + org.riggvar.js08.SailTime.toString(TimePenalty);
                } else if ((po == TISAFPenaltyOther.TMP) && showPts) {
                    s += '/' + Utils.IntToStr(Percent) + "%";
                }
                SLPenalty.Add(s);
            }
        }
        if (SLPenalty.getCount() > 0) {
            return SLPenalty.getDelimitedText();
        } else {
            return "";
        }
    }

    @Override
    public boolean FromString(String Value) {
        boolean result = true;
        Value = Value.replace('"', '#');
        SLPenalty.setDelimitedText(Value);
        for (int i = 0; i < SLPenalty.getCount(); i++) {
            result = result && Parse(SLPenalty.getString(i));
        }
        return result;
    }

    public String Invert(String Value) {
        String result = "ok";
        if (Value == null)
            return result;

        String pen = Value.toLowerCase();
        if (pen.equals(""))
            return result;
        if (pen.length() < 1)
            return result;

        if (Value.charAt(0) == '-') {
            if (pen.equals("-dsq"))
                result = TISAFPenaltyDSQ.getString(PenaltyDSQ);
            else if (pen.equals("-f"))
                result = TISAFPenaltyNoFinish.getString(PenaltyNoFinish);
            else if (pen.equals("-tim"))
                result = TISAFPenaltyOther.getString(TISAFPenaltyOther.TIM);
            else if (pen.equals("-zfp"))
                result = TISAFPenaltyOther.getString(TISAFPenaltyOther.ZFP);
            else if (pen.equals("-avg"))
                result = TISAFPenaltyOther.getString(TISAFPenaltyOther.AVG);
            else if (pen.equals("-scp"))
                result = TISAFPenaltyOther.getString(TISAFPenaltyOther.SCP);
            else if (pen.equals("-rdg"))
                result = TISAFPenaltyOther.getString(TISAFPenaltyOther.RDG);
            else if (pen.equals("-man"))
                result = TISAFPenaltyOther.getString(TISAFPenaltyOther.MAN);
            else if (pen.equals("-cnf"))
                result = TISAFPenaltyOther.getString(TISAFPenaltyOther.CNF);
            else if (pen.equals("-tmp"))
                result = TISAFPenaltyOther.getString(TISAFPenaltyOther.TMP);
            else if (pen.equals("-dpi"))
                result = TISAFPenaltyOther.getString(TISAFPenaltyOther.DPI);
        } else {
            pen = Utils.Copy(pen, 1, 3);

            // disqualification penalty enum
            if (pen.equals("dsq"))
                result = "-dsq";
            else if (pen.equals("dne"))
                result = "-dsq";
            else if (pen.equals("raf"))
                result = "-dsq";
            else if (pen.equals("ocs"))
                result = "-dsq";
            else if (pen.equals("bfd"))
                result = "-dsq";
            else if (pen.equals("dgm"))
                result = "-dsq";

            // nofinish penalty enum
            else if (pen.equals("tle"))
                result = "-f";
            else if (pen.equals("dnf"))
                result = "-f";
            else if (pen.equals("dns"))
                result = "-f";
            else if (pen.equals("dnc"))
                result = "-f";

            // other (penalties set
            else if (pen.equals("tim"))
                result = "-tim";
            else if (pen.equals("zfp"))
                result = "-zfp";
            else if (pen.equals("avg"))
                result = "-avg";
            else if (pen.equals("scp"))
                result = "-scp";
            else if (pen.equals("rdg"))
                result = "-rdg";
            else if (pen.equals("man"))
                result = "-man";
            else if (pen.equals("cnf"))
                result = "-cnf";
            else if (pen.equals("tmp"))
                result = "-tmp";
            else if (pen.equals("dpi"))
                result = "-dpi";
        }

        return result;
    }

    @Override
    public boolean Parse(String Value) {
        boolean result = true;

        String val = "";
        String s = Value.toLowerCase();
        String pen = s;

        int i = Utils.Pos("/", s);
        if (i > 0) {
            pen = Utils.Copy(s, 1, i - 1).trim();
            val = Utils.Copy(s, i + 1, s.length()).trim();
        }

        if (pen.equals("")) {
            return result;
        }

        else if (pen.equals("ok")) {
            Clear();

        } else if (pen.equals("*")) {
            DSQPending = true;

            // disqualification penalty enum
        } else if (pen.equals("-dsq")) {
            PenaltyDSQ = TISAFPenaltyDSQ.NoDSQ;
        } else if (pen.equals("dsq")) {
            PenaltyDSQ = TISAFPenaltyDSQ.DSQ;
        } else if (pen.equals("dne")) {
            PenaltyDSQ = TISAFPenaltyDSQ.DNE;
        } else if (pen.equals("raf")) {
            PenaltyDSQ = TISAFPenaltyDSQ.RAF;
        } else if (pen.equals("ocs")) {
            PenaltyDSQ = TISAFPenaltyDSQ.OCS;
        } else if (pen.equals("bfd")) {
            PenaltyDSQ = TISAFPenaltyDSQ.BFD;
        } else if (pen.equals("dgm")) {
            PenaltyDSQ = TISAFPenaltyDSQ.DGM;
        }

        // nofinish
        else if (pen.equals("-f")) {
            PenaltyNoFinish = TISAFPenaltyNoFinish.NoFinishBlank;
        } else if (pen.equals("tle")) {
            PenaltyNoFinish = TISAFPenaltyNoFinish.TLE;
        } else if (pen.equals("dnf")) {
            PenaltyNoFinish = TISAFPenaltyNoFinish.DNF;
        } else if (pen.equals("dns")) {
            PenaltyNoFinish = TISAFPenaltyNoFinish.DNS;
        } else if (pen.equals("dnc")) {
            PenaltyNoFinish = TISAFPenaltyNoFinish.DNC;

            // other penalties set
            // else if (pen.equals("tim")) PenaltyOther.Include(TIM);
        } else if (pen.equals("zfp")) {
            PenaltyOther.Include(TISAFPenaltyOther.ZFP);
        } else if (pen.equals("avg")) {
            PenaltyOther.Include(TISAFPenaltyOther.AVG);
            // else if (pen.equals("scp")) PenaltyOther.Include((int)TISAFPenaltyOther.SCP);
            // else if (pen.equals("rdg")) PenaltyOther.Include((int)TISAFPenaltyOther.RDG);
            // else if (pen.equals("man")) PenaltyOther.Include((int)TISAFPenaltyOther.MAN);
        } else if (pen.equals("cnf")) {
            PenaltyOther.Include(TISAFPenaltyOther.CNF);
            // else if (pen.equals("tmp")) PenaltyOther.Include((int)TISAFPenaltyOther.TMP);

        } else if (pen.equals("-tim")) {
            PenaltyOther.Exclude(TISAFPenaltyOther.TIM);
        } else if (pen.equals("-zfp")) {
            PenaltyOther.Exclude(TISAFPenaltyOther.ZFP);
        } else if (pen.equals("-avg")) {
            PenaltyOther.Exclude(TISAFPenaltyOther.AVG);
        } else if (pen.equals("-scp")) {
            PenaltyOther.Exclude(TISAFPenaltyOther.SCP);
        } else if (pen.equals("-rdg")) {
            PenaltyOther.Exclude(TISAFPenaltyOther.RDG);
        } else if (pen.equals("-man")) {
            PenaltyOther.Exclude(TISAFPenaltyOther.MAN);
        } else if (pen.equals("-cnf")) {
            PenaltyOther.Exclude(TISAFPenaltyOther.CNF);
        } else if (pen.equals("-tmp")) {
            PenaltyOther.Exclude(TISAFPenaltyOther.TMP);
            // all of the rest should have <pen>/<number>
        } else if (pen.equals("-dpi")) {
            PenaltyOther.Exclude(TISAFPenaltyOther.DPI);
        }

        else if (pen.charAt(pen.length() - 1) == '%') {
            PenaltyOther.Include(TISAFPenaltyOther.SCP);
            Percent = Utils.StrToIntDef(Utils.Copy(pen, 1, pen.length() - 1), 0);
        }

        else if (pen.charAt(0) == 'p') {
            PenaltyOther.Include(TISAFPenaltyOther.SCP);
            Percent = Utils.StrToIntDef(Utils.Copy(pen, 2, pen.length()), 0);
        }

        else if (pen.equals("tim")) {
            PenaltyOther.Include(TISAFPenaltyOther.TIM);
            TimePenalty = org.riggvar.js08.SailTime.forceToLong(val);
        }

        else if ((pen.equals("rdg")) || (pen.equals("rdr")) || (pen.equals("man")) || (pen.equals("dpi"))) {
            if (Utils.Copy(pen, 1, 3).equals("man")) {
                PenaltyOther.Include(TISAFPenaltyOther.MAN);
            } else if (Utils.Copy(pen, 1, 3).equals("dpi")) {
                PenaltyOther.Include(TISAFPenaltyOther.DPI);
            } else {
                PenaltyOther.Include(TISAFPenaltyOther.RDG);
                // assume is form "MAN/<pts>"
            }
            Points = Utils.StrToFloatDef(val, 0);
        }

        else if ((pen.equals("tmp")) || (pen.equals("scp")) || (pen.equals("pct"))) {
            if (Utils.Copy(pen, 1, 3).equals("tmp")) {
                PenaltyOther.Include((int) TISAFPenaltyOther.TMP);
            } else {
                PenaltyOther.Include((int) TISAFPenaltyOther.SCP);
                // assume is form "MAN/<pts>"
            }
            Percent = Utils.StrToIntDef(val, 0);
        }

        return result;
    }

    @Override
    public boolean getIsOK() {
        return (PenaltyDSQ == TISAFPenaltyDSQ.NoDSQ) && (PenaltyNoFinish == TISAFPenaltyNoFinish.NoFinishBlank)
                && (PenaltyOther.IsEmpty());
        // && (FDSQPending == false);
    }

    @Override
    public boolean getIsOut() {
        return !getIsOK();
    }

    @Override
    public int getAsInteger() {
        int result = 0;

        switch (PenaltyDSQ) {
        case TISAFPenaltyDSQ.DSQ:
            result = result | ISAF_DSQ;
            break; // $0001; //Disqualification
        case TISAFPenaltyDSQ.DNE:
            result = result | ISAF_DNE;
            break; // $0002; //Disqualification not excludable under rule 88.3(b)
        case TISAFPenaltyDSQ.RAF:
            result = result | ISAF_RAF;
            break; // $0003; //Retired after finishing
        case TISAFPenaltyDSQ.OCS:
            result = result | ISAF_OCS;
            break; // $0004; //Did not start; on the course side of the starting line and broke
                   // rule 29.1 or 30.1
        case TISAFPenaltyDSQ.BFD:
            result = result | ISAF_BFD;
            break; // $0005; //Disqualification under rule 30.3 - black flag
        case TISAFPenaltyDSQ.DGM:
            result = result | ISAF_DGM;
            break; // $0006;
        }
        switch (PenaltyNoFinish) {
        case TISAFPenaltyNoFinish.TLE:
            result = result | ISAF_TLE;
            break; // $6000; //an amount of time applied to elapsed time?
        case TISAFPenaltyNoFinish.DNF:
            result = result | ISAF_DNF;
            break; // $8000; //Did not finish
        case TISAFPenaltyNoFinish.DNS:
            result = result | ISAF_DNS;
            break; // $A000; //Did not start (other than DNC and OCS)
        case TISAFPenaltyNoFinish.DNC:
            result = result | ISAF_DNC;
            break; // $C000; //Did not start; did not come to the starting area
        case TISAFPenaltyNoFinish.NoFinishBlank:

            // result = result;
            break;
        }
        for (int i = PenaltyOther.Low(); i <= PenaltyOther.High(); i++) {
            if (PenaltyOther.IsMember(i)) {
                int other = i; // (TISAFPenaltyOther) i;
                switch (other) {
                case TISAFPenaltyOther.TIM:
                    result = result | ISAF_TIM;
                    break; // $0010; //time limit
                case TISAFPenaltyOther.ZFP:
                    result = result | ISAF_ZFP;
                    break; // $0020; //20% penalty under rule 30. 2
                case TISAFPenaltyOther.AVG:
                    result = result | ISAF_AVG;
                    break; // $0040; //average
                case TISAFPenaltyOther.SCP:
                    result = result | ISAF_SCP;
                    break; // $0080; //Took a scoring penalty under rule 44.3
                case TISAFPenaltyOther.RDG:
                    result = result | ISAF_RDG;
                    break; // $0100; //Redress given
                case TISAFPenaltyOther.MAN:
                    result = result | ISAF_MAN;
                    break; // $0200; //manual
                case TISAFPenaltyOther.CNF:
                    result = result | ISAF_CNF;
                    break; // $0400; //check-in failure
                case TISAFPenaltyOther.TMP:
                    result = result | ISAF_TMP;
                    break; // $0800; //scoring time penalty, pct (percent) of time
                case TISAFPenaltyOther.DPI:
                    result = result | ISAF_DPI;
                    break; // $1000; //descretionary penalty imposed
                }
            }
        }

        return result;
    }

    @Override
    public void setAsInteger(int Value) {
        Clear();
        if (Value == ISAF_NO_PENALTY) {
            return;
        }

        int i = Value & ISAF_DSQ_MASK;
        switch (i) {
        case ISAF_DSQ:
            PenaltyDSQ = TISAFPenaltyDSQ.DSQ;
            break;
        case ISAF_DNE:
            PenaltyDSQ = TISAFPenaltyDSQ.DNE;
            break;
        case ISAF_RAF:
            PenaltyDSQ = TISAFPenaltyDSQ.RAF;
            break;
        case ISAF_OCS:
            PenaltyDSQ = TISAFPenaltyDSQ.OCS;
            break;
        case ISAF_BFD:
            PenaltyDSQ = TISAFPenaltyDSQ.BFD;
            break;
        case ISAF_DGM:
            PenaltyDSQ = TISAFPenaltyDSQ.DGM;
            break;
        }

        i = Value & ISAF_NOFINISH_MASK;
        switch (i) {
        case ISAF_TLE:
            PenaltyNoFinish = TISAFPenaltyNoFinish.TLE;
            break;
        case ISAF_DNF:
            PenaltyNoFinish = TISAFPenaltyNoFinish.DNF;
            break;
        case ISAF_DNS:
            PenaltyNoFinish = TISAFPenaltyNoFinish.DNS;
            break;
        case ISAF_DNC:
            PenaltyNoFinish = TISAFPenaltyNoFinish.DNC;
            break;
        default:
            PenaltyNoFinish = TISAFPenaltyNoFinish.NoFinishBlank;
            break;
        }

        i = Value & ISAF_OTHER_MASK;
        if ((i & ISAF_TIM) == ISAF_TIM) {
            PenaltyOther.Include((int) TISAFPenaltyOther.TIM);
        }
        if ((i & ISAF_ZFP) == ISAF_ZFP) {
            PenaltyOther.Include((int) TISAFPenaltyOther.ZFP);
        }
        if ((i & ISAF_AVG) == ISAF_AVG) {
            PenaltyOther.Include((int) TISAFPenaltyOther.AVG);
        }
        if ((i & ISAF_SCP) == ISAF_SCP) {
            PenaltyOther.Include((int) TISAFPenaltyOther.SCP);
        }
        if ((i & ISAF_RDG) == ISAF_RDG) {
            PenaltyOther.Include((int) TISAFPenaltyOther.RDG);
        }
        if ((i & ISAF_MAN) == ISAF_MAN) {
            PenaltyOther.Include((int) TISAFPenaltyOther.MAN);
        }
        if ((i & ISAF_CNF) == ISAF_CNF) {
            PenaltyOther.Include((int) TISAFPenaltyOther.CNF);
        }
        if ((i & ISAF_TMP) == ISAF_TMP) {
            PenaltyOther.Include((int) TISAFPenaltyOther.TMP);
        }
        if ((i & ISAF_DPI) == ISAF_DPI) {
            PenaltyOther.Include((int) TISAFPenaltyOther.DPI);
        }
    }

}
