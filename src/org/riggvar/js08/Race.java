package org.riggvar.js08;

import java.util.*;

public final class Race extends BaseObject implements Constants {
    private String fName;
    public FinishList fFinishList;
    private Map<String, FinishList> fRoundingList; // with String as key, string is "mark name",
    private Regatta fRegatta;
    private Date fStartDate;
    private boolean fLongDistance;
    private int fBFactor;
    private String fComment;
    private boolean fNonDiscardable;
    private double fWeight;

    public boolean HasFleets;
    public int TargetFleetSize;
    public boolean IsFinalRace;

    private boolean fIsRacing = true;

    public final transient static int MAX_ROUNDINGS = 10;

    private int fId;
    private static int sNextId = 1;

    public static final double DEFAULT_WEIGHT = 1.00;

    public boolean getIsRacing() {
        return fIsRacing;
    }

    public void setIsRacing(boolean value) {
        fIsRacing = value;
    }

    public long getStartTime() {
        return 0;
    }

    public int getId() {
        return fId;
    }

    public Race() {
        this(null, "");
    }

    public Race(Regatta inReg, String inName) {
        super();
        fId = sNextId++;
        fRegatta = inReg;
        fName = inName;
        fFinishList = new FinishList();
        fRoundingList = null;
        fStartDate = new Date(System.currentTimeMillis());
        fLongDistance = false;
        fComment = "";
        fWeight = DEFAULT_WEIGHT;
        fNonDiscardable = false;
    }

    public Date getStartDate() {
        return fStartDate;
    }

    public void setStartDate(Date inD) {
        fStartDate = inD;
    }

    public boolean isLongDistance() {
        return fLongDistance;
    }

    public void setLongDistance(boolean b) {
        fLongDistance = b;
    }

    public int getBFactor() {
        return fBFactor;
    }

    public void setBFactor(int b) {
        fBFactor = b;
    }

    /**
     * returns true if this race occurs after the specified race. Returns false if
     * either race is not in the list of races for the regatta.
     * 
     * @param r
     * @return
     */
    public boolean isAfter(Race r) {
        if (fRegatta == null)
            return false;

        RaceList orderedRaces = new RaceList();
        orderedRaces.addAll(fRegatta.getRaces());
        orderedRaces.sort();

        int myNum = orderedRaces.indexOf(this);
        int yourNum = orderedRaces.indexOf(r);

        return (myNum >= 0 && yourNum >= 0 && myNum > yourNum);
    }

    /**
     * sorts based on finishes WITHOUT regard to penalties except for non-finishing
     * penalties
     **/
    public int compareTo(BaseObject obj) {
        if (!(obj instanceof Race))
            return -1;
        if (this.equals(obj))
            return 0;
        Race that = (Race) obj;

        if (this.fId < that.fId)
            return -1;
        else if (this.fId == that.fId)
            return 0;
        else
            return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        try {
            return (this.fId == ((Race) obj).fId);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = fName.hashCode();
        return hash;
    }

    public void setName(String name) {
        fName = name;
    }

    public int getNextFinishNumber() {
        int n = fFinishList.size() + 1;
        for (Finish f : fFinishList) {
            if (!f.getFinishPosition().isValidFinish()) {
                n--;
            }
        }
        return n;
    }

    public void deleteRoundings() {
        if (fRoundingList != null)
            fRoundingList.clear();
    }

    public FinishList getRoundings(String markName) {
        if (fRoundingList == null) {
            fRoundingList = new TreeMap<String, FinishList>();
        }
        FinishList retList = fRoundingList.get(markName);
        if (retList == null) {
            retList = new FinishList();
            fRoundingList.put(markName, retList);
        }
        retList.syncWithEntries(this);
        return retList;
    }

    public static String[] getAllRoundingNames() {
        String[] ret = new String[MAX_ROUNDINGS];
        for (int m = 0; m < MAX_ROUNDINGS; m++) {
            ret[m] = "M" + Integer.toString(m + 1);
        }
        return ret;
    }

    public Map<String, FinishList> getAllRoundings() {
        return fRoundingList;
    }

    public boolean haveRoundings() {
        return (fRoundingList != null);
    }

    public String getName() {
        return fName;
    }

    public Regatta getRegatta() {
        return fRegatta;
    }

    /**
     * returns the entries eligible to race in this race. Currently returns entries
     * in the regatta, but this should evolve into entries in this race when
     * multiple divisions are implemented
     */
    public EntryList getEntries() {
        EntryList elist = (EntryList) fRegatta.getAllEntries().clone();
        Collection<Entry> dropem = new ArrayList<Entry>(10);
        for (Entry e : elist) {
            if (!isSailing(e))
                dropem.add(e);
        }
        elist.removeAll(dropem);
        return elist;
    }

    /**
     * return true if the specified entry should be sailing in the race
     **/
    public boolean isSailing(Entry e) {
        return true;
    }

    public void setRegatta(Regatta reg) {
        fRegatta = reg;
    }

    @Override
    public String toString() {
        if (getName().length() == 0)
            return "noname";
        else
            return getName();
    }

    public void syncFinishesWithEntries() {
        fFinishList.syncWithEntries(this);
    }

    /**
     * removes all finishes (permanently) from this race
     */
    public void clearAllFinishes() {
        fFinishList.clear();
    }

    /**
     * returns the finish for entry e in this race May return null if entry e was
     * not a valid entrant in this race If e is valid entrant but does not have a
     * finish, a finish with FinishPosition of NOFINISH is created and returned
     */
    public Finish getFinish(Entry e) {
        if (!isSailing(e))
            return null;
        Finish f = fFinishList.findEntry(e);
        if (f == null) {
            f = new Finish(this, e);
            f.setPenalty(new Penalty(NOFINISH));
        }
        return f;
    }

    /**
     * adds or replaces the finish for the f.getEntry() in this race ignores the
     * finish if e is not valid entrant
     */
    public void setFinish(Finish f) {
        Entry e = f.getEntry();
        if (e == null || !isSailing(e))
            return;
        Finish oldFinish = fFinishList.findEntry(e);
        if (oldFinish != null) {
            fFinishList.remove(oldFinish);
        }
        fFinishList.add(f);
    }

    public Iterator<Finish> finishers() {
        return fFinishList.iterator();
    }

    /**
     * returns number of finishers in divisions... just a front to the finishlist's
     * call, but avoids the sorting/synching that happens with getallfinishers call
     */
    public int getNumberFinishers() {
        return fFinishList.getNumberFinishers();
    }

    public String getComment() {
        return fComment;
    }

    public void setComment(String comment) {
        fComment = comment;
    }

    /**
     * @return Returns the isNonDiscardable.
     */
    public boolean isNonDiscardable() {
        return fNonDiscardable;
    }

    /**
     * @param isNonDiscardable The isNonDiscardable to set.
     */
    public void setNonDiscardable(boolean isNonDiscardable) {
        fNonDiscardable = isNonDiscardable;
    }

    /**
     * @return Returns the weight.
     */
    public double getWeight() {
        return fWeight;
    }

    /**
     * @param weight The weight to set.
     */
    public void setWeight(double weight) {
        fWeight = weight;
    }

}
