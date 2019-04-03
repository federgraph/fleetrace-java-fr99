package org.riggvar.col;

import org.riggvar.base.*;

public class TBaseColProp<G extends TColGrid<G, B, N, C, I, PC, PI>, B extends TBaseColBO<G, B, N, C, I, PC, PI>, N extends TBaseNode<G, B, N, C, I, PC, PI>, C extends TBaseRowCollection<G, B, N, C, I, PC, PI>, I extends TBaseRowCollectionItem<G, B, N, C, I, PC, PI>, PC extends TBaseColProps<G, B, N, C, I, PC, PI>, PI extends TBaseColProp<G, B, N, C, I, PC, PI>> {
    private String fNameID;
    public int NumID; // for better performance of method GetTextDefault
    public String Caption;
    public int Width;
    public int Alignment;
    public int ColType;
    public boolean Visible;
    public boolean Sortable;
    public boolean ReadOnly;

    IColGridGetText<G, B, N, C, I, PC, PI> fOnGetSortKey;
    IColGridGetText<G, B, N, C, I, PC, PI> fOnGetSortKey2;
    IColGridGetText<G, B, N, C, I, PC, PI> fOnGetText;
    IColGridGetText<G, B, N, C, I, PC, PI> fOnFinishEdit;
    IColGridGetText<G, B, N, C, I, PC, PI> fOnFinishEdit2;

    private static int sId = -1;
    private int fId;
    public PC Collection;

    public int getId() {
        return fId;
    }

    public int getIndex() {
        return Collection.indexOf(this);
    }

    public TBaseColProp() {
        sId++;
        fId = sId;
        setNameID("");
        Visible = true;
        Alignment = TColAlignment.taRightJustify;
        Width = 35;
        ReadOnly = true;
    }

    public int getWidth() {
        return Width;
    }

    public void setWidth(int value) {
        Width = value;
    }

    public boolean getVisible() {
        return Visible;
    }

    public void setVisible(boolean value) {
        Visible = value;
    }

    public boolean getSortable() {
        return Sortable;
    }

    public void setSortable(boolean value) {
        Sortable = value;
    }

    public String getNameID() {
        return fNameID;
    }

    public void setNameID(String value) {
        PC o = Collection;
        if ((value.equals("")) || ((!value.equals(fNameID)) && o.IsDuplicateNameID(value))) {
            fNameID = "col_" + getId();
            if ((Caption == null || Caption.equals("")) || (Caption.equals(fNameID))) {
                Caption = value;
            }
        } else {
            if (Caption == null || Caption.equals(fNameID)) {
                Caption = value;
            }
            fNameID = value;
        }
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String value) {
        Caption = value;
    }

    public int getAlignment() {
        return Alignment;
    }

    public void setAlignment(int value) {
        Alignment = value;
    }

    public boolean getReadOnly() {
        return ReadOnly;
    }

    public void setReadOnly(boolean value) {
        ReadOnly = value;
    }

    public int getColType() {
        return ColType;
    }

    public void setColType(int value) {
        ColType = value;
    }

    public IColGridGetText<G, B, N, C, I, PC, PI> getOnGetSortKey() {
        return fOnGetSortKey;
    }

    public void setOnGetSortKey(IColGridGetText<G, B, N, C, I, PC, PI> value) {
        fOnGetSortKey = value;
    }

    public void setOnGetSortKey2(IColGridGetText<G, B, N, C, I, PC, PI> value) {
        fOnGetSortKey2 = value;
    }

    public IColGridGetText<G, B, N, C, I, PC, PI> getOnGetSortKey2() {
        return fOnGetSortKey2;
    }

    public void removeOnGetSortKey2(IColGridGetText<G, B, N, C, I, PC, PI> value) {
        fOnGetSortKey2 = value;
    }

    public IColGridGetText<G, B, N, C, I, PC, PI> getOnGetText() {
        return fOnGetText;
    }

    public void setOnGetText(IColGridGetText<G, B, N, C, I, PC, PI> value) {
        fOnGetText = value;
    }

    public IColGridGetText<G, B, N, C, I, PC, PI> getOnFinishEdit() {
        return fOnFinishEdit;
    }

    public void setOnFinishEdit(IColGridGetText<G, B, N, C, I, PC, PI> value) {
        fOnFinishEdit = value;
    }

    public IColGridGetText<G, B, N, C, I, PC, PI> getOnFinishEdit2() {
        return fOnFinishEdit2;
    }

    public void setOnFinishEdit2(IColGridGetText<G, B, N, C, I, PC, PI> value) {
        fOnFinishEdit2 = value;
    }

    public void InitColsAvail() {
        // virtual
    }

    protected String GetTextDefault(I cr, String Value) {
        if (cr == null) {
            return Value;
        }
        if ((NumID == 0)) // (getNameID().equals("col_BaseID"))
        {
            Value = "" + cr.BaseID;
        }
        return Value;
    }

    public String GetText(I cr) {
        if (fOnGetText != null) {
            return fOnGetText.GetText(cr, "");
        } else {
            return GetTextDefault(cr, "");
        }
    }

    public String GetSortKey(I cr, String SortKey) {
        SortKey = GetText(cr);

        // move 0 and blanks towards end in some columns
        if ((ColType == TColType.colTypeRank) && ((SortKey.equals("0")) || (SortKey.equals("")))) {
            SortKey = "" + (999 + cr.BaseID);
        } else if ((ColType == TColType.colTypeString) && (SortKey.equals(""))) {
            SortKey = Utils.PadLeft("ZZZ" + cr.BaseID, 3, '0');

        }
        if (fOnGetSortKey != null) {
            SortKey = fOnGetSortKey.GetText(cr, SortKey);
        } else if (fOnGetSortKey2 != null) {
            SortKey = fOnGetSortKey2.GetText2(cr, SortKey, fNameID);
        }
        return SortKey;
    }

    public void Assign(PI source) {
        if (source != null) {
            PI cp = source;
            setNameID(cp.getNameID());
            NumID = cp.NumID;
            Caption = cp.Caption;
            Width = cp.Width;
            Alignment = cp.Alignment;
            Visible = cp.Visible;
            Sortable = cp.Sortable;
            ColType = cp.ColType;
            ReadOnly = cp.ReadOnly;

            fOnGetSortKey = cp.fOnGetSortKey;
            fOnGetSortKey2 = cp.fOnGetSortKey2;
            fOnGetText = cp.fOnGetText;
            fOnFinishEdit = cp.fOnFinishEdit;
            fOnFinishEdit2 = cp.fOnFinishEdit2;
        }
    }
}
