package org.riggvar.base;

import org.riggvar.bridge.TSwitchOp;

public interface IBridgeService {

    int Plugin();

    void Plugout(int SwitchID);

    void SendAnswer(int Target, String Answer);

    int SendBackupAndLog(int SwitchID, String Backup, String Log);

    void SendDiffLog(int SwitchID, String DiffLog);

    void SendMsg(int SwitchID, String msg);

    void SendContextMsg(int SwitchID, TContextMsg cm);

    String GetBackup();

    String GetNewMessages(int SwitchID, int StartMsgID);

    boolean LogValid();

    int GetBackupSwitchID();

    int GetLastBackupID();

    int GetLastMsgID();

    boolean CheckForBackup(int SwitchID, int StartBackupID);

    boolean CheckForLog(int SwitchID, int StartMsgID);

    String getServerUrl();

    void setServerUrl(String Value);

    boolean getHasError();

    boolean IsSameBridge();

    boolean IsEnabled(TSwitchOp Op);

    void DoOnIdle();

    void Close();

}
