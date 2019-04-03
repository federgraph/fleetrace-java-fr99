package org.riggvar.bo;

import org.riggvar.base.*;
import org.riggvar.event.*;
import org.riggvar.inspector.*;
import org.riggvar.scoring.*;
import org.riggvar.stammdaten.*;

public class TEventProps extends TLineParser implements IInspectable {
    public boolean NormalizeOutput;
    public String DetailUrl = "";
    public String EventNameID = "1";
    public String SortColName = "";

    public TBO BO; // handle of event business object
    public IConnection Connection; // used to send test data

    // Regatta Props
    public String EventName;
    public String EventDates;
    public String HostClub;
    public String PRO; // Principal Race Officer (Wettfahrtleiter)
    public String JuryHead;
    public int ScoringSystem; // enum TScoringSystem
    public int ScoringSystem2;// external scoring system code, pass on as is via proxy
    public int Throwouts;
    public int ThrowoutScheme; // enum TThrowoutScheme
    public boolean FirstIs75;
    public boolean ReorderRAF;

    // Uniqua Props
    public boolean ShowCupColumn;
    public boolean EnableUniquaProps; // override calculated values
    public int UniquaGemeldet; // Count of Entries
    public int UniquaGesegelt; // Count of Races
    public int UniquaGezeitet; // Count of Entries at start
    public double FFaktor;

    // Other
    public boolean IsTimed;

    public TEventProps(TBO abo) {
        BO = abo;
        LoadDefaultData();
        Connection = BO.InputServer.Server.Connect();
    }

    public int getGemeldet() {
        if (EnableUniquaProps) {
            return UniquaGemeldet;
        } else {
            return FRGemeldet();
        }
    }

    public void setGemeldet(int value) {
        UniquaGemeldet = value;

    }

    public int getGesegelt() {
        if (EnableUniquaProps) {
            return UniquaGesegelt;
        } else {
            return FRGesegelt();
        }
    }

    public void setGesegelt(int value) {
        UniquaGesegelt = value;
    }

    public int getGezeitet() {
        if (EnableUniquaProps) {
            return UniquaGezeitet;
        } else {
            return FRGezeitet();
        }
    }

    public void setGezeitet(int value) {
        UniquaGezeitet = value;
    }

    public double getFaktor() {
        return FFaktor;
    }

    public void setFaktor(double value) {
        if ((value > 0.1) && (value < 10)) {
            FFaktor = value;
        }
    }

    public String getDivisionName() {
        return BO.cTokenDivision;
    }

    public void setDivisionName(String value) {
        BO.SetDivisionName(value);
    }

    public int getInputMode() {
        if (BO.EventBO.getRelaxedInputMode()) {
            return TInputMode.Relaxed;
        } else {
            return TInputMode.Strict;
        }
    }

    public void setInputMode(int value) {
        if (value == TInputMode.Relaxed) {
            BO.EventBO.setRelaxedInputMode(true);
        } else {
            BO.EventBO.setRelaxedInputMode(false);
        }
    }

    public String getFieldMap() {
        return BO.StammdatenNode.getBaseRowCollection().getFieldMap();
    }

    public void setFieldMap(String value) {
        BO.StammdatenNode.getBaseRowCollection().setFieldMap(value);
    }

    public String getFieldCaptions() {
        return BO.StammdatenNode.getBaseRowCollection().getFieldCaptions();
    }

    public void setFieldCaptions(String value) {
        BO.StammdatenNode.getBaseRowCollection().setFieldCaptions(value);
    }

    public String getFieldCount() {
        int i = BO.StammdatenNode.getBaseRowCollection().getFieldCount();
        return "" + i;
    }

    public void setFieldCount(String value) {
        int i = Utils.StrToIntDef(value, -1);
        if (i != -1) {
            BO.StammdatenNode.getBaseRowCollection().setFieldCount(i);
        }
    }

    public String getNameFieldCount() {
        return "" + BO.EventBO.NameFieldCount;
    }

    public void setNameFieldCount(String value) {
        int i = Utils.StrToIntDef(value, -1);
        if (i != -1) {
            BO.EventBO.NameFieldCount = i;
        }
    }

    public String getNameFieldOrder() {
        return BO.EventBO.NameFieldOrder;
    }

    public void setNameFieldOrder(String value) {
        BO.EventBO.NameFieldOrder = value;
    }

    public String getRaceLayout() {
        if (BO.EventNode.getShowPoints() == TEventNode.Layout_Finish) {
            return "Finish";
        } else {
            return "Points"; // default
        }
    }

    public void setRaceLayout(String value) {
        if (value.equals("Finish")) {
            BO.EventNode.setShowPoints(TEventNode.Layout_Finish);
        } else {
            BO.EventNode.setShowPoints(TEventNode.Layout_Points);
        }
    }

    public String getNameSchema() {
        switch (FieldNames.getSchemaCode()) {
        case 2:
            return "NX";
        case 1:
            return "LongNames";
        default:
            return "";
        }
    }

    public void setNameSchema(String value) {
        if (value.equals("NX")) {
            FieldNames.setSchemaCode(2);
        } else if (value.equals("LongNames")) {
            FieldNames.setSchemaCode(1);
        } else {
            FieldNames.setSchemaCode(0);
        }
    }

    public boolean getShowPLZColumn() {
        return BO.EventNode.ShowPLZColumn;
    }

    public void setShowPLZColumn(boolean value) {
        BO.EventNode.ShowPLZColumn = value;
    }

    public boolean getShowPosRColumn() {
        return BO.EventNode.ShowPosRColumn;
    }

    public void setShowPosRColumn(boolean value) {
        BO.EventNode.ShowPosRColumn = value;
    }

    public String getColorMode() {
        switch (BO.EventNode.ColorMode) {
        case ColorMode_Fleet:
            return "Fleet";
        case ColorMode_None:
            return "None";
        default:
            return "Normal";
        }
    }

    public void setColorMode(String value) {
        if (value.equals("Fleet"))
            BO.EventNode.ColorMode = TColorMode.ColorMode_Fleet;
        else if (value.equals("None"))
            BO.EventNode.ColorMode = TColorMode.ColorMode_None;
        else
            BO.EventNode.ColorMode = TColorMode.ColorMode_Error;
    }

    public boolean getUseCompactFormat() {
        return BO.UseCompactFormat;
    }

    public void setUseCompactFormat(boolean value) {
        BO.UseCompactFormat = value;
    }

    public boolean getUseFleets() {
        return BO.EventNode.UseFleets;
    }

    public void setUseFleets(boolean value) {
        BO.EventNode.UseFleets = value;
    }

    public boolean getUseInputFilter() {
        return BO.UseInputFilter;
    }

    public void setUseInputFilter(boolean value) {
        BO.UseInputFilter = value;
    }

    public boolean getUseOutputFilter() {
        return BO.UseOutputFilter;
    }

    public void setUseOutputFilter(boolean value) {
        BO.UseOutputFilter = value;
    }

    public int getTargetFleetSize() {
        return BO.EventNode.TargetFleetSize;
    }

    public void setTargetFleetSize(int value) {
        BO.EventNode.TargetFleetSize = value;
    }

    public int getFirstFinalRace() {
        return BO.EventNode.getFirstFinalRace();
    }

    public void setFirstFinalRace(int value) {
        BO.EventNode.setFirstFinalRace(value);
    }

    @Override
    protected boolean ParseKeyValue(String Key, String Value) {
        if (Key.startsWith("EP.")) {
            Key = Key.substring("EP.".length());
        } else if (Key.startsWith("Event.Prop_")) {
            Key = Key.substring("Event.Prop_".length());
        }

        if (Key.equals("Name")) {
            EventName = Value;
        } else if (Key.equals("Dates")) {
            EventDates = Value;
        } else if (Key.equals("HostClub")) {
            HostClub = Value;
        } else if (Key.equals("PRO")) {
            PRO = Value;
        } else if (Key.equals("JuryHead")) {
            JuryHead = Value;
        } else if (Key.equals("ScoringSystem")) {
            if (Utils.Pos("DSV", Value) > 0) {
                ScoringSystem = TScoringSystem.BonusDSV;
            } else if (Value.indexOf("onus") > -1) {
                ScoringSystem = TScoringSystem.Bonus;
            } else {
                ScoringSystem = TScoringSystem.LowPoint;
            }
        } else if (Key.equals("ScoringSystem2")) {
            ScoringSystem2 = Utils.StrToIntDef(Value, ScoringSystem2);
        } else if (Key.equals("Throwouts")) {
            Throwouts = Utils.StrToIntDef(Value, Throwouts);
        } else if (Key.equals("ThrowoutScheme")) {
            ThrowoutScheme = TThrowoutScheme.ParseDef(Value, TThrowoutScheme.throwoutNONE);
//				if (Value.equals("ByNumRaces"))
//					ThrowoutScheme = TThrowoutScheme.throwoutBYNUMRACES;
//				else if (Value.equals("ByBestXRaces"))
//					ThrowoutScheme = TThrowoutScheme.throwoutBESTXRACES;
//				else if (Value.equals("PerXRaces"))
//					ThrowoutScheme = TThrowoutScheme.throwoutPERXRACES;
//				else
//					ThrowoutScheme = TThrowoutScheme.throwoutNONE;
        } else if (Key.equals("FirstIs75"))
            FirstIs75 = true;
        else if (Key.equals("ReorderRAF"))
            FirstIs75 = Utils.IsTrue(Value);
        else if (Key.equals("ColorMode"))
            setColorMode(Value);
        else if (Key.equals("UseFleets"))
            setUseFleets(Utils.IsTrue(Value));
        else if (Key.equals("TargetFleetSize"))
            setTargetFleetSize(Utils.StrToIntDef(Value, getTargetFleetSize()));
        else if (Key.equals("FirstFinalRace"))
            setFirstFinalRace(Utils.StrToIntDef(Value, getFirstFinalRace()));
        else if (Key.equals("IsTimed"))
            IsTimed = Utils.IsTrue(Value);
        else if (Key.equals("UseCompactFormat"))
            setUseCompactFormat(Utils.IsTrue(Value));
        else if (Key.equals("ShowPosRColumn"))
            setShowPosRColumn(Utils.IsTrue(Value));
        else if (Key.equals("ShowCupColumn")) {
            ShowCupColumn = Utils.IsTrue(Value);
        } else if (Key.equals("Uniqua_Enabled")) {
            EnableUniquaProps = Utils.IsTrue(Value);
        } else if (Key.equals("Uniqua_Gesegelt")) {
            UniquaGesegelt = Utils.StrToIntDef(Value, getGesegelt());
        } else if (Key.equals("Uniqua_Gemeldet")) {
            UniquaGemeldet = Utils.StrToIntDef(Value, getGemeldet());
        } else if (Key.equals("Uniqua_Gezeitet")) {
            UniquaGezeitet = Utils.StrToIntDef(Value, getGezeitet());
        } else if (Key.equals("Uniqua_Faktor")) {
            setFaktor(Utils.StrToFloatDef(Value, getFaktor()));
        } else if (Key.equals("DivisionName")) {
            setDivisionName(Value);
        } else if (Key.equals("InputMode") || Key.equals("IM")) {
            if (Value.toLowerCase().equals("strict")) {
                setInputMode(TInputMode.Strict);
            } else {
                setInputMode(TInputMode.Relaxed);
            }
        } else if (Key.equals("FieldMap")) {
            setFieldMap(Value);
        } else if (Key.equals("FieldCaptions")) {
            setFieldCaptions(Value);
        } else if (Key.equals("FieldCount")) {
            setFieldCount(Value);
        } else if (Key.equals("NameFieldCount")) {
            setNameFieldCount(Value);
        } else if (Key.equals("NameFieldOrder")) {
            setNameFieldOrder(Value);
        } else if (Key.equals("RaceLayout")) {
            setRaceLayout(Value);
        } else if (Key.equals("NameSchema")) {
            setNameSchema(Value);
        } else if (Key.equals("DetailUrl"))
            DetailUrl = Value;
        else if (Key.equals("EventNameID"))
            EventNameID = Value;
        else if (Key.equals("NormalizeOutput"))
            setNormalizeOutput(Value);
        else if (Key.equals("SortColName"))
            SortColName = Value;

        else {
            return false;
        }

        return true;
    }

    public void LoadDefaultData() {
        EventName = "Regatta X";
        EventDates = "Event Dates";
        HostClub = "Club Y";
        ScoringSystem = TScoringSystem.LowPoint;
        Throwouts = 1;
        ThrowoutScheme = TThrowoutScheme.throwoutBYNUMRACES; // 1
        // setDivisionName("420");

        // copy properties from BO
        ShowCupColumn = false;
        EnableUniquaProps = false;
        setGemeldet(getGemeldet());
        setGezeitet(getGezeitet());
        setGesegelt(getGesegelt());
        setFaktor(1.10);
    }

    //
    public void SaveProps(TStrings SLBackup) {
        SLBackup.Add("EP.Name = " + EventName);
        SLBackup.Add("EP.Dates = " + EventDates);
        SLBackup.Add("EP.HostClub = " + HostClub);
        SLBackup.Add("EP.PRO = " + PRO);
        SLBackup.Add("EP.JuryHead = " + JuryHead);
        SLBackup.Add("EP.ScoringSystem = " + TScoringSystem.getString(ScoringSystem));
        SLBackup.Add("EP.ScoringSystem2 = " + ScoringSystem2);
        SLBackup.Add("EP.Throwouts = " + Utils.IntToStr(Throwouts));
        SLBackup.Add("EP.ThrowoutScheme = " + TThrowoutScheme.getString(ThrowoutScheme));
        if (FirstIs75)
            SLBackup.Add("EP.FirstIs75 = true");
        if (ReorderRAF == false)
            SLBackup.Add("EP.ReorderRAF = false");

        SLBackup.Add("EP.ColorMode = " + getColorMode());
        SLBackup.Add("EP.UseFleets = " + Utils.BoolStr(getUseFleets()));
        SLBackup.Add("EP.TargetFleetSize = " + getTargetFleetSize());
        SLBackup.Add("EP.FirstFinalRace = " + getFirstFinalRace());
        SLBackup.Add("EP.IsTimed = " + Utils.BoolStr(IsTimed));
        SLBackup.Add("EP.UseCompactFormat = " + Utils.BoolStr(getUseCompactFormat()));
        SLBackup.Add("EP.DivisionName = " + getDivisionName());
        SLBackup.Add("EP.InputMode = " + TInputMode.getString(getInputMode()));
        SLBackup.Add("EP.RaceLayout = " + getRaceLayout());
        SLBackup.Add("EP.NameSchema = " + getNameSchema());
        SLBackup.Add("EP.FieldMap = " + getFieldMap());
        SLBackup.Add("EP.FieldCaptions = " + getFieldCaptions());
        SLBackup.Add("EP.FieldCount = " + getFieldCount());
        SLBackup.Add("EP.NameFieldCount = " + getNameFieldCount());
        SLBackup.Add("EP.NameFieldOrder = " + getNameFieldOrder());
        SLBackup.Add("EP.ShowPosRColumn = " + Utils.BoolStr(getShowPosRColumn()));
        SLBackup.Add("EP.ShowCupColumn = " + Utils.BoolStr(ShowCupColumn));
        if (ShowCupColumn) {
            SLBackup.Add("EP.Uniqua.Faktor = " + Utils.FloatToStr(getFaktor(), "F2"));
            SLBackup.Add("EP.Uniqua.Enabled  = " + Utils.BoolStr(EnableUniquaProps));
            SLBackup.Add("EP.Uniqua.Gesegelt = " + Utils.IntToStr(getGesegelt()));
            SLBackup.Add("EP.Uniqua.Gemeldet = " + Utils.IntToStr(getGemeldet()));
            SLBackup.Add("EP.Uniqua.Gezeitet = " + Utils.IntToStr(getGezeitet()));
        }
    }

    public boolean EditRegattaProps() {
        // result := FrmRegattaProps.EditRegattaProps(Self);
        return false;
    }

    public boolean EditUniquaProps() {
        // result := FrmUniquaProps.EditUniquaProps(Self);
        return false;
    }

    public boolean EditFleetProps() {
        // result := Main.FormAdapter.EditFleetProps(Self);
        return false;
    }

    public int FRGemeldet() {
        return BO.getGemeldet();
    }

    public int FRGesegelt() {
        return BO.getGesegelt();
    }

    public int FRGezeitet() {
        return BO.Gezeitet;
    }

    public void setNormalizeOutput(String value) {
        NormalizeOutput = (value != null && value.toLowerCase().startsWith("t"));
    }

    public String getNormalizeOutput() {
        if (NormalizeOutput)
            return "true";
        else
            return "false";
    }

    public void inspectorOnLoad(Object sender) {
        TNameValueRowCollection cl;
        TNameValueRowCollectionItem cr;
        if (!(sender instanceof TNameValueRowCollection))
            return;

        cl = (TNameValueRowCollection) sender;

        cr = cl.AddRow();
        cr.Category = "File";
        cr.FieldName = "UseCompactFormat";
        cr.FieldType = NameValueFieldType.FTBoolean;
        cr.FieldValue = Utils.BoolStr(getUseCompactFormat());
        cr.Caption = "UseCompactFormat";
        cr.Description = "use delimited-value tables";

        cr = cl.AddRow();
        cr.Category = "File";
        cr.FieldName = "IsTimed";
        cr.FieldType = NameValueFieldType.FTBoolean;
        cr.FieldValue = Utils.BoolStr(IsTimed);
        cr.Caption = "IsTimed";
        cr.Description = "save space if event is not timed";

        cr = cl.AddRow();
        cr.Category = "Scoring";
        cr.FieldName = "ReorderRAF";
        cr.FieldType = NameValueFieldType.FTBoolean;
        cr.FieldValue = Utils.BoolStr(ReorderRAF);
        cr.Caption = "ReorderRAF";
        cr.Description = "if false, do not shuffle finish position";

        cr = cl.AddRow();
        cr.Category = "Layout";
        cr.FieldName = "ShowPLZColumn";
        cr.FieldType = NameValueFieldType.FTBoolean;
        cr.FieldValue = Utils.BoolStr(getShowPLZColumn());
        cr.Caption = "ShowPLZColumn";
        cr.Description = "show index column for debugging...";

        cr = cl.AddRow();
        cr.Category = "Layout";
        cr.FieldName = "ShowPosRColumn";
        cr.FieldType = NameValueFieldType.FTBoolean;
        cr.FieldValue = Utils.BoolStr(getShowPosRColumn());
        cr.Caption = "ShowPosRColumn";
        cr.Description = "show column with unique ranking";

        cr = cl.AddRow();
        cr.Category = "File";
        cr.FieldName = "UseOutputFilter";
        cr.FieldType = NameValueFieldType.FTBoolean;
        cr.FieldValue = Utils.BoolStr(getUseOutputFilter());
        cr.Caption = "UseOutputFilter";
        cr.Description = "apply filter when saving once";

        cr = cl.AddRow();
        cr.Category = "Layout";
        cr.FieldName = "NameFieldCount";
        cr.FieldType = NameValueFieldType.FTInteger;
        cr.FieldValue = getNameFieldCount();
        cr.Caption = "NameFieldCount";
        cr.Description = "count of name columns in event table display";

        cr = cl.AddRow();
        cr.Category = "Layout";
        cr.FieldName = "NameFieldOrder";
        cr.FieldType = NameValueFieldType.FTString;
        cr.FieldValue = getNameFieldOrder();
        cr.Caption = "NameFieldOrder";
        cr.Description = "namefield index string";

    }

    public void inspectorOnSave(Object sender) {
        TNameValueRowCollection cl;
        TNameValueRowCollectionItem cr;

        if (!(sender instanceof TNameValueRowCollection))
            return;

        cl = (TNameValueRowCollection) sender;

        for (int i = 0; i < cl.size(); i++) {
            cr = cl.getItem(i);
            if (cr.FieldName.equals("UseCompactFormat"))
                setUseCompactFormat(Utils.IsTrue(cr.FieldValue));
            else if (cr.FieldName.equals("IsTimed"))
                IsTimed = Utils.IsTrue(cr.FieldValue);
            else if (cr.FieldName.equals("ReorderRAF"))
                ReorderRAF = Utils.IsTrue(cr.FieldValue);
            else if (cr.FieldName.equals("ShowPLZColumn"))
                setShowPLZColumn(Utils.IsTrue(cr.FieldValue));
            else if (cr.FieldName.equals("ShowPosRColumn"))
                setShowPosRColumn(Utils.IsTrue(cr.FieldValue));
            else if (cr.FieldName.equals("UseOutputFilter"))
                setUseOutputFilter(Utils.IsTrue(cr.FieldValue));
            else if (cr.FieldName.equals("NameFieldCount"))
                setNameFieldCount(cr.FieldValue);
            else if (cr.FieldName.equals("NameFieldOrder"))
                setNameFieldOrder(cr.FieldValue);
        }
    }

}
