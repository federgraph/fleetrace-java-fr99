package org.riggvar.eventmenu;

public interface IEventData {
    void Load(String ed);

    String getText();

    String getInfo();

    String getError();

    boolean hasError();

    boolean isOK();
}
