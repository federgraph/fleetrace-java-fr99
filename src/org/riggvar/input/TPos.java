package org.riggvar.input;

public class TPos extends TInputValue {
    public TPos() {
        super();
    }

    public void Bib(String Value) {
        SendMsg(IsValidBib(Value), "Bib", Value);
    }

    public void SNR(String Value) {
        SendMsg(IsValidSNR(Value), "SNR", Value);
    }
}
