package org.riggvar.inspector;

import org.riggvar.col.*;

public class TNameValueDelegate implements
        IColGridGetText<TNameValueColGrid, TNameValueBO, TNameValueNode, TNameValueRowCollection, TNameValueRowCollectionItem, TNameValueColProps, TNameValueColProp> {
    private int command = -1;
    private TNameValueBO owner;

    public TNameValueDelegate(TNameValueBO aOwner, int aCommand) {
        owner = aOwner;
        command = aCommand;
    }

    public static final int commandEditValue = 0;

    public String GetText(TNameValueRowCollectionItem cr, String value) {
        switch (command) {
        case commandEditValue:
            return owner.EditValue(cr, value);
        }
        return value;

    }

    public String GetText2(TNameValueRowCollectionItem cr, String value, String colName) {
        return value;
    }

    public String GetText3(TNameValueRowCollectionItem cr, String value, int index) {
        return value;
    }
}
