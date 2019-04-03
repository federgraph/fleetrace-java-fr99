package org.riggvar.inspector;

import org.riggvar.col.*;

public class TNameValueRowCollection extends
        TBaseRowCollection<TNameValueColGrid, TNameValueBO, TNameValueNode, TNameValueRowCollection, TNameValueRowCollectionItem, TNameValueColProps, TNameValueColProp> {
    private static final long serialVersionUID = 1L;

    public TNameValueRowCollection() {
        super();
    }

    @Override
    protected TNameValueRowCollectionItem NewItem() {
        TNameValueRowCollectionItem o = new TNameValueRowCollectionItem();
        o.Collection = this;
        return o;
    }

    public TNameValueRowCollectionItem FindKey(int Bib) {
        for (int i = 0; i < getCount(); i++) {
            TNameValueRowCollectionItem o = getItem(i);
            if (o != null && o.BaseID == Bib) {
                return o;
            }
        }
        return null;
    }

}
