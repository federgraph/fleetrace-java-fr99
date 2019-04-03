package org.riggvar.base;

public class LookupKatID {
    public static final int Readme_en = 1;
    public static final int Readme_de = 2;

    public static final int SBPGS = 300;
    public static final int PGSF = 301;
    public static final int PGSQ = 302;
    public static final int PGSE = 303;
    public static final int PGSC = 304;
    public static final int PGSV = 305;

    public static final int FR = 400;

    public static final int Rgg = 500;
    public static final int SKK = 600;

    public static final int Adapter = 700;

    public LookupKatID() {
    }

    public static String AsString(int KatID) {
        switch (KatID) {
        case Readme_en:
            return "Readme_en";
        case Readme_de:
            return "Readme_de";

        case PGSF:
            return "PGSF";
        case PGSQ:
            return "PGSQ";
        case PGSE:
            return "PGSE";
        case PGSC:
            return "PGSC";
        case PGSV:
            return "PGSV";

        case FR:
            return "FR";
        case Rgg:
            return "Rgg";
        case SKK:
            return "SKK";

        case Adapter:
            return "Adapter";

        default:
            break;
        }
        return "";
    }

    public static int AsInteger(String KatName) {
        if (KatName.equals("Readme_en")) {
            return Readme_en;
        } else if (KatName.equals("Readme_de")) {
            return Readme_de;
        }

        else if (KatName.equals("PGSF")) {
            return PGSF;
        } else if (KatName.equals("PGSQ")) {
            return PGSQ;
        } else if (KatName.equals("PGSE")) {
            return PGSE;
        } else if (KatName.equals("PGSC")) {
            return PGSC;
        } else if (KatName.equals("PGSV")) {
            return PGSV;
        }

        else if (KatName.equals("FR")) {
            return FR;
        } else if (KatName.equals("FleetRace")) {
            return FR;
        } else if (KatName.equals("Rgg")) {
            return Rgg;
        } else if (KatName.equals("SKK")) {
            return SKK;
        }

        else if (KatName.equals("Adapter")) {
            return Adapter;
        }

        return 0;
    }

}
