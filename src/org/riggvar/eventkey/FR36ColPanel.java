package org.riggvar.eventkey;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.table.*;

import java.beans.*;

import org.riggvar.base.*;
import org.riggvar.bo.TMain;
import org.riggvar.col.*;

public class FR36ColPanel extends TColPanelBase implements CellEditorListener, PropertyChangeListener {
    private static final long serialVersionUID = 1L;
    protected static Border noFocusBorder;

    public TableModel fTableModel;
    private JTextField tf; // Editor, selects content when VK_F2 key up

    public boolean Clearing;

    public FR36ColPanel() {
    }

    public void editingStopped(ChangeEvent e) {
        UpdateView();
    }

    public void editingCanceled(ChangeEvent e) {
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (e.getSource() == tf) {
            /*
             * select cell content if VK_F2 is pressed; select and delete old content if
             * user starts editing. (First character input overwrites selection.)
             */
            String s = e.getPropertyName();
            if (s.equals("ancestor") && e.getNewValue() != null) {
                if (tf.isVisible()) {
                    tf.selectAll();
                }
            }
        }
    }

    @Override
    public void UpdateView() {
        // virtual
    }

    protected void CancelScheduledUpdate(int drawTarget) {
        DrawNotifierEventArgs e = new DrawNotifierEventArgs(drawTarget, DrawNotifierEventArgs.OperationCancel);
        TMain.DrawNotifier.ScheduleFullUpdate(this, e);
    }

    class TColJTable extends JTable {
        private static final long serialVersionUID = 1L;

        TableCellRenderer LeftAlignCellRenderer;
        TableCellRenderer RightAlignCellRenderer;

        public TColJTable() {
            LeftAlignCellRenderer = new TColJTableCellRenderer(0);
            RightAlignCellRenderer = new TColJTableCellRenderer(1);
        }

        class TColJTableCellRenderer extends JLabel implements TableCellRenderer {
            private static final long serialVersionUID = 1L;

            public TColJTableCellRenderer(int Alignment) {
                super();
                noFocusBorder = new EmptyBorder(1, 2, 1, 2);
                setOpaque(true);
                setBorder(noFocusBorder);
                if (Alignment == TColAlignment.taRightJustify)
                    this.setHorizontalAlignment(RIGHT);
            }

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Color foreground = null;
                Color background = null;
                Font font = null;

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
                    setBackground(Color.yellow);
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

    }

}
