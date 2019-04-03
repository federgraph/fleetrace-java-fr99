package org.riggvar.race;

import org.riggvar.penalty.TPenaltyISAF;
import org.riggvar.base.*;
import org.riggvar.bo.*;

public class TRaceBO {
    TPenaltyISAF QU = new TPenaltyISAF();

    public void EditQU(int raceIndex, int crIndex, String Value) {
        QU.Clear();
        if (Utils.Pos(",", Value) > 0) {
            QU.FromString(Value);
        } else {
            QU.Parse(Value);
        }
        TMain.BO.setPenalty(raceIndex, crIndex, QU);
    }

    public void EditDG(int raceIndex, int crIndex, String Value) {
        int DG = Utils.StrToIntDef(Value, -1);
        if (DG > -1)
            TMain.BO.setDG(raceIndex, crIndex, DG);
    }

    public void EditOTime(int raceIndex, int crIndex, String Value) {
        int MRank = Utils.StrToIntDef(Value, -1);
        if (MRank > -1)
            TMain.BO.setOT(raceIndex, crIndex, MRank);
    }

}
