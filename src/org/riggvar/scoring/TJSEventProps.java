package org.riggvar.scoring;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class TJSEventProps extends TProxyProps {
    public static int LowPoint = 0;
    public static int Bonus = 1;
    public static int BonusDSV = 2;

    public int ScoringSystem2 = 0;
    public int ScoringSystem;
    public int ThrowoutScheme;
    public int Throwouts;
    public boolean FirstIs75;
    public boolean ReorderRAF;
    public String DivisionName;

    public TJSEventProps() {
        Clear();
    }

    public void Clear() {
        ScoringSystem = 0;
        ThrowoutScheme = 0;
        Throwouts = 1;
        FirstIs75 = false;
        ReorderRAF = true;
        DivisionName = "*";
    }

    public void Assign(TJSEventProps ep) {
        ScoringSystem = ep.ScoringSystem;
        ThrowoutScheme = ep.ThrowoutScheme;
        Throwouts = ep.Throwouts;
        FirstIs75 = ep.FirstIs75;
        ReorderRAF = ep.ReorderRAF;
        DivisionName = ep.DivisionName;
    }

    public boolean Equals(TJSEventProps ep) {
        if (ScoringSystem != ep.ScoringSystem) {
            return false;
        }
        if (ThrowoutScheme != ep.ThrowoutScheme) {
            return false;
        }
        if (Throwouts != ep.Throwouts) {
            return false;
        }
        if (FirstIs75 != ep.FirstIs75) {
            return false;
        }
        if (ReorderRAF != ep.ReorderRAF) {
            return false;
        }
        if (!DivisionName.equals(ep.DivisionName)) {
            return false;
        }
        return true;
    }

    public void WriteXml(XmlWriter xw) {
        xw.WriteStartElement(p_EventProps);

        xw.WriteAttributeString(p_ScoringSystem, ScoringSystem);
        xw.WriteAttributeString(p_ScoringSystem2, ScoringSystem2);
        xw.WriteAttributeString(p_ThrowoutScheme, ThrowoutScheme);
        xw.WriteAttributeString(p_Throwouts, Throwouts);
        if (FirstIs75) {
            xw.WriteAttributeString(p_FirstIs75, true);
        }
        if (ReorderRAF) {
            xw.WriteAttributeString(p_ReorderRAF, true);
        }
        xw.WriteAttributeString(p_DivisionName, DivisionName);

        xw.WriteEndElement(p_EventProps);
    }

    public void ReadXml(Node n) {
        NamedNodeMap kids = n.getAttributes();
        for (int k = 0; k < kids.getLength(); k++) {
            Node n2 = kids.item(k);
            String xname = n2.getNodeName();
            String xvalue = n2.getNodeValue();

            if (xname.equals(p_ScoringSystem)) {
                ScoringSystem = ScoringUtils.StrToIntDef(xvalue, 0);
            } else if (xname.equals(p_ThrowoutScheme)) {
                ThrowoutScheme = ScoringUtils.StrToIntDef(xvalue, 0);
            } else if (xname.equals(p_Throwouts)) {
                Throwouts = ScoringUtils.StrToIntDef(xvalue, 0);
            } else if (xname.equals(p_FirstIs75)) {
                FirstIs75 = ScoringUtils.IsTrue(xvalue);
            } else if (xname.equals(p_ReorderRAF)) {
                ReorderRAF = ScoringUtils.IsTrue(xvalue);
            } else if (xname.equals(p_DivisionName)) {
                DivisionName = xvalue;
            }
        }
    }

}
