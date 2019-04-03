package org.riggvar.namefields;

import org.riggvar.bo.TColPanel;
import org.riggvar.col.TBaseGridColorSchema;
import javax.swing.JScrollPane;
import java.beans.PropertyChangeListener;
import org.riggvar.namefields.TNameFieldBO;
import org.riggvar.namefields.TNameFieldColGrid;
import org.riggvar.namefields.TNameFieldColProp;
import org.riggvar.namefields.TNameFieldColProps;
import org.riggvar.namefields.TNameFieldNode;
import org.riggvar.namefields.TNameFieldRowCollection;
import org.riggvar.namefields.TNameFieldRowCollectionItem;
import org.riggvar.stammdaten.*;

public class TNameFieldPanel extends
        TColPanel<TNameFieldColGrid, TNameFieldBO, TNameFieldNode, TNameFieldRowCollection, TNameFieldRowCollectionItem, TNameFieldColProps, TNameFieldColProp>
        implements PropertyChangeListener {
    private static final long serialVersionUID = 1L;
    private TNameFieldGrid grid;

    public TNameFieldPanel() {
        super();
        InitPanel();
    }

    @Override
    public void InitPanel() {
        try {
            grid = new TNameFieldGrid();
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

    public void LoadModel(TStammdatenNode sn) {
        grid.Node.getBaseRowCollection().clear();
        grid.Node.Init(sn);
        grid.ColGrid.UpdateAll();
    }

    public void ApplySchemaCode(int schemaCode) {
        TNameFieldRowCollection cl;
        TNameFieldRowCollectionItem cr;

        cl = grid.Node.getBaseRowCollection();
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            cr.Caption = FieldNames.GetStandardFieldCaption(i + 1, schemaCode);
        }
        ColGrid.UpdateAll();
    }

    public void UpdateFieldCaptions(TStammdatenRowCollection sl) {
        TNameFieldRowCollection cl;
        cl = grid.Node.getBaseRowCollection();
        if (cl.getCount() != sl.getFieldCount())
            sl.setFieldCount(cl.getCount());
        sl.setFieldCaptions(cl.GetFieldCaptions());
    }

    public void SwapNameFields(TStammdatenRowCollection sl) {
        TNameFieldRowCollection cl;
        TNameFieldRowCollectionItem cr;
        int f1 = 0;
        int f2 = 0;
        boolean ambiguous = false;

        cl = grid.Node.getBaseRowCollection();
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            if (cr.Swap == 1) {
                if (f1 > 0)
                    ambiguous = true;
                f1 = cr.BaseID;
            }
            if (cr.Swap == 2) {
                if (f2 > 0)
                    ambiguous = true;
                f2 = cr.BaseID;
            }
        }
        if (!ambiguous && f1 > 0 && f2 > 0) {
            sl.Swap(f1, f2);
        }
    }

    public void AddRow() {
        TNameFieldRowCollection cl;
        cl = grid.Node.getBaseRowCollection();
        if (cl.getCount() < 32) {
            cl.AddRow();
            grid.ColGrid.UpdateAll();
        }
    }

    public void RemoveRow() {
        TNameFieldRowCollection cl;
        cl = grid.Node.getBaseRowCollection();
        if (cl.getCount() > 6) {
            cl.Delete(cl.getCount() - 1);
            grid.ColGrid.UpdateAll();
        }
    }

}
