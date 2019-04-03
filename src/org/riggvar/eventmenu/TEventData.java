package org.riggvar.eventmenu;

import org.riggvar.base.TStringList;
import org.riggvar.base.Utils;

public class TEventData implements IEventData {
    TStringList SL = new TStringList();

    boolean hasError;

    boolean hasBOM;
    boolean hasHash;
    boolean hasPI;
    boolean isXml;
    boolean isHtml;

    String DataText;
    String InfoText;
    String MemoText;

    private String RemovePreamble(String s) {
        String t;
        int i;

        hasBOM = false;
        if (s.length() > 1 && (byte) s.charAt(0) == -1) {
            s = s.substring(1);
            hasBOM = true;
        }

        hasHash = false;
        t = "#";
        i = s.indexOf(t);
        if (i > 0 && i < 5) {
            s = s.substring(i);
            hasHash = true;
        }

        return s;
    }

    private String MoveToHtml(String s) {
        String t = "<html";
        int i = s.indexOf(t);
        if (i > 0) {
            s = s.substring(i);
        }
        return s;
    }

    private String MoveToProcessingInstruction(String s) {
        hasPI = false;
        String t = "<?";
        int i = s.indexOf(t);
        if (i > 0) {
            s = s.substring(i);
            hasPI = true;
        }
        return s;
    }

    private boolean IsHtml(String t) {
        String s;
        int p;
        boolean IsFragment;
        boolean result;
        IsFragment = false;
        result = false;
        SL.setText(t);
        int c = Math.min(SL.getCount(), 10); // max 10 lines
        for (int i = 0; i < c; i++) {
            s = SL.getString(i);
            p = s.indexOf("<html");
            if (p >= 0) {
                result = true;
                break;
            }
            p = s.indexOf("<thead");
            if (p >= 0) {
                result = true;
                IsFragment = true;
                break;
            }
            p = s.indexOf("<h2>");
            if (p >= 0) {
                result = true;
                IsFragment = true;
                break;
            }
        }
        if (IsFragment) {
            if (SL.getCount() > 0) {
                s = SL.getString(0);
                s = MoveToFirstAngleBracket(s);
                SL.setString(0, s);
            }
            SL.Insert(0, "<html><body>");
            SL.Add("'</body></html>");
        }
        return result;
    }

    String MoveToFirstAngleBracket(String s) {
        String t = "<";
        int i = s.indexOf(t);
        if (i >= 0) {
            s = s.substring(i);
        }
        return s;

    }

    private boolean IsXmlData(String s) {
        isXml = true;

        if (s.startsWith("<?")) {
            isXml = true;
            return true;
        }

        if (s.startsWith("<"))
            return true;

        if (s.contains("<?"))
            return true;

        isXml = false;
        return false;
    }

    @Override
    public void Load(String ed) {
        Reset();
        String s;
        if (IsHtml(ed)) {
            s = MoveToHtml(SL.getText());
            TEventDataHtml ED = new TEventDataHtml();
            try {
                DataText = ED.TransformEventData(s);
            } catch (Exception ex) {
                hasError = true;
                MemoText = ex.getMessage();
                InfoText = "GetEventData: error when transforming event data xml";
            }
        } else if (IsXmlData(ed)) {
            s = RemovePreamble(ed);
            s = MoveToProcessingInstruction(s);
            TEventDataXml ED = new TEventDataXml();
            try {
                DataText = ED.TransformEventData(s);
            } catch (Exception ex) {
                hasError = true;
                MemoText = ex.getMessage();
                InfoText = "GetEventData: error when transforming event data xml";
            }
        } else {
            s = RemovePreamble(ed);
            DataText = Utils.SwapLineFeed(s);
        }

    }

    @Override
    public String getText() {
        return DataText;
    }

    @Override
    public String getError() {
        return MemoText;
    }

    @Override
    public String getInfo() {
        return InfoText;
    }

    @Override
    public boolean hasError() {
        return false;
    }

    @Override
    public boolean isOK() {
        return !hasError();
    }

    private void Reset() {
        DataText = "";
        MemoText = "";
        InfoText = "";
        hasError = false;
    }

    public void Transform(String data) {
        Reset();
        String s;
        if (IsHtml(data)) {
            TEventDataHtml ED = new TEventDataHtml();
            try {
                s = MoveToHtml(SL.getText());
                DataText = ED.TransformEventData(s);
                MemoText = DataText;
                InfoText = "Conversion: ok";
            } catch (Exception ex) {
                hasError = true;
                MemoText = ex.getMessage();
                InfoText = "Conversion: has error";
            }
        } else if (IsXmlData(data)) {
            TEventDataXml ED = new TEventDataXml();
            try {
                s = MoveToProcessingInstruction(data);
                DataText = ED.TransformEventData(s);
                MemoText = DataText;
                InfoText = "Transform: ok";
            } catch (Exception ex) {
                hasError = true;
                MemoText = ex.getMessage();
                InfoText = "Transform: has error";
            }
        } else {
            InfoText = "Transform: skipped, data must be xml";
        }
    }

}
