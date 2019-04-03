package org.riggvar.scoring;

public class TDateTime {
    public TDateTime() {
    }

    public TDateTime(long ticks) {
    }

    public static TDateTime Now() {
        return new TDateTime();
    }

    public int Year() {
        return 0;
    }

    public int Month() {
        return 0;
    }

    public int Day() {
        return 0;
    }

    public int Hour() {
        return 0;
    }

    public int Minute() {
        return 0;
    }

    public int Second() {
        return 0;
    }

    public int SubSecond() {
        return 0;
    }

    public static long Millies() {
        return 0;
    }

    public long Ticks() {
        return 0;
    }

    public String ToString(String format) {
        return "";
    }

    public TDateTime FromMillies(long millies) {
        // 621355968000000000 == TickCount(1.Januar 1970);
        long tempTicks = millies * 10000 + 621355968000000000L;
        return new TDateTime(tempTicks);
    }

    public long ToMillies(TDateTime d) {
        return (d.Ticks() - 621355968000000000L) / 10000;
    }

}
