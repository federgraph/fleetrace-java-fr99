package org.riggvar.base;

import java.io.File;

import org.riggvar.bo.TMain;

public class TFolderInfo implements IFolderInfo {
    private static TFolderInfo fFolderInfo;

    boolean canCreateDir = false;

    String fAppDir;
    String fAppName;
    String fWorkspacePath;
    String fSettingsPath;
    String fDataPath;
    String fBackupPath;
    String fTracePath;
    String fHelpPath;
    String fPublishPath;

    public static final String StrWorkspace = "RiggVar Workspace";
    public static final String StrFSData = "DBEvent Unicode";
    public static final String StrDBData = "DBEvent";
    public static final String StrBackup = "Backup";
    public static final String StrHelp = "Help";
    public static final String StrTrace = "Trace";
    public static final String StrSettings = "Settings Java";
    public static final String StrConfigExtension = ".xml";
    public static String StrDocuments = "Eigene Dateien";
    public static final String StrPublish = "Published";

    private TFolderInfo(String appName) {
        fAppName = appName;
    }

    public static TFolderInfo getInstance(String appName) {
        if (fFolderInfo == null) {
            fFolderInfo = new TFolderInfo(appName);
        }
        return fFolderInfo;
    }

    public String getAppName() {
        if (fAppName == null) {
            fAppName = "FR00";
            if (TMain.IsWebApp)
                fAppName = TMain.AppName;
//            else
//                fAppName = Path.GetFileNameWithoutExtension(Application.ExecutablePath);

        }
        return fAppName;
    }

    public void setAppName(String value) {
        fAppName = value;
    }

    public String getAppDir() {
        if (fAppDir == null) {
            String d = java.lang.System.getProperty("user.dir");
            fAppDir = d + File.separator;
        }
        return fAppDir;
    }

    private String getSharedWorkspacePath() {
        if (fWorkspacePath == null) {
            fWorkspacePath = getAppDir(); // (old) default

            String os_name = System.getProperty("os.name");
            if (os_name != null && os_name.startsWith("Windows")) {
                File my_documents_folder = javax.swing.filechooser.FileSystemView.getFileSystemView()
                        .getDefaultDirectory();
                StrDocuments = my_documents_folder.getName();
            }
            // try to find the user-created work space in the user or personal documents
            // folder
            String d = java.lang.System.getProperty("user.home");
            File fi = new File(d);
            if (File.separator.equals("\\"))
                d = fi.getPath() + File.separator + StrDocuments + File.separator + StrWorkspace + File.separator;
            else
                d = fi.getPath() + File.separator + StrWorkspace + File.separator;

            fWorkspacePath = d;
        }
        return fWorkspacePath;
    }

    private String getLocalWorkspacePath() {

        return "";
    }

    public String getWorkspacePath() {
        if (!(fWorkspacePath == null || fWorkspacePath == ""))
            return fWorkspacePath;

        if (TMain.WorkspaceInfo == null)
            return "";

        int wt = TMain.WorkspaceInfo.WorkspaceType;
        if (wt < 1 || wt > 6) {
            if (TMain.IsWebApp)
                wt = 6; // WebBroker default
            else if (TMain.IsService)
                wt = 6; // Service default
            else
                wt = 1; // GUI Application default
        }

        switch (wt) {
        // Shared Workspace - use 'RiggVar Workspace' in user documents dir
        case 1:
            fWorkspacePath = getSharedWorkspacePath();
            canCreateDir = true;

            // C# code:
            // String dn = Environment.GetFolderPath(Environment.SpecialFolder.Personal);
            // FWorkspacePath = System.IO.Path.Combine(dn, TFolderInfo.StrWorkspace);
            // CreateDir(FWorkspacePath);
            break;

        // Local Workspace - create 'RiggVar Workspace' as subfolder
        case 2:
            fWorkspacePath = getLocalWorkspacePath();

            // C# code:
            // FWorkspacePath = Assembly.GetExecutingAssembly().FullName;
            // FWorkspacePath = Path.Combine(FWorkspacePath, StrWorkspace);
            // CreateDir(FWorkspacePath);
            break;

        // Local DB
        case 3:
            fWorkspacePath = "\\";

            // CreateDir(FWorkspacePath); //( create root directory within DB )
            break;

        // Remote DB / WebService
        case 4:
        case 5:
            fWorkspacePath = "\\";
            break;

        // Fixed WorkspaceRoot in local FileSystem (must exist)
        case 6:
            String d = TMain.WorkspaceInfo.WorkspaceRoot;
            if (d != null && d != "") {
                File fi = new File(d);
                if (fi.isDirectory()) {
                    fWorkspacePath = IncludeTrailingPathDelimiter(d);
                    canCreateDir = true;
                }
            }

            // fall back to using Mock
            if (fWorkspacePath == null || fWorkspacePath == "") {
                TMain.UseDB = true; // Mock!
                fWorkspacePath = "\\";
                canCreateDir = false; // optional
                break;
            }
            break;

        }

        return fWorkspacePath;
    }

    private String IncludeTrailingPathDelimiter(String dn) {
        if (!dn.endsWith(File.separator))
            return dn + File.separator;
        return dn;
    }

    private void CreateDir(String dn) {
        if (!TMain.Redirector.DBDirectoryExists(dn))
            TMain.Redirector.DBCreateDir(dn);
    }

    protected String getWorkspaceSubDir(String s) {
        String d = getWorkspacePath() + s + File.separator;
        File fi = new File(d);
        if (!fi.exists()) {
            if (canCreateDir)
                CreateDir(d);
        }
        return d;
    }

    public String getSettingsPath() {
        if (fSettingsPath == null) {
            fSettingsPath = getWorkspaceSubDir(StrSettings);
        }
        return fSettingsPath + getAppName();
    }

    public String getDataPath() {
        if (fDataPath == null) {
            if (TMain.UseDB)
                fDataPath = getWorkspaceSubDir(StrDBData);
            else
                fDataPath = getWorkspaceSubDir(StrFSData);
        }
        return fDataPath;
    }

    public String getBackupPath() {
        if (fBackupPath == null) {
            fBackupPath = getWorkspaceSubDir(StrBackup);
        }
        return fBackupPath + getAppName();
    }

    public String getHelpPath() {
        if (fHelpPath == null) {
            fHelpPath = getWorkspaceSubDir(StrHelp);
        }
        return fHelpPath + getAppName();
    }

    public String getTracePath() {
        if (fTracePath == null) {
            fTracePath = getWorkspaceSubDir(StrTrace);
        }
        return fTracePath + getAppName();
    }

    public String getPublishPath() {
        if (fPublishPath == null) {
            fPublishPath = getWorkspaceSubDir(StrPublish);
        }
        return fPublishPath + getAppName();
    }

    @Override
    public String toString() {
        String nl = "\n";
        StringBuffer sb = new StringBuffer();

        sb.append("AppName: ");
        sb.append(getAppName());
        sb.append(nl);

        sb.append("AppDir: ");
        sb.append(getAppDir());
        sb.append(nl);

        sb.append("WorkspacePath: ");
        sb.append(getWorkspacePath());
        sb.append(nl);

        sb.append("SettingsPath: ");
        sb.append(getSettingsPath());
        sb.append(nl);

        sb.append("DataPath: ");
        sb.append(getDataPath());
        sb.append(nl);

        sb.append("BackupPath: ");
        sb.append(getBackupPath());
        sb.append(nl);

        sb.append("HelpPath: ");
        sb.append(getHelpPath());
        sb.append(nl);

        sb.append("TracePath: ");
        sb.append(getTracePath());
        sb.append(nl);

        sb.append("PublishPath: ");
        sb.append(getPublishPath());
        sb.append(nl);

        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            TFolderInfo o = new TFolderInfo("FR00");
            System.out.println(o.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void WorkspaceInfoChanged() {
        Clear();
    }

    private void Clear() {
        fWorkspacePath = null;
        fSettingsPath = null;
        fDataPath = null;
        fBackupPath = null;
        fHelpPath = null;
        fTracePath = null;
        fPublishPath = null;
    }

}
