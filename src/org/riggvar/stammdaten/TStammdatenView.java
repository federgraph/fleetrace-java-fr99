package org.riggvar.stammdaten;

import org.riggvar.bo.*;
import org.riggvar.col.*;
import org.riggvar.stammdaten.TStammdatenBO;
import org.riggvar.stammdaten.TStammdatenColGrid;
import org.riggvar.stammdaten.TStammdatenColProp;
import org.riggvar.stammdaten.TStammdatenColProps;
import org.riggvar.stammdaten.TStammdatenNode;
import org.riggvar.stammdaten.TStammdatenRowCollection;
import org.riggvar.stammdaten.TStammdatenRowCollectionItem;

public class TStammdatenView extends
        TColGridTableModel<TStammdatenColGrid, TStammdatenBO, TStammdatenNode, TStammdatenRowCollection, TStammdatenRowCollectionItem, TStammdatenColProps, TStammdatenColProp> {
    private static final long serialVersionUID = 1L;

    public TStammdatenView() {
        super(); // StringGrid, ColGrid
        Init();
    }

    private void Init() {
        ColBO = TMain.BO.StammdatenBO;
        Node = TMain.BO.StammdatenNode;
        ColBO.CurrentNode = Node;

        ColGrid.setOnGetBaseNode(this); // this is a IColGridGetBaseNode

        ColGrid.SetColBOReference(ColBO);
        ColGrid.setColPropClass();
        // ColGrid.getColsAvail().Init();
        ColBO.InitColsActive(ColGrid);

        ColGrid.setAlwaysShowCurrent(true);

        ColGrid.setUseHTML(false); // true //initialize TCellProp
        ColGrid.setAutoMark(true);
        ColGrid.setColorPaint(true);

        ColGrid.UpdateAll();

        // RggGrid.OnBaseSelectCell = RggGridSelectCell;
    }

    @Override
    protected TStammdatenColGrid newColGrid(IColGrid StringGrid) {
        return new TStammdatenColGrid(StringGrid);
    }

}
