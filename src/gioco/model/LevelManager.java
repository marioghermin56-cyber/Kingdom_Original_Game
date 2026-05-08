package gioco.model;

public class LevelManager {

	public static Level getLevel(int level) {
		switch(level) {
			case 1:
				return createLevel1();
			case 2:
				return createLevel2();
			case 3:
				return createLevel3();
			default:
					return null;
		}
	}
	
	public static Level createLevel1() {
		Level level = new Level("/assets/maps/mappaColfiorito.tmx", 20, 400, "/assets/SFONDI_MAPPE/backColfiorito.png", "/assets/SFONDI_MAPPE/topColfiorito.png");
		
		Wave wave = new Wave(120); 
		wave.addEnemyGroup(Enemy.GOBLIN_TYPE, 10, 0); // Sentiero 0
		wave.addEnemyGroup(Enemy.ORC_TYPE, 10, 1);    // Sentiero 1
		wave.shuffleEnemies(); 
		level.addWave(wave);
		
		// ONDATA 2: Attacco da due lati!
		Wave wave2 = new Wave(50);
		wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 4, 0); // 4 scorpioni da sopra (percorso 0)
		wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 4, 1); // 4 scorpioni da sotto (percorso 1)
		wave2.shuffleEnemies(); 
		level.addWave(wave2);
		
		return level;
	}
	
	public static Level createLevel2() {
		Level level = new Level("/assets/maps/mappaLago.tmx", 20, 150, "/assets/SFONDI_MAPPE/backLago.png", "/assets/SFONDI_MAPPE/topLago.png");
		
		Wave wave = new Wave(120); 
		wave.addEnemyGroup(Enemy.GOBLIN_TYPE, 15, 0); // Sentiero 0
		wave.addEnemyGroup(Enemy.ORC_TYPE, 5, 1);    // Sentiero 1
		wave.shuffleEnemies(); 
		level.addWave(wave);
		
		// ONDATA 2: Attacco da due lati!
		Wave wave2 = new Wave(50);
		wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 20, 0); // 4 scorpioni da sopra (percorso 0)
		wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 20, 1); // 4 scorpioni da sotto (percorso 1)
		wave2.shuffleEnemies(); 
		level.addWave(wave2);
		
		return level;
	}
	
	public static Level createLevel3() {
		Level level = new Level("/assets/maps/mappaNorcia.tmx", 20, 250, "/assets/SFONDI_MAPPE/backNorcia.png", "/assets/SFONDI_MAPPE/topNorcia.png");
		
		
		Wave wave1 = new Wave(80);
		wave1.addEnemyGroup(Enemy.ORC_TYPE, 3, 3);
		wave1.addEnemyGroup(Enemy.ORC_TYPE, 3, 1);
		wave1.addEnemyGroup(Enemy.ORC_TYPE, 3, 2);
		wave1.addEnemyGroup(Enemy.ORC_TYPE, 3, 0);
		wave1.shuffleEnemies();
		level.addWave(wave1);
		
		
		return level;
	}
}
