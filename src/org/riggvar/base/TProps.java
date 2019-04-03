package org.riggvar.base;

public class TProps extends TBOPersistent {
    public TStringList Props = new TStringList();

    public TProps() {
    }

    @Override
    public void Assign(Object Source) {
        if (Source instanceof TProps) {
            TProps o = (TProps) Source;
            Props.Assign(o.Props);
        } else if (Source instanceof TStringList) {
            Props.Assign((TStringList) Source);
        } else {
            super.Assign(Source);
        }
    }

    public void getProp(int Index, TProp Prop) {
        Prop.Key = Props.getName(Index);
        Prop.Value = Props.getValueFromIndex(Index);
    }

    public String getValue(String Key) {
        int idx = Props.IndexOfKey(Key);
        if (idx == -1)
            return "";
        return Props.getValueFromIndex(idx);
    }

    public void setValue(String Key, String Value) {
        Props.setValue(Key, Value);
    }

    public int getCount() {
        return Props.getCount();
    }
}
