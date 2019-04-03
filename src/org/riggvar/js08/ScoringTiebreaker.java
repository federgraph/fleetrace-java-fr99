package org.riggvar.js08;

/**
 * covering abstract class to run tied boats through a tiebreaker looks thru
 * seriespoints lists, gathers groups of tied boats and calls an abstract class
 * to handle that group of tied boats
 */
class ScoringTiebreaker {
    protected RaceList fRaces;
    protected RacePointsList fRacePointsList;
    protected SeriesPointsList fSeriesPointsList;
    protected ScoringModel fModel;

    protected RaceList getRaces() {
        return fRaces;
    }

    public ScoringTiebreaker(ScoringModel model, RaceList rlist, RacePointsList rpl, SeriesPointsList spl) {
        fRaces = rlist;
        fRacePointsList = rpl;
        fSeriesPointsList = spl;
        fModel = model;
    }

    public void breakTies(EntryList tiedBunch) {
        fModel.calculateTieBreakers(fRaces, tiedBunch, fRacePointsList, fSeriesPointsList);
    }

    public void process() {
        EntryList tiedBunch = new EntryList();
        SeriesPoints basePoints = fSeriesPointsList.get(0);

        for (int i = 1; i < fSeriesPointsList.size(); i++) {
            SeriesPoints newPoints = fSeriesPointsList.get(i);

            if (basePoints.getPoints() == newPoints.getPoints()) {
                // have a tie, see if starting a new group
                if (tiedBunch.size() == 0) {
                    tiedBunch.add(basePoints.getEntry());
                }
                tiedBunch.add(newPoints.getEntry());
            } else {
                // this one not tie, send bunch to tiebreaker resolution
                if (tiedBunch.size() > 0) {
                    breakTies(tiedBunch);
                    tiedBunch.clear();
                }
                basePoints = newPoints;
            }
        }

        // at end of loop, see if we are tied at the bottom
        if (tiedBunch.size() > 0) {
            breakTies(tiedBunch);
        }
    }

}
