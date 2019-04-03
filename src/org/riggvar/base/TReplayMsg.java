package org.riggvar.base;

public class TReplayMsg extends TBOPersistent {
    protected String FDivision;
    protected String FRunID;
    protected int FBib;
    protected String FCmd;
    protected String FMsgValue;

    public int FMsgType;
    public String FMsgKey;

    public int DBID; // auto incremented in DB
    public TDateTime LogTime;
    public int SeqNo;
    public boolean Delivered;
    public int CheckInt;
    public int ReplayInterval;
    public boolean Hidden;
    public int ReplayOrder;
    public boolean IsError; // do not persist
    public boolean IsCheckSumError; // do not persist

    public TReplayMsg() {
        ClearResult();
    }

    public void ClearResult() {
        // Content
        FDivision = "*";
        FRunID = "RunID";
        FBib = 0;
        FCmd = "Cmd";
        FMsgValue = "00:00:00.000";
        FMsgKey = "MsgKey";
        FMsgType = TBaseConst.MsgType_None;

        // Management
        DBID = -1;
        LogTime = TDateTime.Now();
        SeqNo = 1;
        IsError = false;
        IsCheckSumError = false;
        Delivered = false;
        CheckInt = 0;
        ReplayInterval = 1000;
        Hidden = false;
        ReplayOrder = 0;
    }

    @Override
    public void Assign(Object source) {
        if (source instanceof TReplayMsg) {
            TReplayMsg cr = (TReplayMsg) source;

            // Content
            FDivision = cr.FDivision;
            FRunID = cr.FRunID;
            FBib = cr.FBib;
            FCmd = cr.FCmd;
            FMsgValue = cr.FMsgValue;
            FMsgKey = cr.FMsgKey;
            FMsgType = cr.FMsgType;

            // Management
            DBID = cr.DBID;
            LogTime = cr.LogTime;
            SeqNo = cr.SeqNo;
            IsError = cr.IsError;
            IsCheckSumError = cr.IsCheckSumError;
            Delivered = cr.Delivered;
            CheckInt = cr.CheckInt;
            ReplayInterval = cr.ReplayInterval;
            Hidden = cr.Hidden;
            ReplayOrder = cr.ReplayOrder;
        } else {
            super.Assign(source);
        }
    }

    public String getDivision() {
        return FDivision;
    }

    public void setDivision(String value) {
        FDivision = value;
    }

    public String getRunID() {
        return FRunID;
    }

    public void setRunID(String value) {
        FRunID = value;
    }

    public int getBib() {
        return FBib;
    }

    public void setBib(int value) {
        FBib = value;
    }

    public String getCmd() {
        return FCmd;
    }

    public void setCmd(String value) {
        FCmd = value;
    }

    public String getMsgValue() {
        return FMsgValue;
    }

    public void setMsgValue(String value) {
        FMsgValue = value;
    }

    public String getMsgKey() {
        return FMsgKey;
    }

    public void setMsgKey(String value) {
        FMsgKey = value;
    }

    public int getMsgType() {
        return FMsgType;
    }

    public void setMsgType(int value) {
        FMsgType = value;
    }

    public String getAsString() {
        String sDBID;
        if (DBID < 0) {
            sDBID = "DBID";
        } else {
            sDBID = Utils.IntToStr(DBID);
        }
        return getCmd() + "," + getMsgValue() + "," + sDBID;
    }

    public void setAsString(String value) {
        java.util.StringTokenizer t = new java.util.StringTokenizer(value, ",");
        if (t.countTokens() >= 2) {
            setCmd(t.nextToken());
            setMsgValue(t.nextToken());
        }
        if (t.hasMoreTokens()) {
            DBID = Utils.StrToIntDef(t.nextToken(), -1);
        }
    }

    public String getDiskMsgHeader() {
        return "Cmd,MsgValue,ReplayInterval";
    }

    public String getDiskMsg() {
        String sep = ",";
        return getCmd() + sep + getMsgValue() + sep + Utils.IntToStr(ReplayInterval) + sep;
    }

}
