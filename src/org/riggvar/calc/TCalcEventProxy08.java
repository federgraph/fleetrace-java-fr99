package org.riggvar.calc;

import java.util.*;

import org.riggvar.event.*;
import org.riggvar.js08.Regatta;
import org.riggvar.base.*;
import org.riggvar.bo.*;
import org.riggvar.scoring.TScoringSystem;

public class TCalcEventProxy08 extends TCalcEventProxy {
    // public static final int THROWOUT_BYNUMRACES = 1;
    // public static final int THROWOUT_PERXRACES = 2;
    // public static final int THROWOUT_BESTXRACES = 3;
    public static final int THROWOUT_NONE = 4;

    public static final int DNC = 0xC000;

    private TStrings FSL = new TStringList();

    public Regatta regatta;
    public TEventNode qn;
    public TEventProps EventProps;

    public TCalcEventProxy08() {
    }

    private void InitThrowoutScheme() {
        // precondition
        if (regatta == null) {
            return;
        }
        if (qn == null) {
            return;
        }
        if (qn.getBaseRowCollection().getCount() == 0) {
            return;
        }

        // count of sailed races
        int IsRacingCount = 0;
        for (int r = 1; r <= qn.getRaceCount(); r++) {
            if (TMain.BO.RNode[r].IsRacing) {
                IsRacingCount++;
            }
        }

        // number of throwouts
        int t = TMain.BO.EventProps.Throwouts;
        if (t >= IsRacingCount) {
            t = IsRacingCount - 1;
        }

        // update scoring-system-attributes in scoring
        org.riggvar.js08.ScoringLowPoint sm = new org.riggvar.js08.ScoringLowPoint();
        if (t <= 0) {
            sm.setThrowoutScheme(THROWOUT_NONE);
        } else {
            sm.setFixedNumberOfThrowouts(t);
            sm.setThrowoutPerX(t);
            sm.setThrowoutBestX(t);
            sm.setThrowoutScheme(EventProps.ThrowoutScheme);
        }
        sm.FirstIs75 = EventProps.FirstIs75;
        sm.ReorderRAF = EventProps.ReorderRAF;

        if (EventProps.ScoringSystem == TScoringSystem.BonusDSV) {
            regatta.getScoringManager().setModel(org.riggvar.js08.ScoringBonusPointDSV.NAME_BonusPointSystemDSV);
        } else if (EventProps.ScoringSystem == TScoringSystem.Bonus) {
            regatta.getScoringManager().setModel(org.riggvar.js08.ScoringBonusPoint.NAME_BonusPointSystem);
        } else {
            regatta.getScoringManager().setModel(org.riggvar.js08.ScoringLowPoint.NAME);

        }
        org.riggvar.js08.ScoringModel m = regatta.getScoringManager().getModel();
        m.setAttributes(sm);
    }

    public void LoadProxy() {
        org.riggvar.js08.Entry e;
        org.riggvar.js08.Race r;
        org.riggvar.js08.Finish f;
        org.riggvar.js08.FinishPosition fp;
        org.riggvar.js08.Penalty p;

        TEventRowCollectionItem cr;
        TEventRaceEntry er;
        long ft;

        long NowMillies = TDateTime.Now().ToMillies(TDateTime.Now());

        if (regatta == null) {
            return;
        }
        if (qn == null) {
            return;
        }

        TEventRowCollection cl = qn.getBaseRowCollection();

//        d = new Division(EventProps.getDivisionName());
//        regatta.addDivision(d);

        if (qn.getRaceCount() >= qn.getFirstFinalRace())
            Regatta.IsInFinalPhase = true;

        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            cr.isGezeitet = false;
            e = new org.riggvar.js08.Entry();
            e.setSailID(cr.SNR);
//            try
//            {
//                e.setDivision(d);
//            }
//            catch (Exception ex)
//            {
//            }
            regatta.addEntry(e);

            Date startDate;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            startDate = calendar.getTime();
            for (int j = 1; j < cr.RCount(); j++) {
                if (qn.PartialCalcLastRace > 0 && j > qn.PartialCalcLastRace)
                    break;

                if (i == 0) {
                    r = new org.riggvar.js08.Race(regatta, "R" + Utils.IntToStr(j));
                    regatta.getRaces().add(r);
                    // r.getDivInfo().setStartTime(d, NowMillies + (j * 1000));
                    // r.getDivInfo().setIsRacing(d, qn.BO.RNode[j].IsRacing);
                    r.setStartDate(startDate);
                    r.HasFleets = cr.ru().UseFleets;
                    r.TargetFleetSize = cr.ru().TargetFleetSize;
                    if (j >= qn.getFirstFinalRace())
                        r.IsFinalRace = true;

                } else {
                    r = regatta.getRaces().get(j - 1);
                }
                er = cr.Race[j];
                if (er.OTime == 0) {
                    fp = new org.riggvar.js08.FinishPosition(DNC);
                } else {
                    fp = new org.riggvar.js08.FinishPosition(er.OTime);
                }
                // need finish time ft for race ties
                if (fp.isValidFinish()) {
                    cr.isGezeitet = true;
                    ft = NowMillies + (1000 * er.OTime);
                } else {
                    ft = org.riggvar.js08.SailTime.NOTIME;
                }
                if (er.getQU() == 0) {
                    p = null;
                } else {
                    p = new org.riggvar.js08.Penalty(er.getQU());
                    p.setPoints(er.getPenalty().Points);
                    p.setNote(er.getPenalty().Note);
                    p.setPercent(er.getPenalty().Percent);
                    p.setTimePenalty(er.getPenalty().TimePenalty);
                }
                f = new org.riggvar.js08.Finish(r, e, ft, fp, p);
                f.Fleet = er.Fleet;
                f.IsRacing = er.IsRacing;
                r.fFinishList.add(f);
            }
        }
        // update Gezeitet
        int a = 0;
        for (int i = 0; i < cl.getCount(); i++) {
            if (cl.get(i).isGezeitet) {
                a++;
            }
        }
        TMain.BO.Gezeitet = a;
    }

    public void UnLoadProxy() {
        org.riggvar.js08.SeriesPointsList seriesPoints;
        org.riggvar.js08.SeriesPoints sp;

        org.riggvar.js08.Race race;
        org.riggvar.js08.RacePoints racepts;

        TEventRowCollectionItem cr;
        TEventRowCollectionItem crPLZ;

        if (regatta == null) {
            return;
        }
        if (qn == null) {
            return;
        }

        try {
            seriesPoints = regatta.getScoringManager().getTotalPoints();
            regatta.getScoringManager().getModel().sortSeries(seriesPoints);

            TEventRowCollection cl = qn.getBaseRowCollection();
            for (int e = 0; e < seriesPoints.size(); e++) {
                sp = seriesPoints.get(e);
                cr = cl.FindKey(sp.getEntry().SailID);
                if ((cr == null) || (sp == null)) {
                    continue;
                }

                cr.isTied = sp.isTied();
                cr.Race[0].setCPoints(sp.getPoints());
                cr.Race[0].Rank = sp.getPosition();
                cr.Race[0].PosR = e + 1;

                crPLZ = cl.get(e);
                if (crPLZ != null) {
                    crPLZ.Race[0].PLZ = cr.getIndex();
                }

                // loop thru races display points for each race
                for (int i = 0; i < regatta.getRaces().size(); i++) {
                    if (qn.PartialCalcLastRace > 0 && i > qn.PartialCalcLastRace - 1)
                        break;

                    race = regatta.getRaces().get(i);
                    racepts = regatta.getScoringManager().getRacePointsList().find(race, sp.getEntry());
                    if (racepts != null) {
                        cr.Race[i + 1].setCPoints(racepts.getPoints());
                        cr.Race[i + 1].Drop = racepts.isThrowout();
                    } else if (!race.getIsRacing()) {
                        cr.Race[i + 1].setCPoints(0);
                        cr.Race[i + 1].Drop = false;
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void Calc(TEventNode aqn) {
        qn = aqn;
        EventProps = TMain.BO.EventProps;
        if (qn.getBaseRowCollection().getCount() == 0) {
            return;
        }

        // Note: wird beim Laden eines events zweimal aufgerufen
        // einmal bei Clear, hier wird Gezeitet 0, da noch keine Daten da sind,
        // dann mit OnIdle/Modified = false.
        // if Gezeitet is 0 Ranglistenpunkte cannot be calculated

        regatta = new org.riggvar.js08.Regatta();
        try {
            InitThrowoutScheme();
            LoadProxy();
            regatta.scoreRegatta();
            UnLoadProxy();
        } finally {
            regatta = null;
        }
    }

    @Override
    public void GetScoringNotes(TStrings SL) {
        for (int i = 0; i < FSL.getCount(); i++) {
            SL.Add(FSL.getString(i));
        }
    }
}
