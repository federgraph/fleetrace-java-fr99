package org.riggvar.eventmenu;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TEventMenuImpl implements IEventMenu {

    class X extends TXmlDocumentParser {
    }

    class BtnInfo {
        public String Data;
        public String Img;
        public String Text;
    }

    protected String String_Empty = "";

    public String root;
    public String comboCaption;

    List<BtnInfo> Info;

    public TEventMenuImpl() {
        Info = new ArrayList<BtnInfo>();
    }

    @Override
    public int getCount() {
        return Info.size();
    }

    @Override
    public String GetCaption(int i) {
        if (i <= 0)
            return "no selection";
        else if (i > 0 && i <= getCount())
            return Info.get(i - 1).Text;
        else
            return "B" + i;
    }

    @Override
    public String GetDataUrl(int i) {
        if (i > 0 && i <= getCount())
            return Info.get(i - 1).Data;
        else
            return String_Empty;
    }

    @Override
    public String GetImageUrl(int i) {
        if (i > 0 && i <= getCount())
            return Info.get(i - 1).Img;
        else
            return String_Empty;
    }

    @Override
    public void Load(String data) {
        Node xe = TXmlDocumentParser.GetNode(data);
        String r = ParseRoot(xe);
        Node xa = X.ReadElement(xe, "ComboEntry");
        if (xa != null)
            ParseComboEntry(xa, r);
    }

    @Override
    public String getComboCaption() {
        return comboCaption;
    }

    @Override
    public boolean isMock() {
        return false;
    }

    public static String ParseRoot(Node xe) {
        return X.ReadAttribute(xe, "Root");
    }

    public void ParseComboEntry(Node xe, String r) {
        try {
            root = r;
            comboCaption = X.ReadAttribute(xe, "Caption");
            Node xa = X.ReadElement(xe, "DataFolder");
            if (xa != null)
                ParseDataFolder(xa);
        } catch (Exception ex) {
            Info.clear();
            ReportError(ex);
        }
    }

    private void ParseDataFolder(Node xe) {
        if (xe == null)
            return;

        BtnInfo bi;
        String DataUri;
        String ImgUri = null;

        DataUri = BuildUri(X.ReadAttribute(xe, "Url"));

        NodeList A = xe.getChildNodes();
        for (int a = 0; a < A.getLength(); a++) {
            Node xa = A.item(a);
            if (xa.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String na = xa.getNodeName();
            if (na.equalsIgnoreCase("ImgFolder")) {
                ImgUri = BuildUri(X.ReadAttribute(xa, "Url"));

                NodeList B = xa.getChildNodes();
                for (int b = 0; b < B.getLength(); b++) {
                    Node xb = B.item(b);
                    String nb = xb.getNodeName();
                    if (!nb.equals("Btn"))
                        continue;
                    bi = new BtnInfo();
                    bi.Text = X.ReadAttribute(xb, "Text");
                    bi.Data = DataUri + X.ReadAttribute(xb, "Data");
                    if (ImgUri != null)
                        bi.Img = ImgUri + X.ReadAttribute(xb, "Img");
                    Info.add(bi);
                }
            }
        }

    }

    private String BuildUri(String s) {
        if (!s.isEmpty()) {
            if (root.isEmpty() || s.startsWith("http://"))
                return s;
            else
                return root + s;
        }
        return null;
    }

    private void ReportError(Exception ex) {

    }

}
