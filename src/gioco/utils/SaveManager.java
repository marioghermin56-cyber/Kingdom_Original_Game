package gioco.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SaveManager {
    
    // Il nome del file in cui salveremo i dati
    private static final String SAVE_FILE = "savegame.dat";

    // Metodo per salvare il progresso e le stelle
    public static void saveProgress(int maxUnlockedLevel, Map<Integer, Integer> levelStars) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            // Salviamo il livello massimo nella prima riga
            writer.write(String.valueOf(maxUnlockedLevel));
            writer.newLine();
            
            // Salviamo le stelle per ogni livello nelle righe successive (Formato: Livello,Stelle)
            if (levelStars != null) {
                for (Map.Entry<Integer, Integer> entry : levelStars.entrySet()) {
                    writer.write(entry.getKey() + "," + entry.getValue());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del gioco: " + e.getMessage());
        }
    }

    // Metodo per caricare il progresso (livello massimo sbloccato)
    public static int loadProgress() {
        File file = new File(SAVE_FILE);
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
        return 1; 
    }
    
    // Nuovo metodo per caricare la mappa delle stelle
    public static Map<Integer, Integer> loadStars() {
        Map<Integer, Integer> stars = new HashMap<>();
        File file = new File(SAVE_FILE);
        
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                // Saltiamo la prima riga perché contiene il maxUnlockedLevel
                reader.readLine(); 
                
                String line;
                // Leggiamo tutte le righe rimanenti
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        String[] parts = line.split(",");
                        if (parts.length == 2) {
                            int level = Integer.parseInt(parts[0].trim());
                            int starCount = Integer.parseInt(parts[1].trim());
                            stars.put(level, starCount);
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                System.err.println("Errore durante il caricamento delle stelle: " + e.getMessage());
            }
        }
        
        return stars;
    }
}