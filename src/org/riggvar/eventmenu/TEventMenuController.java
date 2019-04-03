package org.riggvar.eventmenu;

import java.text.MessageFormat;

import org.riggvar.bo.TMain;
import org.riggvar.eventmenu.TWorkspaceListBase.UrlScheme;

public class TEventMenuController {
    IEventMenuUI UI;
    TEventCombo EC = new TEventCombo();

    boolean skipDownload;
    boolean skipImport;

    String TestMemoText;
    String MemoText;

    private IEventMenuConnection FileCon;
    // private IEventMenuConnection HttpCon;
    private IEventMenuConnection AppCon;

    private String MoveToProcessingInstruction(String s) {
        String t = "<?";
        int i = s.indexOf(t);
        if (i > 0) {
            s = s.substring(i);
        }
        return s;
    }

    public void GetEventMenu(String url) {
        String data;
        IEventMenuConnection c = GetCon(url);
        try {
            c.setUrl(url);
            data = c.Get();
            data = MoveToProcessingInstruction(data);
            MemoText = data;
            if (c.hasError()) {
                TestMemoText = c.getError();
            }
        } catch (Exception ex) {
            MemoText = ex.getMessage();
            TestMemoText = "GetEventMenu: download has error";
            return;
        }

        EC = new TEventCombo();
        EC.Load(data);

        if (EC.LoadingCompleted) {
            UI.UpdateWorkspaceCombo();
            UI.InitEventButtons();
        } else {
            MemoText = "";
            TestMemoText = "GetEventMenu: transform failed";
        }
    }

    public void GetEventData(int i) {
        String url = EC.CurrentMenu().GetDataUrl(i);
        String en = EC.getCaption(i);
        String ed = "";

        TestMemoText = MessageFormat.format("B{0} ({1}): {2}", i, en, url);

        if (!skipDownload) {
            if (EC.isMock()) {
                MemoText = "download skipped (Error/Timeout before or url not selected)";
                return;
            }

            IEventMenuConnection c = GetCon(url);
            try {
                c.setUrl(url);
                ed = c.Get();
                MemoText = ed;
                if (c.hasError()) {
                    TestMemoText = c.getError();
                }
            } catch (Exception ex) {
                MemoText = ex.getMessage();
                TestMemoText = "GetEventData: error when downloading event data";
                return;
            }

            TEventData ED = new TEventData();
            ED.Load(ed);
            ed = ED.getText();
            if (ED.hasError()) {
                MemoText = ED.getError();
                TestMemoText = ED.getInfo();
            }

            if (!skipImport) {
                LoadEventData(ed);
                UI.UpdateEventName(en);
            }
        } else {
            MemoText = "GetEventData(), download skipped.";
        }
    }

    public String Download(String url) {
        if (url != null) {
            IEventMenuConnection c = GetCon(url);
            try {
                c.setUrl(url);
                String result = c.Get();
                TestMemoText = url;
                MemoText = result;
                if (c.hasError()) {
                    TestMemoText = c.getError();
                }
                return result;
            } catch (Exception ex) {
                MemoText = ex.getMessage();
                TestMemoText = "Download: has error";
            }
        }
        return "";
    }

    public void Transform(String data) {
        TEventData ED = new TEventData();
        ED.Transform(data);
        MemoText = ED.getText();
        TestMemoText = ED.getError();
    }

    public void LoadEventData(String EventData) {
        if (EventData != "")
            TMain.GuiManager.SwapEvent(EventData);
    }

    public void Upload(String url, String ed) {
        IEventMenuConnection c = GetCon(url);
        if (c != null) {
            c.setUrl(url);
            c.Post(ed);
        }
    }

    private IEventMenuConnection GetCon(String url) {
        switch (GetScheme(url)) {
        case Http:
            return getHttpCon();
        case File:
            return getFileCon();
        case App:
            return getAppCon();
        default:
            return FileCon;
        }
    }

    private UrlScheme GetScheme(String url) {
        if (IsHttpScheme(url))
            return UrlScheme.Http;
        if (IsAppScheme(url))
            return UrlScheme.App;
        return UrlScheme.File;
    }

    private boolean IsHttpScheme(String url) {
        return url.contains("http");
    }

    private boolean IsAppScheme(String url) {
        return url.contains("App://");
    }

    private IEventMenuConnection getHttpCon() {
//    	if (HttpCon == null)
//    		HttpCon = new TEventMenuConnection();
//    	return HttpCon;

        // always create a new one
        return new TEventMenuConnectionHttp();
    }

    private IEventMenuConnection getFileCon() {
        if (FileCon == null)
            FileCon = new TEventMenuConnectionFile();
        return FileCon;
    }

    private IEventMenuConnection getAppCon() {
        if (AppCon == null)
            AppCon = new TEventMenuConnection();
        return AppCon;
    }

}
