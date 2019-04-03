package org.riggvar.base;

import java.util.ArrayList;

public class TBaseMsgQueue {
    private ArrayList<TContextMsg> FList;

    public TBaseMsgQueue() {
        FList = new ArrayList<TContextMsg>();
    }

    public void Enqueue(TContextMsg msg) {
        FList.add(msg);
    }

    public TContextMsg Dequeue() {
        if (FList.size() > 0) {
            return FList.remove(0);
        } else {
            return null;
        }
    }

    public int getCount() {
        int temp = FList.size();
        return temp;
    }
}
