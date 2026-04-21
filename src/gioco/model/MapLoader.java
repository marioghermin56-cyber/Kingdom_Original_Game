package gioco.model; 

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {

    public static List<EnemyPath> loadPathsFromTMX(String filePath) {
        List<EnemyPath> paths = new ArrayList<>();
        
        try {
            InputStream xmlStream = MapLoader.class.getResourceAsStream(filePath);
            if (xmlStream == null) {
                System.err.println("File mappa non trovato: " + filePath);
                return paths;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlStream);

            NodeList objectList = doc.getElementsByTagName("object");

            for (int i = 0; i < objectList.getLength(); i++) {
                Element objElement = (Element) objectList.item(i);

                // 1. REINSERITO: Leggiamo le coordinate di partenza dell'oggetto su Tiled
                double startX = Double.parseDouble(objElement.getAttribute("x"));
                double startY = Double.parseDouble(objElement.getAttribute("y"));

                NodeList polyList = objElement.getElementsByTagName("polyline");
                if (polyList.getLength() > 0) {
                    Element polyElement = (Element) polyList.item(0);
                    String[] pointPairs = polyElement.getAttribute("points").split(" ");

                    EnemyPath newPath = new EnemyPath();

                    // 2. REINSERITO: Cicliamo tutte le coordinate per unirle a quelle di partenza
                    for (String pair : pointPairs) {
                        String[] coords = pair.split(",");
                        
                        double relativeX = Double.parseDouble(coords[0]);
                        double relativeY = Double.parseDouble(coords[1]);

                        // Aggiungiamo le coordinate nude e crude (senza divider)
                        newPath.addPoint(startX + relativeX, startY + relativeY);
                    }
                    
                    paths.add(newPath);
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        
        return paths;
    }
    
    public static List<TowerSlot> loadSlotsFromTMX(String filePath) {
        List<TowerSlot> slots = new ArrayList<>();
        
        try {
            InputStream xmlStream = MapLoader.class.getResourceAsStream(filePath);
            if (xmlStream == null) return slots;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlStream);

            // Cerchiamo tutti i gruppi di oggetti
            NodeList objectGroups = doc.getElementsByTagName("objectgroup");

            for (int i = 0; i < objectGroups.getLength(); i++) {
                Element groupElement = (Element) objectGroups.item(i);
                
                // Se troviamo il livello chiamato "TowerSlots" (Attento alle maiuscole/minuscole che hai usato su Tiled!)
                if ("towerSlot".equals(groupElement.getAttribute("name"))) {
                    
                    NodeList objects = groupElement.getElementsByTagName("object");
                    
                    for (int j = 0; j < objects.getLength(); j++) {
                        Element objElement = (Element) objects.item(j);
                        
                        // Leggiamo i 4 numeri magici!
                        double x = Double.parseDouble(objElement.getAttribute("x"));
                        double y = Double.parseDouble(objElement.getAttribute("y"));
                        double width = Double.parseDouble(objElement.getAttribute("width"));
                        double height = Double.parseDouble(objElement.getAttribute("height"));
                        
                        // Creiamo lo slot e lo aggiungiamo alla lista
                        slots.add(new TowerSlot((int)x, (int)y, (int)width, (int)height));
                    }
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        
        return slots;
    }
}