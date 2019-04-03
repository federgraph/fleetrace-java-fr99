package org.riggvar.js08;

import java.util.*;

public final class ScoringBonusPointDSV extends ScoringLowPoint {
    public static final String NAME_BonusPointSystemDSV = "Bonus Point";

    public ScoringBonusPointDSV() {
    }

    @Override
    public String getName() {
        return NAME_BonusPointSystemDSV;
    }

    @Override
    protected void scoreRace(Race r, RacePointsList points, boolean firstIs75, boolean positionOnly) {
        // sort points on finish position sorted top to bottom by finish
        points.sortCorrectedTimePosition();

        int pts = 0;

        // position within the division (as opposed to within the fleet)
        int divPosition = 1;

        // loop thru the race's finishes, for each finish in the list, set the
        // points
        for (RacePoints rp : points) {
            Finish f = rp.getFinish();
            double basePts = pts;
            rp.setPosition(divPosition++);

            if ((f.getFinishPosition().isValidFinish()) && (!f.getPenalty().isDsqPenalty())) {
                // increment points to be assigned to next guy if this
                // guy is a valid finisher and not disqualified
                if (pts == 0)
                    pts = 16;
                else if (pts == 16)
                    pts = 29;
                else if (pts == 29)
                    pts = 40;
                else
                    pts = pts + 10;
            } else {
                rp.setPosition(f.getFinishPosition().intValue());
            }
            if (f.hasPenalty()) {
                basePts = getPenaltyPoints(f.getPenalty(), points, basePts);
            }
            if (!positionOnly)
                rp.setPoints(basePts / 10);
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
                    // coming out of set of tied boats, reset their points
                    // and clear out
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

    @Override
    public double getPenaltyPoints(Penalty p, RacePointsList rpList, double basePts) {
        int nEntries = 0;
        if (rpList != null) {
            nEntries = rpList.size();
        }

        double result = super.getPenaltyPoints(p, rpList, basePts);

        if (p.hasPenalty(Constants.DNS))
            result = nEntries;
        else if (p.hasPenalty(Constants.DNC))
            result = nEntries;
        else if (p.hasPenalty(Constants.DNF))
            result = nEntries;
        else if (p.isDsqPenalty())
            result = nEntries;

        return result * 10;
    }

}
