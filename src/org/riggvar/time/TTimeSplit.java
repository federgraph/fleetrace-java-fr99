package org.riggvar.time;

import java.text.*;
import org.riggvar.base.*;

public class TTimeSplit {
    private boolean minus;
    private int h;
    private int m;
    private int s;
    private int ss;

//    private String sSign;
//    private String sHour;
//    private String sMin;
//    private String sSec;
//    private String sSubSec;

    public int Value;

    private String FormatNumber2(int aNumber) {
        if (aNumber < 10) {
            return "0" + Utils.IntToStr(aNumber);
        } else {
            return Utils.IntToStr(aNumber);
        }
    }

    private String FormatNumber4(int aNumber) {
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(4);
        return nf.format(aNumber);
    }

    public void Split() {
        int TimeVal = Value;
        minus = false;
        if (TimeVal < 0) {
            minus = true;
            TimeVal = Math.abs(TimeVal);
        }
        ss = TimeVal % 10000;
        TimeVal = TimeVal / 10000;
        s = TimeVal % 60;
        TimeVal = TimeVal / 60;
        m = TimeVal % 60;
        TimeVal = TimeVal / 60;
        h = TimeVal;
    }

//    public void SplitToStrings()
//    {
//        Split();
//
//
//        if (minus)
//        {
//            sSign = "M";
//        }
//        else
//        {
//            sSign = "P";
//
//        }
//        sHour = FormatNumber2(h);
//        sMin = FormatNumber2(m);
//        sSec = FormatNumber2(s);
//        sSubSec = FormatNumber2(ss / 100); //Hundertstel
//    }

    public String AsString() {
        String result = "";
        Split();
        ss = ss / 100;
        result = FormatNumber2(h) + ":" + FormatNumber2(m) + ":" + FormatNumber2(s) + "." + FormatNumber2(ss);
        if (minus) {
            result = "-" + result;
        }
        return result;
    }

    public String AsString3() {
        String result = "";
        Split();
        ss = ss / 100;
        result = FormatNumber2(h) + ":" + FormatNumber2(m) + ":" + FormatNumber2(s) + "." + FormatNumber2(ss) + "0";
        if (minus) {
            result = "-" + result;
        }
        return result;
    }

    public String AsString4() {
        String result = "";
        Split();
        result = FormatNumber2(h) + ":" + FormatNumber2(m) + ":" + FormatNumber2(s) + "." + FormatNumber4(ss);
        if (minus) {
            result = "-" + result;
        }
        return result;
    }

    public String getHour() {
        Split();
        return FormatNumber2(h);
    }

    public String getMin() {
        Split();
        return FormatNumber2(m);
    }

    public String getSec() {
        Split();
        return FormatNumber2(s);
    }

    public String getSubSec() {
        Split();
        return FormatNumber2(ss);
    }

    public String getSign() {
        if (Value < 0) {
            return "M";
        } else {
            return "P";
        }
    }

}
