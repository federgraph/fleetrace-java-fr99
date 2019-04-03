package org.riggvar.bo;

import java.net.URL;
import java.net.MalformedURLException;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class TSoundPlayerAudio implements LineListener, ISoundPlayer {
    boolean failedToLoad;
    boolean isPlaying;
    boolean playCompleted;

    // http://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples

    public void PlaySound(int soundID) {
        if (!isPlaying) {
            try {
                URL url = getUrl(soundID);
                String s = url.toString();
                play(s);
            } catch (MalformedURLException e) {
                System.out.println(e.getMessage());
                failedToLoad = true;
            }
        }
    }

    private URL getUrl(int soundID) throws MalformedURLException {
        String s;
        switch (soundID) {
        case 1:
            s = "download.wav";
            break;
        case 2:
            s = "upload.wav";
            break;
        case 3:
            s = "notify.wav";
            break;
        case 4:
            s = "clicker.wav";
            break;
        case 5:
            s = "shutter.wav";
            break;
        case 6:
            s = "ringin.wav";
            break;
        case 7:
            s = "ringout.wav";
            break;
        case 8:
            s = "recycle.wav";
            break;
        default:
            s = "clicker.wav";
            break;
        }
        URL url = new URL("file", "", TMain.FolderInfo.getAppDir() + "Sound\\" + s);
        return url;

    }

    /**
     * Play a given audio file.
     * 
     * @param audioFilePath Path of the audio file.
     */
    void play(String audioFilePath) {
        File audioFile = new File(audioFilePath);
        if (!audioFile.exists()) {
            return;
        }

        isPlaying = true;
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);

            Clip audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.addLineListener(this);

            audioClip.open(audioStream);

            audioClip.start();

            while (!playCompleted) {
                // wait for the clip to complete
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            audioClip.close();

        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }

    }

    /**
     * Listens to the START and STOP events of the audio line.
     */
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();

        if (type == LineEvent.Type.START) {
            // System.out.println("playing started.");

        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            // System.out.println("playing completed.");
            isPlaying = false;
        }

    }

}
