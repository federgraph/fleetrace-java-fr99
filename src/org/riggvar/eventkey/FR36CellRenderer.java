package org.riggvar.eventkey;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import org.riggvar.eventkey.FR36TableModel;

public class FR36CellRenderer extends JLabel implements TableCellRenderer {
    private static final long serialVersionUID = 1L;

    protected static Border noFocusBorder;
    public static Color clFocus = Color.green.brighter();

    public FR36CellRenderer() {
        noFocusBorder = new EmptyBorder(1, 2, 1, 2);
        setOpaque(true);
        setBorder(noFocusBorder);
        setHorizontalAlignment(RIGHT);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Color foreground = null;
        Color background = null;
        Font font = null;

        TableModel model = table.getModel();

        if (model instanceof FR36TableModel) {
            FR36TableModel tm = (FR36TableModel) model;
            foreground = tm.getForeground(row, column);
            background = tm.getBackground(row, column);
        }
        if (isSelected) {
            setForeground((foreground != null) ? foreground : table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground((foreground != null) ? foreground : table.getForeground());
            setBackground((background != null) ? background : table.getBackground());
        }
        setFont((font != null) ? font : table.getFont());

        if (hasFocus) {
            setBorder(noFocusBorder);
            setBackground(clFocus);
        } else {
            setBorder(noFocusBorder);
        }

        setValue(value);
        return this;
    }

    protected void setValue(Object value) {
        setText((value == null) ? "" : value.toString());
    }
}
