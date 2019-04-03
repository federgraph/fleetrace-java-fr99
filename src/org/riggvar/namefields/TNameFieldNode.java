package org.riggvar.namefields;

import org.riggvar.stammdaten.*;
import org.riggvar.col.*;

public class TNameFieldNode extends
        TBaseNode<TNameFieldColGrid, TNameFieldBO, TNameFieldNode, TNameFieldRowCollection, TNameFieldRowCollectionItem, TNameFieldColProps, TNameFieldColProp> {

    public TNameFieldNode(TNameFieldBO cbo, String aNameID) {
        super();
        BO = cbo;
        NameID = aNameID;
        setBaseRowCollection(new TNameFieldRowCollection());
        getBaseRowCollection().Owner = this;
    }

    public void Init(TStammdatenNode sdn) {
        getBaseRowCollection().clear();
        TStammdatenRowCollection cl = sdn.getBaseRowCollection();
        TNameFieldRowCollectionItem o;
        for (int i = 1; i <= cl.getFieldCount(); i++) {
            o = getBaseRowCollection().AddRow();
            // o.FieldName = "N" + i.ToString(); //automatic see Add
            o.Caption = cl.getFieldCaption(i);
            // o.Map = i; //automatic see Add
        }
    }

}
