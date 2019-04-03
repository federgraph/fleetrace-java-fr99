package org.riggvar.stammdaten;

import org.riggvar.col.TBaseColProps;

public class TStammdatenColProps extends
        TBaseColProps<TStammdatenColGrid, TStammdatenBO, TStammdatenNode, TStammdatenRowCollection, TStammdatenRowCollectionItem, TStammdatenColProps, TStammdatenColProp> {
    private static final long serialVersionUID = 1L;

    @Override
    protected TStammdatenColProp NewItem() {
        TStammdatenColProp cp = new TStammdatenColProp();
        cp.Collection = this;
        return cp;
    }

}
