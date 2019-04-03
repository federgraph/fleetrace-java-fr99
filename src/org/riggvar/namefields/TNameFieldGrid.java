package org.riggvar.namefields;

import org.riggvar.col.*;
import org.riggvar.namefields.TNameFieldBO;
import org.riggvar.namefields.TNameFieldColGrid;
import org.riggvar.namefields.TNameFieldColProp;
import org.riggvar.namefields.TNameFieldColProps;
import org.riggvar.namefields.TNameFieldNode;
import org.riggvar.namefields.TNameFieldRowCollection;
import org.riggvar.namefields.TNameFieldRowCollectionItem;

public class TNameFieldGrid extends
        TColGridTableModel<TNameFieldColGrid, TNameFieldBO, TNameFieldNode, TNameFieldRowCollection, TNameFieldRowCollectionItem, TNameFieldColProps, TNameFieldColProp> {
    private static final long serialVersionUID = 1L;
    protected String SortColName;

    public TNameFieldGrid() {
        super();
        // SortColName = "col_FieldName";
        Init();
    }

    private void Init() {
        ColBO = new TNameFieldBO();
        Node = new TNameFieldNode(ColBO, "NameFieldGrid");
        ColBO.setCurrentNode(Node);

        ColGrid.setOnGetBaseNode(this);

        ColGrid.SetColBOReference(ColBO);
        ColGrid.setColPropClass();
        // ColGrid.getColsAvail().Init();
        ColBO.InitColsActive(ColGrid);

        ColGrid.setAlwaysShowCurrent(true);

        ColGrid.setUseHTML(false);
        ColGrid.setAutoMark(true);
        ColGrid.setExcelStyle(true);
        ColGrid.setColorPaint(true);

        ColGrid.UpdateAll();
    }

    public boolean getIsOutputSorted() {
        return ColGrid.getColsActive().getSortColIndex() > 0;
    }

    @Override
    protected TNameFieldColGrid newColGrid(IColGrid StringGrid) {
        return new TNameFieldColGrid(StringGrid);
    }

}
