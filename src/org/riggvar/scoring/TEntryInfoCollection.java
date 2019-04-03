package org.riggvar.scoring;

import java.util.*;

public class TEntryInfoCollection {
    private ArrayList<TEntryInfo> EntryList;

    public TEntryInfoCollection() {
        EntryList = new ArrayList<TEntryInfo>();
    }

    public int Count() {
        return EntryList.size();
    }

    public TEntryInfo Items(int i) {
        if (i < EntryList.size()) // && i >= 0
        {
            return EntryList.get(i);
        } else {
            return null;
        }
    }

    public void Clear() {
        EntryList.clear();
    }

    public boolean Equals(TEntryInfoCollection eic) {
        if (Count() != eic.Count()) {
            return false;
        }
        for (int i = 0; i < Count(); i++) {
            TEntryInfo ei = Items(i);
            if (!ei.Equals(eic.Items(i))) {
                return false;
            }
        }
        return true;
    }

    public TEntryInfo FindKey(int SNR) {
        // foreach (TEntryInfo ei in EntryList)
        for (int i = 0; i < Count(); i++) {
            TEntryInfo ei = Items(i);
            if (ei.SNR == SNR) {
                return ei;
            }
        }
        return null;
    }

    public TEntryInfo Add() {
        TEntryInfo ei = new TEntryInfo();
        ei.Index = Count();
        EntryList.add(ei);
        return ei;
    }

}
