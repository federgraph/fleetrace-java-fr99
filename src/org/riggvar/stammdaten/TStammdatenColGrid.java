package org.riggvar.stammdaten;

import org.riggvar.bo.TMain;
import org.riggvar.col.IColGrid;
import org.riggvar.col.TColGrid;

public class TStammdatenColGrid extends
        TColGrid<TStammdatenColGrid, TStammdatenBO, TStammdatenNode, TStammdatenRowCollection, TStammdatenRowCollectionItem, TStammdatenColProps, TStammdatenColProp> {

    public TStammdatenColGrid(IColGrid grid) {
        super(grid);
    }

    @Override
    protected TStammdatenColProps newPC() {
        return new TStammdatenColProps();
    }

    @Override
    protected TStammdatenBO getColBOInstance() {
        return TMain.BO.StammdatenBO;
    }

}
