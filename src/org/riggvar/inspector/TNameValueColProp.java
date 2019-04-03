package org.riggvar.inspector;

import org.riggvar.col.*;

public class TNameValueColProp extends
        TBaseColProp<TNameValueColGrid, TNameValueBO, TNameValueNode, TNameValueRowCollection, TNameValueRowCollectionItem, TNameValueColProps, TNameValueColProp> {
    public TNameValueColProp() {
        super();
    }

    @Override
    public void InitColsAvail() {
        TNameValueColProps ColsAvail = Collection;

        super.InitColsAvail();

        TNameValueColProp cp;

        // FieldName
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_FieldName");
        cp.Caption = "Name";
        cp.Width = 100;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.NumID = TNameValueColProp.NumID_FieldName;

        // FieldValue
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_FieldValue");
        cp.Caption = "Value";
        cp.Width = 80;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.NumID = TNameValueColProp.NumID_FieldValue;

        // FieldType
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_FieldType");
        cp.Caption = "Type";
        cp.Width = 40;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.NumID = TNameValueColProp.NumID_FieldType;

        // Caption
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_Caption");
        cp.Caption = "Caption";
        cp.Width = 120;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.NumID = TNameValueColProp.NumID_Caption;

        // Description
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_Description");
        cp.Caption = "Description";
        cp.Width = 260;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.NumID = TNameValueColProp.NumID_Description;

        // Category
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_Category");
        cp.Caption = "Category";
        cp.Width = 60;
        cp.Sortable = true;
        cp.Alignment = TColAlignment.taLeftJustify;
        cp.NumID = TNameValueColProp.NumID_Category;

    }

    @Override
    protected String GetTextDefault(TNameValueRowCollectionItem cr, String Value) {

        Value = super.GetTextDefault(cr, Value);

        if (NumID == NumID_FieldName) {
            Value = cr.FieldName;
        } else if (NumID == NumID_FieldValue) {
            Value = cr.FieldValue;
        } else if (NumID == NumID_FieldType) {
            Value = getNameValueFieldTypeString(cr.FieldType);
        } else if (NumID == NumID_Caption) {
            Value = cr.Caption;
        } else if (NumID == NumID_Description) {
            Value = cr.Description;
        } else if (NumID == NumID_Category) {
            Value = cr.Category;
        }

        return Value;
    }

    public String getNameValueFieldTypeString(NameValueFieldType value) {
        switch (value) {
        case FTInteger:
            return "int";
        case FTBoolean:
            return "bool";
        case FTString:
            return "string";
        }
        return "";
    }

    public static final int NumID_FieldName = 1;
    public static final int NumID_FieldValue = 2;
    public static final int NumID_FieldType = 3;
    public static final int NumID_Caption = 4;
    public static final int NumID_Description = 5;
    public static final int NumID_Category = 6;

}
