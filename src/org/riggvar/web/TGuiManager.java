package org.riggvar.web;

import org.riggvar.base.*;
import org.riggvar.bo.*;
import org.riggvar.event.*;

public class TGuiManager {
    String ExceptionLabel_Caption;

    private int FRace;
    private int FIT;

    public IGuiManager GuiInterface; // injected reference
    public String AppName;

    public int getRace() {
        return FRace;
    }

    public void setRace(int value) {
        if (value > 0 && value <= TMain.BO.BOParams.RaceCount && value != FRace) {
            FRace = value;
            if (GuiInterface != null)
                GuiInterface.HandleInform(GuiAction.RaceChanged);
        }
    }

    public int getIT() {
        return FIT;
    }

    public void setIT(int value) {
        if (value >= 0 && value <= TMain.BO.BOParams.ITCount && value != FIT) {
            FIT = value;
        }
    }

    public TGuiManager() {
        TMain.GuiManager = this;

        AppName = TMain.FolderInfo.getAppName();
//          TOutputCache.CacheRequestToken = "FR.*.Request.";

        FIT = -1;
        FRace = 0;

        setIT(0);
        setRace(1);
    }

    public void DoOnException(Object sender, Exception ex) {
        // Main().Logger.Log(ex.getMessage());
        ExceptionLabel_Caption = ex.getMessage();
    }

    public void DoOnIdle() {
        TMain.BO.OnIdle();
    }

    public void InitViews() {
        if (GuiInterface != null)
            GuiInterface.InitViews();
        else
            InitCache();
    }

    public void DisposeViews() {
        if (GuiInterface != null)
            GuiInterface.DisposeViews();
        else
            DisposeCache();
    }

    private void InitNewBO() {
        // if BO is recreated, Views must immediately be reinitialized,
        // because object references become invalid;
        // in MDI all Views are destroyed, in SDI this is not possible
        FIT = 0;
        FRace = 1;
        DisposeViews();
        InitViews();
        setIT(0);
        setRace(1);
    }

    public void OpenEvent(String en) {
        String s;

        s = TMain.DocManager.DBManager.DocDownloadByName(en);
        if (s != "")
            SwapEvent(s);
    }

    public void SaveEvent() {
        TMain.DocManager.DocSave(TMain.BO);
        TMain.BO.UndoManager.Clear();
    }

    public void SaveEventAs(String en) {
        // called from WebInterface, after input of new event name
        TMain.DocManager.DBManager.setEventName(en);
        TMain.DocManager.DocSave(TMain.BO);
        UpdateCaption();
        TMain.BO.UndoManager.Clear();
    }

    // public void acSaveAsExecute(object sender)
    // {
    // Main.DocManager.DocSaveAs(BO);
    // UpdateCaption();
    // Main.BO.UndoManager.Clear();
    // }

    public void DeleteEvent(String en) {
        // not implemented
    }

    public void UpdateWorkspace(int WorkspaceType, int WorkspaceID) {
        TMain.Controller.UpdateWorkspace(WorkspaceType, WorkspaceID);
        if (GuiInterface != null)
            GuiInterface.UpdateWorkspaceStatus();
    }

    public void UpdateEventParams(int RaceCount, int ITCount, int StartlistCount) {
        boolean wasConnected;
        TBOParams newParams;

        newParams = new TBOParams();
        newParams.RaceCount = RaceCount;
        newParams.ITCount = ITCount;
        newParams.StartlistCount = StartlistCount;
        if (newParams.IsWithinLimits()) {
            wasConnected = TMain.BOConnector.getConnected();
            DisposeViews();
            TMain.Controller.RecreateBO(newParams);
            InitNewBO();
            if (wasConnected)
                TMain.BO.Connect();
        }
    }

    public void UpdateFleetProps(boolean UseFleets, int TargetFleetSize, int FirstFinalRace) {
        TEventProps Model;

        Model = TMain.BO.EventProps;
        Model.setUseFleets(UseFleets);
        Model.setFirstFinalRace(FirstFinalRace);
        Model.setTargetFleetSize(TargetFleetSize);
        TMain.BO.EventNode.setModified(true);
    }

    public void SwapEvent(String EventData) {
        if (EventData != null && !EventData.equals("")) {
            boolean wasConnected = TMain.BO.getConnected();
            DisposeViews();
            TMain.BO.LoadNew(EventData);
            InitNewBO();
            if (wasConnected)
                TMain.BO.Connect();
            if (GuiInterface != null)
                GuiInterface.UpdateCaption();
        }

//        if (TMain.PeerManager.getPeer().IsEnabled(TSwitchOp.Upload))
//        {
//            if (TMain.IniImage.AutoUpload)
//                TMain.PeerManager.getPeer().Upload(TMain.BO.Save());
//        }        
    }

    public void UpdateCaption() {
        if (GuiInterface != null)
            GuiInterface.UpdateCaption();
    }

    public void acBackupExecute(Object sender) {
        TMain.BO.BackupBtnClick();
        TMain.BO.UndoManager.Clear();
    }

    public void acRecreateExecute(Object sender) {
        // boolean wasConnected = TBaseMain.BOConnector.getConnected(); //C#
        boolean wasConnected = TMain.BO.getConnected(); // java
        DisposeViews();
        TMain.Controller.RecreateBOFromBackup();
        InitNewBO();
        if (wasConnected)
            TMain.BO.Connect();
    }

    public void acRestoreExecute(Object sender) {
        DisposeViews();
        TMain.BO.RestoreBtnClick();
        TMain.BO.Calc();
        TMain.BO.UndoManager.Clear();
        InitViews();
        if (GuiInterface != null)
            GuiInterface.HandleInform(GuiAction.acRestore);
    }

    public void acClearExecute(Object sender) {
        TMain.BO.ClearBtnClick();
        TMain.BO.Calc();
        if (GuiInterface != null)
            GuiInterface.HandleInform(GuiAction.acClear);
        TMain.BO.UndoManager.Clear();
    }

    public void LoadTestDataItemClick(Object sender) {
        SwapEvent(TMain.Controller.getTestData());
    }

    public void InitPeer() {
        if (GuiInterface != null) {
            GuiInterface.HandleInform(GuiAction.InitBridge);

            // see FrmFR66
            // Controller().getSwitchController().Connect();
            // Controller().getSwitchController().OnBackup = this;
        } else {
            // ToDo: implement OnBackup in GuiManager for gui less target
        }
    }

    public void DisposePeer() {
//        TMain.Controller.getSwitchController().Disconnect();
//        TMain.Controller.getSwitchController().OnBackup = null;    	
    }

    public void InitCache() {
    }

    public void DisposeCache() {
    }

    public void ScheduleFullUpdate(Object sender, DrawNotifierEventArgs e) {
        if (GuiInterface != null && e != null) {
            switch (e.getDrawTarget()) {
            case DrawNotifierEventArgs.DrawTargetEvent:
                GuiInterface.HandleInform(GuiAction.ScheduleEventUpdate);
                break;
            case DrawNotifierEventArgs.DrawTargetRace:
                GuiInterface.HandleInform(GuiAction.ScheduleRaceUpdate);
                break;
            }
        }
    }

    public void acUndoExecute() {
        if (TMain.BO.UndoManager.UndoCount() > 0) {
            String s = TMain.BO.UndoManager.GetUndo();
            if (TMain.BO.UndoConnection != null) {
                TMain.BO.EventBO.UndoAgent.setUndoLock(true);
                try {
                    TMain.BO.UndoConnection.InjectMsg(s);
                } finally {
                    TMain.BO.EventBO.UndoAgent.setUndoLock(false);
                }
                if (GuiInterface != null)
                    GuiInterface.HandleInform(GuiAction.acUndo);
            }
        }
    }

    public void acRedoExecute() {
        if (TMain.BO.UndoManager.RedoCount() > 0) {
            String s = TMain.BO.UndoManager.Redo();
            if (TMain.BO.UndoConnection != null) {
                TMain.BO.EventBO.UndoAgent.setUndoLock(true);
                try {
                    TMain.BO.UndoConnection.InjectMsg(s);
                } finally {
                    TMain.BO.EventBO.UndoAgent.setUndoLock(false);
                }
                if (GuiInterface != null)
                    GuiInterface.HandleInform(GuiAction.acRedo);
            }
        }
    }

    public void acColorCycleExecute() {
        TColorMode cm = TMain.BO.EventNode.ColorMode;
        cm = IncColorMode(cm);
        TMain.BO.EventNode.ColorMode = cm;
        if (GuiInterface != null)
            GuiInterface.HandleInform(GuiAction.acColor);
    }

    public void acStrictModeExecute() {
        TMain.BO.EventBO.setRelaxedInputMode(false);
        if (GuiInterface != null)
            GuiInterface.HandleInform(GuiAction.acStrict);
    }

    public void acRelaxedModeExecute() {
        TMain.BO.EventBO.setRelaxedInputMode(true);
        if (GuiInterface != null)
            GuiInterface.HandleInform(GuiAction.acRelaxed);
    }

    private TColorMode IncColorMode(TColorMode cm) {
        return cm.next();
    }

}
