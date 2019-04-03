package org.riggvar.js08;

/**
 * Class for storing finish position numbers. This is mostly an integer with the
 * raw finish order, but also handles non-finish values such as DNC, and DNF.
 * NOTE this class is responsible only for specifying the finish numbers, NOT
 * for determining the points to be assigned. See @ScoringSystems for changing
 * penalties into points.
 * <P>
 * See also the @Penalty class for handling of penalties assigned to boats
 * (whether or not they have a valid finish).
 * 
 * to set a new finish position, recreate the instance
 */
public final class FinishPosition extends BaseObject implements Constants {
    private int fFinishPosition;

    public FinishPosition(int inPen) {
        if ((inPen & NOFINISH_MASK) != 0) // setting to non-finish penalty...
                                          // mask out other bits
        {
            fFinishPosition = inPen & NOFINISH;
        } else {
            fFinishPosition = inPen;
        }
    }

    public static int parseString(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            try {
                return Penalty.parsePenalty(value).getPenalty();
            } catch (Exception x) {
                return NOFINISH;
            }
        }
    }

    public int compareTo(BaseObject obj) {
        if (!(obj instanceof FinishPosition))
            return -1;

        FinishPosition that = (FinishPosition) obj;
        if (this.fFinishPosition < that.fFinishPosition)
            return -1;
        else if (this.fFinishPosition > that.fFinishPosition)
            return 1;
        else
            return 0;
    }

    public boolean isFinisher() {
        return ((fFinishPosition < HIGHEST_FINISH) && (fFinishPosition > 0));
    }

    public boolean isNoFinish() {
        return fFinishPosition == NOFINISH;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FinishPosition))
            return false;
        return fFinishPosition == ((FinishPosition) obj).fFinishPosition;
    }

    public int intValue() {
        return fFinishPosition;
    }

    public boolean isValidFinish() {
        return isValidFinish(fFinishPosition);
    }

    public static boolean isValidFinish(int order) {
        return (order <= HIGHEST_FINISH);
    }

    @Override
    public String toString() {
        return toString(fFinishPosition, true);
    }

    public static String toString(int order) {
        return toString(order, true);
    }

    public static String toString(int order, boolean doShort) {
        switch (order) {
        case NOFINISH:
            return "NoFin";
        case DNC:
            return "DNC";
        case DNS:
            return "DNS";
        case DNF:
            return "DNF";
        case TLE:
            return "TLE";
        default:
            return Integer.toString(order);
        }
    }
}
