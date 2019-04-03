package org.riggvar.base;

import org.riggvar.bo.TMain;

public class TAdapterOutput implements IOutput {
    public TAdapterOutput() {
    }

    public String getMsg(String sRequest) {
        // if the application (BO) is connected
        if (TMain.AdapterBO != null && TMain.AdapterBO.AdapterOutputConnection != null) {
            return TMain.AdapterBO.AdapterOutputConnection.HandleMsg(sRequest);
        } else {
            return "not connected";
        }
    }

    public String getAll(TStrings OutputRequestList) {
        return "";
    }
}
