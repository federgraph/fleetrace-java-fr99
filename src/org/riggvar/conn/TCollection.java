package org.riggvar.conn;

import java.util.ArrayList;

public abstract class TCollection<C extends TCollection<C, I>, I extends TCollectionItem<C, I>> extends ArrayList<I> {
    private static final long serialVersionUID = 1L;

    public TCollection() {
        super();
    }

    protected abstract I NewItem();

//	private I NewItem()
//	{
//		I o = new I();
//		o.Collection = this;
//		return o;
//	}

    public I AddRow() {
        I cr = NewItem();
        add(cr);
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
        if (index >= 0 && index < size())
            super.set(index, value);
    }

}
