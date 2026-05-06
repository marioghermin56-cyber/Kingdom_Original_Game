package gioco.model;

import java.util.List;

import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.awt.image.BufferedImage;


public class Level {
	
	private String tmxPath;
	private List<Wave> waves;
	private int startingHealth;
	private int startingGold;
	private BufferedImage backLayerImage;
	private BufferedImage topLayerImage;
	
	
	public Level(String tmxPath, int startingHealth, int startingGold, String backLayerImagePath, String topLayerImagePath) {
		this.tmxPath = tmxPath;
		this.startingHealth = startingHealth;
		this.startingGold = startingGold;
		this.waves = new ArrayList<>();
		this.backLayerImage = loadImage(backLayerImagePath);
		this.topLayerImage = loadImage(topLayerImagePath);
	}
	
	 private BufferedImage loadImage(String path) {
	        try {
	            return ImageIO.read(getClass().getResourceAsStream(path));
	        } catch (Exception e) {
	            System.err.println("Impossibile caricare asset: " + path);
	            return null; 
	        }
	    }
	
	public void addWave(Wave wave) {
		waves.add(wave);
	}
	
	public BufferedImage getBackLayerImage() {
		return this.backLayerImage;
	}
	
	public BufferedImage getTopLayerImage() {
		return this.topLayerImage;
	}
	
	public String getTmxPath() {
		return this.tmxPath;
	}
	public List<Wave> getWaves(){
		return this.waves;
	}
	public int getStartingGold() {
		return this.startingGold;
	}
	public int getStartingHealth() {
		return this.startingHealth;
	}
	
}
