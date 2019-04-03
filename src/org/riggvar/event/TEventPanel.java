package org.riggvar.event;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.riggvar.base.*;
import org.riggvar.bo.*;
import org.riggvar.event.TColorMode;
import org.riggvar.event.TEventBO;
import org.riggvar.event.TEventColGrid;
import org.riggvar.event.TEventColProp;
import org.riggvar.event.TEventColProps;
import org.riggvar.event.TEventNode;
import org.riggvar.event.TEventRowCollection;
import org.riggvar.event.TEventRowCollectionItem;

public class TEventPanel extends
        TColPanel<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp> {
    private static final long serialVersionUID = 1L;

    JToolBar ToolBarEvent = new JToolBar();
    JButton UpdateBtn = new JButton();
    JButton StrictBtn = new JButton();
    JButton XBtn = new JButton();
    JButton PointsBtn = new JButton();
    JButton FinishBtn = new JButton();
    JButton ThrowoutsDownBtn = new JButton();
    JButton ThrowoutsBtn = new JButton();
    JButton ThrowoutsUpBtn = new JButton();
    JButton ColorModeBtn = new JButton();
    JButton SelectRaceBtn = new JButton();
    JScrollPane jScrollPane = new JScrollPane();
    BorderLayout EventPanelLayout = new BorderLayout();

    private boolean isFirstTimeInit = true;
    private TEventView FModel;

    private int fThrowouts;
    private int fThrowoutsMax;

    public TEventView EventView() {
        return FModel;
    }

    public TEventPanel() {
        super();
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        InitPanel();
    }

    @Override
    public void InitPanel() {
        try {
            BO = TMain.BO;

            FModel = new TEventView();

            fTableModel = FModel;
            ColGrid = fTableModel.ColGrid;
            fThrowouts = BO.EventProps.Throwouts;
            fThrowoutsMax = BO.BOParams.RaceCount - 1;

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

    @Override
    public void DisposePanel() {
    }

    @Override
    public boolean getModified() {
        if (FModel.Node != null)
            return FModel.Node.getModified();
        else
            return false;
    }

    private void InitForm() {
        String s = "" + fThrowouts;
        this.ThrowoutsBtn.setText(s);
        UpdateStrictBtnText();
        UpdateColorModeBtnText(BO.EventNode.ColorMode);
        jScrollPane.getViewport().add(jTable, null);
        TMain.Controller.ScheduleFullUpdate(DrawNotifierEventArgs.DrawTargetEvent);
    }

    // Component initialization
    private void jbInit() throws Exception {
        UpdateBtn.setText("Update");
        UpdateBtn.addActionListener(new TEventPanel_UpdateBtn_actionAdapter(this));
        UpdateBtn.setBorder(BorderFactory.createEtchedBorder());
        UpdateBtn.setToolTipText("Help");
        UpdateBtn.setIcon(null);
        StrictBtn.setBorder(BorderFactory.createEtchedBorder());
        StrictBtn.setText("Strict");
        StrictBtn.addActionListener(new TEventPanel_StrictBtn_actionAdapter(this));
        XBtn.setBorder(BorderFactory.createEtchedBorder());
        XBtn.setActionMap(null);
        XBtn.setText(" X ");
        XBtn.addActionListener(new TEventPanel_XBtn_actionAdapter(this));
        PointsBtn.setBorder(BorderFactory.createEtchedBorder());
        PointsBtn.setText("Points");
        PointsBtn.addActionListener(new TEventPanel_PointsBtn_actionAdapter(this));
        FinishBtn.setBorder(BorderFactory.createEtchedBorder());
        FinishBtn.setText("Finish");
        FinishBtn.addActionListener(new TEventPanel_FinishBtn_actionAdapter(this));
        ThrowoutsDownBtn.setBorder(BorderFactory.createEtchedBorder());
        ThrowoutsDownBtn.setText(" < ");
        ThrowoutsDownBtn.addActionListener(new TEventPanel_ThrowoutsDownBtn_actionAdapter(this));
        ThrowoutsBtn.setEnabled(false);
        ThrowoutsBtn.setBorder(null);
        ThrowoutsBtn.setActionCommand("");
        ThrowoutsBtn.setText("1");
        ThrowoutsUpBtn.setBorder(BorderFactory.createEtchedBorder());
        ThrowoutsUpBtn.setToolTipText("");
        ThrowoutsUpBtn.setText(" > ");
        ThrowoutsUpBtn.addActionListener(new TEventPanel_ThrowoutsUpBtn_actionAdapter(this));
        ColorModeBtn.setBorder(BorderFactory.createEtchedBorder());
        ColorModeBtn.setText("Color");
        ColorModeBtn.addActionListener(new TEventPanel_ColorModeBtn_actionAdapter(this));
        SelectRaceBtn.setBorder(BorderFactory.createEtchedBorder());
        SelectRaceBtn.setText("Select");
        SelectRaceBtn.addActionListener(new TEventPanel_SelectRaceBtn_actionAdapter(this));
        this.setLayout(EventPanelLayout);
        ToolBarEvent.setAlignmentY((float) 0.5);
        ToolBarEvent.setBorder(BorderFactory.createLineBorder(Color.black));
        ToolBarEvent.setFloatable(false);
        jScrollPane.setBorder(null);
        this.add(jScrollPane, BorderLayout.CENTER);
        ToolBarEvent.add(PointsBtn, null);
        ToolBarEvent.add(FinishBtn, null);
        ToolBarEvent.addSeparator();
        ToolBarEvent.add(StrictBtn, null);
        ToolBarEvent.addSeparator();
        ToolBarEvent.add(XBtn, null);
        ToolBarEvent.addSeparator();
        ToolBarEvent.add(ThrowoutsDownBtn, null);
        ToolBarEvent.add(ThrowoutsBtn, null);
        ToolBarEvent.add(ThrowoutsUpBtn, null);
        ToolBarEvent.addSeparator();
        ToolBarEvent.add(UpdateBtn);
        ToolBarEvent.add(ColorModeBtn);
        ToolBarEvent.add(SelectRaceBtn);
        this.add(ToolBarEvent, java.awt.BorderLayout.NORTH);
    }

    @Override
    public void UpdateView() {
        BO.OnIdle();
        ColGrid.UpdateAll();
        this.CancelScheduledUpdate(DrawNotifierEventArgs.DrawTargetEvent);
    }

    void UpdateBtn_actionPerformed(ActionEvent e) {
        BO.EventNode.ErrorList.CheckAll(BO.EventNode);
        UpdateView();
    }

    void StrictBtn_actionPerformed(ActionEvent e) {
        BO.EventBO.setRelaxedInputMode(!BO.EventBO.getRelaxedInputMode());
        UpdateStrictBtnText();
    }

    void UpdateStrictBtnText() {
        if (BO.EventBO.getRelaxedInputMode()) {
            StrictBtn.setText("Relaxed");
        } else {
            StrictBtn.setText("Strict");
        }
    }

    public int getSelectedRaceIndex() {
        int c = ColGrid.Grid.getCol();
        String sColCaption = ColGrid.Grid.getCells(c, 0);
        String sPrefix = "R";
        int RaceIndex = -1;
        if (Utils.Copy(sColCaption, 1, sPrefix.length()).equals(sPrefix)) {
            String sRaceIndex = Utils.Copy(sColCaption, sPrefix.length() + 1, sColCaption.length());
            RaceIndex = Utils.StrToIntDef(sRaceIndex, -1);
        }
        return RaceIndex;
    }

    private void SwapRaceEnabled(int r) {
        if (r != -1) {
            TEventRowCollectionItem cr = BO.EventNode.getBaseRowCollection().get(r - 1);
            if (cr != null) {
                String sValue = "$";
                sValue = BO.EventBO.EditRaceValue(cr, sValue, "colR_" + Utils.IntToStr(r));
                InvalidateView();
            }
        }
    }

    private void InvalidateView() {
        UpdateView();
    }

    void XBtn_actionPerformed(ActionEvent e) {
        this.SwapRaceEnabled(getSelectedRaceIndex());
    }

    void SelectRaceBtn_actionPerformed(ActionEvent e) {
        TMain.GuiManager.setRace(getSelectedRaceIndex());
    }

    void PointsBtn_actionPerformed(ActionEvent e) {
        BO.EventNode.setShowPoints(TEventNode.Layout_Points);
        InvalidateView();
    }

    void FinishBtn_actionPerformed(ActionEvent e) {
        BO.EventNode.setShowPoints(TEventNode.Layout_Finish);
        InvalidateView();
    }

    void ThrowoutsDownBtn_actionPerformed(ActionEvent e) {
        if (fThrowouts > 0) {
            fThrowouts--;
            NumberOfThrowoutsChanged(fThrowouts);
        }
        String s = "" + fThrowouts;
        this.ThrowoutsBtn.setText(s);
    }

    void ThrowoutsUpBtn_actionPerformed(ActionEvent e) {
        if (fThrowouts < fThrowoutsMax) {
            fThrowouts++;
            NumberOfThrowoutsChanged(fThrowouts);
        }
        String s = "" + fThrowouts;
        this.ThrowoutsBtn.setText(s);
    }

    private boolean NumberOfThrowoutsChanged(int value) {
        if (value >= BO.BOParams.RaceCount) {
            return false;
        } else if (value < 0) {
            return false;
        } else if (BO.EventProps.Throwouts != value) {
            BO.EventProps.Throwouts = value;
            BO.EventNode.setModified(true);
            InvalidateView();
        }
        return true;
    }

    public void ColorModeBtn_actionPerformed(ActionEvent e) {
        TColorMode cm = IncColorMode(BO.EventNode.ColorMode);
        UpdateColorModeBtnText(cm);
        BO.EventNode.ColorMode = cm;
        TMain.Controller.ScheduleFullUpdate(DrawNotifierEventArgs.DrawTargetEvent);
    }

    private TColorMode IncColorMode(TColorMode cm) {
        switch (cm) {
        case ColorMode_None:
            return TColorMode.ColorMode_Error;
        case ColorMode_Error:
            return TColorMode.ColorMode_Fleet;
        case ColorMode_Fleet:
            return TColorMode.ColorMode_None;
        default:
            return cm;
        }
    }

    public void UpdateColorModeBtnText(TColorMode cm) {
        switch (cm) {
        case ColorMode_None:
            ColorModeBtn.setText("Color N");
            break;
        case ColorMode_Error:
            ColorModeBtn.setText("Color E");
            break;
        case ColorMode_Fleet:
            ColorModeBtn.setText("Color F");
            break;
        }
    }

}

class TEventPanel_ColorModeBtn_actionAdapter implements ActionListener {
    private TEventPanel adaptee;

    TEventPanel_ColorModeBtn_actionAdapter(TEventPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ColorModeBtn_actionPerformed(e);
    }
}

class TEventPanel_UpdateBtn_actionAdapter implements java.awt.event.ActionListener {
    TEventPanel adaptee;

    TEventPanel_UpdateBtn_actionAdapter(TEventPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.UpdateBtn_actionPerformed(e);
    }
}

class TEventPanel_StrictBtn_actionAdapter implements java.awt.event.ActionListener {
    TEventPanel adaptee;

    TEventPanel_StrictBtn_actionAdapter(TEventPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.StrictBtn_actionPerformed(e);
    }
}

class TEventPanel_XBtn_actionAdapter implements java.awt.event.ActionListener {
    TEventPanel adaptee;

    TEventPanel_XBtn_actionAdapter(TEventPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.XBtn_actionPerformed(e);
    }
}

class TEventPanel_PointsBtn_actionAdapter implements java.awt.event.ActionListener {
    TEventPanel adaptee;

    TEventPanel_PointsBtn_actionAdapter(TEventPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.PointsBtn_actionPerformed(e);
    }
}

class TEventPanel_FinishBtn_actionAdapter implements java.awt.event.ActionListener {
    TEventPanel adaptee;

    TEventPanel_FinishBtn_actionAdapter(TEventPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.FinishBtn_actionPerformed(e);
    }
}

class TEventPanel_ThrowoutsDownBtn_actionAdapter implements java.awt.event.ActionListener {
    TEventPanel adaptee;

    TEventPanel_ThrowoutsDownBtn_actionAdapter(TEventPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ThrowoutsDownBtn_actionPerformed(e);
    }
}

class TEventPanel_ThrowoutsUpBtn_actionAdapter implements java.awt.event.ActionListener {
    TEventPanel adaptee;

    TEventPanel_ThrowoutsUpBtn_actionAdapter(TEventPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ThrowoutsUpBtn_actionPerformed(e);
    }
}

class TEventPanel_SelectRaceBtn_actionAdapter implements java.awt.event.ActionListener {
    TEventPanel adaptee;

    TEventPanel_SelectRaceBtn_actionAdapter(TEventPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.SelectRaceBtn_actionPerformed(e);
    }
}
