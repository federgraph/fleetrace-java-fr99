package org.riggvar.col;

import java.util.*;
import org.riggvar.base.*;

public class TDisplayOrderList {
    TreeMap<String, Integer> tm = new TreeMap<String, Integer>();

    private static int fID;
    Object[] v; // vector of BaseID integers in order of rows

    @Override
    public String toString() {
        String s;
        StringBuffer sb = new StringBuffer();
        Iterator<String> it = tm.keySet().iterator();
        while (it.hasNext()) {
            s = it.next();
            sb.append(s);
            sb.append(", ");
        }
        for (int i = 0; i < v.length; i++) {
            sb.append(v[i].toString());
            sb.append(", ");
        }
        return sb.toString();
    }

    public void Sort() {
        v = tm.values().toArray();
    }

    public int getByIndex(int index) {
        if (index >= 0 && index < v.length) {
            return ((Integer) v[index]).intValue();
        } else {
            return index;
        }
    }

    public void Add2(String aKey, Integer aValue) {
        // make key unique with 6 digit suffix
        try {
            fID++;
            String s = String.valueOf(fID);
            s = Utils.PadLeft(s, 6, '0');

            StringBuffer sb = new StringBuffer();
            sb.append(aKey);
            sb.append(s);
            String o = sb.toString();
            sb = null;
            tm.put(o, aValue);
        } catch (Exception e) {
        }
    }

    public void clear() {
        tm.clear();
    }

    public int size() {
        return tm.size();
    }

}
