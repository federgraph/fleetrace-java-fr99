package org.riggvar.col;

public class TBaseColBO<G extends TColGrid<G, B, N, C, I, PC, PI>, B extends TBaseColBO<G, B, N, C, I, PC, PI>, N extends TBaseNode<G, B, N, C, I, PC, PI>, C extends TBaseRowCollection<G, B, N, C, I, PC, PI>, I extends TBaseRowCollectionItem<G, B, N, C, I, PC, PI>, PC extends TBaseColProps<G, B, N, C, I, PC, PI>, PI extends TBaseColProp<G, B, N, C, I, PC, PI>> {

    public N CurrentNode;
    public I CurrentRow;

    public TBaseColBO() {
    }

    public N getCurrentNode() {
        return CurrentNode;
    }

    public void setCurrentNode(N value) {
        CurrentNode = value;
    }

    public I getCurrentRow() {
        return CurrentRow;
    }

    public void setCurrentRow(I value) {
        CurrentRow = value;
    }

    public void InitColsActive(G StringGrid) {
        InitColsActiveLayout(StringGrid, 0);
    }

    public void InitColsActiveLayout(G StringGrid, int aLayout) {
        // virtual;
    }
}
