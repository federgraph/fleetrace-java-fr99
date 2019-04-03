package org.riggvar.base;

import java.text.*;

public class TWorkspaceInfo {
    public int WorkspaceType;
    public int WorkspaceID = 1;
    public boolean AutoSaveIni = false;
    public String WorkspaceUrl;
    public String WorkspaceRoot;
    public String WorkspaceName;

    public TWorkspaceInfo() {
        WorkspaceType = TWorkspaceManager.WorkspaceType_Unknown;
        WorkspaceID = 1;
        WorkspaceRoot = "\\";

        // gsVista Fixed FS
        // WorkspaceRoot = "C:\\Users\\Public\\Documents\\Workspace\\FR64";
        // WorkspaceType = TWorkspaceManager.WorkspaceType_FixedFS;

        // gsup3 - Fixed FS
        // WorkspaceRoot = "D:\\Test\\Workspace\\FR64";
        // WorkspaceType = TWorkspaceManager.WorkspaceType_FixedFS;

        // gsup3 - Web Service
        // WorkspaceType = TWorkspaceManager.WorkspaceType_WebService;
        // WorkspaceID = 1;

        // Load();
    }

    public void Assign(TWorkspaceInfo wi) {
        WorkspaceType = wi.WorkspaceType;
        WorkspaceID = wi.WorkspaceID;
        AutoSaveIni = wi.AutoSaveIni;
        WorkspaceUrl = wi.WorkspaceUrl;
        WorkspaceRoot = wi.WorkspaceRoot;
    }

    public String getWorkspaceTypeName() {
        switch (WorkspaceType) {
        case TWorkspaceManager.WorkspaceType_SharedFS:
            return "Shared FS";
        case TWorkspaceManager.WorkspaceType_PrivateFS:
            return "Private FS";
        case TWorkspaceManager.WorkspaceType_LocalDB:
            return "Local DB";
        case TWorkspaceManager.WorkspaceType_RemoteDB:
            return "Remote DB";
        case TWorkspaceManager.WorkspaceType_WebService:
            return "Web Service";
        case TWorkspaceManager.WorkspaceType_FixedFS:
            return "Fixed FS";
        default:
            return "";
        }
    }

    public void WorkspaceReport(TStrings Memo) {
        Memo.Add(MessageFormat.format("WorkspaceInfo.WorkspaceType={0} [{1}]", WorkspaceType, getWorkspaceTypeName()));
        Memo.Add("WorkspaceInfo.WorkspaceID=" + WorkspaceID);
        Memo.Add("WorkspaceInfo.AutoSaveIni=" + Utils.BoolStr(AutoSaveIni));
        Memo.Add("WorkspaceInfo.WorkspaceUrl=" + WorkspaceUrl);
        Memo.Add("WorkspaceInfo.WorkspaceRoot=" + WorkspaceRoot);
    }

    public void WorkspaceReport(StringBuilder sb) {
        String crlf = "\r\n";
        sb.append("WorkspaceTypeName: " + getWorkspaceTypeName() + crlf);
        sb.append("WorkspaceType: " + WorkspaceType + crlf);
        sb.append("WorkspaceID: " + WorkspaceID + crlf);
        sb.append("AutoSaveIni: " + AutoSaveIni + crlf);
        sb.append("WorkspaceUrl: " + WorkspaceUrl + crlf);
        sb.append("WorkspaceRoot: " + WorkspaceRoot + crlf);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        WorkspaceReport(sb);
        return sb.toString();
    }

    public void Load() {
        // load from configuration
    }

}
