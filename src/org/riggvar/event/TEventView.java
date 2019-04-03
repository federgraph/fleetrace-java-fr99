package org.riggvar.event;

import org.riggvar.bo.*;
import org.riggvar.col.*;
import org.riggvar.event.TEventBO;
import org.riggvar.event.TEventColGrid;
import org.riggvar.event.TEventColProp;
import org.riggvar.event.TEventColProps;
import org.riggvar.event.TEventNode;
import org.riggvar.event.TEventRowCollection;
import org.riggvar.event.TEventRowCollectionItem;

public class TEventView extends
        TColGridTableModel<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp> {
    private static final long serialVersionUID = 1L;

    public TEventView() {
        super();
        Init();
    }

    private void Init() {
        ColBO = TMain.BO.EventBO;
        Node = TMain.BO.EventNode;
        ColBO.setCurrentNode(Node);
        Node.setShowPoints(1); // 0

        ColGrid.setOnGetBaseNode(this); // this as IColGridGetBaseNode
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
    protected TEventColGrid newColGrid(IColGrid StringGrid) {
        return new TEventColGrid(StringGrid);
    }

}
