package gioco.model;


import java.util.LinkedList;
import java.util.Collections;

public class Wave {

	
	public static class SpawnRequest{
		public int type;
		public int pathIndex;
		
		public SpawnRequest(int type, int pathIndex) {
			this.type = type;
			this.pathIndex = pathIndex;
		}
	}
	private int spawnDelay;
	private LinkedList<Integer> enemiesQueue;
	
	public Wave(int spawnDelay) {
		this.enemiesQueue = new LinkedList<>();
		this.spawnDelay = spawnDelay;
		
		}
	
	public void addEnemyGroup(int enemyType, int count) {
		for (int i = 0; i < count; i++) {
			enemiesQueue.add(enemyType);
		}
	}
	
	public void shuffleEnemies() {
		Collections.shuffle(enemiesQueue);
	}
	
	public int getSpawnDelay() {
		return this.spawnDelay;
	}
	
	public boolean isFinished() {
		return enemiesQueue.isEmpty();
	}
	
	public int getNextEnemyType() {
		return enemiesQueue.poll(); 
	}
}

