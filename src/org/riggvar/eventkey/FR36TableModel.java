package org.riggvar.eventkey;

import java.awt.*;
import javax.swing.table.*;

public class FR36TableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    public FR36TableModel() {
    }

    public int getColumnCount() {
        return KeyMatrix.getColCount();
    }

    @Override
    public String getColumnName(int ACol) {
        return "" + (ACol + 1);
    }

    public int getRowCount() {
        return KeyMatrix.getRowCount();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (KeyMatrix.Model == null) {
            return "";
        }
        String s = KeyMatrix.Model.getValue(columnIndex, rowIndex);
        if (s.equals("")) {
            return s;
        } else {
            return Integer.parseInt(s);
        }
    }

    public Color getForeground(int row, int column) {
        return Color.black;
    }

    public void setForeground(Color color, int row, int column) {
    }

    public Color getBackground(int row, int column) {
        try {
            return KeyMatrix.Model.getColor(column, row);
        } catch (Exception e) {
            return Color.cyan;
        }
    }

    public void setBackground(Color color, int row, int column) {
    }

}
