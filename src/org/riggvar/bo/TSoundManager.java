package org.riggvar.bo;

public class TSoundManager {
    public static final int Sound_Click = 1;
    public static final int Sound_Recycle = 2;

    public boolean Enabled;
    public ISoundPlayer SoundPlayer = new TSoundPlayerMock();

    public TSoundManager() {
    }

    public void PlaySound(int soundID) {
        if (Enabled) {
            SoundPlayer.PlaySound(soundID);
        }
    }

}