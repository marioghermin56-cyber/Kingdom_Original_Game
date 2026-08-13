package gioco.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SaveManager {
    
    // Il nome del file in cui salveremo i dati (senza il percorso)
    private static final String FILE_NAME = "savegame.dat";
    
    // Il nome della cartella del tuo gioco (puoi modificarlo a piacimento)
    private static final String FOLDER_NAME = "KingdomRushClone";

    // 1. Metodo privato per ottenere e creare la cartella di sistema corretta
    private static String getSaveDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");
        String saveDir = "";

        if (os.contains("win")) {
            // Windows: C:\Users\NomeUtente\AppData\Roaming\KingdomRushClone\
            saveDir = System.getenv("APPDATA") + File.separator + FOLDER_NAME + File.separator;
        } else if (os.contains("mac")) {
            // Mac: /Users/NomeUtente/Library/Application Support/KingdomRushClone/
            saveDir = userHome + "/Library/Application Support/" + FOLDER_NAME + "/";
        } else {
            // Linux: /home/NomeUtente/.local/share/KingdomRushClone/
            saveDir = userHome + "/.local/share/" + FOLDER_NAME + "/";
        }

        // Se la cartella non esiste nel computer, la creiamo in automatico
        File directory = new File(saveDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return saveDir;
    }

    // 2. Metodo per ottenere il percorso assoluto e completo del file di salvataggio
    private static String getSaveFilePath() {
        return getSaveDirectory() + FILE_NAME;
    }

    // Metodo per salvare il progresso e le stelle
    public static void saveProgress(int maxUnlockedLevel, Map<Integer, Integer> levelStars) {
        String fullPath = getSaveFilePath();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
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
        File file = new File(getSaveFilePath());
        
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
    
    // Metodo per caricare la mappa delle stelle
    public static Map<Integer, Integer> loadStars() {
        Map<Integer, Integer> stars = new HashMap<>();
        File file = new File(getSaveFilePath());
        
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