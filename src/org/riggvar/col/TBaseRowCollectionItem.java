package org.riggvar.col;

import java.awt.*;

public class TBaseRowCollectionItem<G extends TColGrid<G, B, N, C, I, PC, PI>, B extends TBaseColBO<G, B, N, C, I, PC, PI>, N extends TBaseNode<G, B, N, C, I, PC, PI>, C extends TBaseRowCollection<G, B, N, C, I, PC, PI>, I extends TBaseRowCollectionItem<G, B, N, C, I, PC, PI>, PC extends TBaseColProps<G, B, N, C, I, PC, PI>, PI extends TBaseColProp<G, B, N, C, I, PC, PI>> {
    private static int SID = -1;
    private int FID;
    public C Collection;

    public int BaseID;

    public TBaseRowCollectionItem() {
        SID++;
        FID = SID;
    }

    public int getID() {
        return FID;
    }

    public int getIndex() {
        return Collection.IndexOfRow(this);
    }

    /**
     * Dispose of additional resources added in subclass.
     */
    protected void Dispose() {
        // virtual
    }

    public void Delete() {
        Dispose();
        Collection.DeleteRow(getIndex());
    }

    public void ClearList() {
    }

    public void ClearResult() {
    }

    public boolean IsInFilter() {
        return true;
    }

    public Color ColumnToColorDef(PI cp, Color aColor) {
        return aColor;
    }

    public N GetBaseNode() {
        return Collection.getBaseNode();
    }

    public N ru() {
        return Collection.getBaseNode();
    }

    public void setModified() {
        N rd = GetBaseNode();
        if (rd != null) {
            rd.setModified(true);
        }
    }

}
