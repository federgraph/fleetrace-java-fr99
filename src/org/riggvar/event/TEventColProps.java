package org.riggvar.event;

import org.riggvar.col.TBaseColProps;

public class TEventColProps extends
        TBaseColProps<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp> {
    private static final long serialVersionUID = 1L;

    public TEventColProps() {
        super();
    }

    @Override
    protected TEventColProp NewItem() {
        TEventColProp cp = new TEventColProp();
        cp.Collection = this;
        return cp;
    }
}
