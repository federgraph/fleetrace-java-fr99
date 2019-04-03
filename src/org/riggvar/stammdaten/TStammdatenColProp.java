package org.riggvar.stammdaten;

import org.riggvar.col.*;
import org.riggvar.base.*;
import org.riggvar.bo.*;

public class TStammdatenColProp extends
        TBaseColProp<TStammdatenColGrid, TStammdatenBO, TStammdatenNode, TStammdatenRowCollection, TStammdatenRowCollectionItem, TStammdatenColProps, TStammdatenColProp> {
    public TStammdatenColProp() {
        super();
    }

    public int GetFieldCount() {
        int i = TStammdatenRowCollection.FixFieldCount;
        if (TMain.BO != null) {
            i = TMain.BO.AdapterParams.FieldCount;
            if (i < TStammdatenRowCollection.FixFieldCount) {
                i = TStammdatenRowCollection.FixFieldCount;
            }
        }
        return i;
    }

    public String GetFieldCaptionDef(TStammdatenRowCollection cl, int index, String def) {
        return (cl != null) ? cl.getFieldCaption(index) : def;
    }

    public String GetFieldCaptionDef(int index, String def) {
        TStammdatenNode n = this.GetStammdatenNode();
        return (n != null) ? n.getBaseRowCollection().getFieldCaption(index) : def;
    }

    public TStammdatenRowCollection GetStammdatenRowCollection() {
        TStammdatenNode n = this.GetStammdatenNode();
        return (n != null) ? n.getBaseRowCollection() : null;
    }

    public TStammdatenNode GetStammdatenNode() {
        return (TMain.BO != null) ? TMain.BO.StammdatenNode : null;
    }

    @Override
    public void InitColsAvail() {
        TStammdatenColProps ColsAvail = Collection;
        TStammdatenColProp cp;
        TStammdatenRowCollection cl = this.GetStammdatenRowCollection();

        // SNR
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_SNR");
        cp.Caption = "SNR";
        cp.Width = 40;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taRightJustify;
        cp.NumID = TStammdatenColProp.NumID_SNR;

        // FN
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_FN");
        cp.Caption = this.GetFieldCaptionDef(cl, 1, FieldNames.FN);
        cp.Width = 80;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.ColType = TColType.colTypeString;
        cp.NumID = TStammdatenColProp.NumID_FN;

        // LN
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_LN");
        cp.Caption = cp.Caption = this.GetFieldCaptionDef(cl, 2, FieldNames.LN);
        cp.Width = 80;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.ColType = TColType.colTypeString;
        cp.NumID = TStammdatenColProp.NumID_LN;

        // SN
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_SN");
        cp.Caption = cp.Caption = this.GetFieldCaptionDef(cl, 3, FieldNames.SN);
        cp.Width = 80;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.ColType = TColType.colTypeString;
        cp.NumID = TStammdatenColProp.NumID_SN;

        // NC
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_NC");
        cp.Caption = cp.Caption = this.GetFieldCaptionDef(cl, 4, FieldNames.NC);
        cp.Width = 40;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.ColType = TColType.colTypeString;
        cp.NumID = TStammdatenColProp.NumID_NC;

        // GR
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_GR");
        cp.Caption = cp.Caption = this.GetFieldCaptionDef(cl, 5, FieldNames.GR);
        cp.Width = 55;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.ColType = TColType.colTypeString;
        cp.NumID = TStammdatenColProp.NumID_GR;

        // PB
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_PB");
        cp.Caption = cp.Caption = this.GetFieldCaptionDef(cl, 6, FieldNames.PB);
        cp.Width = 35;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.ColType = TColType.colTypeString;
        cp.NumID = TStammdatenColProp.NumID_PB;

        int fieldCount = GetFieldCount();
        if (fieldCount > TStammdatenRowCollection.FixFieldCount) {
            for (int i = TStammdatenRowCollection.FixFieldCount + 1; i <= fieldCount; i++) {
                cp = ColsAvail.AddBaseColProp();
                cp.setNameID("col_N" + i);
                cp.Caption = this.GetFieldCaptionDef(i, "N" + i);
                cp.Width = 60;
                cp.Sortable = true;
                cp.Alignment = TColAlignment.taLeftJustify;
                cp.ColType = TColType.colTypeString;
                cp.NumID = i;
            }
        }

    }

    @Override
    protected String GetTextDefault(TStammdatenRowCollectionItem cr, String Value) {
        Value = "";

        Value = super.GetTextDefault(cr, Value);

        if (NumID == TStammdatenColProp.NumID_SNR) {
            Value = Utils.IntToStr(cr.SNR);
        } else if (NumID == TStammdatenColProp.NumID_FN) {
            Value = cr.getFN();
        } else if (NumID == TStammdatenColProp.NumID_LN) {
            Value = cr.getLN();
        } else if (NumID == TStammdatenColProp.NumID_SN) {
            Value = cr.getSN();
        } else if (NumID == TStammdatenColProp.NumID_NC) {
            Value = cr.getNC();
        } else if (NumID == TStammdatenColProp.NumID_GR) {
            Value = cr.getGR();
        } else if (NumID == TStammdatenColProp.NumID_PB) {
            Value = cr.getPB();
        } else if (NumID > TStammdatenRowCollection.FixFieldCount) {
            Value = cr.getFieldValue(NumID);
        }
        return Value;
    }

    protected static final int NumID_SNR = -1;
    protected static final int NumID_FN = 1;
    protected static final int NumID_LN = 2;
    protected static final int NumID_SN = 3;
    protected static final int NumID_NC = 4;
    protected static final int NumID_GR = 5;
    protected static final int NumID_PB = 6;
}
