package org.riggvar.col;

public class TBaseNode<G extends TColGrid<G, B, N, C, I, PC, PI>, B extends TBaseColBO<G, B, N, C, I, PC, PI>, N extends TBaseNode<G, B, N, C, I, PC, PI>, C extends TBaseRowCollection<G, B, N, C, I, PC, PI>, I extends TBaseRowCollectionItem<G, B, N, C, I, PC, PI>, PC extends TBaseColProps<G, B, N, C, I, PC, PI>, PI extends TBaseColProp<G, B, N, C, I, PC, PI>> {
    public B BO;
    private C fCollection;
    private boolean fModified;
    public String NameID;
    public int Layout;

    public TBaseNode() {
    }

    public B getBaseColBO() {
        return BO;
    }

    public String getNameID() {
        return NameID;
    }

    public boolean getModified() {
        return fModified;
    }

    public void setModified(boolean value) {
        fModified = value;
    }

    public void setModified() {
        fModified = true;
    }

    public C getBaseRowCollection() {
        return fCollection;
    }

    public void setBaseRowCollection(C value) {
        fCollection = value;
    }

    public void Calc() {
    }

}
