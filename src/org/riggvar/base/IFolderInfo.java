package org.riggvar.base;

public interface IFolderInfo {
    String getAppDir();

    String getWorkspacePath();

    String getSettingsPath();

    String getDataPath();

    String getBackupPath();

    String getHelpPath();

    String getTracePath();

    String getPublishPath();

    String getAppName();

    void WorkspaceInfoChanged();
}
