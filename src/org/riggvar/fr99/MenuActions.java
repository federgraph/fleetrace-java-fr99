package org.riggvar.fr99;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.riggvar.bo.TMain;
import org.riggvar.event.TEventPanel;
import org.riggvar.event.TEventRaceEntry;
import org.riggvar.event.TEventRowCollectionItem;
import org.riggvar.eventkey.TEventKeyPanel;
import org.riggvar.output.TReportPanel;
import org.riggvar.web.GuiAction;

public class MenuActions {

    TIdleManager IdleManager;
    TEventPanel EventPanel;
    TEventKeyPanel EventKeyPanel;
    TReportPanel ReportPanel;

    IFormInterface FormInterface;

    void DocumentMenu_actionPerformed(ActionEvent e) {
        System.out.println("DocumentMenu clicked.");
    }

    void NewItem_actionPerformed(ActionEvent e) {
        FormInterface.NewItem();
    }

    void OpenItem_actionPerformed(ActionEvent e) {
        String s = TMain.DocManager.DBManager.DocDownload();
        if (!s.equals("")) {
            TMain.GuiManager.SwapEvent(s);
        }
    }

    void SaveItem_actionPerformed(ActionEvent e) {
        TMain.DocManager.DocSave(TMain.BO);
        TMain.BO.UndoManager.Clear();
    }

    void SaveAsItem_actionPerformed(ActionEvent e) {
        TMain.DocManager.DocSaveAs(TMain.BO);
        FormInterface.UpdateCaption();
        TMain.BO.UndoManager.Clear();
    }

    void DeleteItem_actionPerformed(ActionEvent e) {
        TMain.DocManager.DBManager.DocDelete();
    }

    void ExitItem_actionPerformed(ActionEvent e) {
        FormInterface.Exit();
    }

    void ConnectItem_actionPerformed(ActionEvent e) {
        TMain.BO.Connect();
    }

    void DisconnectItem_actionPerformed(ActionEvent e) {
        TMain.BO.Disconnect();
    }

    void BackupItem_actionPerformed(ActionEvent e) {
        TMain.GuiManager.acBackupExecute(null);
    }

    void RestoreItem_actionPerformed(ActionEvent e) {
        TMain.GuiManager.acRestoreExecute(null);
    }

    void FullRestoreItem_actionPerformed(ActionEvent e) {
        TMain.GuiManager.acRecreateExecute(null);
    }

    void ClearItem_actionPerformed(ActionEvent e) {
        TMain.GuiManager.acClearExecute(null);
    }

    public void CalcItem_actionPerformed(ActionEvent e) {
        TMain.BO.EventNode.setModified(true);
        TMain.BO.Calc();
    }

    public void PartialCalcItem_actionPerformed(ActionEvent e) {
        int r = EventPanel.getSelectedRaceIndex();
        if (r != -1) {
            TMain.BO.EventNode.PartialCalc(r);
        }
    }

    protected void ExportDataItem_actionPerformed(ActionEvent e) {
        ReportPanel.ExportData();
    }

    public void InitFleetItem_actionPerformed(ActionEvent e) {
        int r = EventPanel.getSelectedRaceIndex();
        if (r != -1) {
            TMain.BO.EventNode.InitFleet(r);
        }
    }

    public void InitFleetFromFinishItem_actionPerformed(ActionEvent e) {
        int r = EventPanel.getSelectedRaceIndex();
        if (r != -1) {
            TMain.BO.EventNode.InitFleetByFinishHack(r);
        }
    }

    public void CopyFleetItem_actionPerformed(ActionEvent e) {
        int r = EventPanel.getSelectedRaceIndex();
        if (r != -1) {
            TMain.BO.EventNode.CopyFleet(r);
        }
    }

    public void DisableFleetItem_actionPerformed(ActionEvent e) {
        TEventRowCollectionItem cb;
        TEventRowCollectionItem cr;
        TEventRaceEntry ere;
        TEventPanel p;
        p = EventPanel;
        int r = p.getSelectedRaceIndex();
        if (r != -1) {
            int i = p.ColGrid.Grid.getRow();
            cb = p.ColGrid.GetRowCollectionItem(i);
            if (cb != null && cb instanceof TEventRowCollectionItem) {
                cr = (TEventRowCollectionItem) cb;
                ere = cr.Race[r];
                if (ere != null)
                    TMain.BO.EventNode.DisableFleet(r, ere.Fleet, !ere.IsRacing);
            }
            IdleManager.EventUpdate.ScheduleFullUpdate();
        }
    }

    void SelectRaceItem_actionPerformed(ActionEvent e) {
        int r = EventPanel.getSelectedRaceIndex();
        TMain.GuiManager.setRace(r);
    }

    void ClearRaceItem_actionPerformed(ActionEvent e) {
        int r = EventPanel.getSelectedRaceIndex();
        if (r != -1)
            TMain.BO.EventNode.getBaseRowCollection().ClearRace(r);
        CalcItem_actionPerformed(null);
        if (r == TMain.GuiManager.getRace())
            EventKeyPanel.ResetAge();
        FormInterface.HandleInform(GuiAction.ScheduleEventUpdate);
    }

    void GoBackToRaceItem_actionPerformed(ActionEvent e) {
        int r = EventPanel.getSelectedRaceIndex() + 1;
        TMain.GuiManager.setRace(r);
        TMain.BO.EventNode.GoBackToRace(r);
        EventKeyPanel.ResetAge();
        CalcItem_actionPerformed(null);
        FormInterface.HandleInform(GuiAction.ScheduleEventUpdate);
    }

    void TestMenu_actionPerformed(ActionEvent e) {
        // System.out.println("TestMenu clicked.");
    }

    void LoadTestDataItem_actionPerformed(ActionEvent e) {
        TMain.GuiManager.SwapEvent(TMain.Controller.getTestData());
    }

    void WriteProxyXMLItem_actionPerformed(ActionEvent e) {
        TMain.BO.CalcEV.setWithTest();
        TMain.BO.EventNode.Calc();
    }

    void ShowWatchesItem_actionPerformed(ActionEvent e) {
        TMain.BO.LocalWatches.setWatchGUI(TMain.WatchGUI);
        TMain.BO.LocalWatches.Show(TMain.BO.EventProps.EventName);
    }

    void EventMenuItem_actionPerformed(ActionEvent e) {
        FormInterface.ToggleEventMenu();
    }

    void KeyPadItem_actionPerformed(ActionEvent e) {
        FormInterface.ToggleKeyPad();
    }

}

class CalcItemAdapter implements ActionListener {
    private MenuActions adaptee;

    CalcItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.CalcItem_actionPerformed(e);
    }
}

class DocumentMenuAdapter implements ActionListener {
    MenuActions adaptee;

    DocumentMenuAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.DocumentMenu_actionPerformed(e);
    }
}

class NewItemAdapter implements ActionListener {
    MenuActions adaptee;

    NewItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.NewItem_actionPerformed(e);
    }
}

class OpenItemAdapter implements ActionListener {
    MenuActions adaptee;

    OpenItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.OpenItem_actionPerformed(e);
    }
}

class SaveItemAdapter implements ActionListener {
    MenuActions adaptee;

    SaveItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.SaveItem_actionPerformed(e);
    }
}

class SaveAsItemAdapter implements ActionListener {
    MenuActions adaptee;

    SaveAsItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.SaveAsItem_actionPerformed(e);
    }
}

class DeleteItemAdapter implements ActionListener {
    MenuActions adaptee;

    DeleteItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.DeleteItem_actionPerformed(e);
    }
}

class ExitItemAdapter implements ActionListener {
    MenuActions adaptee;

    ExitItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ExitItem_actionPerformed(e);
    }
}

class BackupItemAdapter implements ActionListener {
    MenuActions adaptee;

    BackupItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.BackupItem_actionPerformed(e);
    }
}

class RestoreItemAdapter implements ActionListener {
    MenuActions adaptee;

    RestoreItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.RestoreItem_actionPerformed(e);
    }
}

class FullRestoreItemAdapter implements ActionListener {
    MenuActions adaptee;

    FullRestoreItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.FullRestoreItem_actionPerformed(e);
    }
}

class ClearItemAdapter implements ActionListener {
    MenuActions adaptee;

    ClearItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ClearItem_actionPerformed(e);
    }
}

class TestMenuAdapter implements ActionListener {
    MenuActions adaptee;

    TestMenuAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.TestMenu_actionPerformed(e);
    }
}

class LoadTestDataItemAdapter implements ActionListener {
    MenuActions adaptee;

    LoadTestDataItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.LoadTestDataItem_actionPerformed(e);
    }
}

class ShowWatchesItemAdapter implements ActionListener {
    MenuActions adaptee;

    ShowWatchesItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ShowWatchesItem_actionPerformed(e);
    }
}

class CopyFleetItemAdapter implements ActionListener {
    private MenuActions adaptee;

    CopyFleetItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.CopyFleetItem_actionPerformed(e);
    }
}

class DisableFleetItemAdapter implements ActionListener {
    private MenuActions adaptee;

    DisableFleetItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.DisableFleetItem_actionPerformed(e);
    }
}

class InitFleetFromFinishItemAdapter implements ActionListener {
    private MenuActions adaptee;

    InitFleetFromFinishItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.InitFleetFromFinishItem_actionPerformed(e);
    }
}

class InitFleetItemAdapter implements ActionListener {
    private MenuActions adaptee;

    InitFleetItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.InitFleetItem_actionPerformed(e);
    }
}

class PartialCalcItemAdapter implements ActionListener {
    private MenuActions adaptee;

    PartialCalcItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.PartialCalcItem_actionPerformed(e);
    }
}

class WriteProxyXMLItemAdapter implements ActionListener {
    MenuActions adaptee;

    WriteProxyXMLItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.WriteProxyXMLItem_actionPerformed(e);
    }
}

class ExportDataItemAdapter implements ActionListener {
    MenuActions adaptee;

    ExportDataItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ExportDataItem_actionPerformed(e);
    }
}

class EventMenuItemAdapter implements ActionListener {
    private MenuActions adaptee;

    EventMenuItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.EventMenuItem_actionPerformed(e);
    }
}

class SelectRaceItemAdapter implements ActionListener {
    private MenuActions adaptee;

    SelectRaceItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.SelectRaceItem_actionPerformed(e);
    }
}

class ClearRaceItemAdapter implements ActionListener {
    private MenuActions adaptee;

    ClearRaceItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ClearRaceItem_actionPerformed(e);
    }
}

class GoBackToRaceItemAdapter implements ActionListener {
    private MenuActions adaptee;

    GoBackToRaceItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.GoBackToRaceItem_actionPerformed(e);
    }
}

class KeyPadItemAdapter implements ActionListener {
    private MenuActions adaptee;

    KeyPadItemAdapter(MenuActions adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.KeyPadItem_actionPerformed(e);
    }
}
