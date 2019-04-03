package org.riggvar.eventmenu;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TEventCombo {
    private boolean hasMock;
    private List<TEventMenuImpl> menuCollection;
    public int index = 0;

    boolean LoadingCompleted;

    public TEventCombo() {
        menuCollection = new ArrayList<TEventMenuImpl>();
    }

    public List<TEventMenuImpl> MenuCollection() {
        return menuCollection;
    }

    public int ComboCount() {
        return MenuCollection().size();
    }

    public int getMenuIndex() {
        return index;
    }

    public void setMenuIndex(int value) {
        if (value >= 0 && value < MenuCollection().size())
            index = value;
    }

    public TEventMenuImpl CurrentMenu() {
        if (MenuCollection().size() == 0) {
            MenuCollection().add(new TEventMenuImpl());
            hasMock = true;
            index = 0;
        }
        return MenuCollection().get(index);
    }

    String getComboCaption() {
        return CurrentMenu().getComboCaption();
    }

    int getCount() {
        return CurrentMenu().getCount();
    }

    String getCaption(int i) {
        return CurrentMenu().GetCaption(i);
    }

    String GetImageUrl(int i) {
        return CurrentMenu().GetImageUrl(i);
    }

    String GetDataUrl(int i) {
        return CurrentMenu().GetDataUrl(i);
    }

    boolean isMock() {
        return hasMock;
    }

    void Load(String data) {
        try {
            hasMock = false;
            Clear();
            Node xe = TXmlDocumentParser.GetNode(data);
            String root = TEventMenuImpl.ParseRoot(xe);

            NodeList A = xe.getChildNodes();
            for (int a = 0; a < A.getLength(); a++) {
                Node xa = A.item(a);
                if (xa.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                String na = xa.getNodeName();
                if (!na.equalsIgnoreCase("ComboEntry")) {
                    continue;
                }

                TEventMenuImpl em = new TEventMenuImpl();
                em.ParseComboEntry(xa, root);
                MenuCollection().add(em);
            }
            LoadingCompleted = true;
        } catch (Exception ex) {
            error = ex.getMessage();
        }
    }

    String error;

    void Clear() {
        MenuCollection().clear();
        index = 0;
    }

}
