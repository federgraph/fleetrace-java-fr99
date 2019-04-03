package org.riggvar.eventkey;

import java.awt.*;
import java.awt.event.*;
import java.beans.Beans;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.riggvar.base.IConnection;
import org.riggvar.bo.TMain;
import org.riggvar.eventkey.KeyMatrix;
import org.riggvar.eventkey.FR36ColPanel;
import org.riggvar.eventkey.FR36TableModel;

public class TKeyPanel extends FR36ColPanel {
    private static final long serialVersionUID = 1L;
    private Random random = new Random();

    private boolean isFirstTimeInit = true;
    private FR36TableModel fTableModel;
    private IConnection client;

    protected int BibIndex = 0;
    protected String BibValue = "0";
    protected int selectedRow;
    protected int selectedCol;
    private int selectedBib;

    protected int msgCounterOut;
    private boolean autoSend = false;

    JPanel northPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel contentPanel = new JPanel();

    BorderLayout borderLayoutCenter = new BorderLayout();
    BorderLayout borderLayoutContent = new BorderLayout();
    BorderLayout borderLayout = new BorderLayout();

    FlowLayout flowLayoutTool = new FlowLayout();

    GridLayout gridLayoutText = new GridLayout();
    GridLayout gridLayoutNorth = new GridLayout();

    JPanel toolbar = new JPanel();
    JButton AgeBtn = new JButton();
    JButton RandomBtn = new JButton();
    JButton SendBtn = new JButton();
    JButton AutoSendBtn = new JButton();

    JPanel textboxPanel = new JPanel();
    JTextField Log = new JTextField();
    JTextField Memo = new JTextField();

    JScrollPane scrollPane = new JScrollPane();
    JTable Grid = new JTable();

    public TKeyPanel() {
        super();
        InitPanel();
    }

    @Override
    public void InitPanel() {
        try {
            if (!Beans.isDesignTime()) {
                new KeyMatrix();
                client = TMain.BO.InputServer.Server.Connect();
            }

            if (isFirstTimeInit) {
                isFirstTimeInit = false;
                jbInit();
            }

            if (!Beans.isDesignTime())
                InitForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void DisposePanel() {
        client = null;
        this.Memo.setText("");
        this.Log.setText("");
        KeyMatrix.Model.ResetAge();
    }

    protected void InitForm() {
        scrollPane.getViewport().add(Grid, null);
    }

    protected void jbInit() throws Exception {
        AgeBtn.setActionCommand("Age");
        AgeBtn.setText("Age");
        AgeBtn.addActionListener(new AgeBtn_actionAdapter(this));

        RandomBtn.setActionCommand("Random");
        RandomBtn.setText("Random");
        RandomBtn.addActionListener(new RandomBtn_actionAdapter(this));

        SendBtn.setText("Send");
        SendBtn.addActionListener(new SendBtn_actionAdapter(this));

        AutoSendBtn.setText("AutoSend");
        AutoSendBtn.addActionListener(new AutoSendBtn_actionAdapter(this));

        Log.setBackground(new Color(152, 194, 255));
        Log.setFont(new java.awt.Font("Dialog", 0, 11));
        Log.setText("Log");

        Memo.setBackground(Color.white);
        Memo.setFont(new java.awt.Font("Dialog", 0, 11));
        Memo.setText("Memo");

        flowLayoutTool.setAlignment(FlowLayout.LEFT);
        toolbar.setLayout(flowLayoutTool);

        gridLayoutNorth.setColumns(1);
        gridLayoutNorth.setRows(1);
        gridLayoutNorth.setVgap(0);
        northPanel.setLayout(gridLayoutNorth);
        northPanel.setBackground(Color.lightGray);
        northPanel.add(toolbar, null);

        gridLayoutText.setRows(2);
        textboxPanel.setLayout(gridLayoutText);
        textboxPanel.setMaximumSize(new Dimension(32767, 32767));
        textboxPanel.add(Log, null);
        textboxPanel.add(Memo, null);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setRowHeader(null);
        scrollPane.setColumnHeader(null);
        scrollPane.setToolTipText("");

        contentPanel.setLayout(borderLayoutContent);
        contentPanel.setBackground(Color.orange);
        contentPanel.add(textboxPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.setLayout(borderLayoutCenter);
        centerPanel.add(contentPanel, BorderLayout.CENTER);

        setLayout(borderLayout);
        add(centerPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        initGrid();

        setPreferredSize(new Dimension(1024, 220));
        setAutoSend(autoSend);
    }

    private void initGrid() {
        Grid.setGridColor(Color.gray);
        Grid.setAutoscrolls(true);
        Grid.setToolTipText("");
        Grid.setVerifyInputWhenFocusTarget(false);
        Grid.setRowMargin(1);
        Grid.setRowSelectionAllowed(false);
        Grid.setSelectionForeground(Color.white);
        Grid.addMouseListener(new TTimingPanel_Grid_mouseAdapter(this));
        Grid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Grid.getTableHeader().setReorderingAllowed(false);
    }

    protected void SetupGrid() throws Exception {
        fTableModel = new FR36TableModel();

        ListSelectionModel rowSM = Grid.getSelectionModel();
        rowSM.addListSelectionListener(new TTimingPanelListSelectionListener(this));

        TableColumnModel colModel = Grid.getColumnModel();
        colModel.addColumnModelListener(new TTimingPanelTableColumnModelListener(this));

        Grid.addKeyListener(new TTimingPanelKeyListener(this));
        Grid.setDefaultRenderer(Object.class, new FR36CellRenderer());
        Grid.setModel(fTableModel);

        for (int i = 0; i < colModel.getColumnCount(); i++) {
            TableColumn tc = colModel.getColumn(i);
            tc.setPreferredWidth(40);
        }
    }

    private boolean getAutoSend() {
        return autoSend;
    }

    private void setAutoSend(boolean value) {
        autoSend = value;
        if (value)
            AutoSendBtn.setText("On");
        else
            AutoSendBtn.setText("Off");
    }

    protected void ShowColor() {
        if (this.fTableModel != null) {
            this.fTableModel.fireTableDataChanged();
        }
    }

    protected void UpdateMemo() {
        setCurrent(this.selectedCol, this.selectedRow);
        this.HandleTransponderEvent(BibIndex, BibValue);
    }

    protected void HandleTransponderEvent(int bibIndex, String bibValue) {
        // virtual
        PaintCell(bibIndex);
    }

    protected void PaintCell(int idx) {
        if (idx < 0 || idx > 255)
            return;
        int col = idx % KeyMatrix.getColCount();
        int row = idx / KeyMatrix.getColCount();
        KeyMatrix.Model.TimerTick();
        KeyMatrix.Model.setAge(col, row);
    }

    protected void Send(String s) {
        if (autoSend) {
            SendMsg(s);
        } else {
            Memo.setText(s);
        }
    }

    private void SendMsg(String s) {
        if (client != null) {
            client.HandleMsg(s);
            msgCounterOut++;
            Log.setText(s);
        }
        Memo.setText(s);
    }

    public void ResetAge() {
        KeyMatrix.Model.ResetAge();
        ShowColor();
    }

    public void UpdateRace() {
        ShowColor();
    }

    /**
     * update selectedCol, selectedRow, selectedBib, BibIndex and BibValue from
     * Column and Row
     * 
     * @param c column
     * @param r row
     * @return true if valid bib and selected cell changed
     */
    private boolean setCurrent(int c, int r) {
        selectedCol = c;
        selectedRow = r;
        selectedBib = c + (r * KeyMatrix.getColCount());
        return setBibIndexAndValue(selectedBib);
    }

    /**
     * Do not call this method directly. This private method is supposed to be
     * called only from within method setCurrent. It updates BibIndex and BibValue;
     * 
     * @param bib zero-based bib
     * @return true if we have a valid bib and it was changed from previous value.
     */
    private boolean setBibIndexAndValue(int bib) {
        if (bib < 0) {
            BibIndex = -1;
            BibValue = "0";
            return false;
        }
        if (bib >= KeyMatrix.BibCount) {
            BibIndex = -1;
            BibValue = "0";
            return false;
        }
        if (bib == BibIndex) {
            BibValue = KeyMatrix.Model.getValue(bib);
            return false;
        } else {
            BibIndex = bib;
            BibValue = KeyMatrix.Model.getValue(bib);
            return true;
        }
    }

    void AgeBtn_actionPerformed(ActionEvent e) {
        ResetAge();
    }

    void RandomBtn_actionPerformed(ActionEvent e) {
        int bib = random.nextInt(KeyMatrix.BibCount);
        if (setBibIndexAndValue(bib)) {
            HandleTransponderEvent(BibIndex, BibValue);
            ShowColor();
        }
    }

    void SendBtn_actionPerformed(ActionEvent e) {
        SendMsg(Memo.getText());
    }

    void AutoSendBtn_actionPerformed(ActionEvent e) {
        setAutoSend(!getAutoSend());
    }

    void Grid_mousePressed(MouseEvent e) {
        setCurrent(Grid.getSelectedColumn(), Grid.getSelectedRow());
    }

}

class TTimingPanel_Grid_mouseAdapter extends java.awt.event.MouseAdapter {
    TKeyPanel adaptee;

    TTimingPanel_Grid_mouseAdapter(TKeyPanel adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        adaptee.Grid_mousePressed(e);
    }
}

class TTimingPanelListSelectionListener implements ListSelectionListener {
    TKeyPanel adaptee;

    public TTimingPanelListSelectionListener(TKeyPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (!lsm.isSelectionEmpty())
            adaptee.selectedRow = lsm.getMinSelectionIndex();
    }
}

class TTimingPanelTableColumnModelListener implements TableColumnModelListener {
    TKeyPanel adaptee;

    public TTimingPanelTableColumnModelListener(TKeyPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void columnAdded(TableColumnModelEvent e) {
    }

    public void columnRemoved(TableColumnModelEvent e) {
    }

    public void columnMoved(TableColumnModelEvent e) {
    }

    public void columnMarginChanged(ChangeEvent e) {
    }

    public void columnSelectionChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;
        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        if (!lsm.isSelectionEmpty())
            adaptee.selectedCol = lsm.getMinSelectionIndex();
    }
}

class TTimingPanelKeyListener extends KeyAdapter implements KeyListener {
    TKeyPanel adaptee;

    public TTimingPanelKeyListener(TKeyPanel adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            adaptee.UpdateMemo();
        }
    }
}

class RandomBtn_actionAdapter implements java.awt.event.ActionListener {
    TKeyPanel adaptee;

    RandomBtn_actionAdapter(TKeyPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.RandomBtn_actionPerformed(e);
    }
}

class AgeBtn_actionAdapter implements java.awt.event.ActionListener {
    TKeyPanel adaptee;

    AgeBtn_actionAdapter(TKeyPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.AgeBtn_actionPerformed(e);
    }
}

class SendBtn_actionAdapter implements java.awt.event.ActionListener {
    TKeyPanel adaptee;

    SendBtn_actionAdapter(TKeyPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.SendBtn_actionPerformed(e);
    }
}

class AutoSendBtn_actionAdapter implements java.awt.event.ActionListener {
    TKeyPanel adaptee;

    AutoSendBtn_actionAdapter(TKeyPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.AutoSendBtn_actionPerformed(e);
    }
}
