package org.riggvar.base;

public class TAdapterParams extends TBOPersistent {
    public boolean IsAdapter = false;
    public int InputPort = 3027;
    public int OutputPort = 3028;
    //
    public int MaxStartlistCount = 128;
    public int MinStartlistCount = 1;
    public int StartlistCount = 1;
    //
    public int MinRaceCount = 1;
    public int MaxRaceCount = 1;
    public int RaceCount = 1;
    //
    public int MinITCount = 0;
    public int MaxITCount = 10;
    public int ITCount = 0;
    //
    public int FieldCount = 6;
    //
    public String DivisionName = "*";

    public TAdapterParams() {
    }

    @Override
    public void Assign(Object Source) {
        if (Source instanceof TAdapterParams) {
            TAdapterParams o = (TAdapterParams) Source;

            IsAdapter = o.IsAdapter;
            //
            MaxStartlistCount = o.MaxStartlistCount;
            MinStartlistCount = o.MinStartlistCount;
            StartlistCount = o.StartlistCount;
            //
            MinRaceCount = o.MinRaceCount;
            MaxRaceCount = o.MaxRaceCount;
            RaceCount = o.RaceCount;
            //
            MinITCount = o.MinITCount;
            MaxITCount = o.MaxITCount;
            ITCount = o.ITCount;
            //
            DivisionName = o.DivisionName;
        } else {
            super.Assign(Source);
        }
    }

}
