package org.riggvar.event;

import org.riggvar.col.IColGrid;
import org.riggvar.col.TColGrid;

public class TEventColGrid extends
        TColGrid<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp> {

    public TEventColGrid(IColGrid grid) {
        super(grid);
    }

    @Override
    protected TEventColProps newPC() {
        return new TEventColProps();
    }

    @Override
    protected TEventBO getColBOInstance() {
        return new TEventBO();
    }

}
