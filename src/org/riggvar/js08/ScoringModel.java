package org.riggvar.js08;

import java.util.List;

/**
 * Interface that any scoring system must implement in order to be part of
 * JavaScore's systems
 **/
public interface ScoringModel {
    /**
     * returns the name of this scoring system
     */
    public String getName();

    /**
     * Given a Race, and a list of Entries, calculates the RacePoints objects. The
     * entries should be assumed to represent a single class within the Race
     * calculateRace can assume that an Entries without a finish in the Race is DNC
     * but should recognize that the Race may well have finishers not in the
     * Entries.
     * <P>
     * Can assume: (1) that any "non-finish penalties" have been properly passed
     * thru to the FinishPosition. (2) FinishPosition is otherwise sound and matches
     * finish times if any (3) All items in Entry list should be valid racers in
     * Race r (4) None of race, entries, or points is null
     * 
     * @param race         the Race to be scored
     * @param entries      a list of entries participating in the race
     * @param points       a list of racepoints in which the points should be stored
     * @param positionOnly when true do NOT recalculate race points, do race
     *                     position only
     **/
    public void scoreRace(Race race, RacePointsList points, boolean positionOnly) throws ScoringException;

    /**
     * Given a list or races, entries, points lists - calculates the overall series
     * points. Assume that each individual race has already been calculated, and
     * that throwouts have already be designated in the points objects. Do not worry
     * about tiebreakers.
     * 
     * @param races   list of races involved in the series
     * @param entries list of entries whose series totals are to be calculated
     * @param points  list of points for all races and entries (and maybe more)
     * @param series  map in which (key=entry, value=Double) series points are to be
     *                calculated.
     * 
     * @param race    the Race to be scored
     * @param entries a list of entries participating in the race
     * @param points  a list of racepoints of the points for the races involved
     * @param series  a list of series points where the series points should be
     *                stored
     **/
    public void scoreSeries(RaceList races, EntryList entries, RacePointsList points, SeriesPointsList series)
            throws ScoringException;

    /**
     * resolve ties among a group of tied boats. A tie that is breakable should have
     * .01 point increments added as appropriate. Assume that each individual race
     * and series points have calculated, and that throwouts have already be
     * designated in the points objects
     * 
     * @param races   list of involved races, in the order they were sailed
     * @param entries list of tied entries
     * @param points  list of points for all races and entries (and maybe more!)
     * @param series  map containing series points for the entries, prior to
     *                handling ties (and maybe more than just those entries
     **/
    public void calculateTieBreakers(RaceList races, EntryList entries, RacePointsList points, SeriesPointsList series);

    /**
     * Given a penalty, returns the number of points to be assigned Do NOT handle
     * AVG, it will be dealt with by ScoringManager. Note that the race could be
     * null, and base points might be 0 or NaN
     * 
     * @param p       the Penalty to be calculated
     * @param rpList  the RacePointsList of the points being calculated
     * @param basepts the points calculated before applying a penalty
     **/
    public double getPenaltyPoints(Penalty p, RacePointsList rpList, double basePts);

    /**
     * Calculates throwouts... its also the responsibility of the ScoringModel to
     * manage the setting of throwout criteria.
     * 
     * @param pointsList list of the RacePoints for all races of one entry for which
     *                   throwouts should be considered.
     */
    public void calcThrowouts(RacePointsList pointsList);

    public int getNumberThrowouts(RacePointsList pointsList);

    /**
     * Sort a list of series points from best to worst
     */
    public void sortSeries(SeriesPointsList seriesPoints);

    /**
     * generates list of notes for series scoring of specified group of race points
     * (generally a single division, might be whole fleet)
     * 
     * @param rpList the list of race points on which to generate notes
     * @return list of strings containing notes, empty list if no notes
     */
    public List<String> getSeriesScoringNotes(RacePointsList rpList);

    /**
     * generates list of notes for series scoring of specified group of race points
     * (generally a single division, might be whole fleet)
     * 
     * @param rpList the list of race points on which to generate notes
     * @return list of strings containing notes, empty list if no notes
     */
    public List<String> getRaceScoringNotes(RacePointsList rpList);

    /**
     * pulls the relative parameters from the sourceModel
     * 
     * @param sourceModel the originating model from which parameters are drawn
     */
    public void setAttributes(ScoringModel sourceModel);

}
