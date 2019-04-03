package org.riggvar.bo;

import org.riggvar.base.*;

public class TBOMsgFactory extends TMsgListFactory {
    @Override
    public TBaseMsgList CreateMsg() {
        return new TBOMsgList();
    }
}
