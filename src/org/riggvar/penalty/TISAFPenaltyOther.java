package org.riggvar.penalty;

/**
 * enum TPenaltyISAFOther.
 */
public class TISAFPenaltyOther {
    public static final int TIM = 0; // time limit
    public static final int ZFP = 1;
    public static final int AVG = 2; // average
    public static final int SCP = 3; // scoring penalty, pct (percent) of finish position
    public static final int RDG = 4; // redress given
    public static final int MAN = 5; // manual
    public static final int CNF = 6; // check-in failure
    public static final int TMP = 7; // scoring time penalty, pct (percent) of time
    public static final int DPI = 8; // descretionary penalty imposed

    public static final int Count = 8;

    public static String getString(int o) {
        switch (o) {
        case TISAFPenaltyOther.TIM:
            return "TIM";
        case TISAFPenaltyOther.ZFP:
            return "ZFP";
        case TISAFPenaltyOther.AVG:
            return "AVG";
        case TISAFPenaltyOther.SCP:
            return "SCP";
        case TISAFPenaltyOther.RDG:
            return "RDG";
        case TISAFPenaltyOther.MAN:
            return "MAN";
        case TISAFPenaltyOther.CNF:
            return "CNF";
        case TISAFPenaltyOther.TMP:
            return "TMP";
        case TISAFPenaltyOther.DPI:
            return "DPI";
        }
        return "";
    }

    public static int getBits(int o) {
        switch (o) {
        case TISAFPenaltyOther.TIM:
            return 0x0010;
        case TISAFPenaltyOther.ZFP:
            return 0x0020;
        case TISAFPenaltyOther.AVG:
            return 0x0040;
        case TISAFPenaltyOther.SCP:
            return 0x0080;
        case TISAFPenaltyOther.RDG:
            return 0x0100;
        case TISAFPenaltyOther.MAN:
            return 0x0200;
        case TISAFPenaltyOther.CNF:
            return 0x0400;
        case TISAFPenaltyOther.TMP:
            return 0x0800;
        case TISAFPenaltyOther.DPI:
            return 0x1000;
        }
        return 0;
    }

}
