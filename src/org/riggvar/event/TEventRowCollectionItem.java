package org.riggvar.event;

import java.awt.*;

import org.riggvar.race.*;
import org.riggvar.stammdaten.*;
import org.riggvar.col.*;
import org.riggvar.base.*;
import org.riggvar.bo.TMain;

public class TEventRowCollectionItem extends
        TBaseRowCollectionItem<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp> {
    public static Color clFleetYellow = new Color(0xFF, 0xFF, 0xCC);
    public static Color clFleetBlue = new Color(0xCC, 0xFF, 0xFF);
    public static Color clFleetRed = new Color(0xFF, 0xCC, 0xCC);
    public static Color clFleetGreen = new Color(0xCC, 0xFF, 0xCC);

    public int SNR;
    public int Bib;

    private TStammdatenRowCollectionItem GetSDItem() {
        return ru().StammdatenRowCollection.FindKey(SNR);
    }

    public TEventRaceEntry[] Race;
    public TEventRaceEntry GRace;
    public int Cup;
    public double RA; // Uniqua-Ranglistenpunkte
    public double QR; // WM qualification points
    public boolean isTied;
    public boolean isGezeitet;
    public FREnumSet EntryErrors = new FREnumSet(TEntryError.Count);

    public TEventRowCollectionItem() {
        super();
        // SetLength(Race, RaceCount + 1);
        Race = new TEventRaceEntry[this.RaceCount() + 1];
        for (int i = 0; i < this.RCount(); i++) {
            Race[i] = new TEventRaceEntry(TMain.BO.EventNode);
            // Race[i].FNode = ru;
        }
        GRace = Race[0];
    }

    // @Override
    public void Assign(TEventRowCollectionItem source) {
        if (source != null) {
            TEventRowCollectionItem o = source;
            SNR = o.SNR;
            Bib = o.Bib;
            for (int i = 0; i < Race.length; i++) {
                Race[i] = o.Race[i];
            }
        }
    }

    public void Assign(TEventEntry source) {
        if (source != null) {
            TEventEntry o = source;
            SNR = o.SNR;
            Bib = o.Bib;
            for (int i = 0; i < Race.length; i++) {
                Race[i] = o.Race[i];
            }
        }
    }

    @Override
    public void ClearResult() {
        super.ClearResult();
        TEventRaceEntry ere;
        for (int r = 1; r < this.RaceCount(); r++) {
            ere = Race[r];
            ere.Clear();
        }
    }

    @Override
    public Color ColumnToColorDef(TEventColProp cp, Color aColor) {
        TColorMode ColorMode = ru().ColorMode;
        if (ColorMode == TColorMode.ColorMode_None)
            return Color.white;
        else {
            int NumID = cp.NumID;
            if (NumID == TEventColProp.NumID_Bib)
                return BibColor(aColor);
            else if (NumID == TEventColProp.NumID_SNR)
                return SNRColor(aColor);
            else if (TEventColProp.IsRaceNumID(NumID))
                return RaceColor(NumID, aColor);
            else
                return aColor;
        }
    }

    private Color FleetColor(int r, Color aColor) {
        int i = Race[r].Fleet;
        switch (i) {
        case 0:
            return Color.white;
        case 1:
            return clFleetYellow;
        case 2:
            return clFleetBlue;
        case 3:
            return clFleetRed;
        case 4:
            return clFleetGreen;
        default:
            return aColor;
        }
    }

    private Color BibColor(Color aColor) {
        if (EntryErrors.IsMember(TEntryError.error_Duplicate_Bib))
            return Color.cyan;
        else if (EntryErrors.IsMember(TEntryError.error_OutOfRange_Bib))
            return Color.cyan;
        else
            return aColor;
    }

    private Color SNRColor(Color aColor) {
        if (EntryErrors.IsMember(TEntryError.error_Duplicate_SNR))
            return Color.cyan;
        else if (EntryErrors.IsMember(TEntryError.error_OutOfRange_SNR))
            return Color.cyan;
        else
            return aColor;
    }

    private Color RaceErrorColor(int r, Color aColor) {
        if (Race[r].FinishErrors.IsMember(TFinishError.error_Duplicate_OTime))
            return TColGridColors.clHellRot;
        else if (Race[r].FinishErrors.IsMember(TFinishError.error_Contiguous_OTime))
            return TColGridColors.clAqua;
        else if (Race[r].FinishErrors.IsMember(TFinishError.error_OutOfRange_OTime_Max))
            return TColGridColors.clOlive;
        else if (Race[r].FinishErrors.IsMember(TFinishError.error_OutOfRange_OTime_Min))
            return TColGridColors.clOlive;
        else
            return aColor;
    }

    private Color RaceColor(int NumID, Color aColor) {
        Color result = aColor;
        int r = TEventColProp.RaceIndex(NumID);
        if (r > 0) {
            TRaceNode rn = TMain.BO.FindNode("W" + r);
            if (!rn.IsRacing)
                result = TColGridColors.clBtnFace;
            else if (ru().ColorMode == TColorMode.ColorMode_Error)
                result = RaceErrorColor(r, aColor);
            else if (ru().ColorMode == TColorMode.ColorMode_Fleet)
                result = FleetColor(r, aColor);
        }
        return result;
    }

    public String getRaceValue(int Index) // RaceValue
    {
        if ((Index >= 0) && (Index < Race.length)) {
            return Race[Index].getRaceValue();
        } else {
            return "";
        }
    }

    public void setRaceValue(int Index, String value) {
        if ((Index >= 0) && (Index < Race.length)) {
            Race[Index].setRaceValue(value);
        }
    }

    public int RCount() {
        return Race.length;
    }

    public int RaceCount() {
        return TMain.BO.BOParams.RaceCount;
    }

    public String FN() {
        TStammdatenRowCollectionItem sd = GetSDItem();
        if (sd != null) {
            return sd.getFN();
        } else {
            return "";
        }
    }

    public String LN() {
        TStammdatenRowCollectionItem sd = GetSDItem();
        if (sd != null) {
            return sd.getLN();
        } else {
            return "";
        }
    }

    public String SN() {
        TStammdatenRowCollectionItem sd = GetSDItem();
        if (sd != null) {
            return sd.getSN();
        } else {
            return "";
        }
    }

    public String NC() {
        TStammdatenRowCollectionItem sd = GetSDItem();
        if (sd != null) {
            return sd.getNC();
        } else {
            return "";
        }
    }

    public String GR() {
        TStammdatenRowCollectionItem sd = GetSDItem();
        if (sd != null) {
            return sd.getGR();
        } else {
            return "";
        }
    }

    public String PB() {
        TStammdatenRowCollectionItem sd = GetSDItem();
        if (sd != null) {
            return sd.getPB();
        } else {
            return "";
        }
    }

    public String DN() {
        TStammdatenRowCollectionItem sd = GetSDItem();
        if (sd != null) {
            return sd.DN;
        } else {
            return "";
        }
    }

    public TProps Props() {
        TStammdatenRowCollectionItem sd = GetSDItem();
        if (sd != null) {
            return sd.Props;
        } else {
            return null;
        }
    }

    public String GPoints() {
        return Race[0].getDecimalPoints();
    }

    public int GTime1() {
        return Race[0].getCTime1();
    }

    public int GRank() {
        return Race[0].Rank;
    }

    public int GPosR() {
        return Race[0].PosR;
    }

    public int PLZ() {
        return Race[0].PLZ;
    }
}
