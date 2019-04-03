package org.riggvar.inspector;

import org.riggvar.col.TBaseColProps;

public class TNameValueColProps extends
        TBaseColProps<TNameValueColGrid, TNameValueBO, TNameValueNode, TNameValueRowCollection, TNameValueRowCollectionItem, TNameValueColProps, TNameValueColProp> {
    private static final long serialVersionUID = 1L;

    public TNameValueColProps() {

    }

    @Override
    protected TNameValueColProp NewItem() {
        TNameValueColProp cp = new TNameValueColProp();
        cp.Collection = this;
        return cp;
    }
}
