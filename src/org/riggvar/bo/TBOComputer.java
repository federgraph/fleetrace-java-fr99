package org.riggvar.bo;

import org.riggvar.base.*;

public class TBOComputer implements IBOComputer {
    private TStrings SL = new TStringList();
    private TExcelImporter ExcelImporter = new TExcelImporter();

    public TBO StateFullBO;

    public TBOComputer() {
        InitGlobals();
    }

    private void InitGlobals() {
        TMain.MsgListFactory = new TBOMsgFactory();
    }

    public String Test(String EventData, boolean IsWebService) {
        if (IsWebService) {
            SL.setText(Utils.SwapLineFeed(EventData));
        } else {
            SL.setText(EventData);
        }
        for (int i = 0; i < SL.getCount(); i++) {
            SL.setString(i, i + ": " + SL.getString(i));

        }
        return SL.getText();
    }

    public String CalcStatefull(String EventData, boolean IsWebService) {
        return Calc(EventData, false, IsWebService);
    }

    public String CalcStateless(String EventData, boolean IsWebService) {
        InitGlobals();
        return Calc(EventData, true, IsWebService);
    }

    public String Calc(String EventData, boolean IsStateless, boolean IsWebService) {
        String t;

        if (IsWebService) {
            t = Utils.SwapLineFeed(EventData);
        } else {
            t = EventData;
        }

        ExcelImporter.RunImportFilter(t, SL);

        if (IsStateless) {
            if (SL.getCount() > 2) {
                TMain.BOManager.CreateNew(SL);
                SL.setText(TMain.BO.InputServer.Server.Connect().HandleMsg(SL.getText()));
            }
        } else {
            if (StateFullBO == null) {
                TMain.BOManager.CreateNew(SL);
                TMain.BO.Load(SL.getText());
                StateFullBO = TMain.BO;
            } else {
                TMain.BO = StateFullBO;
                TMain.BO.Load(SL.getText());
            }
            String s = SL.getString(0);
            SL.setText(TMain.BO.InputServer.Server.Connect().HandleMsg(s));
        }

        return SL.getText();
    }

}
