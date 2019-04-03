package org.riggvar.output;

import org.riggvar.col.*;
import org.riggvar.event.*;

import java.text.*;
import org.riggvar.base.*;
import org.riggvar.bo.*;

/*

 0 - Report without formatting
 3 - Normal Report with css attributes without sort-links
 5 - Normal Report with bgcolor attributes without sort-links

 6 - DataSet Report matching FR11.xsd

 7 - EventReport with css attributes and Props.DetailUrl sort-links
 8 - EventReport with sort gif, map and Props.DetailUrl sort-links

11 - EventReport with bgcolor attributes and 1 line url sort-link
12 - EventReport with bgcolor attributes and 2 line javascript sort-links
14 - EventReport with bgcolor attributes and 1 line javascript sort-links
15 - EventReport with bgcolor attributes and 2 line url sort-links


*/

public class TEventColReport extends TEventColGrid {
    public TEventColReport(IColGrid aGrid) {
        super(aGrid);
    }

    /**
     * DataSet Report
     * 
     * @param SL StringList
     */
    public void FRContent6(TStrings SL) {
        TEventRowCollection cl = GetBaseRowCollection();
        if (cl != null) {
            SL.Add("<e xmlns=\"http://riggvar.net/FR11.xsd\">");
            SL.Add("");
            cl.GetXMLSchema(SL);
            SL.Add("");

            TEventRowCollectionItem cr;
            for (int j = 0; j < cl.getCount(); j++) {
                int i = j;
                if (j < this.getDisplayOrder().size())
                    i = (int) getDisplayOrder().getByIndex(j);
                if ((i >= 0) && (i < cl.getCount())) {
                    cr = cl.get(i);

                    String s;
                    s = "<r ID=\"" + cr.BaseID + "\" ";
                    s = s + "Pos=\"" + cr.GRank() + "\" ";
                    s = s + "Bib=\"" + cr.Bib + "\" ";
                    s = s + "SNR=\"" + cr.SNR + "\" ";
                    s = s + "DN=\"" + cr.DN() + "\" ";

                    for (int r = 1; r < cl.RCount(); r++) {
                        s = s + "W" + r + "=\"" + cr.Race[r] + "\" "; // RaceValue Indexer
                    }
                    s = s + "Pts=\"" + cr.GPoints() + "\" ";
                    s = s + "/>";
                    SL.Add(s);
                }
            }

            SL.Add("");
            cl.GetXMLBackup(SL);
            SL.Add("");
            SL.Add("</e>");
        }
    }

    /**
     * Normal Report with css class attributes without sort-links
     * 
     * @param SL       StringList
     * @param aCaption Table caption
     */
    public void FRContent3(TStrings SL, String aCaption) {
        SL.Add("<table border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"1\">");
        SL.Add(crlf);
        if (!aCaption.equals("")) {
            SL.Add("<caption>" + aCaption + "</caption>");
            SL.Add(crlf);
        }
        // int sortColIndex_ = this.ColsActive.SortColIndex;
        for (int r = 0; r < Grid.getRowCount(); r++) {
            SL.Add("<tr>");
            for (int c = 0; c < Grid.getColCount(); c++) {
                TEventColProp cp = this.getColsActive().getBaseColProp(c);
                if (cp == null)
                    continue;

                String s = Grid.getCells(c, r);
                String sColor;
                if (s.equals(""))
                    s = "&nbsp;";
                if (r == 0) {
                    sColor = "h";
                    if (cp.Alignment == TColAlignment.taLeftJustify)
                        sColor += "l";

                    SL.Add("<th class=\"" + sColor + "\">" + s + "</th>");
                } else {
                    sColor = TColGridColors.CSSClass(fCellProps.getItem(c, r));
                    if (cp.Alignment == TColAlignment.taLeftJustify)
                        sColor += "l";
                    SL.Add("<td" + sColor + ">" + s + "</td>");
                }
            }
            SL.Add("</tr>");
            SL.Add(crlf);
        }
        SL.Add("</table>");
    }

    /**
     * Normal Report with bgcolor attributes and without sort-links
     * 
     * @param SL       StringList
     * @param aCaption Table caption
     */
    public void FRContent5(TStrings SL, String aCaption) {
        SL.Add("<table border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"1\">");
        SL.Add(crlf);
        if (!aCaption.equals("")) {
            SL.Add("<caption>" + aCaption + "</caption>");
            SL.Add(crlf);
        }
        for (int r = 0; r < Grid.getRowCount(); r++) {
            SL.Add("<tr align=\"left\">");
            for (int c = 0; c < Grid.getColCount(); c++) {
                TEventColProp cp = this.getColsActive().getBaseColProp(c);
                if (cp == null)
                    continue;

                String sColor = fCellProps.getItem(c, r).HTMLColor;
                sColor = " bgcolor=\"" + sColor + "\"";

                String s = Grid.getCells(c, r);
                if (s == null || s.equals(""))
                    s = "&nbsp;";

                String sAlign = "";
                if (cp.Alignment == TColAlignment.taRightJustify)
                    sAlign = " align=\"right\"";

                if (r == 0) {
                    sColor = " bgcolor=\"Beige\"";
                    SL.Add("<th" + sAlign + sColor + ">" + s + "</th>");
                    // SL.Add(string.Format("<th{0}{1}>{2}</th>", sAlign, sColor, s));
                } else {
                    SL.Add("<td" + sAlign + sColor + ">" + s + "</td>");
                    // SL.Add(string.Format("<td{0}{1}>{2}</td>", sAlign, sColor, s));
                }
            }
            SL.Add("</tr>");
            SL.Add(crlf);
        }
        SL.Add("</table>");
    }

    /// <summary>
    /// Report without formatting
    /// </summary>
    /// <param name="SL">StringList
    /// <param name="aCaption">Table caption
    public void FRContent0(TStrings SL, String aCaption) {
        SL.Add("<table border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"1\">");
        SL.Add(crlf);
        if (!aCaption.equals("")) {
            SL.Add("<caption>" + aCaption + "</caption>");
            SL.Add(crlf);
        }
        for (int r = 0; r < Grid.getRowCount(); r++) {
            SL.Add("<tr>");
            for (int c = 0; c < Grid.getColCount(); c++) {
                String s = Grid.getCells(c, r);
                if (s.equals(""))
                    s = "&nbsp;";
                if (r == 0) {
                    SL.Add("<th>" + s + "</th>");
                } else {
                    SL.Add("<td>" + s + "</td>");
                }
            }
            SL.Add("</tr>");
            SL.Add(crlf);
        }
        SL.Add("</table>");
    }

    /// <summary>
    /// EventReport with bgcolor attributes and 2 line url sort-links
    /// </summary>
    /// <param name="SL">StringList
    /// <param name="aCaption">Table caption
    /// <param name="Modus">Finish or Points
    /// <param name="Props">filled with DetailUrl
    public void FRContent1d(TStrings SL, String aCaption, String Modus, TEventProps Props) {
        SL.Add("<table border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"1\">");
        SL.Add(crlf);
        if (!aCaption.equals("")) {
            SL.Add("<caption>" + aCaption + "</caption>");
            SL.Add(crlf);
        }
        int sortColIndex_ = this.getColsActive().getSortColIndex();
        for (int r_ = -1; r_ < Grid.getRowCount(); r_++) {
            SL.Add("<tr align=\"left\">");
            for (int c = 0; c < Grid.getColCount(); c++) {
                int r = r_;
                if (r_ == -1)
                    r = 0;

                TEventColProp cp = getColsActive().getBaseColProp(c);
                if (cp == null)
                    continue;

                String s = Grid.getCells(c, r);
                String sColor = fCellProps.getItem(c, r).HTMLColor;
                if (s.equals(""))
                    s = "&nbsp;";
                if (r_ == -1) {
                    sColor = " bgcolor=\"Beige\"";
                    String sAlign = "";
                    if (cp.Sortable) {
                        if (c == sortColIndex_ && Modus.equals("Points"))
                            sColor = " bgcolor=\"LightSkyBlue\"";
                        s = "<a href=\"" + Props.DetailUrl + "?s=" + cp.getIndex() + "&amp;m=Points\">" + s + "</a>";
                        // s = string.Format(@"<a href=""{0}?s={1}&amp;m=Points"">{2}</a>",
                        // Props.DetailUrl, cp.Index, s);
                    }
                    if (cp.Alignment == TColAlignment.taRightJustify)
                        sAlign = " align=\"right\"";
                    SL.Add("<th" + sAlign + sColor + ">" + s + "</th>");
                    // SL.Add(string.Format("<th{0}{1}>{2}</th>", sAlign, sColor, s));
                } else if (r_ == 0) {
                    sColor = " bgcolor=\"Beige\"";
                    String sAlign = "";
                    if (cp.Sortable) {
                        if (c == sortColIndex_ && Modus.equals("Finish"))
                            sColor = " bgcolor=\"LightSkyBlue\"";
                        s = "<a href=\"" + Props.DetailUrl + "?s=" + cp.getIndex() + "&amp;m=Finish\">" + s + "</a>";
                        // s = string.Format(@"<a href=""{0}?s={1}&amp;m=Finish"">{2}</a>",
                        // Props.DetailUrl, cp.Index, s);
                    }
                    if (cp.Alignment == TColAlignment.taRightJustify)
                        sAlign = " align=\"right\"";
                    SL.Add("<th" + sAlign + sColor + ">" + s + "</th>");
                    // SL.Add(string.Format("<th{0}{1}>{2}</th>", sAlign, sColor, s));
                } else {
                    if (cp.Alignment == TColAlignment.taRightJustify)
                        SL.Add("<td bgcolor=\"" + sColor + "\" align=\"right\">" + s + "</td>");
                    else
                        SL.Add("<td bgcolor=\"" + sColor + "\">" + s + "</td>");
                }
            }
            SL.Add("</tr>");
            SL.Add(crlf);
        }
        SL.Add("</table>");
    }

    /// <summary>
    /// EventReport with bgcolor attributes and 1 line url sort-link
    /// </summary>
    /// <param name="SL">StringList
    /// <param name="aCaption">Table caption
    /// <param name="Modus">Finish or Points
    /// <param name="Props">filled with DetailUrl
    public void FRContent11(TStrings SL, String aCaption, String Modus, TEventProps Props) {
        SL.Add("<table border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"1\">");
        SL.Add(crlf);
        if (!aCaption.equals("")) {
            SL.Add("<caption>" + aCaption + "</caption>");
            SL.Add(crlf);
        }
        // int sortColIndex_ = this.getColsActive().getSortColIndex();
        for (int r = 0; r < Grid.getRowCount(); r++) {

            SL.Add("<tr align=\"left\">");
            for (int c = 0; c < Grid.getColCount(); c++) {
                TEventColProp cp = getColsActive().getBaseColProp(c);
                if (cp == null)
                    continue;

                String s = Grid.getCells(c, r);
                String sColor = fCellProps.getItem(c, r).HTMLColor;
                if (s.equals(""))
                    s = "&nbsp;";
                if (r == 0) {
                    String a;
                    if (Modus.equals("Finish"))
                        a = "<a href=\"finish?Sort=" + c + "\">" + s + "</a>";
                    else
                        a = "<a href=\"points?Sort=" + c + "\">" + s + "</a>";

                    if (cp.Alignment == TColAlignment.taRightJustify)
                        SL.Add("<th bgcolor=\"" + sColor + "\" align=\"right\">" + a + "</th>");
                    else
                        SL.Add("<th bgcolor=\"" + sColor + "\">" + a + "</th>");
                } else {
                    if (cp.Alignment == TColAlignment.taRightJustify)
                        SL.Add("<td bgcolor=\"" + sColor + "\" align=\"right\">" + s + "</td>");
                    else
                        SL.Add("<td bgcolor=\"" + sColor + "\">" + s + "</td>");
                }
            }
            SL.Add("</tr>");
            SL.Add(crlf);
        }
        SL.Add("</table>");
    }

    /// <summary>
    /// EventReport with bgcolor attributes and 2 line javascript sort-links
    /// </summary>
    /// <param name="SL">StringList
    /// <param name="aCaption">Table caption
    /// <param name="Modus">Finish or Points
    /// <param name="Props">filled with DetailUrl
    public void FRContent1b(TStrings SL, String aCaption, String Modus, TEventProps Props) {
        SL.Add("<table border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"1\">");
        SL.Add(crlf);
        if (!aCaption.equals("")) {
            SL.Add("<caption>" + aCaption + "</caption>");
            SL.Add(crlf);
        }
        int sortColIndex_ = this.getColsActive().getSortColIndex();
        for (int r_ = -1; r_ < Grid.getRowCount(); r_++) {
            SL.Add("<tr align=\"left\">");
            for (int c = 0; c < Grid.getColCount(); c++) {
                int r = r_;
                if (r_ == -1)
                    r = 0;

                TEventColProp cp = getColsActive().getBaseColProp(c);
                if (cp == null)
                    continue;

                String s = Grid.getCells(c, r);
                String sColor = fCellProps.getItem(c, r).HTMLColor;
                if (s.equals(""))
                    s = "&nbsp;";
                if (r_ == -1) {
                    sColor = " bgcolor=\"Beige\"";
                    String sAlign = "";
                    if (cp.Sortable) {
                        if (c == sortColIndex_ && Modus.equals("Points"))
                            sColor = " bgcolor=\"LightSkyBlue\"";
                        s = MessageFormat.format("<a href=\"#\" onclick=\"javascript:__Sort({0},{1})\" >{2}</a>",
                                new Object[] { cp.getIndex(), "'Points'", s });
                    }
                    if (cp.Alignment == TColAlignment.taRightJustify)
                        sAlign = " align=\"right\"";
                    SL.Add("<th" + sAlign + sColor + ">" + s + "</th>");
                    // SL.Add(string.Format("<th{0}{1}>{2}</th>", sAlign, sColor, s));
                } else if (r_ == 0) {
                    sColor = " bgcolor=\"Beige\"";
                    String sAlign = "";
                    if (cp.Sortable) {
                        if (c == sortColIndex_ && Modus.equals("Finish"))
                            sColor = " bgcolor=\"LightSkyBlue\"";
                        s = MessageFormat.format("<a href=\"#\" onclick=\"javascript:__Sort({0},{1})\" >{2}</a>",
                                new Object[] { cp.getIndex(), "'Finish'", s });
                    }
                    if (cp.Alignment == TColAlignment.taRightJustify)
                        sAlign = " align=\"right\"";
                    SL.Add("<th" + sAlign + sColor + ">" + s + "</th>");
                } else {
                    if (cp.Alignment == TColAlignment.taRightJustify)
                        SL.Add("<td bgcolor=\"" + sColor + "\" align=\"right\">" + s + "</td>");
                    else
                        SL.Add("<td bgcolor=\"" + sColor + "\">" + s + "</td>");
                }
            }
            SL.Add("</tr>");
            SL.Add(crlf);
        }
        SL.Add("</table>");
    }

    /// <summary>
    /// EventReport with bgcolor attributes and 1 line javascript sort-links
    /// </summary>
    /// <param name="SL">StringList
    /// <param name="aCaption">Table caption
    /// <param name="Modus">Finish or Points
    /// <param name="Props">filled with DetailUrl
    public void FRContent1c(TStrings SL, String aCaption, String Modus, TEventProps Props) {
        SL.Add("<table border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"1\">");
        SL.Add(crlf);
        if (!aCaption.equals("")) {
            SL.Add("<caption>" + aCaption + "</caption>");
            SL.Add(crlf);
        }
        int sortColIndex_ = this.getColsActive().getSortColIndex();
        for (int r = 0; r < Grid.getRowCount(); r++) {
            SL.Add("<tr align=\"left\">");
            for (int c = 0; c < Grid.getColCount(); c++) {
                TEventColProp cp = getColsActive().getBaseColProp(c);
                if (cp == null)
                    continue;

                String s = Grid.getCells(c, r);
                String sColor = fCellProps.getItem(c, r).HTMLColor;
                if (s.equals(""))
                    s = "&nbsp;";
                if (r == 0) {
                    sColor = " bgcolor=\"Beige\"";
                    String sAlign = "";
                    if (cp.Sortable) {
                        if (c == sortColIndex_)
                            sColor = " bgcolor=\"LightSkyBlue\"";
                        String mf = "<a href=\"#\" onclick=\"javascript:__Sort({0},{1})\" >{2}</a>";
                        s = MessageFormat.format(mf, new Object[] { cp.getIndex(), "'" + Modus + "'", s });
                    }
                    if (cp.Alignment == TColAlignment.taRightJustify)
                        sAlign = " align=\"right\"";
                    SL.Add("<th" + sAlign + sColor + ">" + s + "</th>");
                } else {
                    if (cp.Alignment == TColAlignment.taRightJustify)
                        SL.Add("<td bgcolor=\"" + sColor + "\" align=\"right\">" + s + "</td>");
                    else
                        SL.Add("<td bgcolor=\"" + sColor + "\">" + s + "</td>");
                }
            }
            SL.Add("</tr>");
            SL.Add(crlf);
        }
        SL.Add("</table>");
    }

    /// <summary>
    /// EventReport with css attributes and Props.DetailUrl sort-links
    /// </summary>
    /// <param name="SL">StringList
    /// <param name="aCaption">Table caption
    /// <param name="Modus">Finish or Points
    /// <param name="Props">filled filled with DetailUrl and EventName
    public void FRContent7(TStrings SL, String aCaption, String Modus, TEventProps Props) {
        SL.Add("<table border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"1\">");
        SL.Add(crlf);
        if (!aCaption.equals("")) {
            SL.Add("<caption>" + aCaption + "</caption>");
            SL.Add(crlf);
        }
        int sortColIndex_ = this.getColsActive().getSortColIndex();
        int startRow = 0;
        if (!Props.DetailUrl.equals(""))
            startRow = -1;
        for (int r_ = startRow; r_ < Grid.getRowCount(); r_++) {
            SL.Add("<tr>");
            for (int c = 0; c < Grid.getColCount(); c++) {
                int r = r_;
                if (r_ == -1)
                    r = 0;

                TEventColProp cp = getColsActive().getBaseColProp(c);
                if (cp == null)
                    continue;

                String s = Grid.getCells(c, r);
                String sColor;
                if (s.equals(""))
                    s = "&nbsp;";
                if (r_ == -1) {
                    sColor = "h";
                    if (cp.Sortable) {
                        if (c == sortColIndex_ && Modus.equals("Points"))
                            sColor = "sm";
                        s = "<a href=\"" + Props.DetailUrl + "?en=" + Props.EventNameID + "&s=" + cp.getIndex()
                                + "&m=Points\">" + s + "</a>";
                    }
                    if (cp.Alignment == TColAlignment.taLeftJustify)
                        sColor += "l";
                    sColor = " class=\"" + sColor + "\"";
                    SL.Add("<th" + sColor + ">" + s + "</th>");
                } else if (r_ == 0) {
                    sColor = "h";
                    if (cp.Sortable) {
                        if (c == sortColIndex_ && Modus.equals("Finish"))
                            sColor = "sm";
                        if (!Props.DetailUrl.equals(""))
                            s = "<a href=\"" + Props.DetailUrl + "?en=" + Props.EventNameID + "&s=" + cp.getIndex()
                                    + "&m=Finish\">" + s + "</a>";
                    }
                    if (cp.Alignment == TColAlignment.taLeftJustify)
                        sColor += "l";
                    sColor = " class=\"" + sColor + "\"";
                    SL.Add("<th" + sColor + ">" + s + "</th>");
                } else {
                    sColor = TColGridColors.CSSClass(fCellProps.getItem(c, r));
                    SL.Add("<td" + sColor + ">" + s + "</td>");
                }
            }
            SL.Add("</tr>");
            SL.Add(crlf);
        }
        SL.Add("</table>");
    }

    /// <summary> old: FRContent3
    /// EventReport with Sort-gif
    /// </summary>
    /// <param name="SL">StringList
    /// <param name="aCaption">Table caption
    /// <param name="Modus">Finish or Points
    /// <param name="Props">Container for additional properties
    public void FRContent8(TStrings SL, String aCaption, String Modus, TEventProps Props) {
        SL.Add("<table border=\"1\" width=\"100%\" cellspacing=\"0\" cellpadding=\"1\">");
        SL.Add(crlf);
        if (!aCaption.equals("")) {
            SL.Add("<caption>" + aCaption + "</caption>");
            SL.Add(crlf);
        }
        int sortColIndex_ = this.getColsActive().getSortColIndex();
        for (int r = 0; r < Grid.getRowCount(); r++) {
            SL.Add("<tr align=\"left\">");
            for (int c = 0; c < Grid.getColCount(); c++) {
                TEventColProp cp = getColsActive().getBaseColProp(c);
                if (cp == null)
                    continue;

                String s = Grid.getCells(c, r);
                String sColor = fCellProps.getItem(c, r).HTMLColor;
                if (s.equals(""))
                    s = "&nbsp;";
                if (r == 0) {
                    sColor = " bgcolor=\"Beige\"";
                    String sAlign = "";
                    if (cp.Sortable) {
                        if (c == sortColIndex_ && Modus.equals("Finish"))
                            sColor = " bgcolor=\"#AA99FF\"";
                        if (c == sortColIndex_ && Modus.equals("Points"))
                            sColor = " bgcolor=\"#FF99CC\"";
                        s = "<img border=\"0\" usemap=\"#IM" + cp.getIndex() + "\" src=\"F.gif\">&nbsp;" + s;
                    }
                    if (cp.Alignment == TColAlignment.taRightJustify)
                        sAlign = " align=\"right\"";
                    SL.Add("<th" + sAlign + sColor + ">" + s + "</th>");
                } else {
                    if (cp.Alignment == TColAlignment.taRightJustify)
                        SL.Add("<td bgcolor=\"" + sColor + "\" align=\"right\">" + s + "</td>");
                    else
                        SL.Add("<td bgcolor=\"" + sColor + "\">" + s + "</td>");
                }
            }
            SL.Add("</tr>");
            SL.Add(crlf);
        }
        SL.Add("</table>");

        if (Props.DetailUrl.startsWith("javascript")) {
            // post back
            for (int c = 0; c < Grid.getColCount(); c++) {
                String sHREF;
                SL.Add("<MAP name=\"IM" + c + "\">");
                sHREF = MessageFormat.format(Props.DetailUrl, new Object[] { c, "Finish", Props.EventNameID });
                SL.Add("<area id=\"F" + c + "\" href=\"" + sHREF + "\" shape=\"RECT\" coords=\"20,0,19,15\">");
                sHREF = MessageFormat.format(Props.DetailUrl, new Object[] { c, "Points", Props.EventNameID });
                SL.Add("<area id=\"P" + c + "\" href=\"" + sHREF + "\" shape=\"RECT\" coords=\"20,0,39,15\">");
                SL.Add("</MAP>");
            }
        } else {
            // query string
            for (int c = 0; c < Grid.getColCount(); c++) {
                SL.Add("<MAP name=\"IM" + c + "\">");
                SL.Add("<area id=\"F" + c + "\" href=\"" + Props.DetailUrl + "?en=" + Props.EventNameID + "&s=" + c
                        + "&m=Finish\" shape=\"RECT\" coords=\"0,0,19,15\">");
                SL.Add("<area id=\"P" + c + "\" href=\"" + Props.DetailUrl + "?en=" + Props.EventNameID + "&s=" + c
                        + "&m=Points\" shape=\"RECT\" coords=\"20,0,39,15\">");
                SL.Add("</MAP>");
            }
        }
    }

    public String getCSS(int c, int r) {
        return TColGridColors.CSSClass(fCellProps.getItem(c, r));
    }

}
