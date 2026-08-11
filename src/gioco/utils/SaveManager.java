package gioco.utils;

import java.io.*;

public class SaveManager {
    
    // Il nome del file in cui salveremo i dati
    private static final String SAVE_FILE = "savegame.dat";

    // Metodo per salvare il progresso
    public static void saveProgress(int maxUnlockedLevel) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            writer.write(String.valueOf(maxUnlockedLevel));
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del gioco: " + e.getMessage());
        }
    }

    // Metodo per caricare il progresso
    public static int loadProgress() {
        File file = new File(SAVE_FILE);
        // Se il file esiste, lo leggiamo
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null) {
                    return Integer.parseInt(line.trim());
                }
            } catch (IOException | NumberFormatException e) {
                System.err.println("Errore durante il caricamento del salvataggio: " + e.getMessage());
            }
        }
        // Se il file non esiste (es. prima volta che si gioca) restituiamo 1
        return 1; 
    }
}