package org.riggvar.penalty;

public class TISAFPenaltyNoFinish {
    public static final int NoFinishBlank = 0;
    public static final int TLE = 1;
    public static final int DNF = 2;
    public static final int DNS = 3;
    public static final int DNC = 4;

    // public static final int NoFinish = 5;

    public static final int Count = 5;

    public static String getString(int o) {
        switch (o) {
        case TISAFPenaltyNoFinish.NoFinishBlank:
            return "";
        case TISAFPenaltyNoFinish.TLE:
            return "TLE";
        case TISAFPenaltyNoFinish.DNF:
            return "DNF";
        case TISAFPenaltyNoFinish.DNS:
            return "DNS";
        case TISAFPenaltyNoFinish.DNC:
            return "DNC";
        // case TISAFPenaltyNoFinish.NoFinish: return "NOFINISH";
        }
        return "";
    }

    public static int getBits(int o) {
        switch (o) {
        case TISAFPenaltyNoFinish.NoFinishBlank:
            return 0;
        case TISAFPenaltyNoFinish.TLE:
            return 0x6000;
        case TISAFPenaltyNoFinish.DNF:
            return 0x8000;
        case TISAFPenaltyNoFinish.DNS:
            return 0xA000;
        case TISAFPenaltyNoFinish.DNC:
            return 0xC000;
        // case TISAFPenaltyNoFinish.NoFinish: return 0xE000;
        }
        return 0;
    }

}
