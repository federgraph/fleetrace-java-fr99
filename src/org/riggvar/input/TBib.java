package org.riggvar.input;

import org.riggvar.base.*;

public class TBib extends TInputValue {
    public TBib() {
        super();
    }

    /** Test Message **/
    public void XX(String Value) {
        SendMsg(true, "XX", Value);
    }

    /** Quit Message (dsq, dnf, ...) **/
    public void QU(String Value) {
        SendMsg(IsValidStatus(Value), "QU", Value);
    }

    /** DSQ Gate **/
    public void DG(String Value) {
        SendMsg(IsValidDSQGate(Value), "DG", Value);
    }

    /** Start Time **/
    public void ST(String Value) {
        SendMsg(IsValidTime(Value), "ST", Value);
    }

    /** Intermediate Time **/
    public void IT(int channel, String Value) {
        SendMsg(IsValidTime(Value), "IT" + Utils.IntToStr(channel), Value);
    }

    /** Finish Time **/
    public void FT(String Value) {
        SendMsg(IsValidTime(Value), "FT", Value);
    }

    /** Finish Position **/
    public void Rank(String Value) {
        SendMsg(IsPositiveInteger(Value), "Rank", Value);
    }

    public void RV(String Value) {
        SendMsg(IsValidRace(Value), "RV", Value);
    }

    public void FM(String Value) {
        SendMsg(IsValidRace(Value), "FM", Value);
    }

}
