package org.riggvar.col;

import java.util.ArrayList;

public abstract class TBaseRowCollection<G extends TColGrid<G, B, N, C, I, PC, PI>, B extends TBaseColBO<G, B, N, C, I, PC, PI>, N extends TBaseNode<G, B, N, C, I, PC, PI>, C extends TBaseRowCollection<G, B, N, C, I, PC, PI>, I extends TBaseRowCollectionItem<G, B, N, C, I, PC, PI>, PC extends TBaseColProps<G, B, N, C, I, PC, PI>, PI extends TBaseColProp<G, B, N, C, I, PC, PI>>
        extends ArrayList<I> {
    private static final long serialVersionUID = 1L;

    public N Owner;

    public TBaseRowCollection() {
        super();
    }

    protected abstract I NewItem();

    public void Assign(I source) {
        // virtual
    }

    public I AddRow() {
        I cr = NewItem();
        add(cr);
        cr.BaseID = this.size();
        return cr;
    }

    public I InsertRow(int index) {
        I cr = NewItem();
        super.add(index, cr);
        return cr;
    }

    public void DeleteRow(int index) {
        super.remove(index);
    }

    public int IndexOfRow(Object row) {
        return super.indexOf(row);
    }

    public I getItem(int index) {
        if (index < 0 || index >= size())
            return null;
        else
            return super.get(index);
    }

    public void setItem(int index, I value) {
        if (index >= 0 && index < size()) {
            super.set(index, value);
        }
    }

    public N getBaseNode() {
        return Owner;
    }

    public void ClearList() {
        I cr;
        for (int i = 0; i < size(); i++) {
            cr = getItem(i);
            cr.ClearList();
        }
    }

    public void ClearResult() {
        I cr;
        for (int i = 0; i < size(); i++) {
            cr = getItem(i);
            cr.ClearResult();
        }
    }

    public I FindBase(int BaseId) {
        for (int i = 0; i < size(); i++) {
            if (BaseId == getItem(i).BaseID) {
                return getItem(i);
            }
        }
        return null;
    }

    public int FilteredCount() {
        return size();
    }

    public int getCount() {
        return size();
    }

    public I getBaseRowCollectionItem(int index) {
        return getItem(index);
    }

    public I AddBaseRowCollectionItem() {
        return AddRow();
    }

    public void Delete(int index) {
        DeleteRow(index);
    }

    public I Insert(int index) {
        return InsertRow(index);
    }

    public N ru() {
        return getBaseNode();
    }

}
