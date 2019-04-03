package org.riggvar.base;

import org.riggvar.bo.TMain;

public class TDocManager {
    public TStringList SLDocsAvail = new TStringList();

    public IDBManager DBManager;

    public void setDbManager(IDBManager dbManager) {
        DBManager = dbManager;
    }

    public TDocManager(IDBManager dbManager) {
        DBManager = dbManager;
    }

    protected void InitDBInterface(String CompData) {
        if (DBManager != null) {
            DBManager.setSelectedDB(CompData);
            TMain.IniImage.DBInterface = DBManager.getSelectedDB();
        }
    }

    public void EditDBEvent() {
        if (TMain.FormAdapter.EditDBEvent()) {
            if (DBManager != null)
                if (!DBManager.getSelectedDB().equals(TMain.IniImage.DBInterface))
                    InitDBInterface(TMain.IniImage.DBInterface);
        }
    }

    public void FillEventNameList(TStrings SL) {
        if (DBManager != null)
            SL.setText(DBManager.getDB().GetEventNames(DBManager.getEventType()));
        else
            SL.Clear();
    }

    protected String GetNewDocName() {
        return TMain.FormAdapter.ShowInputBox("GetNewDocName", "input a name", "NewEvent");
    }

    public boolean DocNew(TBaseBO BO) {
        if (DBManager == null) {
            return false;
        }
        SLDocsAvail.setText(DBManager.getDB().GetEventNames(DBManager.getEventType()));
        String s = GetNewDocName();
        if (SLDocsAvail.IndexOf(s) != -1) {
            TMain.FormAdapter.ShowMessageBox("NewDocName must have unique name");
            return false;
        } else if (!s.equals("")) {
            DBManager.setEventName(s);
            BO.Clear();
        }
        return true;
    }

    public void DocOpen(TBaseBO BO) {
        if (DBManager == null)
            return;

        String s = DBManager.DocDownload();
        if (!s.equals("")) {
            BO.LoadNew(DBManager.getDB().Load(DBManager.getEventType(), DBManager.getEventName())); // this still done
                                                                                                    // here
            // + further care must be taken (recreating views) by caller
        }
    }

    public void DocSave(TBaseBO BO) {
        if (DBManager == null)
            return;

        DBManager.getDB().Save(DBManager.getEventType(), DBManager.getEventName(), BO.Save());
    }

    public void DocSaveAs(TBaseBO BO) {
        if (DBManager == null)
            return;

        String s = TMain.FormAdapter.ShowInputBox("GetNewDocName", "save file as", DBManager.getEventName());
        if (!s.equals("")) {
            DBManager.setEventName(s);
            DocSave(BO);
        }
    }

}
