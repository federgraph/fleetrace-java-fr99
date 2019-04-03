package org.riggvar.output;

import org.riggvar.base.TStringList;
import org.riggvar.base.TStrings;
import org.riggvar.base.TTokenParser;
import org.riggvar.base.Utils;
import org.riggvar.bo.TMain;
import org.riggvar.col.IColGrid;
import org.riggvar.col.TBaseGridColorSchema;
import org.riggvar.col.TColAlignment;
import org.riggvar.col.TColGridTableModel;
import org.riggvar.col.THashGridJava;
import org.riggvar.event.TEventBO;
import org.riggvar.event.TEventColGrid;
import org.riggvar.event.TEventColProp;
import org.riggvar.event.TEventColProps;
import org.riggvar.event.TEventNode;
import org.riggvar.event.TEventRowCollection;
import org.riggvar.event.TEventRowCollectionItem;

public class TOutput01 extends
        TColGridTableModel<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp> {
    private static final long serialVersionUID = 1L;
    TTokenParser TokenParser;

    int Layout;
    String TableCaption;
    TEventColReport g;

    public TOutput01() {
        Node = TMain.BO.EventNode;
        ColBO = Node.BO;
        StringGrid = new THashGridJava();
        g = new TEventColReport(StringGrid);
        ColGrid = g;

        ColGrid.setColorSchema(TBaseGridColorSchema.colorRed);
        ColGrid.setOnGetBaseNode(this);
        ColGrid.setUseHTML(true);

        TokenParser = new TTokenParser("");
        Layout = 1;
    }

    @Override
    protected TEventColReport newColGrid(IColGrid StringGrid) {
        return new TEventColReport(StringGrid);
    }

    public void GetMsg(TStrings SL, int ReportID) {
        switch (ReportID) {
        case 93:
            FinishReport(SL);
            break;
        case 94:
            PointsReport(SL);
            break;

        case 95:
            FinishTable(SL);
            break;
        case 96:
            PointsTable(SL);
            break;

        case 98:
            CssReport(SL);
            break;
        case 99:
            IndexReport(SL);
            break;
        }
    }

    public void IndexReport(TStrings SL) {
        int k;
        char sep;

        TEventRowCollection cl;
        TEventRowCollectionItem cr;
        TEventColProp cp;

        SL.Clear();
        sep = ',';

        // shortcuts
        cl = Node.getBaseRowCollection();

        // Setup
        g.SetColBOReference(Node.getBaseColBO());
        g.getColsAvail().Init();
        ColBO.InitColsActiveLayout(g, Layout);
        // g.getColsActive().setSortColIndex(0);
        g.UpdateAll();

        // first column is ID-column, always counting from 1 upwards
        for (int r = 0; r < cl.size(); r++) {
            SL.Add(Utils.IntToStr(r + 1));
        }

        // from second column to last column
        for (int c = 1; c < g.getColsActive().size(); c++) {
            cp = g.getColsActive().getBaseColProp(c);

            // retain default sort-order for unsortable columns
            if (!cp.Sortable) {
                for (int r = 0; r < cl.size(); r++) {
                    SL.setString(r, SL.getString(r) + sep + (r + 1));
                }
                continue;
            }

            g.InitDisplayOrder(c);
            for (int r = 0; r < cl.size(); r++) {
                k = g.getDisplayOrder().getByIndex(r);
                cr = cl.getBaseRowCollectionItem(k);
                SL.setString(r, SL.getString(r) + sep + cr.BaseID);
            }
        }

        for (int r = 0; r < cl.size() - 1; r++) {
            SL.setString(r, '[' + SL.getString(r) + "],");
        }
        int z = cl.size() - 1;
        SL.setString(z, '[' + SL.getString(z) + ']');
        SL.Insert(0, "[");
        SL.Add("]");
    }

    public void CssReport(TStrings SL) {
        SL.Clear();

        TEventRowCollection cl = Node.getBaseRowCollection();
        TEventRowCollectionItem cr;
        TEventColProp cp;

        // grid setup
        g.SetColBOReference(Node.getBaseColBO());
        g.getColsAvail().Init();
        ColBO.InitColsActiveLayout(g, Layout);
        g.getColsActive().setSortColIndex(0);
        g.setAlwaysShowCurrent(false); // true=with Error-Colors and Current-Colors
        g.UpdateAll();

        String crlf = "";

        SL.Add("<table class=\"sortable\" border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"1\">");
        SL.Add(crlf);

        String s;
        String css = "";
        String clss;

        // header row
        SL.Add("<thead>");
        SL.Add("<tr>");
        for (int c = 0; c < g.Grid.getColCount(); c++) {
            cp = g.getColsActive().getBaseColProp(c);
            if (cp == null)
                continue;

            // content of field
            s = g.Grid.getCells(c, 0);
            if (s == "")
                s = "&nbsp;";

            // css for field
            css = "h";
            if (cp.Alignment == TColAlignment.taLeftJustify)
                css += "l";
            clss = " class=\"" + css + "\"";

            // add field
            SL.Add("<th" + clss + ">" + s + "</th>");
        }
        SL.Add("</tr>");
        SL.Add("</thead>");
        SL.Add(crlf);

        int race;
        String srace;
        int fleet;
        String cpNameID;

        // normal rows
        for (int r = 1; r < g.Grid.getRowCount(); r++) {
            cr = cl.get(r - 1);

            SL.Add("<tr>");
            for (int c = 0; c < g.Grid.getColCount(); c++) {
                cp = g.getColsActive().getBaseColProp(c);
                if (cp == null)
                    continue;

                // content of field
                s = g.Grid.getCells(c, r);
                if (s.equals(""))
                    s = "&nbsp;";

                // css for field
                clss = g.getCSS(c, r);

                // Regex rx = new Regex(@"col_R(\d)+");
                cpNameID = cp.getNameID();
                if (cpNameID.startsWith("col_R")) {
                    srace = Utils.Copy(cpNameID, 6, cpNameID.length());
                    race = Utils.StrToIntDef(srace, -1);
                    if (race > 0) {
                        fleet = cr.Race[race].Fleet;
                        clss = " class=\"g" + fleet + "\"";
                    }
                } else {
                    if (clss.contains("bgcolor")) {
                        css = "n";
                        clss = " class=\"" + css + "\"";
                    }
                }

                // add field
                SL.Add("<td" + clss + ">" + s + "</td>");
            }
            SL.Add("</tr>");
            SL.Add(crlf);
        }

        SL.Add("</table>");
    }

    protected void TableSortReport(TStrings SL, int Modus) {
        SL.Add("    <h2>" + TMain.BO.EventProps.EventName + "</h2>");
        SL.Add("");

        boolean b = TMain.BO.EventBO.WantDiffCols;

        if (Modus == 1)
            TMain.BO.EventNode.WebLayout = 1; // Finish
        else
            TMain.BO.EventNode.WebLayout = 2; // Points

        TMain.BO.EventBO.WantDiffCols = false;

        TableSortData(SL);
        TMain.BO.EventNode.WebLayout = 0;

        TMain.BO.EventBO.WantDiffCols = b;

        SL.Add("");
    }

    public void TableSortData(TStrings SL) {
        TStringList SL1 = new TStringList();
        CssReport(SL1);
        for (int i = 0; i < SL1.getCount(); i++) {
            SL.Add(SL1.getString(i));
        }
        SL.Add("");
        SL.Add("<div id=\"index_table\"><pre>");
        IndexReport(SL1);
        for (int i = 0; i < SL1.getCount(); i++) {
            SL.Add(SL1.getString(i));
        }
        SL.Add("</pre></div>");
    }

    public void FinishTable(TStrings SL) {
        SL.Clear();
        TMain.BO.EventNode.WebLayout = 1;
        TableSortData(SL);
        TMain.BO.EventNode.WebLayout = 0;
    }

    public void PointsTable(TStrings SL) {
        SL.Clear();
        TMain.BO.EventNode.WebLayout = 2;
        TableSortData(SL);
        TMain.BO.EventNode.WebLayout = 0;
    }

    public void FinishReport(TStrings SL) {
        TableSortReport(SL, 1);
    }

    public void PointsReport(TStrings SL) {
        TableSortReport(SL, 2);
    }

}
