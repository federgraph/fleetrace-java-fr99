package org.riggvar.base;

public class TUndoManager {
    private TBaseBO BaseBO;

    private int FIndex;

    private TStringList BaseList;
    private TStringList LogList;
    private TStringList CurrentList;
    private TStringList UndoList;
    private TStringList RedoList;

    public TStringList ProxyXML; // buffer xml for debugging purpose only

    public TUndoManager(TBaseBO abo) {
        BaseBO = abo;

        ProxyXML = new TStringList();
        BaseList = new TStringList();
        LogList = new TStringList();
        CurrentList = new TStringList();
        UndoList = new TStringList();
        RedoList = new TStringList();
    }

    public void Clear() {
        UndoList.Clear();
        RedoList.Clear();
        LogList.Clear();
        CurrentList.Clear();
        // BaseList.Clear(); ###
        FIndex = 0;
    }

    public void AddMsg(String UndoMsg, String RedoMsg) {
        Trim();
        UndoList.Add(UndoMsg);
        RedoList.Add(RedoMsg);
        LogList.Add(RedoMsg);
        FIndex = UndoList.getCount();
        BaseBO.getWatches().setUndo(UndoMsg);
        BaseBO.getWatches().setRedo(RedoMsg);

        // Avoid endless loop: do not broadcast messages from Switch.
        if (!MsgContext.SwitchLocked)
            BaseBO.OutputServer.InjectMsg(LookupKatID.FR, TMsgSource.UndoRedo, RedoMsg);
    }

    public int LogCount() {
        return LogList.getCount();
    }

    public int UndoCount() {
        return UndoList.getCount();
    }

    public int RedoCount() {
        return RedoList.getCount();
    }

    public String Redo() {
        if (FIndex >= 0 && FIndex < RedoList.getCount()) {
            String result = RedoList.getString(FIndex);
            FIndex = FIndex + 1;
            LogList.Add(result);
            return result;
        }
        return "";
    }

    public String Undo() {
        if (FIndex > 0 && FIndex <= UndoList.getCount()) {
            FIndex = FIndex - 1;
            String result = UndoList.getString(FIndex);
            LogList.Add(result);
            return result;
        }
        return "";
    }

    public void UpdateBase(String s) {
        BaseList.setText(s);
    }

    public void UpdateCurrent(String s) {
        CurrentList.setText(s);
    }

    public String GetBase() {
        return BaseList.getText();
    }

    public String GetLog() {
        String result = LogList.getText();
        if (result.isEmpty())
            return "Log list is empty.";
        else
            return result;
    }

    public String GetRedo() {
        String result = SubList(RedoList, FIndex, RedoList.getCount() - 1);
        if (result.isEmpty())
            return "Redo sublist is empty.";
        else
            return result;
    }

    public String GetUndo() {
        String result = SubList(UndoList, 0, FIndex - 1);
        if (result.isEmpty())
            return "Undo sublist is empty.";
        else
            return result;
    }

    public String SubList(TStrings SL, int Index1, int Index2) {
        if (Index1 >= 0 && Index2 < SL.getCount()) {
            TStringList TL = new TStringList();
            for (int i = Index1; i <= Index2; i++) {
                TL.Add(SL.getString(i));
            }
            return TL.getText();
        }
        return "";
    }

    public String GetUndoRedo() {
        String result = "";
        if (UndoList.getCount() > 0) {
            TStringList TL = new TStringList();
            for (int i = 0; i < UndoList.getCount(); i++) {
                TL.Add(UndoList.getString(i) + " | " + RedoList.getString(i));
            }
            result = TL.getText();
        }
        if (result.isEmpty())
            return "UndoRedo combined list is empty.";
        else
            return result;
    }

    private void Trim() {
        for (int i = UndoList.getCount() - 1; i >= FIndex; i--) {
            UndoList.Delete(i);
            RedoList.Delete(i);
        }

    }

}
