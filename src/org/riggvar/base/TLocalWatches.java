package org.riggvar.base;

public class TLocalWatches extends TAdapterWatches {
    private IWatchGUI WatchGUI;
    private String FUndo;
    private String FRedo;

    public TLocalWatches() {
        FMsgOffset = 4;
        TGlobalWatches.Instance.Subscribe(this);
    }

    public void Dispose() {
        TGlobalWatches.Instance.UnSubscribe(this);
    }

    public void setWatchGUI(IWatchGUI value) {
        WatchGUI = value;
    }

    @Override
    public void Clear() {
        super.Clear();
        FUndo = "";
        FRedo = "";
    }

    public void Init() {
        if (WatchGUI != null) {
            WatchGUI.InitLabel(1, "Undo");
            WatchGUI.InitLabel(2, "Redo");
            WatchGUI.InitLabel(3, "AdapterMsgIn");
            WatchGUI.InitLabel(4, "AdapterMsgInCount");
            WatchGUI.InitLabel(5, "AdapterMsgOut");
            WatchGUI.InitLabel(6, "AdapterMsgOutCount");
            WatchGUI.InitLabel(7, "MsgIn");
            WatchGUI.InitLabel(8, "MsgInCount");
            WatchGUI.InitLabel(9, "MsgOut");
            WatchGUI.InitLabel(10, "MsgOutCount");
        }
    }

    @Override
    public void Show(String EventName) {
        if (WatchGUI != null) {
            WatchGUI.Show();
            if (WatchGUI.IsNew()) {
                Init();
                WatchGUI.UpdateFormCaption("FR Watches - ", EventName);
            }
            UpdateAll();
        }
    }

    @Override
    public void Update(int LabelID) {
        if (WatchGUI != null && WatchGUI.IsVisible()) {
            switch (LabelID) {
            case 1:
                WatchGUI.UpdateValue(1, getUndo());
                break;
            case 2:
                WatchGUI.UpdateValue(2, getRedo());
                break;
            case 3:
                WatchGUI.UpdateValue(3, TGlobalWatches.Instance.getMsgIn());
                WatchGUI.UpdateValue(4, String.valueOf(TGlobalWatches.Instance.FMsgInCount));
                break;
            case 5:
                WatchGUI.UpdateValue(5, TGlobalWatches.Instance.getMsgOut());
                WatchGUI.UpdateValue(6, String.valueOf(TGlobalWatches.Instance.FMsgOutCount));
                break;
            case 7:
                WatchGUI.UpdateValue(7, getMsgIn());
                WatchGUI.UpdateValue(8, String.valueOf(FMsgInCount));
                break;
            case 9:
                WatchGUI.UpdateValue(9, getMsgOut());
                WatchGUI.UpdateValue(10, String.valueOf(FMsgOutCount));
                break;
            }
        }
    }

    public void UpdateAll() {
        for (int i = 1; i <= 10; i++)
            Update(i);
    }

    public String getUndo() {
        return FUndo;
    }

    @Override
    public void setUndo(String value) {
        FUndo = value;
        Update(1);
    }

    public String getRedo() {
        return FRedo;
    }

    @Override
    public void setRedo(String value) {
        FRedo = value;
        Update(2);
    }

}
