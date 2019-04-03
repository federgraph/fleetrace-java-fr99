package org.riggvar.calc;

import org.riggvar.bo.*;
import org.riggvar.event.*;
import org.riggvar.scoring.TScoringSystem;

public class TUniquaPoints {
    public static void Calc(TEventNode qn) {
        TEventProps u;
        TEventRowCollection cl;
        TEventRowCollectionItem cr;
        TEventRowCollectionItem cr1;

        double f; // factor
        int s; // count of finishers (for at least one race in the regatta)
        int z; // count of races
        double PL; // points of the nominal last
        double P1; // points of the overall leader
        double PX; // points of the boat
        int Platz;

        try {
            u = TMain.BO.EventProps;

            // point for the 'charts'
            f = u.getFaktor(); // factor of the regatta
            s = u.getGezeitet(); // count of finishers (at least one time in the regatta)
            if (s == 0) {
                cl = qn.getBaseRowCollection();
                for (int i = 0; i < cl.getCount(); i++) {
                    cr = cl.get(i);
                    cr.RA = 0.0;
                    cr.QR = 0.0;
                }
                return;
            }

            z = u.getGesegelt(); // count of races

            // points of the nominal last
            if (u.ScoringSystem == TScoringSystem.LowPoint) {
                PL = s * z; // Low Point System
            } else {
                PL = (s + 6) * z; // Bonus System

            }
            cl = qn.getBaseRowCollection();

            // points of the first
            cr1 = cl.get(cl.get(0).PLZ());
            if (cr1 != null) {
                P1 = cr1.GRace.getCPoints(); // fast way using index
            } else {
                P1 = 0;
                for (int i = 0; i < cl.getCount(); i++) {
                    cr = cl.get(i);
                    if (cr.GRace.getCPoints() < P1) {
                        P1 = cr.GRace.getCPoints();
                    }
                }
            }

            for (int i = 0; i < cl.getCount(); i++) {
                cr = cl.get(i);

                // Ranglistenpunkte
                PX = cr.GRace.getCPoints(); // point of the boat
                if (PL - P1 != 0) {
                    cr.RA = f * 100 * (PL - PX) / (PL - P1);
                } else {
                    cr.RA = 0;
                }
                if (cr.RA < 0) {
                    cr.RA = 0;
                }
                Platz = cr.GRace.Rank;
                if (s != 0) {
                    cr.QR = 100 * (s + 1 - Platz) / s;
                } else {
                    cr.QR = 0; // limit
                }
                if (cr.QR < 0) {
                    cr.QR = 0;
                }
            }
        } catch (Exception ex) {
        }
    }
}
