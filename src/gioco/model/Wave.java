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
	private LinkedList<SpawnRequest> enemiesQueue;
	
	public Wave(int spawnDelay) {
		this.enemiesQueue = new LinkedList<>();
		this.spawnDelay = spawnDelay;
		
		}
	
	public void addEnemyGroup(int enemyType, int count, int pathIndex) {
        for (int i = 0; i < count; i++) {
            enemiesQueue.add(new SpawnRequest(enemyType, pathIndex));
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
	
	public SpawnRequest getNextEnemyType() {
		return enemiesQueue.poll(); 
	}
}

