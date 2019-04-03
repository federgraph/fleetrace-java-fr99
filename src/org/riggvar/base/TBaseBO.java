package org.riggvar.base;

import org.riggvar.bo.TMain;
import org.riggvar.output.TOutput;

public abstract class TBaseBO {
    // atoms
    public String cTokenFleetRace = "FR";
    public String cTokenDivision = "Division";

    // compositions
    public String cTokenModul = "FR.";
    public String cTokenSport = "FR.Division.";
    public String cTokenCC = "FR.Division.CC.";
    public String cTokenRequest = "FR.Division.Request.";
    public String cTokenAnonymousRequest = "FR.*.Request.";
    public String cTokenAnonymousOutput = "FR.*.Output.";
    public String cTokenOutput = "FR.Division.Output.";
    public String cTokenOutputXML = "FR.Division.Output.XML.";
    public String cTokenOutputCSV = "FR.Division.Output.CSV.";
    public String cTokenOutputHTM = "FR.Division.Output.HTM.";

    // special
    public String cTokenAthlete = "SNR";
    public String cTokenRace = "W";
    public String cTokenOption = "Graph";

    public void SetDivisionName(String Value) {
        cTokenDivision = Value;
        cTokenModul = cTokenFleetRace + ".";
        cTokenSport = cTokenModul + cTokenDivision + ".";
        cTokenCC = cTokenSport + "CC.";
        cTokenRequest = cTokenSport + "Request.";
        cTokenAnonymousRequest = cTokenModul + "*.Request.";
        cTokenOutput = cTokenSport + "Output.";
        cTokenOutputXML = cTokenOutput + "XML.";
        cTokenOutputCSV = cTokenOutput + "CSV.";
        cTokenOutputHTM = cTokenOutput + "HTM.";
    }

    public TAdapterParams AdapterParams;
    public TInputNCP InputServer;
    public TOutputNCP OutputServer;
    public TOutput Output;
    public int CounterMsgHandled;

    public TBaseBO(TAdapterParams aParams) {
        this.AdapterParams = aParams;
    }

    public abstract TAdapterWatches getWatches();

    public void OnIdle() {
        // virtual;
    }

    public boolean Calc() {
        return false;
    }

    protected IBOConnector getConnector() {
        return TMain.BOConnector;
    }

    public void Connect() {
        getConnector().ConnectBO();
    }

    public void Disconnect() {
        getConnector().DisconnectBO();
    }

    public boolean getConnected() {
        IBOConnector c = getConnector();
        return (c.getConnected() && (c.getConnectedBO()));
    }

    public void setConnected(boolean value) {
        if (!value) {
            Disconnect();
        } else if (value && (!getConnected())) {
            Connect();
        }
    }

    public TBaseBO FindDestinationBO(String Prot) {
        // virtual
        return this;
    }

    public void BackupToSL(TStrings SL) {
        // virtual;
    }

    public String Save() {
        // virtual
        return "";
    }

    public void LoadNew(String Data) {
        // virtual
    }

    public void Clear() {
        // virtual
    }

    public TBaseMsg NewMsg() {
        return new TBaseMsg();
    }

}
