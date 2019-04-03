package org.riggvar.input;

import org.riggvar.base.*;
import org.riggvar.bo.TMain;

public class TMsgTree extends TBaseMsgTree {
    public TGender Division;

    public TMsgTree(String aNameID, int aActionID) {
        super(aNameID);
        TBaseToken.NewActionID = aActionID;
        Division = new TGender(this, TMain.BO.cTokenDivision);
    }
}
