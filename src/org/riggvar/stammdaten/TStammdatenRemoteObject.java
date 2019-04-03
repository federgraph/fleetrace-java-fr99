package org.riggvar.stammdaten;

import org.riggvar.base.*;

public class TStammdatenRemoteObject extends TStammdatenEntry {
    @Override
    protected void GetOutput() {
        SLADD("SNR", Utils.IntToStr(SNR));
        SLADD(FieldNames.FN, FN);
        SLADD(FieldNames.LN, LN);
        SLADD(FieldNames.SN, SN);
        SLADD(FieldNames.NC, NC);
        SLADD(FieldNames.GR, GR);
        SLADDLAST(FieldNames.PB, PB);
    }

    @Override
    public void Assign(Object source) {
        if (source instanceof TStammdatenRowCollectionItem) {
            TStammdatenRowCollectionItem e = (TStammdatenRowCollectionItem) source;
            SNR = e.SNR;
            FN = e.fFN;
            LN = e.fLN;
            SN = e.fSN;
            NC = e.fNC;
            GR = e.fGR;
            PB = e.fPB;
        } else if (source instanceof TStammdatenRemoteObject) {
            TStammdatenRemoteObject e = (TStammdatenRemoteObject) source;
            SNR = e.SNR;
            FN = e.FN;
            LN = e.LN;
            SN = e.SN;
            NC = e.NC;
            GR = e.GR;
            PB = e.PB;
        } else {
            super.Assign(source);
        }
    }

    public String getCommaText(TStrings SL) {
        if (SL == null) {
            return "";
        }

        SL.Clear();

        SL.Add(Utils.IntToStr(SNR));
        SL.Add(FN);
        SL.Add(LN);
        SL.Add(SN);
        SL.Add(NC);
        SL.Add(GR);
        SL.Add(PB);

        return SL.getCommaText();
    }

    @Override
    public void SetCommaText(TStrings SL) {
        if (SL == null) {
            return;
        }
        //
        TSLIterator i = new TSLIterator(SL);
        //
        SNR = i.NextI();
        FN = i.NextS();
        LN = i.NextS();
        SN = i.NextS();
        NC = i.NextS();
        GR = i.NextS();
        PB = i.NextS();
    }

    @Override
    public String GetCSV_Header() {
        return "SNR" + sep + FieldNames.FN + sep + FieldNames.LN + sep + FieldNames.SN + sep + FieldNames.NC + sep
                + FieldNames.GR + sep + FieldNames.PB;
    }

    // procedure GetFieldDefs(FD: TFieldDefs);
    // procedure UpdateDataSet(DS: TDataSet);
}
