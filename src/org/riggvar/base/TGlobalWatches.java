package org.riggvar.base;

import java.util.*;

public class TGlobalWatches extends TAdapterWatches {
    public static TGlobalWatches Instance = new TGlobalWatches();

    private Vector<TAdapterWatches> FList;

    public TGlobalWatches() {
        FList = new Vector<TAdapterWatches>();
    }

    public void Subscribe(TAdapterWatches Subject) {
        FList.add(Subject);
    }

    public void UnSubscribe(TAdapterWatches Subject) {
        FList.remove(Subject);
    }

    @Override
    public void Update(int LabelID) {
        for (int i = 0; i < FList.size() - 1; i++)
            (FList.get(i)).Update(LabelID);
    }
}
