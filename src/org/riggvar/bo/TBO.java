package org.riggvar.bo;

import java.util.ArrayList;

import org.riggvar.event.*;
import org.riggvar.eventkey.KeyMatrix;
import org.riggvar.input.*;
import org.riggvar.output.*;
import org.riggvar.penalty.*;
import org.riggvar.race.*;
import org.riggvar.stammdaten.*;
import org.riggvar.base.*;
import org.riggvar.calc.*;
import org.riggvar.col.*;
import org.riggvar.conn.*;

public class TBO extends TBaseBO implements IMsgEvent {
    public boolean UseInputFilter;
    public boolean UseOutputFilter;
    public boolean UseCompactFormat = true;
    public String ConvertedData;

    private boolean FModified;
    private ArrayList<IBaseNode> FNodeList;
    public INotifyEvent OnCalc = null;

    private TStrings FSLBackup;
    protected boolean FLoading;

    public int CounterCalc;
    public String BackupDir;
    public int MsgCounter;

    public TStammdatenBO StammdatenBO;
    public TStammdatenNode StammdatenNode;

    public TEventBO EventBO;
    public TEventNode EventNode;

    public TRaceBO RaceBO;
    public TRaceNode[] RNode;

    public TCalcEvent CalcEV;
//    public TCalcTP CalcTP;

    public TBOParams BOParams;

//    public TJavaScoreXML JavaScoreXML;
    public TRaceDataXML RaceDataXML;
    public TEventProps EventProps;
    public TStatusFeedback StatusFeedback;

    public TUndoManager UndoManager;
    public IConnection UndoConnection;
    public TExcelImporter ExcelImporter;
    public TLocalWatches LocalWatches;

    public TUndoAgent UndoAgent;
    public TMsgTree MsgTree;

    @Override
    public TAdapterWatches getWatches() {
        return LocalWatches;
    }

    public boolean isLoading() {
        return FLoading;
    }

    public static String getBackupDir() {
        if (TMain.Controller.HaveLocalAccess()) {
            return TMain.FolderInfo.getBackupPath();
        } else {
            return "no local Access";
        }
    }

    public TBO(TAdapterParams aParams) {
        super(aParams);
        TMain.BO = this;

        TColCaptions.InitDefaultColCaptions();

        cTokenFleetRace = "FR";

        if (AdapterParams instanceof TBOParams) {
            BOParams = (TBOParams) AdapterParams;
        } else {
            BOParams = new TBOParams();
            AdapterParams = BOParams;
        }
        BackupDir = getBackupDir();

        SetDivisionName(BOParams.DivisionName);

        CalcEV = TCalcEvent.getInstance();
//        CalcTP = new TCalcTP(this);

        FNodeList = new ArrayList<IBaseNode>();
        OnCalc = new TBODelegate(this, TBODelegate.commandModified);

        UndoAgent = new TUndoAgent();
        MsgTree = new TMsgTree(cTokenFleetRace, 0);

        // Athlete data
        StammdatenBO = new TStammdatenBO();
        StammdatenNode = new TStammdatenNode(StammdatenBO, "Stammdaten");
        StammdatenBO.CurrentNode = StammdatenNode;

        // EventNode
        EventBO = new TEventBO();
        EventNode = new TEventNode(EventBO, "Event");
        EventNode.NameID = "E";
        EventNode.StammdatenRowCollection = StammdatenNode.getBaseRowCollection();
        EventBO.setCurrentNode(EventNode);
        FNodeList.add(EventNode);

        // Race
        RaceBO = new TRaceBO();
        RNode = new TRaceNode[BOParams.RaceCount + 1];
        for (int i = 0; i <= BOParams.RaceCount; i++) {
            TRaceNode ru = new TRaceNode();
            RNode[i] = ru;
        }

        InitStartlistCount(BOParams.StartlistCount);

        Output = new TOutput();

        IServer ts;
        try {
            ts = new TServerIntern(3027, TServerFunction.Input);
            InputServer = new TInputNCP(ts);
            ts = new TServerIntern(3028, TServerFunction.Output);
            OutputServer = new TOutputNCP(ts);
        } catch (Exception ex) {
            InputServer = null;
            OutputServer = null;
        }

//        JavaScoreXML = new TJavaScoreXML(this);
        RaceDataXML = new TRaceDataXML(this);
        EventProps = new TEventProps(this);
        StatusFeedback = new TStatusFeedback(this);

        UndoManager = new TUndoManager(this);
        UndoConnection = InputServer.Server.Connect();

        LocalWatches = new TLocalWatches();
        ExcelImporter = new TExcelImporter();
    }

    public void Dispose(boolean disposing) {
    }

    public int getSNR(int Index) {
        TEventRowCollectionItem cr = EventNode.getBaseRowCollection().get(Index);
        if (cr != null) {
            return cr.SNR;
        } else {
            return -1;
        }
    }

    public int getBib(int Index) {
        TEventRowCollectionItem cr = EventNode.getBaseRowCollection().get(Index);
        if (cr != null) {
            return cr.Bib;
        } else {
            return -1;
        }
    }

    public int getQU(int RaceIndex, int Index) {
        TEventRowCollectionItem cr = EventNode.getBaseRowCollection().get(Index);
        if (cr != null) {
            return cr.Race[RaceIndex].getQU();
        } else {
            return 0;
        }
    }

    public int getDG(int RaceIndex, int Index) {
        TEventRowCollectionItem cr = EventNode.getBaseRowCollection().get(Index);
        if (cr != null) {
            return cr.Race[RaceIndex].DG;
        } else {
            return 0;
        }
    }

    public int getOT(int RaceIndex, int Index) {
        TEventRowCollectionItem cr = EventNode.getBaseRowCollection().get(Index);
        if (cr != null) {
            return cr.Race[RaceIndex].OTime;
        } else {
            return 0;
        }
    }

    public void setSNR(int Index, int Value) {
        TEventRowCollectionItem cr = EventNode.getBaseRowCollection().get(Index);
        if (cr != null) {
            cr.SNR = Value;
        }
//        for (int i = 0; i <= BOParams.RaceCount; i++)
//        {
//            TRaceRowCollectionItem wr = RNode[i].getBaseRowCollection().get(Index);
//            if (wr != null)
//            {
//                wr.SNR = Value;
//            }
//        }
    }

    public void setBib(int Index, int Value) {
        TEventRowCollectionItem cr = EventNode.getBaseRowCollection().get(Index);
        if (cr != null) {
            cr.Bib = Value;
        }
//        for (int i = 0; i <= BOParams.RaceCount; i++)
//        {
//            TRaceRowCollectionItem wr = RNode[i].getBaseRowCollection().get(Index);
//            if (wr != null)
//            {
//                wr.Bib = Value;
//            }
//        }
    }

    public void setQU(int RaceIndex, int Index, int Value) {
        TEventRowCollectionItem cr = EventNode.getBaseRowCollection().get(Index);
        if (cr != null) {
            cr.Race[RaceIndex].setQU(Value);
            cr.setModified();
        }
//        TRaceRowCollectionItem wr = RNode[RaceIndex].getBaseRowCollection().get(Index);
//        if (wr != null)
//        {
//            wr.QU.setAsInteger(Value);
//            wr.setModified();
//        }
    }

    public void setDG(int RaceIndex, int Index, int Value) {
        TEventRowCollectionItem cr = EventNode.getBaseRowCollection().get(Index);
        if (cr != null) {
            cr.Race[RaceIndex].DG = Value;
            cr.setModified();
        }
//        TRaceRowCollectionItem wr = RNode[RaceIndex].getBaseRowCollection().get(Index);
//        if (wr != null)
//        {
//            wr.DG = Value;
//            wr.setModified();
//        }
    }

    public void setOT(int RaceIndex, int Index, int Value) {
        TEventRowCollectionItem cr = EventNode.getBaseRowCollection().get(Index);
        if (cr != null) {
            cr.Race[RaceIndex].OTime = Value;
            cr.setModified();
        }
//        TRaceRowCollectionItem wr = RNode[RaceIndex].getBaseRowCollection().get(Index);
//        if (wr != null)
//        {
//            wr.MRank = Value;
//            wr.setModified();
//        }
    }

    public TPenalty getPenalty(int RaceIndex, int Index) {
        TEventRowCollectionItem cr = EventNode.getBaseRowCollection().get(Index);
        if (cr != null) {
            return cr.Race[RaceIndex].getPenalty();
        } else {
            return null;
        }
    }

    public void setPenalty(int RaceIndex, int Index, TPenalty Value) {
        TEventRowCollectionItem cr = EventNode.getBaseRowCollection().get(Index);
        if (cr != null) {
            cr.Race[RaceIndex].getPenalty().Assign(Value);
            cr.setModified();
        }
//        TRaceRowCollectionItem wr = RNode[RaceIndex].getBaseRowCollection().get(Index);
//        if (wr != null)
//        {
//            wr.QU.Assign(Value);
//            wr.setModified();
//        }
    }

    public int getGemeldet() {
        return this.EventNode.getBaseRowCollection().getCount();
    }

    public int getGesegelt() {
        return BOParams.RaceCount;
    }

    public int Gezeitet = 8;

    public void HandleMsg(Object sender, String s) {
        FSLBackup.Add(s);
    }

    private IBaseNode FindBaseNode(String rd) {
        for (IBaseNode bn : FNodeList) {
            if (bn.getNameID().equals(rd))
                return bn;
        }
        return null;
    }

    private void ClearList(String rd) {
        IBaseNode bn = FindBaseNode(rd);
        if (bn != null) {
            bn.ClearList();
            bn.setModified();
        } else {
            for (IBaseNode n : FNodeList) {
                n.ClearList();
            }
        }
    }

    private void ClearResult(String rd) {
        IBaseNode bn = FindBaseNode(rd);
        if (bn != null) {
            bn.ClearResult();
            bn.Calc();
        } else {
            for (IBaseNode n : FNodeList) {
                n.ClearResult();
                if (!FLoading) {
                    n.Calc();
                }
            }
        }
    }

    public void setModified(Object sender) {
        FModified = true;
    }

    private void CalcEvent() {
        CalcNodes();
        CounterCalc++;
        FModified = false;
    }

    private void CalcNodes() {
        for (IBaseNode n : FNodeList) {
            CalcNode(n);
        }
    }

    private void CalcNode(IBaseNode bn) {
        if (bn.getModified()) {
            bn.Calc();
        }
    }

    private void InitStartlistCount(int newCount) {
//        for (int i = 0; i <= BOParams.RaceCount; i++)
//        {
//            RNode[i].Init(newCount);
//        }
        EventNode.Init(newCount);
        KeyMatrix.BibCount = newCount;
    }

    public boolean UpdateStartlistCount(String roName, int newCount) {
        boolean result = false;
        TEventRowCollection cl = EventNode.getBaseRowCollection();
        if ((cl.getCount() < newCount) && (newCount <= BOParams.MaxStartlistCount)) {
            while (cl.getCount() < newCount) {
//                for (int i = 0; i <= BOParams.RaceCount; i++)
//                {
//                    RNode[i].getBaseRowCollection().AddRow();
//                }
                EventNode.getBaseRowCollection().AddRow();
            }
            result = true;
        }
        if ((cl.getCount() > newCount) && (newCount >= BOParams.MinStartlistCount)) {
            while (cl.getCount() > newCount) {
                int c = cl.getCount();
//                for (int i = 0; i <= BOParams.RaceCount; i++)
//                {
//                    RNode[i].getBaseRowCollection().Delete(c - 1);
//                }
                EventNode.getBaseRowCollection().Delete(c - 1);
            }
            result = true;
        }
        BOParams.StartlistCount = cl.getCount();
        return result;
    }

    public boolean UpdateAthlete(int SNR, String Cmd, String Value) {
        TStammdatenRowCollectionItem cr;

        cr = StammdatenNode.getBaseRowCollection().FindKey(SNR);
        if (cr == null) {
            cr = StammdatenNode.getBaseRowCollection().AddRow();
            cr.BaseID = StammdatenNode.getBaseRowCollection().getCount();
            cr.SNR = SNR;
        }

        TStammdatenBO bo = StammdatenBO;

        if (Utils.Pos("Prop_", Cmd) > 0) {
            String Key = Utils.Copy(Cmd, 6, Cmd.length());
            cr.Props.setValue(Key, Value);
        } else if (Cmd.equals(FieldNames.FN) || Cmd.equals("FN")) {
            Value = bo.editFN(cr, Value);
        } else if (Cmd.equals(FieldNames.LN) || Cmd.equals("LN")) {
            Value = bo.editLN(cr, Value);
        } else if (Cmd.equals(FieldNames.SN) || Cmd.equals("SN")) {
            Value = bo.editSN(cr, Value);
        } else if (Cmd.equals(FieldNames.NC) || Cmd.equals("NC")) {
            Value = bo.editNC(cr, Value);
        } else if (Cmd.equals(FieldNames.GR) || Cmd.equals("GR")) {
            Value = bo.editGR(cr, Value);
        } else if (Cmd.equals(FieldNames.PB) || Cmd.equals("PB")) {
            Value = bo.editPB(cr, Value);
        } else if (Cmd.startsWith("N")) {
            bo.editNameColumn(cr, Value, "col_" + Cmd);
        }
        return true;
    }

    public TRaceNode FindNode(String roName) {
        if (!(Utils.Copy(roName, 1, 1).equals("W"))) {
            return null;
        }
        String s = Utils.Copy(roName, 2, roName.length());
        int i = Utils.StrToIntDef(s, -1);
        if ((i < 1) || (i > BOParams.RaceCount)) {
            return null;
        }
        return RNode[i];
    }

    @Override
    public String Save() {
        String result = "";
        FSLBackup = new TDBStringList();
        try {
            BackupToSL(null);
            result = FSLBackup.getText();
        } finally {
            // FSLBackup.Free;
            FSLBackup = null;
        }
        return result;
    }

    public void Load(String Data) {
        FLoading = true; // avoid calling calc in Clear()-->ClearResult()

        Clear();

        TStringList m = new TStringList();
        TBOMsg msg = new TBOMsg();

        try {
            ExcelImporter.RunImportFilter(Data, m);
            ConvertedData = m.getText();

            for (int i = 0; i < m.getCount(); i++) {
                String s = m.getString(i);
                msg.Prot = s;
                try {

                    if (!msg.DispatchProt()) {
                        FRTrace.Trace("MessageError: " + s);
                    }
                } catch (Exception ex) {
                    System.out.println("problem msg: " + s);
                    ex.printStackTrace();
                }

            }
//                InitEventNode();
        } finally {
            FLoading = false;
            // m.Free;
            // msg.Free;
        }
    }

    @Override
    public void LoadNew(String Data) {
        Load(Data);
    }

    @Override
    public void Clear() {
        ClearBtnClick();
    }

    public void Backup(String aFileName) {
        FSLBackup = new TDBStringList();
        try {
            BackupToSL(null);
            if (TMain.Controller.HaveLocalAccess()) {
                FSLBackup.SaveToFile(aFileName);
            }
        } finally {
            // FSLBackup.Free;
            FSLBackup = null;
        }
    }

    public void Restore(String aFileName) {
        // ###
        // difference to Load: 1. no Clear(), 2. data from file

        // Clear();

        TStringList m = new TDBStringList();
        TBOMsg msg = new TBOMsg();

        FLoading = true;
        try {
            m.LoadFromFile(aFileName); // m.Text = Data;
            for (int i = 0; i < m.getCount(); i++) {
                String s = m.getString(i);
                msg.Prot = s;
                if (!msg.DispatchProt()) {
                    FRTrace.Trace("MessageError: " + s);
                }
            }
//            InitEventNode();
        } finally {
            FLoading = false;
            // m.Free;
            // msg.Free;
        }
    }

    public void BackupAthletesHack(TMsgTree Model) {
        BackupAthletes(Model);
    }

    public void BackupAthletes(TMsgTree Model) {
        int savedSchemaCode = FieldNames.getSchemaCode();
        if (this.EventProps.NormalizeOutput)
            FieldNames.setSchemaCode(2);

        TStammdatenRowCollection cl = StammdatenNode.getBaseRowCollection();
        TStammdatenRowCollectionItem cr;
        TProp prop = new TProp();

        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            if (!cr.getFN().equals("")) {
                Model.Division.Athlete(cr.SNR).FN(cr.getFN());
            }
            if (!cr.getLN().equals("")) {
                Model.Division.Athlete(cr.SNR).LN(cr.getLN());
            }
            if (!cr.getSN().equals("")) {
                Model.Division.Athlete(cr.SNR).SN(cr.getSN());
            }
            if (!cr.getNC().equals("")) {
                Model.Division.Athlete(cr.SNR).NC(cr.getNC());
            }
            if (!cr.getGR().equals("")) {
                Model.Division.Athlete(cr.SNR).GR(cr.getGR());
            }
            if (!cr.getPB().equals("")) {
                Model.Division.Athlete(cr.SNR).PB(cr.getPB());
            }
            if (cl.getFieldCount() > TStammdatenRowCollection.FixFieldCount) {
                for (int j = TStammdatenRowCollection.FixFieldCount + 1; j <= cl.getFieldCount(); j++) {
                    Model.Division.Athlete(cr.SNR).FieldN(j, cr.getFieldValue(j));
                }
            } else {
                for (int j = 0; j < cr.Props.getCount(); j++) {
                    cr.Props.getProp(j, prop); // ###
                    Model.Division.Athlete(cr.SNR).Prop(prop.Key, prop.Value);
                }
            }
            if (FSLBackup != null)
                FSLBackup.Add("");
        }
        FieldNames.setSchemaCode(savedSchemaCode);
    }

    @Override
    public String toString() {
        return toTXT();
    }

    public String toTXT() {
        TStrings SL = new TStringList();
        try {
            BackupToSL(SL);
            return SL.getText();
        } catch (Exception ex) {
            return "";
        }
    }

    public String toXML() {
        // TODO: implement TBO.ToXML()
        return "<EventData>not implemented</EventData>";
    }

    @Override
    public void BackupToSL(TStrings SL) {
        TInputAction InputAction;
        TGender g;
        TRun r;

        TRaceNode rn;
        TEventNode en;
        TEventRowCollection cl;
        TEventRowCollectionItem cr;
        TEventRaceEntry ere;

//        UpdateRaceNodes();

        if (SL != null)
            FSLBackup = SL;

        InputAction = new TInputAction();
        InputAction.OnSend = this; // new TInputActionEvent(SaveLine);
        TInputActionManager.DynamicActionRef = InputAction;

        try {
            FSLBackup.Add("#Params");
            FSLBackup.Add("");
            FSLBackup.Add("DP.StartlistCount = " + Utils.IntToStr(BOParams.StartlistCount));
            FSLBackup.Add("DP.ITCount = " + Utils.IntToStr(BOParams.ITCount));
            FSLBackup.Add("DP.RaceCount = " + Utils.IntToStr(BOParams.RaceCount));

            // EventProps
            FSLBackup.Add("");
            FSLBackup.Add("#Event Properties");
            FSLBackup.Add("");

            EventProps.SaveProps(FSLBackup);

            TExcelExporter o = new TExcelExporter();
            o.Delimiter = ';';

            // CaptionList
            if (TBaseColProps.ColCaptionBag.isPersistent() && TBaseColProps.ColCaptionBag.getCount() > 0) {
                FSLBackup.Add("");
                o.AddSection(TExcelImporter.TableID_CaptionList, this, FSLBackup);
            }

            en = EventNode;
            cl = en.getBaseRowCollection();

            if (UseCompactFormat) {
                try {
                    // NameList
                    FSLBackup.Add("");
                    o.AddSection(TExcelImporter.TableID_NameList, this, FSLBackup);

                    // StartList
                    FSLBackup.Add("");
                    o.AddSection(TExcelImporter.TableID_StartList, this, FSLBackup);

                    // FleetList
                    if (EventNode.UseFleets) {
                        FSLBackup.Add("");
                        o.AddSection(TExcelImporter.TableID_FleetList, this, FSLBackup);
                    }

                    // FinishList
                    FSLBackup.Add("");
                    o.AddSection(TExcelImporter.TableID_FinishList, this, FSLBackup);

                    // TimeList(s)
                    if (BOParams.ITCount > 0 || EventProps.IsTimed) {
                        FSLBackup.Add("");
                        o.AddSection(TExcelImporter.TableID_TimeList, this, FSLBackup);
                    }
                } catch (Exception ex) {
                }
            }

            else {

                // Athletes
                FSLBackup.Add("");
                FSLBackup.Add("#Athletes");
                FSLBackup.Add("");

                if (UseOutputFilter)
                    BackupAthletesHack(MsgTree);
                else
                    BackupAthletes(MsgTree);

                // Startlist
                FSLBackup.Add("");
                FSLBackup.Add("#Startlist");
                FSLBackup.Add("");

                g = MsgTree.Division;
                for (int i = 0; i < cl.getCount(); i++) {
                    cr = cl.get(i);
                    if ((cr.Bib > 0) && (cr.Bib != cr.BaseID)) {
                        g.Race1.Startlist.Pos(cr.BaseID).Bib(Utils.IntToStr(cr.Bib));
                    }
                    if (cr.SNR > 0) {
                        g.Race1.Startlist.Pos(cr.BaseID).SNR(Utils.IntToStr(cr.SNR));
                    }
                }
            }

            // Results
            for (int n = 1; n <= BOParams.RaceCount; n++) {
                FSLBackup.Add("");
                FSLBackup.Add("#" + cTokenRace + Utils.IntToStr(n));
                FSLBackup.Add("");

                rn = RNode[n];
                g = MsgTree.Division;
                if (n == 1) {
                    r = g.Race1;
                } else if ((n > 1) && (n <= BOParams.RaceCount)) {
                    r = g.Race(n);
                } else {
                    r = null;
                }
                if (r == null) {
                    continue;
                }
                if (!rn.IsRacing) {
                    r.IsRacing(Utils.BoolStr(false));
                }
                for (int i = 0; i < cl.getCount(); i++) {
                    cr = cl.get(i); // Indexer Items
                    ere = cr.Race[n];

                    if (!UseCompactFormat) {
                        if (ere.OTime > 0)
                            r.Bib(cr.Bib).Rank(Utils.IntToStr(ere.OTime));

                        if (EventNode.UseFleets) {
                            int f = ere.Fleet;
                            if (f > 0)
                                r.Bib(cr.Bib).FM("" + f);
                        }
                    }

                    if (EventNode.UseFleets) {
                        if (!ere.IsRacing)
                            r.Bib(cr.Bib).RV("x");
                    }

                    if (ere.getQU() != 0) {
                        r.Bib(cr.Bib).QU(ere.getPenalty().toString());
                    }
                    if (ere.DG > 0) {
                        r.Bib(cr.Bib).DG(Utils.IntToStr(ere.DG));
                    }
                }
            }

            FSLBackup.Add("");
            FSLBackup.Add("EP.IM = " + TInputMode.getString(EventProps.getInputMode()));

            // Errors
            EventNode.ErrorList.CheckAll(EventNode);
            if (EventNode.ErrorList.hasErrors()) {
                FSLBackup.Add("");
                FSLBackup.Add("#Errors");
                FSLBackup.Add("");
                EventNode.ErrorList.getMsg(FSLBackup);
            }
        } finally {
            if (SL != null)
                FSLBackup = null;
            TInputActionManager.DynamicActionRef = null;
        }
    }

    public void BackupBtnClick() {
        String fn = BackupDir + "_Backup.txt";
        Backup(fn);
    }

    public void RestoreBtnClick() {
        Clear();
        String fn = BackupDir + "_Backup.txt";
        Restore(fn);
    }

    public void ClearBtnClick() {
        ClearResult("");
        ClearList("");
//        UpdateEventNode();
    }

    @Override
    public boolean Calc() {
        CalcNodes();
        boolean result = FModified;
        if (FModified) {
            CalcEvent();
        }
        // UpdateEventNode(EventNode);
        return result;
    }

    @Override
    public void OnIdle() {
        Calc();
        // pass any input message on to output clients
        if (InputServer != null) {
            InputServer.ProcessQueue();
        }
    }

    public void LoadTestData() {
        return;
        // only for debugging, because BOParams will not adjust

        /*
         * Clear(); TDataSender DataSender = new TDataSender(this);
         * DataSender.SendTestData(); EventProps.Import(); InitEventNode();
         */
    }

    public void LoadTestStartList() {
    }

    public void CreateTimestampedBackupBtnClick() {
        String sTime = TDateTime.Now().ToString("%yyMMdd_HHmmss");
        String fn = BackupDir + "_Backup_" + sTime + ".txt";
        Backup(fn);
    }

//    public void CopyFromRaceNode(TRaceNode ru, boolean MRank)
//    {
//        int RaceIndex = ru.Index;
//        TEventRowCollection wl = EventNode.getBaseRowCollection();
//        TRaceRowCollection cl = ru.getBaseRowCollection();
//        TEventRowCollectionItem wr;
//        TRaceRowCollectionItem cr;
//        for (int i = 0; i < cl.getCount(); i++)
//        {
//            cr = cl.get(i);
//            wr = wl.get(i);
//            //wr.Race[RaceIndex].QU = cr.QU.AsInteger;
//            wr.Race[RaceIndex].getPenalty().Assign(cr.QU);
//            wr.Race[RaceIndex].DG = cr.DG;
//            if (MRank)
//            {
//                wr.Race[RaceIndex].OTime = cr.MRank;
//            }
//            else
//            {
//                wr.Race[RaceIndex].OTime = cr.FT.ORank;
//            }
//        }
//        wl.getBaseNode().setModified(true);
//    }

//    public void CopyToRaceNode(TRaceNode ru)
//    {
//        int RaceIndex = ru.Index;
//        TEventRowCollection wl = EventNode.getBaseRowCollection();
//        TRaceRowCollection cl = ru.getBaseRowCollection();
//        TEventRowCollectionItem wr;
//        TRaceRowCollectionItem cr;
//        for (int i = 0; i < cl.getCount(); i++)
//        {
//            cr = cl.get(i);
//            wr = wl.get(i);
//            //cr.QU.AsInteger = wr.Race[RaceIndex].QU;
//            cr.QU.Assign(wr.Race[RaceIndex].getPenalty());
//            cr.DG = wr.Race[RaceIndex].DG;
//            cr.MRank = wr.Race[RaceIndex].OTime;
//        }
//    }

//    public void CopyOTimes(int RaceIndex)
//    {
//        TEventRowCollection wl = EventNode.getBaseRowCollection();
//        TRaceRowCollection cl = RNode[RaceIndex].getBaseRowCollection();
//        TEventRowCollectionItem wr;
//        TRaceRowCollectionItem cr;
//        for (int i = 0; i < cl.getCount(); i++)
//        {
//            cr = cl.get(i);
//            wr = wl.get(i);
//            cr.MRank = wr.Race[RaceIndex].OTime;
//        }
//    }

//    public void InitEventNode()
//    {
//        for (int i = 1; i <= BOParams.RaceCount; i++)
//        {
//            CopyFromRaceNode(RNode[i], true);
//        }
//    }

//    public void UpdateEventNode()
//    {
//        for (int i = 1; i <= BOParams.RaceCount; i++)
//        {
//            CopyFromRaceNode(RNode[i], false);
//        }
//    }

//    public void UpdateRaceNodes()
//    {
//        for (int i = 1; i <= BOParams.RaceCount; i++)
//        {
//            CopyToRaceNode(RNode[i]);
//        }
//    }

//    public void RebuildEventNode()
//    {
//        TEventRowCollection wl = EventNode.getBaseRowCollection();
//        wl.clear();
//        TRaceRowCollection cl = RNode[0].getBaseRowCollection();
//        TEventRowCollectionItem wr;
//        TRaceRowCollectionItem cr;
//        for (int i = 0; i < cl.getCount(); i++)
//        {
//            cr = cl.get(i);
//            wr = wl.AddRow();
//            wr.BaseID = i + 1;
//            wr.SNR = cr.SNR;
//            wr.Bib = cr.Bib;
//        }
//        UpdateEventNode();
//    }

    public String getHashString() {
        return EventNode.getBaseRowCollection().GetHashString();
    }

    public void ClearCommand() {
        ClearBtnClick();
        UndoManager.Clear();
    }

    @Override
    public TBaseMsg NewMsg() {
        return new TBOMsg();
    }
}
