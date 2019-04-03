package org.riggvar.fr99;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.riggvar.base.*;
import org.riggvar.bo.*;
import org.riggvar.event.*;
import org.riggvar.output.*;
import org.riggvar.stammdaten.*;
import org.riggvar.web.*;
import org.riggvar.eventkey.*;
import org.riggvar.eventmenu.*;

public class FrameFR62 extends JFrame implements IGuiManager, IShowMemo, IFormInterface {
    private static final long serialVersionUID = 1L;

    private int paramTraceLevel = 0;
    private boolean disposed;
    private TIdleManager IdleManager;

    protected boolean showEventMenu = false;
    protected boolean showKeyPane = false;
    protected boolean showPanelColor = false;

    ColorScheme colorScheme = new ColorScheme();

    JPanel northContainer = new JPanel();
    JPanel southContainer = new JPanel();

    JPanel ToolBar = new JPanel();
    JToggleButton EventMenuBtn = new JToggleButton("Data", showEventMenu);
    JToggleButton TimingBtn = new JToggleButton("Timing", showKeyPane);
    JToggleButton StyleBtn = new JToggleButton("Style", showPanelColor);
    JButton InfoBtn = new JButton();
    JButton TestDataBtn = new JButton();

    // JPanel StatusBar = new JPanel();

    BorderLayout MainLayout = new BorderLayout();
    JMenuBar MainMenu = new JMenuBar();

    JTabbedPane PageControl = new JTabbedPane();
    JTabbedPane TabControl = new JTabbedPane();
    JPanel EventMenuPane = new JPanel();

    TStammdatenPanel PanelEntries;
    TEventKeyPanel PanelEventKey;
    TEventPanel PanelEvent;
    TReportPanel PanelStatus;
    TEventMenuPanel PanelEventMenu;
    TWorkspacePanel PanelWorkspace;

    void initConcretePanels() {
        PanelEntries = new TStammdatenPanel();
        PanelEventKey = new TEventKeyPanel();
        PanelEvent = new TEventPanel();
        PanelStatus = new TReportPanel();
        PanelEventMenu = new TEventMenuPanel();
        PanelWorkspace = new TWorkspacePanel();
    }

    void initToolbar() {
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);
        ToolBar.setLayout(fl);

        TestDataBtn.setText("Clear");
        TestDataBtn.setToolTipText("reset to builtin test data");
        TestDataBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TMain.GuiManager.SwapEvent(TMain.Controller.getTestData());
            }
        });

        EventMenuBtn.setToolTipText("toggle visibility of event menu download controls");
        EventMenuBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ToggleEventMenu();
            }
        });

        TimingBtn.setToolTipText("toggle visibility of finish position keypad");
        TimingBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ToggleKeyPad();
            }
        });

        InfoBtn.setText("Info");
        InfoBtn.setToolTipText("show info in memo on page status");
        InfoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShowMemo();
                StringBuilder sb = new StringBuilder();
                writeLogo(sb);
                String t = sb.toString();
                PanelStatus.StatusMemo.setText(t);
            }
        });

        StyleBtn.setToolTipText("toggle colouring of panel background");
        StyleBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ToggleColorScheme();
            }
        });

        ToolBar.add(TestDataBtn);
        ToolBar.add(EventMenuBtn);
        ToolBar.add(TimingBtn);
        ToolBar.add(InfoBtn);
        ToolBar.add(StyleBtn);
    }

    private void PrintDebugInfo(String s) {
        if (paramTraceLevel > 0) {
            System.out.println(s);
        }
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            Exit();
        }
    }

    public FrameFR62() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            InitForm();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void InitForm() throws Exception {
        TMain.GuiManager.GuiInterface = this;

        initConcretePanels();

        this.setSize(new Dimension(1100, 640));
        this.setTitle("FR99");
        this.setJMenuBar(MainMenu);

        initToolbar();

        northContainer.setLayout(new BorderLayout());
        northContainer.add(ToolBar, BorderLayout.NORTH);
        northContainer.add(EventMenuPane, BorderLayout.CENTER);

        southContainer.setLayout(new BorderLayout());
        southContainer.add(TabControl, BorderLayout.CENTER);
        // southContainer.add(StatusBar, BorderLayout.SOUTH);

        MainLayout.setHgap(0);
        getContentPane().setLayout(MainLayout);
        getContentPane().add(northContainer, BorderLayout.NORTH);
        getContentPane().add(southContainer, BorderLayout.SOUTH);
        getContentPane().add(PageControl, BorderLayout.CENTER);

        // EventMenu
        PanelEventMenu.setVisible(showEventMenu);
        PanelEventMenu.Memo = PanelStatus.StatusMemo;
        PanelEventMenu.TestMemo = PanelStatus.TestMemo;
        PanelEventMenu.MemoManager = this;

        // EventMenuContainer
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);
        EventMenuPane.setLayout(fl);
        EventMenuPane.add(PanelEventMenu);

        // PageControl
        PageControl.add(PanelEvent, "Event");
        PageControl.setSelectedComponent(PanelEvent);
        PageControl.add(PanelStatus, "Reports");

        // TabControl
        TabControl.setPreferredSize(new Dimension(-1, 300));
        TabControl.setVisible(showKeyPane);
        TabControl.add(PanelEntries, "Entries");
        TabControl.add(PanelEventKey, "Timing");
        TabControl.add(PanelWorkspace, "Workspace");
        TabControl.setSelectedComponent(PanelEventKey);

        // IdleManager
        IdleManager = new TIdleManager(this);
        TMain.DrawNotifier = IdleManager;
        IdleManager.setEnabled(true);
        IdleManager.EventUpdate.ScheduleFullUpdate();

        allowItems();
        updatePanelColor();

        // TMain.GuiManager.InitPeer();
    }

    private void updatePanelColor() {
        if (showPanelColor) {
            ToolBar.setBackground(colorScheme.clToolBar);
            EventMenuPane.setBackground(colorScheme.clEventMenu);
            PanelEventMenu.setBackground(colorScheme.clEventMenu);
            PageControl.getParent().setBackground(colorScheme.clPageControl);
            southContainer.setBackground(colorScheme.clSouthContainer);
        } else {
            ToolBar.setBackground(null);
            EventMenuPane.setBackground(null);
            PanelEventMenu.setBackground(null);
            PageControl.getParent().setBackground(null);
            southContainer.setBackground(null);
        }
        org.riggvar.eventkey.FR36CellRenderer.clFocus = colorScheme.clTimingCellFocus;
    }

    private void allowItems() {
        PanelEntries.LoadAthletesBtn.setEnabled(false);
        PanelEntries.SaveAthletesBtn.setEnabled(false);
    }

    private void writeLogo(StringBuilder sb) {
        sb.append("\n");
        sb.append("-       F        info: http://www.riggvar.de\n");
        sb.append("-      * * *\n");
        sb.append("-     *   *   G\n");
        sb.append("-    *     * *   *\n");
        sb.append("-   E - - - H - - - I\n");
        sb.append("-    *     * *         *\n");
        sb.append("-     *   *   *           *\n");
        sb.append("-      * *     *             *\n");
        sb.append("-       D-------A---------------B\n");
        sb.append("-                *\n");
        sb.append("-                (C) 2012 RiggVar Software UG (haftungsbeschränkt)\n");
        sb.append("\n");
    }

    @Override
    public void Exit() {
        PrintDebugInfo("Exit.");
        if (!disposed) {
            TMain.GuiManager.GuiInterface = null;
            TMain.ConnectionNotifier = null;
            disposed = true;
            IdleManager.setEnabled(false);
            String dn = TMain.FolderInfo.getSettingsPath() + ".properties";
            TMain.IniImage.SaveToFile(dn);
            TMain.Controller.Dispose();
            System.exit(0);
            PrintDebugInfo("disposed.");
        }
    }

    @Override
    public void InitViews() {
        PrintDebugInfo("InitViews.");

        PanelEvent.InitPanel();
        PanelEntries.InitPanel();
        PanelEventKey.InitPanel();

        IdleManager.EnableViews();

        TMain.GuiManager.InitPeer();
    }

    @Override
    public void DisposeViews() {
        PrintDebugInfo("DisposeViews.");
        TMain.GuiManager.DisposePeer();

        IdleManager.DisableViews();

        PanelEvent.DisposePanel();
        PanelEntries.DisposePanel();
        PanelEventKey.DisposePanel();
    }

    @Override
    public void HandleInform(GuiAction action) {
        switch (action) {
        case acClear:
            IdleManager.ScheduleFullUpdate(this, new DrawNotifierEventArgs(DrawNotifierEventArgs.DrawTargetAll));
            break;

        case acRestore:
            IdleManager.HandleInform(this, null);
            break;

        case RaceChanged:
            PanelEventKey.UpdateRace();
            PanelEventKey.ResetAge();
            break;

        case ScheduleEventUpdate:
            IdleManager.ScheduleFullUpdate(this, new DrawNotifierEventArgs(DrawNotifierEventArgs.DrawTargetEvent));

        case ScheduleRaceUpdate:
            IdleManager.ScheduleFullUpdate(this, new DrawNotifierEventArgs(DrawNotifierEventArgs.DrawTargetRace));

        case WorkspaceListChanged:
            PanelEventMenu.InitUrlCombo();

        default:
            IdleManager.HandleInform(this, null);
            break;
        }
    }

    @Override
    public void NewItem() {
        if (TMain.DocManager.DocNew(TMain.BO)) {
            DisposeViews();
            InitViews();
            UpdateCaption();
            TMain.BO.UndoManager.Clear();
        }
    }

    @Override
    public void UpdateCaption() {
        this.setTitle(TMain.GuiManager.AppName + " - " + TMain.DocManager.DBManager.getEventName());
    }

    @Override
    public void ToggleEventMenu() {
        boolean b = !PanelEventMenu.isVisible();
        PanelEventMenu.setVisible(b);
    }

    @Override
    public void ToggleKeyPad() {
        boolean b = !TabControl.isVisible();
        TabControl.setVisible(b);
    }

    public void ToggleColorScheme() {
        showPanelColor = !showPanelColor;
        updatePanelColor();
    }

    @Override
    public void UpdateWorkspaceStatus() {
    }

    @Override
    public void ShowMemo() {
        PageControl.setSelectedComponent(PanelStatus);
    }

    @Override
    public void ShowEvent() {
        PageControl.setSelectedComponent(PanelEvent);
    }

}
