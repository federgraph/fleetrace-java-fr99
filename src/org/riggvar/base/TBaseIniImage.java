package org.riggvar.base;

//import org.riggvar.bridge.TBridgeProxyType;

public class TBaseIniImage {
    public static final String Scoring_Simple = "Simple";
    public static final String Scoring_Inline = "Inline";
    public static final String Scoring_Proxy = "Proxy";

    public TIniIO iniIO;

    public String DefaultEventName = "Test";
    public static int DefaultEventType = 0; // see constructor of TIniImage
    public int EventType = 0; // initialize later with DefaultEventType

    // --not persisted
    public boolean UseIni;
    public boolean IsApplet = false;

    // --expanded version of HomeUrl: http://WebHost: + WebHomePort + WebHomePath
    public String WebHost = "localhost";
    public int WebHomePort = 80;
    public String WebHomePath = "/";

    // --Options
    public boolean SaveStartupMode;
    public boolean AutoLoadAdminConfig;

    public boolean WantSockets;
    public boolean SearchForUsablePorts;
    public boolean IsMaster = true;

    public boolean WantLocalAccess = false;
    public boolean AutoSave;
    public boolean NoAutoSave;
    public boolean CopyRankEnabled;
    public boolean LogProxyXML;
    public boolean AutoPlugin;
    public boolean AutoUpload;

    // --Connections
    public boolean WantAdapter = false;
    public boolean AutoConnect = false;
    public String Host;
    public int PortIn = 3027;
    public int PortOut = 3028;

    public String CalcHost = "";
    public int CalcPort = 3037;

    public boolean FeedbackEnabled = false;
    public String FeedbackHost = "";
    public int FeedbackPort = 0;

    public int WebServerPort = 8099;
    public String HomeUrl = "";
    public String WebApplicationUrl = "http://webhoster.net/cgi-bin/FR86/";
    public String BrowserHomePage = "http://gsup3";
    public String AppTitle = "";
    public String JSDataDir = "unspecified";

    // --Switch
    public String SwitchHost = "switchhoster.com";
    public int SwitchPort = 4029;
    public int SwitchPortHTTP = 8085;
    public String RouterHost = "routerhost.dynalias.net";
    public boolean UseRouterHost = true;
    public boolean UseAddress = true;
    public int MaxConnections = 32;

    // --Bridge
//    public TBridgeProxyType BridgeProxyType = TBridgeProxyType.Client;
    public String BridgeHost = "gsup3";
    public int BridgePort = 4030;
    public int BridgePortHTTP = 8087;
    public String BridgeUrl = "http://gsup3:8087";
    public String BridgeHomePage = "http://gsup3/FR88";
    public int TaktIn = 0;
    public int TaktOut = 0;

    // --Output
    public String OutputHost = "gsup3";
    public int OutputPort = 3428;

    // --Provider
    public String DBInterface = "TXT";
    public int ScoringProvider = 2;
    public int BridgeProvider = 2;

    public TBaseIniImage() {
        iniIO = new TIniIO(this);
        EnableEntries();
    }

    public String getScoringModule() {
        switch (ScoringProvider) {
        case 1:
            return Scoring_Simple;
        case 2:
            return Scoring_Inline;
        case 3:
            return Scoring_Proxy;
        }
        return Scoring_Simple;
    }

    public void setScoringModule(String value) {
        if (value.equals(Scoring_Simple))
            ScoringProvider = 1;
        else if (value.equals(Scoring_Inline))
            ScoringProvider = 2;
        else if (value.equals(Scoring_Proxy))
            ScoringProvider = 3;
    }

    public void setWebApplicationUrl(String value) {
        WebApplicationUrl = value;
    }

    public void setWantAdapter(boolean value) {
        WantAdapter = value;
    }

    public void setSelectedDB(String value) {
        DBInterface = value;
    }

    public void setEventType(int value) {
        EventType = value;
    }

    public void setPortIn(int value) {
        PortIn = value;
    }

    public void setPortOut(int value) {
        PortOut = value;
    }

    public void setWantLocalAccess(boolean value) {
        WantLocalAccess = value;
    }

    public void LoadFromFile(String fn) {
        iniIO.LoadFromFile(fn);
    }

    public void SaveToFile(String fn) {
        iniIO.SaveToFile(fn);
    }

    protected void EnableEntries() {
    }

}
