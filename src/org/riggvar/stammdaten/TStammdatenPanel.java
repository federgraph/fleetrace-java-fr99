package org.riggvar.stammdaten;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.riggvar.bo.*;
import org.riggvar.stammdaten.TStammdatenBO;
import org.riggvar.stammdaten.TStammdatenColGrid;
import org.riggvar.stammdaten.TStammdatenColProp;
import org.riggvar.stammdaten.TStammdatenColProps;
import org.riggvar.stammdaten.TStammdatenNode;
import org.riggvar.stammdaten.TStammdatenRowCollection;
import org.riggvar.stammdaten.TStammdatenRowCollectionItem;

public class TStammdatenPanel extends
        TColPanel<TStammdatenColGrid, TStammdatenBO, TStammdatenNode, TStammdatenRowCollection, TStammdatenRowCollectionItem, TStammdatenColProps, TStammdatenColProp> {
    private static final long serialVersionUID = 1L;

    private boolean isFirstTimeInit = true;

    JToolBar jToolBar = new JToolBar();
    JScrollPane jScrollPane = new JScrollPane();
    BorderLayout StammdatenLayout = new BorderLayout();

    public JButton LoadAthletesBtn = new JButton();
    public JButton SaveAthletesBtn = new JButton();
    JButton ClearAthletesBtn = new JButton();
    JButton AddBtn = new JButton();
    JButton UpdateBtn = new JButton();

    public TStammdatenPanel() {

        super();
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);

        InitPanel();
    }

    @Override
    public void InitPanel() {
        try {
            BO = TMain.BO;

            fTableModel = new TStammdatenView();
            ColGrid = fTableModel.ColGrid;

            InitTable();

            if (isFirstTimeInit) {
                isFirstTimeInit = false;
                jbInit();
            }

            InitForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitForm() {
        jScrollPane.getViewport().add(jTable, null);
    }

    @Override
    public void DisposePanel() {
    }

    private void jbInit() throws Exception {
        LoadAthletesBtn.setBorder(BorderFactory.createEtchedBorder());
        LoadAthletesBtn.setActionCommand("LoadAthletes");
        LoadAthletesBtn.setText("Load");

        SaveAthletesBtn.setBorder(BorderFactory.createEtchedBorder());
        SaveAthletesBtn.setActionCommand("SaveAthletes");
        SaveAthletesBtn.setIcon(null);
        SaveAthletesBtn.setText("Save");

        AddBtn.setText("Add");
        AddBtn.addActionListener(new TStammdatenPanel_AddBtn_actionAdapter(this));
        AddBtn.setBorder(BorderFactory.createEtchedBorder());
        AddBtn.setToolTipText("Add Row");
        AddBtn.setActionCommand("AddAthlete");

        ClearAthletesBtn.setBorder(BorderFactory.createEtchedBorder());
        ClearAthletesBtn.setActionCommand("ClearAthletes");
        ClearAthletesBtn.setText("Clear");
        ClearAthletesBtn.addActionListener(new TStammdatenPanel_ClearAthletesBtn_actionAdapter(this));

        UpdateBtn.setText("Update");
        UpdateBtn.addActionListener(new TStammdatenPanel_UpdateBtn_actionAdapter(this));
        UpdateBtn.setBorder(BorderFactory.createEtchedBorder());
        UpdateBtn.setToolTipText("Update Grid");
        UpdateBtn.setActionCommand("UpdateAthletes");

        this.setLayout(StammdatenLayout);
        jToolBar.setDoubleBuffered(true);
        jToolBar.setFloatable(false);
        this.add(jScrollPane, BorderLayout.CENTER);

        this.add(jToolBar, BorderLayout.NORTH);
        jToolBar.add(LoadAthletesBtn, null);
        jToolBar.add(SaveAthletesBtn, null);
        jToolBar.add(AddBtn);
        jToolBar.addSeparator();
        jToolBar.add(ClearAthletesBtn, null);
        jToolBar.add(UpdateBtn);
    }

//    private void InvalidateView()
//    {
//        UpdateView();
//    }

    @Override
    public void UpdateView() {
        BO.OnIdle();
        ColGrid.UpdateAll();
        ColGrid.ShowData();
    }

    void UpdateBtn_actionPerformed(ActionEvent e) {
        UpdateView();
    }

    void AddBtn_actionPerformed(ActionEvent e) {
        BO.StammdatenNode.getBaseRowCollection().AddRow();
        UpdateView();
    }

    public void ClearAthletesBtn_actionPerformed(ActionEvent e) {
        BO.StammdatenNode.getBaseRowCollection().clear();
        UpdateView();
    }
}

class TStammdatenPanel_ClearAthletesBtn_actionAdapter implements ActionListener {
    private TStammdatenPanel adaptee;

    TStammdatenPanel_ClearAthletesBtn_actionAdapter(TStammdatenPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ClearAthletesBtn_actionPerformed(e);
    }
}

class TStammdatenPanel_UpdateBtn_actionAdapter implements java.awt.event.ActionListener {
    TStammdatenPanel adaptee;

    TStammdatenPanel_UpdateBtn_actionAdapter(TStammdatenPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.UpdateBtn_actionPerformed(e);
    }
}

class TStammdatenPanel_AddBtn_actionAdapter implements java.awt.event.ActionListener {
    TStammdatenPanel adaptee;

    TStammdatenPanel_AddBtn_actionAdapter(TStammdatenPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.AddBtn_actionPerformed(e);
    }
}
