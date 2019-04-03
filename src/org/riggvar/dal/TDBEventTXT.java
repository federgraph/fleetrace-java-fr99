package org.riggvar.dal;

import java.io.*;
import org.riggvar.base.*;
import org.riggvar.bo.TMain;

public class TDBEventTXT implements IDBEvent, FilenameFilter {
    private String suffix = ".txt";
    private String prefix;
    private int prefixLength;
    private int suffixLength;
    protected boolean isOK = false;
    private TStringList SL;
    private boolean haveLocalAccess;

    private String GetPrefix(int KatID) {
        switch (KatID) {
        case LookupKatID.FR:
            return "FR_";
        case LookupKatID.SKK:
            return "SKK_";
        case LookupKatID.SBPGS:
            return "PGS_";
        case LookupKatID.Adapter:
            return "Adapter_";
        default:
            return Utils.IntToStr(KatID) + "_";
        }
    }

    private String GetSuffix(int KatID) {
        switch (KatID) {
        case LookupKatID.FR:
            return "_FRData.txt";
        case LookupKatID.SKK:
            return ".txt";
        case LookupKatID.SBPGS:
            return ".txt";
        case LookupKatID.Adapter:
            return ".txt";
        default:
            return ".txt";
        }
    }

    private String GetFileName(int KatID, String EventName) {
        if (TMain.UseDB)
            return getDir() + EventName + ".txt";
        else
            return getDir() + GetPrefix(KatID) + EventName + GetSuffix(KatID);
    }

    private String getDir() {
        return TMain.FolderInfo.getDataPath();
    }

    public TDBEventTXT(boolean localAccess) {
        haveLocalAccess = localAccess;

        if (TMain.UseDB)
            haveLocalAccess = true;

        if (haveLocalAccess) {
            // defaults to <WorkspacePath>/DBEvent/
            File fi = new File(getDir());
            if (!fi.exists()) {
                fi.mkdir(); // create sub-directory DBEvent in workspace
                isOK = fi.isDirectory();
            }
        }
        SL = new TDBStringList();

        InitSearchPattern(400);
    }

    private void InitSearchPattern(int KatID) {
        prefix = GetPrefix(KatID);
        suffix = GetSuffix(KatID);
        prefixLength = prefix.length();
        suffixLength = suffix.length();
    }

    public boolean accept(File dir, String name) {
        boolean b = name.startsWith(prefix) && name.endsWith(suffix);
        return b;
    }

    private String fixInput(String s) {
        if (s.length() > 0) {
            s = s.replaceAll("\r\n", "\n");
            s = s.replaceAll("\n", "\r\n");
        }
        return s;
    }

    /**
     * Load
     *
     * @param KatID     int
     * @param EventName String
     * @return String
     */
    public String Load(int KatID, String EventName) {
        if (haveLocalAccess) {
            String fn = GetFileName(KatID, EventName);
            if (TMain.Redirector.DBFileExists(fn)) {
                SL.LoadFromFile(fn);
                return fixInput(SL.getText());
            }
        }
        return "";
        // return EventName;
    }

    /**
     * Save
     *
     * @param KatID     int
     * @param EventName String
     * @param Data      String
     */
    public void Save(int KatID, String EventName, String Data) {
        if (haveLocalAccess) {
            String fn = GetFileName(KatID, EventName);
            SL.setText(Data);
            SL.SaveToFile(fn); // uses platform-specific line ending
        }
    }

    /**
     * Delete
     *
     * @param KatID     int
     * @param EventName String
     */
    public void Delete(int KatID, String EventName) {
        if (haveLocalAccess) {
            String fn = GetFileName(KatID, EventName);
            if (TMain.Redirector.DBFileExists(fn)) {
                TMain.Redirector.DBDeleteFile(fn);
            }
        }
    }

    /**
     * GetEventNames
     *
     * @param KatID int
     * @return String
     */
    public String GetEventNames(int KatID) {
        try {
            if (TMain.UseDB)
                return GetEventNamesDB(KatID); // DataBase
            else
                return GetEventNamesFS(KatID); // FileSystem
        } catch (Exception ex) {
            // Debug.WriteLine(ex.Message);
            return "";
        }
    }

    private String GetEventNamesDB(int KatID) {
        return TMain.WorkspaceManager.DBWorkspace.DBGetEventNames(".txt");
    }

    private String GetEventNamesFS(int KatID) {
        InitSearchPattern(KatID);

        SL.Clear();

        if (haveLocalAccess) {
            File di = new File(getDir());
            String[] files = di.list(this); // see 'accept' FileNameFilter

            if (files == null)
                return "";

            String s = "";
            for (int i = 0; i < files.length; i++) {
                s = files[i];
                int netLength = s.length() - prefixLength - suffixLength;
                if (netLength > 0) {
                    s = s.substring(prefixLength, s.length() - suffixLength);
                    SL.Add(s);
                }
            }
        }
        return SL.getText();
    }

    /**
     * Close
     */
    public void Close() {
        // do nothing here
    }
}
