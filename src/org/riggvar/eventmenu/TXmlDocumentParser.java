package org.riggvar.eventmenu;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TXmlDocumentParser {

    public static Node GetNode(String data) {
        Reader reader = new StringReader(data);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(reader);
            Document doc = builder.parse(is);
            Node xe = doc.getDocumentElement();
            return xe;
        } catch (IOException e) {
            System.err.println("cannot parse xml-String");
            System.err.println(e.getMessage());
        } catch (ParserConfigurationException e) {
            System.err.println("cannot parse xml - ParserConfigurationException");
            System.err.println(e.getMessage());
        } catch (SAXException e) {
            System.err.println("cannot parse xml - SAXException");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static String ReadAttribute(Node n, String an) {
        NamedNodeMap A = n.getAttributes();
        Node xa = A.getNamedItem(an);
        if (xa != null)
            return xa.getNodeValue();
        else
            return "";
    }

    public static Node ReadElement(Node n, String an) {
        NodeList A = n.getChildNodes();
        for (int a = 0; a < A.getLength(); a++) {
            Node xa = A.item(a);
            if (xa.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String na = xa.getNodeName();
            if (!na.equalsIgnoreCase(an)) {
                continue;
            }
            return xa;
        }
        return null;
    }

    public static List<Node> Descendants(Node n, String an) {
        List<Node> result = new ArrayList<Node>();
        NodeList A = n.getChildNodes();
        for (int a = 0; a < A.getLength(); a++) {
            Node xa = A.item(a);
            if (xa.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String na = xa.getNodeName();
            if (na.equalsIgnoreCase(an)) {
                result.add(xa);
            }
        }
        return result;
    }

}