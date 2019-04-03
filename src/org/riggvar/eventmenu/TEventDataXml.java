package org.riggvar.eventmenu;

import java.util.List;

import org.riggvar.base.Utils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class TEventDataXml {

    class X extends TXmlDocumentParser {
    }

    private StringBuilder sb = null;
    String crlf = "\r\n";
    private int RaceCount = 0;
    public String EventName = "";

    public String TransformEventData(String EventData) {
        Node xe = X.GetNode(EventData);
        StringBuilder sb = new StringBuilder();
        TransformEventData(xe, sb);
        return sb.toString();
    }

    public void TransformEventData(Node xe, StringBuilder stringBuilder) {
        sb = stringBuilder;
        ExtractEventName(xe);
        ExtractParams(xe);
        ExtractProperties(xe);
        ExtractNameList(xe);
        ExtractStartList(xe);
        ExtractFleetList(xe);
        ExtractFinishList(xe);
        for (int r = 1; r <= RaceCount; r++)
            ExtractTimeList(xe, "R" + r);
        ExtractMsgList(xe);
        sb = null;
    }

    private void ExtractEventName(Node xe) {
        // EventName must be in first EP Element
        Node e;
        e = X.ReadElement(xe, "Properties");
        if (e != null) {
            e = X.ReadElement(e, "EP");
            if (e != null) {
                EventName = X.ReadAttribute(e, "V");
            }
        }
    }

    private void ExtractMsgList(Node xe) {
        sb.append(crlf);
        sb.append("#QU Messages");
        sb.append(crlf);
        sb.append(crlf);
        Node A = X.ReadElement(xe, "MsgList");
        List<Node> cl = X.Descendants(A, "ML");
        for (Node cr : cl) {
            sb.append(cr.getTextContent());
            sb.append(crlf);
        }
    }

    private void ExtractTimeList(Node xe, String RaceID) {
        List<Node> el = X.Descendants(xe, "TimeList");
        for (Node e : el) {
            if (X.ReadAttribute(e, "RaceID").equals(RaceID)) {
                sb.append(crlf);
                sb.append("TimeList.Begin.");
                sb.append(RaceID);
                sb.append(crlf);
                List<Node> cl = X.Descendants(e, "TL");
                for (Node cr : cl) {
                    sb.append(cr.getTextContent());
                    sb.append(crlf);
                }
                sb.append("TimeList.End");
                sb.append(crlf);
                break;
            }
        }
    }

    private void ExtractFinishList(Node xe) {
        sb.append(crlf);
        sb.append("FinishList.Begin");
        sb.append(crlf);
        Node A = X.ReadElement(xe, "FinishList");
        List<Node> cl = X.Descendants(A, "FL");
        for (Node cr : cl) {
            sb.append(cr.getTextContent());
            sb.append(crlf);
        }
        sb.append("FinishList.End");
        sb.append(crlf);
    }

    private void ExtractFleetList(Node xe) {
        if (X.ReadElement(xe, "FleetList") != null) {
            sb.append(crlf);
            sb.append("FleetList.Begin");
            sb.append(crlf);
            Node A = X.ReadElement(xe, "FleetList");
            List<Node> cl = X.Descendants(A, "FL");
            for (Node cr : cl) {
                sb.append(cr.getTextContent());
                sb.append(crlf);
            }
            sb.append("FleetList.End");
            sb.append(crlf);
        }
    }

    private void ExtractParams(Node xe) {
        Node cr = X.ReadElement(xe, "Properties");

        sb.append("#Params");
        sb.append(crlf);
        sb.append(crlf);

        sb.append("DP.StartlistCount=");
        sb.append(X.ReadAttribute(cr, "StartlistCount"));
        sb.append(crlf);

        sb.append("DP.ITCount=");
        sb.append(X.ReadAttribute(cr, "ITCount"));
        sb.append(crlf);

        sb.append("DP.RaceCount=");
        RaceCount = Utils.StrToIntDef(X.ReadAttribute(cr, "RaceCount"), RaceCount);
        sb.append(RaceCount);
        sb.append(crlf);
    }

    private void ExtractProperties(Node xe) {
        Node ce = X.ReadElement(xe, "Properties");

        sb.append(crlf);
        sb.append("#Event Properties");
        sb.append(crlf);
        sb.append(crlf);

//        sb.append("EP.DivisionName=");
//        sb.append(X.ReadAttribute(ce, "DivisionName"));
//        sb.append(crlf);
//
//        sb.append("EP.InputMode=");
//        sb.append(X.ReadAttribute(ce, "InputMode"));
//        sb.append(crlf);

        List<Node> cl = X.Descendants(ce, "EP");
        for (Node cr : cl) {
            sb.append("EP.");
            sb.append(X.ReadAttribute(cr, "K"));
            sb.append('=');
            sb.append(X.ReadAttribute(cr, "V"));
            sb.append(crlf);
        }
    }

    private void ExtractStartList(Node xe) {
        sb.append(crlf);
        sb.append("StartList.Begin");
        sb.append(crlf);
        sb.append("Pos;SNR;Bib");
        Node A = X.ReadElement(xe, "StartList");
        List<Node> cl = X.Descendants(A, "Pos");
        for (Node cr : cl) {
            sb.append(crlf);
            sb.append(X.ReadAttribute(cr, "oID"));
            sb.append(';');
            sb.append(X.ReadAttribute(cr, "SNR"));
            sb.append(';');
            sb.append(X.ReadAttribute(cr, "Bib"));
        }
        sb.append(crlf);
        sb.append("StartList.End");
        sb.append(crlf);
    }

    private void ExtractNameList(Node xe) {
        boolean isFirstLine;
        boolean isFirstNode;
        NamedNodeMap cl;
        String k;
        String v;

        sb.append(crlf);
        sb.append("NameList.Begin");
        Node A = X.ReadElement(xe, "Entries");
        if (A == null)
            return;
        List<Node> B = X.Descendants(A, "SNR");

        isFirstLine = true;
        for (Node C : B) {
            sb.append(crlf);
            cl = C.getAttributes();

            if (isFirstLine) {
                isFirstLine = false;
                sb.append("SNR");
                for (int i = 1; i < cl.getLength(); i++) {
                    sb.append(";N");
                    sb.append(i);
                }
                sb.append(crlf);
            }

            isFirstNode = true;
            for (int i = 0; i < cl.getLength(); i++) {
                if (isFirstNode) {
                    isFirstNode = false;
                    k = "oID";
                    v = cl.getNamedItem(k).getNodeValue();
                    sb.append(v);
                    continue;
                } else {
                    sb.append(';');
                    k = "N" + i;
                    v = cl.getNamedItem(k).getNodeValue();
                    if (v.contains(" ")) {
                        sb.append('"');
                        sb.append(v);
                        sb.append('"');
                    } else {
                        sb.append(v);
                    }
                }
            }
        }
        sb.append(crlf);
        sb.append("NameList.End");
        sb.append(crlf);
    }

}
