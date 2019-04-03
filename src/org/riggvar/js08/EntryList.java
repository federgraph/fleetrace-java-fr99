package org.riggvar.js08;

import java.util.*;

public final class EntryList extends BaseList<Entry> {
    private static final long serialVersionUID = 1L;

    public EntryList findId(int sail) {
        EntryList list = new EntryList();

        for (Entry e : this) {
            if (e.matchesId(sail)) {
                list.add(e);
            }
        }
        return list;
    }

    public EntryList findSail(SailId sail) {
        EntryList list = new EntryList();

        for (Entry e : this) {
            if (e.matchesId(sail)) {
                list.add(e);
            }
        }
        return list;
    }

    public Entry getEntry(int id) {
        for (Entry e : this) {
            if (e.getId() == id)
                return e;
        }
        return null;
    }

    public void sortSailId() {
        Collections.sort(this, new Comparator<Entry>() {
            public int compare(Entry left, Entry right) {
                if (left == null && right == null)
                    return 0;
                if (left == null)
                    return -1;
                if (right == null)
                    return 1;
                Entry eLeft = left;
                Entry eRight = right;
                return eLeft.getSailId().compareTo(eRight.getSailId());
            }
        });
    }

    public void sort(final String property) {
        Collections.sort(this, new Comparator<Entry>() {
            public int compare(Entry left, Entry right) {
                if (left == null && right == null)
                    return 0;
                if (left == null)
                    return -1;
                if (right == null)
                    return 1;
                Entry eleft = left;
                Entry eright = right;

                return eleft.getSailId().compareTo(eright.getSailId());
            }
        });
    }

    /**
     * returns a list of duplicate sail/bow id's
     */
    public EntryList getDuplicateIds() {
        sortSailId();
        EntryList dupList = new EntryList();

        Entry laste = null;
        String lastid = null;
        for (Entry e : this) {
            String thisid = e.getSailId().toString();
            if (laste != null) {
                if (lastid.equals(thisid)) {
                    if (!dupList.contains(laste))
                        dupList.add(laste);
                }
            }
            laste = e;
            lastid = thisid;
        }
        return dupList;
    }

    // clone causes InvalidCastException in J#/dotNet
    public EntryList CloneEntries() {
        EntryList result = new EntryList();
        for (Entry e : this) {
            result.add(e);
        }
        return result;
    }
}
