package org.riggvar.base;

public interface IDBManager {
    void setSelectedDB(String value);

    String getSelectedDB();

    boolean isSaveAsEnabled();

    boolean isSaveEnabled();

    boolean isDeleteEnabled();

    IDBEvent getDB();

    int getEventType();

    void setEventType(int value);

    String getEventName();

    void setEventName(String value);

    String DocDownload();

    String DocDownloadByName(String en);

    void DocDelete();

}
