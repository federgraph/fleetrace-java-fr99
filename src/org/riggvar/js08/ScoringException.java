package org.riggvar.js08;

/**
 * Covering class for scoring related exceptions
 **/
public class ScoringException extends Exception {
    private static final long serialVersionUID = 1L;

    Entry fEntry;
    Race fRace;

    public ScoringException(String msg) {
        this(msg, null, null);
    }

    public ScoringException(String msg, Race race, Entry entry) {
        super(msg);
        fEntry = entry;
        fRace = race;
    }

    public Entry getEntry() {
        return fEntry;
    }

    public Race getRace() {
        return fRace;
    }

    @Override
    public String toString() {
        return super.toString() + ", entry=" + fEntry + ", race" + fRace;
    }

}
