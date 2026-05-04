package gioco.utils; 

import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {
    
    private static Clip currentMusic;
    private static String currentTrackPath = ""; // Si ricorda quale canzone sta suonando!

    // Unico metodo per far partire o riprendere QUALSIASI canzone
    public static void playMusic(String filePath) {
        try {
            // Se c'è già una canzone, controlliamo cos'è
            if (currentMusic != null && currentMusic.isOpen()) {
                if (currentTrackPath.equals(filePath)) {
                    // È la STESSA: togliamo solo la pausa
                    currentMusic.start();
                    currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
                    return;
                } else {
                    // È DIVERSA (es. dal menu passiamo al livello): stoppiamo la vecchia
                    currentMusic.stop();
                    currentMusic.close();
                }
            }

            // Carichiamo la nuova traccia
            URL url = SoundManager.class.getResource(filePath);
            if (url != null) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
                currentMusic = AudioSystem.getClip();
                currentMusic.open(audioInput);
                currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
                currentMusic.start();
                currentTrackPath = filePath; // Salviamo il nome in memoria
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