package org.riggvar.conn;

import java.util.*;
import java.net.*;
import org.riggvar.base.*;

public class TRestConnection {
    protected int eventType = 400;
    protected String fileExtension = ".aspx";

    protected static final String ApplicationPath = "/FR88/";
    protected static final String HostName = "localhost";
    protected static final String controllerUrl = "http://localhost/FR88/";

    URL url = null;

    TStringList SL = new TStringList();
    Hashtable<String, String> HT = new Hashtable<String, String>();

    TRestClient httpBridgeClient;

    public TRestConnection(int paramEventType) {
        eventType = paramEventType;
        httpBridgeClient = new TRestClient();
        setServerUrl(controllerUrl);
    }

    public TStringList getSL() {
        return SL;
    }

    public Hashtable<String, String> getHT() {
        return HT;
    }

    public void setDebug(boolean value) {
        httpBridgeClient.debug = value;
    }

    public String getServerUrl() {
        if (url != null)
            return url.toString();
        else
            return controllerUrl;
    }

    public void setServerUrl(String Value) {
        try {
            if (!Value.endsWith("/"))
                url = new URL(Value + "/");
            else
                url = new URL(Value);

            httpBridgeClient.hostname = getHostName();
            httpBridgeClient.port = getPort();
        } catch (MalformedURLException ex) {
        }
    }

    protected String GetPageUrl(String pageName) {
        if (eventType == 400)
            return getServerUrl() + "FR/" + pageName + fileExtension;
        else if (eventType == 600)
            return getServerUrl() + "SKK/" + pageName + fileExtension;
        else
            return getServerUrl() + pageName + fileExtension;
    }

    protected String GetPagePath(String pageName) {
        if (eventType == 400)
            return getApplicationPath() + "FR/" + pageName + fileExtension;
        else if (eventType == 600)
            return getApplicationPath() + "SKK/" + pageName + fileExtension;
        else
            return getApplicationPath() + pageName + fileExtension;
    }

    protected String getHostName() {
        if (url != null) {
            return url.getHost();
        } else
            return HostName;
    }

    protected int getPort() {
        if (url != null && url.getPort() != -1) {
            return url.getPort();
        } else
            return 80;
    }

    protected String getApplicationPath() {
        if (url != null) {
            return url.getPath();
        } else
            return ApplicationPath;
    }

    public void setFileExtension(String value) {
        fileExtension = value;
    }

    public String Get(String pageName) {
        String s = GetPageUrl(pageName);
        return httpBridgeClient.Get(s);
    }

    public String Post(String pageName) {
        String s = GetPagePath(pageName);
        return httpBridgeClient.Post(s, SL);
    }

    public String PostMultiPart(String pageName) {
        String s = GetPagePath(pageName);
        return httpBridgeClient.PostMultiPart(s, HT);
    }

}
