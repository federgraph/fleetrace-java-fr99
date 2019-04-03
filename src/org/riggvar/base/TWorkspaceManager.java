package org.riggvar.base;

import org.riggvar.bo.TMain;

public class TWorkspaceManager {
    public static final int WorkspaceType_Unknown = 0;
    public static final int WorkspaceType_SharedFS = 1;
    public static final int WorkspaceType_PrivateFS = 2;
    public static final int WorkspaceType_LocalDB = 3;
    public static final int WorkspaceType_RemoteDB = 4;
    public static final int WorkspaceType_WebService = 5;
    public static final int WorkspaceType_FixedFS = 6;

    int WorkspaceType_Default = WorkspaceType_SharedFS;
    int WorkspaceID_Default = 1;

    public IDBWorkspace DBWorkspace;

    public TWorkspaceManager() {
        TWorkspaceInfo info = TMain.WorkspaceInfo;
        // Debug.Assert(info != null, "WorkspaceInfo must be created before
        // WorkspaceManager");
        Init(info.WorkspaceType, info.WorkspaceID);
    }

    public void InitNew(TWorkspaceInfo wsi) {
        DBWorkspace = null;
        Init(wsi.WorkspaceType, wsi.WorkspaceID);
    }

    public void Init(int aWorkspaceType, int aWorkspaceID) {
        setWorkspaceID(aWorkspaceID);

        if (DBWorkspace == null || aWorkspaceType != getWorkspaceType()) {
            switch (aWorkspaceType) {
            case WorkspaceType_SharedFS:
                DBWorkspace = new TDBWorkspaceFiles();
                setUseDB(false);
                break;
            case WorkspaceType_PrivateFS:
                DBWorkspace = new TDBWorkspaceFiles();
                setUseDB(false);
                break;
            // case WorkspaceType_LocalDB:
            // FDBWorkspace = new TdmPdxWorkspaceFiles(null);
            // UseDB = DBWorkspace != null;
            // break;
            // case WorkspaceType_RemoteDB:
            // FDBWorkspace = new TSQLWorkspaceFiles();
            // UseDB = DBWorkspace != null;
            // break;
//                case WorkspaceType_WebService:
//                    DBWorkspace = new TDBWorkspaceWeb(getWorkspaceUrl());
//                    setUseDB(DBWorkspace != null);
//                    break;
            case WorkspaceType_FixedFS:
                DBWorkspace = new TDBWorkspaceFiles();
                setUseDB(false);
                break;

            default:
                DBWorkspace = new TDBWorkspaceFiles();
                setUseDB(true); // Mock!
                break;
            }
        }

        if (DBWorkspace != null) {
            // update WorkspaceInfo, FolderInfo and DBWorkspace
            setWorkspaceType(aWorkspaceType);
            setWorkspaceID(aWorkspaceID);
        }
    }

    public String getWorkspaceUrl() {
        if (TMain.WorkspaceInfo != null)
            return TMain.WorkspaceInfo.WorkspaceUrl;
        else
            return "";
    }

    public int getWorkspaceID() {
        if (TMain.WorkspaceInfo != null)
            return TMain.WorkspaceInfo.WorkspaceID;
        else
            return WorkspaceID_Default;
    }

    public void setWorkspaceID(int value) {
        if (DBWorkspace != null && TMain.WorkspaceInfo != null) {
            DBWorkspace.SetWorkspaceID(value);
            TMain.WorkspaceInfo.WorkspaceID = value;
        }
    }

    public int getWorkspaceType() {
        if (TMain.WorkspaceInfo != null)
            return TMain.WorkspaceInfo.WorkspaceType;
        else
            return WorkspaceType_Default;
    }

    public void setWorkspaceType(int value) {
        if (TMain.FolderInfo != null && TMain.WorkspaceInfo != null) {
            TMain.WorkspaceInfo.WorkspaceType = value;
            TMain.FolderInfo.WorkspaceInfoChanged();
        }
    }

    public void setUseDB(boolean value) {
        TMain.UseDB = value;
    }

}
