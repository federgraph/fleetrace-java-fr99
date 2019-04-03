package org.riggvar.output;

import org.riggvar.base.TStrings;
import org.riggvar.base.TTokenParser;
import org.riggvar.bo.TBO;
import org.riggvar.bo.TMain;
import org.riggvar.col.IColGrid;
import org.riggvar.col.TBaseGridColorSchema;
import org.riggvar.col.TColGridTableModel;
import org.riggvar.event.TEventBO;
import org.riggvar.event.TEventColGrid;
import org.riggvar.event.TEventColProp;
import org.riggvar.event.TEventColProps;
import org.riggvar.event.TEventNode;
import org.riggvar.event.TEventRowCollection;
import org.riggvar.event.TEventRowCollectionItem;

public class TOutput02 extends
        TColGridTableModel<TEventColGrid, TEventBO, TEventNode, TEventRowCollection, TEventRowCollectionItem, TEventColProps, TEventColProp>

{
    private static final long serialVersionUID = 1L;

    TTokenParser TokenParser;
    TBO BO;

    int SortColIndex;
    String Modus = "Points";
    int Layout;
    String TableCaption;

    public int ReportID = 0;

    public TOutput02() {
        super();

        StringGrid.TableModel = null;
        ColGrid.setColorSchema(TBaseGridColorSchema.colorRed);
        ColGrid.setOnGetBaseNode(this);
        ColGrid.setUseHTML(true);

        BO = TMain.BO;
        TokenParser = new TTokenParser("");
        Layout = 1;
    }

    @Override
    protected TEventColReport newColGrid(IColGrid StringGrid) {
        return new TEventColReport(StringGrid);
    }

    /**
     * Wrapper for calling the appropriate FRContent method of FRColGrid.
     * 
     * <p>
     * The parameters have already been parsed from a request, see GetMsg().
     * </p>
     * 
     * @param SL TStrings container for the report being built
     */
    private void GetHTML(TStrings SL) {
        int i;
        TEventColProp cp;
        if (ColBO == null)
            return;
        if (Node == null)
            return;

        ColGrid.SetColBOReference(ColBO);
        if (Node instanceof TEventNode)
            ColGrid.setColPropClass();
        ColBO.InitColsActiveLayout(ColGrid, Layout); // Layout does not matter here

        if (ReportID == 9) {
            cp = ColGrid.getColsActive().ByName(BO.EventProps.SortColName);
            if (cp != null)
                i = cp.getIndex();
            else
                i = -1;
            ColGrid.getColsActive().setSortColIndex(i);
            ColGrid.setAlwaysShowCurrent(false);
            ColGrid.SetupGrid();
            ColGrid.InitDisplayOrder(ColGrid.getColsActive().getSortColIndex());
            ((TEventColReport) ColGrid).FRContent6(SL);
        } else {
            if (SortColIndex > -1)
                i = SortColIndex;
            else
                i = -1;

            ColGrid.getColsActive().setSortColIndex(i);

            // AlwaysShowCurrentRowAndErrorCellsInFullColor
            ColGrid.setAlwaysShowCurrent(true);

            ColGrid.UpdateAll();
            TEventColReport ecr = (TEventColReport) ColGrid;
            switch (ReportID) {
            case 0:
                ecr.FRContent0(SL, "");
                break;
            case 3:
                ecr.FRContent3(SL, "");
                break; // css
            case 5:
                ecr.FRContent5(SL, "");
                break; // bgcolor
            case 7:
                ecr.FRContent7(SL, "", Modus, BO.EventProps);
                break;
            case 8:
                ecr.FRContent8(SL, "", Modus, BO.EventProps);
                break;
            case 11:
                ecr.FRContent11(SL, "", Modus, BO.EventProps);
                break;
            case 12:
                ecr.FRContent1b(SL, "", Modus, BO.EventProps);
                break;
            case 14:
                ecr.FRContent1c(SL, "", Modus, BO.EventProps);
                break;
            case 15:
                ecr.FRContent1d(SL, "", Modus, BO.EventProps);
                break;
            default:
                ecr.FRContent0(SL, "");
                break;
            }
        }
    }

    /**
     * Parses the request in PathInfo, then calls GetHTML().
     * 
     * internally calls FRContentMethods.
     * 
     * @param SL       container for the report being built
     * @param PathInfo the request
     */
    public void GetMsg(TStrings SL, String PathInfo) {

        Layout = 0;
        SortColIndex = -1;
        TableCaption = PathInfo;
        TokenParser.setString(PathInfo);

        if (PathInfo.startsWith("Event.")) {
            TokenParser.NextToken(); // Event.
            ReportID = TokenParser.NextTokenX("Report");
            SortColIndex = TokenParser.NextTokenX("Sort");
            TokenParser.NextToken();
            Modus = TokenParser.getToken(); // .sToken;

            TEventNode ev = BO.EventNode;
            Node = ev;
            if (Modus.equals("Finish"))
                ev.WebLayout = 1; // Layout_Finish
            else
                ev.WebLayout = 2; // --> default Layout_Points = 0 will be used
                                  // used
            try {
                ColBO = Node.getBaseColBO();
                GetHTML(SL);
            } catch (Exception ex) {
                SL.Clear();
            } finally {
                ev.WebLayout = 0;
            }
        }

        if (PathInfo.startsWith("Race.")) {
            return;
        }

    }

}
