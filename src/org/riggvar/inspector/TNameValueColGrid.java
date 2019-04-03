package org.riggvar.inspector;

import org.riggvar.col.IColGrid;
import org.riggvar.col.TColGrid;

public class TNameValueColGrid extends
        TColGrid<TNameValueColGrid, TNameValueBO, TNameValueNode, TNameValueRowCollection, TNameValueRowCollectionItem, TNameValueColProps, TNameValueColProp> {

    public TNameValueColGrid(IColGrid grid) {
        super(grid);
    }

    @Override
    protected TNameValueColProps newPC() {
        return new TNameValueColProps();
    }

    @Override
    protected TNameValueBO getColBOInstance() {
        return new TNameValueBO();
    }

}
