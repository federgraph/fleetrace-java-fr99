package org.riggvar.bo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.table.*;

import java.beans.*;

import org.riggvar.base.*;
import org.riggvar.bo.TBO;
import org.riggvar.bo.TMain;
import org.riggvar.col.*;

public class TColPanel<G extends TColGrid<G, B, N, C, I, PC, PI>, B extends TBaseColBO<G, B, N, C, I, PC, PI>, N extends TBaseNode<G, B, N, C, I, PC, PI>, C extends TBaseRowCollection<G, B, N, C, I, PC, PI>, I extends TBaseRowCollectionItem<G, B, N, C, I, PC, PI>, PC extends TBaseColProps<G, B, N, C, I, PC, PI>, PI extends TBaseColProp<G, B, N, C, I, PC, PI>>
        extends TColPanelBase implements CellEditorListener, PropertyChangeListener {
    private static final long serialVersionUID = 1L;
    protected static Border noFocusBorder;

    protected TBO BO; // shortcut to TMain.Controller.BOContainer.BO;
    public G ColGrid; // shortcut to fTableModel.ColGrid

    public TColGridTableModel<G, B, N, C, I, PC, PI> fTableModel;
    public JTable jTable;
    private JTextField tf; // Editor, selects content when VK_F2 key up

    public boolean Clearing;

    public TColPanel() {
        BO = TMain.BO;
    }

    public void editingStopped(ChangeEvent e) {
        UpdateView();
    }

    public void editingCanceled(ChangeEvent e) {
        // System.out.println("editing canceled"); //never called
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

    public void InitTable() throws Exception {
        jTable = new TColJTable();
        jTable.setGridColor(Color.gray);

        // listener for current row
        ListSelectionModel rowSM = jTable.getSelectionModel();
        rowSM.addListSelectionListener(new MyListSelectionListener());

        // listener for current column
        TableColumnModel colModel = jTable.getColumnModel();
        colModel.addColumnModelListener(new MyTableColumnModelListener());

        // listener for VK_F4 (MarkRow)
        jTable.getInputMap().put(KeyStroke.getKeyStroke("F4"), "marked");
        jTable.getActionMap().put("marked", new MarkRowAction());

        jTable.setCellSelectionEnabled(true);

        // get Editor instance
        TableCellEditor tce = jTable.getDefaultEditor(String.class);
        // listen to 'editingStopped' (--> trigger update)
        tce.addCellEditorListener(this);

        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable.setModel(fTableModel);

        // header: listener for updating SortMarker
        JTableHeader header = jTable.getTableHeader();
        header.addMouseListener(new ColumnListener(jTable));
        header.setReorderingAllowed(false);

        /*
         * when user starts editing make sure old cell content is properly
         * selected/deleted. now get and cache instance of editor and attach listener
         */
        Object o = tce.getTableCellEditorComponent(jTable, "", false, 0, 0);
        if (o != null) {
            if (o instanceof JTextField) {
                tf = (JTextField) o;
                tf.addPropertyChangeListener(this);
            }
        }

    }

    class ColumnListener extends MouseAdapter {
        protected JTable m_table;

        public ColumnListener(JTable table) {
            m_table = table;
        }

        public void mouseClicked_(MouseEvent e) {
            fTableModel.ColGrid.MarkRowCollectionItem();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            TableColumnModel colModel = m_table.getColumnModel();
            int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
            if (columnModelIndex == -1)
                return;
            int modelIndex = colModel.getColumn(columnModelIndex).getModelIndex();

            if (modelIndex < 0) {
                return;
            }

            try {
                fTableModel.ColGrid.MouseDown(null, modelIndex, 0);
                for (int i = 0; i < fTableModel.StringGrid.getColCount(); i++) {
                    javax.swing.table.TableColumn column = colModel.getColumn(i);
                    column.setHeaderValue(fTableModel.getColumnName(i));
                }
                m_table.getTableHeader().repaint();
            } catch (Exception ex) {
                return;
            }
        }
    }

    class TColJTable extends JTable {
        private static final long serialVersionUID = 1L;

        TableCellRenderer LeftAlignCellRenderer;
        TableCellRenderer RightAlignCellRenderer;

        public TColJTable() {
            LeftAlignCellRenderer = new TColJTableCellRenderer(0);
            RightAlignCellRenderer = new TColJTableCellRenderer(1);
        }

        @Override
        public void createDefaultColumnsFromModel() {
//			if (fTableModel == null || fTableModel.StringGrid == null
//					|| fTableModel.ColGrid == null || fTableModel != getModel())
//				return;

            // remove any current columns
            TableColumnModel cm = getColumnModel();
            while (cm.getColumnCount() > 0) {
                cm.removeColumn(cm.getColumn(0));
            }

            // create new columns from the data model info
            for (int i = 0; i < fTableModel.getColumnCount(); i++) {
                TableColumn newColumn = new TableColumn(i);
                addColumn(newColumn);
                newColumn.setPreferredWidth(fTableModel.StringGrid.getColWidth(i));
                PI cp = fTableModel.ColGrid.getColsActive().getBaseColProp(i);
                if (cp != null) {
                    if (cp.getAlignment() == TColAlignment.taRightJustify)
                        newColumn.setCellRenderer(RightAlignCellRenderer);
                    else
                        newColumn.setCellRenderer(LeftAlignCellRenderer);
                }
            }
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
                Font font = null;
                Color foreground = fTableModel.getForeground(row, column);
                Color background = fTableModel.getBackground(row, column);

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

    class MyListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            // ignore extra messages
            if (e.getValueIsAdjusting()) {
                return;
            }

            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            if (lsm.isSelectionEmpty()) {
                // no rows are selected
            } else {
                int selectedRow = lsm.getMinSelectionIndex();
                fTableModel.StringGrid.setRow(selectedRow + 1);
            }
        }
    }

    class MyTableColumnModelListener implements TableColumnModelListener {
        public void columnAdded(TableColumnModelEvent e) {
        }

        public void columnRemoved(TableColumnModelEvent e) {
        }

        public void columnMoved(TableColumnModelEvent e) {
        }

        public void columnMarginChanged(ChangeEvent e) {
        }

        public void columnSelectionChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }

            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            if (lsm.isSelectionEmpty()) {
                // no rows are selected
            } else {
                int selectedCol = lsm.getMinSelectionIndex();
                fTableModel.StringGrid.setCol(selectedCol);
            }
        }
    }

    class MarkRowAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        public MarkRowAction() {
        }

        public void actionPerformed(ActionEvent e) {
            fTableModel.ColGrid.MarkRow();
        }
    }

}
