package org.riggvar.namefields;

import org.riggvar.base.*;
import org.riggvar.col.*;

public class TNameFieldBO extends
        TBaseColBO<TNameFieldColGrid, TNameFieldBO, TNameFieldNode, TNameFieldRowCollection, TNameFieldRowCollectionItem, TNameFieldColProps, TNameFieldColProp> {

    public TNameFieldBO() {
        super();
    }

    @Override
    public void InitColsActiveLayout(TNameFieldColGrid g, int aLayout) {
        TNameFieldColProp cp;

        g.getColsActive().clear();
        g.AddColumn("col_BaseID");
        g.AddColumn("col_FieldName");

        cp = g.AddColumn("col_Caption");
        cp.setOnFinishEdit(new TNameFieldDelegate(this, TNameFieldDelegate.commandEditCaption));
        cp.ReadOnly = false;

        cp = g.AddColumn("col_Swap");
        cp.setOnFinishEdit(new TNameFieldDelegate(this, TNameFieldDelegate.commandEditSwap));
        cp.ReadOnly = false;

        cp = g.AddColumn("col_Map");
        cp.setOnFinishEdit(new TNameFieldDelegate(this, TNameFieldDelegate.commandEditMap));
        cp.ReadOnly = false;
    }

    public String EditCaption(TNameFieldRowCollectionItem cr, String Value) {
        if ((cr != null)) {
            cr.Caption = Value;
        }
        return Value;
    }

    public String EditSwap(TNameFieldRowCollectionItem cr, String Value) {
        if (cr != null) {
            cr.Swap = Utils.StrToIntDef(Value, cr.Swap);

            int t = Utils.StrToIntDef(Value, -1);
            if (t >= 0 && t <= cr.ru().getBaseRowCollection().size())
                cr.Swap = Utils.StrToIntDef(Value, cr.Swap);
            Value = "" + cr.Swap;
        }
        return Value;
    }

    public String EditMap(TNameFieldRowCollectionItem cr, String Value) {
        if (cr != null) {
            int t = Utils.StrToIntDef(Value, -1);
            if (t == 0)
                cr.Map = 0;
            if (t > 0 && t <= NameFieldMap.JS_NameFieldMax) {
                cr.Map = t;
                NameFieldMap.Instance().setFieldIndex(t, cr.BaseID);
            }
            Value = NameFieldMap.Instance().FieldName(cr.Map);
        }
        return Value;
    }

}
