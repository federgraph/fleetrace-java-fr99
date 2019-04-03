package org.riggvar.namefields;

import org.riggvar.base.*;
import org.riggvar.col.*;

public class TNameFieldRowCollection extends
        TBaseRowCollection<TNameFieldColGrid, TNameFieldBO, TNameFieldNode, TNameFieldRowCollection, TNameFieldRowCollectionItem, TNameFieldColProps, TNameFieldColProp> {
    private static final long serialVersionUID = 1L;

    public TNameFieldRowCollection() {
        super();
    }

    @Override
    protected TNameFieldRowCollectionItem NewItem() {
        TNameFieldRowCollectionItem o = new TNameFieldRowCollectionItem();
        o.Collection = this;
        return o;
    }

    @Override
    public TNameFieldRowCollectionItem AddRow() {
        TNameFieldRowCollectionItem cr = super.AddRow();
        cr.FieldName = "N" + getCount();
        cr.Caption = cr.FieldName;
        cr.Map = cr.BaseID;
        return cr;
    }

    public TNameFieldRowCollectionItem FindKey(int Bib) {
        for (int i = 0; i < getCount(); i++) {
            TNameFieldRowCollectionItem o = get(i);
            if (o != null && o.BaseID == Bib) {
                return o;
            }
        }
        return null;
    }

    public String GetFieldCaptions() {
        TStringList SL = new TStringList();
        TNameFieldRowCollectionItem cr;
        for (int i = 0; i < getCount(); i++) {
            cr = this.get(i);
            SL.Add(cr.Caption);
        }
        return "SNR," + SL.getCommaText();
    }

}
