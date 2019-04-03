package org.riggvar.namefields;

import org.riggvar.base.*;
import org.riggvar.col.*;

public class TNameFieldColProp extends
        TBaseColProp<TNameFieldColGrid, TNameFieldBO, TNameFieldNode, TNameFieldRowCollection, TNameFieldRowCollectionItem, TNameFieldColProps, TNameFieldColProp> {
    public TNameFieldColProp() {
        super();
    }

    @Override
    public void InitColsAvail() {
        TNameFieldColProps ColsAvail = Collection;

        super.InitColsAvail();

        TNameFieldColProp cp;

        // FieldName
        cp = ColsAvail.AddRow();
        cp.setNameID("col_FieldName");
        cp.Caption = "Name";
        cp.Width = 50;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.NumID = TNameFieldColProp.NumID_FieldName;

        // Caption
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_Caption");
        cp.Caption = "Caption";
        cp.Width = 100;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.NumID = TNameFieldColProp.NumID_Caption;

        // Swap
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_Swap");
        cp.Caption = "Swap";
        cp.Width = 35;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taRightJustify;
        cp.NumID = TNameFieldColProp.NumID_Swap;

        // Map
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_Map");
        cp.Caption = "JS Field Map";
        cp.Width = 100;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.NumID = TNameFieldColProp.NumID_Map;

    }

    @Override
    protected String GetTextDefault(TNameFieldRowCollectionItem cr, String Value) {
        Value = super.GetTextDefault(cr, Value);

        if (NumID == NumID_FieldName) {
            Value = cr.FieldName;
        } else if (NumID == NumID_Caption) {
            Value = cr.Caption;
        } else if (NumID == NumID_Swap) {
            Value = Utils.IntToStr(cr.Swap);
        } else if (NumID == NumID_Map) {
            Value = NameFieldMap.Instance().FieldName(cr.Map);
        }

        return Value;
    }

    public static final int NumID_FieldName = 1;
    public static final int NumID_Caption = 2;
    public static final int NumID_Swap = 3;
    public static final int NumID_Map = 4;

}
