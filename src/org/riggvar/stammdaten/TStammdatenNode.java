package org.riggvar.stammdaten;

import org.riggvar.col.*;

public class TStammdatenNode extends
        TBaseNode<TStammdatenColGrid, TStammdatenBO, TStammdatenNode, TStammdatenRowCollection, TStammdatenRowCollectionItem, TStammdatenColProps, TStammdatenColProp> {
    public TStammdatenBO BO;

    public TStammdatenNode(TStammdatenBO cbo, String aNameID) {
        super();
        BO = cbo;
        NameID = aNameID;
        this.setBaseRowCollection(new TStammdatenRowCollection());
        getBaseRowCollection().Owner = this;
    }

    public void Load() {
        TStammdatenRowCollectionItem o;

        TStammdatenRowCollection cl = this.getBaseRowCollection();
        cl.clear();

        o = cl.AddRow();
        o.SNR = 1000;
        o.BaseID = 1;

        o = cl.AddRow();
        o.SNR = 1001;
        o.BaseID = 2;

        o = cl.AddRow();
        o.SNR = 1002;
        o.BaseID = 3;
    }

    public void Save() {
    }

    public void Init(int RowCount) {
        TStammdatenRowCollectionItem o;
        TStammdatenRowCollection cl = this.getBaseRowCollection();
        cl.clear();

        for (int i = 0; i < RowCount; i++) {
            o = cl.AddRow();
            o.BaseID = i + 1;
            o.SNR = 999 + i + 1;
        }
    }

}
