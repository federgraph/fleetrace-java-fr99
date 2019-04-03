package org.riggvar.input;

import org.riggvar.stammdaten.*;

public class TAthlete extends TInputValue {
    public TAthlete() {
        super();
    }

    public void SNR(String Value) {
        SendMsg(IsValidSNR(Value), "SNR", Value);
    }

    public void FN(String Value) {
        SendMsg(IsValidName(Value), FieldNames.FN, Value);
    }

    public void LN(String Value) {
        SendMsg(IsValidName(Value), FieldNames.LN, Value);
    }

    public void SN(String Value) {
        SendMsg(IsValidName(Value), FieldNames.SN, Value);
    }

    public void NC(String Value) {
        SendMsg(IsValidNC(Value), FieldNames.NC, Value);
    }

    public void GR(String Value) {
        SendMsg(IsValidGR(Value), FieldNames.GR, Value);
    }

    public void PB(String Value) {
        SendMsg(IsValidPB(Value), FieldNames.PB, Value);
    }

    public void Prop(String Key, String Value) {
        SendMsg(IsValidProp(Key, Value), "Prop_" + Key, Value);
    }

    public void FieldN(int index, String Value) {
        SendMsg(IsValidName(Value), "N" + index, Value);
    }

}
