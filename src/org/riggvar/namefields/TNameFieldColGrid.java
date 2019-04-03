package org.riggvar.namefields;

import org.riggvar.col.IColGrid;
import org.riggvar.col.TColGrid;

public class TNameFieldColGrid extends
        TColGrid<TNameFieldColGrid, TNameFieldBO, TNameFieldNode, TNameFieldRowCollection, TNameFieldRowCollectionItem, TNameFieldColProps, TNameFieldColProp> {

    public TNameFieldColGrid(IColGrid grid) {
        super(grid);
    }

    @Override
    protected TNameFieldColProps newPC() {
        return new TNameFieldColProps();
    }

    @Override
    protected TNameFieldBO getColBOInstance() {
        return new TNameFieldBO();
    }

}
