package org.riggvar.penalty;

public class StatusConst {
    public static final int Status_OK = 0;
    public static final int Status_DNS = 1;
    public static final int Status_DNF = 2;
    public static final int Status_DSQ = 3;
    public static final int Status_DSQPending = 4;

    public static final int csOK = 0;
    public static final int csDNS = 1;
    public static final int csDNF = 2;
    public static final int csDSQ = 3;
    public static final int csDSQPending = 4;

    public static String StatusStrings(int status) {
        switch (status) {
        case Status_OK:
            return "ok";
        case Status_DNS:
            return "dns";
        case Status_DNF:
            return "dnf";
        case Status_DSQ:
            return "dsq";
        case Status_DSQPending:
            return "*";

        // case csOK: return "ok";
        // case csDNS: return "dns";
        // case csDNF: return "dnf";
        // case csDSQ: return "dsq";
        // case csDSQPending: return "*";
        }
        return "";
    }

    public static int crsNone = 0;
    public static int crsStarted = 1;
    public static int crsResults = 2;
}
