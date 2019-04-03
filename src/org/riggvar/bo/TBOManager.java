package org.riggvar.bo;

import java.io.File;

import org.riggvar.base.*;

public class TBOManager implements IBOFactory, IBOConnector, IBOContainer {
    private TStringList SL;

    public TAdapterBO AdapterBO = null;
    public boolean ConnectedBO = false;

    public TBOManager() {
        TMain.MsgListFactory = new TBOMsgFactory();

        SL = new TStringList();
        GetDefaultData(); // initialize SL
        CreateNew(SL); // with parameters from SL
        TMain.BO.Load(SL.getText()); // ignore parameters in SL
    }

    public void CreateAdapterBO() {
        TBOParams params = new TBOParams();
        params.IsAdapter = true;

        AdapterBO = new TAdapterBO(params);

        FRTrace.Trace("InputServer.Port := " + Utils.IntToStr(AdapterBO.InputServer.Server.Port()) + ";");
        FRTrace.Trace("OutputServer.Port := " + Utils.IntToStr(AdapterBO.OutputServer.Server.Port()) + ";");
    }

    public void CreateBO(TAdapterParams aboParams) {
        if (TMain.BO == null) {
            TBOParams params = new TBOParams();
            params.Assign(aboParams);
            params.ForceWithinLimits();
            params.IsAdapter = false;
            TMain.BO = new TSDIBO(params);
        }
    }

    public void DeleteBO() {
        if (TMain.BO != null) {
            if (ConnectedBO) {
                DisconnectBO();
            }
            TMain.BO.Dispose(true);
            TMain.BO = null;
        }
    }

    public void ConnectBO() {
        if (TMain.BO != null && AdapterBO != null) {
            AdapterBO.AdapterInputConnection = TMain.BO.InputServer.Server.Connect();
            AdapterBO.AdapterOutputConnection = TMain.BO.OutputServer.Server.Connect();
            AdapterBO.AdapterOutputConnection.SetOnSendMsg(AdapterBO); // IHandleMsgEvent.setMsg
            ConnectedBO = true;
        } else {
            DisconnectBO();
        }
    }

    public void DisconnectBO() {
        if (AdapterBO != null) {
            if (AdapterBO.AdapterInputConnection != null)
                AdapterBO.AdapterInputConnection.Delete();
            if (AdapterBO.AdapterOutputConnection != null)
                AdapterBO.AdapterOutputConnection.Delete();
            AdapterBO.AdapterInputConnection = null;
            AdapterBO.AdapterOutputConnection = null;
        }
        ConnectedBO = false;
    }

    public boolean getConnected() {
        if (AdapterBO != null) {
            return ((AdapterBO.InputServer.Server.Status() == TBaseServer.Status_Active)
                    && (AdapterBO.OutputServer.Server.Status() == TBaseServer.Status_Active));
        } else {
            return false;
        }
    }

    public boolean getConnectedBO() {
        return ConnectedBO;
    }

    /**
     * create BO with parameters in SL but do not (yet) load the data
     * 
     * @param SL TStrings
     */
    public void CreateNew(TStrings SL) {
        DeleteBO();
        TBOParams BOParams = new TBOParams();
        String n, v;
        int paramsRead = 0;
        for (int i = 0; i < SL.getCount(); i++) {
            n = SL.getName(i).trim();
            v = SL.getValueFromIndex(i).trim();
            if (n.equals("DP.StartlistCount") || n.equals("Event.StartlistCount")) {
                BOParams.StartlistCount = Utils.StrToIntDef(v, BOParams.StartlistCount);
                paramsRead++;
            } else if (n.equals("DP.ITCount") || n.equals("Event.ITCount")) {
                BOParams.ITCount = Utils.StrToIntDef(v, BOParams.ITCount);
                paramsRead++;
            } else if (n.equals("DP.RaceCount") || n.equals("Event.RaceCount")) {
                BOParams.RaceCount = Utils.StrToIntDef(v, BOParams.RaceCount);
                paramsRead++;
            } else if (n.equals("EP.DivisionName") || n.equals("Event.Prop_DivisionName")) {
                BOParams.DivisionName = v;
                paramsRead++;
            }
            if (paramsRead == 4) {
                break;
            }
        }
        CreateBO(BOParams);
    }

    /**
     * Create a new BO with 'the parameters and data' taken from a stream of text.
     * 
     * @param Data - the data given as a String
     */
    public void LoadNew(String Data) {
        TStrings ml = new TStringList();
        try {
            ml.setText(Data);
        } catch (Exception ex) {
            ml.setText("");
        }
        CreateNew(ml);
        TMain.BO.Load(ml.getText());
    }

    /**
     * Create a new BO with new parameters, then load 'old' data.
     * 
     * @param BOParams - An object of type TAdapterParams, holding the new
     *                 parameters.
     */
    public void RecreateBO(TAdapterParams BOParams) {
        TStrings ml = new TStringList();
        try {
            ml.setText(TMain.BO.Save());
        } catch (Exception ex) {
            ml.Clear();
        }
        TBOParams tempParams = new TBOParams();
        tempParams.Assign(BOParams);
        DeleteBO();
        CreateBO(tempParams);
        TMain.BO.Load(ml.getText());
    }

    /**
     * create BO with parameters and data from backup in standard location
     */
    public void RecreateBOFromBackup() {
        String fn = TMain.BO.BackupDir + "_Backup.txt";
        File fi = new File(fn);
        if (!fi.exists())
            return;
        TStringList ml = new TDBStringList();
        ml.LoadFromFile(fn);
        CreateNew(ml);
        TMain.BO.Load(ml.getText());
        TMain.BO.Calc();
        TMain.BO.UndoManager.Clear();
    }

    public String GetTestData() {
        return SL.getText();
    }

    /**
     * initialize SL with test data
     */
    private void GetDefaultData() {
        // #Parameters

        SL.Add("DP.StartlistCount = 8");
        SL.Add("DP.ITCount = 0");
        SL.Add("DP.RaceCount = 2");

        // #Event Properties

        // Regatta Properties
        SL.Add("EP.Name = Regatta X");
        SL.Add("EP.Dates = Event Dates");
        SL.Add("EP.HostClub = Club Y");
        SL.Add("EP.PRO =");
        SL.Add("EP.JuryHead =");
        SL.Add("EP.ScoringSystem = Low Point System");
        SL.Add("EP.Throwouts = 0");
        SL.Add("EP.ThrowoutScheme = ByNumRaces");
        SL.Add("EP.DivisionName = *");
        SL.Add("EP.InputMode = Relaxed");
        SL.Add("EP.RaceLayout = Finish");
        SL.Add("EP.NameSchema = ");

        // Uniqua Ranglisten Properties
        SL.Add("EP.Uniqua.Enabled  = False");
        SL.Add("EP.Uniqua.Gesegelt = 2");
        SL.Add("EP.Uniqua.Gemeldet = 8");
        SL.Add("EP.Uniqua.Gezeitet = 8");

        // #Athletes

        // #Startlist

        SL.Add("FR.*.W1.STL.Pos1.SNR=1001");
        SL.Add("FR.*.W1.STL.Pos2.SNR=1002");
        SL.Add("FR.*.W1.STL.Pos3.SNR=1003");
        SL.Add("FR.*.W1.STL.Pos4.SNR=1004");
        SL.Add("FR.*.W1.STL.Pos5.SNR=1005");
        SL.Add("FR.*.W1.STL.Pos6.SNR=1006");
        SL.Add("FR.*.W1.STL.Pos7.SNR=1007");
        SL.Add("FR.*.W1.STL.Pos8.SNR=1008");

        // #W1

        SL.Add("FR.*.W1.Bib1.Rank=2");
        SL.Add("FR.*.W1.Bib2.Rank=7");
        SL.Add("FR.*.W1.Bib3.Rank=5");
        SL.Add("FR.*.W1.Bib4.Rank=1");
        SL.Add("FR.*.W1.Bib5.Rank=6");
        SL.Add("FR.*.W1.Bib6.Rank=8");
        SL.Add("FR.*.W1.Bib7.Rank=4");
        SL.Add("FR.*.W1.Bib8.Rank=3");

        // #W2

        SL.Add("FR.*.W2.Bib1.Rank=3");
        SL.Add("FR.*.W2.Bib2.Rank=4");
        SL.Add("FR.*.W2.Bib3.Rank=8");
        SL.Add("FR.*.W2.Bib4.Rank=7");
        SL.Add("FR.*.W2.Bib5.Rank=5");
        SL.Add("FR.*.W2.Bib6.Rank=6");
        SL.Add("FR.*.W2.Bib7.Rank=2");
        SL.Add("FR.*.W2.Bib8.Rank=1");

        SL.Add("EP.IM = Strict");
    }

}
