package org.riggvar.base;

import org.riggvar.bo.TMain;

public class TDBStringList extends TStringList {
    private TRedirector Redirector() {
        return TMain.Redirector;
    }

    @Override
    public void LoadFromFile(String AFileName) {
        if (Redirector().UseDB())
            Redirector().DBLoadFromFile(AFileName, this);
        else
            super.LoadFromFile(AFileName);
    }

    @Override
    public void SaveToFile(String AFileName) {
        if (Redirector().UseDB())
            Redirector().DBSaveToFile(AFileName, this);
        else
            super.SaveToFile(AFileName);
    }

}
