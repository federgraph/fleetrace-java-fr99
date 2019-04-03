package org.riggvar.scoring;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class TRaceInfo extends TProxyProps {
    // Input
    public int OTime; // Original Time/FinishPosition
    public int QU; // Encoded Penalty Type ('QUit Packet')
    public double Penalty_Points;
    public String Penalty_Note;
    public int Penalty_Percent;
    public long Penalty_TimePenalty;
    public int Fleet;
    public boolean IsRacing;

    // Output
    public double CPoints; // Calculated Points
    public int Rank;
    public int PosR; // unique Ranking
    public int PLZ; // PlatzZiffer
    public boolean Drop; // IsThrowout

    public TRaceInfo() {
    }

    public void Assign(TRaceInfo ri) {
        // Input
        OTime = ri.OTime;
        QU = ri.QU;
        Penalty_Points = ri.Penalty_Points;
        Penalty_Note = ri.Penalty_Note;
        Penalty_Percent = ri.Penalty_Percent;
        Penalty_TimePenalty = ri.Penalty_TimePenalty;
        Fleet = ri.Fleet;
        IsRacing = ri.IsRacing;

        // Output
        // intentionally not copied
    }

    public boolean Equals(TRaceInfo ri) {
        if (OTime != ri.OTime) {
            return false;
        }
        if (QU != ri.QU) {
            return false;
        }
        if (Math.abs(Penalty_Points - ri.Penalty_Points) > 0.00001) {
            return false;
        }
        if (!Penalty_Note.equals(ri.Penalty_Note)) {
            return false;
        }
        if (Penalty_Percent != ri.Penalty_Percent) {
            return false;
        }
        if (Penalty_TimePenalty != ri.Penalty_TimePenalty) {
            return false;
        }
        if (Fleet != ri.Fleet)
            return false;
        if (IsRacing != ri.IsRacing)
            return false;

        // Output
        if (Math.abs(CPoints - ri.CPoints) > 0.00001) {
            return false;
        }
        if (Rank != ri.Rank) {
            return false;
        }
        if (PosR != ri.PosR) {
            return false;
        }
        if (PLZ != ri.PLZ) {
            return false;
        }
        if (Drop != ri.Drop) {
            return false;
        }

        return true;
    }

    public void WriteXml(XmlWriter xw, int Index) {
        xw.WriteStartElement(p_Race);

        xw.WriteAttributeString(p_Index, Index);

        // Input
        if (OTime != 0) {
            xw.WriteAttributeString(p_OTime, OTime);
        }
        if (QU != 0) {
            xw.WriteAttributeString(p_QU, QU);
        }
        if (Penalty_Points != 0) {
            xw.WriteAttributeString(p_Penalty_Points, Penalty_Points);
        }
        if (Penalty_Note != null && (!Penalty_Note.equals(""))) {
            xw.WriteAttributeString(p_Penalty_Note, Penalty_Note);
        }
        if (Penalty_Percent != 0) {
            xw.WriteAttributeString(p_Penalty_Percent, Penalty_Percent);
        }
        if (Penalty_TimePenalty != 0) {
            xw.WriteAttributeString(p_Penalty_Time, Penalty_TimePenalty);
        }
        if (Fleet != 0)
            xw.WriteAttributeString(p_Fleet, Fleet);
        if (IsRacing != true)
            xw.WriteAttributeString(p_IsRacing, IsRacing); // default true

        // Output
        xw.WriteAttributeString(p_CPoints, CPoints);
        if (Rank != 0) {
            xw.WriteAttributeString(p_Rank, Rank);
        }
        if (PosR != Rank) {
            xw.WriteAttributeString(p_PosR, PosR);
        }
        if (Index == 0) {
            xw.WriteAttributeString(p_PLZ, PLZ);
        }
        if (Drop) {
            xw.WriteAttributeString(p_Drop, Drop);

        }
        xw.WriteEndElement(p_Race);
    }

    public void XmlRead(Node n) {
        NamedNodeMap kids = n.getAttributes();
        for (int k = 0; k < kids.getLength(); k++) {
            Node n2 = kids.item(k);
            String xname = n2.getNodeName();
            String xvalue = n2.getNodeValue();

            // Input
            if (xname.equalsIgnoreCase(p_OTime)) {
                OTime = ScoringUtils.StrToIntDef(xvalue, 0);
            } else if (xname.equalsIgnoreCase(p_QU)) {
                QU = ScoringUtils.StrToIntDef(xvalue, 0);
            } else if (xname.equalsIgnoreCase(p_Penalty_Points)) {
                xvalue = xvalue.replace(',', '.');
                Penalty_Points = Double.parseDouble(xvalue);
            } else if (xname.equalsIgnoreCase(p_Penalty_Note)) {
                Penalty_Note = xvalue;
            } else if (xname.equalsIgnoreCase(p_Penalty_Percent)) {
                Penalty_Percent = ScoringUtils.StrToIntDef(xvalue, 0);
            } else if (xname.equalsIgnoreCase(p_Penalty_Time)) {
                Penalty_TimePenalty = ScoringUtils.StrToIntDef(xvalue, 0);
            } else if (xname.equalsIgnoreCase(p_Fleet))
                Fleet = ScoringUtils.StrToIntDef(xvalue, 0);
            else if (xname.equalsIgnoreCase(p_IsRacing))
                IsRacing = ScoringUtils.IsTrue(xvalue);

            // Output
            if (xname.equalsIgnoreCase(p_CPoints)) {
                xvalue = xvalue.replace(',', '.');
                CPoints = Double.parseDouble(xvalue);
            } else if (xname.equalsIgnoreCase(p_Rank)) {
                Rank = ScoringUtils.StrToIntDef(xvalue, 0);
                PosR = Rank;
            } else if (xname.equalsIgnoreCase(p_PosR)) {
                PosR = ScoringUtils.StrToIntDef(xvalue, 0); // read after Rank!
            } else if (xname.equalsIgnoreCase(p_PLZ)) {
                PLZ = ScoringUtils.StrToIntDef(xvalue, 0);
            } else if (xname.equalsIgnoreCase(p_Drop)) {
                Drop = ScoringUtils.IsTrue(xvalue);
            }
        }
    }

}
