package gioco.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.net.URISyntaxException;

public class SaveManager {
    
    
    private static final String FILE_NAME = "savegame.dat";

   
    private static String getSaveDirectory() {
        String saveDir = "";
        try {
            
            File appLocation = new File(SaveManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            
            saveDir = appLocation.getParentFile().getAbsolutePath() + File.separator;
            
        } catch (URISyntaxException | SecurityException e) {
            System.err.println("Impossibile determinare la cartella del JAR, uso la cartella corrente.");
            saveDir = System.getProperty("user.dir") + File.separator;
        }

        return saveDir;
    }

    private static String getSaveFilePath() {
        return getSaveDirectory() + FILE_NAME;
    }

    public static void saveProgress(int maxUnlockedLevel, Map<Integer, Integer> levelStars) {
        String fullPath = getSaveFilePath();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {

            writer.write(String.valueOf(maxUnlockedLevel));
            writer.newLine();
            
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
    
    public static Map<Integer, Integer> loadStars() {
        Map<Integer, Integer> stars = new HashMap<>();
        File file = new File(getSaveFilePath());
        
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                reader.readLine(); 
                
                String line;

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