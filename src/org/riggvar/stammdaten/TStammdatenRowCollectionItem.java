package org.riggvar.stammdaten;

import org.riggvar.base.*;
import org.riggvar.col.*;

public class TStammdatenRowCollectionItem extends
        TBaseRowCollectionItem<TStammdatenColGrid, TStammdatenBO, TStammdatenNode, TStammdatenRowCollection, TStammdatenRowCollectionItem, TStammdatenColProps, TStammdatenColProp> {
    public int SNR;

    protected String fFN = "";
    protected String fLN = "";
    protected String fSN = "";
    protected String fNC = "";
    protected String fGR = "";
    protected String fPB = "";

    public String DN = "";
    public TProps Props = new TProps();

    public TStammdatenRowCollectionItem() {
        super();
    }

    public String getFN() {
        return fFN;
    }

    public void setFN(String value) {
        fFN = value;
        calcDisplayName();
    }

    public String getLN() {
        return fLN;
    }

    public void setLN(String value) {
        fLN = value;
        calcDisplayName();
    }

    public String getSN() {
        return fSN;
    }

    public void setSN(String value) {
        fSN = value;
        calcDisplayName();
    }

    public String getNC() {
        return fNC;
    }

    public void setNC(String value) {
        fNC = value;
        calcDisplayName();
    }

    public String getGR() {
        return fGR;
    }

    public void setGR(String value) {
        fGR = value;
        calcDisplayName();
    }

    public String getPB() {
        return fPB;
    }

    public void setPB(String value) {
        fPB = value;
        calcDisplayName();
    }

    // @Override
    public void Assign(TStammdatenRowCollectionItem source) {
        if (source != null) {
            TStammdatenRowCollectionItem e = source;
            SNR = e.SNR;
            fFN = e.fFN;
            fLN = e.fLN;
            fSN = e.fSN;
            fNC = e.fNC;
            fGR = e.fGR;
            fPB = e.fPB;
            //
            Props.Assign(e.Props);
        }
    }

    public void Assign(TStammdatenEntry source) {
        if (source != null) {
            TStammdatenEntry e = source;
            SNR = e.SNR;
            fFN = e.FN;
            fLN = e.LN;
            fSN = e.SN;
            fNC = e.NC;
            fGR = e.GR;
            fPB = e.PB;
        }
    }

    public void calcDisplayName() {
        Collection.CalcDisplayName(this);
    }

    public String getFieldValue(int fieldIndex) {
        switch (fieldIndex) {
        case 1:
            return fFN;
        case 2:
            return fLN;
        case 3:
            return fSN;
        case 4:
            return fNC;
        case 5:
            return fGR;
        case 6:
            return fPB;
        default: {
            if (fieldIndex > 0 && fieldIndex <= getFieldCount()) {
                return Props.getValue("N" + fieldIndex);
            }
            break;
        }
        }
        return "";
    }

    public void setFieldValue(int fieldIndex, String value) {
        switch (fieldIndex) {
        case 1:
            fFN = value;
            break;
        case 2:
            fLN = value;
            break;
        case 3:
            fSN = value;
            break;
        case 4:
            fNC = value;
            break;
        case 5:
            fGR = value;
            break;
        case 6:
            fPB = value;
            break;
        default: {
            if (fieldIndex > 0 && fieldIndex <= getFieldCount()) {
                Props.setValue("N" + fieldIndex, value);
            }
            break;
        }
        }
        calcDisplayName();
    }

    public boolean isFieldUsed(int fieldIndex) {
        TStammdatenRowCollection cl = Collection;
        TStammdatenRowCollectionItem cr;
        for (int i = 0; i < cl.size(); i++) {
            cr = cl.get(i);
            if (cr.getFieldValue(fieldIndex) != "")
                return true;

        }
        return false;
    }

    public int getFieldCount() {
        TStammdatenRowCollection cl = Collection;
        return cl.getFieldCount();
    }

}
