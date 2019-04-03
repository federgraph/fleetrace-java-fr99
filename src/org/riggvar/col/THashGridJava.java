package org.riggvar.col;

import java.util.*;

public class THashGridJava implements IColGrid {
    class TKeyPoint {
        int c, r;

        public TKeyPoint(int ACol, int ARow) {
            setLocation(ACol, ARow);
        }

        void setLocation(int ACol, int ARow) {
            c = ACol;
            r = ARow;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof TKeyPoint))
                return false;
            TKeyPoint that = (TKeyPoint) obj;
            if (this.c == that.c && this.r == that.r)
                return true;
            return false;
        }

        @Override
        public int hashCode() {
            return r;
        }

    }

    Hashtable<TKeyPoint, String> ht;
    private static int default_width = 35;
    private int fColCount;
    private int fRowCount;
    private int fCol;
    private int fRow;
    private boolean fHasFixedRow;
    private boolean fEnabled;
    private boolean fIsEditorMode;
    private Vector<Integer> fColWidth;
    private TKeyPoint p;

    public javax.swing.table.AbstractTableModel TableModel;

    public THashGridJava() {
        ht = new Hashtable<TKeyPoint, String>(1024);
        fColWidth = new Vector<Integer>();
        p = new TKeyPoint(0, 0);
    }

    public String getCells(int ACol, int ARow) {
        // Point p = new Point(ACol, ARow);
        p.setLocation(ACol, ARow);
        String cp = ht.get(p);
        if (cp == null)
            return "";
        else
            return cp;
    }

    public void setCells(int ACol, int ARow, String value) {
        TKeyPoint p1 = new TKeyPoint(ACol, ARow);
        p1.setLocation(ACol, ARow);
        ht.put(p1, value);
    }

    public int getColCount() {
        return fColCount;
    }

    public void setColCount(int value) {
        fColCount = value;
    }

    public int getRowCount() {
        return fRowCount;
    }

    public void setRowCount(int value) {
        fRowCount = value;
    }

    public int getCol() {
        return fCol;
    }

    public void setCol(int value) {
        fCol = value;
    }

    public int getRow() {
        return fRow;
    }

    public void setRow(int value) {
        fRow = value;
    }

    public int getColWidth(int ACol) {
        if (ACol >= fColWidth.size()) {
            return default_width;
        }
        Object o = fColWidth.elementAt(ACol);
        if (o instanceof Integer) {
            return ((Integer) o).intValue();
        } else {
            return default_width;
        }
    }

    public void setColWidth(int ACol, int value) {
        while (fColWidth.size() <= ACol) {
            fColWidth.addElement(default_width);
        }
        fColWidth.setElementAt(value, ACol);
    }

    public boolean getHasFixedRow() {
        return fHasFixedRow;
    }

    public void setHasFixedRow(boolean value) {
        fHasFixedRow = value;
    }

    public boolean getEnabled() {
        return fEnabled;
    }

    public void setEnabled(boolean value) {
        fEnabled = value;
    }

    public boolean getIsEditorMode() {
        return fIsEditorMode;
    }

    public void setIsEditorMode(boolean value) {
        fIsEditorMode = value;
    }

    public Object getDataSet() {
        return null;
    }

    public void setDataSet(Object value) {
    }

    public void ClearRow(int ARow) {
    }

    public void CancelEdit() {
        fIsEditorMode = false;
    }

    public void UpdateInplace(String Value) {
        setCells(fCol, fRow, Value);
        // if (TableModel != null) TableModel.fireTableCellUpdated(fRow, fCol);
    }

    public void SetupGrid(Object colGrid) {
        if (TableModel != null) {
            // TableModel.fireTableChanged();
            TableModel.fireTableStructureChanged();
        }
    }

    public void ShowData() {
        if (TableModel != null) {
            TableModel.fireTableDataChanged();
        }
    }
}
