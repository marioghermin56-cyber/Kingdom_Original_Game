package gioco.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
	
	private Map<Integer, LinkedList<Integer>> pathQueues;
	private Map<Integer, Integer> pathTimers;
	private int spawnDelay;
	
	public Wave(int spawnDelay) {
		this.spawnDelay = spawnDelay;
		this.pathQueues = new HashMap<>();
        this.pathTimers = new HashMap<>();
		
		}
	
	public void addEnemyGroup(int enemyType, int count, int pathIndex) {
		
		pathQueues.putIfAbsent(pathIndex, new LinkedList<>());
        pathTimers.putIfAbsent(pathIndex, 0);
        LinkedList<Integer> queue = pathQueues.get(pathIndex);
        for (int i = 0; i < count; i++) {
            queue.add(enemyType);
        }
    }
	
	public void shuffleEnemies() {
		for(LinkedList<Integer> queue : pathQueues.values()) {
			Collections.shuffle(queue);
		}
	}
	
	public int getSpawnDelay() {
		return this.spawnDelay;
	}
	
	public boolean isFinished() {
		for(LinkedList<Integer> queue : pathQueues.values()) {
			if(!queue.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	public Map<Integer, LinkedList<Integer>> getPathQueues() {
        return pathQueues;
    }
	
	public Map<Integer,Integer> getPathTimer(){
		return pathTimers;
	}
}

