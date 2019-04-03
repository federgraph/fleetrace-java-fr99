package org.riggvar.namefields;

import org.riggvar.col.*;

public class TNameFieldRowCollectionItem extends
        TBaseRowCollectionItem<TNameFieldColGrid, TNameFieldBO, TNameFieldNode, TNameFieldRowCollection, TNameFieldRowCollectionItem, TNameFieldColProps, TNameFieldColProp> {
    public String FieldName;
    public String Caption;
    public int Swap;
    public int Map;

    public TNameFieldRowCollectionItem() {
        super();
    }

    public void Assign(TNameFieldRowCollectionItem source) {
        if (source != null) {
            FieldName = source.FieldName;
            Caption = source.Caption;
            Swap = source.Swap;
            Map = source.Map;
        }
    }

}
