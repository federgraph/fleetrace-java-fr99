package org.riggvar.event;

import org.riggvar.col.*;

public class TEventDelegate implements
        IColGridGetText<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp> {
    private int command = -1;
    private TEventBO owner;

    public TEventDelegate(TEventBO aOwner, int aCommand) {
        owner = aOwner;
        command = aCommand;
    }

    public static final int commandEditBib = 0;
    public static final int commandEditSNR = 1;
    public static final int commandEditRaceValue = 2;
    public static final int commandEditOTime = 3;
    public static final int commandEditNOC = 4;

    public String GetText(TEventRowCollectionItem cr, String value) {
        switch (command) {
        case commandEditBib:
            return owner.EditBib(cr, value);
        case commandEditSNR:
            return owner.EditSNR(cr, value);
        case commandEditNOC:
            return owner.EditNOC(cr, value);
        }
        return value;
    }

    public String GetText2(TEventRowCollectionItem cr, String value, String colName) {
        switch (command) {
        case commandEditRaceValue:
            return owner.EditRaceValue(cr, value, colName);
        }
        return value;
    }

    public String GetText3(TEventRowCollectionItem cr, String value, int index) {
        switch (command) {
        case commandEditOTime:
            return owner.EditOTime(cr, value, index);
        }
        return value;
    }

}
