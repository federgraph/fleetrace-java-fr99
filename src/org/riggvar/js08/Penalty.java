package org.riggvar.js08;

/**
 * Class for storing penalty settings. NOTE this class is responsible only for
 * specifying the penalty assignments: NOT for determining the points to be
 * assigned. See @ScoringSystems for changing penalties into points.
 * <P>
 * There are three sets of penalties supported in this class: <br>
 * - NonFinishPenalties: penalties that can be assigned to boats that have not
 * finished a race. Examples include DNC, DNS <br>
 * - Disqualification Penalties: penalties that override other penalties and
 * involve some variant of causing a boat's finish to be ignored. Examples
 * include, DSQ, OCS <br>
 * - ScoringPenalties: penalties that may accumulate as various "hits" on a
 * boat's score. Examples include SCP, ZFP
 * <p>
 * Although it is unusual a boat may have more than one penalty applied. For
 * example a boat may get a Z Flag penalty and a 20 Percent penalty. Or a boat
 * may miss a finish time window and still be scored with a 20 Percent penalty.
 * <p>
 * In general, a boat can have a Non-Finish penalty AND any other penalty
 * applied And the scoring penalties can accumulate. But the disqualification
 * penalties do not accumulate and will override other penalties assigned
 */
public final class Penalty extends BaseObject implements Constants {
    public static Penalty[] getAllNonFinishPenalties() {
        return new Penalty[] { new Penalty(DNC), new Penalty(DNS), new Penalty(DNF), new Penalty(TLE) };
    }

    /**
     * contains the percentage assigned if a SCP penalty is set
     */
    private int fPercent;

    /**
     * contains the Points to be awarded for RDG and MAN penalties
     */
    private double fPoints;

    /**
     * contains the amount (if any) of a elapsed time penalty
     */
    private long fTimePenalty;

    /**
     * contains the penalties assigned. This is a "bit-wise" field, each bit
     * represents a different penalty
     */
    private int fPenalty;

    /**
     * contains a user-entered note to be saved in accordance with this penalty
     */
    private String fNote;

    /**
     * default constructor, creates and empty penalty
     */
    public Penalty() {
        this(NO_PENALTY);
    }

    /**
     * default constructor, creates and empty penalty
     */
    public Penalty(int pen) {
        super();
        fPenalty = pen;
        fPercent = 0;
        fPoints = 0;
        fTimePenalty = 0;
        fNote = "";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        try {
            Penalty that = (Penalty) obj;
            if (this.fPenalty != that.fPenalty)
                return false;
            if (this.fPercent != that.fPercent)
                return false;
            if (this.fPoints != that.fPoints)
                return false;
            if ((fNote == null) ? (that.fNote != null) : !(fNote.equals(that.fNote)))
                return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int compareTo(BaseObject obj) {
        if (!(obj instanceof Penalty))
            return -1;
        if (this.equals(obj))
            return 0;

        Penalty that = (Penalty) obj;

        // so far all penalties are equal
        if (that.fPenalty > this.fPenalty)
            return -1;
        else if (that.fPenalty < this.fPenalty)
            return 1;
        else if (that.fPercent > this.fPercent)
            return -1;
        else if (that.fPercent < this.fPercent)
            return 1;
        else if (that.fPoints > this.fPoints)
            return -1;
        else if (that.fPoints < this.fPoints)
            return 1;
        else
            return this.fNote.compareTo(that.fNote);
    }

    /**
     * Adds the specified penalty to the set of other penalties applied All other
     * penalties remain
     */
    public int addOtherPenalty(int newPen) {
        newPen = newPen & OTHER_MASK; // mask out stray bits out of Other area
        fPenalty = fPenalty | newPen;
        return fPenalty;
    }

    /**
     * replaces the finish penalty leaving others alone
     */
    public void setFinishPenalty(int newPen) {
        fPenalty = fPenalty & (NOFINISH_MASK ^ 0xFFFF); // clear previous finish
        // bits
        fPenalty = fPenalty | (newPen & NOFINISH_MASK); // add in to finish
        // penalty bits
    }

    /**
     * replaces the disqualification penalty leaving others alone
     */
    public void setDsqPenalty(int newPen) {
        fPenalty = fPenalty & (DSQ_MASK ^ 0xFFFF); // clear previous finish bits
        fPenalty = fPenalty | (newPen & DSQ_MASK); // add in to finish penalty
        // bits
    }

    /**
     * Clears the specified penalty in the set of penalties applied
     */
    public int clearPenalty(int newPen) {
        int notPen = 0xFFFF ^ newPen;
        fPenalty = (fPenalty & notPen);
        return fPenalty;
    }

    /**
     * Replaces the current penalty settings with the specified penalty resets
     * percentage and manual points to 0
     */
    public void setPenalty(int newPen) {
        fPenalty = newPen;
        fPercent = 0;
        fPoints = 0;
    }

    /**
     * Replaces the current percentage penalty amount with the specified amount
     * leaves other penalty settings alone, does NOT light the SCP penalty flag
     */
    public void setPercent(int newPen) {
        fPercent = newPen;
    }

    /**
     * Replaces the current manual points with the specified points, does NOT light
     * the MAN or RDG flag
     */
    public void setPoints(double newPen) {
        fPoints = newPen;
    }

    /**
     * Replaces the current manual points with the specified points, does NOT light
     * the MAN or RDG flag
     */
    public void setTimePenalty(long time) {
        fTimePenalty = time;
    }

    /**
     * Replaces the current manual points with the specified points, resets
     * percentage and other penalties to zero
     */
    public void setNote(String n) {
        fNote = n;
    }

    public boolean hasPenalty(int inPen) {
        // presumes that inPen is a "simple" penalty, no a combination penalty,
        // fPenalty might well be a combination
        if (isOtherPenalty(inPen)) {
            return (fPenalty & inPen & OTHER_MASK) != 0; // AND of the two in
            // the other range
            // should return
            // good
        } else if (isDsqPenalty(inPen)) {
            return (fPenalty & DSQ_MASK) == (inPen & DSQ_MASK);
        } else if (isFinishPenalty(inPen)) {
            return (fPenalty & NOFINISH_MASK) == (inPen & NOFINISH_MASK);
        }
        return (inPen == fPenalty);
    }

    public void clear() {
        fPoints = 0;
        fPercent = 0;
        setPenalty(NO_PENALTY);
    }

    public int getPenalty() {
        return fPenalty;
    }

    public int getPercent() {
        return fPercent;
    }

    public double getPoints() {
        return fPoints;
    }

    public long getTimePenalty() {
        return fTimePenalty;
    }

    public String getNote() {
        return fNote;
    }

    // ==================
    // Static methods
    // ==================

    public boolean isFinishPenalty() {
        return isFinishPenalty(fPenalty);
    }

    public boolean isDsqPenalty() {
        return isDsqPenalty(fPenalty);
    }

    public boolean isOtherPenalty() {
        return isOtherPenalty(fPenalty);
    }

    public static boolean isFinishPenalty(int pen) {
        return ((pen & NOFINISH_MASK) != 0);
    }

    public static boolean isDsqPenalty(int pen) {
        return ((pen & DSQ_MASK) != 0);
    }

    public static boolean isOtherPenalty(int pen) {
        return ((pen & OTHER_MASK) != 0);
    }

    /**
     * runs thru each bit, adds the string of that bit
     */
    @Override
    public String toString() {
        return toString(this, true);
    }

    /**
     * runs thru each bit, adds the string of that bit
     */
    public String toString(boolean showPts) {
        return toString(this, showPts);
    }

    public static String toString(Penalty inP) {
        return toString(inP, true);
    }

    public static String toString(Penalty inP, boolean showPts) {
        int pen = inP.getPenalty();
        if (pen == 0)
            return "";

        StringBuffer sb = new StringBuffer();
        if ((inP.getPenalty() & NOFINISH_MASK) != 0) {
            sb.append(FinishPosition.toString(pen & NOFINISH_MASK));
            sb.append(",");
        }

        pen = (inP.getPenalty() & Constants.DSQ_MASK);
        if (pen == DSQ)
            sb.append("DSQ,");
        if (pen == DGM)
            sb.append("DGM,");
        if (pen == DNE)
            sb.append("DNE,");
        if (pen == RAF)
            sb.append("RAF,");
        if (pen == OCS)
            sb.append("OCS,");
        if (pen == BFD)
            sb.append("BFD,");

        pen = (inP.getPenalty() & Constants.OTHER_MASK);
        if ((pen & CNF) != 0)
            sb.append("CNF,");
        if ((pen & ZFP) != 0)
            sb.append("ZFP,");
        if ((pen & AVG) != 0)
            sb.append("AVG,");
        if ((pen & SCP) != 0) {
            sb.append(Integer.toString(inP.getPercent()));
            sb.append("%,");
        }
        if ((pen & MAN) != 0) {
            sb.append("MAN");
            if (showPts) {
                sb.append("/");
                sb.append(Double.toString(inP.getPoints()));
            }
            sb.append(",");
        }
        if ((pen & RDG) != 0) {
            sb.append("RDG");
            if (showPts) {
                sb.append("/");
                sb.append(Double.toString(inP.getPoints()));
            }
            sb.append(",");
        }

        if ((pen & TIM) != 0) {
            sb.append("TIM");
            if (showPts) {
                sb.append("/");
                sb.append(SailTime.toString(inP.getTimePenalty()));
            }
            sb.append(",");
        }

        if ((pen & TMP) != 0) {
            sb.append("TMP");
            if (showPts) {
                sb.append("/");
                sb.append(inP.getPercent());
                sb.append("%");
            }
            sb.append(",");
        }

        String r = sb.toString();
        if ((r.length() > 0) && (r.substring(r.length() - 1).equals(","))) {
            r = r.substring(0, r.length() - 1);
        }
        return r;
    }

    public static String toString(int pen) {
        return toString(pen, true);
    }

    public static String toString(int pen, boolean doShort) {
        return toString(new Penalty(pen), doShort);
    }

    public static void parsePenalty(Penalty pen, String penString) throws IllegalArgumentException {
        Penalty newpen = parsePenalty(penString);
        pen.fPoints = newpen.fPoints;
        pen.fPenalty = newpen.fPenalty;
        pen.fPercent = newpen.fPercent;
        pen.fTimePenalty = newpen.fTimePenalty;
    }

    public static Penalty parsePenalty(String origPen) throws IllegalArgumentException {
        String pen = origPen.toUpperCase();

        if (pen.length() == 0)
            return new Penalty(NO_PENALTY);
        if (pen.indexOf(",") >= 0) {
            // have comma(s) recurse thru each comma adding the penalties
            int leftc = 0;
            Penalty newpen = new Penalty();

            while (leftc <= pen.length()) {
                int rightc = pen.indexOf(",", leftc);
                if (rightc < 0)
                    rightc = pen.length();
                String sub = pen.substring(leftc, rightc);
                Penalty addpen = parsePenalty(sub);
                if (addpen.isOtherPenalty()) {
                    newpen.addOtherPenalty(addpen.getPenalty());

                    if (addpen.hasPenalty(MAN) || addpen.hasPenalty(RDG)) {
                        newpen.setPoints(addpen.getPoints());
                    }
                    if (addpen.hasPenalty(TMP) || addpen.hasPenalty(SCP)) {
                        newpen.setPercent(addpen.getPercent());
                    }
                    if (addpen.hasPenalty(TIM)) {
                        newpen.setTimePenalty(addpen.getTimePenalty());
                    }
                } else if (addpen.isDsqPenalty()) {
                    newpen.setDsqPenalty(addpen.getPenalty());
                } else if (addpen.isFinishPenalty()) {
                    newpen.setFinishPenalty(addpen.getPenalty());
                }
                leftc = rightc + 1;
            }
            return newpen;
        }

        int slash = pen.indexOf("/");
        String val = "";
        if (slash >= 0) {
            val = pen.substring(slash + 1);
            pen = pen.substring(0, slash);
        }

        if (pen.equals("DSQ"))
            return new Penalty(DSQ);

        if (pen.equals("DGM"))
            return new Penalty(DGM);

        if (pen.equals("DNE"))
            return new Penalty(DNE);
        if (pen.equals("DND"))
            return new Penalty(DNE);

        if (pen.equals("RAF"))
            return new Penalty(RAF);
        if (pen.equals("RET"))
            return new Penalty(RAF);

        if (pen.equals("OCS"))
            return new Penalty(OCS);
        if (pen.equals("PMS"))
            return new Penalty(OCS);

        if (pen.equals("BFD"))
            return new Penalty(BFD);
        if (pen.equals("CNF"))
            return new Penalty(CNF);

        if (pen.equals("ZPG"))
            return new Penalty(ZFP);
        if (pen.equals("ZFP"))
            return new Penalty(ZFP);

        if (pen.equals("AVG"))
            return new Penalty(AVG);

        if (pen.equals("DNC"))
            return new Penalty(DNC);
        if (pen.equals("DNS"))
            return new Penalty(DNS);

        if (pen.equals("DNF"))
            return new Penalty(DNF);
        if (pen.equals("WTH"))
            return new Penalty(DNF); // 2001

        if (pen.equals("TLE"))
            return new Penalty(TLE);
        if (pen.equals("TLM"))
            return new Penalty(TLE);

        if (pen.endsWith("%")) {
            Penalty pctPen = new Penalty(SCP);
            try {
                int pct = Integer.parseInt(pen.substring(0, pen.length() - 1));
                pctPen.setPercent(pct);
            } catch (Exception dontcare) {
            }
            return pctPen;
        }

        if (pen.startsWith("P")) {
            Penalty pctPen = new Penalty(SCP);
            try {
                int pct = Integer.parseInt(pen.substring(1));
                pctPen.setPercent(pct);
                return pctPen;
            } catch (Exception dontcare) {
            }
        }

        if (pen.equals("TIM")) {
            Penalty penalty = new Penalty(TIM);
            penalty.setTimePenalty(SailTime.forceToLong(val));
            return penalty;
        }

        if (pen.equals("RDG") || pen.equals("RDR") || pen.equals("MAN")) {
            Penalty penalty = (pen.startsWith("MAN") ? new Penalty(MAN) : new Penalty(RDG));
            // assume is form "MAN/<points>"
            try {
                double pts = Double.parseDouble(val);
                penalty.setPoints(pts);
            } catch (Exception e) {
            }
            return penalty;
        }

        if (pen.equals("TMP") || pen.equals("SCP") || pen.equals("PCT")) {
            Penalty penalty = (pen.equals("TMP") ? new Penalty(TMP) : new Penalty(SCP));
            // assume is form "MAN/<points>"
            try {
                int pct = Integer.parseInt(val);
                penalty.setPercent(pct);
            } catch (Exception e) {
            }
            return penalty;
        }

        throw new java.lang.IllegalArgumentException("Unable to parse penalty, pen=" + pen);
    }
}
