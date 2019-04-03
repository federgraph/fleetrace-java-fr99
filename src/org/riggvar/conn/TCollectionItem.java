package org.riggvar.conn;

public class TCollectionItem<C extends TCollection<C, I>, I extends TCollectionItem<C, I>> {
    private static int SID = -1;
    private int FID;

    public C Collection;

    public TCollectionItem() {
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
    }

    public void Delete() {
        Dispose();
        Collection.DeleteRow(getIndex());
    }

}
