package org.riggvar.eventmenu;

public class TEventMenuConnection implements IEventMenuConnection {

    @Override
    public String Get() {
        return "";
    }

    @Override
    public void Post(String s) {
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public void setUrl(String url) {
    }

    @Override
    public boolean hasError() {
        return true;
    }

    @Override
    public String getError() {
        return "not implemented";
    }

}
