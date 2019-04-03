package org.riggvar.eventmenu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.riggvar.base.IShowMemo;
import org.riggvar.base.TStringList;
import org.riggvar.base.TStrings;
import org.riggvar.base.Utils;
import org.riggvar.bo.TMain;

public class TEventMenuPanel extends JPanel implements IEventMenuUI {
    private static final long serialVersionUID = 1L;

    int CurrentEventIndex = -1;

//    String u0 = "http://data.riggvar.de/EventMenu.xml";
//    String u1 = "http://www.riggvar.de/results/EventMenu.xml";
//    String u2 = "http://www.fleetrace.org/DemoIndex.xml";
//    String u3 = "http://www.riggvar.de/results/EventMenuHtml.xml";

    BorderLayout EventMenuLayout = new BorderLayout();

    JPanel ComboBar = new JPanel();
    JPanel EventBtnBar = new JPanel();
    JPanel TestBtnBar = new JPanel();

    JComboBox<String> UrlCombo = new JComboBox<String>();
    JButton GetMenuBtn = new JButton();
    JComboBox<String> WorkspaceCombo = new JComboBox<String>();
    JButton DebugBtn = new JButton();

    JButton EventBtn = new JButton();

    JButton DownloadBtn = new JButton();
    JButton TransformBtn = new JButton();
    JButton ConvertBtn = new JButton();
    JCheckBox cbSkipDownload = new JCheckBox();
    JCheckBox cbSkipImport = new JCheckBox();
    JLabel EventNameLabel = new JLabel();
    JButton UrlBtn = new JButton();
    JButton WriteBtn = new JButton();
    JButton ReadBtn = new JButton();

    public JTextPane TestMemo;
    public JTextPane Memo;
    public IShowMemo MemoManager;

    private TEventMenuController EMC;
    private ActionListener comboChangedHandler;
    private boolean debugMode;

    public TEventMenuPanel() {
        try {
            jbInit();

            EMC = new TEventMenuController();
            EMC.UI = this;

            InitUrlCombo();

            comboChangedHandler = new WorkspaceCombo_actionAdapter(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(EventMenuLayout);

        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);

        ComboBar.setLayout(fl);
        EventBtnBar.setLayout(fl);
        TestBtnBar.setLayout(fl);

        ComboBar.setBackground(null);
        EventBtnBar.setBackground(null);
        TestBtnBar.setBackground(null);

        UrlCombo.setEditable(true);

        GetMenuBtn.setText("Get");
        GetMenuBtn.addActionListener(new GetMenuBtn_actionAdapter(this));

        DebugBtn.setText("More");
        DebugBtn.addActionListener(new DebugBtn_actionAdapter(this));

        WriteBtn.setText("Write");
        WriteBtn.addActionListener(new WriteBtn_actionAdapter(this));

        ReadBtn.setText("Read");
        ReadBtn.addActionListener(new ReadBtn_actionAdapter(this));

        EventBtn.setText("TestEvent");
        EventBtn.addActionListener(new EventBtn_actionAdapter(this));

        DownloadBtn.setText("Download");
        DownloadBtn.addActionListener(new DownloadBtn_actionAdapter(this));

        TransformBtn.setText("Transform");
        TransformBtn.addActionListener(new TransformBtn_actionAdapter(this));
        ConvertBtn.setText("Convert");
        ConvertBtn.addActionListener(new ConvertBtn_actionAdapter(this));

        cbSkipDownload.setText("skip Download");
        cbSkipDownload.setSelected(true);
        cbSkipDownload.setToolTipText("Show the url assigned to the EventBtn only, do not attemtp to download data.");
        cbSkipDownload.addActionListener(new SkipDownload_actionAdapter(this));

        cbSkipImport.setText("skip Import");
        cbSkipImport.setSelected(true);
        cbSkipImport
                .setToolTipText("Download event data into Memo only, do not import downloaded data into application.");
        cbSkipImport.addActionListener(new SkipImport_actionAdapter(this));

        EventNameLabel.setText("EventNameLabel");

        UrlBtn.setText("Url info");
        UrlBtn.addActionListener(new UrlBtn_actionAdapter(this));

        ComboBar.add(UrlCombo);
        ComboBar.add(GetMenuBtn);
        ComboBar.add(WorkspaceCombo);
        ComboBar.add(DebugBtn);
        ComboBar.add(WriteBtn);

        EventBtnBar.add(EventBtn);

        TestBtnBar.add(DownloadBtn);
        TestBtnBar.add(TransformBtn);
        TestBtnBar.add(ConvertBtn);
        TestBtnBar.add(ReadBtn);
        TestBtnBar.add(cbSkipDownload);
        TestBtnBar.add(cbSkipImport);
        TestBtnBar.add(EventNameLabel);
        TestBtnBar.add(UrlBtn);
        TestBtnBar.setVisible(false);

        this.add(ComboBar, BorderLayout.NORTH);
        this.add(EventBtnBar, BorderLayout.CENTER);
        this.add(TestBtnBar, BorderLayout.SOUTH);

        setDebugMode(debugMode);
    }

    TWorkspaceListBase getWorkspaceList() {
        return TMain.WorkspaceList;
    }

    public void InitUrlCombo() {
        TStrings SL = new TStringList();
        getWorkspaceList().FillCombo(SL);
        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<String>();
        for (int i = 0; i < SL.getCount(); i++) {
            m.addElement(SL.getString(i));
        }
        UrlCombo.setModel(m);
        UrlCombo.setSelectedIndex(0);
    }

    @Override
    public void UpdateWorkspaceCombo() {
        if (WorkspaceCombo.getAncestorListeners().length > 0)
            WorkspaceCombo.removeActionListener(comboChangedHandler);
        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<String>();
        for (TEventMenuImpl em : EMC.EC.MenuCollection()) {
            m.addElement(em.getComboCaption());
        }
        WorkspaceCombo.setModel(m);
        WorkspaceCombo.addActionListener(comboChangedHandler);
    }

    @Override
    public void InitEventButtons() {
        EventBtnBar.removeAll();
        JButton b;
        String en;
        TEventMenuImpl em = EMC.EC.CurrentMenu();
        for (int i = 1; i <= em.getCount(); i++) {
            en = em.GetCaption(i);
            b = new JButton();
            b.setActionCommand("" + i);
            b.setText(en);
            b.addActionListener(new EventBtn_actionAdapter(this));
            EventBtnBar.add(b);
        }
        EventBtnBar.updateUI();
    }

    @Override
    public void UpdateEventName(String EventName) {
        EventNameLabel.setText(EventName);
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean value) {
        debugMode = value;
        if (value) {
            this.setPreferredSize(new Dimension(900, 110));
            TestBtnBar.setVisible(true);
            DebugBtn.setText("Less");
            cbSkipImport.getModel().setSelected(true);
            if (MemoManager != null)
                MemoManager.ShowMemo(); // select tab report in PageControl
        } else {
            TestBtnBar.setVisible(false);
            this.setPreferredSize(new Dimension(1024, 70));
            DebugBtn.setText("More");
            cbSkipDownload.getModel().setSelected(false);
            cbSkipImport.getModel().setSelected(false);
            if (MemoManager != null)
                MemoManager.ShowEvent(); // select tab event in PageControl
        }
    }

    public void ShowMemo() {
        TestMemo.setText(EMC.TestMemoText);
        Memo.setText(EMC.MemoText);
        if (EMC.skipDownload || EMC.skipImport) {
            if (MemoManager != null)
                MemoManager.ShowMemo(); // select tab report in PageControl
        }
    }

    String getUrl() {
        Object o = UrlCombo.getModel().getSelectedItem();
        if (o != null && o instanceof String) {
            return (String) o;
        } else {
            return null;
        }
    }

    String getSelectedUrl() {
        int i = UrlCombo.getSelectedIndex();
        return getWorkspaceList().GetUrl(i);
    }

    void GetMenuBtn_actionPerformed(ActionEvent e) {
        CurrentEventIndex = -1;
        EMC.GetEventMenu(getSelectedUrl());
    }

    void WorkspaceCombo_actionPerformed(ActionEvent e) {
        int i = WorkspaceCombo.getSelectedIndex();
        EMC.EC.setMenuIndex(i);
        InitEventButtons();
    }

    void DebugBtn_actionPerformed(ActionEvent e) {
        setDebugMode(!debugMode);
    }

    void WriteBtn_actionPerformed(ActionEvent e) {
        int i = CurrentEventIndex;
        if (i > -1) {
            String u = EMC.EC.GetDataUrl(i);

            TestMemo.setText("DataUrl = " + u);

            String s;

            // s = TMain.BO.ToTXT();
            // s = TMain.BO.ToXML();
            // s = TMain.BO.ToString();

//            if (TMain.GuiManager.CacheMotor != null)
//                s = TMain.GuiManager.CacheMotor.FinishReport;
//            else
            s = TMain.BO.Output.getMsg("FR.*.Output.Report.FinishReport");

            Memo.setText(s);

            EMC.Upload(u, s);
        } else {
            TestMemo.setText("CurrentEventIndex = -1, cannot post data.");
            Memo.setText("");
        }
    }

    void ReadBtn_actionPerformed(ActionEvent e) {
        String EventData = Memo.getText();
        if (!EventData.isEmpty())
            TMain.GuiManager.SwapEvent(EventData);
    }

    void EventBtn_actionPerformed(ActionEvent e) {
        CurrentEventIndex = -1;
        String s = e.getActionCommand();
        int i = Utils.StrToIntDef(s, 0);
        EMC.skipDownload = cbSkipDownload.isSelected();
        EMC.skipImport = cbSkipImport.isSelected();
        EMC.GetEventData(i);
        ShowMemo();
        CurrentEventIndex = i;
    }

    void DownloadBtn_actionPerformed(ActionEvent e) {
        EMC.skipDownload = cbSkipDownload.isSelected();
        EMC.skipImport = cbSkipImport.isSelected();
        EMC.Download(getSelectedUrl());
        ShowMemo();
    }

    void TransformBtn_actionPerformed(ActionEvent e) {
        EMC.Transform(Memo.getText());
        ShowMemo();
    }

    void ConvertBtn_actionPerformed(ActionEvent e) {
        ShowMemo();
//		  TestMemo.Clear();
//		  EMC.GetEventData().Load(Memo.getText());
//		  Memo.setText(EMC.EventData.getText());		
    }

    void UrlBtn_actionPerformed(ActionEvent e) {
        ShowMemo();
        StringBuilder sb = new StringBuilder();
        UriInfo1(sb);
        String s = sb.toString();
        TestMemo.setText(s);
    }

    private void UriInfo1(StringBuilder sb) {
        String fs1, fs2, fs3, fs4;

        /*
         * Selected ComboEntry (WorkspaceMenuUrl): 14 (RN05)
         * http://www.fleetrace.org/DemoIndex.xml Current Button (EventDataUrl) = 2
         * (FleetTest) http://www.fleetrace.org/Demo/Test/NameTest.xml
         */

        fs1 = "Selected ComboEntry (EventMenu): %d (%s)\n";
        fs2 = "  %s\n";
        fs3 = "Current Button (EventData): %d (%s)\n";
        fs4 = "  %s\n";

        int UrlComboItemIndex = UrlCombo.getSelectedIndex();
        String UrlComboText = getUrl();
        String SelectedUrlString = getSelectedUrl();

        int CurrentBtnIndex = CurrentEventIndex;
        String CurrentBtnCaption = EMC.EC.getCaption(CurrentEventIndex);
        String CurrentBtnUrl = EMC.EC.GetDataUrl(CurrentEventIndex);

        fs1 = String.format(fs1, UrlComboItemIndex, UrlComboText);
        fs2 = String.format(fs2, SelectedUrlString);
        fs3 = String.format(fs3, CurrentBtnIndex, CurrentBtnCaption);
        fs4 = String.format(fs4, CurrentBtnUrl);

        sb.append(fs1);
        sb.append(fs2);
        sb.append(fs3);
        sb.append(fs4);
    }

    void SkipDownload_actionPerformed(ActionEvent e) {
        if (cbSkipDownload.isSelected())
            cbSkipImport.setSelected(true);
    }

    void SkipImport_actionPerformed(ActionEvent e) {
        if (!cbSkipImport.isSelected())
            cbSkipDownload.setSelected(false);
    }

}

class GetMenuBtn_actionAdapter implements ActionListener {
    private TEventMenuPanel adaptee;

    GetMenuBtn_actionAdapter(TEventMenuPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.GetMenuBtn_actionPerformed(e);
    }
}

class DebugBtn_actionAdapter implements ActionListener {
    private TEventMenuPanel adaptee;

    DebugBtn_actionAdapter(TEventMenuPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.DebugBtn_actionPerformed(e);
    }
}

class WriteBtn_actionAdapter implements ActionListener {
    private TEventMenuPanel adaptee;

    WriteBtn_actionAdapter(TEventMenuPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.WriteBtn_actionPerformed(e);
    }
}

class ReadBtn_actionAdapter implements ActionListener {
    private TEventMenuPanel adaptee;

    ReadBtn_actionAdapter(TEventMenuPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ReadBtn_actionPerformed(e);
    }
}

class EventBtn_actionAdapter implements ActionListener {
    private TEventMenuPanel adaptee;

    EventBtn_actionAdapter(TEventMenuPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.EventBtn_actionPerformed(e);
    }
}

class DownloadBtn_actionAdapter implements ActionListener {
    private TEventMenuPanel adaptee;

    DownloadBtn_actionAdapter(TEventMenuPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.DownloadBtn_actionPerformed(e);
    }
}

class TransformBtn_actionAdapter implements ActionListener {
    private TEventMenuPanel adaptee;

    TransformBtn_actionAdapter(TEventMenuPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.TransformBtn_actionPerformed(e);
    }
}

class ConvertBtn_actionAdapter implements ActionListener {
    private TEventMenuPanel adaptee;

    ConvertBtn_actionAdapter(TEventMenuPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ConvertBtn_actionPerformed(e);
    }
}

class UrlBtn_actionAdapter implements ActionListener {
    private TEventMenuPanel adaptee;

    UrlBtn_actionAdapter(TEventMenuPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.UrlBtn_actionPerformed(e);
    }
}

class SkipDownload_actionAdapter implements ActionListener {
    private TEventMenuPanel adaptee;

    SkipDownload_actionAdapter(TEventMenuPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.SkipDownload_actionPerformed(e);
    }
}

class SkipImport_actionAdapter implements ActionListener {
    private TEventMenuPanel adaptee;

    SkipImport_actionAdapter(TEventMenuPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.SkipImport_actionPerformed(e);
    }
}

class WorkspaceCombo_actionAdapter implements ActionListener {
    private TEventMenuPanel adaptee;

    WorkspaceCombo_actionAdapter(TEventMenuPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.WorkspaceCombo_actionPerformed(e);
    }
}
