package org.riggvar.dal;

import org.riggvar.base.*;
import org.riggvar.bo.TIniImage;
import org.riggvar.bo.TMain;

public class TDBManager implements IDBManager {
    private TStringList SLDocsAvail = new TStringList();

    protected IDBEvent DB = new TDBEvent();
    protected String selectedDB = "";

    protected boolean saveEnabled = false;
    protected boolean saveAsEnabled = false;
    protected boolean deleteEnabled = false;

    private String eventName;
    private int eventType;

    public TDBManager() {
        eventName = TMain.IniImage.DefaultEventName;
        eventType = TIniImage.DefaultEventType;
        setSelectedDB(TMain.IniImage.DBInterface);
    }

    protected String ChooseDocAvail() {
        return TMain.FormAdapter.SelectName("Select Document", "Documents in Database:", SLDocsAvail);
    }

    public void setSelectedDB(String CompData) {
        saveEnabled = false;
        saveAsEnabled = false;
        deleteEnabled = false;
    }

    public String DocDownload() {
        SLDocsAvail.setText(DB.GetEventNames(eventType));
        String en = ChooseDocAvail();
        return DocDownloadByName(en);
    }

    public String DocDownloadByName(String en) {
        if (!en.equals("")) {
            eventName = en;
            return DB.Load(eventType, en);
        }
        return "";
    }

    public void DocDelete() {
        SLDocsAvail.setText(DB.GetEventNames(eventType));
        // Get Name of Document to be deleted
        String s = ChooseDocAvail();
        if (!s.equals(""))
            DB.Delete(eventType, s);
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int EventType) {
        this.eventType = EventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String EventName) {
        this.eventName = EventName;
    }

    public boolean isSaveAsEnabled() {
        return saveAsEnabled;
    }

    public boolean isSaveEnabled() {
        return saveEnabled;
    }

    public boolean isDeleteEnabled() {
        return deleteEnabled;
    }

    public String getSelectedDB() {
        return selectedDB;
    }

    public IDBEvent getDB() {
        return DB;
    }

}
