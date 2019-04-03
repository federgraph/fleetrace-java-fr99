package org.riggvar.base;

import org.riggvar.bridge.TSwitchOp;

public class TPeerController {
    public static String SwitchTokenConnect = "connect";
    public static String SwitchTokenDisconnect = "disconnect";
    public static String SwitchTokenUpload = "upload";
    public static String SwitchTokenDownload = "download";
    public static String SwitchTokenData = "data";
    public static String SwitchTokenSynchronize = "synchronize";

    public static int PluginTypeNone = 0; // -, --
    public static int PluginTypeServer = 1; // S, IO, Server, InputOutput
    public static int PluginTypeInput = 2; // I, IC, Input, InputClient
    public static int PluginTypeOutput = 3; // O, OC, Output, OutputClient
    public static int PluginTypeCacheClient = 4; // C, CC, Cache, CacheClient
    public static int PluginTypeMultilineInput = 5; // M, MI, Multiline, MultilineInput
    public static int PluginTypeBatchRequest = 6; // B, BR, Batch, BatchRequest

    public static char MsgTypeUnspecified = '-';
    public static char MsgTypeInput = 'I';
    public static char MsgTypeOutput = 'O';
    public static char MsgTypeRegister = '+';
    public static char MsgTypeCommand = 'C';
    public static char MsgRypeRequest = 'R';

    public static char MsgType2Unspecified = '-';
    public static char MsgType2Undo = 'U';
    public static char MsgType2Redo = 'R';
    public static char MsgType2MultiLine = 'M';
    public static char MsgType2Request = 'Q';

    public static char MsgCreatorUnspecified = '-';
    public static char MsgCreatorLoad = 'L';
    public static char MsgCreatorRestore = 'R';
    public static char MsgCreatorMuliLineInput = 'M';
    public static char MsgCreatorAdapter = 'A';
    public static char MsgCreatorInput = 'I';
    public static char MsgCreatorDataSender = 'D';

    public static char MsgCategoryUnspecified = '-';
    public static char MsgCategoryParameter = 'P';
    public static char MsgCategoryProperty = 'E';
    public static char MsgCategoryCommand = 'C';
    public static char MsgCategoryTest = 'T';
    public static char MsgCategoryInput = 'I';
    public static char MsgCategoryRequest = 'R';

    public boolean IsBridgeServerPluggedIn;
    public boolean PlugTouched;
    public IBridgeActionListener OnBackup;

    public TPeerController() {
    }

    public boolean IsEnabled(TSwitchOp Op) {
        return false;
    }

    public boolean IsMaster() {
        return false;
    }

    public boolean Connected() {
        return false;
    }

    public void Connect() {
    }

    public void Disconnect() {
    }

    public void DoOnIdle() {
    }

    public void EditProps() {
    }

    public boolean AllowRecreate() {
        return true;
    }

    public void Plugin() {
    }

    public void Plugout() {
    }

    public void Synchronize() {
    }

    public void Upload(String s) {
    }

    public String Download() {
        return "";
    }

    public void Close() {
    }

}
