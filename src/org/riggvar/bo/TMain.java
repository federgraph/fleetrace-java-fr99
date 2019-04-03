package org.riggvar.bo;

import java.beans.*;
import java.util.*;

import org.riggvar.base.*;
import org.riggvar.dal.*;
import org.riggvar.eventmenu.TWorkspaceList;
import org.riggvar.eventmenu.TWorkspaceListBase;
import org.riggvar.conn.*;
//import org.riggvar.bridge.*;
//import org.riggvar.bridgeserver.TServerBridge;
import org.riggvar.web.*;

public class TMain // extends TBaseMain
        implements IBOPermission {
    public static TMain Controller = null;
    public static TIniImage IniImage;
    public static TBO BO;
    public static TAdapterBO AdapterBO;
    public static TBOManager BOManager;
//    public static TPeerManager PeerManager;
    public static TGuiManager GuiManager;
    public static TSoundManager SoundManager;
    public static TRedirector Redirector;
    public static TWorkspaceListBase WorkspaceList;
    public static TWorkspaceInfo WorkspaceInfo;
    public static TWorkspaceManager WorkspaceManager;
    public static TDocManager DocManager;
    public static TBaseFormAdapter FormAdapter;

    public static IWatchGUI WatchGUI;
    public static IMsgListFactory MsgListFactory;
    public static IBOConnector BOConnector;
    public static IBOPermission BOPermission;
    public static IFolderInfo FolderInfo;
    public static ISwitchBO SwitchBO;
    public static IBridgeBO BridgeBO;
    public static IConnectionNotifier ConnectionNotifier = new TConnectionNotifier();
    public static IDrawNotifier DrawNotifier = new TDrawNotifier();

    public static boolean IsService = false;
    public static boolean UseDB = false;
    public static boolean WantAutoSync = true;
    public static boolean WantBridgeMsgBroadcast = true;
    public static boolean ReadWorkspaceInfo = true;
    public static boolean AutoStartJetty = false;

    public static boolean IsWebApp = false;
    public static boolean DocManager_Web_Enabled = false;
    public static String AppName = "FR64";
    public static String ContextPath = "";
    public static String ContextRoot = "";

    protected boolean disposed;
    protected boolean haveSocketPermission = false;
    protected boolean socketPermissionChecked = false;

    public TMain() {
        this(new TIniImage());
    }

    public TMain(TIniImage ini) {
        Controller = this;
        IniImage = ini;

        TSocketConnection.RegisteredKatID = LookupKatID.FR;

        // if dependencies have not already been injected by caller...
        if (WorkspaceInfo == null) {
            WorkspaceInfo = new TWorkspaceInfo();
            WorkspaceInfo.Load();
        }
        if (WorkspaceManager == null)
            WorkspaceManager = new TWorkspaceManager();
        if (FolderInfo == null)
            FolderInfo = TFolderInfo.getInstance("FR62");
        if (SoundManager == null)
            SoundManager = new TSoundManager();
        if (WorkspaceList == null) {
            WorkspaceList = new TWorkspaceList();
            WorkspaceList.Init(false);
        }

        haveSocketPermission = !ini.IsApplet;
        if (ini.WantAdapter) {
            if (HaveSocketPermission()) {
                if (!Beans.isDesignTime()) {
                    IniImage.WantAdapter = true;
                    IniImage.WantSockets = true;
                }
            }
        }

        BOManager = new TBOManager();

        if (IniImage.WantAdapter && IniImage.WantSockets) {
            BOManager.CreateAdapterBO();
        }

        BOConnector = BOManager;

        SwitchBO = new SwitchBO();
        BridgeBO = new BridgeBO();
        BOPermission = new TBOPermission();

        GuiManager = new TGuiManager();

        // it is expected that the 'initialize method' will be called
        // 'after' injection of FormAdapter and DocManager references

//        if (IniImage.AutoPlugin) {
//            PeerManager.getPeer().Plugin();
//            if (IniImage.AutoUpload)
//                PeerManager.getPeer().Upload(BO.Save()); // Save(false));
//        }
    }

    public void Init() {

        if (FormAdapter == null) {
            FormAdapter = new TBaseFormAdapter();
        }

        if (DocManager == null) {
            DocManager = new TDocManager(new TDBManager());
        }

        // WatchGUI depends on package with forms,
        // initialize it from project-specific startup class
        // if (WatchGUI == null)
        // {
        // WatchGUI = new TWatchGUI();
        // }

//        PeerManager = new TPeerManager();
        BO.StatusFeedback.Enabled = IniImage.FeedbackEnabled;
        BOManager.ConnectBO();
    }

    @Override
    public void finalize() throws Throwable {
        System.out.println("in TMain.finalize()");
        if (!disposed) {
            Dispose();
        }
        // super.finalize(); //now deprecated in Oracle Java 9 and I assume, not needed.
    }

    public void Dispose() {
        disposed = true;
        DrawNotifier = null;
//        getSwitchController().Close();
        if (BOManager.AdapterBO != null) {
            BOManager.AdapterBO.Dispose();
        }
        BOManager.DisconnectBO();
        BO.StatusFeedback.Dispose();
    }

    public boolean isDisposed() {
        return disposed;
    }

    public boolean HaveLocalAccess() {
        if (BOPermission != null)
            return BOPermission.HaveLocalAccess();
        return false;
    }

    public boolean HaveNetworkAccess() {
        if (BOPermission != null)
            return BOPermission.HaveNetworkAccess();
        return false;
    }

    public boolean HaveWebPermission() {
        if (BOPermission != null)
            return BOPermission.HaveWebPermission();
        return false;
    }

    public boolean HaveSocketPermission() {
        if (BOPermission != null)
            return BOPermission.HaveSocketPermission();
        return false;
    }

    public String getTestData() {
        return BOManager.GetTestData();
    }

    public void LoadNew(String data) {
        BOManager.LoadNew(data);
    }

    public void RecreateBOFromBackup() {
        BOManager.RecreateBOFromBackup();
    }

    public void RecreateBO(TBOParams newParams) {
        BOManager.RecreateBO(newParams);
    }

    // to be injected
    public void setDocManager(TDocManager value) {
        DocManager = value;
    }

//    public TPeerController getSwitchController() {
//        if (PeerManager != null)
//            return PeerManager.getPeer();
//        return null;
//    }

    public void ScheduleFullUpdate(int target) {
        ScheduleFullUpdate(null, new DrawNotifierEventArgs(target, DrawNotifierEventArgs.OperationSchedule));
    }

    public void ScheduleFullUpdate(Object sender, DrawNotifierEventArgs e) {
        if (DrawNotifier != null)
            DrawNotifier.ScheduleFullUpdate(sender, e);
    }

    public String getStatusString() {
        String crlf = "\r\n";
        StringBuilder sb = new StringBuilder();

        sb.append(Calendar.getInstance().getTime().toString() + crlf);

        sb.append(crlf);
        sb.append("--- Connections ---");
        sb.append(crlf);

        int i = 0;
        i = BO.InputServer.Server.ConnectionCount();
        sb.append("InputConnections: " + i + crlf);
        i = BO.OutputServer.Server.ConnectionCount();
        sb.append("OutputConnections: " + i + crlf);
        if (AdapterBO != null) {
            i = AdapterBO.InputServer.Server.ConnectionCount();
            sb.append("AdapterInputConnections: " + i + crlf);

            i = AdapterBO.OutputServer.Server.ConnectionCount();
            sb.append("AdapterOutputConnections: " + i + crlf);
        }

        sb.append(crlf);
        sb.append("--- Workspace Info ---");
        sb.append(crlf);
        WorkspaceInfo.WorkspaceReport(sb);

        return sb.toString();
    }

    class SwitchBO implements ISwitchBO {
        public int getPortIn() {
            return AdapterBO.InputServer.Server.Port();
        }

        public int getPortOut() {
            return AdapterBO.OutputServer.Server.Port();
        }

        public void Reply(Object Connection, String s) {
            AdapterBO.InputServer.Server.Reply(Connection, s);
        }

        public void HandleContextMsg(TContextMsg cm) {
            BO.InputServer.HandleMsg(cm);
        }

        public TBaseIniImage getIniImage() {
            return IniImage;
        }
    }

    class BridgeBO implements IBridgeBO {
        public IConnection getInputConnection() {
            return BO.InputServer.Server.Connect();
        }

        public IConnection getOutputConnection() {
            return BO.OutputServer.Server.Connect();
        }

        public void InjectClientBridgeMsg(String s) {
            MsgContext.BridgeLocked = true;
            try {
                TBO bo = BO;
                TBaseMsg msg = bo.NewMsg();
                msg.Prot = s;
                if (msg.DispatchProt()) {
//                  if (WantAutoSync)
//                      TMain.GuiManager.CacheMotor.SynchronizeIfNotActive();
                    if (WantBridgeMsgBroadcast) {
                        bo.Calc();
                        bo.OutputServer.InjectMsg(LookupKatID.FR, TMsgSource.Bridge, msg.Prot);
                    }
                    if (s.indexOf("IT") > 0) {
                        DrawNotifier.ScheduleFullUpdate(this,
                                new DrawNotifierEventArgs(DrawNotifierEventArgs.DrawTargetRace));
                    } else {
                        DrawNotifier.ScheduleFullUpdate(this,
                                new DrawNotifierEventArgs(DrawNotifierEventArgs.DrawTargetEvent));
                    }
                    SoundManager.PlaySound(TSoundManager.Sound_Click);
                } else {
                    SoundManager.PlaySound(TSoundManager.Sound_Recycle);
                }
            } finally {
                MsgContext.BridgeLocked = false;
            }

        }

        public void InjectServerBridgeMsg(TContextMsg cm) {
            TBaseMsg msg = BO.NewMsg();
            try {
                MsgContext.BridgeLocked = true;
                msg.Prot = cm.msg;
                if (msg.DispatchProt()) {
//                  if (WantAutoSync)
//                      TMain.GuiManager.CacheMotor.SynchronizeIfNotActive();

                    BO.OutputServer.SendMsg(0, cm);

                    if (cm.msg.indexOf("IT") > 0) {
                        DrawNotifier.ScheduleFullUpdate(this,
                                new DrawNotifierEventArgs(DrawNotifierEventArgs.DrawTargetRace));
                    } else {
                        DrawNotifier.ScheduleFullUpdate(this,
                                new DrawNotifierEventArgs(DrawNotifierEventArgs.DrawTargetEvent));
                    }

                }
            } finally {
                MsgContext.BridgeLocked = false;
            }
        }

        public void Broadcast(String s) {
            TBaseMsg msg = new TBOMsg();
            msg.Prot = s;
            if (msg.DispatchProt()) {
                if (IniImage.EventType == LookupKatID.FR) {
                    TContextMsg cm = new TContextMsg();
                    cm.msg = s;
                    BO.OutputServer.SendMsg(LookupKatID.FR, cm);

                    if (s.indexOf("IT") > 0) {
                        DrawNotifier.ScheduleFullUpdate(this,
                                new DrawNotifierEventArgs(DrawNotifierEventArgs.DrawTargetRace));
                    } else {
                        DrawNotifier.ScheduleFullUpdate(this,
                                new DrawNotifierEventArgs(DrawNotifierEventArgs.DrawTargetEvent));
                    }
                } else if (IniImage.EventType == LookupKatID.SKK) {
                    BO.Calc();
                    TContextMsg cm = new TContextMsg();
                    cm.msg = s;
                    BO.OutputServer.SendMsg(LookupKatID.SKK, cm);
                }
            }
        }

        public String getReport(String sRequest) {
            return BO.Output.getMsg(sRequest);
        }

        public TBaseIniImage getIniImage() {
            return IniImage;
        }

    }

    class TBOPermission implements IBOPermission {
        public boolean HaveLocalAccess() {
            if (!IniImage.WantLocalAccess)
                return false;
            return true;
        }

        public boolean HaveNetworkAccess() {
            if (disposed)
                return false;
            if (!IniImage.WantAdapter)
                return false;
            if (!HaveSocketPermission())
                return false;
            return true;
        }

        public boolean HaveWebPermission() {
            return HaveSocketPermission();
        }

        public boolean HaveSocketPermission() {
            if (socketPermissionChecked)
                return haveSocketPermission;

            // check Permission only once
            socketPermissionChecked = true;
            int m = 0;
            try {
                SecurityManager security = System.getSecurityManager();
                if (security != null) {
                    m++;
                    security.checkAccept(IniImage.SwitchHost, IniImage.PortIn);
                    m++;
                    security.checkAccept(IniImage.SwitchHost, IniImage.PortOut);
                    m++;
                    security.checkConnect(IniImage.SwitchHost, IniImage.SwitchPort);
                }
                haveSocketPermission = true;
            } catch (SecurityException ex) {
                System.err.println("no Socket-Permission: code " + m);
            }
            return haveSocketPermission;
        }
    }

    public String ChooseNewEventName() {
        return "";
    }

    public String GetNewDocName() {
        return "";
    }

    public String ChooseDocAvail(TStringList SL) {
        return "";
    }

    public boolean ChooseDB() {
        return false;
    }

    public String getWebStatusString() {
        TStringList SL;
        int i;
        String s;

        SL = new TStringList();
        try {
            i = IniImage.PortIn;
            SL.Add(Utils.IntToStr(i));

            i = IniImage.PortOut;
            SL.Add(Utils.IntToStr(i));

            i = IniImage.WebServerPort;
            SL.Add(Utils.IntToStr(i));

            i = GuiManager.getRace();
            SL.Add(Utils.IntToStr(i));

            i = GuiManager.getIT();
            SL.Add(Utils.IntToStr(i));

//          if (PeerManager.getPeer().Connected())
//              SL.Add("Plugged");
//          else
//              SL.Add("Unplugged");

            if (BO.getConnected())
                SL.Add("Connected");
            else
                SL.Add("Disconnected");

            s = DocManager.DBManager.getSelectedDB();
            SL.Add(s);

            s = WorkspaceInfo.getWorkspaceTypeName();
            SL.Add(s);

            i = WorkspaceInfo.WorkspaceID;
            SL.Add(Utils.IntToStr(i));

            if (WorkspaceInfo.WorkspaceType == 5) {
                s = WorkspaceInfo.WorkspaceUrl;
                SL.Add(s);
            }

            s = SL.getString(0);
            for (int t = 1; t < SL.getCount(); t++)
                s = s + " | " + SL.getString(t);

            return s;

        } catch (Exception ex) {
            return "";
        }
    }

    public void UpdateWorkspace(int WorkspaceType, int WorkspaceID) {
        if (WorkspaceType != WorkspaceInfo.WorkspaceType || WorkspaceID != WorkspaceInfo.WorkspaceID) {
            InitWorkspace(WorkspaceType, WorkspaceID);
        }
    }

    public void InitWorkspace(int WorkspaceType, int WorkspaceID) {
        if (WorkspaceManager != null) {
            WorkspaceManager.Init(WorkspaceType, WorkspaceID);
        }
    }

//    public boolean isServerBridge() {
//        return getServerBridge() != null;
//    }

//    public TServerBridge getServerBridge() {
//            TBridgeController BridgeController;
//            IBridgeService Bridge;
//            TServerBridge result = null;
//            if (PeerManager.getProviderID() == 2) // Bridge
//            {
//                if (PeerManager.getPeer() instanceof TBridgeController) {
//                    BridgeController = (TBridgeController) PeerManager.getPeer();
//                    if (BridgeController.ProxyType == TBridgeProxyType.Server) // ServerBridge
//                    {
//                        Bridge = BridgeController.Bridge();
//                        if (Bridge instanceof TServerBridge) {
//                            result = (TServerBridge) Bridge;
//                        }
//                    }
//                }
//            }
//            return result;
//    }

}
