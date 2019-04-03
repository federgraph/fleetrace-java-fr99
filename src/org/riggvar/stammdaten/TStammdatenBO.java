package org.riggvar.stammdaten;

import org.riggvar.col.*;
import org.riggvar.base.*;
import org.riggvar.bo.TMain;

public class TStammdatenBO extends
        TBaseColBO<TStammdatenColGrid, TStammdatenBO, TStammdatenNode, TStammdatenRowCollection, TStammdatenRowCollectionItem, TStammdatenColProps, TStammdatenColProp> {

    public TStammdatenBO() {
        super();
    }

    public int getFieldCount() {
        return TMain.BO.AdapterParams.FieldCount;
    }

    @Override
    public void InitColsActiveLayout(TStammdatenColGrid g, int aLayout) {
        TStammdatenColProp cp;
        g.getColsActive().clear();
        g.AddColumn("col_BaseID");

        cp = g.AddColumn("col_SNR");
        cp.setOnFinishEdit(new TStammdatenDelegate(this, TStammdatenDelegate.commandEditSNR));
        cp.ReadOnly = false;

        int fc = getFieldCount();

        if (fc > 0) {
            cp = g.AddColumn("col_FN");
            cp.setOnFinishEdit(new TStammdatenDelegate(this, TStammdatenDelegate.commandEditFN));
            cp.ReadOnly = false;
        }
        if (fc > 1) {
            cp = g.AddColumn("col_LN");
            cp.setOnFinishEdit(new TStammdatenDelegate(this, TStammdatenDelegate.commandEditLN));
            cp.ReadOnly = false;
        }
        if (fc > 2) {
            cp = g.AddColumn("col_SN");
            cp.setOnFinishEdit(new TStammdatenDelegate(this, TStammdatenDelegate.commandEditSN));
            cp.ReadOnly = false;
        }
        if (fc > 3) {
            cp = g.AddColumn("col_NC");
            cp.setOnFinishEdit(new TStammdatenDelegate(this, TStammdatenDelegate.commandEditNOC));
            cp.ReadOnly = false;
        }
        if (fc > 4) {
            cp = g.AddColumn("col_GR");
            cp.setOnFinishEdit(new TStammdatenDelegate(this, TStammdatenDelegate.commandEditGender));
            cp.ReadOnly = false;
        }
        if (fc > 5) {
            cp = g.AddColumn("col_PB");
            cp.setOnFinishEdit(new TStammdatenDelegate(this, TStammdatenDelegate.commandEditPB));
            cp.ReadOnly = false;
        }
        if (fc > TStammdatenRowCollection.FixFieldCount) {

            for (int i = TStammdatenRowCollection.FixFieldCount + 1; i <= fc; i++) {
                cp = g.AddColumn("col_N" + i);
                cp.setOnFinishEdit2(new TStammdatenDelegate(this, TStammdatenDelegate.commandEditGender));
                cp.ReadOnly = false;
            }
        }
    }

    public String EditSNR(TStammdatenRowCollectionItem cr, String Value) {
        if (cr != null) {
            cr.SNR = Utils.StrToIntDef(Value, cr.SNR);
            Value = Utils.IntToStr(cr.SNR);
        }
        return Value;
    }

    public String editFN(TStammdatenRowCollectionItem cr, String Value) {
        if (cr != null) {
            cr.setFN(Value);
        }
        return Value;
    }

    public String editLN(TStammdatenRowCollectionItem cr, String Value) {
        if (cr != null) {
            cr.setLN(Value);
        }
        return Value;
    }

    public String editSN(TStammdatenRowCollectionItem cr, String Value) {
        if (cr != null) {
            cr.setSN(Value);
        }
        return Value;
    }

    public String editNC(TStammdatenRowCollectionItem cr, String Value) {
        if (cr != null) {
            cr.setNC(Value);
        }
        return Value;
    }

    public String editGR(TStammdatenRowCollectionItem cr, String Value) {
        if (cr != null) {
            cr.setGR(Value);
        }
        return Value;
    }

    public String editPB(TStammdatenRowCollectionItem cr, String Value) {
        if (cr != null) {
            cr.setPB(Value);
        }
        return Value;
    }

    public String editNameColumn(TStammdatenRowCollectionItem cr, String Value, String ColName) {
        if (cr != null) {
            int i = Utils.StrToIntDef(ColName.substring(5), -1);
            if (i > -1) {
                cr.setFieldValue(i, Value);
            }
        }
        return Value;
    }

}
