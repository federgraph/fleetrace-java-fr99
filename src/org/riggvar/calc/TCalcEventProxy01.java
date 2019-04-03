package org.riggvar.calc;

import org.riggvar.event.*;
import org.riggvar.penalty.*;
import org.riggvar.time.*;

public class TCalcEventProxy01 extends TCalcEventProxy {
    // in
    private int[] Bib = new int[1];
    private int[] DSQGate = new int[1];
    private int[] Status = new int[1];
    private int[] OTime = new int[1];

    // out
    private int[] Rank = new int[1];
    private int[] PosR = new int[1];
    private int[] PLZ = new int[1];
    private int[] BTime = new int[1]; // behind best at TimePoint

//    private int BestIndex;
    private int BestOTime;

    private boolean HighestBibGoesFirst = true;

    public boolean isHighestBibGoesFirst() {
        return HighestBibGoesFirst;
    }

    public void setHighestBibGoesFirst(boolean HighestBibGoesFirst) {
        this.HighestBibGoesFirst = HighestBibGoesFirst;
    }

    protected void Calc1() {
        // Calc_BestIdx;
        // Calc_BTime;
        EncodeDSQGateAndStatus();
        Calc_Rank_PosR_Encoded();
    }

//    protected void Calc_BestIdx()
//    {
//        BestIndex = 0;
//        BestOTime = Integer.MAX_VALUE;
//        for (int i = 0; i < getCount(); i++)
//        {
//            int t = OTime[i];
//            if ( (t > 0) && (t < BestOTime) && IsOK(Status[i]))
//            {
//                BestIndex = i;
//                BestOTime = OTime[i];
//            }
//        }
//    }

    protected void Calc_BTime() {
        if (BestOTime == TimeConst.TimeNULL) {
            for (int i = 0; i < getCount(); i++) {
                BTime[i] = TimeConst.TimeNULL;
            }
        } else {
            for (int i = 0; i < getCount(); i++) {
                if (OTime[i] > 0) {
                    BTime[i] = OTime[i] - BestOTime;
                } else {
                    BTime[i] = TimeConst.TimeNULL;
                }
            }
        }
    }

    protected void EncodeDSQGateAndStatus() {
        for (int i = 0; i < getCount(); i++) {
            int temp = OTime[i];
            if (Status[i] == StatusConst.Status_DNF) {
                temp = Integer.MAX_VALUE - 300;
            } else if (Status[i] == StatusConst.Status_DSQ) {
                temp = Integer.MAX_VALUE - 200;
            } else if (Status[i] == StatusConst.Status_DNS) {
                temp = Integer.MAX_VALUE - 100;
                // temp = temp - DSQGate[i];
            }
            OTime[i] = temp;
        }
    }

    protected void Calc_Rank_PosR_Encoded() {
        int t1, t2; // Zeit1 and Zeit2
        int BibMerker; // because of 'Highest Bib goes first'
        int temp;

        // reset
        for (int j = 0; j < getCount(); j++) {
            Rank[j] = 1;
            PosR[j] = 1;
            PLZ[j] = -1;
        }

        // new calculation
        for (int j = 0; j < getCount(); j++) {
            t2 = OTime[j];
            BibMerker = Bib[j];
            // TimePresent = False
            if (t2 <= 0) {
                Rank[j] = 0;
                PosR[j] = 0;
            }
            // TimePresent
            else {
                for (int l = j + 1; l < getCount(); l++) {
                    t1 = OTime[l];
                    if (t1 > 0) {
                        if (t1 < t2) {
                            // increment Rank and PosR for j
                            Rank[j] = Rank[j] + 1;
                            PosR[j] = PosR[j] + 1;
                        }

                        if (t1 > t2) {
                            // increment Rank and PosR for l
                            Rank[l] = Rank[l] + 1;
                            PosR[l] = PosR[l] + 1;
                        }

                        if (t1 == t2) {
                            // do not increment Rank if Times are equal
                            // increment PosR for one of the riders, j or l
                            if (isHighestBibGoesFirst()) {
                                if (BibMerker > Bib[l]) {
                                    PosR[l] = PosR[l] + 1;
                                } else {
                                    PosR[j] = PosR[j] + 1;
                                }
                            } else {
                                if (BibMerker < Bib[l]) {
                                    PosR[l] = PosR[l] + 1;
                                } else {
                                    PosR[j] = PosR[j] + 1;
                                }
                            }
                        }
                    }
                }
                if (PosR[j] > 0) {
                    temp = PosR[j];
                    PLZ[temp - 1] = j;
                }
            }
        }
    }

    protected boolean IsOut(int Value) {
        return ((Value == StatusConst.Status_DSQ) || (Value == StatusConst.Status_DNF)
                || (Value == StatusConst.Status_DNS));
    }

    protected boolean IsOK(int Value) {
        return ((Value == StatusConst.Status_OK) || (Value == StatusConst.Status_DSQPending));
    }

    @Override
    public void Calc(TEventNode qn) {
        int RaceCount;
        //
        TEventRowCollection cl;
        TEventRowCollectionItem cr;
        int GPoints;

        if (qn.getBaseRowCollection().getCount() < 1) {
            return;
        }

        RaceCount = qn.getBaseRowCollection().get(0).RCount();
        for (int i = 1; i < RaceCount; i++) {
            LoadProxy(qn, i);
            Calc1();
            UnLoadProxy(qn, i);
        }

        // Points
        cl = qn.getBaseRowCollection();
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            GPoints = 0;
            for (int j = 1; j < cr.RCount(); j++) {
                // RacePoints
                cr.Race[j].setCTime1(cr.Race[j].Rank);

                cr.Race[j].Drop = false;
                if (cr.Race[j].getQU() != 0) {
                    cr.Race[j].setCTime1(400);
                    cr.Race[j].Drop = true;
                }
                // case cr.Race[j].QU of
                // Status_DNF: cr.Race[j].CTime := 100;
                // Status_DSQ: cr.Race[j].CTime := 200;
                // Status_DNS: cr.Race[j].CTime := 300;
                // end;

                // EventPoints
                GPoints = GPoints + cr.Race[j].getCTime1();
            }
            cr.GRace.setCTime1(GPoints);
        }

        LoadProxy(qn, 0); // channel_FT
        Calc1();
        UnLoadProxy(qn, 0); // channel_FT
    }

    public void LoadProxy(TEventNode qn, int channel) {
        TEventRowCollection cl = qn.getBaseRowCollection();
        setCount(cl.getCount());
        for (int i = 0; i < cl.getCount(); i++) {
            TEventRowCollectionItem cr = cl.get(i);
            Bib[i] = cr.Bib;
            DSQGate[i] = cr.Race[channel].DG;
            Status[i] = cr.Race[channel].getQU();
            if (channel == 0) // channel_FT
            {
                OTime[i] = cr.Race[channel].getCTime1();
            } else {
                OTime[i] = cr.Race[channel].OTime;
            }
        }
    }

    public void UnLoadProxy(TEventNode qn, int channel) {
        TEventRowCollection cl = qn.getBaseRowCollection();
        if (getCount() != cl.getCount()) {
            return;
        }
        for (int i = 0; i < cl.getCount(); i++) {
            TEventRowCollectionItem cr = cl.get(i);
            // cr.Race[channel].BTime = BTime[i];
            // cr.ru.BestTime[channel] = BestOTime;
            // cr.ru.BestIndex[channel] = BestIndex;
            cr.Race[channel].Rank = Rank[i];
            cr.Race[channel].PosR = PosR[i];
            cr.Race[channel].PLZ = PLZ[i];
        }
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
            BTime = new int[value];
            Rank = new int[value];
            PosR = new int[value];
            PLZ = new int[value];
        }
    }
}
