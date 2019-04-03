package org.riggvar.event;

public class TEntryError {
    public static final int error_Duplicate_SNR = 0;
    public static final int error_Duplicate_Bib = 1;
    public static final int error_OutOfRange_Bib = 2;
    public static final int error_OutOfRange_SNR = 3;

    public static final int Count = 4;

    public static String getString(int e) {
        switch (e) {
        case TEntryError.error_Duplicate_Bib:
            return "duplicate SNR";
        case TEntryError.error_Duplicate_SNR:
            return "duplicate Bib";
        case TEntryError.error_OutOfRange_Bib:
            return "Bib out of range";
        case TEntryError.error_OutOfRange_SNR:
            return "Bib out of range";
        }
        return "";
    }
}
