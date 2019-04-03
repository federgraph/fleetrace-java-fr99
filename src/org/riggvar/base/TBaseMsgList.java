package org.riggvar.base;

import org.riggvar.bo.TMain;

public class TBaseMsgList extends TBaseMsg {

    private TStringList SL = new TStringList();
    public TStringList OutputRequestList = new TStringList();

    protected TBaseMsg NewMsg() {
        // virtual
        return new TBaseMsg();
    }

    private void ProcessRequestHeader() {
        String s;
        boolean b = false;

        int l = TMain.BO.cTokenRequest.length();
        do {
            if (SL.getCount() > 0) {
                b = (SL.getString(0).startsWith(TMain.BO.cTokenRequest));
            } else {
                b = false;
            }
            if (b) {
                s = Utils.CopyRest(SL.getString(0), l + 1);
                OutputRequestList.Add(TMain.BO.cTokenOutput + s);
                SL.Delete(0);
            }
        } while (b);
    }

    private void ProcessRequestInput() {
        if (SL.getCount() > 0) {
            TBaseMsg msg = NewMsg();
            for (int i = 0; i < SL.getCount(); i++) {
                if (IsComment(SL.getString(i))) {
                    continue;
                }
                msg.Prot = SL.getString(i);
                msg.DispatchProt();
            }
            SL.Clear();
        }
    }

    private boolean IsComment(String s) {
        if ((s.equals("")) || (Utils.Copy(s, 1, 2).equals("//")) || (Utils.Copy(s, 1, 1).equals("#"))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean DispatchProt() {
        ClearResult();

        SL.setText(Prot);

        // --if first line is a request
        if (Prot.startsWith(TMain.BO.cTokenRequest) || Prot.startsWith("RiggVar.Request.")
                || Prot.startsWith(TMain.BO.cTokenAnonymousRequest)) {
            boolean result = false;
            if (SL.getCount() > 0) {
                // one request-line, as usual
                int l;
                if (Prot.startsWith(TMain.BO.cTokenRequest)) {
                    l = TMain.BO.cTokenRequest.length();
                } else if (Prot.startsWith(TMain.BO.cTokenAnonymousRequest)) {
                    l = TMain.BO.cTokenAnonymousRequest.length();
                } else {
                    l = "RiggVar.Request.".length();
                }
                setMsgValue(Utils.CopyRest(SL.getString(0), l + 1));
                setCmd(TMain.BO.cTokenOutput);
                setMsgValue(TMain.BO.cTokenOutput + getMsgValue());
                SL.Delete(0);
                OutputRequestList.Add(getMsgValue());

                // all other output-request-lines
                ProcessRequestHeader();

                // all data lines
                ProcessRequestInput();

                Prot = ""; // we are finished with Prot
                result = true; // send answer later...
                // msg is added to queue next,
                // msg will be pulled from queue after calculation,
                // then the output will be generated and sent
            }
            MsgResult = 1; // do not reply with MsgID now
            return result;
        }

        // --multiline msg without request-header
        if (SL.getCount() > 1) {
            ProcessRequestInput();
            MsgResult = 1; // do not reply with MsgID
            return false;
        }

        // --single line msg
        if (SL.getCount() == 1) {
            TBaseMsg msg = NewMsg();
            msg.Prot = SL.getString(0);
            boolean result = msg.DispatchProt();
            MsgResult = msg.MsgResult;
            return result;
        }

        // --empty msg
        MsgResult = 1;
        return false;
    }

}
