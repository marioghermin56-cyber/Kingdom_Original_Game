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
	
	// ==========================================
	// LIVELLO 1: IL RISCALDAMENTO
	// Vite: 20 | Oro Iniziale: 300
	// ==========================================
	public static Level createLevel1() {
		Level level = new Level("/assets/maps/mappaColfiorito.tmx", 20, 300, "/assets/SFONDI_MAPPE/backColfiorito.png", "/assets/SFONDI_MAPPE/topColfiorito.png");
		
		// ONDATA 1 (Sciame Leggero): Goblins da entrambi i sentieri
		Wave wave1 = new Wave(120); 
		wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 5, 0); 
		wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 5, 1); 
		level.addWave(wave1);
		
		// ONDATA 2 (Sciame Veloce): Goblins mischiati a Scorpioni per testare i riflessi
		Wave wave2 = new Wave(100);
		wave2.addEnemyGroup(Enemy.GOBLIN_TYPE, 7, 0);
		wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 4, 1);
		wave2.shuffleEnemies(); 
		level.addWave(wave2);

		// ONDATA 3 (Tattica Tank): L'Orco prende i danni, gli scorpioni corrono dietro
		// NOTA: Nessuno shuffle! Ordine rigoroso.
		Wave wave3 = new Wave(110);
		wave3.addEnemyGroup(Enemy.ORC_TYPE, 2, 0);       // Il Tank apre la strada
		wave3.addEnemyGroup(Enemy.SCORPION_TYPE, 4, 0);  // I veloci sfruttano la distrazione
		wave3.addEnemyGroup(Enemy.GOBLIN_TYPE, 5, 1);    // Pressione sull'altro lato
		level.addWave(wave3);

		// ONDATA 4 (Tattica Tank Doppia): Orchi su entrambi i lati seguiti da Goblins
		Wave wave4 = new Wave(90);
		wave4.addEnemyGroup(Enemy.ORC_TYPE, 2, 0);
		wave4.addEnemyGroup(Enemy.GOBLIN_TYPE, 8, 0);
		wave4.addEnemyGroup(Enemy.ORC_TYPE, 2, 1);
		wave4.addEnemyGroup(Enemy.GOBLIN_TYPE, 8, 1);
		level.addWave(wave4);
		
		return level;
	}
	
	// ==========================================
	// LIVELLO 2: L'ASSEDIO
	// Vite: 15 | Oro Iniziale: 350
	// ==========================================
	public static Level createLevel2() {
		Level level = new Level("/assets/maps/mappaLago.tmx", 20, 400, "/assets/SFONDI_MAPPE/backLago.png", "/assets/SFONDI_MAPPE/topLago.png");
		
		// ONDATA 1 (Sciame): Assalto immediato e numeroso per forzare l'uso delle torri ad area
		Wave wave1 = new Wave(80); 
		wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 10, 0); 
		wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 10, 1);
		level.addWave(wave1);
		
		// ONDATA 2 (Sciame Veloce): Pioggia di scorpioni mescolati
		Wave wave2 = new Wave(100);
		wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 7, 0);
		wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 7, 1);
		wave2.addEnemyGroup(Enemy.GOBLIN_TYPE, 6, 0);
		wave2.shuffleEnemies();
		level.addWave(wave2);

		// ONDATA 3 (Tattica Tank Invertita): Un lato riceve il tank, l'altro lo sciame
		Wave wave3 = new Wave(100);
		wave3.addEnemyGroup(Enemy.ORC_TYPE, 3, 2);      // I Tank sul lato 1
		wave3.addEnemyGroup(Enemy.SCORPION_TYPE, 8, 1); // Seguiti dagli scorpioni sul lato 1
		wave3.addEnemyGroup(Enemy.GOBLIN_TYPE, 10, 0);  // Mentre il lato 0 è invaso dallo sciame
		level.addWave(wave3);

		// ONDATA 4 (Tattica Tank Pura su entrambi i fronti)
		Wave wave4 = new Wave(80); 
		wave4.addEnemyGroup(Enemy.ORC_TYPE, 3, 0);
		wave4.addEnemyGroup(Enemy.SCORPION_TYPE, 10, 0);
		wave4.addEnemyGroup(Enemy.ORC_TYPE, 3, 1);
		wave4.addEnemyGroup(Enemy.GOBLIN_TYPE, 6, 2);
		level.addWave(wave4);

		// ONDATA 5 (L'Orda Finale): Sciame caotico e continuo (spawn veloce a 50 tick)
		Wave wave5 = new Wave(50);
		wave5.addEnemyGroup(Enemy.ORC_TYPE, 5, 0);
		wave5.addEnemyGroup(Enemy.ORC_TYPE, 5, 1);
		wave5.addEnemyGroup(Enemy.SCORPION_TYPE, 10, 0);
		wave5.addEnemyGroup(Enemy.GOBLIN_TYPE, 10, 1);
		wave5.shuffleEnemies();
		level.addWave(wave5);
		
		return level;
	}
	
	// ==========================================
		// LIVELLO 3: LA RESA DEI CONTI (BILANCIATO)
		// Vite: 15 | Oro Iniziale: 550
		// ==========================================
		public static Level createLevel3() {
			Level level = new Level("/assets/maps/mappaNorcia.tmx", 20, 550, "/assets/SFONDI_MAPPE/backNorcia.png", "/assets/SFONDI_MAPPE/topNorcia.png");
			
			// ONDATA 1 (Sciame a 4 Vie): Pressione su tutte le linee
			Wave wave1 = new Wave(80);
			wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 6, 0);
			wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 6, 1);
			wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 6, 2);
			wave1.addEnemyGroup(Enemy.GOBLIN_TYPE, 6, 3);
			level.addWave(wave1);
			
			// ONDATA 2 (Sciame Veloce): Scorpioni
			Wave wave2 = new Wave(90);
			wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 5, 0);
			wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 4, 1);
			wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 5, 2);
			wave2.addEnemyGroup(Enemy.SCORPION_TYPE, 4, 3);
			level.addWave(wave2);

			// ONDATA 3 (Tattica Tank su Linee Esterne): Orchi fuori, Goblins al centro
			Wave wave3 = new Wave(90);
			wave3.addEnemyGroup(Enemy.ORC_TYPE, 2, 0);
			wave3.addEnemyGroup(Enemy.ORC_TYPE, 2, 3);
			wave3.addEnemyGroup(Enemy.SCORPION_TYPE, 5, 3);
			wave3.addEnemyGroup(Enemy.GOBLIN_TYPE, 8, 1);
			wave3.addEnemyGroup(Enemy.GOBLIN_TYPE, 8, 2);
			level.addWave(wave3);

			// ONDATA 4 (Tattica Tank Incrociata)
			Wave wave4 = new Wave(80); 
			wave4.addEnemyGroup(Enemy.ORC_TYPE, 3, 1);
			wave4.addEnemyGroup(Enemy.SCORPION_TYPE, 8, 1);
			wave4.addEnemyGroup(Enemy.ORC_TYPE, 3, 2);
			wave4.addEnemyGroup(Enemy.SCORPION_TYPE, 8, 2);
			level.addWave(wave4);

			// ONDATA 5 (Sciame Letale Bilanciato): Rallentato da 30 a 50 tick
			Wave wave5 = new Wave(70); 
			wave5.addEnemyGroup(Enemy.GOBLIN_TYPE, 20, 0);
			wave5.addEnemyGroup(Enemy.SCORPION_TYPE, 15, 1);
			wave5.addEnemyGroup(Enemy.SCORPION_TYPE, 15, 2);
			wave5.addEnemyGroup(Enemy.GOBLIN_TYPE, 20, 3);
			wave5.shuffleEnemies(); 
			level.addWave(wave5);

			// ONDATA 6 (Il Muro di Carne Ridotto): Meno Orchi (da 6 a 4) per sentiero
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
			
			// ONDATA 7 (IL BOSS FINALE): L'arrivo del Titano
			Wave wave7 = new Wave(150); 
			wave7.addEnemyGroup(Enemy.YETI_TYPE, 1, 0); 
			level.addWave(wave7);
			
			return level;
		}
}