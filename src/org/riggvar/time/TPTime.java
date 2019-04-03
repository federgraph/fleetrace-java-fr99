package org.riggvar.time;

/**
 * Positive Time
 */
public class TPTime extends TNTime {
    @Override
    public int getAsInteger() {
        if (FStatus == TTimeStatus.tsNone) {
            return -1;
        } else {
            return FTime;
        }
    }

    @Override
    public void setAsInteger(int value) {
        if ((value == TimeConst.TimeNULL) || (value < 0)) {
            setStatus(TTimeStatus.tsNone);
            FTime = 0;
        } else {
            setStatus(TTimeStatus.tsAuto);
            FTime = value;
        }

    }

    @Override
    public String toString() {
        if (FTime < 0) {
            return "";
        } else if ((FTime == 0) && (!this.isTimePresent())) {
            return "";
        } else if (FTime == 0) {
            switch (getDisplayPrecision()) {
            case 1:
                return "0.0";
            case 2:
                return "0.00";
            case 3:
                return "0.000";
            case 4:
                return "0.0000";
            default:
                return "0";
            }
        } else {
            return ConvertTimeToStr3(FTime);
        }
    }

    public void SetPenalty(int PenaltyTime) {
        if (PenaltyTime < 100) {
            PenaltyTime = 100;
        }
        if (PenaltyTime > 595900) {
            PenaltyTime = 595900;
        }
        setStatus(TTimeStatus.tsPenalty);
        FTime = PenaltyTime;
    }
}
