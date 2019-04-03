package org.riggvar.col;

import java.awt.Point;
import java.util.*;

public class TCellProps {
    private Hashtable<Point, TCellProp> ht;
    private Point p;

    public TCellProps() {
        ht = new Hashtable<Point, TCellProp>(512);
        p = new Point();
    }

    public TCellProp getItem(int aCol, int aRow) {
        p.setLocation(aCol, aRow);
        TCellProp cp = ht.get(p);
        if (cp == null) {
            cp = new TCellProp();
            Point p1 = (Point) p.clone();
            ht.put(p1, cp);
        }
        return cp;
    }
}
