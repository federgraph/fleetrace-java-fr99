package org.riggvar.bridge;

public enum TSwitchOp {
    Plugin, Plugout, Synchronize, Upload, Download;

    public int ToInt() {
        switch (this) {
        case Plugin:
            return 0;
        case Plugout:
            return 1;
        case Synchronize:
            return 2;
        case Upload:
            return 3;
        case Download:
            return 4;
        default:
            return -1;
        }
    }
//    public static final int SwitchOp_Plugin = 0;
//    public static final int SwitchOp_Plugout = 1;
//    public static final int SwitchOp_Synchronize = 2;
//    public static final int SwitchOp_Upload = 3;
//    public static final int SwitchOp_Download = 4;

}
