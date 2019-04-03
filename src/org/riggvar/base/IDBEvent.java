package org.riggvar.base;

public interface IDBEvent {
    String Load(int KatID, String EventName);

    void Save(int KatID, String EventName, String Data);

    void Delete(int KatID, String EventName);

    String GetEventNames(int KatID);

    void Close();
}
