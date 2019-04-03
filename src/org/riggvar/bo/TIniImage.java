package org.riggvar.bo;

import org.riggvar.base.*;
import org.riggvar.inspector.*;

public class TIniImage extends TBaseIniImage implements IInspectable {

    public TIniImage() {
        super();
        DefaultEventType = LookupKatID.FR;
        EventType = DefaultEventType;
        FeedbackEnabled = true;
    }

    @Override
    protected void EnableEntries() {
        TIniModifier m = iniIO.Modifier;

        // Options
        m.S_Options = true;
        m.W_SaveStartupMode = true;
        m.W_AutoLoadAdminConfig = true;
        m.W_AutoSave = true;
        m.W_NoAutoSave = true;
        m.W_CopyRankEnabled = true;
        m.W_LogProxyXML = true;
        m.W_WantSockets = true;
        m.W_IsMaster = true;

        // Connections
        m.S_Connections = true;
        m.W_PortIn = true;
        m.W_PortOut = true;
        m.W_CalcHost = true;
        m.W_CalcPort = true;
        m.W_WebApplicationUrl = true;
        m.W_HomeUrl = true;

        // Provider
        m.S_Provider = true;
        m.W_DataProvider = true;
        m.W_ScoringProvider = true;
        m.W_BridgeProvider = true;

        // Switch
        m.S_Switch = true;
        m.W_SwitchHost = true;
        m.W_SwitchPort = true;
        m.W_SwitchPortHTTP = true;
        m.W_RouterHost = true;
        m.W_UseRouterHost = true;

        // Bridge
        m.S_Bridge = true;
        m.W_BridgeProxyType = true;
        m.W_BridgeHost = true;
        m.W_BridgePort = true;
        m.W_BridgePortHTTP = true;
        m.W_BridgeUrl = true;
        m.W_BridgeHomePage = true;
        m.W_TaktIn = true;
        m.W_TaktOut = true;

        // Output
        m.S_Output = true;
        m.W_OutputHost = true;
        m.W_OutputPort = true;

        // Info Sections
        m.S_DataProviderHelp = true;
        m.S_ScoringProviderHelp = true;
        m.S_BridgeProviderHelp = true;
        m.S_BridgeProxyHelp = true;
    }

    public void inspectorOnLoad(Object sender) {
        TNameValueRowCollection cl;
        TNameValueRowCollectionItem cr;
        TIniModifier m = iniIO.Modifier;

        if (!(sender instanceof TNameValueRowCollection))
            return;

        cl = (TNameValueRowCollection) sender;

        if (m.W_AutoSave) {
            cr = cl.AddRow();
            cr.Category = TIniIO.C_Options;
            cr.FieldName = "AutoSave";
            cr.FieldType = NameValueFieldType.FTBoolean;
            cr.FieldValue = Utils.BoolStr(AutoSave);
            cr.Caption = "AutoSave";
            cr.Description = "always save, dominant over NoAutoSave";
        }

        if (m.W_NoAutoSave) {
            cr = cl.AddRow();
            cr.Category = TIniIO.C_Options;
            cr.FieldName = "NoAutoSave";
            cr.FieldValue = Utils.BoolStr(NoAutoSave);
            cr.FieldType = NameValueFieldType.FTBoolean;
            cr.Caption = "NoAutoSave";
            cr.Description = "do not ask if AutoSave=false";
        }

        if (m.W_CopyRankEnabled) {
            cr = cl.AddRow();
            cr.Category = TIniIO.C_Options;
            cr.FieldName = "CopyRankEnabled";
            cr.FieldValue = Utils.BoolStr(CopyRankEnabled);
            cr.FieldType = NameValueFieldType.FTBoolean;
            cr.Caption = "CopyRankEnabled";
            cr.Description = "allow transfer of FinishPos from race to event";
        }

        if (m.W_WantSockets) {
            cr = cl.AddRow();
            cr.Category = TIniIO.C_Options;
            cr.FieldName = "WantSockets";
            cr.FieldValue = Utils.BoolStr(WantSockets);
            cr.FieldType = NameValueFieldType.FTBoolean;
            cr.Caption = "WantSockets";
            cr.Description = "yes triggers firewall popup at startup";
        }

        if (m.W_LogProxyXML) {
            cr = cl.AddRow();
            cr.Category = TIniIO.C_Options;
            cr.FieldName = "LogProxyXML";
            cr.FieldValue = Utils.BoolStr(LogProxyXML);
            cr.FieldType = NameValueFieldType.FTBoolean;
            cr.Caption = "LogProxyXML";
            cr.Description = "save proxy xml to disk for debugging";
        }

        if (m.W_IsMaster) {
            cr = cl.AddRow();
            cr.Category = TIniIO.C_Options;
            cr.FieldName = "IsMaster";
            cr.FieldValue = Utils.BoolStr(IsMaster);
            cr.FieldType = NameValueFieldType.FTBoolean;
            cr.Caption = "IsMaster";
            cr.Description = "master can upload backup";
        }
    }

    public void inspectorOnSave(Object sender) {
        TNameValueRowCollection cl;
        TNameValueRowCollectionItem cr;

        if (!(sender instanceof TNameValueRowCollection))
            return;

        cl = (TNameValueRowCollection) sender;

        for (int i = 0; i < cl.size(); i++) {
            cr = cl.getItem(i);
            if (cr.FieldName.equals("AutoSave"))
                AutoSave = Utils.IsTrue(cr.FieldValue);
            else if (cr.FieldName.equals("NoAutoSave"))
                NoAutoSave = Utils.IsTrue(cr.FieldValue);
            else if (cr.FieldName.equals("CopyRankEnabled"))
                CopyRankEnabled = Utils.IsTrue(cr.FieldValue);
            else if (cr.FieldName.equals("WantSockets"))
                WantSockets = Utils.IsTrue(cr.FieldValue);
            else if (cr.FieldName.equals("LogProxyXML"))
                LogProxyXML = Utils.IsTrue(cr.FieldValue);
            else if (cr.FieldName.equals("IsMaster"))
                IsMaster = Utils.IsTrue(cr.FieldValue);
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        String crlf = "\r\n";

        sb.append("[Options]");
        sb.append(crlf);

        sb.append("SaveStartupMode=");
        sb.append(SaveStartupMode);
        sb.append(crlf);

        sb.append("AutoLoadAdminConfig=");
        sb.append(AutoLoadAdminConfig);
        sb.append(crlf);

        sb.append("WantSockets=");
        sb.append(WantSockets);
        sb.append(crlf);

        sb.append("SearchForUsablePorts=");
        sb.append(SearchForUsablePorts);
        sb.append(crlf);

        sb.append("IsMaster=");
        sb.append(IsMaster);
        sb.append(crlf);

        sb.append("AutoSave=");
        sb.append(AutoSave);
        sb.append(crlf);

        sb.append("NoAutoSave=");
        sb.append(NoAutoSave);
        sb.append(crlf);

        sb.append("CopyRankEnabled=");
        sb.append(CopyRankEnabled);
        sb.append(crlf);

        sb.append("LogProxyXML=");
        sb.append(LogProxyXML);
        sb.append(crlf);

        sb.append(crlf);
        sb.append("[Connections]");
        sb.append(crlf);

        sb.append("WantAdapter=");
        sb.append(WantAdapter);
        sb.append(crlf);

        sb.append("AutoConnect=");
        sb.append(AutoConnect);
        sb.append(crlf);

        sb.append("Host=");
        sb.append(Host);
        sb.append(crlf);

        sb.append("PortIn=");
        sb.append(PortIn);
        sb.append(crlf);

        sb.append("PortOut=");
        sb.append(PortOut);
        sb.append(crlf);

        sb.append("CalcHost=");
        sb.append(CalcHost);
        sb.append(crlf);

        sb.append("CalcPort=");
        sb.append(CalcPort);
        sb.append(crlf);

        sb.append("FeedbackEnabled=");
        sb.append(FeedbackEnabled);
        sb.append(crlf);

        sb.append("FeedbackHost=");
        sb.append(FeedbackHost);
        sb.append(crlf);

        sb.append("FeedbackPort=");
        sb.append(FeedbackPort);
        sb.append(crlf);

        sb.append("WebServerPort=");
        sb.append(WebServerPort);
        sb.append(crlf);

        sb.append("WebApplicationUrl=");
        sb.append(WebApplicationUrl);
        sb.append(crlf);

        sb.append("HomeUrl=");
        sb.append(HomeUrl);
        sb.append(crlf);

        sb.append("BrowserHomePage=");
        sb.append(BrowserHomePage);
        sb.append(crlf);

        sb.append("AppTitle=");
        sb.append(AppTitle);
        sb.append(crlf);

        sb.append("JSDataDir=");
        sb.append(JSDataDir);
        sb.append(crlf);

        sb.append(crlf);
        sb.append("[Switch]");
        sb.append(crlf);

        sb.append("SwitchHost=");
        sb.append(SwitchHost);
        sb.append(crlf);

        sb.append("SwitchPort=");
        sb.append(SwitchPort);
        sb.append(crlf);

        sb.append("SwitchPortHTTP=");
        sb.append(SwitchPortHTTP);
        sb.append(crlf);

        sb.append("RouterHost=");
        sb.append(RouterHost);
        sb.append(crlf);

        sb.append("UseRouterHost=");
        sb.append(UseRouterHost);
        sb.append(crlf);

        sb.append("UseAddress=");
        sb.append(UseAddress);
        sb.append(crlf);

        sb.append("MaxConnections=");
        sb.append(MaxConnections);
        sb.append(crlf);

//        sb.append(crlf);
//        sb.append("[Bridge]");
//        sb.append(crlf);

//        sb.append("BridgeProxyType=");
//        sb.append(BridgeProxyType);
//        sb.append(crlf);

        sb.append("BridgeHost=");
        sb.append(BridgeHost);
        sb.append(crlf);

        sb.append("BridgePort=");
        sb.append(BridgePort);
        sb.append(crlf);

        sb.append("BridgePortHTTP=");
        sb.append(BridgePortHTTP);
        sb.append(crlf);

        sb.append("BridgeUrl=");
        sb.append(BridgeUrl);
        sb.append(crlf);

        sb.append("BridgeHomePage=");
        sb.append(BridgeHomePage);
        sb.append(crlf);

        sb.append("TaktIn=");
        sb.append(TaktIn);
        sb.append(crlf);

        sb.append("TaktOut=");
        sb.append(TaktOut);
        sb.append(crlf);

        sb.append(crlf);
        sb.append("[Output]");
        sb.append(crlf);

        sb.append("OutputHost=");
        sb.append(OutputHost);
        sb.append(crlf);

        sb.append("OutputPort=");
        sb.append(OutputPort);
        sb.append(crlf);

        sb.append(crlf);
        sb.append("[Provider]");
        sb.append(crlf);

        sb.append("DataProvider=");
        sb.append(DBInterface);
        sb.append(crlf);

        sb.append("ScoringProvider=");
        sb.append(ScoringProvider);
        sb.append(crlf);

        sb.append("BridgeProvider=");
        sb.append(BridgeProvider);
        sb.append(crlf);

        sb.append(crlf);

        return sb.toString();
    }

}
