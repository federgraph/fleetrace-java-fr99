package org.riggvar.bo;

import org.riggvar.base.*;

public class TBOParams extends TAdapterParams {
    public TBOParams() {
        MaxStartlistCount = 256;
        MinStartlistCount = 2;
        StartlistCount = 8;
        //
        MinRaceCount = 1;
        MaxRaceCount = 20;
        RaceCount = 7;
        //
        MinITCount = 0;
        MaxITCount = 10;
        ITCount = 0;
    }

    public boolean IsWithinLimits() {
        return RaceCount >= MinRaceCount && ITCount >= MinITCount && StartlistCount >= MinStartlistCount

                && RaceCount <= MaxRaceCount && ITCount <= MaxITCount && StartlistCount <= MaxStartlistCount;
    }

    public void ForceWithinLimits() {
        if (RaceCount < MinRaceCount)
            RaceCount = MinRaceCount;
        if (RaceCount > MaxRaceCount)
            RaceCount = MaxRaceCount;

        if (ITCount < MinITCount)
            ITCount = MinITCount;
        if (ITCount > MaxITCount)
            ITCount = MaxITCount;

        if (StartlistCount < MinStartlistCount)
            StartlistCount = MinStartlistCount;
        if (StartlistCount > MaxStartlistCount)
            StartlistCount = MaxStartlistCount;
    }

}
