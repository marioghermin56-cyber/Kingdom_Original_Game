package gioco.model;

import java.util.List;
import java.util.ArrayList;

public class Level {
	
	private String tmxPath;
	private List<Wave> waves;
	private int startingHealth;
	private int startingGold;
	
	public Level(String tmxPath, int startingHealth, int startingGold) {
		this.tmxPath = tmxPath;
		this.startingHealth = startingHealth;
		this.startingGold = startingGold;
		this.waves = new ArrayList<>();
	}
	
	public void addWave(Wave wave) {
		waves.add(wave);
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
