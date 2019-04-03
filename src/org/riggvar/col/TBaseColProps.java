package org.riggvar.col;

import java.util.ArrayList;

public abstract class TBaseColProps<G extends TColGrid<G, B, N, C, I, PC, PI>, B extends TBaseColBO<G, B, N, C, I, PC, PI>, N extends TBaseNode<G, B, N, C, I, PC, PI>, C extends TBaseRowCollection<G, B, N, C, I, PC, PI>, I extends TBaseRowCollectionItem<G, B, N, C, I, PC, PI>, PC extends TBaseColProps<G, B, N, C, I, PC, PI>, PI extends TBaseColProp<G, B, N, C, I, PC, PI>>
        extends ArrayList<PI> {
    private static final long serialVersionUID = 1L;
    public static TColCaptionBag ColCaptionBag = new TColCaptionBag();
    private int fSortColIndex;
    public boolean UseCustomColCaptions;
    public TColGrid<G, B, N, C, I, PC, PI> ColGrid;

    public TBaseColProps() {
        super();
    }

    protected abstract PI NewItem();

    public PI AddRow() {
        PI cr = NewItem();
        add(cr);
        return cr;
    }

    public PI InsertRow(int index) {
        PI cr = NewItem();
        super.add(index, cr);
        return cr;
    }

    public void DeleteRow(int index) {
        super.remove(index);
    }

    public int IndexOfRow(PI row) {
        return super.indexOf(row);
    }

    public PI getItem(int index) {
        if (index < 0 || index >= size())
            return null;
        else
            return super.get(index);
    }

    public void setItem(int index, PI value) {
        if (index >= 0 && index < size()) {
            super.set(index, value);
        }
    }

    public int getSortColIndex() {
        if (fSortColIndex >= 0 && fSortColIndex < size() && get(fSortColIndex) != null
                && get(fSortColIndex).getSortable()) {
            return fSortColIndex;
        } else {
            fSortColIndex = -1;
            return fSortColIndex;
        }
    }

    public void setSortColIndex(int value) {
        if (value >= 0 && value < size() && get(value).getSortable()) {
            fSortColIndex = value;
        } else {
            fSortColIndex = -1;
        }
    }

    public void Assign(PC source) {
        if (source != null) {
            clear();
            for (int i = 0; i < source.size(); i++) {
                AddRow().Assign(source.get(i));
            }
        }
    }

    public void Init() {
        clear();

        // add column BaseID, the primary key column
        PI cp = this.AddBaseColProp();
        if (cp != null) {
            cp.setNameID("col_BaseID");
            cp.setCaption("ID");
            cp.setWidth(40);
            cp.setSortable(true);
            cp.InitColsAvail(); // virtual
            cp.NumID = 0; // default

            // if Owner is not nil, memory for objects is managed
            // Owner is nil for ColsActive, not nil for ColsAvail
            if (ColGrid != null && this.UseCustomColCaptions)
                InitCustomCaptions();
        } else {
            System.out.println("construction of TBaseColProp for BaseID failed.");
        }
    }

    public boolean IsDuplicateNameID(String s) {
        for (int i = 0; i < size(); i++) {
            PI o = getBaseColProp(i);
            if (o.getNameID().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public int getVisibleCount() {
        int result = 0;
        for (int i = 0; i < size(); i++) {
            if (get(i).getVisible()) {
                result++;
            }
        }
        return result;
    }

    public PI AddBaseColProp() {
        return AddRow();
    }

    public void UpdateRow(IColGrid StringGrid, int ARow, I cr) {
        int i = 0;
        for (PI cp : this) {
            StringGrid.setCells(i, ARow, cp.GetText(cr));
            i++;
        }
    }

    public PI ByName(String NameIndex) {
        for (PI cp : this) {
            if (cp != null && cp.getNameID().equals(NameIndex)) {
                return cp;
            }
        }
        return null;
    }

    public PI getBaseColProp(int index) {
        if ((index >= 0) && (index < size())) {
            return get(index);
        } else {
            return null;
        }
    }

    public void setBaseColProp(int index, PI value) {
        set(index, value);
    }

    public String getGridName() {
        if (ColGrid != null)
            return ColGrid.getName();
        else
            return "";
    }

    public void InitCustomCaptions() {
        PI cp;
        for (int i = 0; i < size(); i++) {
            cp = get(i);
            cp.Caption = getCaptionOverride(cp);
        }
    }

    public String getCaptionOverride(PI cp) {
        String key;
        String result = "";

        // first try, Grid specific search
        String gn = getGridName();
        if (!gn.equals("")) {
            key = gn + "_" + cp.getNameID();
            result = ColCaptionBag.getCaption(key);
        }

        // second try, Table specific search
        if (result.equals("")) {
            if (ColGrid != null) {
                if (ColGrid.GetBaseNode() != null) {
                    if (!ColGrid.GetBaseNode().NameID.equals("")) {
                        key = ColGrid.GetBaseNode().NameID + "_" + cp.getNameID();
                        result = ColCaptionBag.getCaption(key);
                    }
                }
            }
        }

        // third try, cross table, column name based
        if (result.equals("")) {
            result = ColCaptionBag.getCaption(cp.getNameID());
        }

        // else use default
        if (result.equals(""))
            result = cp.Caption;

        return result;
    }

}
