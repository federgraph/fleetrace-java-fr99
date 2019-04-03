package org.riggvar.js08;

public final class Entry extends BaseObject {
    protected transient String aSail; // just for easy look at debug stacks
    protected transient String aDiv; // just for easy look at debug stacks

    private int fId;
    private static int sNextId = 1;

    private SailId fSailId = new SailId(0);
    public int SailID;

    public int getId() {
        return fId;
    }

    public Entry() {
        super();
        fId = sNextId++;
    }

    public int compareTo(BaseObject obj) {
        if (!(obj instanceof Entry))
            return -1;
        if (this.equals(obj))
            return 0;

        Entry that = (Entry) obj;

        return compareSailID(that);
    }

    public int compareSailID(Entry that) {
        if (SailID < that.SailID)
            return -1;
        else if (SailID == that.SailID)
            return 0;
        else
            return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        try {
            return (this.fId == ((Entry) obj).fId);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return fId;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(SailID);
        return sb.toString();
    }

    public SailId getSailId() {
        return fSailId;
    }

    public void setSailID(int value) {
        fSailId.value = value;
        SailID = value;
    }

    /**
     * returns true if entry's sail id match the specified id - this does NOT check
     * bow id
     **/
    public boolean matchesId(SailId id) {
        return getSailId().equals(id);
    }

    public boolean matchesId(int snr) {
        return SailID == snr;
    }

}
