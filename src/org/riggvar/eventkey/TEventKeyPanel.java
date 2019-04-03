package org.riggvar.eventkey;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.riggvar.bo.TMain;
import org.riggvar.web.GuiAction;

public class TEventKeyPanel extends TKeyPanel {
    private static final long serialVersionUID = 1L;

    protected int Race = -1;

    JButton RaceDownBtn;
    JButton RaceBtn;
    JButton RaceUpBtn;
    JButton ClearBtn;

    public TEventKeyPanel() {
        super();
    }

    @Override
    protected void InitForm() {
        Race = 1;
        try {
            Race = getRace();
            SetupGrid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        UpdateRace();
        super.InitForm();

    }

    @Override
    protected void jbInit() throws Exception {
        RaceDownBtn = new JButton();
        RaceBtn = new JButton();
        RaceUpBtn = new JButton();
        ClearBtn = new JButton();

        RaceDownBtn.setText("-R");
        RaceDownBtn.addActionListener(new RaceDownBtn_actionAdapter(this));
        RaceDownBtn.setActionCommand("Race");

        RaceBtn.setText("R");
        RaceBtn.addActionListener(new RaceBtn_actionAdapter(this));

        RaceUpBtn.setText("+R");
        RaceUpBtn.addActionListener(new RaceUpBtn_actionAdapter(this));

        ClearBtn.setText("Clear");
        ClearBtn.addActionListener(new ClearBtn_actionAdapter(this));

        toolbar.add(RaceDownBtn);
        toolbar.add(RaceBtn);
        toolbar.add(RaceUpBtn);
        toolbar.add(ClearBtn);
        toolbar.add(RandomBtn);
        toolbar.add(AgeBtn);
        toolbar.add(SendBtn);
        toolbar.add(AutoSendBtn);

        super.jbInit();
    }

    int getRace() {
        return TMain.GuiManager.getRace();
    }

    public void setRace(int value) {
        TMain.GuiManager.setRace(value);
    }

    public void UpdateRace() {
        Race = getRace();
        RaceBtn.setText("R" + Race);
        super.UpdateRace();
    }

    @Override
    protected void HandleTransponderEvent(int bibIndex, String bibValue) {

        Send("FR.*.W" + Race + ".Bib" + bibValue + ".RV=500");
        super.HandleTransponderEvent(bibIndex, bibValue);
    }

    void RaceDownBtn_actionPerformed(ActionEvent e) {
        TMain.GuiManager.setRace(Race - 1);
    }

    public void RaceBtn_actionPerformed(ActionEvent e) {
        UpdateRace();
    }

    void RaceUpBtn_actionPerformed(ActionEvent e) {
        TMain.GuiManager.setRace(Race + 1);
    }

    void ClearBtn_actionPerformed(ActionEvent e) {
        TMain.BO.EventNode.getBaseRowCollection().ClearRace(Race);
        TMain.GuiManager.GuiInterface.HandleInform(GuiAction.ScheduleEventUpdate);
        AgeBtn_actionPerformed(null);
    }

}

class RaceDownBtn_actionAdapter implements ActionListener {
    TEventKeyPanel adaptee;

    RaceDownBtn_actionAdapter(TEventKeyPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.RaceDownBtn_actionPerformed(e);
    }
}

class RaceBtn_actionAdapter implements ActionListener {
    TEventKeyPanel adaptee;

    RaceBtn_actionAdapter(TEventKeyPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.RaceBtn_actionPerformed(e);
    }
}

class RaceUpBtn_actionAdapter implements java.awt.event.ActionListener {
    TEventKeyPanel adaptee;

    RaceUpBtn_actionAdapter(TEventKeyPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.RaceUpBtn_actionPerformed(e);
    }
}

class ClearBtn_actionAdapter implements java.awt.event.ActionListener {
    TEventKeyPanel adaptee;

    ClearBtn_actionAdapter(TEventKeyPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ClearBtn_actionPerformed(e);
    }
}
