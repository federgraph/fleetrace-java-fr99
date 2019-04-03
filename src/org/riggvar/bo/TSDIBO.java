package org.riggvar.bo;

import org.riggvar.base.*;

public class TSDIBO extends TBO {
    public TSDIBO(TAdapterParams aParams) {
        super(aParams);
    }

    @Override
    public void LoadNew(String Data) {
        TMain.Controller.LoadNew(Data);
    }

    @Override
    public void Clear() {
        this.StammdatenNode.getBaseRowCollection().clear();
        super.Clear();
    }
}
