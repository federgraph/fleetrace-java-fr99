package org.riggvar.event;

import org.riggvar.base.*;

public class TEventEntry extends TBaseEntry {
    public int Bib;
    public int SNR;
    public String DN;
    public String NOC;
    public TEventRaceEntry[] Race;
    public TEventRaceEntry GRace;
    public int Cup;

    public TEventEntry(int aRaceCount) {
        super();
        Race = new TEventRaceEntry[aRaceCount + 1];
        // Race.Length = aRaceCount + 1; //SetLength(Race, aRaceCount + 1);
        for (int i = 0; i < getRCount(); i++) {
            Race[i] = new TEventRaceEntry(null);
        }
        GRace = Race[0];
    }

    public int getRCount() {
        return this.Race.length;
    }

}
