package org.riggvar.js08;

/**
 * Contains points information on an entry in a race This is separated from the
 * Finish object because when fleet scoring gets implemented an entry could have
 * more than one score for a single finish
 **/
public final class SeriesPoints extends Points {
    public static final long serialVersionUID = 1L;

    boolean fTied;

    public SeriesPoints() {
        this(null);
    }

    public SeriesPoints(Entry entry) {
        this(entry, Double.NaN, Integer.MAX_VALUE, false);
    }

    public SeriesPoints(Entry entry, double points, int pos, boolean tied) {
        super(entry, points, pos);
        if (entry != null && entry.getSailId() != null) {
            aId = entry.getSailId().toString();
        }
        fTied = tied;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof SeriesPoints))
            return false;

        if (!super.equals(obj))
            return false;

        SeriesPoints that = (SeriesPoints) obj;
        return (this.fTied == that.fTied);
    }

    @Override
    public String toString() {
        StringBuffer base = new StringBuffer();
        base.append(Util.formatDouble(getPoints(), 2));
        if (isTied())
            base.append("T");
        return base.toString();
    }

    public void setTied(boolean t) {
        fTied = t;
    }

    public boolean isTied() {
        return fTied;
    }

}
