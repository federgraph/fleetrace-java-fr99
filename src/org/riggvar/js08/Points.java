package org.riggvar.js08;

import java.text.DecimalFormat;

/**
 * Contains base points information, abstract class implements as RacePoints and
 * SeriesPoints
 **/
abstract class Points extends BaseObject {
    private Entry fEntry;
    private double fPoints;
    protected int fPosition;
    protected final static DecimalFormat sNumFormat;

    protected transient String aId; // for debugging

    static {
        sNumFormat = new DecimalFormat();
        sNumFormat.setMaximumFractionDigits(2);
        sNumFormat.setMinimumFractionDigits(0);
    }

    protected Points(Entry entry, double points, int pos) {
        fEntry = entry;
        fPoints = points;
        fPosition = pos;
        if (fEntry != null)
            aId = fEntry.getSailId().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Points))
            return false;

        Points that = (Points) obj;
        if (this.fPoints != that.fPoints)
            return false;
        if (this.fPosition != that.fPosition)
            return false;
        if (!Util.equalsWithNull(this.fEntry, that.fEntry))
            return false;
        return true;
    }

    public void setPoints(double points) {
        fPoints = points;
    }

    public double getPoints() {
        return fPoints;
    }

    public int getPosition() {
        return fPosition;
    }

    public void setPosition(int pos) {
        fPosition = pos;
    }

    public Entry getEntry() {
        return fEntry;
    }

    /**
     * returns value based on the points, if points are equal, returns 0 ignores the
     * throwout
     */
    public int compareTo(BaseObject obj) {
        if (obj == null)
            return -1;
        try {
            Points that = (Points) obj;
            if (this.fPoints < that.fPoints)
                return -1;
            else if (this.fPoints > that.fPoints)
                return 1;
            else
                return 0;
        } catch (java.lang.ClassCastException e) {
            return -1;
        }
    }

}
