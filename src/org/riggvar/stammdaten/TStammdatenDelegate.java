package org.riggvar.stammdaten;

import org.riggvar.col.*;

public class TStammdatenDelegate implements
        IColGridGetText<TStammdatenColGrid, TStammdatenBO, TStammdatenNode, TStammdatenRowCollection, TStammdatenRowCollectionItem, TStammdatenColProps, TStammdatenColProp> {
    private int command = -1;
    private TStammdatenBO owner;

    public TStammdatenDelegate(TStammdatenBO aOwner, int aCommand) {
        owner = aOwner;
        command = aCommand;
    }

    public static final int commandEditSNR = 0;
    public static final int commandEditFN = 1;
    public static final int commandEditLN = 2;
    public static final int commandEditSN = 3;
    public static final int commandEditNOC = 4;
    public static final int commandEditGender = 5;
    public static final int commandEditPB = 6;
    public static final int commandEditNameColumn = 7;

    public String GetText(TStammdatenRowCollectionItem cr, String value) {
        switch (command) {
        case commandEditSNR:
            return owner.EditSNR(cr, value);
        case commandEditFN:
            return owner.editFN(cr, value);
        case commandEditLN:
            return owner.editLN(cr, value);
        case commandEditSN:
            return owner.editSN(cr, value);
        case commandEditNOC:
            return owner.editNC(cr, value);
        case commandEditGender:
            return owner.editGR(cr, value);
        case commandEditPB:
            return owner.editPB(cr, value);

        }
        return value;
    }

    public String GetText2(TStammdatenRowCollectionItem cr, String value, String colName) {
        switch (command) {
        // case commandEditRaceValue: return owner.EditRaceValue(cr, value, colName);
        case commandEditNameColumn:
            return owner.editNameColumn(cr, value, colName);
        }
        return value;
    }

    public String GetText3(TStammdatenRowCollectionItem cr, String value, int index) {
//		switch (command)
//		{
//			case commandEditOTime: return owner.EditOTime(cr, value, index);
//		}
        return value;
    }
}
