package org.riggvar.bo;

import org.riggvar.base.*;

public class TBOMsgList extends TBaseMsgList {
    public TBOMsgList() {
        super();
        KatID = LookupKatID.FR;
    }

    @Override
    protected TBaseMsg NewMsg() {
        return new TBOMsg();
    }

}
