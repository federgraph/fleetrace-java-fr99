package org.riggvar.event;

public class TInputMode {
    public static final int Strict = 0;
    public static final int Relaxed = 1;

    public static String getString(int e) {
        switch (e) {
        case Strict:
            return "Strict";
        case Relaxed:
            return "Relaxed";
        }
        return "";
    }
}
