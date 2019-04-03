package org.riggvar.bo;

public class TSoundPlayerBeep implements ISoundPlayer {
    public void PlaySound(int soundID) {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }
}
