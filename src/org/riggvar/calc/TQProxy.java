package org.riggvar.calc;

import org.riggvar.penalty.*;

public class TQProxy {
    public boolean HighestBibGoesFirst;

    // in
    public int[] Bib = new int[1];
    public int[] DSQGate = new int[1];
    public int[] Status = new int[1];
    public int[] OTime = new int[1];

    // out
    public int[] ORank = new int[1];
    public int[] Rank = new int[1];
    public int[] PosR = new int[1];
    public int[] PLZ = new int[1];
    public int[] TimeBehind = new int[1]; // behind best at TimePoint
    public int[] TimeBehindPreviousLeader = new int[1]; // behind previous best at TimePoint
    public int[] TimeBehindLeader = new int[1]; // behind previous best at Finish

    public int BestIndex;
    public int BestOTime;

    public void Calc() {
    }

    public boolean IsOut(int Value) {
        return ((Value == StatusConst.Status_DSQ) || (Value == StatusConst.Status_DNF)
                || (Value == StatusConst.Status_DNS));
    }

    public boolean IsOK(int Value) {
        return ((Value == StatusConst.Status_OK) || (Value == StatusConst.Status_DSQPending));
    }

    public int getCount() {
        return Rank.length;
    }

    public void setCount(int value) {
        if ((value != Rank.length) && (value >= 0)) {
            Bib = new int[value];
            DSQGate = new int[value];
            Status = new int[value];
            OTime = new int[value];
            ORank = new int[value];
            Rank = new int[value];
            PosR = new int[value];
            PLZ = new int[value];
            TimeBehind = new int[value];
            TimeBehindPreviousLeader = new int[value];
            TimeBehindLeader = new int[value];
        }
    }

}
