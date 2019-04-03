package org.riggvar.time;

import org.riggvar.base.*;

public class TTimePoint extends TBOPersistent {
    private TPTime FOTime = new TPTime();
    private TPTime FBehind = new TPTime();
    private TQTime FBFT = new TQTime();
    private TQTime FBPL = new TQTime();

    public int ORank;
    public int Rank;
    public int PosR;
    public int PLZ;

    public TTimePoint() {
    }

    @Override
    public void Assign(Object source) {
        if (source instanceof TTimePoint) {
            TTimePoint o = (TTimePoint) source;
            FOTime.Assign(o.getOTime());
            FBehind.Assign(o.getBehind());
            FBFT.Assign(o.getBFT());
            FBPL.Assign(o.getBPL());
            ORank = o.ORank;
            Rank = o.Rank;
            PosR = o.PosR;
            PLZ = o.PLZ;
        } else if (source instanceof TTimePointEntry) {
            TTimePointEntry e = (TTimePointEntry) source;
            FOTime.Parse(e.OTime);
            FBehind.Parse(e.Behind);
            FBFT.Parse(e.BFT);
            FBPL.Parse(e.BPL);
            ORank = e.ORank;
            Rank = e.Rank;
            PosR = e.PosR;
            PLZ = e.PLZ;
        } else {
            super.Assign(source);
        }
    }

    public void Clear() {
        FOTime.Clear();
        FBehind.Clear();
        FBFT.Clear();
        FBPL.Clear();
        ORank = 0;
        Rank = 0;
        PosR = 0;
        PLZ = 0;
    }

    public TPTime getOTime() {
        return FOTime;
    }

    public void setOTime(TPTime value) {
        if (value != null) {
            FOTime.Assign(value);
        }
    }

    public TPTime getBehind() {
        return FBehind;
    }

    public void setBehind(TPTime value) {
        if (value != null) {
            FBehind.Assign(value);
        }
    }

    public TQTime getBFT() {
        return FBFT;
    }

    public void setBFT(TQTime value) {
        if (value != null) {
            FBFT.Assign(value);
        }
    }

    public TQTime getBPL() {
        return FBPL;
    }

    public void setBPL(TQTime value) {
        if (value != null) {
            FBPL.Assign(value);
        }
    }

}
