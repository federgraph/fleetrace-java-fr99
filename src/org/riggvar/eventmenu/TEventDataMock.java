package org.riggvar.eventmenu;

public class TEventDataMock implements IEventData {

    @Override
    public void Load(String ed) {

    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public String getError() {
        return "not implemented";
    }

    @Override
    public String getInfo() {
        return "TEventDataMock - Info";
    }

    @Override
    public boolean hasError() {
        return true;
    }

    @Override
    public boolean isOK() {
        return !hasError();
    }

}
