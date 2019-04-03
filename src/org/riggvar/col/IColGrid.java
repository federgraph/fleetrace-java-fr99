package org.riggvar.col;

public interface IColGrid {
    public String getCells(int ACol, int ARow);

    public void setCells(int ACol, int ARow, String value);

    public int getColCount();

    public void setColCount(int value);

    public int getRowCount();

    public void setRowCount(int value);

    public int getCol();

    public void setCol(int value);

    public int getRow();

    public void setRow(int value);

    public int getColWidth(int ACol);

    public void setColWidth(int ACol, int value);

    public boolean getHasFixedRow();

    public void setHasFixedRow(boolean value);

    public boolean getEnabled();

    public void setEnabled(boolean value);

    public boolean getIsEditorMode();

    public void setIsEditorMode(boolean value);

    public Object getDataSet();

    public void setDataSet(Object value);

    public void ClearRow(int ARow);

    public void CancelEdit();

    public void UpdateInplace(String value);

    public void SetupGrid(Object colGrid);

    public void ShowData();
}
