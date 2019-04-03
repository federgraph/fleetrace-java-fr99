package org.riggvar.calc;

import org.riggvar.event.*;
import org.riggvar.scoring.*;
import org.riggvar.base.*;
import org.riggvar.bo.*;

public class TCalcEventProxy03 extends TCalcEventProxy {
    public String EventName = "";
    private TEventNode qn;
    private TEventProps EventProps;
    private TFRProxy p;

    public TCalcEventProxy03() {
    }

    @Override
    public void Calc(TEventNode aqn) {
        qn = aqn;
        EventProps = TMain.BO.EventProps;

        if (qn.getBaseRowCollection().size() == 0) {
            return;
        }

        p = new TFRProxy();
        LoadProxy();
        ScoreRegatta(p);
        if (isWithTest()) {
            setWithTest(false);
            CheckResult(p);
        }
        UnLoadProxy();
        p = null;
    }

    private void LoadProxy() {
        if (qn == null) {
            return;
        }

        EventName = TMain.BO.EventProps.EventName;

        TJSEventProps ep = p.EventProps;
        ep.ScoringSystem = EventProps.ScoringSystem;
        ep.ThrowoutScheme = EventProps.ThrowoutScheme;
        ep.Throwouts = EventProps.Throwouts;
        ep.FirstIs75 = EventProps.FirstIs75;
        ep.ReorderRAF = EventProps.ReorderRAF;
        ep.DivisionName = EventProps.getDivisionName();

        p.setRCount(qn.getRCount());
        for (int r = 1; r < qn.getRCount(); r++) {
            if (TMain.BO.RNode[r].IsRacing) {
                p.IsRacing[r] = true;
            } else {
                p.IsRacing[r] = false;
            }
            p.UseFleets = qn.UseFleets;
            p.TargetFleetSize = qn.TargetFleetSize;
            p.FirstFinalRace = qn.getFirstFinalRace();
        }

        TEventRowCollection cl;
        TEventRowCollectionItem cr;
        TEntryInfo ei;
        TRaceInfo ri;
        TEventRaceEntry er;
        p.EntryInfoCollection.Clear();
        cl = qn.getBaseRowCollection();
        for (int i = 0; i < cl.size(); i++) {
            cr = cl.get(i);
            ei = p.EntryInfoCollection.Add();
            ei.SNR = cr.SNR;
            ei.RaceList.clear();
            for (int r = 0; r < cr.RCount(); r++) {
                ri = new TRaceInfo();
                ei.RaceList.add(ri);
                if (r == 0) {
                    continue;
                }
                er = cr.Race[r];
                ri.OTime = er.OTime;
                ri.QU = er.getQU();
                ri.Penalty_Points = er.getPenalty().Points;
                ri.Penalty_Note = er.getPenalty().Note;
                ri.Penalty_Percent = er.getPenalty().Percent;
                ri.Penalty_TimePenalty = er.getPenalty().TimePenalty;
                ri.Fleet = er.Fleet;
                ri.IsRacing = er.IsRacing;
            }
        }
    }

    private void UnLoadProxy() {
        if (qn == null) {
            return;
        }

        try {
            TEventRowCollection cl = qn.getBaseRowCollection();
            TEventRowCollectionItem cr;
            TEntryInfo ei;
            TRaceInfo ri;
            TEventRaceEntry er;
            for (int i = 0; i < cl.getCount(); i++) {
                cr = cl.get(i);
                ei = p.EntryInfoCollection.Items(i);
                cr.isTied = ei.IsTied;
                cr.isGezeitet = ei.IsGezeitet;

                for (int r = 0; r < cr.RCount(); r++) {
                    ri = ei.Race(r);
                    er = cr.Race[r];
                    er.setCPoints(ri.CPoints);
                    er.Drop = ri.Drop;
                    er.Rank = ri.Rank;
                    er.PosR = ri.PosR;
                    er.PLZ = ri.PLZ;
                }
            }
            TMain.BO.Gezeitet = p.Gezeitet;
        } catch (Exception ex) {
        }
    }

    protected void ScoreRegatta(TFRProxy p) {
        TProxyLoader.getInstance().Calc(p);
    }

    private void CheckResult(TFRProxy p) {
        String fn = "FRResult\\" + EventName + ".xml";
        try {
            XmlWriter xw = new XmlWriter();
            p.WriteXml(xw);
            TStringList SL = new TStringList();
            SL.setText(xw.toString());
            if (TMain.Controller.HaveLocalAccess()) {
                SL.SaveToFile(fn);
            }
        } catch (Exception ex) {
            System.out.println("cannot save " + fn);
        }
    }

    public String GetProxyXmlInput(TEventNode aqn) {
        qn = aqn;
        EventProps = TMain.BO.EventProps;

        if (qn.getBaseRowCollection().getCount() == 0)
            return "";

        try {
            p = new TFRProxy();
            LoadProxy();
            XmlWriter xw = new XmlWriter();
            p.WriteXml(xw);
            return xw.toString();
        } catch (Exception ex) {
            // TCalcEvent._ScoringResult = -1;
            // TCalcEvent._ScoringExceptionLocation = "TCalcEventProxy3.GetProxyXml1";
            // TCalcEvent._ScoringExceptionMessage = ex.Message;
        }
        return "";
    }

}
