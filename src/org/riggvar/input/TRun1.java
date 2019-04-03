package org.riggvar.input;

import org.riggvar.base.*;

public class TRun1 extends TRun {
    public TStartlist Startlist;

    public TRun1(TBaseToken aOwner, String aNameID) {
        super(aOwner, aNameID);
        Startlist = new TStartlist(this, "STL");
    }
}
