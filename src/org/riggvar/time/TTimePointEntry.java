package org.riggvar.time;

import org.riggvar.base.*;

public class TTimePointEntry extends TBOPersistent {
    public String OTime;
    public int ORank;
    public int Rank;
    public int PosR;
    public String Behind;
    public String BFT;
    public String BPL;
    public int PLZ;

    @Override
    public void Assign(Object source) {
        if (source instanceof TTimePointEntry) {
            TTimePointEntry e = (TTimePointEntry) source;
            OTime = e.OTime;
            Behind = e.Behind;
            BFT = e.BFT;
            BPL = e.BPL;
            ORank = e.ORank;
            Rank = e.Rank;
            PosR = e.PosR;
            PLZ = e.PLZ;
        } else if (source instanceof TTimePoint) {
            TTimePoint o = (TTimePoint) source;
            OTime = o.getOTime().toString();
            Behind = o.getBehind().toString();
            BFT = o.getBFT().toString();
            BPL = o.getBPL().toString();
            ORank = o.ORank;
            Rank = o.Rank;
            PosR = o.PosR;
            PLZ = o.PosR;
        } else {
            super.Assign(source);
        }
    }
}
