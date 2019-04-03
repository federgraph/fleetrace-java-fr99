package org.riggvar.js08;

import java.text.*;
import java.util.*;

/**
 * Contains all the information about a single regatta.
 */
public final class Regatta extends BaseObject {
    public static boolean IsInFinalPhase = false;
    private RaceList fRaces = new RaceList();
    private EntryList fEntries = new EntryList();
    private ScoringManager fScores = new ScoringManager(this);

    public Regatta() {
        super();
    }

    public int compareTo(BaseObject obj) {
        if (!(obj instanceof Regatta))
            return -1;
        if (this == obj)
            return 0;
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        try {
            Regatta that = (Regatta) obj;

            if (!Util.equalsWithNull(this.fEntries, that.fEntries))
                return false;
            if (!Util.equalsWithNull(this.fRaces, that.fRaces))
                return false;
            if (!Util.equalsWithNull(this.fScores, that.fScores))
                return false;

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        int e = (fEntries == null) ? 0 : fEntries.size();
        int r = (fRaces == null) ? 0 : fRaces.size();

        StringBuffer s = new StringBuffer("regatta");
        s.append(": ");
        s.append(MessageFormat.format("RegattaNameSuffix", new Object[] { e, // new Integer( (fEntries == null) ? 0 :
                                                                             // fEntries.size()),
                r, // new Integer( (fRaces == null) ? 0 : fRaces.size())
        }));
        return s.toString();
    }

    public RaceList getRaces() {
        if (fRaces == null)
            fRaces = new RaceList();
        return fRaces;
    }

    public void addRace(Race r) {
        fRaces.add(r);
    }

    public Race getRace(String name) {
        if (fRaces == null)
            return null;
        return fRaces.getRace(name);
    }

    public Race getRaceIndex(int i) {
        return fRaces.get(i);
    }

    public Race getRaceId(int i) {
        return fRaces.getRace(i);
    }

    public int getNumRaces() {
        return fRaces.size();
    }

    public Iterator<Race> races() {
        return fRaces.iterator();
    }

    /**
     * @TODO deprecate getallentries
     */
    public EntryList getAllEntries() {
        if (fEntries == null)
            fEntries = new EntryList();
        return fEntries;
    }

    public Entry getEntry(int id) {
        return fEntries.getEntry(id);
    }

    public Iterator<Entry> entries() {
        return getAllEntries().iterator();
    }

    public int getNumEntries() {
        return fEntries.size();
    }

    public void addEntry(Entry e) {
        fEntries.add(e);
    }

    public void removeEntry(Entry e) {
        fEntries.remove(e);
    }

    public void scoreRegatta() {
        try {
            fScores.scoreRegatta();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public ScoringManager getScoringManager() {
        return fScores;
    }

    /**
     * returns a list of all finishes found for entry e
     */
    public Map<Race, Finish> getAllFinishes(Entry e) {
        Map<Race, Finish> map = new HashMap<Race, Finish>();
        for (Race r : fRaces) {
            Finish f = r.getFinish(e);
            if (!f.isNoFinish())
                map.put(r, f);
        }
        return map;
    }

}
