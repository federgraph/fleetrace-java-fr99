package org.riggvar.output;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.riggvar.base.TStringList;
import org.riggvar.bo.*;

import java.beans.*;
import javax.swing.BorderFactory;

public class TReportPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    JToolBar ToolBarReport = new JToolBar();
    BorderLayout ReportLayout = new BorderLayout();
    JButton DataBtn = new JButton();
    JButton ReportBtn = new JButton();
    JButton SourceBtn = new JButton();
    JButton StatusBtn = new JButton();
    JButton IniBtn = new JButton();
    JButton IndexBtn = new JButton();
    JButton HashBtn = new JButton();
    JButton SemiBtn = new JButton();
    JButton TabBtn = new JButton();

    JTextField SpaceTaker = new JTextField();

    public JTextPane StatusMemo = new JTextPane();
    public JTextPane TestMemo = new JTextPane();

    JScrollPane MemoScrollPane = new JScrollPane();

    public TReportPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TBO BO() {
        return TMain.BO;
    }

    private void jbInit() throws Exception {
        this.setLayout(ReportLayout);
        DataBtn.setBorder(BorderFactory.createEtchedBorder());
        DataBtn.setText("Data");
        DataBtn.addActionListener(new TPanelReport_DataBtn_actionAdapter(this));
        ReportBtn.setBorder(BorderFactory.createEtchedBorder());
        ReportBtn.setText("Report");
        ReportBtn.addActionListener(new TPanelReport_ReportBtn_actionAdapter(this));
        ToolBarReport.setFloatable(false);
        TestMemo.setFont(new java.awt.Font("Courier New", 0, 12));
        TestMemo.setEditable(true);
        TestMemo.setText("TestMemo (jTextPane)");
        TestMemo.setContentType("text/plain");
        StatusMemo.setFont(new java.awt.Font("Courier New", 0, 12));
        StatusMemo.setEditable(true);
        StatusMemo.setText("StatusMemo (jTextPane)");
        StatusMemo.setContentType("text/plain");
        MemoScrollPane.setAutoscrolls(true);
        MemoScrollPane.setVerifyInputWhenFocusTarget(true);
        SpaceTaker.setText("");
        StatusBtn.setBorder(BorderFactory.createEtchedBorder());
        StatusBtn.setText("Status");
        StatusBtn.addActionListener(new TReportPanel_StatusBtn_actionAdapter(this));
        SourceBtn.setBorder(BorderFactory.createEtchedBorder());
        SourceBtn.setText("Source");
        SourceBtn.addActionListener(new TReportPanel_SourceBtn_actionAdapter(this));
        IniBtn.setBorder(BorderFactory.createEtchedBorder());
        IniBtn.setText("Ini");
        IniBtn.addActionListener(new TReportPanel_IniBtn_actionAdapter(this));
        IndexBtn.setText("Index");
        IndexBtn.addActionListener(new TReportPanel_IndexBtn_actionAdapter(this));
        IndexBtn.setBorder(BorderFactory.createEtchedBorder());
        HashBtn.setText("Hash");
        HashBtn.addActionListener(new TReportPanel_HashBtn_actionAdapter(this));
        HashBtn.setBorder(BorderFactory.createEtchedBorder());
        SemiBtn.setText("Semi");
        SemiBtn.addActionListener(new TReportPanel_SemiBtn_actionAdapter(this));
        SemiBtn.setBorder(BorderFactory.createEtchedBorder());
        TabBtn.setText("Tab");
        TabBtn.addActionListener(new TReportPanel_TabBtn_actionAdapter(this));
        TabBtn.setBorder(BorderFactory.createEtchedBorder());

        ToolBarReport.add(StatusBtn);
        ToolBarReport.add(IniBtn);
        ToolBarReport.add(SourceBtn);
        ToolBarReport.add(DataBtn);
        ToolBarReport.add(IndexBtn);
        ToolBarReport.add(HashBtn);
        ToolBarReport.add(SemiBtn);
        ToolBarReport.add(TabBtn);

        ToolBarReport.add(ReportBtn);
        ToolBarReport.add(SpaceTaker);

        this.add(ToolBarReport, BorderLayout.NORTH);
        this.add(MemoScrollPane, BorderLayout.CENTER);
        this.add(TestMemo, BorderLayout.SOUTH);

        MemoScrollPane.getViewport().add(StatusMemo, null);
    }

    public void ShowTextInMemo(String s) {
        this.StatusMemo.setText(s);
    }

    void DataBtn_actionPerformed(ActionEvent e) {
        StatusMemo.setText(BO().toString());
    }

    void ReportBtn_actionPerformed(ActionEvent e) {
        UpdateReport(0);
    }

    private void UpdateReport(int ReportID) {
        StatusMemo.setText("");

        String Data = "not implemented";
        if (!Data.equals("")) {
            StatusMemo.setText(Data);
        } else {
            StatusMemo.setText("no data");
        }
    }

    void ReportSpinner_propertyChange(PropertyChangeEvent e) {
    }

    public void StatusBtn_actionPerformed(ActionEvent e) {
        StatusMemo.setText(TMain.Controller.getStatusString());
    }

    public void ExportData() {
        TExcelExporter o = new TExcelExporter();
        o.Delimiter = '\t';
        StatusMemo.setText(o.GetString(TExcelImporter.TableID_ResultList, BO()));
        StatusMemo.selectAll();
        StatusMemo.copy();
        StatusMemo.setText("");
    }

    public void SourceBtn_actionPerformed(ActionEvent e) {
        StatusMemo.setText(BO().ConvertedData);
    }

    public void IniBtn_actionPerformed(ActionEvent e) {
        StatusMemo.setText(TMain.IniImage.toString());
    }

    public void IndexBtn_actionPerformed(ActionEvent e) {
        StatusMemo.setText(TMain.BridgeBO.getReport("FR.*.Output.Report.IndexData"));
    }

    public void HashBtn_actionPerformed(ActionEvent e) {
        StatusMemo.setText(TMain.BO.CalcEV.ResultHash.MemoString());
    }

    public void SemiBtn_actionPerformed(ActionEvent e) {
        SwapSep("\t", ";");
    }

    public void TabBtn_actionPerformed(ActionEvent e) {
        SwapSep(";", "\t");
    }

    void SwapSep(String cold, String cnew) {
        TStringList SL = new TStringList();
        SL.setText(StatusMemo.getText());
        String s;
        for (int i = 0; i < SL.getCount() - 1; i++) {
            s = SL.getString(i);
            if (s.indexOf(cold) > 0) {
                s = s.replaceAll(cold, cnew);
                SL.setString(i, s);
            }
        }
        StatusMemo.setText(SL.getText());
    }
}

class TReportPanel_HashBtn_actionAdapter implements ActionListener {
    private TReportPanel adaptee;

    TReportPanel_HashBtn_actionAdapter(TReportPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.HashBtn_actionPerformed(e);
    }
}

class TReportPanel_IndexBtn_actionAdapter implements ActionListener {
    private TReportPanel adaptee;

    TReportPanel_IndexBtn_actionAdapter(TReportPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.IndexBtn_actionPerformed(e);
    }
}

class TReportPanel_StatusBtn_actionAdapter implements ActionListener {
    private TReportPanel adaptee;

    TReportPanel_StatusBtn_actionAdapter(TReportPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.StatusBtn_actionPerformed(e);
    }
}

class TPanelReport_DataBtn_actionAdapter implements java.awt.event.ActionListener {
    TReportPanel adaptee;

    TPanelReport_DataBtn_actionAdapter(TReportPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.DataBtn_actionPerformed(e);
    }
}

class TReportPanel_IniBtn_actionAdapter implements ActionListener {
    private TReportPanel adaptee;

    TReportPanel_IniBtn_actionAdapter(TReportPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.IniBtn_actionPerformed(e);
    }
}

class TPanelReport_ReportBtn_actionAdapter implements java.awt.event.ActionListener {
    TReportPanel adaptee;

    TPanelReport_ReportBtn_actionAdapter(TReportPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ReportBtn_actionPerformed(e);
    }
}

class TReportPanel_SourceBtn_actionAdapter implements ActionListener {
    private TReportPanel adaptee;

    TReportPanel_SourceBtn_actionAdapter(TReportPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {

        adaptee.SourceBtn_actionPerformed(e);
    }
}

class TReportPanel_SemiBtn_actionAdapter implements ActionListener {
    private TReportPanel adaptee;

    TReportPanel_SemiBtn_actionAdapter(TReportPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.SemiBtn_actionPerformed(e);
    }
}

class TReportPanel_TabBtn_actionAdapter implements ActionListener {
    private TReportPanel adaptee;

    TReportPanel_TabBtn_actionAdapter(TReportPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.TabBtn_actionPerformed(e);
    }
}

class TReportPanel_ReportSpinner_propertyChangeAdapter implements java.beans.PropertyChangeListener {
    TReportPanel adaptee;

    TReportPanel_ReportSpinner_propertyChangeAdapter(TReportPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void propertyChange(PropertyChangeEvent e) {
        adaptee.ReportSpinner_propertyChange(e);
    }
}
