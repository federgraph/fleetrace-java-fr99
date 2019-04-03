package org.riggvar.bo;

import org.riggvar.base.*;
import org.riggvar.col.*;
import org.riggvar.event.*;
import org.riggvar.stammdaten.*;

public class TExcelExporter {

    private TStringList SL;
    private TStringList SLToken;
    public char Delimiter;

    public TExcelExporter() {
        SL = new TStringList();
        SLToken = new TStringList();
        Delimiter = ';';
        SL.setQuoteChar('"');
        SLToken.setQuoteChar('"');
    }

    public void FillTable(int TableID, TBO BO) {
        switch (TableID) {
        case TExcelImporter.TableID_NameList:
            GetNameList(BO);
            break;
        case TExcelImporter.TableID_StartList:
            GetStartList(BO);
            break;
        case TExcelImporter.TableID_FleetList:
            GetFleetList(BO);
            break;
        case TExcelImporter.TableID_FinishList:
            GetFinishList(BO);
            break;
        case TExcelImporter.TableID_ResultList:
            GetResultList(BO);
            break;
        case TExcelImporter.TableID_CaptionList:
            GetCaptionList();
            break;
        }
    }

    private void CopyLines(TStrings Memo) {
        for (int i = 0; i < SL.getCount(); i++)
            Memo.Add(SL.getString(i));
    }

    public void AddSection(int TableID, TBO BO, TStrings Memo) {
        switch (TableID) {
        case TExcelImporter.TableID_NameList:
            Memo.Add(TExcelImporter.NameListStartToken);
            GetNameList(BO);
            CopyLines(Memo);
            Memo.Add(TExcelImporter.NameListEndToken);
            break;

        case TExcelImporter.TableID_StartList:
            Memo.Add(TExcelImporter.StartListStartToken);
            GetStartList(BO);
            CopyLines(Memo);
            Memo.Add(TExcelImporter.StartListEndToken);
            break;

        case TExcelImporter.TableID_FleetList:
            Memo.Add(TExcelImporter.FleetListStartToken);
            GetFleetList(BO);
            CopyLines(Memo);
            Memo.Add(TExcelImporter.FleetListEndToken);
            break;

        case TExcelImporter.TableID_FinishList:
            Memo.Add(TExcelImporter.FinishListStartToken);
            GetFinishList(BO);
            CopyLines(Memo);
            Memo.Add(TExcelImporter.FinishListEndToken);
            break;

        case TExcelImporter.TableID_ResultList:
            Memo.Add(TExcelImporter.ResultListStartToken);
            GetResultList(BO);
            CopyLines(Memo);
            Memo.Add(TExcelImporter.ResultListEndToken);
            break;

        case TExcelImporter.TableID_TimeList:
            for (int r = 1; r < BO.BOParams.RaceCount; r++) {
                if (r > 0)
                    SL.Add("");
                Memo.Add(TExcelImporter.TimeListStartToken + ".R" + r);
                GetTimeList(r, BO);
                CopyLines(Memo);
                Memo.Add(TExcelImporter.TimeListEndToken);
                Memo.Add("");
            }
            break;

        case TExcelImporter.TableID_CaptionList:
            Memo.Add(TExcelImporter.CaptionListStartToken);
            GetCaptionList();
            CopyLines(Memo);
            Memo.Add(TExcelImporter.CaptionListEndToken);
            break;
        }
    }

    public void AddLines(int TableID, TBO BO, TStrings Memo) {
        FillTable(TableID, BO);
        for (int i = 0; i < SL.getCount(); i++)
            Memo.Add(SL.getString(i));
    }

    public String GetString(int TableID, TBO BO) {
        FillTable(TableID, BO);
        return SL.getText();
    }

    public void GetNameList(TBO BO) {
        String s;
        TStammdatenRowCollection cl;
        TStammdatenRowCollectionItem cr;

        SL.Clear();
        SLToken.setDelimiter(Delimiter);
        cl = BO.EventNode.StammdatenRowCollection;
        if (cl.getCount() < 1)
            return;

        cr = cl.get(0);

        // HeaderLine
        SLToken.Clear();
        SLToken.Add("SNR");
        for (int j = 1; j < cr.getFieldCount(); j++) {
            if (cr.isFieldUsed(j))
                SLToken.Add("N" + j);
        }
        s = SLToken.getDelimitedText();
        SL.Add(s);

        // DataLines
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            SLToken.Clear();
            SLToken.Add("" + cr.SNR);
            for (int j = 1; j < cr.getFieldCount(); j++) {
                if (!cr.isFieldUsed(j))
                    continue;
                SLToken.Add(cr.getFieldValue(j));
            }
            s = SLToken.getDelimitedText();
            SL.Add(s);
        }
    }

    public void GetStartList(TBO BO) {
        SL.Clear();
        SLToken.setDelimiter(Delimiter);
        TEventRowCollection cl = BO.EventNode.getBaseRowCollection();
        if (cl.getCount() < 1)
            return;

        // HeaderLine
        SLToken.Clear();
        SLToken.Add("Pos");
        SLToken.Add("SNR");
        SLToken.Add("Bib");
        String s = SLToken.getDelimitedText();
        SL.Add(s);

        // DataLines
        for (int i = 0; i < cl.getCount(); i++) {
            TEventRowCollectionItem cr = cl.get(i);
            SLToken.Clear();
            SLToken.Add("" + cr.BaseID);
            SLToken.Add("" + cr.SNR);
            SLToken.Add("" + cr.Bib);
            s = SLToken.getDelimitedText();
            SL.Add(s);
        }
    }

    public void GetFinishList(TBO BO) {
        String s;
        TEventRowCollection cl;
        TEventRowCollectionItem cr;
        TEventRaceEntry ere;

        SL.Clear();
        SLToken.setDelimiter(Delimiter);
        cl = BO.EventNode.getBaseRowCollection();
        if (cl.getCount() < 1)
            return;

        // HeaderLine
        SLToken.Clear();
        SLToken.Add("SNR");
        SLToken.Add("Bib");
        cr = cl.get(0);
        for (int r = 1; r < cr.RCount(); r++)
            SLToken.Add("R" + r);
        s = SLToken.getDelimitedText();
        SL.Add(s);

        // DataLines
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            SLToken.Clear();
            SLToken.Add("" + cr.SNR);
            SLToken.Add("" + cr.Bib);
            for (int r = 1; r < cr.RCount(); r++) {
                ere = cr.Race[r];
                SLToken.Add("" + ere.OTime);
            }
            s = SLToken.getDelimitedText();
            SL.Add(s);
        }
    }

    public void GetFleetList(TBO BO) {
        String s;
        TEventRowCollection cl;
        TEventRowCollectionItem cr;
        TEventRaceEntry ere;

        SL.Clear();
        SLToken.setDelimiter(Delimiter);
        cl = BO.EventNode.getBaseRowCollection();
        if (cl.getCount() < 1)
            return;

        // HeaderLine
        SLToken.Clear();
        SLToken.Add("SNR");
        SLToken.Add("Bib");
        cr = cl.get(0);
        for (int r = 1; r < cr.RCount(); r++)
            SLToken.Add("R" + r);
        s = SLToken.getDelimitedText();
        SL.Add(s);

        // DataLines
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            SLToken.Clear();
            SLToken.Add("" + cr.SNR);
            SLToken.Add("" + cr.Bib);
            for (int r = 1; r < cr.RCount(); r++) {
                ere = cr.Race[r];
                SLToken.Add("" + ere.Fleet);
            }
            s = SLToken.getDelimitedText();
            SL.Add(s);
        }
    }

    public void GetResultList(TBO BO) {
        String s;
        TEventRowCollection cl;
        TEventRowCollectionItem cr;
        TEventRaceEntry ere;

        SL.Clear();
        SLToken.setDelimiter(Delimiter);
        cl = BO.EventNode.getBaseRowCollection();
        if (cl.getCount() < 1)
            return;

        // HeaderLine
        SLToken.Clear();
        SLToken.Add("SNR");
        SLToken.Add("Bib");
        SLToken.Add("N1");
        SLToken.Add("N2");
        SLToken.Add("N3");
        SLToken.Add("N4");
        SLToken.Add("N5");
        SLToken.Add("N6");
        cr = cl.get(0);
        for (int r = 1; r < cr.RCount(); r++)
            SLToken.Add("R" + r);
        s = SLToken.getDelimitedText();
        SL.Add(s);

        // DataLines
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            SLToken.Clear();
            SLToken.Add("" + cr.SNR);
            SLToken.Add("" + cr.Bib);
            SLToken.Add(cr.FN());
            SLToken.Add(cr.LN());
            SLToken.Add(cr.SN());
            SLToken.Add(cr.NC());
            SLToken.Add(cr.GR());
            SLToken.Add(cr.PB());
            for (int r = 1; r < cr.RCount(); r++) {
                ere = cr.Race[r];
                SLToken.Add("" + ere.OTime);
            }
            s = SLToken.getDelimitedText();
            SL.Add(s);
        }
    }

    public void GetTimeList(int r, TBO BO) {
//      String s;
//      TRaceRowCollection cl;
//      TRaceRowCollectionItem cr;
//      TTimePoint tp;
//
//      SL.Clear();
//      SLToken.setDelimiter(Delimiter);
//      cl = BO.RNode[r].getBaseRowCollection();
//      if (cl.getCount() < 1)
//          return;
//
//      // HeaderLine
//      SLToken.Clear();
//      SLToken.Add("SNR");
//      SLToken.Add("Bib");
//      cr = cl.get(0);
//      for (int i = 1; i < cr.ITCount(); i++)
//          SLToken.Add("IT" + i);
//      SLToken.Add("FT");
//      s = SLToken.getDelimitedText();
//      SL.Add(s);
//
//      // DataLines
//      for (int i = 0; i < cl.getCount(); i++) {
//          cr = cl.get(i);
//          SLToken.Clear();
//          SLToken.Add("" + cr.SNR);
//          SLToken.Add("" + cr.Bib);
//          for (int t = 1; t < cr.ITCount(); t++) {
//              tp = cr.Item(t);
//              SLToken.Add("" + tp.getOTime().toString());
//          }
//          tp = cr.FT;
//          SLToken.Add(tp.getOTime().toString());
//          s = SLToken.getDelimitedText();
//          SL.Add(s);
//      }

    }

    public void GetCaptionList() {
        SL.Clear();
        SLToken.Clear();
        SLToken.setDelimiter(Delimiter);
        SL.setText(TBaseColProps.ColCaptionBag.getText());
    }

}
