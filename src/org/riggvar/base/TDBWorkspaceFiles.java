package org.riggvar.base;

public class TDBWorkspaceFiles implements IDBWorkspace {
    public void SetWorkspaceID(int WorkspaceID) {
    }

    public int GetWorkspaceID() {
        return 0;
    }

    public boolean DBFileExists(String fn) {
        return false;
    }

    public boolean DBDirectoryExists(String dn) {
        return false;
    }

    public String DBGetEventNames(String ExtensionFilter) {
        return "";
    }

    public void DBLoadFromFile(String fn, TStrings SL) {
    }

    public void DBSaveToFile(String fn, TStrings SL) {
    }

    public boolean DBDeleteFile(String fn) {
        return false;
    }

    public boolean DBDeleteWorkspace() {
        return false;
    }

    public void ExportFile(String WorkspaceDir) {
    }

    public void ExportFiles(String WorkspaceDir) {
    }

}
