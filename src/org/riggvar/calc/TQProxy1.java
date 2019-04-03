package org.riggvar.calc;

import org.riggvar.penalty.*;
import org.riggvar.time.*;

public class TQProxy1 extends TQProxy {
    private void Calc_ORank() {
        for (int j = 0; j < getCount(); j++) {
            ORank[j] = 1;
        }
        for (int j = 0; j < getCount(); j++) {
            int t2 = OTime[j];
            if (t2 <= 0) {
                ORank[j] = 0;
            } else {
                for (int l = j + 1; l < getCount(); l++) {
                    int t1 = OTime[l];
                    if (t1 > 0) {
                        if (t1 < t2) {
                            ORank[j] = ORank[j] + 1;
                        }
                        if (t1 > t2) {
                            ORank[l] = ORank[l] + 1;
                        }
                    }
                }
            }
        }
    }

    private void Calc_BestIdx() {
        BestIndex = 0;
        BestOTime = Integer.MAX_VALUE;
        for (int i = 0; i < getCount(); i++) {
            int t = OTime[i];
            if ((t > 0) && (t < BestOTime) && IsOK(Status[i])) {
                BestIndex = i;
                BestOTime = OTime[i];
            }
        }
    }

    private void Calc_TimeBehind() {
        if (BestOTime == TimeConst.TimeNULL) {
            for (int i = 0; i < getCount(); i++) {
                TimeBehind[i] = TimeConst.TimeNULL;
            }
        } else {
            for (int i = 0; i < getCount(); i++) {
                if (OTime[i] > 0) {
                    TimeBehind[i] = OTime[i] - BestOTime;
                } else {
                    TimeBehind[i] = TimeConst.TimeNULL;
                }
            }
        }
    }

    private void EncodeDSQGateAndStatus() {
        for (int i = 0; i < getCount(); i++) {
            int temp = OTime[i];
            if (Status[i] == StatusConst.Status_DNF) {
                temp = Integer.MAX_VALUE - 300;
            } else if (Status[i] == StatusConst.Status_DSQ) {
                temp = Integer.MAX_VALUE - 200;
            } else if (Status[i] == StatusConst.Status_DNS) {
                temp = Integer.MAX_VALUE - 100;
            }
            temp = temp - DSQGate[i];
            OTime[i] = temp;
        }
    }

    private void Calc_Rank_PosR_Encoded() {
        int t1, t2; // Zeit1 und Zeit2
        int BibMerker; // wegen 'Highest Bib goes first'

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
                            if (HighestBibGoesFirst) {
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
                    int temp = PosR[j];
                    PLZ[temp - 1] = j;
                }
            }
        }
    }

    @Override
    public void Calc() {
        Calc_ORank();
        Calc_BestIdx();
        Calc_TimeBehind();
        EncodeDSQGateAndStatus();
        Calc_Rank_PosR_Encoded();
    }
}
