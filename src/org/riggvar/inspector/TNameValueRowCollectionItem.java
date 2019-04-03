package org.riggvar.inspector;

import org.riggvar.col.*;

public class TNameValueRowCollectionItem extends
        TBaseRowCollectionItem<TNameValueColGrid, TNameValueBO, TNameValueNode, TNameValueRowCollection, TNameValueRowCollectionItem, TNameValueColProps, TNameValueColProp>

{
    public String FieldName;
    public String FieldValue;
    public NameValueFieldType FieldType;
    public String Caption;
    public String Description;
    public String Category;

    public String FRequest;
    public boolean AddXmlHeader;
    public String FData;
    public boolean IsXml;
    public boolean Requesting;

    public int ID;

    public TNameValueRowCollectionItem() {
        super();
    }

    public void Assign(TNameValueRowCollectionItem source) {
        if (source != null) {
            TNameValueRowCollectionItem o = source;
            FieldName = o.FieldName;
            FieldValue = o.FieldValue;
            FieldType = o.FieldType;
            Caption = o.Caption;
            Description = o.Description;
            Category = o.Category;
        }
    }

}
