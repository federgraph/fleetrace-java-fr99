package org.riggvar.base;

public class TMsgListFactory implements IMsgListFactory {
    public TBaseMsgList CreateMsg() {
        return new TBaseMsgList();
    }
}
