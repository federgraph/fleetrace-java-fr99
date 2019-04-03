package org.riggvar.eventmenu;

public interface IEventMenu {
    void Load(String data);

    String getComboCaption();

    int getCount();

    String GetCaption(int i);

    String GetImageUrl(int i);

    String GetDataUrl(int i);

    boolean isMock();
}
