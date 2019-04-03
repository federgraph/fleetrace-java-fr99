package org.riggvar.js08;

import java.text.MessageFormat;
import java.util.*;

/**
 * ISAF LowPoint scoring system currently does ISAF
 **/
public class ScoringLowPoint extends BaseObject implements ScoringModel, Constants {
    public boolean ReorderRAF = true;
    public boolean FirstIs75;
    public boolean HasFleets;
    public int TargetFleetSize;
    public boolean IsFinalRace;

    public static final String NAME = "ISAF Low Point 2005-2008";

    public String getName() {
        return NAME;
    }

    /**
     * option per RRS2001 A9 for different penalties for "long" series If true, the
     * penalties as per A9 will be applied
     */
    private boolean fIsLongSeries = false;

    public static final int THROWOUT_BYNUMRACES = 1;
    public static final int THROWOUT_PERXRACES = 2;
    public static final int THROWOUT_BESTXRACES = 3;
    public static final int THROWOUT_NONE = 4;

    /**
     * throwouts, this vector is minimum number of races for i'th throwout.
     */
    private List<Integer> fThrowouts = new ArrayList<Integer>();
    private int fThrowoutScheme = THROWOUT_BYNUMRACES;
    private int fThrowoutPerX = 0;
    private int fThrowoutBestX = 0;

    /**
     * Default percentage penalty for failure to check-in
     */
    private int fCheckinPercent = 20;

    public static final int TLE_DNF = 0;
    public static final int TLE_FINISHERSPLUS1 = 1;
    public static final int TLE_FINISHERSPLUS2 = 2;
    public static final int TLE_AVERAGE = 3;

    public static final int TIE_RRS_DEFAULT = 1;
    public static final int TIE_RRS_A82_ONLY = 2;

    private int fTiebreaker = TIE_RRS_DEFAULT;
    private boolean fUserCanChangeTiebreaker = true;

    /**
     * Array of supported Finish Time Limit penalties
     */
    private int fTimeLimitPenalty = TLE_DNF;

    public static final String TIMELIMITPENALTY_PROPERTY = "TimeLimitPenalty";
    public static final String CHECKINPERCENT_PROPERTY = "CheckinPercent";
    public static final String THROWOUT_PROPERTY = "Throwout";
    public static final String LONGSERIES_PROPERTY = "LongSeries";
    public static final String THROWOUT_SCHEME_PROPERTY = "ThrowoutScheme";
    public static final String THROWOUTBESTX_PROPERTY = "ThrowoutBestX";
    public static final String THROWOUTPERX_PROPERTY = "ThrowoutPerX";

    protected static final double TIEBREAK_INCREMENT = 0.0001;

    public ScoringLowPoint() {
        super();
        fThrowouts.add(2);
        fThrowouts.add(0);
        fThrowouts.add(0);
    }

    public void setAttributes(ScoringModel sourceModel) {
        try {
            ScoringLowPoint that = (ScoringLowPoint) sourceModel;

            this.FirstIs75 = that.FirstIs75;
            this.fCheckinPercent = that.fCheckinPercent;
            this.fIsLongSeries = that.fIsLongSeries;
            this.fTimeLimitPenalty = that.fTimeLimitPenalty;

            this.fThrowoutScheme = that.fThrowoutScheme;
            this.fThrowoutBestX = that.fThrowoutBestX;
            this.fThrowoutPerX = that.fThrowoutPerX;

            this.fThrowouts.clear();
            this.fThrowouts.addAll(that.fThrowouts);
        } catch (java.lang.ClassCastException e) {
        }
    }

    /**
     * used to setup for known, fixed number of throwouts
     */
    public void setFixedNumberOfThrowouts(int Value) {
        if ((Value > 0) && (Value < 100)) {
            if (fThrowouts.size() < Value) {
                for (int i = fThrowouts.size(); i < Value; i++) {
                    this.fThrowouts.add(1);
                }
            }
            fThrowouts.set(Value - 1, Value);
        }
    }

    @Override
    public String toString() {
        return "ScoringLowPoint";
    }

    public static final String TIEBREAKER_PROPERTY = "tiebreaker";

    public void setLongSeries(boolean b) {
        fIsLongSeries = b;
    }

    public boolean isLongSeries() {
        return fIsLongSeries;
    }

    public List<Integer> getThrowouts() {
        return fThrowouts;
    }

    public void setThrowout(int nthrow, int min) {
        if (fThrowouts.size() <= nthrow) {
            for (int i = fThrowouts.size(); i <= nthrow; i++) {
                fThrowouts.add(i, 0);
            }
        }
        fThrowouts.set(nthrow, min);
    }

    public void setThrowouts(List<Integer> t) {
        fThrowouts = t;
    }

    public void setThrowoutScheme(int scheme) {
        fThrowoutScheme = scheme;
    }

    public int getThrowoutScheme() {
        return fThrowoutScheme;
    }

    public void setThrowoutPerX(int x) {
        fThrowoutPerX = x;
    }

    public int getThrowoutPerX() {
        return fThrowoutPerX;
    }

    public void setThrowoutBestX(int x) {
        fThrowoutBestX = x;
    }

    public int getThrowoutBestX() {
        return fThrowoutBestX;
    }

    public int getTimeLimitPenalty() {
        return fTimeLimitPenalty;
    }

    public void setTimeLimitPenalty(int p) {
        fTimeLimitPenalty = p;
    }

    public int getCheckinPercent() {
        return fCheckinPercent;
    }

    public void setCheckinPercent(int p) {
        fCheckinPercent = p;
    }

    /**
     * trivial implementation, doesn't really sort at all
     * 
     * @param obj
     * @return int
     **/
    public int compareTo(BaseObject obj) {
        return this.toString().compareTo(obj.toString());
    }

    /**
     * compares two lowpoint systems for equality of their optional settings
     * 
     * @param obj
     * @return int
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ScoringLowPoint))
            return false;
        if (this == obj)
            return true;

        ScoringLowPoint that = (ScoringLowPoint) obj;

        if (!this.toString().equals(that.toString()))
            return false;
        if (this.fCheckinPercent != that.fCheckinPercent)
            return false;
        if (this.fTimeLimitPenalty != that.fTimeLimitPenalty)
            return false;

        return this.fThrowouts.equals(that.fThrowouts);
    }

    /**
     * Given a Race, and a list of Entries calculates the RacePoints object The
     * entries should be assumed to represent a single class within the Race
     * calculateRace can assume that an Entries without a finish in the Race is DNC
     * but should recognize that the Race may well have finishers not in the
     * Entries.
     * <p>
     * Also assumes that points is pre-populated, just needs to have finish points
     * assigned
     * 
     * @param r       race to be scored
     * @param entries entries i the race
     * @param points  racepointslist in which to store the results
     * 
     * @throws ScoringException if there is a problem with the scoring
     **/
    public void scoreRace(Race r, RacePointsList points, boolean positionOnly) throws ScoringException {
        if (r.HasFleets)
            scoreRace2(r, points, FirstIs75, positionOnly);
        else
            scoreRace(r, points, FirstIs75, positionOnly);
    }

    protected void scoreRace2(Race race, RacePointsList points, boolean firstIs75, boolean positionOnly)
            throws ScoringException {
        HasFleets = race.HasFleets;
        TargetFleetSize = race.TargetFleetSize;
        IsFinalRace = race.IsFinalRace;

        // find the number of fleets in the race
        int fc = 0;
        for (RacePoints rp : points) {
            if (rp.getFinish().Fleet > fc)
                fc = rp.fFinish.Fleet;
        }

        RacePointsList rpl = new RacePointsList();

        // call scoreRace for each fleet
        for (int j = 0; j <= fc; j++) {
            for (RacePoints rp : points) {
                if (rp.getFinish().Fleet == j) {
                    rpl.add(rp);
                }
            }
            if (rpl.size() > 0)
                scoreRace(race, rpl, FirstIs75, positionOnly);
            rpl.clear();
        }
    }

    /**
     * Given a Race, and a list of Entries calculates the RacePoints object The
     * entries should be assumed to represent a single class within the Race
     * calculateRace can assume that an Entries without a finish in the Race is DNC
     * but should recognize that the Race may well have finishers not in the
     * Entries.
     * <p>
     * Also assumes that points is pre-populated, just needs to have finish points
     * assigned
     * 
     * @param r         race to be scored
     * @param entries   entries i the race
     * @param points    racepointslist in which to store the results
     * @param firstIs75 true if first place should be .75
     * 
     * @throws ScoringException if there is a problem with the scoring
     **/
    protected void scoreRace(Race r, RacePointsList points, boolean firstIs75, boolean positionOnly)
            throws ScoringException {
        // sort points on finishposition sorted top to bottom by finish
        points.sortCorrectedTimePosition();

        // loop thru the race's finishes, for each finish in entry list, set the
        // points
        double pts = (firstIs75 ? .75 : 1.0);

        int divPosition = 1; // position within the division (as opposed to
        // within the fleet)

        for (RacePoints rp : points) {
            Finish f = rp.getFinish();
            double basePts = pts;
            rp.setPosition(divPosition++);

            boolean valid = f.getFinishPosition().isValidFinish();
            boolean isdsq = f.getPenalty().isDsqPenalty();
            boolean israf = f.getPenalty().hasPenalty(Constants.RAF);

            boolean isNormalCountup = valid && (!isdsq);
            if (!ReorderRAF) {
                isNormalCountup = valid && (israf || !isdsq);
            }
            // if ( valid && !isdsq )
            if (isNormalCountup) {
                // increment number points to be assigned to next guy if this
                // guy is a valid finisher and not disqualified
                if (pts == .75)
                    pts = 1.0;
                pts++;
            } else if (!valid) {
                int newpos = f.getFinishPosition().intValue();
                rp.setPosition(newpos); // has penalty type encoded
            }
            if (f.hasPenalty()) {
                basePts = getPenaltyPoints(f.getPenalty(), points, basePts);
            }
            if (!positionOnly) {
                if (rp.isMedalRace())
                    rp.setPoints(basePts * 2);
                else if (!rp.getFinish().IsRacing)
                    rp.setPoints(0.0);
                else
                    rp.setPoints(basePts);
            }
        }

        if (!positionOnly) {
            // look for ties - must be done with corrected time
            RacePoints lastrp = null;
            List<RacePoints> tied = new ArrayList<RacePoints>(5);
            for (RacePoints rp : points) {
                if (rp.isTiedFinish(lastrp)) {
                    // boats are tied if neither has a penalty and the current
                    // boat
                    // has a valid corrected time, and its the same as the last
                    // corrected time
                    if (tied.size() == 0)
                        tied.add(lastrp);
                    tied.add(rp);
                } else if (tied.size() > 0) {
                    // coming out of set of tied boats, reset their points and
                    // clear out
                    setTiedPoints(tied);
                    tied.clear();
                }
                lastrp = rp;
            }
            // if processing tieds at end of loop
            if (tied.size() > 0)
                setTiedPoints(tied);
        }

    }

    protected void setTiedPoints(List<RacePoints> tied) {
        double pts = 0;
        for (RacePoints rp : tied) {
            pts += rp.getPoints();
        }
        pts = pts / tied.size();
        for (RacePoints rp : tied) {
            rp.setPoints(pts);
        }
    }

    /**
     * Given a Scores object, calculates the overall series points for Assume that
     * each individual race has already been calculated, and that throwouts have
     * already be designated in the points objects
     * 
     * @param races   list of races involved in the series
     * @param entries to be considered in this series
     * @param points  list of points for all races and entries (and maybe more)
     * @param series  map in which (key=entry, value=Double) series points are to be
     *                calculated.
     * 
     * @throws ScoringException if there is a problem with the scoring
     **/
    public void scoreSeries(RaceList races, EntryList entries, RacePointsList points, SeriesPointsList series)
            throws ScoringException {
        for (SeriesPoints ps : series) {
            Entry e = ps.getEntry();
            RacePointsList ePoints = points.findAll(e); // list of e's finishes
            double tot = 0;
            for (RacePoints p : ePoints) {
                if (!p.isThrowout())
                    tot += (p.getPoints());
            }
            ps.setPoints(tot);
        }
    }

    /**
     * resolve ties among a group of tied boats. A tie that is breakable should have
     * .01 point increments added as appropriate. Assume that each individual race
     * and series points have calculated, and that throwouts have already been
     * designated in the points objects.
     * <P>
     * 
     * @param races     races involved
     * @param entriesIn list of tied entries
     * @param points    list of points for all races and entries (and maybe more!)
     * @param series    map containing series points for the entries, prior to
     *                  handling ties (and maybe more than just those entries
     **/
    public void calculateTieBreakers(RaceList races, EntryList entriesIn, RacePointsList points,
            SeriesPointsList series) {

        Entry e;
        Race r;
        RacePoints rp;
        fTiebreaker = TIE_RRS_DEFAULT;

        // look into last race
        if (races.size() > 0) {
            r = races.get(races.size() - 1);
            if (r.IsFinalRace) {
                for (int j = 0; j < entriesIn.size(); j++) {
                    e = entriesIn.get(j);
                    rp = points.find(r, e);
                    if (rp != null) {
                        if (rp.getFinish().Fleet == 0)
                            fTiebreaker = TIE_RRS_A82_ONLY;
                        else
                            fTiebreaker = TIE_RRS_DEFAULT;
                    }
                }
            }
        }

        if (fTiebreaker == TIE_RRS_DEFAULT) {
            calculateTieBreakersDefault(races, entriesIn, points, series);
        } else {
            calculateTieBreakersAlternate(races, entriesIn, points, series);
        }
    }

    /**
     * does RRS App 8 without modification
     **/
    protected void calculateTieBreakersDefault(RaceList races, EntryList entriesIn, RacePointsList points,
            SeriesPointsList series) {
        // list of racepoints, 1 elist item per tied entry, item is sorted list
        // of racepoints that are not throwouts
        List<RacePointsList> eLists = new ArrayList<RacePointsList>(entriesIn.size());
        EntryList entries = (EntryList) entriesIn.clone();

        // first create separate lists of finishes for each of the tied boats.
        for (Entry e : entries) {
            RacePointsList ePoints = points.findAll(e);
            eLists.add(ePoints);
        }

        List<RacePointsList> tiedWithBest = new ArrayList<RacePointsList>(5);
        // pull out best of the bunch one at a time
        // after each scan, best is dropped with no more change
        // in points. Each remaining gets .01 added to total
        // continue until no more left to play
        while (eLists.size() > 1) {
            RacePointsList bestPoints = eLists.get(0);
            tiedWithBest.clear();

            // loop thru entries, apply tiebreaker method (comparetied)
            // keep the best (winner)
            for (int i = 1; i < eLists.size(); i++) {
                RacePointsList leftPoints = eLists.get(i);

                // compares for ties by A8.1
                int c = comparePointsBestToWorst(leftPoints, bestPoints);
                if (c < 0) {
                    bestPoints = leftPoints;
                    tiedWithBest.clear();
                } else if (c == 0) {
                    tiedWithBest.add(leftPoints);
                }
            }
            if (tiedWithBest.size() > 0) {
                // have boats tied after applying A8.1 - so send them into
                // next tiebreakers clauses
                tiedWithBest.add(bestPoints);
                compareWhoBeatWhoLast(tiedWithBest, series);
            }
            // bestPoints should now equal the best, drop it from list
            double inc = (tiedWithBest.size() + 1) * TIEBREAK_INCREMENT;
            eLists.remove(bestPoints);
            eLists.removeAll(tiedWithBest);
            incrementSeriesScores(eLists, inc, series);
        }
        // we be done
    }

    /**
     * handles alternate tiebreaker option. Currently only ability to do just App
     * A8.2 only (best score last race)
     **/
    protected void calculateTieBreakersAlternate(RaceList races, EntryList entriesIn, RacePointsList points,
            SeriesPointsList series) {
        // list of racepoints, 1 elist item per tied entry, item is sorted list
        // of racepoints that are not throwouts
        List<RacePointsList> eLists = new ArrayList<RacePointsList>(entriesIn.size());

        // first create separate lists of finishes for each of the tied boats.
        for (Entry e : entriesIn) {
            RacePointsList ePoints = points.findAll(e);
            eLists.add(ePoints);
        }

        compareWhoBeatWhoLast(eLists, series);
    }

    private RacePointsList prepBestToWorst(RacePointsList rpList) {
        RacePointsList ePoints = (RacePointsList) rpList.clone();
        // delete throwouts from the list
        for (Iterator<RacePoints> rIter = ePoints.iterator(); rIter.hasNext();) {
            RacePoints p = rIter.next();
            if (p.isThrowout())
                rIter.remove();
        }
        ePoints.sortPoints();
        return ePoints;
    }

    /**
     * compares two sets of race points for tie breaker resolution.
     * 
     * <p>
     * RRS2001 A8.1: "If there is a series score tie between two or more boats, each
     * boat's race scores shall be listed in order of best to worst, and at the
     * first point(s) where there is a difference the tie shall be broken in favor
     * of the boat(s) with the best score(s). No excluded scores shall be used."
     * 
     * @param races   races involved
     * @param inLeft  racepointslist of lefty
     * @param inRight racepointslist of right
     * @return -1 if "lefty" wins tiebreaker, 1 if righty wins, 0 if tied.
     */
    protected int comparePointsBestToWorst(RacePointsList inLeft, RacePointsList inRight) {
        RacePointsList left = prepBestToWorst(inLeft);
        RacePointsList right = prepBestToWorst(inRight);

        double lp = 0;
        double rp = 0;

        // we know they are sorted by finish points, look for first non-equal
        // finish
        for (int i = 0; i < left.size(); i++) {
            lp = left.get(i).getPoints();
            rp = right.get(i).getPoints();
            if (lp < rp)
                return -1;
            else if (rp < lp)
                return 1;
        }
        return 0;
    }

    /**
     * applying the remaining tiebreakers of RRS2001 A8 to set of boats tied after
     * comparing their list of scores. This is the new 2002+ formula after ISAF
     * deleted 8.2 and renumbered 8.3 to 8.2
     * <P>
     * RRS2001 modified A8.2 (old 8.3): "If a tie still remains between two or more
     * boats, they shall be ranked in order of their scores in the last race. Any
     * remaining ties shall be broken by using the tied boats scores in the
     * next-to-last race and so on until all ties are broken. These scores shall be
     * used even if some of them are excluded scores."
     * 
     * @param stillTied list of boat scores of the group for which A8.1 does not
     *                  resolve the tie
     * @param series    list of series scores
     */
    protected void compareWhoBeatWhoLast(List<RacePointsList> stillTied, SeriesPointsList series) {
        int nRaces = stillTied.get(0).size();
        int nTied = stillTied.size();
        EntryList tiedEntries = new EntryList();
        double[] beatenCount = new double[nTied];

        for (RacePointsList list : stillTied) {
            if (list.size() == 0)
                continue;
            list.sortRace();
            tiedEntries.add(list.get(0).getEntry());
        }

        // now look to see if anyone is STILL tied, applying A8.3 now
        // now have beatenCount, can increment an entries score
        // TIEBREAK_INCREMENT for each
        // boat in the list with a higher beaten count
        for (int e = 0; e < nTied; e++) {
            otherLoop: for (int o = 0; o < nTied; o++) {
                if ((e != o) && (beatenCount[e] == beatenCount[o])) {
                    //
                    for (int r = nRaces - 1; r >= 0; r--) {
                        double ePts = stillTied.get(e).get(r).getPoints();
                        double oPts = stillTied.get(o).get(r).getPoints();
                        if (ePts > oPts)
                            incrementSeriesScore(tiedEntries.get(e), TIEBREAK_INCREMENT, series);
                        if (ePts != oPts)
                            continue otherLoop;
                    }
                }
            }
        }
    }

    /**
     * the old algorithm, including code for the old RRS2001 A8.2 tiebreaker deleted
     * by ISAF 2002 mid year meetings
     * 
     * old and now deleted, RRS2001 A8.2: "If a tie remains between two boats, it
     * shall be broken in favor of the boat that scored better than the other boat
     * in more races. If more than two boats are tied, they shall be ranked in order
     * of the number of times each boat scored better than another of the tied
     * boats. No race for which a tied boat's score has been excluded shall be
     * used."
     * <P>
     * old RRS2001 A8.3 (now 8.2): "If a tie still remains between two or more
     * boats, they shall be ranked in order of their scores in the last race. Any
     * remaining ties shall be broken by using the tied boats scores in the
     * next-to-last race and so on until all ties are broken. These scores shall be
     * used even if some of them are excluded scores."
     * 
     * @param stillTied
     * @param series
     * @param includeThrowouts
     */
    protected void compareWhoBeatWho_old(List<RacePointsList> stillTied, SeriesPointsList series,
            boolean includeThrowouts) {
        int nRaces = stillTied.get(0).size();
        int nTied = stillTied.size();
        EntryList tiedEntries = new EntryList();
        double[] beatenCount = new double[nTied];

        for (RacePointsList list : stillTied) {
            if (list.size() == 0)
                continue;
            list.sortRace();
            tiedEntries.add(list.get(0).getEntry());
        }

        raceLoop: for (int n = 0; n < nRaces; n++) {
            // first populate the list of points for this race
            double[] points = new double[nTied];
            for (int i = 0; i < nTied; i++) {
                RacePointsList list = stillTied.get(i);
                if (list.size() == 0)
                    continue;

                RacePoints p = list.get(n);
                if (!includeThrowouts && p.isThrowout())
                    continue raceLoop; // if anyones throwout skip to next race

                points[i] = p.getPoints();
            }

            // loop thru and add to the beatenCount each time e (entry) beats o
            // (other)
            for (int e = 0; e < nTied; e++) {
                for (int o = 0; o < nTied; o++) {
                    if (points[e] < points[o])
                        beatenCount[e]++;
                }
            }
        }

        // now have beatenCount, can increment an entries score
        // TIEBREAK_INCREMENT for each
        // boat in the list with a higher beaten count
        for (int e = 0; e < nTied; e++) {
            for (int o = 0; o < nTied; o++) {
                if (beatenCount[e] < beatenCount[o]) {
                    incrementSeriesScore(tiedEntries.get(e), TIEBREAK_INCREMENT, series);
                }
            }
        }

        // now look to see if anyone is STILL tied, applying A8.3 now
        // now have beatenCount, can increment an entries score
        // TIEBREAK_INCREMENT for each
        // boat in the list with a higher beaten count
        for (int e = 0; e < nTied; e++) {
            otherLoop: for (int o = 0; o < nTied; o++) {
                if ((e != o) && (beatenCount[e] == beatenCount[o])) {
                    //
                    for (int r = nRaces - 1; r >= 0; r--) {
                        double ePts = stillTied.get(e).get(r).getPoints();
                        double oPts = stillTied.get(o).get(r).getPoints();
                        if (ePts > oPts)
                            incrementSeriesScore(tiedEntries.get(e), TIEBREAK_INCREMENT, series);
                        if (ePts != oPts)
                            continue otherLoop;
                    }
                }
            }
        }
    }

    protected void incrementSeriesScore(Entry e, double amount, SeriesPointsList series) {
        // find all series points for e, should be exactly 1
        SeriesPoints eSeries = series.findAll(e).get(0);
        // add TIEBREAK_INCREMENT to its score
        eSeries.setPoints(eSeries.getPoints() + amount);
    }

    protected void incrementSeriesScores(List<RacePointsList> eLists, double amount, SeriesPointsList series) {
        // add TIEBREAK_INCREMENT to series points of remaining tied boats
        for (RacePointsList pl : eLists) {
            if (pl.size() == 0) {
                String msg = MessageFormat.format("ScoringMessageInvalidSeries", new Object[] { eLists });
                Util.showError(this, msg, false);
            } else {
                incrementSeriesScore(pl.get(0).getEntry(), amount, series);
            }
        }
    }

    /**
     * sorts a points list as on points ascending
     * 
     * @param series points list to be sorted
     */
    public void sortSeries(SeriesPointsList series) {
        series.sortPoints();
    }

    /**
     * Given a penalty, returns the number of points to be assigned (or added)
     * 
     * @param p       penalty to be calculated, should never be null
     * @param rpList  racepointslist for whole race
     * @param basePts starting points level in case penalty is based on non-penalty
     *                points
     * @return points to be assigned for the penalty
     */
    public double getPenaltyPoints(Penalty p, RacePointsList rpList, double basePts) {
        int nEntries = 0;
        if (rpList != null) {
            nEntries = rpList.size();
        }

        if (HasFleets && TargetFleetSize > nEntries && (!IsFinalRace))
            nEntries = TargetFleetSize;

        // if MAN or RDG, return fixed points and be gone
        if (p.hasPenalty(MAN) || p.hasPenalty(RDG) || p.hasPenalty(DPI)) {
            return p.getPoints();
        }

        // A9 RACE SCORES IN A SERIES LONGER THAN A REGATTA
        // For a series that is held over a period of time longer than a
        // regatta, a boat that
        // came to the starting area but did not start (DNS), did not finish
        // (DNF), retired after finishing (RAF)
        // or was disqualified (allDSQ) shall be scored points for the finishing
        // place one more than
        // the number of boats that came to the starting area. A boat that did
        // not come to
        // the starting area (DNC) shall be scored points for the finishing
        // place one more than the
        // number of boats entered in the series.

        int numberBoatsInStartingArea = 0;
        if (rpList != null)
            numberBoatsInStartingArea = nEntries - rpList.getNumberWithPenalty(DNC)
                    - rpList.getNumberWithPenalty(NOFINISH);

        // if a DSQ, return DSQ points and be gone
        if (p.isDsqPenalty()) {
            if (rpList == null)
                return 0;
            return isLongSeries() ? (numberBoatsInStartingArea + 1) : (nEntries + 1);
        }

        if (p.hasPenalty(DNC) || p.hasPenalty(NOFINISH))
            return nEntries + 1;

        // any finish penalty other than TLE, return entries + 1 and be gone
        if (p.isFinishPenalty() && !p.hasPenalty(TLE)) {
            if (rpList == null)
                return 0;
            return isLongSeries() ? (numberBoatsInStartingArea + 1) : (nEntries + 1);
        }

        if (p.hasPenalty(TLE)) {
            int nFinishers = (rpList == null) ? 0 : rpList.getNumberFinishers();
            // set the base points to the appropriate TLE points
            switch (fTimeLimitPenalty) {
            case TLE_DNF:
                basePts = getPenaltyPoints(new Penalty(DNF), rpList, basePts);
                break;
            case TLE_FINISHERSPLUS1:
                basePts = nFinishers + 1;
                break;
            case TLE_FINISHERSPLUS2:
                basePts = nFinishers + 2;
                break;
            case TLE_AVERAGE:
                basePts = nFinishers + ((((double) nEntries) - nFinishers) / 2.0);
                break;
            default:
                basePts = getPenaltyPoints(new Penalty(DNF), rpList, basePts);
                break;
            }
        }

        // ADD in other non-finish penalties
        double dsqPoints = getPenaltyPoints(new Penalty(DSQ), rpList, basePts);
        if (p.hasPenalty(CNF))
            basePts = calcPercent(fCheckinPercent, basePts, nEntries, dsqPoints);
        if (p.hasPenalty(ZFP))
            basePts = calcPercent(20, basePts, nEntries, dsqPoints);
        if (p.hasPenalty(SCP))
            basePts = calcPercent(p.getPercent(), basePts, nEntries, dsqPoints);

        return basePts;
    }

    /**
     * returns percent of number of entries, to nearest 10th, .5 going up with a
     * maximum points of those for DNC
     * 
     * @param pct       the percent to be assigned
     * @param basePts   initial number of points
     * @param nEntries  number of entries in race
     * @param maxPoints max points to be awarded
     * @return new points value
     */
    protected double calcPercent(int pct, double basePts, double nEntries, double maxPoints) {
        // this gives points * 10
        double points = Math.round(nEntries * (pct / 10.0));
        points = points / 10.0;
        double newPoints = basePts + Math.round(points);
        if (newPoints > maxPoints)
            newPoints = maxPoints;
        return newPoints;
    }

    /**
     * returns number of throwouts to be calculated in a race
     * 
     * @param pointsList racepointslist to be looked into
     * @return number of throwouts for races
     */
    public int getNumberThrowouts(RacePointsList pointsList) {
        int nThrows = 0;
        int nRaces = pointsList.size();

        switch (fThrowoutScheme) {
        case THROWOUT_NONE:
            nThrows = 0;
            break;
        case THROWOUT_BYNUMRACES:
            for (int i = 0; i < fThrowouts.size(); i++) {
                int minRaces = fThrowouts.get(i).intValue();
                if (nRaces >= minRaces && minRaces > 0)
                    nThrows = i + 1;
            }
            break;
        case THROWOUT_PERXRACES:
            if (fThrowoutPerX > 0) {
                nThrows = (nRaces / fThrowoutPerX);
            }
            break;
        case THROWOUT_BESTXRACES:
            if (fThrowoutBestX > 0 && nRaces > fThrowoutBestX) {
                nThrows = nRaces - fThrowoutBestX;
            }
            break;
        }
        return nThrows;
    }

    /**
     * Calculates throwouts... its also the responsibility of the ScoringSystem to
     * manage the setting of throwout criteria. Assumes that prior throwout flags
     * have been cleared prior to calling this method
     * <p>
     * NOTE NOTE: if a boat has more that one race that is equal to their worse race
     * this will select their earliest "worst races" as their throwout THIS CAN, IN
     * RARE CASES, under 97 to 2000 rules this can be a problem But situation is
     * clear in 2001-2004 rules
     * 
     * @param pointsList list of race points on which to calc throwouts
     */
    public void calcThrowouts(RacePointsList pointsList) {
        // / look through the fThrowouts array and determine how many throwouts
        // to award
        int nThrows = getNumberThrowouts(pointsList);

        for (int i = 0; i < nThrows; i++) {
            RacePoints worstRace = null;
            for (RacePoints thisRace : pointsList) {
                if (thisRace.isMedalRace())
                    continue; // do not discard medal race scores

                if (!thisRace.getFinish().getPenalty().hasPenalty(DNE)
                        && !thisRace.getFinish().getPenalty().hasPenalty(DGM)
                        && !thisRace.getFinish().getPenalty().hasPenalty(AVG)) {
                    if (!thisRace.isThrowout() && !thisRace.getRace().isNonDiscardable()
                            && ((worstRace == null) || (thisRace.getPoints() > worstRace.getPoints()))) {
                        worstRace = thisRace;
                    }
                }
            }
            if (worstRace != null) {
                worstRace.setThrowout(true);
            }
        }
    }

    /**
     * generates html text to the printwriter detail scoring conditions for the
     * series at large
     * 
     * @param pw printwriter on which to print notes
     */
    public List<String> getSeriesScoringNotes(RacePointsList rp) {
        List<String> notes = new ArrayList<String>(5);

        notes.add(MessageFormat.format("ScoringNotesSystem", new Object[] { this.toString() }));

        if (fTimeLimitPenalty != TLE_DNF) {
            notes.add("ScoringNotesTimeLimit");
        }

        return notes;
    }

    /**
     * generates html text to the printwriter detail scoring conditions for a
     * particular race
     * 
     * @param pw     printwriter on which to print notes
     * @param rpList racepoints list from which to extract notes
     */
    public List<String> getRaceScoringNotes(RacePointsList rpList) {
        List<String> notes = new ArrayList<String>(5);
        return notes;
    }

    public int getTiebreaker() {
        return fTiebreaker;
    }

    public void setTiebreaker(int tiebreaker) {
        fTiebreaker = tiebreaker;
    }

    protected void setUserCanChangeTiebreaker(boolean tf) {
        fUserCanChangeTiebreaker = tf;
    }

    public boolean canUserChangeTiebreaker() {
        return fUserCanChangeTiebreaker;
    }
}
