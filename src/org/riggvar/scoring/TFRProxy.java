package org.riggvar.scoring;

import java.io.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TFRProxy extends TProxyProps {
    public TJSEventProps EventProps;

    /**
     * RaceProps, at this time not more than a boolean (for each race); RCount
     * elements, element zero not used, one element for each real race, starting at
     * index 1.
     */
    public boolean[] IsRacing;
    public TEntryInfoCollection EntryInfoCollection;

    public boolean UseFleets;
    public int TargetFleetSize;
    public int FirstFinalRace;

    /**
     * Count of Entries with at least one race-result
     */
    public int Gezeitet;

    /**
     * contains ErrorCode, e.g. -1 if exception occurred when calculating results
     */
    public int FResult;

    public TFRProxy() {
        EventProps = new TJSEventProps();
        IsRacing = new boolean[1];
        EntryInfoCollection = new TEntryInfoCollection();
    }

    public void Clear() {
        EventProps.Clear();
        IsRacing = new boolean[1];
        EntryInfoCollection = new TEntryInfoCollection();
        Gezeitet = 0;
        FResult = 0;
        UseFleets = false;
        TargetFleetSize = 8;
        FirstFinalRace = 20;
    }

    public void Assign(TFRProxy p) {
        EventProps.Assign(p.EventProps);

        IsRacing = new boolean[p.getRCount()];
        for (int i = 0; i < getRCount(); i++) {
            IsRacing[i] = p.IsRacing[i];
        }

        EntryInfoCollection.Clear();
        for (int i = 0; i < p.EntryInfoCollection.Count(); i++) {
            TEntryInfo ei = EntryInfoCollection.Add();
            ei.Assign(p.EntryInfoCollection.Items(i));
        }

        UseFleets = p.UseFleets;
        TargetFleetSize = p.TargetFleetSize;
        FirstFinalRace = p.FirstFinalRace;

        Gezeitet = p.Gezeitet;
        FResult = p.FResult;
    }

    public boolean Equals(TFRProxy p) {
        if (getRCount() != p.getRCount()) {
            return false;
        }
        if (!EventProps.Equals(p.EventProps)) {
            return false;
        }
        if (Gezeitet != p.Gezeitet) {
            return false;
        }
        if (FResult != p.FResult) {
            return false;
        }
        if (UseFleets != p.UseFleets)
            return false;
        if (TargetFleetSize != p.TargetFleetSize)
            return false;
        if (FirstFinalRace != p.FirstFinalRace)
            return false;

        if (!EntryInfoCollection.Equals(p.EntryInfoCollection)) {
            return false;
        }
        for (int i = 0; i < getRCount(); i++) {
            if (IsRacing[i] != p.IsRacing[i]) {
                return false;
            }
        }
        return true;
    }

    public void WriteXml(XmlWriter xw) {
        xw.WriteStartElement(p_FRProxy);

        xw.WriteAttributeString(p_Gezeitet, Gezeitet);
        if (FResult != 0) {
            xw.WriteAttributeString(p_FResult, FResult);
        }
        if (UseFleets == true) {
            xw.WriteAttributeString(p_UseFleets, UseFleets);
            xw.WriteAttributeString(p_TargetFleetSize, TargetFleetSize);
            xw.WriteAttributeString(p_FirstFinalRace, FirstFinalRace);
        }
        EventProps.WriteXml(xw);

        xw.WriteStartElement(p_RaceProps);
        xw.WriteAttributeString(p_RCount, getRCount());
        for (int i = 0; i < getRCount(); i++) {
            if (!IsRacing[i]) {
                xw.WriteStartElement(p_Race);
                xw.WriteAttributeString(p_Index, i);
                xw.WriteAttributeString(p_IsRacing, false);
                xw.WriteEndElement(p_Race);
            }
        }
        xw.WriteEndElement(p_RaceProps);

        for (int i = 0; i < EntryInfoCollection.Count(); i++) {
            EntryInfoCollection.Items(i).WriteXml(xw);
        }

        xw.WriteEndElement(p_FRProxy);
    }

    public int getRCount() {
        return IsRacing.length;
    }

    public void setRCount(int value) {
        IsRacing = new boolean[value];
        for (int i = 1; i < value; i++) {
            IsRacing[i] = true;
        }
    }

    public void ReadXml(Node n) {
        NamedNodeMap kids = n.getAttributes();
        for (int k = 0; k < kids.getLength(); k++) {
            Node n2 = kids.item(k);
            String xname = n2.getNodeName();
            String xvalue = n2.getNodeValue();
            if (xname.equalsIgnoreCase(p_Gezeitet)) {
                Gezeitet = ScoringUtils.StrToIntDef(xvalue, 0);
            } else if (xname.equalsIgnoreCase(p_FResult)) {
                FResult = ScoringUtils.StrToIntDef(xvalue, -1);
            }
        }
    }

    public void ReadFromString(String xml) {
        xmlReadFromReader(new StringReader(xml));
    }

    public void ReadFromFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        Reader reader;
        try {

            reader = new FileReader(file);
        } catch (FileNotFoundException ex) {
            Clear();
            return;
        }
        xmlReadFromReader(reader);
    }

    public void XmlRead(Node n) {
        Clear();

        NodeList kids2 = n.getChildNodes();
        for (int k = 0; k < kids2.getLength(); k++) {
            Node n2 = kids2.item(k);
            if (n2.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String xname = n2.getNodeName();
            if (xname.equalsIgnoreCase(p_FRProxy)) {
                ReadXml(n2);
            } else if (xname.equalsIgnoreCase(p_EventProps)) {
                EventProps.ReadXml(n2);
            } else if (xname.equalsIgnoreCase(p_RaceProps)) {
                RaceProps_ReadXml(n2);
            } else if (xname.equalsIgnoreCase(p_Entry)) {
                TEntryInfo ei = EntryInfoCollection.Add();
                ei.ReadXml(n2);
            }
        }
    }

    public void RaceProps_ReadXml(Node n) {
        NamedNodeMap kids = n.getAttributes();
        for (int k = 0; k < kids.getLength(); k++) {
            Node n2 = kids.item(k);
            String xname = n2.getNodeName();
            String xvalue = n2.getNodeValue();
            if (xname.equalsIgnoreCase(p_RCount)) {
                setRCount(ScoringUtils.StrToIntDef(xvalue, 0));
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
                RaceProp_ReadXml(n2);
            }
        }
    }

    public void RaceProp_ReadXml(Node n) {
        int index = -1;
        boolean isRacing = false;
        NamedNodeMap kids = n.getAttributes();
        for (int k = 0; k < kids.getLength(); k++) {
            Node n2 = kids.item(k);
            String xname = n2.getNodeName();
            String xvalue = n2.getNodeValue();

            if (xname.equalsIgnoreCase(p_Index)) {
                index = ScoringUtils.StrToIntDef(xvalue, 0);
            }
            if (xname.equalsIgnoreCase(p_IsRacing)) {
                isRacing = ScoringUtils.IsTrue(xvalue);
                setIsRacing(index, isRacing);
            }
        }
    }

    private void setIsRacing(int index, boolean value) {
        if (index > 0 && index < IsRacing.length) {
            IsRacing[index] = value;
        }
    }

    public void xmlReadFromReader(Reader reader) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(reader);
            Document doc = builder.parse(is);
            Element root = doc.getDocumentElement();
            XmlRead(root);
        } catch (IOException e) {
            Clear();
            System.err.println("cannot parse xml-String");
            System.err.println(e.getMessage());
        } catch (ParserConfigurationException e) {
            Clear();
            System.err.println("cannot parse xml - ParserConfigurationException");
            System.err.println(e.getMessage());
        } catch (SAXException e) {
            Clear();
            System.err.println("cannot parse xml - SAXException");
            System.err.println(e.getMessage());
        }

    }

}
