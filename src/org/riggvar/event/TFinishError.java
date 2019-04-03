package org.riggvar.event;

public class TFinishError {
    public static final int error_OutOfRange_OTime_Min = 0;
    public static final int error_OutOfRange_OTime_Max = 1;
    public static final int error_Duplicate_OTime = 2;
    public static final int error_Contiguous_OTime = 3;

    public static final int Count = 4;

    public static String getString(int e) {
        switch (e) {
        case TFinishError.error_Duplicate_OTime:
            return "duplicate OTime";
        case TFinishError.error_OutOfRange_OTime_Max:
            return "OTime beyond EntryCount";
        case TFinishError.error_OutOfRange_OTime_Min:
            return "OTime below zero";
        case TFinishError.error_Contiguous_OTime:
            return "OTime gap";
        }
        return "";
    }
}
