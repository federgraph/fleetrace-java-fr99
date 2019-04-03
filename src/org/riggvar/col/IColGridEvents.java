package org.riggvar.col;

public interface IColGridEvents<G extends TColGrid<G, B, N, C, I, PC, PI>, B extends TBaseColBO<G, B, N, C, I, PC, PI>, N extends TBaseNode<G, B, N, C, I, PC, PI>, C extends TBaseRowCollection<G, B, N, C, I, PC, PI>, I extends TBaseRowCollectionItem<G, B, N, C, I, PC, PI>, PC extends TBaseColProps<G, B, N, C, I, PC, PI>, PI extends TBaseColProp<G, B, N, C, I, PC, PI>> {
    public N OnGetBaseNode();

    public void OnTrace(String s); // for debugging only

    public void OnEdit(Object Sender);

    public void OnClearContent(Object Sender);

    public void OnMarkRow(Object Sender);

    public void OnCellClick(Object Sender, int ACol, int ARow);

    public void OnKeyDown(Object Sender, Integer Key, int myShift);

    public void OnSelectCell(Object Sender, int ACol, int ARow, Boolean CanSelect);

    public void OnFinishEditCR(I cr); // added July 2006
}
