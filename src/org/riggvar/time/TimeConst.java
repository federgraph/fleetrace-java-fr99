package org.riggvar.time;

public class TimeConst {
    public static final int cRun1 = 1;
    public static final int cRun2 = 2;
    public static final int cRun3 = 3;
    public static final int eaDSQGate = 1;
    public static final int eaStatus = 2;
    public static final int eaOTime = 3;

    public static final int TimeStatus_None = 0; // noch keine Zeit da, kann aber noch kommen
    public static final int TimeStatus_Auto = 1; // Zeit vorhanden, zuletzt automatisch gesetzt
    public static final int TimeStatus_Man = 2; // Zeit vorhanden, zuletzt von Hand gesetzt
    public static final int TimeStatus_TimePresent = 3; // Zeit vorhanden
    public static final int TimeStatus_Penalty = 4; // Penaltytime wurde automatisch eingetragen

    // enum TTimeStatus
    public static final int tsNone = 0;
    public static final int tsAuto = 1;
    public static final int tsMan = 2;
    public static final int tsTimePresent = 3;
    public static final int tsPenalty = 4;

    public static final int TimeNULL = Integer.MAX_VALUE;

    public static final int channel_QA_ST = 0;
    public static final int channel_QA_IT = 1;
    public static final int channel_QA_FT = 2;
    public static final int channel_QB_ST = 3;
    public static final int channel_QB_IT = 4;
    public static final int channel_QB_FT = 5;
    public static final int channel_QC_ST = 6;
    public static final int channel_QC_IT = 7;
    public static final int channel_QC_FT = 8;

    public static String TimeStatusStrings(int status) {
        switch (status) {
        case TimeStatus_None:
            return "";
        case TimeStatus_Auto:
            return "Auto";
        case TimeStatus_Man:
            return "Man";
        case TimeStatus_TimePresent:
            return "Time";
        case TimeStatus_Penalty:
            return "Pen";

        // case tsNone: return "";
        // case tsAuto: return "Auto";
        // case tsMan: return "Man";
        // case tsTimePresent: return "Time";
        // case tsPenalty: return "Pen";
        }
        return "";
    }
}
