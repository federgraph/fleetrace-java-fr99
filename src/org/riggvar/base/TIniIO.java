package org.riggvar.base;

import java.io.File;
import java.util.Properties;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TIniIO {
    public static final String C_Options = "Options";
    public static final String C_Connections = "Connections";
    public static final String C_Switch = "Switch";
    public static final String C_Bridge = "Bridge";
    public static final String C_Output = "Output";
    public static final String C_Provider = "Provider";

    public TIniModifier Modifier;

    protected TIniFile ini = null;

    private TBaseIniImage ii;

    public TIniIO(TBaseIniImage iniImage) {
        ii = iniImage;
        Modifier = new TIniModifier();
    }

    public void LoadFromFile(String fn) {
        try {
            File fi = new File(fn);
            if (fi != null && fi.exists()) {
                FileInputStream f = new FileInputStream(fi);
                Properties p = new Properties();
                p.load(f);
                ini = new TIniFile(p);
                readConfig();
                f.close();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("cannot find " + fn);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void SaveToFile(String fn) {
        try {
            File fi = new File(fn);
            if (fi != null) {
                FileOutputStream f = new FileOutputStream(fi);
                Properties p = new Properties();
                ini = new TIniFile(p);
                writeConfig();
                p.store(f, "comment");
                f.flush();
                f.close();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("cannot find " + fn);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected void readConfig() {
        if (ini == null)
            return;

        ii.SaveStartupMode = ini.ReadBool(C_Options, "SaveStartupMode", ii.SaveStartupMode);
        ii.AutoLoadAdminConfig = ini.ReadBool(C_Options, "AutoLoadAdminConfig", ii.AutoLoadAdminConfig);

        ii.WantSockets = ini.ReadBool(C_Options, "WantSockets", ii.WantSockets);
        ii.SearchForUsablePorts = ini.ReadBool(C_Options, "SearchForUsablePorts", ii.SearchForUsablePorts);
        ii.IsMaster = ini.ReadBool(C_Options, "IsMaster", ii.IsMaster);

        ii.WantLocalAccess = ini.ReadBool(C_Options, "WantLocalAccess", ii.WantLocalAccess);
        ii.AutoSave = ini.ReadBool(C_Options, "AutoSave", ii.AutoSave);
        ii.NoAutoSave = ini.ReadBool(C_Options, "NoAutoSave", ii.NoAutoSave);

        ii.WantAdapter = ini.ReadBool(C_Connections, "WantAdapter", ii.WantAdapter);
        ii.Host = ini.ReadString(C_Connections, "Host", ii.Host);
        ii.PortIn = ini.ReadInteger(C_Connections, "PortIn", ii.PortIn);
        ii.PortOut = ini.ReadInteger(C_Connections, "PortOut", ii.PortOut);

        ii.CalcHost = ini.ReadString(C_Connections, "CalcHost", ii.CalcHost);
        ii.CalcPort = ini.ReadInteger(C_Connections, "CalcPort", ii.CalcPort);

        ii.FeedbackEnabled = ini.ReadBool(C_Connections, "FeedbackEnabled", ii.FeedbackEnabled);
        ii.FeedbackHost = ini.ReadString(C_Connections, "FeedbackHost", ii.FeedbackHost);
        ii.FeedbackPort = ini.ReadInteger(C_Connections, "FeedbackPort", ii.FeedbackPort);

        ii.HomeUrl = ini.ReadString(C_Connections, "HomeUrl", ii.HomeUrl);
        ii.WebApplicationUrl = ini.ReadString(C_Connections, "WebApplicationUrl", ii.WebApplicationUrl);
        ii.MaxConnections = ini.ReadInteger(C_Connections, "MaxConnections", ii.MaxConnections);
        ii.AppTitle = ini.ReadString(C_Connections, "AppTitle", ii.AppTitle);
        ii.JSDataDir = ini.ReadString(C_Connections, "JSDataDir", ii.JSDataDir);

        ii.SwitchHost = ini.ReadString(C_Switch, "SwitchHost", ii.SwitchHost);
        ii.SwitchPort = ini.ReadInteger(C_Switch, "SwitchPort", ii.SwitchPort);
        ii.SwitchPortHTTP = ini.ReadInteger(C_Switch, "SwitchPortHTTP", ii.SwitchPortHTTP);
        ii.RouterHost = ini.ReadString(C_Switch, "RouterHost", ii.RouterHost);
        ii.UseRouterHost = ini.ReadBool(C_Switch, "UseRouterHost", ii.UseRouterHost);
        ii.MaxConnections = ini.ReadInteger(C_Switch, "MaxConnections", ii.MaxConnections);

//        ii.BridgeProxyType = ii.BridgeProxyType
//             .Parse(ini.ReadInteger(C_Bridge, "BridgeProxyType", ii.BridgeProxyType.ToInt()));
        ii.BridgeHost = ini.ReadString(C_Bridge, "BridgeHost", ii.BridgeHost);
        ii.BridgePort = ini.ReadInteger(C_Bridge, "BridgePort", ii.BridgePort);
        ii.BridgePortHTTP = ini.ReadInteger(C_Bridge, "BridgePortHTTP", ii.BridgePortHTTP);
        ii.BridgeUrl = ini.ReadString(C_Bridge, "BridgeUrl", ii.BridgeUrl);
        ii.BridgeHomePage = ini.ReadString(C_Bridge, "BridgeHomePage", ii.BridgeHomePage);
        ii.TaktIn = ini.ReadInteger(C_Bridge, "TaktIn", ii.TaktIn);
        ii.TaktOut = ini.ReadInteger(C_Bridge, "TaktOut", ii.TaktOut);

        ii.OutputHost = ini.ReadString(C_Output, "OutputHost", ii.OutputHost);
        ii.OutputPort = ini.ReadInteger(C_Output, "OutputPort", ii.OutputPort);

        ii.DBInterface = ini.ReadString(C_Provider, "DataProvider", "TXT");
        ii.ScoringProvider = ini.ReadInteger(C_Provider, "ScoringProvider", ii.ScoringProvider);
        ii.BridgeProvider = ini.ReadInteger(C_Provider, "BridgeProvider", ii.BridgeProvider);
    }

    protected void writeConfig() {
        if (ini == null)
            return;

        TIniModifier m = Modifier;

        if (m.W_WantSockets)
            ini.WriteBool(C_Options, "WantSockets", ii.WantSockets);
        if (m.W_SearchForUsablePorts)
            ini.WriteBool(C_Options, "SearchForUsablePorts", ii.SearchForUsablePorts);
        if (m.W_IsMaster)
            ini.WriteBool(C_Options, "IsMaster", ii.IsMaster);

        if (m.W_WantLocalAccess)
            ini.WriteBool(C_Options, "WantLocalAccess", ii.WantLocalAccess);
        if (m.W_AutoSave)
            ini.WriteBool(C_Options, "AutoSave", ii.AutoSave);
        if (m.W_NoAutoSave)
            ini.WriteBool(C_Options, "NoAutoSave", ii.NoAutoSave);

        if (m.W_Host)
            ini.WriteString(C_Connections, "Host", ii.Host);
        if (m.W_PortIn)
            ini.WriteInteger(C_Connections, "PortIn", ii.PortIn);
        if (m.W_PortOut)
            ini.WriteInteger(C_Connections, "PortOut", ii.PortOut);
        if (m.W_CalcHost)
            ini.WriteString(C_Connections, "CalcHost", ii.CalcHost);
        if (m.W_CalcPort)
            ini.WriteInteger(C_Connections, "CalcPort", ii.CalcPort);

        if (m.W_FeedbackEnabled)
            ini.WriteBool(C_Connections, "FeedbackEnabled", ii.FeedbackEnabled);
        if (m.W_FeedbackHost)
            ini.WriteString(C_Connections, "FeedbackHost", ii.FeedbackHost);
        if (m.W_FeedbackPort)
            ini.WriteInteger(C_Connections, "FeedbackPort", ii.FeedbackPort);

        if (m.W_WebServerPort)
            ini.WriteInteger(C_Connections, "WebServerPort", ii.WebServerPort);
        if (m.W_HomeUrl)
            ini.WriteString(C_Connections, "HomeUrl", ii.HomeUrl);
        if (m.W_WebApplicationUrl)
            ini.WriteString(C_Connections, "WebApplicationUrl", ii.WebApplicationUrl);
        if (m.W_AppTitle)
            ini.WriteString(C_Connections, "AppTitle", ii.AppTitle);

        if (m.W_SwitchHost)
            ini.WriteString(C_Switch, "SwitchHost", ii.SwitchHost);
        if (m.W_SwitchPort)
            ini.WriteInteger(C_Switch, "SwitchPort", ii.SwitchPort);
        if (m.W_SwitchPortHTTP)
            ini.WriteInteger(C_Switch, "SwitchPortHTTP", ii.SwitchPortHTTP);
        if (m.W_RouterHost)
            ini.WriteString(C_Switch, "RouterHost", ii.RouterHost);
        if (m.W_UseRouterHost)
            ini.WriteBool(C_Switch, "UseRouterHost", ii.UseRouterHost);
        if (m.W_UseAddress)
            ini.WriteBool(C_Switch, "UseAddress", ii.UseAddress);
        if (m.W_MaxConnections)
            ini.WriteInteger(C_Switch, "MaxConnections", ii.MaxConnections);

//      if (m.W_BridgeProxyType)
//          ini.WriteInteger(C_Bridge, "BridgeProxyType", ii.BridgeProxyType.ToInt());
        if (m.W_BridgeHost)
            ini.WriteString(C_Bridge, "BridgeHost", ii.BridgeHost);
        if (m.W_BridgePort)
            ini.WriteInteger(C_Bridge, "BridgePort", ii.BridgePort);
        if (m.W_BridgePortHTTP)
            ini.WriteInteger(C_Bridge, "BridgePortHTTP", ii.BridgePortHTTP);
        if (m.W_BridgeUrl)
            ini.WriteString(C_Bridge, "BridgeUrl", ii.BridgeUrl);
        if (m.W_BridgeHomePage)
            ini.WriteString(C_Bridge, "BridgeHomePage", ii.BridgeHomePage);
        if (m.W_TaktIn)
            ini.WriteInteger(C_Bridge, "TaktIn", ii.TaktIn);
        if (m.W_TaktOut)
            ini.WriteInteger(C_Bridge, "TaktOut", ii.TaktOut);

        if (m.W_OutputHost)
            ini.WriteString(C_Output, "OutputHost", ii.OutputHost);
        if (m.W_OutputPort)
            ini.WriteInteger(C_Output, "OutputPort", ii.OutputPort);

        if (m.W_DataProvider)
            ini.WriteString(C_Provider, "DataProvider", ii.DBInterface);
        if (m.W_ScoringProvider)
            ini.WriteInteger(C_Provider, "ScoringProvider", ii.ScoringProvider);
        if (m.W_BridgeProvider)
            ini.WriteInteger(C_Provider, "BridgeProvider", ii.BridgeProvider);

    }

}
