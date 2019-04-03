package org.riggvar.js08;

import java.util.*;

public final class SeriesPointsList extends BaseList<SeriesPoints> {

    private static final long serialVersionUID = 1L;

    /**
     * clears old points for race, and creates a new set of them, returns a
     * SeriesPointsList of points for this race.. AND automatically adds DNC
     * finishes for entries without finishes
     */
    public SeriesPointsList initPoints(EntryList entries) {
        clear();
        SeriesPointsList rList = new SeriesPointsList();
        for (Entry e : entries) {
            SeriesPoints rp = new SeriesPoints(e);
            this.add(rp);
            rList.add(rp);
        }
        return rList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        try {
            SeriesPointsList that = (SeriesPointsList) obj;
            if (that.size() != this.size())
                return false;

            for (SeriesPoints rpThis : this) {
                SeriesPoints rpThat = that.find(rpThis.getEntry());
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
    public SeriesPoints find(Entry e) {
        for (SeriesPoints p : this) {
            if (Util.equalsWithNull(p.getEntry(), e))
                return p;
        }
        return null;
    }

    public SeriesPointsList findAll(Entry e) {
        SeriesPointsList list = new SeriesPointsList();
        for (SeriesPoints p : this) {
            if ((p.getEntry() != null) && (p.getEntry().equals(e))) {
                list.add(p);
            }
        }
        return list;
    }

    /**
     * generates a string of elements
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("rplist=(");
        for (Iterator<SeriesPoints> iter = iterator(); iter.hasNext();) {
            sb.append(iter.next().toString());
            if (iter.hasNext())
                sb.append(',');
        }
        sb.append(')');
        return sb.toString();
    }

    public void clearAll(Entry e) {
        for (Iterator<SeriesPoints> iter = iterator(); iter.hasNext();) {
            SeriesPoints p = iter.next();
            if ((p.getEntry() != null) && (p.getEntry().equals(e))) {
                iter.remove();
            }
        }
    }

    public void sortPosition() {
        Collections.sort(this, new ComparatorPosition());
    }

    public static class ComparatorPosition implements Comparator<SeriesPoints> {
        public int compare(SeriesPoints left, SeriesPoints right) {
            if (left == null && right == null)
                return 0;
            if (left == null)
                return -1;
            if (right == null)
                return 1;

            int ileft = left.getPosition();
            int iright = right.getPosition();

            if (ileft < iright)
                return -1;
            if (ileft > iright)
                return 1;
            return 0;
        }
    }

    public void sortPoints() {
        Collections.sort(this, new ComparatorPoints());
    }

    public static class ComparatorPoints implements Comparator<SeriesPoints> {
        public int compare(SeriesPoints left, SeriesPoints right) {
            if (left == null && right == null)
                return 0;
            if (left == null)
                return -1;
            if (right == null)
                return 1;

            double ileft = left.getPoints();
            double iright = right.getPoints();

            if (ileft < iright)
                return -1;
            if (ileft > iright)
                return 1;
            return 0;
        }
    }

}
