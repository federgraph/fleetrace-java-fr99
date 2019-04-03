package org.riggvar.base;

public class DrawNotifierEventArgs {
    public static final int OperationCancel = 0;
    public static final int OperationSchedule = 1;

    public static final int DrawTargetAll = 0;
    public static final int DrawTargetEvent = 1;
    public static final int DrawTargetRace = 2;
    public static final int DrawTargetAthletes = 3;
    public static final int DrawTargetCache = 4;

    private int FDrawTarget = 0;
    private int FDrawAction = 0;

    public DrawNotifierEventArgs(int aDrawTarget) {
        FDrawTarget = aDrawTarget;
        FDrawAction = OperationSchedule;
    }

    public DrawNotifierEventArgs(int aDrawTarget, int aDrawAction) {
        FDrawTarget = aDrawTarget;
        FDrawAction = aDrawAction;
    }

    public int getDrawTarget() {
        return FDrawTarget;
    }

    public int getDrawAction() {
        return FDrawAction;
    }

}
