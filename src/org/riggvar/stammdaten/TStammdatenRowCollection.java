package org.riggvar.stammdaten;

import org.riggvar.base.*;
import org.riggvar.bo.TMain;
import org.riggvar.col.*;

public class TStammdatenRowCollection extends
        TBaseRowCollection<TStammdatenColGrid, TStammdatenBO, TStammdatenNode, TStammdatenRowCollection, TStammdatenRowCollectionItem, TStammdatenColProps, TStammdatenColProp> {
    private static final long serialVersionUID = 1L;

    private String FFieldMap = "SN";
    private TStringList MapList = new TStringList();
    private TStringList FieldCaptionList = new TStringList();

    public TStammdatenRowCollection() {
        super();
    }

    @Override
    protected TStammdatenRowCollectionItem NewItem() {
        TStammdatenRowCollectionItem o = new TStammdatenRowCollectionItem();
        o.Collection = this;
        return o;
    }

    public void UpdateItem(TStammdatenEntry e) {
        TStammdatenRowCollectionItem o = FindKey(e.SNR);
        if (o == null) {
            o = AddRow();
        } else {
            o.Assign(e);
        }
    }

    public TStammdatenRowCollectionItem FindKey(int SNR) {
        for (int i = 0; i < getCount(); i++) {
            TStammdatenRowCollectionItem o = this.get(i);
            if (o != null && o.SNR == SNR) {
                return o;
            }
        }
        return null;
    }

    public void CalcDisplayName(TStammdatenRowCollectionItem cr) {
        String s, t;
        t = "";
        for (int i = 0; i < MapList.getCount(); i++) {
            s = MapList.getString(i);
            if (s.equals(FieldNames.LN) || s.equals("LN")) {
                t += cr.getLN();
            } else if (s.equals(FieldNames.FN) || s.equals("FN")) {
                t += cr.getFN();
            } else if (s.equals(FieldNames.SN) || s.equals("SN")) {
                t += cr.getSN();
            } else if (s.equals(FieldNames.GR) || s.equals("Gender")) {
                t += cr.getGR();
            } else if (s.equals(FieldNames.NC) || s.equals("NOC")) {
                t += cr.getNC();
            } else if (s.equals(FieldNames.PB) || s.equals("PB")) {
                t += cr.getPB();
            } else if (s.equals("_") || s.equals("Space")) {
                t += " ";
            } else if (s.equals("-")) {
                t += " - ";
            } else if (s.equals("Slash")) {
                t += " / ";
            } else if (s.equals("Komma")) {
                t += ", ";
            } else {
                t += s;
            }
        }
        cr.DN = t;
    }

    public String getFieldMap() {
        return FFieldMap;
    }

    public void setFieldMap(String value) {
        FFieldMap = value;
        MapList.setCommaText(value);
        TStammdatenRowCollectionItem cr;
        for (int i = 0; i < this.getCount(); i++) {
            cr = this.get(i);
            CalcDisplayName(cr);

        }
    }

    public int getSchemaCode() {
        return FieldNames.getSchemaCode();
    }

    public void setSchemaCode(int value) {
        FieldNames.setSchemaCode(value);
    }

    public String getFieldCaption(int index) {
        if (index > 0 && index <= FieldCaptionList.getCount()) {
            String s = FieldCaptionList.getString(index - 1);
            if (s != null && !s.equals("")) {
                return s;
            }
        }
        switch (index) {
        case 0:
            return "SNR";
        case 1:
            return FieldNames.FN;
        case 2:
            return FieldNames.LN;
        case 3:
            return FieldNames.SN;
        case 4:
            return FieldNames.NC;
        case 5:
            return FieldNames.GR;
        case 6:
            return FieldNames.PB;
        }
        return "N" + index;
    }

    public String getFieldCaptions() {
        return FieldCaptionList.getCommaText();
    }

    public void setFieldCaptions(String value) {
        FieldCaptionList.setCommaText(value);
    }

    public static final int FixFieldCount = 6;
    private int fieldCount = FixFieldCount; // cached for performance

    public int getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(int value) {
        fieldCount = value;
        TMain.BO.AdapterParams.FieldCount = value;
    }

    public void Swap(int f1, int f2) {
        TStammdatenRowCollectionItem cr;
        String s;
        if (f1 != f2 && f1 > 0 && f2 > 0 && f1 <= getFieldCount() && f2 <= getFieldCount()) {
            for (int i = 0; i < getCount(); i++) {
                cr = this.get(i);
                s = cr.getFieldValue(f1);
                cr.setFieldValue(f1, cr.getFieldValue(f2));
                cr.setFieldValue(f2, s);
            }
        }
    }

    public void GetXML(TStrings SL) {
        SL.Add("<e xmlns=\"http://riggvar.net/Entries.xsd\">");
        SL.Add("");
        GetXMLSchema(SL);
        SL.Add("");
        GetXMLResult(SL);
        SL.Add("");
        GetXMLBackup(SL);
        SL.Add("");
        SL.Add("</e>");
    }

    public void GetXMLSchema(TStrings SL) {
        SL.Add("<xs:schema id=\"e\" targetNamespace=\"http://riggvar.net/Entries.xsd\"");
        SL.Add("xmlns:mstns=\"http://riggvar.net/Entries.xsd\"");
        SL.Add("xmlns=\"http://riggvar.net/Entries.xsd\"");
        SL.Add("xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"");
        SL.Add("xmlns:msdata=\"urn:schemas-microsoft-com:xml-msdata\"");
        SL.Add("attributeFormDefault=\"qualified\"");
        SL.Add("elementFormDefault=\"qualified\">");
        SL.Add("");
        SL.Add("<xs:element name=\"e\" msdata:IsDataSet=\"true\" msdata:Locale=\"de-DE\" msdata:EnforceConstraints=\"False\">");
        SL.Add("<xs:complexType>");
        SL.Add("<xs:choice maxOccurs=\"unbounded\">");
        SL.Add("");
        SL.Add("<xs:element name=\"r\">");
        SL.Add("<xs:complexType>");
        SL.Add("<xs:attribute name=\"Id\" form=\"unqualified\" type=\"xs:positiveInteger\" />");
        SL.Add("<xs:attribute name=\"SNR\" form=\"unqualified\" type=\"xs:positiveInteger\" />");
        for (int r = 1; r <= this.getFieldCount(); r++) {
            SL.Add("<xs:attribute name=\"N" + r + "\" form=\"unqualified\" type=\"xs:string\" />");
        }
        SL.Add("</xs:complexType>");
        SL.Add("</xs:element>");
        SL.Add("");
        SL.Add("<xs:element name=\"b\">");
        SL.Add("<xs:complexType>");
        SL.Add("<xs:attribute name=\"I\" form=\"unqualified\" type=\"xs:positiveInteger\" />");
        SL.Add("<xs:attribute name=\"N\" form=\"unqualified\" type=\"xs:string\" />");
        SL.Add("<xs:attribute name=\"V\" form=\"unqualified\" type=\"xs:string\" />");
        SL.Add("</xs:complexType>");
        SL.Add("</xs:element>");
        SL.Add("");
        SL.Add("</xs:choice>");
        SL.Add("</xs:complexType>");
        SL.Add("</xs:element>");
        SL.Add("</xs:schema>");
    }

    public void GetXMLResult(TStrings SL) {
        String s;
        TStammdatenRowCollectionItem cr;
        for (int i = 0; i < this.getCount(); i++) {
            cr = this.get(i);
            s = "<r Id=\"" + cr.BaseID + "\" ";
            s = s + "SNR=\"" + cr.SNR + "\" ";
            for (int r = 1; r < getFieldCount(); r++) {
                s = s + "N" + r + "=\"" + cr.getFieldValue(r) + "\" ";
            }
            s = s + "/>";
            SL.Add(s);
        }
    }

    public void GetXMLBackup(TStrings SL) {
        TStringList tempSL;
        String s;
        String sName, sValue;

        tempSL = new TStringList();

        TMain.BO.BackupToSL(tempSL);

        int j = 0;
        for (int i = 0; i < tempSL.getCount(); i++) {
            s = tempSL.getString(i);

            if ((s.equals("")) || (s.charAt(0) == '<')) {
                sName = "";
                sValue = "";
            } else if ((s.charAt(0) == '#') || (Utils.Copy(s, 1, 2).equals("//"))) {
                sName = s;
                sValue = "";
            } else {
                sName = tempSL.getName(i).trim();
                sValue = tempSL.getValueFromIndex(i).trim();
            }

            j++;
            s = "<b I=\"" + j + "\" N=\"" + sName + "\" V=\"" + sValue + "\" />";
            tempSL.setValue("" + i, s);
        }

        for (int i = 0; i < tempSL.getCount(); i++)
            SL.Add(tempSL.getString(i));

    }

}
