package gioco.utils; 

import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {
    
    private static Clip currentMusic;
    private static String currentTrackPath = ""; 

    public static void playMusic(String filePath) {
        try {
            if (currentMusic != null && currentMusic.isOpen()) {
                if (currentTrackPath.equals(filePath)) {
                    currentMusic.start();
                    currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
                    return;
                } else {
                    currentMusic.stop();
                    currentMusic.close();
                }
            }

            URL url = SoundManager.class.getResource(filePath);
            if (url != null) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
                currentMusic = AudioSystem.getClip();
                currentMusic.open(audioInput);
                currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
                currentMusic.start();
                currentTrackPath = filePath; 
            } else {
                System.err.println("Impossibile trovare il file audio: " + filePath);
            }
        } catch (Exception e) {
            System.err.println("Errore audio!");
            e.printStackTrace();
        }
    }

    public static void pauseMusic() {
        if (currentMusic != null && currentMusic.isRunning()) {
            currentMusic.stop(); 
        }
    }

    public static void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.close(); 
            currentMusic = null;  
            currentTrackPath = "";
        }
    }
} 