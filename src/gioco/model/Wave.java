package gioco.model;

public class Wave {

	private int enemyCount;
	private int spawnDelay;
	private int enemyType;
	
	public Wave(int enemyCount,int spawnDelay, int enemyType) {
		this.enemyCount = enemyCount;
		this.spawnDelay = spawnDelay;
		this.enemyType = enemyType;
		
		}
	
	public int getEnemyCount(){
			return this.enemyCount;
		}
	
	public int getEnemyType() {
		return this.enemyType;
	}
	
	public int getSpawnDelay() {
		return this.spawnDelay;
	}
	
	public void decreaseEnemyCount() {
		this.enemyCount--;
	}
	
	public boolean isFinished() {
		return this.enemyCount <= 0;
	}
}

