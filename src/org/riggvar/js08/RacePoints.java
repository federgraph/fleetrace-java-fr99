package org.riggvar.js08;

/**
 * Contains points information on an entry in a race. This is separated from the
 * Finish object because when fleet scoring gets implemented an entry could have
 * more than one score for a single finish.
 **/
public final class RacePoints extends Points {
    Race fRace;
    boolean fThrowout;
    transient Finish fFinish;

    public RacePoints() {
        this(null, null, Double.NaN, false);
    }

    public RacePoints(Race race, Entry entry) {
        this(race, entry, Double.NaN, false);
    }

    public RacePoints(Finish f) {
        this(f.getRace(), f.getEntry(), Double.NaN, false);
        fFinish = f;
    }

    public RacePoints(Race race, Entry entry, double points, boolean throwout) {
        super(entry, points, 0);
        if (entry != null && entry.getSailId() != null) {
            aId = entry.getSailId().toString(); // redundant, see Points
        }
        fRace = race;
        fThrowout = throwout;
        fFinish = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof RacePoints))
            return false;

        if (!super.equals(obj))
            return false;

        RacePoints that = (RacePoints) obj;
        if (this.fThrowout != that.fThrowout)
            return false;
        if (!Util.equalsWithNull(this.fRace, that.fRace))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean showPts) {
        Finish finish = getFinish();
        Penalty penalty = finish.getPenalty();
        StringBuffer base = new StringBuffer();

        boolean didPts = false;
        if (showPts || !finish.hasPenalty() || (penalty.isOtherPenalty())) {
            base.append(sNumFormat.format(getPoints()));
            didPts = true;
        }

        if (penalty.isDsqPenalty()) {
            Penalty ptemp = (Penalty) finish.getPenalty().clone();
            ptemp.clearPenalty(Constants.NOFINISH_MASK);
            if (didPts)
                base.append("/");
            base.append(ptemp.toString(false));
        } else if (finish.hasPenalty()) {
            if (didPts)
                base.append("/");
            base.append(penalty.toString(false));
        }

        if (fThrowout) {
            base.insert(0, '[');
            base.append(']');
        }
        return base.toString();
    }

    public void setThrowout(boolean throwout) {
        fThrowout = throwout;
    }

    public boolean isThrowout() {
        return fThrowout;
    }

    public boolean isTiedPoints(RacePoints lastrp) {
        if (lastrp == null)
            return false;
        return (Math.abs(lastrp.getPoints() - this.getPoints()) < 0.0000001);
    }

    public boolean isTiedFinish(RacePoints lastrp) {
        if (lastrp == null)
            return false;

        Finish thisF = getFinish();
        Finish lastF = lastrp.getFinish();

        if (thisF.getCorrectedTime() == SailTime.NOTIME) {
            if (lastF.getCorrectedTime() == SailTime.NOTIME) {
                // both are "no time" match on penalty
                return thisF.getPenalty() == lastF.getPenalty();
            } else {
                return false;
            }
        } else if (lastF.getCorrectedTime() == SailTime.NOTIME) {
            return false;
        } else {
            long lastTime = lastF.getCorrectedTime();
            long thisTime = thisF.getCorrectedTime();
            return (lastTime == thisTime);
        }
    }

    public Race getRace() {
        return fRace;
    }

    public Finish getFinish() {
        if (fFinish == null) {
            if (fRace == null)
                return null;
            if (getEntry() == null)
                return null;
            fFinish = fRace.getFinish(getEntry());
        }
        return fFinish;
    }

    @Override
    public void setPoints(double points) {
        double weight = (fRace == null) ? 1 : fRace.getWeight();
        super.setPoints(points * weight);
    }

    public boolean isMedalRace() {
        if (getFinish() == null || getRace() == null)
            return false;
        return getFinish().Fleet == 0 && getRace().IsFinalRace;
    }

}
