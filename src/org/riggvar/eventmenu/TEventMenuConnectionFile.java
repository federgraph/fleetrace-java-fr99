package org.riggvar.eventmenu;

import org.riggvar.base.TStringList;

public class TEventMenuConnectionFile implements IEventMenuConnection {
    String FUrl;
    String FErrorMsg;
    boolean FHasError;

    boolean UnicodeExpected = true;
    boolean hasCodingError = false;

    @Override
    public String Get() {
        try {
            TStringList SL = new TStringList();
            SL.LoadFromFile(FUrl);
            return SL.getText();
        } catch (Exception e) {
            FHasError = true;
            FErrorMsg = e.getMessage();
            return "";
        }
    }

    @Override
    public void Post(String s) {
        try {
            TStringList SL = new TStringList();
            SL.SaveToFile(FUrl);
        } catch (Exception e) {
            FHasError = true;
            FErrorMsg = e.getMessage();
        }
    }

    @Override
    public String getUrl() {
        return FUrl;
    }

    @Override
    public void setUrl(String url) {
        FUrl = url;
    }

    @Override
    public boolean hasError() {
        return FHasError;
    }

    @Override
    public String getError() {
        return FErrorMsg;
    }

}
