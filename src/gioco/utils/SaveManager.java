package gioco.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.net.URISyntaxException;

public class SaveManager {
    
    // Il nome del file in cui salveremo i dati
    private static final String FILE_NAME = "savegame.dat";

    // 1. Metodo privato per ottenere la cartella del file .jar o .class
    private static String getSaveDirectory() {
        String saveDir = "";
        try {
            // Ottiene il percorso esatto del file .jar o della cartella dei bytecode
            File appLocation = new File(SaveManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            
            // Prende il percorso assoluto della cartella "genitore" (quella che contiene il .jar)
            saveDir = appLocation.getParentFile().getAbsolutePath() + File.separator;
            
        } catch (URISyntaxException | SecurityException e) {
            // Fallback: se qualcosa va storto, usiamo la working directory corrente
            System.err.println("Impossibile determinare la cartella del JAR, uso la cartella corrente.");
            saveDir = System.getProperty("user.dir") + File.separator;
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