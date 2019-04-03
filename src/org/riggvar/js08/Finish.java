package org.riggvar.js08;

public final class Finish extends BaseObject implements Constants {
    protected transient String aSail; // purely for debug ease of read
    protected transient String aRace; // purely for debug ease of read

    private Race fRace;
    private Entry fEntry;
    private FinishPosition fPosition;
    private long fFinishTime;
    private Penalty fPenalty;

    public boolean IsRacing = true;
    public int Fleet;

    public Finish() {
        this(null, null, SailTime.NOTIME, new FinishPosition(NOFINISH), new Penalty(NOFINISH));
    }

    public Finish(Race inRace, Entry inEntry) {
        this(inRace, inEntry, SailTime.NOTIME, new FinishPosition(NOFINISH), new Penalty(NOFINISH));
    }

    public Finish(Race inRace, Entry inEntry, long inTime, FinishPosition inOrder, Penalty inPenalty) {
        super();
        setRace(inRace);
        setEntry(inEntry);
        fFinishTime = inTime;
        fPosition = inOrder;
        fPenalty = ((inPenalty == null) ? new Penalty() : inPenalty);
        if (fPenalty.isFinishPenalty()) {
            fPosition = new FinishPosition(fPenalty.getPenalty());
        }
    }

    /**
     * sorts based on finishes WITHOUT regard to penalties except for non-finishing
     * penalties
     **/
    public int compareTo(BaseObject obj) {
        Finish that = (Finish) obj;

        if (this.getCorrectedTime() != SailTime.NOTIME && that.getCorrectedTime() != SailTime.NOTIME) {
            // have finish times for both boats, use that
            long delta = that.getCorrectedTime() - this.getCorrectedTime();
            if (delta < 0)
                return 1;
            else if (delta > 0)
                return -1;
            else
                return 0;
        } else {
            return fPosition.compareTo(that.fPosition);
        }
    }

    public Object getValueAt(int c) {
        switch (c) {
        case 0:
            return fPosition;
        case 1:
            return ((fEntry == null) ? "" : fEntry.getSailId().toString());
        case 2:
            return SailTime.toString(fFinishTime);
        case 3:
            return fPenalty;
        }
        return "";
    }

    public void setValueAt(Object obj, int c) {
        switch (c) {
        case 0:
            setFinishPosition((FinishPosition) obj);
            break;
        case 1: {
            Entry e = null;
            if (obj instanceof Entry) {
                e = (Entry) obj;
            }
            setEntry(e);
            break;
        }
        case 2: {
            try {
                setFinishTime(SailTime.toLong((String) obj));
            } catch (java.text.ParseException e) {
            } // ignore for now
            break;
        }
        case 3:
            setPenalty((Penalty) obj);
            break;
        default:
            break; // do nothing
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Finish))
            return false;
        if (this == obj)
            return true;

        Finish that = (Finish) obj;

        if ((fEntry == null) ? (that.fEntry != null) : !(fEntry.equals(that.fEntry)))
            return false;
        if ((fPenalty == null) ? (that.fPenalty != null) : !(fPenalty.equals(that.fPenalty)))
            return false;
        if (fFinishTime != that.fFinishTime || !fPosition.equals(that.fPosition))
            return false;

        return true;
    }

    @Override
    public Object clone() {
        Finish newGuy = (Finish) super.clone();

        // these three are not in BaseObject children
        try {
            // fEntry is intentionally not cloned
            if (fPenalty != null)
                newGuy.fPenalty = (Penalty) fPenalty.clone();
        } catch (Exception e) {
            Util.showError(e, true);
        }
        return newGuy;
    }

    public void setFinishPosition(int inVal) {
        setFinishPosition(new FinishPosition(inVal));
    }

    public void setFinishPosition(FinishPosition inVal) {
        fPosition = inVal;
        if (Penalty.isFinishPenalty(inVal.intValue())) {
            getPenalty().setFinishPenalty(inVal.intValue());
        }
    }

    public FinishPosition getFinishPosition() {
        return fPosition;
    }

    public boolean isNoFinish() {
        return fPosition.isNoFinish();
    }

    public void setPenalty(Penalty inPenalty) {
        if (inPenalty == null)
            inPenalty = new Penalty();
        fPenalty = inPenalty;
    }

    public void setEntry(Entry inEntry) {
        fEntry = inEntry;
        aSail = (fEntry == null) ? null : fEntry.getSailId().toString();
    }

    private void setRace(Race inRace) {
        fRace = inRace;
        aRace = (fRace == null) ? null : fRace.toString();
    }

    public void setFinishTime(long inTime) {
        fFinishTime = inTime;
    }

    public long getElapsedTime() {
        if (fEntry == null)
            return SailTime.NOTIME;
        long elapsed = SailTime.getElapsedTime(fRace.getStartTime(), fFinishTime);

        if (fPenalty.hasPenalty(Constants.TIM)) {
            long penTime = fPenalty.getTimePenalty();
            elapsed = elapsed + penTime;
        } else if (fPenalty.hasPenalty(Constants.TMP)) {
            int pct = fPenalty.getPercent();
            elapsed = (long) (elapsed * (1 + ((double) pct) / 100));
        }

        return elapsed;
    }

    public long getCorrectedTime() {
        if (fEntry == null)
            return SailTime.NOTIME;
        return getElapsedTime();
    }

    public Race getRace() {
        return fRace;
    }

    public Penalty getPenalty() {
        return fPenalty;
    }

    public long getFinishTime() {
        return fFinishTime;
    }

    public Entry getEntry() {
        return fEntry;
    }

    public boolean hasPenalty() {
        return (fPenalty.getPenalty() != NO_PENALTY);
    }

    public boolean hasPenalty(Penalty pen) {
        return (fPenalty.getPenalty() == pen.getPenalty());
    }

    public boolean hasPenalty(int ipen) {
        return (fPenalty.getPenalty() == ipen);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (fEntry == null) {
            sb.append("<null entry>");
            sb.append(" @ ");
            sb.append(SailTime.toString(fFinishTime));
        } else {
            sb.append(fEntry.toString());
            sb.append("/ ");
            if (fPosition != null)
                sb.append(fPosition.toString());
            if (fPenalty != null) {
                sb.append("[");
                sb.append(fPenalty.toString());
                sb.append("]");
            }
            sb.append(" @ ");
            sb.append(SailTime.toString(fFinishTime));
        }
        return sb.toString();
    }
}
