package org.riggvar.eventmenu;

public interface IEventMenuConnection {
    void setUrl(String url);

    String getUrl();

    String Get();

    void Post(String s);

    boolean hasError();

    String getError();
}
