package org.riggvar.base;

public interface IDBWorkspace {
    void SetWorkspaceID(int WorkspaceID);

    int GetWorkspaceID();

    boolean DBFileExists(String fn);

    boolean DBDirectoryExists(String dn);

    String DBGetEventNames(String ExtensionFilter);

    void DBLoadFromFile(String fn, TStrings SL);

    void DBSaveToFile(String fn, TStrings SL);

    boolean DBDeleteFile(String fn);

    boolean DBDeleteWorkspace();

    // TDataSet GetDataSet();
    void ExportFile(String WorkspaceDir);

    void ExportFiles(String WorkspaceDir);

}
