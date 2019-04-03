package org.riggvar.js08;

import java.util.*;

abstract class BaseList<T extends BaseObject> extends ArrayList<T> {
//    public static final String ADD_PROPERTY = "AddItem";
//    public static final String REMOVE_PROPERTY = "RemoveItem";

    private static final long serialVersionUID = 1L;
    String fFileName;
    String fRootTag;
    String fElementTag;

    public void sort() {
        Collections.sort(this);
    }

    public void setFileName(String value) {
        fFileName = value;
    }

    public String getFileName() {
        return fFileName;
    }

    public void setRootTag(String value) {
        fRootTag = value;
    }

    public String getRootTag() {
        return fRootTag;
    }

    public void setElementTag(String value) {
        fElementTag = value;
    }

    public String getElementTag() {
        return fElementTag;
    }

    /**
     * removes any/all members of the list that are .equals() to a default instance
     * of the element class
     */
    public void removeBlanks() {
        try {
            Collection<T> c = new ArrayList<T>(5);
            for (T o : this) {
                if (o.isBlank())
                    c.add(o);
            }
            removeAll(c);
        } catch (Exception e) {
        }
    }

}
