package org.riggvar.event;

import org.riggvar.stammdaten.*;

import java.util.*;
import org.riggvar.base.*;
import org.riggvar.bo.*;
import org.riggvar.col.*;

public class TEventNode extends
        TBaseNode<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp>
        implements IBaseNode {
    public static final int Layout_Points = 0;
    public static final int Layout_Finish = 1;

    private INotifyEvent FOnCalc;
    private int FShowPoints;
    public boolean ShowPLZColumn;
    public boolean ShowPosRColumn = true;
    public boolean UseFleets;
    public int TargetFleetSize = 8;
    public int PartialCalcLastRace;
    private int FFirstFinalRace = 20;

    public TColorMode ColorMode = TColorMode.ColorMode_Error;

    public TStammdatenRowCollection StammdatenRowCollection;
    public TOTimeErrorList ErrorList;
    public int WebLayout;

    public TEventNode(TEventBO cbo, String aNameID) {
        super();
        BO = cbo;
        NameID = aNameID;
        setBaseRowCollection(new TEventRowCollection());
        getBaseRowCollection().Owner = this;
        ErrorList = new TOTimeErrorList();
    }

    public void Load() {
        TEventRowCollection cl = getBaseRowCollection();

        TEventRowCollectionItem o;
        cl.clear();

        o = cl.AddRow();
        o.SNR = 1001;
        o.Bib = 1;
        o.BaseID = 1;

        o = cl.AddRow();
        o.SNR = 1002;
        o.Bib = 2;
        o.BaseID = 2;

        o = cl.AddRow();
        o.SNR = 1003;
        o.Bib = 3;
        o.BaseID = 3;
    }

    public void Init(int RowCount) {
        this.getBaseRowCollection().clear();
        TEventRowCollectionItem o;
        for (int i = 0; i < RowCount; i++) {
            o = this.getBaseRowCollection().AddRow();
            o.BaseID = i + 1;
            o.SNR = 1000 + i + 1;
            o.Bib = i + 1;
        }
    }

    public void CopyFleet(int r) {
        UseFleets = true;
        TEventRowCollection cl = getBaseRowCollection();
        TEventRowCollectionItem cr;
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            if (r > 1 && r < getRCount())
                cr.Race[r].Fleet = cr.Race[r - 1].Fleet;
        }
    }

    public void DisableFleet(int r, int f, boolean b) {
        TEventRowCollection cl = getBaseRowCollection();
        TEventRowCollectionItem cr;
        if (r > 0 && r < getRCount() && UseFleets) {
            for (int i = 0; i < cl.getCount(); i++) {
                cr = cl.get(i);
                if (cr.Race[r].Fleet == f)
                    cr.Race[r].IsRacing = b;
            }
        }
    }

    public boolean IsFinalRace(int r) {
        return (FFirstFinalRace > 0 && r >= FFirstFinalRace);
    }

    public int getFirstFinalRace() {
        if (FFirstFinalRace == 0)
            return getRCount();
        else
            return FFirstFinalRace;
    }

    public void setFirstFinalRace(int value) {
        FFirstFinalRace = value;
    }

    public int FleetMaxProposed(int r) {
        int fc = 0;
        if (r > 0 && r < getRCount() && TargetFleetSize > 0) {
            TEventRowCollection cl = getBaseRowCollection();
            fc = cl.getCount() / TargetFleetSize; // cl.Count div TargetFleetSize;
            if (TargetFleetSize > 0 && cl.getCount() > 0) {
                while (TargetFleetSize * fc < cl.getCount())
                    fc++;
            }
        }
        return fc;
    }

    public int FleetMax(int r) {
        int result = 0;
        if (r > 0 && r < getRCount()) {
            TEventRowCollection cl = getBaseRowCollection();
            TEventRowCollectionItem cr;
            for (int i = 0; i < cl.getCount(); i++) {
                cr = cl.get(i);
                if (cr.Race[r].Fleet > result)
                    result = cr.Race[r].Fleet;
            }
        }
        return result;
    }

    public int FillFleetList(int r, int f, List<TEventRowCollectionItem> L) {
        int result = 0;
        if (r > 0 && r < getRCount()) {
            TEventRowCollection cl = getBaseRowCollection();
            TEventRowCollectionItem cr;
            for (int i = 0; i < cl.getCount(); i++) {
                cr = cl.get(i);
                if (cr.Race[r].Fleet == f)
                    L.add(cr);
            }
        }
        return result;
    }

    public TEventRowCollectionItem FindBib(int Bib) {
        if (Bib == 0) {
            return null;
        }
        TEventRowCollection cl = getBaseRowCollection();
        TEventRowCollectionItem cr;
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            if (cr.Bib == Bib) {
                return cr;
            }
        }
        return null;
    }

    public void PartialCalc(int r) {
        PartialCalcLastRace = r;
        TMain.BO.CalcEV.Calc(this);
        setModified(false);
        if (FOnCalc != null)
            FOnCalc.HandleNotifyEvent(this);
        ErrorList.CheckAll(this);
        PartialCalcLastRace = 0;
    }

    public void InitFleet(int r) {
        UseFleets = true;

        int fc = FleetMaxProposed(r);
        int f, c;
        boolean upPhase;

        TEventRowCollection cl = getBaseRowCollection();
        TEventRowCollectionItem cr;
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            c = i % fc;

            // upPhase = not Odd(i div fc);
            upPhase = ((i / fc) % 2) >= 0;

            if (upPhase)
                f = c + 1;
            else
                f = fc - c;

            if (r == 1)
                cr.Race[r].Fleet = f;
            else if (r > 1 && r < getRCount()) {
                cr = cl.get(cr.PLZ());
                cr.Race[r].Fleet = f;
            }
        }
    }

    public void InitFleetByFinishHack(int r) {
        TEventRowCollection cl;
        TEventRowCollectionItem cr;
        TEventRaceEntry ere;

        int fc = FleetMaxProposed(r);
        if (r > 0 && r < getRCount() && TargetFleetSize > 0 && fc > 0) {
            UseFleets = true;
            cl = getBaseRowCollection();
            // clear fleet assignment
            for (int j = 0; j < cl.getCount(); j++) {
                cr = cl.get(j);
                ere = cr.Race[r];
                ere.Fleet = 0;
            }
            // generate new from existing finish position info
            // Fleet f, FinishPosition fp
            int f;
            for (int fp = 1; fp <= TargetFleetSize; fp++) {
                f = 1;
                for (int j = 0; j < cl.getCount(); j++) {
                    cr = cl.get(j);
                    ere = cr.Race[r];
                    if (ere.OTime == fp && ere.Fleet == 0) {
                        ere.Fleet = f;
                        f++;
                    }
                    if (f == fc + 2)
                        break;
                }
            }
        }
    }

    @Override
    public void Calc() {
        TMain.BO.CalcEV.Calc(this);
        setModified(false);
        if (FOnCalc != null) {
            FOnCalc.HandleNotifyEvent(this);
        }
        ErrorList.CheckAll(this);
    }

    public INotifyEvent getOnCalc() {
        return FOnCalc;
    }

    public void setOnCalc(INotifyEvent value) {
        FOnCalc = value;
    }

    public int getShowPoints() {
        if (WebLayout > 0) {
            return WebLayout;
        } else {
            return FShowPoints;
        }
    }

    public void setShowPoints(int value) {
        FShowPoints = value;
    }

    public int getRCount() {
        return getRaceCount() + 1;
    }

    public int getRaceCount() {
        if (TMain.BO != null) {
            return TMain.BO.BOParams.RaceCount;
        } else {
            return -1;
        }
    }

    public void ClearList() {
        getBaseRowCollection().ClearList();
    }

    public void ClearResult() {
        getBaseRowCollection().ClearResult();
    }

    public void GoBackToRace(int r) {
        TEventRowCollection cl = getBaseRowCollection();
        TEventRowCollectionItem cr;
        cl.ClearRace(r);
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.getItem(i);
            for (int j = r + 1; j < getRCount(); j++) {
                cr.Race[j].Clear();
            }
        }
    }

}
