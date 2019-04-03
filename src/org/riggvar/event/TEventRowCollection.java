package org.riggvar.event;

import java.util.*;
import org.riggvar.base.*;
import org.riggvar.bo.TMain;
import org.riggvar.col.*;

public class TEventRowCollection extends
        TBaseRowCollection<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp> {
    private static final long serialVersionUID = 1L;

    public TEventRowCollection() {
        super();
    }

    @Override
    protected TEventRowCollectionItem NewItem() {
        TEventRowCollectionItem o = new TEventRowCollectionItem();
        o.Collection = this;
        return o;
    }

    public String GetHashString() {
        StringBuilder sb = new StringBuilder();
        TEventRowCollectionItem cr;
        for (int i = 0; i < getCount(); i++) {
            cr = get(i);
            if (i == 0)
                sb.append(cr.GRank());
            else {
                sb.append('-');
                sb.append(cr.GRank());
            }
        }
        return sb.toString();
    }

    public void GetXML(TStrings SL) {
        SL.Add("<e xmlns=\"http://riggvar.com/FR11.xsd\">");
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
        SL.Add("<xs:schema id=\"e\" targetNamespace=\"http://riggvar.com/FR11.xsd\"");
        SL.Add("xmlns:mstns=\"http://riggvar.com/FR11.xsd\"");
        SL.Add("xmlns=\"http://riggvar.com/FR11.xsd\"");
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
        SL.Add("<xs:attribute name=\"Pos\" form=\"unqualified\" type=\"xs:positiveInteger\" />");
        SL.Add("<xs:attribute name=\"Bib\" form=\"unqualified\" type=\"xs:positiveInteger\" />");
        SL.Add("<xs:attribute name=\"SNR\" form=\"unqualified\" type=\"xs:positiveInteger\" />");
        SL.Add("<xs:attribute name=\"SN\" form=\"unqualified\" type=\"xs:String\" />");
        for (int r = 1; r < RCount(); r++) {
            SL.Add("<xs:attribute name=\"W" + Utils.IntToStr(r) + "\" form=\"unqualified\" type=\"xs:String\" />");
        }
        SL.Add("<xs:attribute name=\"Pts\" form=\"unqualified\" type=\"xs:double\" />");
        SL.Add("</xs:complexType>");
        SL.Add("</xs:element>");
        SL.Add("");
        SL.Add("<xs:element name=\"b\">");
        SL.Add("<xs:complexType>");
        SL.Add("<xs:attribute name=\"I\" form=\"unqualified\" type=\"xs:positiveInteger\" />");
        SL.Add("<xs:attribute name=\"N\" form=\"unqualified\" type=\"xs:String\" />");
        SL.Add("<xs:attribute name=\"V\" form=\"unqualified\" type=\"xs:String\" />");
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
        TEventRowCollectionItem cr;
        for (int i = 0; i < getCount(); i++) {
            cr = get(i);
            s = "<r Id=\"" + Utils.IntToStr(cr.BaseID) + "\" ";
            s = s + "Pos=\"" + Utils.IntToStr(cr.GRank()) + "\" ";
            s = s + "Bib=\"" + Utils.IntToStr(cr.Bib) + "\" ";
            s = s + "SNR=\"" + Utils.IntToStr(cr.SNR) + "\" ";
            if (cr.SN().equals("")) {
                s = s + "SN=\"" + cr.LN() + "\" ";
            } else {
                s = s + "SN=\"" + cr.SN() + "\" ";

            }
            for (int r = 1; r < RCount(); r++) {
                s = s + "W" + Utils.IntToStr(r) + "=\"" + cr.getRaceValue(r) + "\" ";
            }
            s = s + "Pts=\"" + cr.GPoints() + "\" ";
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
            s = "<b I=\"" + Utils.IntToStr(j) + "\" N=\"" + sName + "\" V=\"" + sValue + "\" />";
            // s = StringReplace(s, cTokenFleetRace + '.' + cTokenDivision + '.', '', []);
            tempSL.setString(i, s);
        }

        for (int i = 0; i < tempSL.getCount(); i++) {
            SL.Add(tempSL.getString(i));

        }
    }

    public void UpdateItem(TEventEntry e) {
        TEventRowCollectionItem o = FindKey(e.SNR);
        if (o == null) {
            o = AddRow();
        }
        if (o != null) {
            o.Assign(e);
        }
    }

    public TEventRowCollectionItem FindKey(int SNR) {
        for (int i = 0; i < getCount(); i++) {
            TEventRowCollectionItem o = get(i);
            if (o != null && o.SNR == SNR) {
                return o;
            }
        }
        return null;
    }

    public int RCount() {
        if (getCount() > 0) {
            return get(0).RCount();
        } else if (getBaseNode() instanceof TEventNode) {
            return (getBaseNode()).getRaceCount() + 1;
        } else {
            return -1;
        }
    }

    public int getFleetCount(int r) {
        int temp;
        int fc = 0;
        for (int i = 0; i < getCount(); i++) {
            temp = this.get(i).Race[r].Fleet;
            if (temp > fc)
                fc = temp;
        }
        return fc;
    }

    public void FillFleetList(List<TEventRowCollectionItem> FL, int r, int f) {
        FL.clear();
        if (r > 0 && r < RCount()) {
            TEventRowCollectionItem cr;
            for (int i = 0; i < getCount(); i++) {
                cr = this.get(i);
                if (cr.Race[r].Fleet == f)
                    FL.add(cr);
            }
        }
    }

    public void ClearRace(int r) {
        TEventRaceEntry ere;
        if (r > 0 && r < RCount()) {
            TEventRowCollectionItem cr;
            int f;
            for (int i = 0; i < getCount(); i++) {
                cr = this.get(i);
                ere = cr.Race[r];
                f = ere.Fleet;
                ere.Clear();
                ere.Fleet = f;
            }
            getBaseNode().setModified(true);
        }
    }

}
