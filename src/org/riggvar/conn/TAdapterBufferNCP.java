package org.riggvar.conn;

import java.util.Hashtable;

import org.riggvar.base.IServer;
import org.riggvar.base.TContextMsg;
import org.riggvar.base.TMsgSource;

public class TAdapterBufferNCP extends TAdapterBaseNCP {

    protected Object lastSender = null;
    protected StringBuffer sb = new StringBuffer();
    private Hashtable<Object, StringBuffer> bufferList = new Hashtable<Object, StringBuffer>();

    public TAdapterBufferNCP(IServer ts) {
        super(ts);
    }

    private void DeleteBuffer(Object sender) {
        if (bufferList.contains(sender)) {
            bufferList.remove(sender);
        }
    }

    private void GetBuffer(Object sender) {
        if (sender == lastSender) {
            return;
        }

        if (sender == null) {
            sb = new StringBuffer();
            return;
        }

        if (lastSender != null) {
            if (bufferList.containsKey(lastSender)) {
                bufferList.put(lastSender, sb);
            }
        }
        if (!bufferList.containsKey(sender)) {
            bufferList.put(sender, new StringBuffer());
        }

        sb = bufferList.get(sender);
        lastSender = sender;
    }

    @Override
    public void InjectMsg(Object sender, TMsgSource ms, String s) {
        GetBuffer(sender);
        char ch;
        for (int i = 0; i < s.length(); i++) {
            ch = s.charAt(i);
            if (ch == (char) 2) {
                sb = new StringBuffer();
            } else if (ch == (char) 3) {
                TContextMsg cm = new TContextMsg();
                cm.MsgSource = ms;
                cm.Sender = sender;
                cm.msg = sb.toString();
                HandleMsg(cm);
                DeleteBuffer(sender);
                break;
            } else {
                sb.append(ch);
            }
        }
    }

}
