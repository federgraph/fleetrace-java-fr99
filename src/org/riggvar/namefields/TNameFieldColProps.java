package org.riggvar.namefields;

import org.riggvar.col.TBaseColProps;

public class TNameFieldColProps extends
        TBaseColProps<TNameFieldColGrid, TNameFieldBO, TNameFieldNode, TNameFieldRowCollection, TNameFieldRowCollectionItem, TNameFieldColProps, TNameFieldColProp> {
    private static final long serialVersionUID = 1L;

    public TNameFieldColProps() {
        super();
    }

    @Override
    protected TNameFieldColProp NewItem() {
        TNameFieldColProp cp = new TNameFieldColProp();
        cp.Collection = this;
        return cp;
    }

}
