package org.riggvar.calc;

import org.riggvar.event.*;
import org.riggvar.base.*;

/**
 * do nothing (abstract) base class implementing ICalcEventProxy
 */
public class TCalcEventProxy implements ICalcEventProxy {
    private boolean WithTest;

    public void Calc(TEventNode aqn) {
    }

    public void GetScoringNotes(TStrings SL) {
        SL.Add("TCalcEventProxy.GetScoringNotes");
    }

    public boolean isWithTest() {
        return WithTest;
    }

    public void setWithTest(boolean WithTest) {
        this.WithTest = WithTest;
    }

}
