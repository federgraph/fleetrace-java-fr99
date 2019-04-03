package org.riggvar.input;

import org.riggvar.base.*;
import org.riggvar.bo.TMain;

public class TGender extends TInputValue {
    private TTokenList FAthleteStore;
    private TTokenList FRaceStore;
    public TRun1 Race1;

    public TGender(TBaseToken aOwner, String aNameID) {
        super(aOwner, aNameID);
        FAthleteStore = new TTokenList(this, TMain.BO.cTokenAthlete, new TAthlete());
        FRaceStore = new TTokenList(this, TMain.BO.cTokenRace, new TRun());
        Race1 = new TRun1(this, TMain.BO.cTokenRace + "1");
    }

    public TRun Race(int index) {
        return (TRun) FRaceStore.Token(index);
    }

    public TAthlete Athlete(int index) {
        return (TAthlete) FAthleteStore.Token(index);
    }
}
