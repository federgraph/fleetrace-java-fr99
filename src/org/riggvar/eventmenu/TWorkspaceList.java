package org.riggvar.eventmenu;

import java.io.File;

import org.riggvar.base.TStringList;
import org.riggvar.base.TStrings;

public class TWorkspaceList extends TWorkspaceListBase {
    private static String StrWorkspaceList = "fr-workspace-urls.txt";

    private boolean FStop;
    private String FK;
    private TWorkspaceUrl FV;
    public String FError;
    // public int FCounter;
    private TStringList NL;

    public boolean WantNames;

    public TWorkspaceList() {
        super();
        // FCounter++;
        NL = new TStringList();
        FV = new TWorkspaceUrl();
        WantNames = true;
    }

//    private void InitLocal()
//    {
//        try
//        {
//            String s;
//            String fn = GetWorkspaceListFileName();
//            if (fn != "")
//            {
//                TStringList WL = new TStringList();
//                WL.LoadFromFile(fn);
//                for (int i = 0; i < WL.getCount(); i++)
//                {
//                    s = WL.getString(i).trim();
//                    if (s.equals("stop"))
//                    {
//                        FStop = true;
//                        break;
//                    }
//
//
//                    if (s.equals("") || s.startsWith("//") || s.startsWith("#"))
//                        continue;
//
//                    ParseLine(s);
//
//                    switch (FV.GetScheme())
//                    {
//                        case Http:
//                            AddEntry();
//                            break;
//                        case File:
//                            if (File_Exists(FV.Value))
//                                AddEntry();
//                            break;
//                        case App:
//                            break;
//                    }
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            FError = ex.getMessage();
//        }
//    }

//    protected String GetHomeDir()
//    {
//        String StrDocuments = "Documents";
//        String d;
//        d = System.getProperty("user.home");
//        File fi = new File(d);
//        if (File.separator.equals("\\"))
//            d = fi.getPath() + File.separator + StrDocuments + File.separator;
//        else
//            d = fi.getPath() + File.separator;
//        return d;
//    }

    protected String GetAppDataDir() {
        String d;
        File fi;
        if (File.separator.equals("\\")) {
            d = System.getenv("LOCALAPPDATA");
            fi = new File(d);
            d = fi.getPath() + File.separator + "RiggVar\\FR" + File.separator;
        } else {
            d = System.getProperty("user.home");
            fi = new File(d);
            d = fi.getPath() + File.separator;
        }
        return d;
    }

    private boolean File_Exists(String fn) {
        return new File(fn).exists();
    }

    private String GetWorkspaceListFileName() {
        String dn;
        String fn;
        dn = GetAppDataDir();
        if (!dn.equals("")) {
            fn = dn + StrWorkspaceList;
            if (File_Exists(fn))
                return fn;
        }
        return "";
    }

    boolean isEmpty() {
        return VL.getCount() == 0;
    }

    public boolean wantDefault() {
        return !FStop;
    }

    void Reset() {
        FStop = false;
        VL.Clear();
        NL.Clear();
    }

    private void AddEntry() {
        NL.Add(FK);
        VL.Add(FV.Value);
    }

    private void ParseLine(String s) {
        String[] sa = s.split("=");
        if (sa.length == 2) {
            FK = sa[0].trim();
            FV.Value = sa[1].trim();
        } else {
            FK = "";
            FV.Value = s.trim();
        }
    }

    void ProcessTL() {
        String s;
        for (int i = 0; i < TL.getCount(); i++) {
            s = TL.getString(i).trim();
            if (s.equals("stop")) {
                FStop = true;
                break;
            }

            if (s.isEmpty() || s.startsWith("//") || s.startsWith("#"))
                continue;

            ParseLine(s);

            switch (FV.GetScheme()) {
            case Http:
                AddEntry();
                break;
            case File:
                if (File_Exists(FV.Value))
                    AddEntry();
                break;
            case App:
                break;
            }
        }
    }

    @Override
    public void Init(boolean FromMemo) {
        Reset();
        if (FromMemo) {
            // assume TL was loaded externally
            ProcessTL();
        } else {
            Load();
            ProcessTL();
            if (isEmpty() || wantDefault()) {
                LoadDefault();
                ProcessTL();
            }
        }
    }

    @Override
    public String GetName(int i) {
        if (i >= 0 && i < NL.getCount())
            return NL.getString(i);
        return "";
    }

    @Override
    public String GetUrl(int i) {
        if (i >= 0 && i < VL.getCount())
            return VL.getString(i);
        return "";
    }

    @Override
    public String GetText() {
        String n, v, s;
        TL.Clear();
        for (int i = 0; i < NL.getCount(); i++) {
            n = NL.getString(i);
            v = VL.getString(i);
            if (!n.isEmpty())
                s = n + '=' + v;
            else
                s = v;
            TL.Add(s);
        }
        return TL.getText();
    }

    @Override
    public void SetText(String value) {
        TL.setText(value);
    }

    @Override
    public void Load() {
        try {
            String fn = GetWorkspaceListFileName();
            if (!fn.isEmpty() && File_Exists(fn))
                TL.LoadFromFile(fn);
            else
                TL.Clear();
        } catch (Exception ex) {
            FError = ex.getMessage();
            TL.Clear();
        }
    }

    @Override
    public void Save() {
        String fn = GetWorkspaceListFileName();
        String s;
        for (int i = TL.getCount() - 1; i > 0; i--) {
            s = TL.getString(i);
            if (s.startsWith("Example: "))
                TL.Delete(i);
            if (s.contains("Local Workspace"))
                TL.Delete(i);
        }
        TL.SaveToFile(fn);
    }

    @Override
    public boolean IsWritable(int i) {
        return GetName(i).startsWith("*");
    }

    @Override
    public void FillCombo(TStrings Combo) {
        Combo.Clear();
        String s;
        for (int i = 0; i < NL.getCount(); i++) {
            if (WantNames && NL.getString(i) != "")
                s = NL.getString(i);
            else
                s = VL.getString(i);
            Combo.Add(s);
        }
    }

}
