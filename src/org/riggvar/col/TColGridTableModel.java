package org.riggvar.col;

import java.awt.*;
import javax.swing.table.*;

public abstract class TColGridTableModel<G extends TColGrid<G, B, N, C, I, PC, PI>, B extends TBaseColBO<G, B, N, C, I, PC, PI>, N extends TBaseNode<G, B, N, C, I, PC, PI>, C extends TBaseRowCollection<G, B, N, C, I, PC, PI>, I extends TBaseRowCollectionItem<G, B, N, C, I, PC, PI>, PC extends TBaseColProps<G, B, N, C, I, PC, PI>, PI extends TBaseColProp<G, B, N, C, I, PC, PI>>
        extends AbstractTableModel implements IColGridGetBaseNode<G, B, N, C, I, PC, PI> {
    private static final long serialVersionUID = 1L;

    public THashGridJava StringGrid;
    public G ColGrid;
    public B ColBO;
    public N Node;

    public TColGridTableModel() {
        StringGrid = new THashGridJava();
        ColGrid = newColGrid(StringGrid);
        StringGrid.TableModel = this;
    }

    protected abstract G newColGrid(IColGrid StringGrid);

    public N ColGridGetBaseNode() {
        return Node;
    }

    public int getColumnCount() {
        return StringGrid.getColCount();
    }

    @Override
    public String getColumnName(int ACol) {
        int i = ColGrid.getColsActive().getSortColIndex();
        if (ACol == i) {
            return StringGrid.getCells(ACol, 0) + " *";
        } else {
            return StringGrid.getCells(ACol, 0);
        }
    }

    public int getRowCount() {
        return StringGrid.getRowCount() - 1;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return !ColGrid.getColsActive().getBaseColProp(columnIndex).getReadOnly();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return StringGrid.getCells(columnIndex, rowIndex + 1);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        StringGrid.setRow(rowIndex + 1);
        StringGrid.setCol(columnIndex);
        ColGrid.FinishEdit(columnIndex, rowIndex + 1, aValue.toString());
    }

    public Color getForeground(int row, int column) {
        return Color.black;
    }

    public void setForeground(Color color, int row, int column) {
    }

    public Color getBackground(int row, int column) {
        try {
            TCellProp cellProp = ColGrid.InitCellProp(column, row + 1);
            if (cellProp != null) {
                return cellProp.Color;
            } else {
                return Color.orange;
            }
        } catch (Exception e) {
            return Color.cyan;
        }
    }

    public void setBackground(Color color, int row, int column) {
    }

}
