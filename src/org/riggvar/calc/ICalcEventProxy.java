package org.riggvar.calc;

import org.riggvar.base.TStrings;
import org.riggvar.event.TEventNode;

public interface ICalcEventProxy {

    void Calc(TEventNode aqn);

    void GetScoringNotes(TStrings SL);

    boolean isWithTest();

    void setWithTest(boolean WithTest);

}
