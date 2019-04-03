package org.riggvar.js08;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public final class RacePointsList extends BaseList<RacePoints> implements Constants {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        try {
            RacePointsList that = (RacePointsList) obj;
            if (that.size() != this.size())
                return false;

            for (RacePoints rpThis : this) {
                RacePoints rpThat = that.find(rpThis.getRace(), rpThis.getEntry());
                if (rpThat == null)
                    return false;
                if (!rpThis.equals(rpThat))
                    return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * returns first (and hopefully only) entry in list for specified race and entry
     */
    public RacePoints find(Race r, Entry e) {
        for (RacePoints p : this) {
            if ((e == null) || (p.getEntry() != null) && (p.getEntry().equals(e))) {
                if ((r == null) || (p.getRace() != null) && (p.getRace().equals(r))) {
                    return p;
                }
            }
        }
        return null;
    }

    public RacePointsList findAll(Entry entry) {
        RacePointsList list = new RacePointsList();
        for (RacePoints p : this) {
            if ((p.getEntry() != null) && (p.getEntry().equals(entry))) {
                list.add(p);
            }
        }
        return list;
    }

    public RacePointsList findAll(Race race) {
        RacePointsList list = new RacePointsList();
        for (RacePoints p : this) {
            if ((p.getRace() != null) && (p.getRace().equals(race))) {
                list.add(p);
            }
        }
        return list;
    }

    /**
     * calculates number of valid finishers in this list of race points NOTE: if any
     * of the finishes are null, returns 0 NOTE: this is computationally intensive,
     * if you can go straight to the raw finish list, that is better
     */
    public int getNumberFinishers() {
        int n = 0;
        for (RacePoints pts : this) {
            if (pts.getRace() == null) {
                // if race is null, then must be series standings,
                // assume all valid
                n++;
            } else {
                Finish f = pts.getRace().getFinish(pts.getEntry());
                if (f != null && f.getFinishPosition() != null && f.getFinishPosition().isValidFinish()) {
                    n++;
                }
            }
        }
        return n;
    }

    /**
     * generates a string of elements
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("rplist=(");
        for (Iterator<RacePoints> iter = iterator(); iter.hasNext();) {
            sb.append(iter.next().toString());
            if (iter.hasNext())
                sb.append(',');
        }
        sb.append(')');
        return sb.toString();
    }

    /**
     * calculates number of valid starters in this list of race points NOTE: if any
     * of the finishes are null, returns 0 NOTE: this is computationally intensive,
     * if you can go straight to the raw finish list, that is better
     */
    public int getNumberStarters() {
        int n = 0;
        for (RacePoints pts : this) {
            if (pts.getRace() == null) {
                // if race is null, then must be series standings, assume all
                // valid
                n++;
            } else {
                Finish f = pts.getRace().getFinish(pts.getEntry());
                if (f != null && f.getFinishPosition() != null) {
                    if (f.getFinishPosition().isValidFinish()) {
                        n++;
                    } else if (!(f.getPenalty().hasPenalty(DNC) || f.getPenalty().hasPenalty(DNS))) {
                        n++;
                    }
                }
            }
        }
        return n;
    }

    /**
     * calculates number of racers with specified penalty NOTE: if any of the
     * finishes are null, returns 0 NOTE: this is computationally intensive, if you
     * can go straight to the raw finish list, that is better
     */
    public int getNumberWithPenalty(int pen) {
        int n = 0;
        for (RacePoints pts : this) {
            try {
                Finish f = pts.getRace().getFinish(pts.getEntry());
                if (f.hasPenalty(pen)) {
                    n++;
                }
            } catch (NullPointerException e) {
                // trop and ignore
            }
        }
        return n;
    }

    public void clearAll(Entry e) {
        for (Iterator<RacePoints> iter = iterator(); iter.hasNext();) {
            RacePoints p = iter.next();
            if ((p.getEntry() != null) && (p.getEntry().equals(e))) {
                iter.remove();
            }
        }
    }

    public void clearAll(Race e) {
        for (Iterator<RacePoints> iter = iterator(); iter.hasNext();) {
            RacePoints p = iter.next();
            if ((p.getRace() != null) && (p.getRace().equals(e))) {
                iter.remove();
            }
        }
    }

    /**
     * clears old points for race, and creates a new set of them, returns a
     * RacePointsList of points for this race.. AND automatically adds DNC finishes
     * for entries without finishes
     */
    public RacePointsList initPoints(Race r, EntryList entries) {
        clearAll(r);
        RacePointsList rList = new RacePointsList();
        for (Entry e : entries) {
            Finish f = r.getFinish(e);
            if (f == null) {
                f = new Finish(r, e);
                f.setFinishPosition(new FinishPosition(DNC));
                f.setPenalty(new Penalty(DNC));
                r.setFinish(f);
            }
            RacePoints rp = new RacePoints(f.getRace(), f.getEntry(), Double.NaN, false);
            this.add(rp);
            rList.add(rp);
        }
        return rList;
    }

    public void sortPointsPositionRounding() {
        Collections.sort(this, new ComparatorPointsPositionRounding());
    }

    public static class ComparatorPointsPositionRounding implements Comparator<RacePoints> {
        public int compare(RacePoints left, RacePoints right) {
            if (left == null && right == null)
                return 0;
            if (left == null)
                return -1;
            if (right == null)
                return 1;
            double l = left.getPoints();
            double r = right.getPoints();

            if (l != r)
                return ((l > r) ? 1 : -1);

            int li = left.getFinish().getFinishPosition().intValue();
            int ri = right.getFinish().getFinishPosition().intValue();

            if (li != ri)
                return ((li > ri) ? 1 : -1);

            Race race = left.getRace();
            if (race.getAllRoundings() != null) {
                for (String markName : race.getAllRoundings().keySet()) {
                    FinishList marks = race.getRoundings(markName);
                    if (marks == null)
                        return 0;

                    Entry el = left.getEntry();
                    Entry er = right.getEntry();
                    if (el == null)
                        return 1;
                    else if (er == null)
                        return -1;

                    Finish ml = marks.findEntry(el);
                    Finish mr = marks.findEntry(er);
                    li = ((ml == null) ? 99999 : ml.getFinishPosition().intValue());
                    ri = ((mr == null) ? 99999 : mr.getFinishPosition().intValue());
                    if (li != ri)
                        return ((li > ri) ? 1 : -1);
                }
            }
            return 0;
        }
    }

    public void sortPosition() {
        Collections.sort(this, new ComparatorPosition());
    }

    public static class ComparatorPosition implements Comparator<RacePoints> {
        public int compare(RacePoints left, RacePoints right) {
            if (left == null && right == null)
                return 0;
            if (left == null)
                return -1;
            if (right == null)
                return 1;

            return left.getFinish().getFinishPosition().compareTo(right.getFinish().getFinishPosition());
        }
    }

    public void sortCorrectedTimePosition() {
        Collections.sort(this, new ComparatorTimePosition());
    }

    public static class ComparatorTimePosition implements Comparator<RacePoints> {
        public int compare(RacePoints left, RacePoints right) {
            if (left == null && right == null)
                return 0;
            if (left == null)
                return -1;
            if (right == null)
                return 1;

            if (left.getFinish() == null)
                return -1;
            if (right.getFinish() == null)
                return 1;

            long ileft = left.getFinish().getCorrectedTime();
            long iright = right.getFinish().getCorrectedTime();

            if (ileft != SailTime.NOTIME && iright != SailTime.NOTIME) {
                if (ileft < iright)
                    return -1;
                if (ileft > iright)
                    return 1;
            }

            return left.getFinish().getFinishPosition().compareTo(right.getFinish().getFinishPosition());
        }
    }

    public void sortDivisionPoints() {
        Collections.sort(this, new ComparatorDivisionPoints());
    }

    public static class ComparatorDivisionPoints implements Comparator<RacePoints> {
        public int compare(RacePoints left, RacePoints right) {
            if (left == null && right == null)
                return 0;
            if (left == null)
                return -1;
            if (right == null)
                return 1;
            RacePoints r1 = left;
            RacePoints r2 = right;
            return r1.compareTo(r2);
        }
    }

    public void sortRace() {
        Collections.sort(this, new ComparatorRace());
    }

    public static class ComparatorRace implements Comparator<RacePoints> {
        public int compare(RacePoints left, RacePoints right) {
            if (left == null && right == null)
                return 0;
            if (left == null)
                return -1;
            if (right == null)
                return 1;
            try {
                return left.getRace().compareTo(right.getRace());
            } catch (NullPointerException e) {
                return 0;
            }
        }
    }

    public void sortPoints() {
        Collections.sort(this, new ComparatorPoints());
    }

    public static class ComparatorPoints implements Comparator<RacePoints> {
        public int compare(RacePoints left, RacePoints right) {
            if (left == null && right == null)
                return 0;
            if (left == null)
                return -1;
            if (right == null)
                return 1;
            int c1 = left.compareTo(right);
            if (c1 != 0)
                return c1;

            return left.getFinish().getFinishPosition().compareTo(right.getFinish().getFinishPosition());
        }
    }

}
