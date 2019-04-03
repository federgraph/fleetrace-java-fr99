package org.riggvar.js08;

import java.util.*;

public final class FinishList extends BaseList<Finish> {
    private static final long serialVersionUID = 1L;

    public int findLastValidFinish() {
        for (int i = size() - 1; i >= 0; i--) {
            Finish f = get(i);
            if (f.getFinishPosition().isValidFinish() && (f.getEntry() != null))
                return i;
        }
        return -1;
    }

    /**
     * returns a finish if found, otherwise returns null
     */
    public Finish findEntry(Entry e) {
        if (size() == 0)
            return null;
        for (Finish f : this) {
            if ((f.getEntry() != null) && (f.getEntry().equals(e))) {
                return f;
            }
        }
        return null;
    }

    public int getNumberFinishers() {
        int n = 0;
        for (Finish f : this) {
            if ((f.getEntry() != null) && (f.getFinishPosition().isValidFinish()))
                n++;
        }
        return n;
    }

    /**
     * starting with the specified finish, slides the finish number of that finish
     * and all below it "down" one, and returns a new incomplete Finish in the base
     * Finishes spot
     * 
     * @param base Finish to head the list of finishes moved down
     */
    public void insertPosition(Finish base) {
        int i = indexOf(base);
        int basePos = base.getFinishPosition().intValue();

        if (i < 0)
            return;

        for (Finish f : this) {
            FinishPosition pos = f.getFinishPosition();
            if (pos.isValidFinish() && (pos.intValue() >= basePos))
                f.setFinishPosition(new FinishPosition(pos.intValue() + 1));
        }
    }

    /**
     * returns a list of finishes sorted on finish position contains an element for
     * every entrant in the racing in the race For entrants without a finish, a
     * NOFINISH finish is included
     */
    public void syncWithEntries(Race r)
    // -- try making this static and returning a _new_ list of finishes
    {
        FinishList fin2 = new FinishList();
        for (Entry e : r.getRegatta().getAllEntries()) {
            if (r.isSailing(e)) {
                Finish f = findEntry(e);
                if (f == null)
                    f = new Finish(r, e);
                fin2.add(f);
            }
        }

        fin2.sortPosition();
        fin2.reNumber();
        fin2.sortPosition();

        clear();
        addAll(fin2);
    }

    /**
     * ensures that finish positions go from 1 to x
     */
    public void reNumber() {
        int n = 1;
        for (Finish f : this) {
            if (f.getFinishPosition().isValidFinish()) {
                if (n != f.getFinishPosition().intValue()) {
                    f.setFinishPosition(new FinishPosition(n));
                }
                n++;
            }
        }
    }

    /**
     * resorts the array by finishposition
     */
    public void sortPosition() {
        Collections.sort(this, new ComparatorPosition());
    }

    /**
     * resorts the array by finishposition
     */
    private static class ComparatorPosition implements Comparator<Finish> {
        public int compare(Finish left, Finish right) {
            if (left == null && right == null)
                return 0;
            if (left == null)
                return -1;
            if (right == null)
                return 1;

            FinishPosition fleft = left.getFinishPosition();
            FinishPosition fright = right.getFinishPosition();
            if (fleft == null && fright == null)
                return 0;
            if (fleft == null)
                return -1;
            if (fright == null)
                return 1;

            return fleft.compareTo(fright);
        }
    }

}
