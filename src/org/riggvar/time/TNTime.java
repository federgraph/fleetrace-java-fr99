package org.riggvar.time;

import org.riggvar.base.*;

/**
 * Normal time span, can be negative.
 */
public class TNTime extends TBOPersistent {
    protected int FTime;
    protected int FStatus;
    private int FDisplayPrecision = 2;
    private int FPrecision = 2;

    //
    public TNTime() {
    }

    //
    private String EnsureLeadingZero(int Value) {
        // Setzt eine Null vor einstellige Zahlen
        if (Value < 10) {
            return "0" + Utils.IntToStr(Value);
        } else {
            return Utils.IntToStr(Value);
        }
    }

    private String LeadingZeros(int anz, String sIn) {
        // prepends a string with anz leading zeroes
        String hs; // helpstring
        hs = "";
        // erstmal anz Nullen holen
        for (int i = 1; i <= anz; i++) {
            hs = hs + "0";
            // davorsetzen
        }
        hs = hs + sIn;
        // read from the right
        return Utils.Copy(hs, hs.length() - anz + 1, anz);
    }

    private String CheckTime(String TimeStr) {
        int dotpos;
        String TimeStr2;
        int lastdd;
        String sNachkomma;
        String sNachkommaChecked;

        if (TimeStr.equals("")) {
            return "";
        }
        TimeStr2 = "";
        // ersten Punkt suchen
        dotpos = Utils.Pos(".", TimeStr);
        lastdd = dotpos;
        // wurde entgegen den Regeln doch ein Komma eingegeben
        dotpos = Utils.Pos(",", TimeStr);

        if ((lastdd == 0) && (dotpos == 0)) {
            TimeStr = TimeStr + ".0";
            dotpos = Utils.Pos(".", TimeStr);
        }
        if ((lastdd == 0) && (dotpos > 0)) {
            lastdd = dotpos; // es war ein Komma
        } else if (lastdd > 0) {
            dotpos = lastdd; // war es ein Punkt
        }
        // check chars before comma/decimal point
        for (int i = dotpos - 1; i >= 1; i--) {
            char c = TimeStr.charAt(i - 1);
            if (c == ':') {
                if (lastdd > 0) // gab es schon einen Punkt oder Doppelpunkt
                {
                    if (lastdd - i < 3) // two chars?
                    {
                        TimeStr2 = "0" + TimeStr2; // no, prepend one
                    }
                }
                lastdd = i; // Position speichern
            } else if ((c >= '0') && (c <= '9')) {
                // war es wenigstens eine Zahl
                TimeStr2 = TimeStr.charAt(i - 1) + TimeStr2; // accept char
            }
        }
        TimeStr2 = LeadingZeros(6, TimeStr2); // prepend leading zeroes

        // check decimal digits
        sNachkommaChecked = "";
        sNachkomma = Utils.Copy(TimeStr, dotpos, TimeStr.length());
        for (int i = 2; i <= sNachkomma.length(); i++) {
            // check the current char
            char c = sNachkomma.charAt(i - 1);
            if ((c >= '0') && (c <= '9')) {
                sNachkommaChecked = sNachkommaChecked + sNachkomma.charAt(i - 1);
            }
        }
        if (sNachkommaChecked.equals("")) {
            sNachkommaChecked = "0";

        }
        return TimeStr2 + "." + sNachkommaChecked;
    }

    protected String ConvertTimeToStr3(int TimeVal) {
        // Konvertiert einen numerischen Wert in einen String -> Format (-)HH:MM:SS.mm
        // drop leading hour/min: 674326 -> '1:07.43'
        // does not output leading zero
        // Tausendstel und Zehntausendstel werden nicht ausgegeben
        int hours, min, sec, msec;
        String temp;
        boolean minus = false;

        if (TimeVal < 0) {
            minus = true;
            TimeVal = java.lang.Math.abs(TimeVal);
        }

        msec = TimeVal % 10000; // Convert.ToInt32(TimeVal % 10000);
        TimeVal = TimeVal / 10000;
        sec = TimeVal % 60; // Convert.ToInt32(TimeVal % 60);
        TimeVal = TimeVal / 60;
        min = TimeVal % 60; // Convert.ToInt32(TimeVal % 60);
        TimeVal = TimeVal / 60;
        hours = TimeVal; // Convert.ToInt32(TimeVal);

        // do not output missing parts on the left
        temp = "";
        if (hours > 0) {
            temp = temp + EnsureLeadingZero(hours) + ":";
        }
        if (min + hours > 0) {
            temp = temp + EnsureLeadingZero(min) + ":";
        }
        if (sec + min + hours > 0) {
            temp = temp + EnsureLeadingZero(sec);
        } else {
            temp = temp + "00";
        }
        temp = temp + "." + LeadingZeros(4, Utils.IntToStr(msec));

        // do not output leading zeroes
        if (temp.charAt(0) == '0') {
            temp = Utils.Copy(temp, 2, temp.length());

        }
        if (minus) {
            temp = "-" + temp;

            // Tausendstel und Zehntausendstel nicht ausgeben
        }
        temp = Utils.Copy(temp, 1, temp.length() - (4 - FDisplayPrecision));

        return temp;
    }

    private int ConvertStrToTime1(String TimeStr) {

        // Konvertiert TimeStr in einen numerischen Wert
        // wurde mit 'Zeit holen' verwendet
        // Liefert Zeit als Anzahl Hundertstel
        // Beispiel( Eine Miute, zwei Sekunden, 3 Hundertstel; also 1:02.03
        // ConvertStrToTime2 passes only the part before the comma
        // 00010200 --> 6200

        int i, j;
        int k;

        if (TimeStr.charAt(2) == ':') // Sonderzeichen ignorieren, wenn vorhanden
        {
            k = 1;
        } else {
            k = 0;
        }
        i = Utils.StrToIntDef(Utils.Copy(TimeStr, 1, 2), 0); // Stunden
        j = i;
        i = Utils.StrToIntDef(Utils.Copy(TimeStr, 3 + k, 2), 0); // Minuten
        j = j * 60 + i;
        i = Utils.StrToIntDef(Utils.Copy(TimeStr, 5 + k * 2, 2), 0); // Sekunden
        j = j * 60 + i;
        i = Utils.StrToIntDef(Utils.Copy(TimeStr, 7 + k * 3, 2), 0); // Hundertstel
        j = j * 100 + i;
        return j;
    }

    private int ConvertStrToTime2(String TimeStr) {
        // Konvertiert TimeStr in einen numerischen Wert
        int V; // VorkommaTeil
        int N; // NachkommaTeil
        int pos1, pos2, posi;
        String str;

        pos1 = Utils.Pos(".", TimeStr); // Sonderzeichen vorhanden?
        pos2 = Utils.Pos(",", TimeStr);
        posi = pos1 + pos2;
        if (posi > 0) {
            // Vorkommastellen V
            str = "000000" + Utils.Copy(TimeStr, 1, posi - 1) + "00";
            str = Utils.Copy(str, str.length() - 7, 8); // last 8 char
            V = ConvertStrToTime1(str) * 100; // diese Konvertieren
            // V jetzt in Zehntausendstel

            // Nachkommastellen N
            str = Utils.Copy(TimeStr, 8, TimeStr.length());
            if (str.length() == 0) {
                str = "0";
                // Runden auf Precision
            }
            double d = Utils.StrToIntDef(str, 0) * Math.pow(10, FPrecision - str.length());
            N = Utils.ToInt32(d); // Convert.ToInt64(Math.Round(d));
            // aber intern immer Tausendstel speichern
            for (int i = FPrecision; i <= 3; i++) {
                N = N * 10;

            }
            return V + N;
        } else {
            return 0;
        }
    }

    // protected virtual int GetAsInteger()
    // {
    // if (FStatus == TTimeStatus.tsNone)
    // return 0;
    // else
    // return FTime;
    // }
    // protected virtual void SetAsInteger(int Value)
    // {
    // if (Value == TimeConst.TimeNULL)
    // {
    // Status = TTimeStatus.tsNone;
    // Time = 0;
    // }
    // else
    // {
    // Status = TTimeStatus.tsAuto;
    // Time = Value;
    // }
    // }
    @Override
    public void Assign(Object source) {
        if (source instanceof TNTime) {
            TNTime o = (TNTime) source;
            setAsInteger(o.getAsInteger());
        } else {
            super.Assign(source);
        }
    }

    public void Clear() {
        setStatus(TTimeStatus.tsNone);
        FTime = 0;
    }

    public boolean Parse(String Value) {
        if (!IsValidTimeString(Value)) {
            return false;
        }
        setStatus(TTimeStatus.tsAuto);
        FTime = ConvertStrToTime2(CheckTime(Value));
        return true;
    }

    @Override
    public String toString() {
        // if (FTime < 0)
        // return "";
        // else
        if ((FTime == 0) && (!isTimePresent())) {
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

    public void UpdateQualiTimeBehind(int aBestTime, int aOTime) {
        if (aBestTime == TimeConst.TimeNULL) {
            setAsInteger(TimeConst.TimeNULL);
        } else if (aOTime > 0) {
            setAsInteger(aOTime - aBestTime);
        } else {
            setAsInteger(TimeConst.TimeNULL);
        }
    }

    public boolean IsValidTimeString(String TimeStr) {
        if (TimeStr.equals("")) {
            return false;
        }

        // den letzten Punkt suchen (find last decimal point)
        int dotpos = TimeStr.lastIndexOf('.');
        int lastcolon = dotpos;

        // auch nach letztem Komma suchen (find last Comma)
        dotpos = TimeStr.lastIndexOf(',');

        // ensure decimal point
        if ((lastcolon == -1) && (dotpos == -1)) {
            TimeStr = TimeStr + ".0";
            dotpos = TimeStr.lastIndexOf('.');
        }

        // Bemerkung: lastcolon und dotps werden beide auf den Dezimalpunktes gestellt
        // dotpos does not change from here on
        if ((lastcolon == -1) && (dotpos > 0)) {
            lastcolon = dotpos; // es war ein Komma
        } else if (lastcolon > 0) {
            dotpos = lastcolon; // war es ein Punkt

            // der Vorkommateil wird von dotpos aus nach links geparst:
            // Sekunden zuerst, dann Minuten, dann Stunden
            // dabei die Sonderzeichen entfernen,
            // fill with leading zeroes (Sekunden, Minuten, Stunden, alles 2-Stellig)
        }
        for (int i = dotpos - 1; i >= 0; i--) {
            char c = TimeStr.charAt(i);
            if (c == ':') { // ok
            } else if ((c >= '0') && (c <= '9')) { // ok
            } else {
                return false;
            }
        }

        // der Nachkommateil wird von dotpos aus nach rechts geparst:
        String sNachkomma = Utils.Copy(TimeStr, dotpos + 2, TimeStr.length());
        for (int i = 0; i < sNachkomma.length(); i++) {
            char c = sNachkomma.charAt(i);
            if ((c < '0') || (c > '9')) {
                return false;
            }
        }
        return true;
    }

    public String getStatusAsString() {
        // result := TimeStatusStrings[Status];
        return TimeConst.TimeStatusStrings(getStatus());
    }

    public TTimeSplit getSbdTime() {
        TTimeSplit o = new TTimeSplit();
        o.Value = FTime;
        return o;
    }

    public boolean isTimePresent() {
        return (FStatus != TTimeStatus.tsNone);
    }

    public String getAsString() {
        return toString();
    }

    public void setAsString(String value) {
        Parse(value);
    }

    public int getDisplayPrecision() {
        return FDisplayPrecision;
    }

    public void setDisplayPrecision(int value) {
        if ((getDisplayPrecision() > 0) && (getDisplayPrecision() <= 4)) {
            setDisplayPrecision(value);
        }
    }

    public int getAsInteger() {
        if (this.getStatus() == TTimeStatus.tsNone) {
            return 0;
        } else {
            return FTime;
        }
    }

    public void setAsInteger(int value) {
        if (value == TimeConst.TimeNULL) {
            this.setStatus(TTimeStatus.tsNone);
            FTime = 0;
        } else {
            setStatus(TTimeStatus.tsAuto);
            FTime = value;
        }
    }

    public int getStatus() {
        // Assert(FTime >= 0, "Zeit darf nicht negativ sein");
        if (FTime < 0) {
            FTime = 0;
            FStatus = TTimeStatus.tsNone;
        }
        return FStatus;
    }

    public void setStatus(int value) {
        FStatus = value;
    }

}
