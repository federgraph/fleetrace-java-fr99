package org.riggvar.js08;

/**
 * contains a list of Races and operation that apply specifically to Races
 */
public final class RaceList extends BaseList<Race> {

    private static final long serialVersionUID = 1L;

    public Race getRace(int id) {
        for (Race r : this) {
            if (r.getId() == id)
                return r;
        }
        return null;
    }

    public Race getRace(String name) {
        for (Race r : this) {
            if (r.getName().equals(name))
                return r;
        }
        return null;
    }

}
