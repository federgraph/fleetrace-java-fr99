package org.riggvar.js08;

/**
 * constants applicable to javascore
 */
public interface Constants {
    /**
     * these are the constants that defined the penalty values They come in three
     * groups:
     *
     * Non-Finish penalty values are for boats that do not have a valid Finish these
     * are used in BOTH the FinishPosition class and in the Penalty class These are
     * not-bitwise penalties and a boat cannot have more than one of these at a
     * time. See FinishPosition class
     *
     * Disqualification penalties are the various ways a boat can be disqualified.
     * These also are not bitwise, as a boat can only be disqualified once. But a
     * boat can be disqualified with or without a valid finish. So a boat can carry
     * both a non-finish penalty and a disqualification penalty. See Penalty class
     *
     * Other penalties are the various other ways a boat can be "dinged". This
     * includes check-in penalties, redress, percentage penalties, etc. These ARE
     * Bit-wise penalties, and a boat can have more than one of them. Also, a boat
     * CAN have a non-finish penalty and an other penalty, but a boat may not have a
     * disqualification penalty and "other" penalty. See Penalty class
     *
     * These penalty values are/should be used only internally to the program and
     * NOT written out to persistent storage (that is done by string name).
     * Therefore it should be safe to reset the values, provided the orders are not
     * changed and the types of penalties keep their bit-boundaries straight.
     */

    // These masks break up the integer into the portions reserved for
    // each penalty type.
    public final static int HIGHEST_FINISH = 0x1FFF; // highest possible real finish
    public final static int NOFINISH_MASK = 0xE000;
    public final static int DSQ_MASK = 0x0007;
    public final static int OTHER_MASK = 0x1FF8;

    public final static int NO_PENALTY = 0x0000;

    // Disqualification penalties
    public final static int DSQ = 0x0001; // these are not stable numbers!
    public final static int DNE = 0x0002;
    public final static int RAF = 0x0003;
    public final static int OCS = 0x0004;
    public final static int BFD = 0x0005; // added for 2001 rules
    public final static int DGM = 0x0006; // disqualified, gross misconduct, 2005 addition

    // Scoring penalties, these ARE BITWISE
    // available = 0x0008;
    public final static int TIM = 0x0010; // new apr03, an amount of time applied to elapsed time
    public final static int ZFP = 0x0020;
    public final static int AVG = 0x0040;
    public final static int SCP = 0x0080; // scoring penalty, percent of finish position
    public final static int RDG = 0x0100;
    public final static int MAN = 0x0200;
    public final static int CNF = 0x0400;
    public final static int TMP = 0x0800; // new apr03, scoring time penalty, percent of time
    public final static int DPI = 0x1000; // descretionary penalty imposed

    // Non-finishing penalties, these show up in the finish order column
    // and can get set as Finish "Positions"
    public final static int NOFINISH = 0xE000; // means no finish recorded yet
    public final static int DNC = 0xC000;
    public final static int DNS = 0xA000;
    public final static int DNF = 0x8000;
    public final static int TLE = 0x6000;
    // available = 0x4000;
    // available = 0x2000;

}
