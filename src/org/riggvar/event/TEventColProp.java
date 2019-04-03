package org.riggvar.event;

import org.riggvar.col.*;
import org.riggvar.base.*;
import org.riggvar.bo.*;

public class TEventColProp extends
        TBaseColProp<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp>
        implements
        IColGridGetText<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp>

{
    public TEventColProp() {
        super();
    }

    public int GetRaceCount() {
        if (TMain.BO != null) {
            return TMain.BO.BOParams.RaceCount;
        } else {
            return -1;
        }
    }

    public String GetText(TEventRowCollectionItem cr, String value) {
        return GetSortKeyGPosR(cr, value);
    }

    public String GetText2(TEventRowCollectionItem cr, String value, String colName) {
        return GetSortKeyPoints(cr, value, colName);
    }

    public String GetText3(TEventRowCollectionItem cr, String value, int index) {
        return GetSortKeyRace(cr, value, "");
    }

    public String GetSortKeyRace(TEventRowCollectionItem cr, String Value, String ColName) {
        if (cr != null) {
            int i = Utils.StrToIntDef(Utils.Copy(ColName, 6, ColName.length()), -1);
            if (i > 0) // && (i <= RaceCount)
            {
                TEventRaceEntry ere = cr.Race[i];
                if (ere.OTime > 0) {
                    Value = Utils.IntToStr(ere.OTime + ere.Fleet * 2000);
                } else {
                    Value = Utils.IntToStr(999 + cr.BaseID + ere.Fleet * 2000);
                }
            }
        }
        return Value;
    }

    public String GetSortKeyPoints(TEventRowCollectionItem cr, String Value, String ColName) {
        if (cr != null) {
            if (cr.ru().getShowPoints() == TEventNode.Layout_Points) {
                int i = Utils.StrToIntDef(Utils.Copy(ColName, 6, ColName.length()), -1);
                if (i > 0) { // && (i <= RaceCount)
                    if (cr.Race[i].CTime > 0) {
                        return Utils.IntToStr(cr.Race[i].CTime);
                    } else {
                        return Utils.IntToStr(99 + cr.BaseID);
                    }
                }
            } else {
                return GetSortKeyRace(cr, Value, ColName);
            }
        }
        return "";
    }

    public String GetSortKeyGPosR(TEventRowCollectionItem cr, String Value) {
        if (cr != null) {
            return Utils.IntToStr(cr.GPosR());
        }
        return "";
    }

    @Override
    public void InitColsAvail() {
        TEventColProps ColsAvail = Collection;
        ColsAvail.UseCustomColCaptions = true;
        TEventColProp cp;

        // SNR
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_SNR");
        cp.Caption = "SNR";
        cp.Width = 40;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taRightJustify);
        cp.NumID = TEventColProp.NumID_SNR;

        // Bib
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_Bib");
        cp.Caption = "Bib";
        cp.Width = 40;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taRightJustify);
        cp.NumID = TEventColProp.NumID_Bib;

        // FN
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_FN");
        cp.Caption = "FN";
        cp.Width = 80;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taLeftJustify);
        cp.setColType(TColType.colTypeString);
        cp.NumID = TEventColProp.NumID_NF1;

        // LN
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_LN");
        cp.Caption = "LN";
        cp.Width = 80;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taLeftJustify);
        cp.setColType(TColType.colTypeString);
        cp.NumID = TEventColProp.NumID_NF2;

        // SN
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_SN");
        cp.Caption = "SN";
        cp.Width = 80;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taLeftJustify);
        cp.setColType(TColType.colTypeString);
        cp.NumID = TEventColProp.NumID_NF3;

        // NC
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_NC");
        cp.Caption = "NC";
        cp.Width = 40;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taLeftJustify);
        cp.setColType(TColType.colTypeString);
        cp.NumID = TEventColProp.NumID_NF4;

        // GR
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_GR");
        cp.Caption = "GR";
        cp.Width = 80;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taLeftJustify);
        cp.setColType(TColType.colTypeString);
        cp.NumID = TEventColProp.NumID_NF5;

        // PB
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_PB");
        cp.Caption = "PB";
        cp.Width = 80;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taLeftJustify);
        cp.setColType(TColType.colTypeString);
        cp.NumID = TEventColProp.NumID_NF6;

        // DN
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_DN");
        cp.Caption = "DN";
        cp.Width = 70;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taLeftJustify);
        cp.setColType(TColType.colTypeString);
        cp.NumID = TEventColProp.NumID_DN;

        // Race[0] not shown, only Race[1]..Race[RCount-1]
        // e.g. Race[1]..Race[RaceCount]
        int rc = GetRaceCount();
        for (int i = 1; i <= rc; i++) {
            // Ri
            cp = ColsAvail.AddBaseColProp();
            cp.setNameID("col_R" + i);
            cp.Caption = "R" + i;
            cp.Width = 60;
            cp.Sortable = true;
            cp.setAlignment(TColAlignment.taRightJustify);
            cp.setColType(TColType.colTypeRank);
            // OnGetSortKey2 = new TBaseGetTextEvent2(GetSortKeyPoints);
            cp.setOnGetSortKey2(this); // IColGridGetText;
            cp.NumID = NumID_Race(i); // 10000 + i * 1000 + 1;
        }

        // GPoints
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_GPoints");
        cp.Caption = "GPoints";
        cp.Width = 50;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taRightJustify);
        cp.setColType(TColType.colTypeRank);
        // cp.OnGetSortKey2 = new TBaseGetTextEvent2(GetSortKeyGPosR);
        cp.setOnGetSortKey(this); // IColGridGetText;
        cp.NumID = TEventColProp.NumID_GPoints;

        // GRank
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_GRank");
        cp.Caption = "GRank";
        cp.Width = 50;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taRightJustify);
        cp.setColType(TColType.colTypeRank);
        cp.NumID = TEventColProp.NumID_GRank;

        // GPosR
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_GPosR");
        cp.Caption = "GPosR";
        cp.Width = 50;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taRightJustify);
        cp.setColType(TColType.colTypeRank);
        cp.NumID = TEventColProp.NumID_GPosR;

        // PLZ
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_PLZ");
        cp.Caption = "PLZ";
        cp.Width = 30;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taRightJustify);
        cp.setColType(TColType.colTypeRank);
        cp.NumID = TEventColProp.NumID_PLZ;

        // Cup
        cp = ColsAvail.AddBaseColProp();
        cp.setNameID("col_Cup");
        cp.Caption = "Cup";
        cp.Width = 45;
        cp.Sortable = true;
        cp.setAlignment(TColAlignment.taRightJustify);
        cp.setColType(TColType.colTypeRank);
        cp.NumID = TEventColProp.NumID_Cup;
    }

    @Override
    protected String GetTextDefault(TEventRowCollectionItem cr, String Value) {
        Value = super.GetTextDefault(cr, Value);

        if (NumID == NumID_SNR) {
            Value = Utils.IntToStr(cr.SNR);
        } else if (NumID == NumID_Bib) {
            Value = Utils.IntToStr(cr.Bib);

        } else if (NumID == NumID_DN) {
            Value = cr.DN();

        } else if (NumID == NumID_NF4) {
            Value = cr.NC();
        } else if (NumID == NumID_GPoints) {
            Value = cr.GPoints();

        } else if (NumID == NumID_GRank) {
            Value = Utils.IntToStr(cr.GRank());

        } else if (NumID == NumID_GPosR) {
            Value = Utils.IntToStr(cr.GPosR());

        } else if (NumID == NumID_PLZ) {
            Value = Utils.IntToStr(cr.PLZ() + 1);

        } else if (NumID == NumID_Cup) {
            Value = Utils.FloatToStr(cr.RA, "0.00");
        }
        // { Race[0] is not shown in UI }
        else if (IsRaceNumID(NumID)) {
            int i = RaceIndex(NumID);
            Value = cr.Race[i].getRaceValue();
        }

        else if (NumID == NumID_NF1)
            Value = cr.FN();
        else if (NumID == NumID_NF2)
            Value = cr.LN();
        else if (NumID == NumID_NF3)
            Value = cr.SN();
        else if (NumID == NumID_NF5)
            Value = cr.GR();
        else if (NumID == NumID_NF6)
            Value = cr.PB();

        return Value;
    }

    public static int NumID_Race(int index) {
        return 10000 + index * 1000;
    }

    public static int RaceIndex(int numID) {
        return (numID - 10000) / 1000;
    }

    public static boolean IsRaceNumID(int numID) {
        return numID > 10000;
    }

    public static final int NumID_SNR = 1;
    public static final int NumID_Bib = 2;
    public static final int NumID_GPoints = 3;
    public static final int NumID_GRank = 4;
    public static final int NumID_GPosR = 5;
    public static final int NumID_PLZ = 6;
    public static final int NumID_Cup = 7;

    public static final int NumID_DN = 10;
    public static final int NumID_NF1 = 11;
    public static final int NumID_NF2 = 12;
    public static final int NumID_NF3 = 13;
    public static final int NumID_NF4 = 14;
    public static final int NumID_NF5 = 15;
    public static final int NumID_NF6 = 16;

}
