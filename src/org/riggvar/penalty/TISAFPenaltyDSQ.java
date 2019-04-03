package org.riggvar.penalty;

public class TISAFPenaltyDSQ {
    public static final int NoDSQ = 0;
    public static final int DSQ = 1;
    public static final int DNE = 2;
    public static final int RAF = 3;
    public static final int OCS = 4;
    public static final int BFD = 5;
    public static final int DGM = 6;

    public static final int Count = 7;

    public static String getString(int e) {
        switch (e) {
        case TISAFPenaltyDSQ.NoDSQ:
            return "";
        case TISAFPenaltyDSQ.DSQ:
            return "DSQ";
        case TISAFPenaltyDSQ.DNE:
            return "DNE";
        case TISAFPenaltyDSQ.RAF:
            return "RAF";
        case TISAFPenaltyDSQ.OCS:
            return "OCS";
        case TISAFPenaltyDSQ.BFD:
            return "BFD";
        case TISAFPenaltyDSQ.DGM:
            return "DGM";
        }
        return "";
    }

    public static int getBits(int o) {
        switch (o) {
        case TISAFPenaltyDSQ.NoDSQ:
            return 0;
        case TISAFPenaltyDSQ.DSQ:
            return 0x0001;
        case TISAFPenaltyDSQ.DNE:
            return 0x0002;
        case TISAFPenaltyDSQ.RAF:
            return 0x0003;
        case TISAFPenaltyDSQ.OCS:
            return 0x0004;
        case TISAFPenaltyDSQ.BFD:
            return 0x0005;
        case TISAFPenaltyDSQ.DGM:
            return 0x0006;
        }
        return 0;
    }

}
