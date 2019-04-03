package org.riggvar.base;

public class TContextMsg {
    public int KatID;
    public Object Sender;
    public boolean IsQueued;
    public boolean IsAdapterMsg;
    public TMsgSource MsgSource;
    public TMsgDirection MsgDirection;
    public char MsgType;
    public String msg;
    public boolean HasRequest;
    public String RequestString;
    public TStringList OutputRequestList; // initially nil by default
    public String Answer;

    public void Clear() {
        Sender = null;
        IsQueued = false;
        MsgSource = TMsgSource.Unknown;
        MsgDirection = TMsgDirection.Unknown;
        MsgType = '-';
        msg = "";
        HasRequest = false;
        RequestString = "";
        OutputRequestList = null;
    }

    public void DecodeHeader() {
        if (msg == null)
            return;
        String sep = String.valueOf((char) 4);
        String[] split = msg.split(sep, 2);
        if (split.length == 2) {
            String MsgHeader = split[0];
            if (MsgHeader.length() > 0) {
                MsgType = MsgHeader.charAt(0);
            }
            msg = split[1];
        }
    }

    public String getEncodedMsg() {
        // attention: parentheses necessary
        // error case: (char) 'I' + (char) '4' == "77"
        return MsgType + ((char) 4 + msg);
    }

    public boolean getIsSwitchMsg() {
        return MsgSource == TMsgSource.Switch;
    }

    public boolean getIsBridgeMsg() {
        return MsgSource == TMsgSource.Bridge;
    }

    public boolean getIsOutgoingMsg() {
        return MsgDirection == TMsgDirection.Outgoing;
    }

    public void setIsOutgoingMsg() {
        MsgDirection = TMsgDirection.Outgoing;
    }

    public boolean getIsIncomingMsg() {
        return MsgDirection == TMsgDirection.Incoming;
    }

    public void setIsIncomingMsg() {
        MsgDirection = TMsgDirection.Incoming;
    }
}
