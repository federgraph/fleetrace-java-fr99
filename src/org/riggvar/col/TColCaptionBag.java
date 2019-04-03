package org.riggvar.col;

import org.riggvar.base.*;

public class TColCaptionBag {
    TStringList FSL;
    boolean IsPersistent;

    public TColCaptionBag() {
        FSL = new TStringList();
        IsPersistent = false;
    }

    public String getCaption(String key) {
        return FSL.getValue(key);
    }

    public void setCaption(String key, String value) {
        FSL.setValue(key, value);
        IsPersistent = true;
    }

    public String getText() {
        return FSL.getText();
    }

    public void setText(String value) {
        FSL.setText(value);
    }

    public int getCount() {
        return FSL.getCount();
    }

    public boolean isPersistent() {
        return IsPersistent;
    }

    public void setIsPersistent(boolean value) {
        IsPersistent = value;
    }

}
