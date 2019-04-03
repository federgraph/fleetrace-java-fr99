package org.riggvar.inspector;

import org.riggvar.inspector.IInspectable;
import org.riggvar.inspector.IInspector;
import org.riggvar.inspector.TNameValueBO;
import org.riggvar.inspector.TNameValueColGrid;
import org.riggvar.inspector.TNameValueColProp;
import org.riggvar.inspector.TNameValueColProps;
import org.riggvar.inspector.TNameValueNode;
import org.riggvar.inspector.TNameValueRowCollection;
import org.riggvar.inspector.TNameValueRowCollectionItem;
import org.riggvar.bo.TColPanel;
import org.riggvar.col.TBaseGridColorSchema;
import javax.swing.JScrollPane;
import java.beans.PropertyChangeListener;

public class TNameValuePanel extends
        TColPanel<TNameValueColGrid, TNameValueBO, TNameValueNode, TNameValueRowCollection, TNameValueRowCollectionItem, TNameValueColProps, TNameValueColProp>
        implements PropertyChangeListener, IInspector {
    private static final long serialVersionUID = 1L;

    private TNameValueGrid grid;
    private IInspectable model;

    public TNameValuePanel() {
        super();
        InitPanel();
    }

    @Override
    public void InitPanel() {
        try {
            grid = new TNameValueGrid();
            fTableModel = grid;
            ColGrid = fTableModel.ColGrid;
            ColGrid.setColorSchema(TBaseGridColorSchema.colorBlue);
            InitTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InitForm(JScrollPane scrollPane) {
        scrollPane.getViewport().add(jTable, null);
    }

    @Override
    public void DisposePanel() {
    }

    @Override
    public void UpdateView() {
        ColGrid.UpdateAll();
        grid.ColBO.getCurrentNode().setModified(false);
    }

    public void loadModel() {
        grid.Node.getBaseRowCollection().clear();
        if (model != null)
            model.inspectorOnLoad(grid.Node.getBaseRowCollection());
        grid.ColGrid.UpdateAll();
    }

    public void saveModel() {
        if (model != null)
            model.inspectorOnSave(grid.Node.getBaseRowCollection());
    }

    public void setInspectable(IInspectable model) {
        this.model = model;
        InitPanel();
        loadModel();
    }

}
