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
		Level level = new Level("/assets/maps/mappaColfiorito.tmx", 20, 300, "/assets/SFONDI_MAPPE/backColfiorito.png", "/assets/SFONDI_MAPPE/topColfiorito.png");
		
		
		Wave wave1 = new Wave(120); 
		wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 5, 0); 
		wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 5, 1); 
		level.addWave(wave1);
		
		
		Wave wave2 = new Wave(100);
		wave2.addEnemyGroup(Enemy.GOBLIN_TYPE, 7, 0);
		wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 4, 1);
		wave2.shuffleEnemies(); 
		level.addWave(wave2);

		
		Wave wave3 = new Wave(110);
		wave3.addEnemyGroup(Enemy.ORC_TYPE, 2, 0);       
		wave3.addEnemyGroup(Enemy.SCORPION_TYPE, 4, 0);  
		wave3.addEnemyGroup(Enemy.GOBLIN_TYPE, 5, 1);    
		level.addWave(wave3);

		
		Wave wave4 = new Wave(90);
		wave4.addEnemyGroup(Enemy.ORC_TYPE, 2, 0);
		wave4.addEnemyGroup(Enemy.GOBLIN_TYPE, 8, 0);
		wave4.addEnemyGroup(Enemy.ORC_TYPE, 2, 1);
		wave4.addEnemyGroup(Enemy.GOBLIN_TYPE, 8, 1);
		level.addWave(wave4);
		
		return level;
	}
	
	
	public static Level createLevel2() {
		Level level = new Level("/assets/maps/mappaLago.tmx", 20, 400, "/assets/SFONDI_MAPPE/backLago.png", "/assets/SFONDI_MAPPE/topLago.png");
		
	
		Wave wave1 = new Wave(80); 
		wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 10, 0); 
		wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 10, 1);
		level.addWave(wave1);
		
		
		Wave wave2 = new Wave(100);
		wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 7, 0);
		wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 7, 1);
		wave2.addEnemyGroup(Enemy.GOBLIN_TYPE, 6, 0);
		wave2.shuffleEnemies();
		level.addWave(wave2);

		
		Wave wave3 = new Wave(100);
		wave3.addEnemyGroup(Enemy.ORC_TYPE, 3, 2);      
		wave3.addEnemyGroup(Enemy.SCORPION_TYPE, 8, 1); 
		wave3.addEnemyGroup(Enemy.GOBLIN_TYPE, 10, 0);  
		level.addWave(wave3);

		
		Wave wave4 = new Wave(80); 
		wave4.addEnemyGroup(Enemy.ORC_TYPE, 3, 0);
		wave4.addEnemyGroup(Enemy.SCORPION_TYPE, 10, 0);
		wave4.addEnemyGroup(Enemy.ORC_TYPE, 3, 1);
		wave4.addEnemyGroup(Enemy.GOBLIN_TYPE, 6, 2);
		level.addWave(wave4);

		
		Wave wave5 = new Wave(50);
		wave5.addEnemyGroup(Enemy.ORC_TYPE, 5, 0);
		wave5.addEnemyGroup(Enemy.ORC_TYPE, 5, 1);
		wave5.addEnemyGroup(Enemy.SCORPION_TYPE, 10, 0);
		wave5.addEnemyGroup(Enemy.GOBLIN_TYPE, 10, 1);
		wave5.shuffleEnemies();
		level.addWave(wave5);
		
		return level;
	}
	
	
		public static Level createLevel3() {
			Level level = new Level("/assets/maps/mappaNorcia.tmx", 20, 550, "/assets/SFONDI_MAPPE/backNorcia.png", "/assets/SFONDI_MAPPE/topNorcia.png");
			
			
			Wave wave1 = new Wave(80);
			wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 6, 0);
			wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 6, 1);
			wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 6, 2);
			wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 6, 3);
			level.addWave(wave1);
			
			
			Wave wave2 = new Wave(90);
			wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 5, 0);
			wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 4, 1);
			wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 5, 2);
			wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 4, 3);
			level.addWave(wave2);

			
			Wave wave3 = new Wave(90);
			wave3.addEnemyGroup(Enemy.ORC_TYPE, 2, 0);
			wave3.addEnemyGroup(Enemy.ORC_TYPE, 2, 3);
			wave3.addEnemyGroup(Enemy.SCORPION_TYPE, 5, 3);
			wave3.addEnemyGroup(Enemy.GOBLIN_TYPE, 8, 1);
			wave3.addEnemyGroup(Enemy.GOBLIN_TYPE, 8, 2);
			level.addWave(wave3);

			
			Wave wave4 = new Wave(80); 
			wave4.addEnemyGroup(Enemy.ORC_TYPE, 3, 1);
			wave4.addEnemyGroup(Enemy.SCORPION_TYPE, 8, 1);
			wave4.addEnemyGroup(Enemy.ORC_TYPE, 3, 2);
			wave4.addEnemyGroup(Enemy.SCORPION_TYPE, 8, 2);
			level.addWave(wave4);

			
			Wave wave5 = new Wave(70); 
			wave5.addEnemyGroup(Enemy.GOBLIN_TYPE, 20, 0);
			wave5.addEnemyGroup(Enemy.SCORPION_TYPE, 15, 1);
			wave5.addEnemyGroup(Enemy.SCORPION_TYPE, 15, 2);
			wave5.addEnemyGroup(Enemy.GOBLIN_TYPE, 20, 3);
			wave5.shuffleEnemies(); 
			level.addWave(wave5);

			
			Wave wave6 = new Wave(70);
			wave6.addEnemyGroup(Enemy.ORC_TYPE, 3, 0);
			wave6.addEnemyGroup(Enemy.SCORPION_TYPE, 8, 0);
			
			wave6.addEnemyGroup(Enemy.ORC_TYPE, 4, 1);
			wave6.addEnemyGroup(Enemy.GOBLIN_TYPE, 10, 1);
			
			wave6.addEnemyGroup(Enemy.ORC_TYPE, 4, 2);
			wave6.addEnemyGroup(Enemy.GOBLIN_TYPE, 10, 2);
			
			wave6.addEnemyGroup(Enemy.ORC_TYPE, 3, 3);
			wave6.addEnemyGroup(Enemy.SCORPION_TYPE, 8, 3);
			level.addWave(wave6);
			
			
			Wave wave7 = new Wave(150); 
			wave7.addEnemyGroup(Enemy.YETI_TYPE, 1, 0); 
			level.addWave(wave7);
			
			return level;
		}
}