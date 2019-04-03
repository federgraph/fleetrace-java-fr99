package org.riggvar.scoring;

import java.util.*;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TEntryInfo extends TProxyProps {
    public int Index; // Index in EntryInfoCollection
    public int SNR; // Primary Key (SportlerNummer/SailNumber)
    public boolean IsGezeitet = true;
    public boolean IsTied;
    public ArrayList<TRaceInfo> RaceList;

    public TRaceInfo Race(int i) {
        return RaceList.get(i);
    }

    public TEntryInfo() {
        RaceList = new ArrayList<TRaceInfo>();
    }

    public void Assign(TEntryInfo ei) {
        // FIndex intentionally not copied
        SNR = ei.SNR;
        IsGezeitet = ei.IsGezeitet;
        IsTied = ei.IsTied;

        RaceList.clear();
        for (int i = 0; i < ei.RCount(); i++) {
            TRaceInfo ri = new TRaceInfo();
            RaceList.add(ri);
            ri.Assign(ei.Race(i));
        }
    }

    public boolean Equals(TEntryInfo ei) {
        if (Index != ei.Index) {
            return false;
        }
        if (SNR != ei.SNR) {
            return false;
        }
        if (IsGezeitet != ei.IsGezeitet) {
            return false;
        }
        if (IsTied != ei.IsTied) {
            return false;
        }
        if (RCount() != ei.RCount()) {
            return false;
        }
        for (int i = 0; i < ei.RCount(); i++) {
            if (!Race(i).Equals(ei.Race(i))) {
                return false;
            }
        }
        return true;
    }

    public void WriteXml(XmlWriter xw) {
        xw.WriteStartElement(p_Entry);

        xw.WriteAttributeString(p_Index, Index);
        xw.WriteAttributeString(p_SNR, SNR);
        if (!IsGezeitet) {
            xw.WriteAttributeString(p_IsGezeitet, IsGezeitet);
        }
        if (IsTied) {
            xw.WriteAttributeString(p_IsTied, true);
        }
        for (int i = 0; i < RCount(); i++) {
            Race(i).WriteXml(xw, i);
        }

        xw.WriteEndElement(p_Entry);
    }

    public void ReadXml(Node n) {
        NamedNodeMap kids = n.getAttributes();
        for (int k = 0; k < kids.getLength(); k++) {
            Node n2 = kids.item(k);
            String xname = n2.getNodeName();
            String xvalue = n2.getNodeValue();

            if (xname.equalsIgnoreCase(p_SNR)) {
                SNR = ScoringUtils.StrToIntDef(xvalue, 0);
            } else if (xname.equalsIgnoreCase(p_IsGezeitet)) {
                IsGezeitet = ScoringUtils.IsTrue(xvalue);
            } else if (xname.equalsIgnoreCase(p_IsTied)) {
                IsTied = ScoringUtils.IsTrue(xvalue);
            }
        }

        NodeList kids2 = n.getChildNodes();
        for (int k = 0; k < kids2.getLength(); k++) {
            Node n2 = kids2.item(k);
            if (n2.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String xname = n2.getNodeName();
            if (xname.equalsIgnoreCase(p_Race)) {
                TRaceInfo ri = new TRaceInfo();
                RaceList.add(ri);
                ri.XmlRead(n2);
            }
        }
    }

    public int RaceCount() {
        return RCount() - 1;
    }

    public int RCount() {
        return RaceList.size();
    }

}
