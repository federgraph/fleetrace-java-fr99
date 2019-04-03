package org.riggvar.namefields;

import org.riggvar.col.*;

public class TNameFieldDelegate implements
        IColGridGetText<TNameFieldColGrid, TNameFieldBO, TNameFieldNode, TNameFieldRowCollection, TNameFieldRowCollectionItem, TNameFieldColProps, TNameFieldColProp> {
    private int command = -1;
    private TNameFieldBO owner;

    public TNameFieldDelegate(TNameFieldBO aOwner, int aCommand) {
        owner = aOwner;
        command = aCommand;
    }

    public static final int commandEditCaption = 0;
    public static final int commandEditSwap = 1;
    public static final int commandEditMap = 2;

    public String GetText(TNameFieldRowCollectionItem cr, String value) {
        switch (command) {
        case commandEditCaption:
            return owner.EditCaption(cr, value);
        case commandEditSwap:
            return owner.EditSwap(cr, value);
        case commandEditMap:
            return owner.EditMap(cr, value);
        }
        return value;

    }

    public String GetText2(TNameFieldRowCollectionItem cr, String value, String colName) {
        return value;
    }

    public String GetText3(TNameFieldRowCollectionItem cr, String value, int index) {
        return value;
    }
}
