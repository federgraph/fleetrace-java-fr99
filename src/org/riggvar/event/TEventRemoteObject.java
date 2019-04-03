package org.riggvar.event;

import org.riggvar.stammdaten.*;
import org.riggvar.base.*;

public class TEventRemoteObject extends TEventEntry {
    public TEventRemoteObject(int aRaceCount) {
        super(aRaceCount);
    }

    @Override
    protected void GetOutput() {
        SLADD("Pos", Utils.IntToStr(SO));
        SLADD("Bib", Utils.IntToStr(Bib));
        //
        SLADD("SNR", Utils.IntToStr(SNR));
        SLADD("DN", DN);
        SLADD(FieldNames.NC, NOC);
        for (int i = 1; i < getRCount(); i++) {
            SLADD("R" + Utils.IntToStr(i), Race[i].getRaceValue());
        }
        SLADD("GPoints", GRace.getDecimalPoints());
        SLADD("GRank", "" + GRace.Rank);
        SLADDLAST("GPosR", "" + GRace.PosR);
    }

    @Override
    public void Assign(Object source) {
        if (source instanceof TEventRowCollectionItem) {
            TEventRowCollectionItem o = (TEventRowCollectionItem) source;
            //
            SO = o.BaseID;
            Bib = o.Bib;
            //
            SNR = o.SNR;
            DN = o.DN();
            NOC = o.NC();
            for (int i = 0; i < this.getRCount(); i++) {
                if (i < o.RCount()) {
                    Race[i].Assign(o.Race[i]);
                }
            }
            Cup = o.Cup;
        } else if (source instanceof TEventRemoteObject) {
            TEventRemoteObject e = (TEventRemoteObject) source;
            //
            SO = e.SO;
            Bib = e.Bib;
            //
            SNR = e.SNR;
            DN = e.DN;
            NOC = e.NOC;
            for (int i = 0; i < getRCount(); i++) {
                if (i < e.getRCount()) {
                    Race[i].Assign(e.Race[i]);
                }
            }
            Cup = e.Cup;
        } else {
            super.Assign(source);
        }
    }

    @Override
    public String GetCommaText(TStrings SL) {
        if (SL == null) {
            return "";
        }
        SL.Clear();
        SL.Add(Utils.IntToStr(SO));
        SL.Add(Utils.IntToStr(Bib));
        SL.Add(Utils.IntToStr(SNR));
        SL.Add(DN);
        SL.Add(NOC);

        for (int r = 1; r < getRCount(); r++)
            SL.Add(Race[r].getRaceValue());

        SL.Add(GRace.getDecimalPoints());
        SL.Add("" + GRace.Rank);
        SL.Add("" + GRace.PosR);

        return SL.getCommaText();
    }

    @Override
    public void SetCommaText(TStrings SL) {
        if (SL == null) {
            return;
        }
        TSLIterator i = new TSLIterator(SL);
        SO = i.NextI();
        Bib = i.NextI();
        SNR = i.NextI();
        DN = i.NextS();
        NOC = i.NextS();

        for (int r = 1; r < getRCount(); r++)
            Race[r].setRaceValue(i.NextS());

        GRace.setCPoints(Utils.StrToFloatDef(i.NextS(), 0.0));
        GRace.Rank = i.NextI();
        GRace.PosR = i.NextI();
    }

    @Override
    public String GetCSV_Header() {
        StringBuffer sb = new StringBuffer();
        sb.append("Pos" + sep + "Bib" + sep + "SNR" + sep + "DN" + sep + FieldNames.NC + sep);
        for (int r = 1; r < getRCount(); r++)
            sb.append("R" + r + sep);
        sb.append("GPoints" + sep + "GRank" + sep + "GPosR");
        return sb.toString();
    }
}
