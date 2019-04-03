package org.riggvar.fr99;

import java.awt.*;
import javax.swing.*;

import org.riggvar.base.*;
import org.riggvar.dal.*;
import org.riggvar.bo.*;

public class FR62 {
    public static boolean packFrame = false;
    private static FrameFR62 frmFR62;

    public static void main(String[] args) {
        try {
            new FR62();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FR62() {
        InitController();
        frmFR62 = new FrameFR62();
        ShowFrame(0);
    }

    private void InitController() {
        TMain.Redirector = new TRedirector();

        TWorkspaceInfo wi = new TWorkspaceInfo();
        wi.AutoSaveIni = false;
        wi.WorkspaceType = TWorkspaceManager.WorkspaceType_Unknown;
        wi.WorkspaceID = 1;
        TMain.WorkspaceInfo = wi;

        TMain.WorkspaceManager = new TWorkspaceManager();

        TMain.FolderInfo = TFolderInfo.getInstance("FR99");
        String dn = TMain.FolderInfo.getSettingsPath() + ".properties";

        TIniImage ini = new TIniImage();
        ini.LoadFromFile(dn);
        ini.WantAdapter = false;
        ini.WantLocalAccess = false;
        ini.WantSockets = false;

        TMain.IniImage = ini;
        TMain.FormAdapter = new TBaseFormAdapter();
        TMain.DocManager = new TDocManager(new TDBManager());

        TMain.SoundManager = new TSoundManager();
        TMain.SoundManager.SoundPlayer = new TSoundPlayerAudio();

        TMain m = new TMain(ini);
        // TMain.WatchGUI = new TWatchGUI();
        m.Init();
    }

    public static void ShowFrame(int f) {
        JFrame frame = frmFR62;
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }

}
