package org.riggvar.dal;

import org.riggvar.base.*;

/**
 * Dummy do nothing implementation of IDBEvent2
 */
public class TDBEvent implements IDBEvent {
    public TDBEvent() {
    }

    /**
     * Load
     *
     * @param KatID     int
     * @param EventName String
     * @return String
     */
    public String Load(int KatID, String EventName) {
        return "";
    }

    /**
     * Save
     *
     * @param KatID     int
     * @param EventName String
     * @param Data      String
     */
    public void Save(int KatID, String EventName, String Data) {
    }

    /**
     * Delete
     *
     * @param KatID     int
     * @param EventName String
     */
    public void Delete(int KatID, String EventName) {
    }

    /**
     * GetEventNames
     *
     * @param KatID int
     * @return String
     */
    public String GetEventNames(int KatID) {
        return "";
    }

    /**
     * Close
     */
    public void Close() {
    }
}
