package org.riggvar.base;

public interface IWatchGUI {
    void Show();

    void Hide();

    boolean IsNew();

    boolean IsVisible();

    void UpdateFormCaption(String Titel, String EventName);

    void InitLabel(int LabelID, String Caption);

    void UpdateValue(int LabelID, String Content);
}
