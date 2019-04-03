package org.riggvar.base;

public class TBaseFormAdapter {

    public void EditCacheProps(Object controller) {
    }

    public void EditConnectionProps(ConfigSection section) {
    }

    public void EditBridgeProps(Object controller, TBaseIniImage ini) {
    }

    public void EditSwitchProps(Object controller, TBaseIniImage ini) {
    }

    public int EditProviderID(int CurrentProviderID, TBaseIniImage ini) {
        return 0;
    }

    public boolean EditDBEvent() {
        return false;
    }

    public void ShowMessageBox(String s) {
    }

    public String ShowInputBox(String caption, String prompt, String suggestion) {
        return suggestion;
    }

    public String SelectName(String caption, String prompt, TStrings SLDocsAvail) {
        return "";
    }

    public void ShowError(String msg) {
    }

}
