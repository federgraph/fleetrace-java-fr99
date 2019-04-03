package org.riggvar.inspector;

import org.riggvar.base.*;
import org.riggvar.col.*;

public class TNameValueBO extends
        TBaseColBO<TNameValueColGrid, TNameValueBO, TNameValueNode, TNameValueRowCollection, TNameValueRowCollectionItem, TNameValueColProps, TNameValueColProp> {

    public TNameValueBO() {
        super();
    }

    @Override
    public void InitColsActiveLayout(TNameValueColGrid g, int aLayout) {
        TNameValueColProp cp;

        g.getColsActive().clear();
        g.AddColumn("col_BaseID");

        g.AddColumn("col_FieldType");
        // g.AddColumn("col_FieldName");
        g.AddColumn("col_Caption");

        cp = g.AddColumn("col_FieldValue");
        cp.setOnFinishEdit(new TNameValueDelegate(this, TNameValueDelegate.commandEditValue));
        cp.ReadOnly = false;

        g.AddColumn("col_Description");
        g.AddColumn("col_Category");
    }

    public String EditValue(TNameValueRowCollectionItem cr, String Value) {
        if (cr != null) {
            switch (cr.FieldType) {
            case FTInteger:
                Value = CheckInteger(cr.FieldValue, Value);
                break;
            case FTBoolean:
                Value = CheckBoolean(Value);
                break;
            case FTString:
                Value = CheckString(cr.FieldValue, Value);
                break;
            }
            cr.FieldValue = Value;
        }
        return Value;
    }

    private String CheckInteger(String OldValue, String Value) {
        int i;
        i = Utils.StrToIntDef(OldValue, 0);
        i = Utils.StrToIntDef(Value, i);
        return "" + i;
    }

    private String CheckBoolean(String Value) {
        boolean b = Utils.IsTrue(Value);
        return Utils.BoolStr(b);
    }

    private String CheckString(String OldValue, String Value) {
        if (Value.length() < 20)
            return Value;
        else
            return OldValue;

    }

}
