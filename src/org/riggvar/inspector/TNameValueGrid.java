package org.riggvar.inspector;

import org.riggvar.col.*;
import org.riggvar.inspector.TNameValueBO;
import org.riggvar.inspector.TNameValueColGrid;
import org.riggvar.inspector.TNameValueColProp;
import org.riggvar.inspector.TNameValueColProps;
import org.riggvar.inspector.TNameValueNode;
import org.riggvar.inspector.TNameValueRowCollection;
import org.riggvar.inspector.TNameValueRowCollectionItem;

public class TNameValueGrid extends
        TColGridTableModel<TNameValueColGrid, TNameValueBO, TNameValueNode, TNameValueRowCollection, TNameValueRowCollectionItem, TNameValueColProps, TNameValueColProp> {
    private static final long serialVersionUID = 1L;
    protected String SortColName;

    private TNameValueColGrid webGrid;

    public TNameValueGrid() {
        super();
        SortColName = "col_Report";
        Init();
    }

    @Override
    protected TNameValueColGrid newColGrid(IColGrid StringGrid) {
        return new TNameValueColGrid(StringGrid);
    }

    private void Init() {
        ColBO = new TNameValueBO();
        Node = new TNameValueNode(ColBO, "NameValueGrid");
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

    TNameValueColGrid getWebGrid() {
        if (webGrid == null)
            CreateWebGrid();
        return webGrid;
    }

    private void CreateWebGrid() {
        webGrid = new TNameValueColGrid(new THashGridJava());
        webGrid.setColorSchema(TBaseGridColorSchema.colorMoneyGreen);

        webGrid.setOnGetBaseNode(this);

        webGrid.SetColBOReference(ColBO);
        ColBO.InitColsActiveLayout(webGrid, 0);

        webGrid.setUseHTML(true);
    }

    public String GetHTM() {
        getWebGrid().UpdateAll();
        return webGrid.toString();
    }

}
