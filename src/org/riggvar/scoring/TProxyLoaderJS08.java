package org.riggvar.scoring;

import org.riggvar.js08.*;

public class TProxyLoaderJS08 implements IProxyLoader {
    public static final int THROWOUT_NONE = 4;

    protected Regatta regatta;
    private TFRProxy proxyNode;
    public TJSEventProps EventProps;

    public TProxyLoaderJS08() {
    }

    private void InitThrowoutScheme() {
        // precondition
        if (regatta == null) {
            return;
        }
        if (proxyNode == null) {
            return;
        }
        if (proxyNode.EntryInfoCollection.Count() == 0) {
            return;
        }

        // count of sailed races
        int IsRacingCount = 0;
        for (int r = 1; r < proxyNode.getRCount(); r++) {
            if (proxyNode.IsRacing[r]) {
                IsRacingCount++;

                // number of throwouts
            }
        }
        int t = proxyNode.EventProps.Throwouts;
        if (t >= IsRacingCount) {
            t = IsRacingCount - 1;

            // update scoring-system-attributes in javascore
        }
        ScoringLowPoint sm = new ScoringLowPoint();
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

        if (EventProps.ScoringSystem == TJSEventProps.Bonus) {
            regatta.getScoringManager().setModel(ScoringBonusPoint.NAME_BonusPointSystem);
        } else if (EventProps.ScoringSystem == TJSEventProps.BonusDSV) {
            regatta.getScoringManager().setModel(ScoringBonusPointDSV.NAME_BonusPointSystemDSV);
        } else {
            regatta.getScoringManager().setModel(ScoringLowPoint.NAME);
        }
        ScoringModel m = regatta.getScoringManager().getModel();
        m.setAttributes(sm);

    }

    private void LoadProxy() {
        Entry e;
        Race r;
        Finish f;
        FinishPosition fp;
        Penalty p;

        TEntryInfo cr;
        TRaceInfo er;
        long ft;

        long NowMillies = TDateTime.Now().ToMillies(TDateTime.Now());

        if (regatta == null) {
            return;
        }
        if (proxyNode == null) {
            return;
        }

        TEntryInfoCollection cl = proxyNode.EntryInfoCollection;

        for (int i = 0; i < cl.Count(); i++) {
            cr = cl.Items(i);
            cr.IsGezeitet = false;
            e = new Entry();
            e.SailID = cr.SNR;
            regatta.addEntry(e);

            if (proxyNode.getRCount() > proxyNode.FirstFinalRace)
                Regatta.IsInFinalPhase = true;

            for (int j = 1; j < cr.RCount(); j++) {
                if (i == 0) {
                    r = new Race(regatta, "R" + j);
                    regatta.getRaces().add(r);
                    r.setIsRacing(proxyNode.IsRacing[j]);
                    r.HasFleets = proxyNode.UseFleets;
                    r.TargetFleetSize = proxyNode.TargetFleetSize;
                    if (j >= proxyNode.FirstFinalRace)
                        r.IsFinalRace = true;
                } else {
                    r = regatta.getRaces().get(j - 1);
                }
                er = cr.Race(j);
                if (er.OTime == 0) {
                    fp = new FinishPosition(Constants.DNC);
                } else {
                    fp = new FinishPosition(er.OTime);
                }
                // need finishtime ft for raceties
                if (fp.isValidFinish()) {
                    cr.IsGezeitet = true;
                    ft = NowMillies + (1000 * er.OTime);
                } else {
                    ft = SailTime.NOTIME;
                }
                if (er.QU == 0) {
                    p = null;
                } else {
                    p = new Penalty(er.QU);
                    p.setPoints(er.Penalty_Points);
                    p.setNote(er.Penalty_Note);
                    p.setPercent(er.Penalty_Percent);
                    p.setTimePenalty(er.Penalty_TimePenalty);
                }
                f = new Finish(r, e, ft, fp, p);
                f.Fleet = er.Fleet;
                if (!er.IsRacing)
                    f.IsRacing = false;
                r.fFinishList.add(f);
            }
        }
        // count Gezeitet
        int a = 0;
        for (int i = 0; i < cl.Count(); i++) {
            if (cl.Items(i).IsGezeitet) {
                a++;
            }
        }
        proxyNode.Gezeitet = a;
    }

    private void UnLoadProxy() {
        SeriesPointsList seriesPoints;
        SeriesPoints sp;

        Race race;
        RacePoints racepts;

        TEntryInfo cr;
        TEntryInfo crPLZ;

        if (regatta == null) {
            return;
        }
        if (proxyNode == null) {
            return;
        }

        try {
            seriesPoints = regatta.getScoringManager().getTotalPoints();
            regatta.getScoringManager().getModel().sortSeries(seriesPoints);

            TEntryInfoCollection cl = proxyNode.EntryInfoCollection;
            for (int e = 0; e < seriesPoints.size(); e++) {
                sp = seriesPoints.get(e);
                cr = cl.FindKey(sp.getEntry().SailID);
                if ((cr == null) || (sp == null)) {
                    continue;
                }

                cr.IsTied = sp.isTied();
                cr.Race(0).CPoints = sp.getPoints();
                cr.Race(0).Rank = sp.getPosition();
                cr.Race(0).PosR = e + 1;

                crPLZ = cl.Items(e);
                if (crPLZ != null) {
                    crPLZ.Race(0).PLZ = cr.Index;
                }
                // loop thru races display points for each race
                for (int i = 0; i < regatta.getRaces().size(); i++) {
                    TRaceInfo ri = cr.Race(i + 1);
                    race = regatta.getRaces().get(i);
                    racepts = regatta.getScoringManager().getRacePointsList().find(race, sp.getEntry());
                    if (!ri.IsRacing)
                        ri.CPoints = 0.0;
                    else if (racepts != null) {
                        ri.CPoints = racepts.getPoints();
                        ri.Drop = racepts.isThrowout();
                    } else if (!race.getIsRacing()) {
                        ri.CPoints = 0;
                        ri.Drop = false;
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    public void Calc(TFRProxy proxy) {
        long t1 = System.currentTimeMillis();

        proxyNode = proxy;
        EventProps = proxyNode.EventProps;
        if (proxyNode.EntryInfoCollection.Count() == 0) {
            return;
        }

        regatta = new Regatta();
        try {
            if (EventProps.ScoringSystem == TJSEventProps.Bonus) {
                regatta.getScoringManager().setModel(ScoringBonusPoint.NAME_BonusPointSystem);
            } else if (EventProps.ScoringSystem == TJSEventProps.BonusDSV) {
                regatta.getScoringManager().setModel(ScoringBonusPointDSV.NAME_BonusPointSystemDSV);

            }
            InitThrowoutScheme();
            LoadProxy();
            regatta.scoreRegatta();
            UnLoadProxy();
        } finally {
            regatta = null;
        }
        long t2 = System.currentTimeMillis();
        System.out.println("calc completed in " + (t2 - t1) + " milliseconds");
    }

}
