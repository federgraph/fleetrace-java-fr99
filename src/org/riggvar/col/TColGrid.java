package org.riggvar.col;

import java.awt.*;
import org.riggvar.base.*;

public abstract class TColGrid<G extends TColGrid<G, B, N, C, I, PC, PI>, B extends TBaseColBO<G, B, N, C, I, PC, PI>, N extends TBaseNode<G, B, N, C, I, PC, PI>, C extends TBaseRowCollection<G, B, N, C, I, PC, PI>, I extends TBaseRowCollectionItem<G, B, N, C, I, PC, PI>, PC extends TBaseColProps<G, B, N, C, I, PC, PI>, PI extends TBaseColProp<G, B, N, C, I, PC, PI>> {
    public IColGrid Grid = null;

    protected TCellProps fCellProps = null;

    // private B fColBODefault;
    private B fColBO; // reference, don't create

    private PC fColsActiveDefault;
    private PC fColsAvail; // use assign
    private PC fColsActive; // reference, don't create

    private TDisplayOrderList fDisplayOrder;

    private IColGridGetBaseNode<G, B, N, C, I, PC, PI> fOnGetBaseNode;

    protected IColGridEvents<G, B, N, C, I, PC, PI> fOnTrace;

    private IColGridEvents<G, B, N, C, I, PC, PI> fOnBaseEdit;
    private IColGridEvents<G, B, N, C, I, PC, PI> fOnBaseClearContent;
    private IColGridEvents<G, B, N, C, I, PC, PI> fOnMarkRow;
    private IColGridEvents<G, B, N, C, I, PC, PI> fOnBaseKeyDown;
    private IColGridEvents<G, B, N, C, I, PC, PI> fOnBaseSelectCell;
    private IColGridEvents<G, B, N, C, I, PC, PI> fOnCellClick;
    private IColGridEvents<G, B, N, C, I, PC, PI> fOnFinishEditCR;

    private int fColorSchema = TBaseGridColorSchema.colorRed;

    private Color fDefaultColor;
    private Color fAlternatingColor;
    private Color fFocusColor;
    private Color fEditableColor;
    private Color fAlternatingEditableColor;
    private Color fCurrentColor;
    private Color fTransColor;

    private boolean fAlwaysShowCurrent;
    private boolean fAutoMark;
    private boolean fAutoInsert;
    private boolean fCellsBold;
    private boolean fHeaderBold = true;
    private boolean fUseHTML;
    private boolean fMenuMode;
    private boolean fExcelStyle;

    private int fHeaderRowIndex;
    private int fHeatSize = 1;
    private String fName = "";

    public TColGrid(IColGrid aGrid) {
        Grid = aGrid;
        setColorSchema(TBaseGridColorSchema.colorRed);
        fCellProps = new TCellProps();
        fDisplayOrder = new TDisplayOrderList();

        // fColBODefault = getColBOInstance();
        // fColBO = fColBODefault;

        fColsAvail = newPC();
        fColsAvail.ColGrid = this;
        // fColsAvail.Init();

        fColsActiveDefault = newPC();
        fColsActiveDefault.ColGrid = this;
        fColsActive = fColsActiveDefault;

        AddColumn("col_BaseID");
    }

    protected abstract PC newPC();

    protected abstract B getColBOInstance();

    public int StrToIntDef(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    public int StrToIntDefEmpty(String s, int def) {
        try {
            if (s.equals(""))
                return def;
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    public boolean Odd(int i) {
        if (i % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }

    public N GetBaseNode() {
        if (fOnGetBaseNode != null) {
            return fOnGetBaseNode.ColGridGetBaseNode();
        }
        return null;
    }

    protected B getColBO() {
        return fColBO;
    }

    public int getHeaderRowIndex() {
        return fHeaderRowIndex;
    }

    public Color getFocusColor() {
        return fFocusColor;
    }

    public boolean getExcelStyle() {
        return fExcelStyle;
    }

    public void setExcelStyle(boolean value) {
        fExcelStyle = value;
    }

    public String getName() {
        return fName;
    }

    public void setName(String value) {
        fName = value;
    }

    public boolean getMenuMode() {
        return fMenuMode;
    }

    public void setMenuMode(boolean value) {
        fMenuMode = value;
        if (fMenuMode) {
            Grid.setHasFixedRow(false);
            fHeaderRowIndex = -1;
        } else {
            Grid.setHasFixedRow(true);
            fHeaderRowIndex = 0;
        }
    }

    public TDisplayOrderList getDisplayOrder() {
        return fDisplayOrder;
    }

    public boolean getUseHTML() {
        return fUseHTML;
    }

    public void setUseHTML(boolean value) {
        fUseHTML = value;
    }

    public int getHeatSize() {
        return fHeatSize;
    }

    public void setHeatSize(int value) {
        fHeatSize = value;
    }

    public boolean getAutoInsert() {
        return fAutoInsert;
    }

    public void setAutoInsert(boolean value) {
        fAutoInsert = value;
    }

    public boolean AutoMark() {
        return fAutoMark;
    }

    public void setAutoMark(boolean value) {
        fAutoMark = value;
    }

    public int getColorSchema() {
        return fColorSchema;
    }

    public void setColorSchema(int value) {
        if (value == TBaseGridColorSchema.colorMoneyGreen) {
            fColorSchema = TBaseGridColorSchema.colorMoneyGreen;
            fDefaultColor = TColGridColors.clCream;
            fAlternatingColor = TColGridColors.clMoneyGreen;
            fFocusColor = Color.yellow;
            fEditableColor = TColGridColors.clEditable;
            fAlternatingEditableColor = TColGridColors.clEditable;
            fCurrentColor = TColGridColors.clHellRot;
            fTransColor = TColGridColors.clTransRot;
        } else if (value == TBaseGridColorSchema.color256) {
            fColorSchema = TBaseGridColorSchema.color256;
            fDefaultColor = Color.white;
            fAlternatingColor = TColGridColors.clBtnFace;
            fFocusColor = TColGridColors.clTeal;
            fEditableColor = Color.yellow;
            fAlternatingEditableColor = Color.yellow;
            fCurrentColor = Color.red;
            fTransColor = Color.red;
        } else if (value == TBaseGridColorSchema.colorBlue) {
            fColorSchema = TBaseGridColorSchema.colorBlue;
            fDefaultColor = TColGridColors.clNormal;
            fAlternatingColor = TColGridColors.clAlternate;
            fFocusColor = TColGridColors.clLime;
            fEditableColor = TColGridColors.clEditable;
            fAlternatingEditableColor = TColGridColors.clEditable;
            fCurrentColor = TColGridColors.clHellBlau;
            fTransColor = TColGridColors.clTransBlau;
        } else {
            fColorSchema = TBaseGridColorSchema.colorRed;
            fDefaultColor = TColGridColors.clAlternate;
            fAlternatingColor = TColGridColors.clNormal;
            fFocusColor = TColGridColors.clLime;
            fEditableColor = TColGridColors.clEditable;
            fAlternatingEditableColor = TColGridColors.clEditable;
            fCurrentColor = TColGridColors.clHellRot;
            fTransColor = TColGridColors.clTransRot;
        }
    }

    public boolean getAlwaysShowCurrent() {
        return fAlwaysShowCurrent;
    }

    public void setAlwaysShowCurrent(boolean value) {
        fAlwaysShowCurrent = value;
    }

    public IColGridEvents<G, B, N, C, I, PC, PI> getOnCellClick() {
        return fOnCellClick;
    }

    public void setOnCellClick(IColGridEvents<G, B, N, C, I, PC, PI> value) {
        fOnCellClick = value;
    }

    public IColGridEvents<G, B, N, C, I, PC, PI> getOnBaseSelectCell() {
        return fOnBaseSelectCell;
    }

    public void setOnBaseSelectCell(IColGridEvents<G, B, N, C, I, PC, PI> value) {
        fOnBaseSelectCell = value;
    }

    public IColGridEvents<G, B, N, C, I, PC, PI> getOnBaseKeyDown() {
        return fOnBaseKeyDown;
    }

    public void setOnBaseKeyDown(IColGridEvents<G, B, N, C, I, PC, PI> value) {
        fOnBaseKeyDown = value;
    }

    public IColGridEvents<G, B, N, C, I, PC, PI> getOnMarkRow() {
        return fOnMarkRow;
    }

    public void setOnMarkRow(IColGridEvents<G, B, N, C, I, PC, PI> value) {
        fOnMarkRow = value;
    }

    public IColGridEvents<G, B, N, C, I, PC, PI> getOnBaseClearContent() {
        return fOnBaseClearContent;
    }

    public void setOnBaseClearContent(IColGridEvents<G, B, N, C, I, PC, PI> value) {
        fOnBaseClearContent = value;
    }

    public IColGridEvents<G, B, N, C, I, PC, PI> getOnBaseEdit() {
        return fOnBaseEdit;
    }

    public void setOnBaseEdit(IColGridEvents<G, B, N, C, I, PC, PI> value) {
        fOnBaseEdit = value;
    }

    public IColGridEvents<G, B, N, C, I, PC, PI> getOnTrace() {
        return fOnTrace;
    }

    public void setOnTrace(IColGridEvents<G, B, N, C, I, PC, PI> value) {
        fOnTrace = value;
    }

    public IColGridGetBaseNode<G, B, N, C, I, PC, PI> getOnGetBaseNode() {
        return fOnGetBaseNode;
    }

    public void setOnGetBaseNode(IColGridGetBaseNode<G, B, N, C, I, PC, PI> value) {
        fOnGetBaseNode = value;
    }

    public IColGridEvents<G, B, N, C, I, PC, PI> getOnFinishEditCR() {
        return fOnFinishEditCR;
    }

    public void setOnFinishEditCR(IColGridEvents<G, B, N, C, I, PC, PI> value) {
        fOnFinishEditCR = value;
    }

    public PC getColsAvail() {
        return fColsAvail;
    }

    public void setColsAvail(PC value) throws Exception {
        if (fColsAvail != value) {
            if (fColsAvail != null) {
                fColsAvail.Assign(value);
            }
        }
    }

    public boolean getCellsBold() {
        return fCellsBold;
    }

    public void setCellsBold(boolean value) {
        fCellsBold = value;
    }

    public boolean getHeaderBold() {
        return fHeaderBold;
    }

    public void setHeaderBold(boolean value) {
        fHeaderBold = value;
    }

    public boolean getColorPaint() {
        return fColsActive.getSortColIndex() == -1;
    }

    public void setColorPaint(boolean value) {
        if (value) {
            fColsActive.setSortColIndex(-1);
        } else {
            fColsActive.setSortColIndex(0);
        }
        UpdateAll();
    }

    public PC getColsActive() {
        return fColsActive;
    }

    public void setColPropClass() {
        fColsAvail.Init();
        AddColumn("col_BaseID");
    }

    public void SetColBOReference(B Value) {
//        if (Value != null)
//        {
        fColBO = Value;
//        }
//        else
//        {
//            fColBO = fColBODefault;
//        }
    }

    public void SetColsActiveReference(PC Value) {
        Grid.CancelEdit();
        if (Value != null) {
            fColsActive = Value;
        } else {
            fColsActive = fColsActiveDefault;
        }
        UpdateAll();
    }

    public I GetRowCollectionItem(int row) {
        if (fColsActive.size() == 0) {
            return null;
        }
        C cl = GetBaseRowCollection();
        if (cl == null) {
            return null;
        }
        if ((row > fHeaderRowIndex) && (row <= cl.getCount())) {
            PI cp = fColsActive.ByName("col_BaseID");
            if (cp != null) {
                return cl.FindBase(StrToIntDef(Grid.getCells(cp.getIndex(), row), -1));
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuffer SL = new StringBuffer();
        try {
            Content(SL, "");
            return SL.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    public static String crlf = "\r\n";

    public void Content(StringBuffer SL, String aCaption) {
        // SL.append("<html><head><title>StringGrid</title></head><body>");
        SL.append("<table border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"1\">");
        SL.append(crlf);
        if (!aCaption.equals("")) {
            SL.append("<caption>" + aCaption + "</caption>");
            SL.append(crlf);
        }
        for (int r = 0; r < Grid.getRowCount(); r++) {
            SL.append("<tr align=\"left\">");
            for (int c = 0; c < Grid.getColCount(); c++) {
                PI cp = fColsActive.getBaseColProp(c);
                if (cp == null) {
                    continue;
                }

                String s = Grid.getCells(c, r);
                String sColor = fCellProps.getItem(c, r).HTMLColor;
                if (s.equals("")) {
                    s = "&nbsp;";
                }
                if (r == 0) {
                    if (cp.getAlignment() == TColAlignment.taRightJustify) {
                        // SL.append("<th align=\"right\">" + s + "</th>");
                        SL.append("<th align=\"right\">");
                        SL.append(s);
                        SL.append("</th>");
                    } else {
                        SL.append("<th>" + s + "</th>");
                    }
                } else {
                    if (cp.getAlignment() == TColAlignment.taRightJustify) {
//                        SL.append("<td bgcolor=\"" + sColor +
//                                  "\" align=\"right\">" + s + "</td>");
                        SL.append("<td bgcolor=\"");
                        SL.append(sColor);
                        SL.append("\" align=\"right\">" + s + "</td>");
                    } else {
                        SL.append("<td bgcolor=\"" + sColor + "\">" + s + "</td>");
                    }
                }
            }
            SL.append("</tr>");
            SL.append(crlf);
        }
        SL.append("</table>");
        // SL.append("</body></html>");
    }

    public void ToggleColorPaint() {
        if (getColorPaint()) /* ColsActive.SortColIndex == -1 */
        {
            fColsActive.setSortColIndex(0);
        } else {
            fColsActive.setSortColIndex(-1);

        }
        UpdateAll();
    }

    public void MarkRowCollectionItem() {
        // usually mapped to VK_F4
        if (fColBO != null) {
            I cr = GetRowCollectionItem(Grid.getRow());
            if (cr == fColBO.CurrentRow)
                fColBO.CurrentRow = null;
            else
                fColBO.CurrentRow = cr;
            ShowData();
        }
    }

    public void DeleteRowCollectionItem() {
        // delete single row
        C cl = null;
        if ((fColBO != null) && (fColBO.getCurrentNode() != null)) {
            cl = fColBO.getCurrentNode().getBaseRowCollection();
        }
        I cr1 = GetRowCollectionItem(Grid.getRow());
        if (cr1 != null) {
            if (fColBO.getCurrentRow() == cr1) {
                fColBO.setCurrentRow(null);
            }
            cl.Delete(cr1.getIndex());
            for (int i = 0; i < cl.getCount(); i++) {
                I cr = cl.getBaseRowCollectionItem(i);
                cr.BaseID = i + 1;
            }
            UpdateAll();
            if (cl.FilteredCount() == 0) {
                Grid.setEnabled(false);
            }
        }
    }

    public void InsertRowCollectionItem() {
        C cl = null;
        if ((fColBO != null) && (fColBO.getCurrentNode() != null)) {
            cl = fColBO.getCurrentNode().getBaseRowCollection();
        }
        I cr1 = GetRowCollectionItem(Grid.getRow());
        if ((cl != null) && (cr1 != null)) {
            cl.Insert(cr1.getIndex());
            for (int i = 0; i < cl.getCount(); i++) {
                I cr = cl.getBaseRowCollectionItem(i);
                cr.BaseID = i + 1;
            }
            UpdateAll();
            Grid.setEnabled(false);
        }
    }

    public void AddRowCollectionItem() {
        C cl = null;
        if ((fColBO != null) && (fColBO.getCurrentNode() != null)) {
            cl = fColBO.getCurrentNode().getBaseRowCollection();
            I cr = cl.AddBaseRowCollectionItem();
            cr.BaseID = cl.getCount();
            UpdateAll();
            Grid.setEnabled(cl.FilteredCount() > 0);
        }
    }

    public void UpdateAll() {
        Grid.CancelEdit();
        fDisplayOrder.clear();
        SetupGrid();
        InitDisplayOrder(fColsActive.getSortColIndex());
        ShowData();
        if (fUseHTML) {
            InitCellProps();
        }
    }

    public void ShowData() {
        if (Grid.getIsEditorMode()) {
            return;
        }
        C cl = GetBaseRowCollection();
        if (cl != null) {
            // check RowCount
            if (Grid.getRowCount() != cl.getCount() + 1 + fHeaderRowIndex) {
                if (cl.getCount() > 0) {
                    Grid.setRowCount(cl.FilteredCount() + 1 + fHeaderRowIndex);
                } else {
                    Grid.setRowCount(2 + fHeaderRowIndex);
                }
            }
            // update all rows
            int r = 0 + fHeaderRowIndex;
            I cr;
            for (int j = 0; j < cl.getCount(); j++) {
                int i = j;
                if ((fDisplayOrder.size() == cl.FilteredCount()) && (j < fDisplayOrder.size())) {
                    i = fDisplayOrder.getByIndex(j);
                }
                if ((i < 0) || (i > cl.getCount() - 1)) {
                    continue;
                }
                cr = cl.getBaseRowCollectionItem(i);
                if (cr.IsInFilter()) {
                    ++r;
                    if (fColsActive != null) {
                        fColsActive.UpdateRow(Grid, r, cr);
                    }
                }
            }
            Grid.ShowData();
        }
    }

    public void SetupGrid() {
        C cl = GetBaseRowCollection();

        // initialize RowCount
        if ((cl != null) && (cl.getCount() > 0)) {
            Grid.setRowCount(cl.FilteredCount() + 1 + fHeaderRowIndex);
        } else {
            Grid.setRowCount(2 + fHeaderRowIndex);
        }

        // clear visible cells
        for (int i = fHeaderRowIndex; i < Grid.getRowCount() + fHeaderRowIndex; i++) {
            Grid.ClearRow(i);
        }
        // initialize column-width, show captions (and sort marker)
        ShowHeader();

        // Grid specific setup
        Grid.SetupGrid(this);
    }

    public PI AddColumn(String aNameIndex) {
        // find column in ColsAvail and add to ColsActive
        PI cp = fColsAvail.ByName(aNameIndex);
        if (cp != null) {
            PI o = fColsActive.AddBaseColProp();
            if (o != null)
                o.Assign(cp);
            return o;
        } else {
            return null;
        }
    }

    public TCellProp InitCellProp(int ACol, int ARow) {
        N rd = GetBaseNode();
        if (rd == null) {
            return null;
        }
        try {
            PI cp = fColsActive.getBaseColProp(ACol);
            if (cp == null) {
                return null;
            }
            boolean IsSorted = fColsActive.getSortColIndex() != -1;
            return InitCellProp2(rd, cp, IsSorted, ACol, ARow);
        } catch (Exception e) {
            return null;
        }
    }

    public void CellSelect(PI cp, int ACol, int ARow, Boolean CanSelect) {
        if (fOnBaseSelectCell != null) {
            fOnBaseSelectCell.OnSelectCell(cp, ACol, ARow, CanSelect);
        }
    }

    public void KeyDown(Object Sender, Integer Key, int myShift) {
        if (fOnBaseKeyDown != null) {
            fOnBaseKeyDown.OnKeyDown(Sender, Key, myShift);
        }
    }

    public void MouseDown(Object Sender, int ACol, int ARow) {
        if (fOnCellClick != null) {
            fOnCellClick.OnCellClick(this, ACol, ARow);
        }
        // only interested in header-row-clicks
        // (in MenuMode there is no header)
        if (fMenuMode || (ARow != 0)) {
            return;
        }
        InitDisplayOrder(ACol);
        ShowHeader(); // update sort marker
        ShowData(); // fill cells with data
    }

    public void InsertRow() {
        if (fAutoInsert) {
            InsertRowCollectionItem();
        }
    }

    public void AddRow() {
        if (fAutoInsert) {
            AddRowCollectionItem();
        }
    }

    public void DeleteRow() {
        if (fAutoInsert) {
            DeleteRowCollectionItem();
        }
    }

    public void MarkRow() {
        if (fAutoMark) {
            MarkRowCollectionItem();
        }
        if (fOnMarkRow != null) {
            fOnMarkRow.OnMarkRow(this);
        }
    }

    public void ClearContent() {
        if (fOnBaseClearContent != null) {
            fOnBaseClearContent.OnClearContent(this);
        }
    }

    public String FinishEdit(int ACol, int ARow, String Value) {
        String result = Value;
        PI cp = fColsActive.getBaseColProp(ACol);
        I cr;
        if ((cp != null) && (cp.getOnFinishEdit() != null)) {
            cr = GetRowCollectionItem(ARow);
            if (cr != null) {
                result = cp.getOnFinishEdit().GetText(cr, Value);
            }
        } else if ((cp != null) && (cp.getOnFinishEdit2() != null)) {
            cr = GetRowCollectionItem(ARow);
            if (cr != null) {
                result = cp.getOnFinishEdit2().GetText2(cr, Value, cp.getNameID());
            }
        }
        Grid.UpdateInplace(result);

        if ((getOnFinishEditCR() != null)) {
            cr = GetRowCollectionItem(ARow);
            if (cr != null) {
                getOnFinishEditCR().OnFinishEditCR(cr);
            }
        }

        return result;
    }

    public void BeginEdit() {
        PI cp = fColsActive.getBaseColProp(Grid.getCol());
        if ((cp != null) && cp.getReadOnly() && (fOnBaseEdit != null)) {
            fOnBaseEdit.OnEdit(this);
        }
    }

    protected void Trace(String s) {
        if (fOnTrace != null) {
            fOnTrace.OnTrace(s);
        }
    }

    private TCellProp InitCellProp2(N rd, PI cp, boolean IsSorted, int ACol, int ARow) {
        if (rd == null) {
            return null;
        }
        if (cp == null) {
            return null;
        }

        try {
            Color TempColor = TColGridColors.clDefault;
            int ccc = TColGridColorClass.Blank;

            boolean IsNormalRow;
            if ((ARow > fHeaderRowIndex) && (fAlwaysShowCurrent || (IsSorted == false))) {
                IsNormalRow = Odd((ARow + 1) / fHeatSize);
                // alternating row color
                if (IsNormalRow) {
                    TempColor = fDefaultColor;
                    ccc = TColGridColorClass.DefaultColor;
                } else {
                    TempColor = fAlternatingColor;
                    ccc = TColGridColorClass.AlternatingColor;
                }
                // editable columns color
                if (cp.getReadOnly() == false) {
                    if (IsNormalRow) {
                        TempColor = fEditableColor;
                        ccc = TColGridColorClass.EditableColor;
                    } else {
                        TempColor = fAlternatingEditableColor;
                        ccc = TColGridColorClass.AlternatingEditableColor;
                    }
                }

                PI cpBaseID = fColsActive.ByName("col_BaseID");
                if (cpBaseID != null) {
                    int BaseID = StrToIntDefEmpty(Grid.getCells(cpBaseID.getIndex(), ARow), -1);
                    if (BaseID >= 0) {
                        if (rd.getBaseRowCollection() != null) {
                            I cr = rd.getBaseRowCollection().FindBase(BaseID);
                            if ((cr != null) && (getColBO().getCurrentRow() == cr)) {
                                if (cp.ReadOnly) {
                                    TempColor = fCurrentColor;
                                    ccc = TColGridColorClass.CurrentColor;
                                } else {
                                    TempColor = fTransColor;
                                    ccc = TColGridColorClass.TransColor;
                                }
                            }
                            if (cr != null) {
                                Color TempColor2 = TempColor;
                                TempColor = cr.ColumnToColorDef(cp, TempColor);
                                if (TempColor != TempColor2) {
                                    ccc = TColGridColorClass.CustomColor;
                                }
                            }
                        }
                    }
                }
            }

            if ((ARow > fHeaderRowIndex) && ((this.getAlwaysShowCurrent() == false) && IsSorted)) {
                IsNormalRow = Odd((ARow + 1 + fHeaderRowIndex) / getHeatSize());
                // alternating row color
                if (IsNormalRow) {
                    TempColor = fDefaultColor;
                    ccc = TColGridColorClass.DefaultColor;
                } else {
                    TempColor = fAlternatingColor;
                    ccc = TColGridColorClass.AlternatingColor;
                }

                // editable column color
                if (cp.getReadOnly() == false) {
                    if (IsNormalRow) {
                        TempColor = fEditableColor;
                        ccc = TColGridColorClass.EditableColor;
                    } else {
                        TempColor = fAlternatingEditableColor;
                        ccc = TColGridColorClass.AlternatingEditableColor;
                    }
                }
            }

            if (ARow == fHeaderRowIndex) {
                TempColor = TColGridColors.clBtnFace;
                ccc = TColGridColorClass.HeaderColor;
            }
            TCellProp CellProp = fCellProps.getItem(ACol, ARow);
            CellProp.HTMLColor = TColGridColors.HTMLColor(TempColor);
            CellProp.Color = TempColor;
            CellProp.Alignment = cp.getAlignment();
            CellProp.ColorClass = ccc;
            return CellProp;
        } catch (Exception e) {
            return null;
        }
    }

    private void InitCellProps() {
        for (int c = 0; c < Grid.getColCount(); c++) {
            N rd = GetBaseNode();
            PI cp = fColsActive.getBaseColProp(c);
            boolean IsSorted = fColsActive.getSortColIndex() != -1;
            for (int r = 0; r < Grid.getRowCount(); r++) {
                InitCellProp2(rd, cp, IsSorted, c, r);
            }
        }
    }

    public void InitDisplayOrder(int col) {
        // only when column is sortable
        PI cp = fColsActive.getBaseColProp(col);
        if ((cp == null) || (!cp.getSortable())) {
            return;
        }

        // remember index of column
        fColsActive.setSortColIndex(cp.getIndex());

        // ----------------------------------------------------------------------------
        // get the data
        C cl = GetBaseRowCollection();
        if (cl == null) {
            return;
        }

        // * check RowCount and draw header
        // * 020804
        // * InitDisplayOrder only called from UpdateAll
        // * SetupGrid will be called from UpdateAll prior to InitDisplayOrder

        // SetupGrid; //called already in method UpdateAll

        // ----------------------------------------------------------------------------
        // update DisplayOrder
        fDisplayOrder.clear();
        I cr;
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.getBaseRowCollectionItem(i);
            if (cr.IsInFilter()) {
                String sortkey = "";
                sortkey = cp.GetSortKey(cr, sortkey);
                if (cp.getColType() == TColType.colTypeString) {
                    // use string as key unchanged
                    fDisplayOrder.Add2(sortkey, i);
                } else {
                    // add leading zeros if key-string is number
                    fDisplayOrder.Add2(Utils.PadLeft(sortkey, 8, '0'), i);
                }
            }
        }
        fDisplayOrder.Sort();
    }

    public void ShowHeader() {
        // ColCount always >= 1, see TCustomGrid.SetColCount
        Grid.setColCount(fColsActive.getVisibleCount());
        for (int i = 0; i < fColsActive.size(); i++) {
            PI cp = fColsActive.getBaseColProp(i);
            if ((cp != null) && cp.getVisible()) {
                Grid.setColWidth(i, cp.getWidth());
                if (!fMenuMode) {
                    Grid.setCells(i, 0, cp.getCaption());
                }
            }
        }
    }

    protected C GetBaseRowCollection() {
        N rd = GetBaseNode();
        if (rd != null) {
            return rd.getBaseRowCollection();
        } else {
            return null;
        }
    }

}
