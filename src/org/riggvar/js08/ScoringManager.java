package org.riggvar.js08;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Parent holder of information about scoring the races in a regatta. Provides
 * all the covering information calculating race and series points for a set of
 * entries (a division or fleet) in a set of races.
 * <p>
 * One instance of this class will exist for every "series" in a regatta For now
 * there is only one class in a regatta, so all boats are in all race and there
 * will only be one ScoringManager instance.
 * <p>
 * But when multi-classes and possibly overall fleet results come in, then there
 * will be one of these for each scored class, and each scored fleet.
 **/
public final class ScoringManager extends BaseObject implements Constants {
    private static Mutex ScoringLock = new Mutex();

    private transient static Map<String, String> sSupportedSystems;

    private transient static boolean sTraceStatus = false;

    static {
        sSupportedSystems = new TreeMap<String, String>();
        addSupportedModel(ScoringLowPoint.NAME, ScoringLowPoint.class.getName());
        addSupportedModel(ScoringBonusPoint.NAME_BonusPointSystem, ScoringBonusPoint.class.getName());
        addSupportedModel(ScoringBonusPointDSV.NAME_BonusPointSystemDSV, ScoringBonusPoint.class.getName());
    }

    protected ScoringModel fModel = new ScoringLowPoint();

    /**
     * contain RacePoints objects, one for each entry in each race
     */
    protected RacePointsList fPointsList = new RacePointsList();

    /**
     * key will be entry and division, value will be series total points
     */
    protected SeriesPointsList fSeries = new SeriesPointsList();

    private Regatta fRegatta;

    /**
     * list of strings containing Warning Messages
     */
    protected static WarningList sWarnings = new WarningList();

    public ScoringManager(Regatta parentRegatta) {
        super();
        fRegatta = parentRegatta;
    }

    /**
     * sets tracing level of information sent to standard output during scoring
     * 
     * @param b true if want tracing, false if not
     */
    public static void setTrace(boolean b) {
        sTraceStatus = b;
        System.out.println("ScoringManager.tracing is now " + b);
    }

    /**
     * list of warning messages generated during last scoring run
     * 
     * @return list of warnings
     */
    public static WarningList getWarnings() {
        return sWarnings;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        try {
            ScoringManager that = (ScoringManager) obj;
            if (!Util.equalsWithNull(this.fSeries, that.fSeries))
                return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * list of all supported scoring systems
     * 
     * @return array of scoring models
     */
    public static Object[] getSupportedModels() {
        return sSupportedSystems.keySet().toArray();
    }

    /**
     * adds model to list of supported models
     * 
     * @param newModel
     */
    public static void addSupportedModel(String modelName, String modelClass) {
        sSupportedSystems.put(modelName, modelClass);
    }

    /**
     * sets the active scoring model
     * 
     * @param modelName name of model to be added
     */
    public void setModel(String modelName) {
        String modelClass = sSupportedSystems.get(modelName);
        ScoringModel newModel = null;

        if (modelClass == null) {
            System.out
                    .println("WARNING!!! Unsupported scoring system requested, set to lowpoint, request=" + modelName);
            newModel = new ScoringLowPoint();
        } else {
            try {
                newModel = (ScoringModel) Class.forName(modelClass).getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (newModel == null) {
                System.out.println(
                        "WARNING!!! Unsupported scoring class requested, set to lowpoint, request=" + modelClass);
                newModel = new ScoringLowPoint();
            }
        }

        // copy over the throwouts and other stuff
        if (fModel != null && newModel != null)
            newModel.setAttributes(fModel);
        fModel = newModel;
    }

    /**
     * trivial implementation
     * 
     * @param obj
     * @return int
     */
    public int compareTo(BaseObject obj) {
        return 0;
    }

    /**
     * returns the active scoring model
     * 
     * @return current ScoringModel
     */
    public ScoringModel getModel() {
        return fModel;
    }

    /**
     * returns the list of series points for an entry
     * 
     * @param entry entry whose points are wanted
     * @return list of seriespoints
     */
    public SeriesPoints getSeriesPoints(Entry entry) {
        return fSeries.find(entry);
    }

    /**
     * returns list of all racepoints
     * 
     * @return RacePointsList
     */
    public RacePointsList getRacePointsList() {
        return fPointsList;
    }

    /**
     * returns list of all seriespoints
     * 
     * @return SeriesPointsList
     */
    public SeriesPointsList getTotalPoints() {
        return fSeries;
    }

    /**
     * calculates a racepoints array for specified race NOTE that this instance is
     * automatically scored in the hashmap returns null if the Scoring system is not
     * defined.
     * 
     * @param race    race to be scored
     * @param entries entries in the race
     * @param points  points list in which race's points are stored
     * @throws ScoringException if problem occurs
     */
    private void scoreRace(Race race, RacePointsList points) throws ScoringException {
        scoreRace(race, points, false);
    }

    /**
     * calculates a racepoints array for specified race NOTE that this instance is
     * automatically scored in the hashmap returns null if the Scoring system is not
     * defined.
     * 
     * @param race    race to be scored
     * @param entries entries in the race
     * @param points  points list in which race's points are stored
     * @throws ScoringException if problem occurs
     */
    private void scoreRace(Race race, RacePointsList points, boolean positionOnly) throws ScoringException {
        if (fModel == null || race == null)
            return;
        fModel.scoreRace(race, points, positionOnly);
    }

    /**
     * scores all boats, all races in the regatta
     * 
     * @param regatta Regatta to be scored
     * @param inRaces races to be included in the scoring
     * @throws ScoringException if a problem is encountered
     */
    public void scoreRegatta() throws ScoringException {
        ScoringLock.lock();
        try {
            if (sTraceStatus)
                System.out.println("ScoringManager: scoring started...");
            if (fModel == null || fRegatta == null || fRegatta.getNumRaces() == 0 || fRegatta.getNumEntries() == 0) {
                if (sTraceStatus)
                    System.out.println("ScoringManager: (empty) done.");
                return;
            }

            // check for entries with a division not in the fRegatta
            sWarnings.clear();

            fPointsList.clear();
            fSeries.clear();

            scoreRegatta(fRegatta.getRaces());

            if (!testing)
                sWarnings.showPopup(null);
            if (sTraceStatus)
                System.out.println("ScoringManager: scoring completed.");
        } finally {
            ScoringLock.unlock();
        }
    }

    /**
     * scores all boats for specified subset of races
     * 
     * @param regatta Regatta to be scored
     * @param inRaces races to be included in the scoring
     * @throws ScoringException if a problem is encountered
     */
    private void scoreRegatta(RaceList inRaces) throws ScoringException {
        // inRaces.sort();
        scoreNormalRegatta();
    }

    private void scoreNormalRegatta() throws ScoringException {
        scoreDivision();
    }

    public static boolean testing = false;

    private void scoreDivision() throws ScoringException {
        if (sTraceStatus)
            System.out.println("ScoringManager: scoring races...");

        EntryList entries = fRegatta.getAllEntries();

        // make up list of races for this div
        RaceList divRaces = new RaceList();
        for (Iterator<Race> iter = fRegatta.races(); iter.hasNext();) {
            Race r = iter.next();
            if (r.getIsRacing()) {
                divRaces.add(r);
            }
        }
        divRaces.sort();

        RacePointsList divPointsList = new RacePointsList();

        // calc races points for each race and each division in a race
        for (Race r : divRaces) {
            RacePointsList racePoints = new RacePointsList();
            for (Entry e : entries) {
                racePoints.add(new RacePoints(r, e, Double.NaN, false));
            }
            scoreRace(r, racePoints);
            divPointsList.addAll(racePoints);
        }

        fPointsList.addAll(divPointsList);
        scoreSeries(entries, divRaces, divPointsList);
    }

    private void scoreSeries(EntryList entries, RaceList divRaces, RacePointsList divPointsList)
            throws ScoringException {
        Race r;
        if (divRaces.size() > 0) {
            r = divRaces.get(0); // divRaces.getRace(1);
            if (r.HasFleets && Regatta.IsInFinalPhase) // &&
                // r.Regatta.IsInFinalPhase
                scoreQualiFinalSeries(entries, divRaces, divPointsList);
            else
                scoreSeries1(entries, divRaces, divPointsList);
        }
    }

    private void scoreQualiFinalSeries(EntryList entries, RaceList divRaces, RacePointsList points)
            throws ScoringException {
        RacePoints rp;
        // find the number of fleets in the race
        int fc = 0;
        for (int i = 0; i < points.size(); i++) {
            rp = points.get(i);
            if ((rp.fFinish != null) && (rp.fFinish.Fleet > fc))
                fc = rp.fFinish.Fleet;
        }

        Race fr = null;
        if ((divRaces != null) && (divRaces.size() > 0))
            fr = divRaces.get(divRaces.size() - 1);

        if (fr == null)
            return;

        if (!fr.IsFinalRace) {
            scoreSeries1(entries, divRaces, points);
        } else {
            RacePointsList rpl = new RacePointsList();
            EntryList el = new EntryList();

            int posOffset = 0;
            Finish f;
            Entry e;
            // call ScoreSeries1 for each fleet
            for (int j = 0; j <= fc; j++) {
                // get the entries for the fleet
                for (int i = 0; i < fr.fFinishList.size(); i++) {
                    f = fr.fFinishList.get(i);
                    e = f.getEntry();
                    if (f.Fleet == j)
                        el.add(e);
                }

                // get the racepoints for the fleet
                for (int i = 0; i < points.size(); i++) {
                    rp = points.get(i);
                    if (rp.fFinish != null)
                        rpl.add(rp);
                }
                if ((el.size() > 0) && (rpl.size() > 0)) {
                    scoreSeries2(el, divRaces, rpl, posOffset);
                    posOffset = posOffset + el.size();
                }
                // clear the temp lists for the next run of the loop
                rpl.clear();
                el.clear();
            }
        }
    }

    private void scoreSeries2(EntryList entries, RaceList divRaces, RacePointsList divPointsList, int posOffset)
            throws ScoringException {
        SeriesPointsList divSeriesPoints = new SeriesPointsList().initPoints(entries);

        // calc throwouts,
        for (Entry e : entries) {
            fModel.calcThrowouts(divPointsList.findAll(e));
        }

        // run thru looking for average points
        calcAveragePoints(divPointsList);

        // no finishes go to next division
        if (divSeriesPoints.size() == 0)
            return;

        fModel.scoreSeries(divRaces, entries, divPointsList, divSeriesPoints);

        // now run through looking for clumps of tied boats
        // pass the clumps of tied boats on to scoring model for resolution
        fModel.sortSeries(divSeriesPoints);

        ScoringTiebreaker doties = new ScoringTiebreaker(fModel, divRaces, divPointsList, divSeriesPoints);
        doties.process();

        // now set series position
        divSeriesPoints.sortPoints();
        int position = 1;
        double lastpoints = 0;
        boolean tied = false;
        for (int e = 0; e < divSeriesPoints.size(); e++) {
            SeriesPoints sp = divSeriesPoints.get(e);
            double thispoints = sp.getPoints();
            double nextpoints = ((e + 1 < divSeriesPoints.size()) ? divSeriesPoints.get(e + 1).getPoints()
                    : 99999999.0);
            tied = !((thispoints != lastpoints) && (thispoints != nextpoints));
            if (!tied) {
                position = e + 1;
            } else {
                // position is same if tied with last
                if (thispoints != lastpoints)
                    position = e + 1;
            }
            sp.setPosition(position + posOffset);
            sp.setTied(tied);
            lastpoints = thispoints;
        }

        fSeries.addAll(divSeriesPoints);
    }

    private void scoreSeries1(EntryList entries, RaceList divRaces, RacePointsList divPointsList)
            throws ScoringException {
        scoreSeries2(entries, divRaces, divPointsList, 0);
    }

    /**
     * calculates average points as per RRS2001 A10(a) (throwouts included): "points
     * equal to the average, to the nearest tenth of a point (0.05 to be rounded
     * upward), of her points in all the races in the series except the race in
     * question;"
     * <P>
     * NOTE: this formula assumes that "the race in question" really wants to say
     * the "race(s) in question"
     * 
     * @param regatta regatta to be scored. All instances of AVG in all races in all
     *                divisions in the regatta will be scanned and AVG points
     *                calculated
     */
    private void calcAveragePoints(RacePointsList divRacePoints) {
        calcAveragePoints(divRacePoints, true);
    }

    /**
     * calculates average points as per RRS2001 A10(a) except that including the
     * throwout (or not) is an optional
     * 
     * @param regatta            regatta to be scored. All instances of AVG in all
     *                           races in all divisions in the regatta will be
     *                           scanned and AVG points calculated
     * @param throwoutIsIncluded true if throwouts are to be included in calculation
     */
    private void calcAveragePoints(RacePointsList divRacePoints, boolean throwoutIsIncluded) {
        if (sTraceStatus)
            System.out.println("ScoringManager: calculating average points...");

        EntryList eWithAvg = new EntryList();
        for (RacePoints rp : divRacePoints) {
            if (rp.getFinish().hasPenalty(AVG)) {
                Entry e = rp.getEntry();
                if (!eWithAvg.contains(e))
                    eWithAvg.add(e);
            }
        }

        for (Entry e : eWithAvg) {
            RacePointsList list = divRacePoints.findAll(e);
            double pts = 0;
            double n = 0;
            boolean hasAvg = false;

            double[] tempPts = new double[list.size()];
            int[] tempPen = new int[list.size()];
            int t = 0;

            for (RacePoints p : list) {
                Finish finish = p.getRace().getFinish(p.getEntry());

                tempPts[t] = p.getPoints();
                tempPen[t++] = finish.getPenalty().getPenalty();

                if ((!p.isThrowout() || throwoutIsIncluded) && finish != null && !finish.getPenalty().hasPenalty(AVG)) {
                    pts = pts + p.getPoints();
                    n++;
                } else if (finish != null && finish.getPenalty().hasPenalty(AVG)) {
                    hasAvg = true;
                }
            }

            if (hasAvg) {
                double avg = pts / n;
                avg = Math.round(avg * 10);
                avg = avg / 10.0;
                for (RacePoints p : list) {
                    Finish finish = p.getRace().getFinish(p.getEntry());
                    if (finish != null && finish.getPenalty().hasPenalty(AVG)) {
                        p.setPoints(avg);
                    }
                }
            }

        }

    }

}
