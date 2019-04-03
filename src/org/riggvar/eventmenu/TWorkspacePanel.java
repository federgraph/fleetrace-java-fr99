package org.riggvar.eventmenu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

import org.riggvar.bo.TMain;
import org.riggvar.web.GuiAction;

public class TWorkspacePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    JToolBar ToolBar = new JToolBar();
    BorderLayout WorkspaceLayout = new BorderLayout();
    JButton ShowDefaultBtn = new JButton();
    JButton ShowComboBtn = new JButton();
    JButton LoadBtn = new JButton();
    JButton SaveBtn = new JButton();
    JButton InitMemoBtn = new JButton();
    JButton InitFileBtn = new JButton();

    public JTextPane Memo = new JTextPane();

    JScrollPane MemoScrollPane = new JScrollPane();

    public TWorkspacePanel() {
        try {
            initToolBar();

            MemoScrollPane.setAutoscrolls(true);
            MemoScrollPane.setVerifyInputWhenFocusTarget(true);

            Memo.setFont(new java.awt.Font("Courier New", 0, 12));
            Memo.setEditable(true);
            Memo.setText("Memo");
            Memo.setContentType("text/plain");

            this.setLayout(new BorderLayout());
            this.add(ToolBar, BorderLayout.NORTH);
            this.add(MemoScrollPane, BorderLayout.CENTER);

            MemoScrollPane.getViewport().add(Memo, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    TWorkspaceListBase getWorkspaceList() {
        return TMain.WorkspaceList;
    }

    private void Reset() {
        SaveBtn.setEnabled(false);
        InitFileBtn.setEnabled(false);
    }

    private void initToolBar() throws Exception {
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);
        ToolBar.setLayout(fl);
        ToolBar.setFloatable(false);

        ShowDefaultBtn.setText("Default");
        ShowDefaultBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Reset();
                getWorkspaceList().LoadDefault();
                Memo.setText(getWorkspaceList().GetTemp());
            }
        });

        ShowComboBtn.setText("Combo");
        ShowComboBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Reset();
                Memo.setText(getWorkspaceList().GetText());
            }
        });

        LoadBtn.setText("Load");
        LoadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getWorkspaceList().Load();
                String s = getWorkspaceList().GetTemp();
                Memo.setText(s);
                if (!s.isEmpty()) {
                    InitFileBtn.setEnabled(true);
                }
                SaveBtn.setEnabled(true);
            }
        });

        SaveBtn.setText("Save");
        SaveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getWorkspaceList().SetText(Memo.getText());
                getWorkspaceList().Save();
            }
        });

        InitMemoBtn.setText("Init from Memo");
        InitMemoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getWorkspaceList().SetText(Memo.getText());
                getWorkspaceList().Init(true);
                TMain.GuiManager.GuiInterface.HandleInform(GuiAction.WorkspaceListChanged);
            }
        });

        InitFileBtn.setText("Init from File");
        InitFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getWorkspaceList().Init(false);
                TMain.GuiManager.GuiInterface.HandleInform(GuiAction.WorkspaceListChanged);
            }
        });

        ShowDefaultBtn.setToolTipText("show default urls");
        ShowComboBtn.setToolTipText("show list of urls (as of last normal load)");
        LoadBtn.setToolTipText("load file into Memo");
        SaveBtn.setToolTipText("save Memo content to file");
        InitMemoBtn.setToolTipText("init UrlCombo from Memo");
        InitFileBtn.setToolTipText("init UrlCombo normally (file + default");

        ToolBar.add(ShowDefaultBtn);
        ToolBar.add(ShowComboBtn);
        ToolBar.add(LoadBtn);
        ToolBar.add(SaveBtn);
        ToolBar.add(InitMemoBtn);
        ToolBar.add(InitFileBtn);
    }

}
