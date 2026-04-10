package gioco.model;

import java.util.List;

public interface IModel {
	
	int getPlayerHealth();
	
	int getGold();
	
	void setRallyPoint(int mouseX, int mouseY);
	
	boolean isSettingRallyPoint();
	
	void startSettingRallyPoint(TowerSlot slot);
	
	void subtractPlayerHealth(int health);
	
	void addGold(int gold);
	
	boolean isGameOver();
	
	void updateGame();
	
	List<TowerSlot> getAvailableSlots();
	
	boolean buildTower(TowerSlot slots, String towerType);
	
	List<Enemy> getActiveEnemies();
	
	List<Point> getEnemyPath();
	
	List<Projectile> getActiveProjectiles();
	
	List<Soldier> getActiveSoldier();
	
	int getCurrentWaveNumber();
	
	int getTotalWaves();
	
	TowerSlot getSelectedBuildSlot();
	
	void selectBuildSlot(TowerSlot slot);
	
	void deselectBuildSlot();
	
	TowerSlot getActiveBarracksSlot();
	
	void upgradeSelectedTower();
	
	
}
