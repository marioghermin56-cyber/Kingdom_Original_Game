package gioco.model;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class KingdomRushModel implements IModel{
	private int playerHealth;
	private int gold;
	private boolean gameOver;
	private List<TowerSlot> slots;
	private List<Enemy> enemies;
    //private List<Point> enemyPath;
    private List<Wave> waves;
    private int currentWaveIndex;
    private int spawnTimer;
    private List<Projectile> projectiles;
    private TowerSlot selectedSlot = null;
    private int tikCounter;
    private List<Soldier> activeSoldiers;
    private boolean isSettingRallyPoint = false;
	private TowerSlot activeBarracksSlot = null;
	private TowerSlot hoveredSlot = null;
	private List<EnemyPath> enemyPath;
	private Level currentLevel;
	
	public KingdomRushModel(int levelNumber) {
		Level data = LevelManager.getLevel(levelNumber);
		this.currentLevel = data;
		this.gold = data.getStartingGold();
		this.gameOver = false;
		this.playerHealth = data.getStartingHealth();
		this.enemies = new ArrayList<>();
		this.projectiles = new ArrayList();
		this.waves = data.getWaves();
		this.activeSoldiers = new ArrayList<>();
		this.enemyPath = MapLoader.loadPathsFromTMX(data.getTmxPath());
		this.slots = MapLoader.loadSlotsFromTMX(data.getTmxPath());
	}
	
	@Override
	public void setCurrentLevel(Level level) {
        this.currentLevel = level;
    }
	
	@Override
	public Level getCurrentLevel() {
        return currentLevel;
    }	
	
	@Override
	public List<EnemyPath> getMapPaths() {
        return enemyPath;
    }
	
	@Override
	public TowerSlot getSelectedBuildSlot() {
		return selectedSlot;
	}
	
	@Override
	public TowerSlot getHoveredSlot() {
		return this.hoveredSlot;
	}
	
	@Override 
	public void setHoveredSlot(TowerSlot slot) {
		this.hoveredSlot = slot;
	}
	
	@Override
	public void selectBuildSlot(TowerSlot slot) {
		this.selectedSlot = slot;
	}
	
	@Override 
	public void deselectBuildSlot(){
		this.selectedSlot = null;
	}
	
	public void startSettingRallyPoint(TowerSlot slot) {
		this.isSettingRallyPoint = true;
		this.activeBarracksSlot = slot;
	}
	
	public boolean isSettingRallyPoint() {
		return isSettingRallyPoint;
	}
	
	public void setRallyPoint(int logicalX, int logicalY) {
	    if (!isSettingRallyPoint || activeBarracksSlot == null) return;

	    Tower tower = activeBarracksSlot.getTower();

	    // 1. Calcoliamo il vero centro della piazzola (come nella View)
	    int cx = activeBarracksSlot.getX() + (activeBarracksSlot.getWidth() / 2);
	    int cy = activeBarracksSlot.getY() + (activeBarracksSlot.getHeight() / 2);

	    // 2. Calcoliamo se il click è dentro al range partendo dal CENTRO
	    double distToTower = Math.hypot(logicalX - cx, logicalY - cy);

	    // Usa tower.getRange() se la tua torre ha quel getter, oppure lascia 200!
	    if (distToTower <= tower.getRange()) {
	        
	        tower.setRallyPoint(new Point(logicalX, logicalY));

	        // 3. IL FIX DEI SOLDATI: Troviamo i nostri soldati tramite la carta d'identità
	        List<Soldier> mySoldiers = new ArrayList<>();
	        for (Soldier s : activeSoldiers) {
	            if (s.getParentTower() == tower) {
	                mySoldiers.add(s);
	            }
	        }

	        // 4. Assegniamo le nuove destinazioni sfalsate per tenerli in formazione
	        if (mySoldiers.size() > 0) mySoldiers.get(0).setDestination(new Point(logicalX - 10, logicalY - 10));
	        if (mySoldiers.size() > 1) mySoldiers.get(1).setDestination(new Point(logicalX + 10, logicalY - 10));
	        if (mySoldiers.size() > 2) mySoldiers.get(2).setDestination(new Point(logicalX, logicalY + 10));
	    }

	    // A prescindere che il click fosse valido o fuori range, usciamo dalla modalità
	    isSettingRallyPoint = false;
	    activeBarracksSlot = null;
	}
	
	@Override
	public void upgradeSelectedTower() {
		
		TowerSlot slot = getSelectedBuildSlot();
		
		if(slot != null && slot.isOccupied()) {
			Tower tower = slot.getTower();
			
			if(tower.canUpgrade() && this.gold >= tower.getUpgradeCost()) {
				this.gold -= tower.getUpgradeCost();
				tower.upgradeTower();
				deselectBuildSlot();
			}
		}
	}
	
	@Override
	public boolean buildTower(TowerSlot slots, String towerType) {
		if(slots.isOccupied()) {
			return false;
		}
		
		int cost = 0;
		
		if(towerType.equals("ARCHER")) {
			cost = Tower.ARCHER_BASE_COST;
		}else if(towerType.equals("MAGE")) {
			cost = Tower.MAGE_BASE_COST;
		}else if(towerType.equals("CANNON")) {
			cost = Tower.CANNON_BASE_COST;
		}else if(towerType.equals("BARRACKS")) {
			cost = Tower.BARRACKS_BASE_COST;
		}
		
		if(gold >= cost) {
			gold -= cost;
			
			if(towerType.equals("ARCHER")) {
				slots.setTower(new Tower(3,220,3, Tower.ARCHER_TYPE));
			}else if(towerType.equals("MAGE")) {
				slots.setTower(new Tower(8,130,5,Tower.MAGE_TYPE));
			}else if(towerType.equals("CANNON")) {
				slots.setTower(new Tower(15,150,7,Tower.CANNON_TYPE));
			}else if(towerType.equals("BARRACKS")) {
				slots.setTower(new Tower(0,150,90,Tower.BARRACKS_TYPE));
			}
			return true;
		}
		return false;
		
	}
	
	@Override
	public List<Projectile> getActiveProjectiles(){
		return projectiles;
	}
	
	@Override
	public List<Enemy> getActiveEnemies(){
		return enemies;
	}
	@Override
	public TowerSlot getActiveBarracksSlot() {
		return this.activeBarracksSlot;
	}
	
	public List<Soldier> getActiveSoldier(){
		return activeSoldiers;
	}
	
	public int getCurrentWaveNumber() {
		return currentWaveIndex + 1;
	}
	
	public int getTotalWaves() {
		return waves.size();
	}
	
	@Override
	public void updateGame() {
		if (gameOver) return;

		// 1. ECCO LE LISTE TEMPORANEE! (Vengono create qui dentro ad ogni "tick")
		List<Soldier> soldiersToAdd = new ArrayList<>();
		List<Soldier> soldiersToRemove = new ArrayList<>();
		List<Enemy> enemiesToAdd = new ArrayList<>();
		List<Enemy> enemiesToRemove = new ArrayList<>();
		List<Projectile> projectilesToAdd = new ArrayList<>();
		List<Projectile> projectilesToRemove = new ArrayList<>();

		// 2. MOVIMENTO E MORTE NEMICI
		for (Enemy enemy : enemies) {
			enemy.move();

			if (enemy.hasReachedEnd()) {
				subtractPlayerHealth(1);
				enemiesToRemove.add(enemy); // Invece di cancellarlo subito, lo mettiamo in lista
			} else if (enemy.isDead()) {
				addGold(enemy.getValue());
				enemiesToRemove.add(enemy);
			}
		}
		
		// 3. TORRI E CASERME
		for (TowerSlot slot : slots) {
			if (slot.isOccupied()) {
				Tower tower = slot.getTower();
				if (tower.getType() == Tower.BARRACKS_TYPE) {
					
					int numSoldiers = 0;
					boolean[] occupiedIndex = new boolean[3];
					for (Soldier soldier : activeSoldiers) {
						if (soldier.getParentTower() == tower) {
							numSoldiers++;
							occupiedIndex[soldier.getFormationIndex()] = true;
							if(numSoldiers >= 3) {
								break;
							}
							
						}
					}
					
					if (numSoldiers < 3) {
						tower.decreaseCooldown();
						
						if(tower.canShoot()) {
							double spawnX = slot.getX() - 5;
							double spawnY = slot.getY() + 30;
							
							int freeIndex = 0;
							
							for(int i = 0; i < 3; i++) {
								if(!occupiedIndex[i]) {
									freeIndex = i;
									break;
								}
							}						
						
							double offsetX = 0;
							double offsetY = 0;
							
							if (freeIndex == 0) {
								offsetX = -10;
								offsetY = -10;
								}
							else if (freeIndex  == 1) { 
								offsetX = 10;
								offsetY = -10; 
								}
							else if (freeIndex == 2) { 
								offsetX = 0;
								offsetY = 10; 
								}
							
							// C. CREIAMO IL SOLDATO DANDOGLI LA SUA "MATRICOLA"
							Soldier newSoldier = new Soldier(spawnX, spawnY, freeIndex, tower);
							soldiersToAdd.add(newSoldier);
							
							
							Point rp = tower.getRallyPoint();
							if (rp == null) {
								Point defaultPoint = calculateDefaultRallyPoint(slot.getX(), slot.getY());
								tower.setRallyPoint(defaultPoint);
								rp = defaultPoint;
							}
							
							newSoldier.setDestination(new Point(rp.getX() + (int)offsetX, rp.getY() + (int)offsetY));
							
							if (!tower.isInitialTrainingDone() && numSoldiers < 2) {
								tower.setCooldown(15); 
							} else {
								tower.setInitialTrainingDone(true); 
								tower.setCooldown(240); 
							}
						}
					} else {
						// SE CI SONO TUTTI E 3 I SOLDATI, IL TIMER SI FERMA A 240
						tower.setInitialTrainingDone(true);
						tower.setCooldown(240);
					}				
				
				} else { // Se è una torre d'attacco (non Caserma)
				    if (tower.canShoot()) {
				        for (Enemy enemy : enemies) {
				            if (!enemy.isDead() && !enemiesToRemove.contains(enemy)) {
				            	// CALCOLO DAL CENTRO (Sincronizza logica e grafica)
				                double towerCenterX = slot.getX() + (slot.getWidth() / 2.0);
				                double towerCenterY = slot.getY() + (slot.getHeight() / 2.0);
				                double enemyCenterX = enemy.getX() + 18.0; 
				                double enemyCenterY = enemy.getY() + 18.0;
				                
				                double distance = Math.hypot(enemyCenterX - towerCenterX, enemyCenterY - towerCenterY);
				                
				                if (distance <= tower.getRange()) {
				                    
				                    // Ripristiniamo la partenza dalla punta della torre per la grafica
				                    double spawnX = towerCenterX; 
				                    double spawnY = slot.getY() - 15; 
				                    
				                    if(tower.getType() == Tower.ARCHER_TYPE) {
				                        projectilesToAdd.add(new Projectile(spawnX, spawnY, 4.0, tower.getDamage(), enemy, tower.getType()));
				                    } else if(tower.getType() == Tower.MAGE_TYPE) {
				                        projectilesToAdd.add(new Projectile(spawnX, spawnY + 5, 3.0, tower.getDamage(), enemy, tower.getType()));
				                    } else if(tower.getType() == Tower.CANNON_TYPE) {
				                        projectilesToAdd.add(new Projectile(spawnX, slot.getY() + 16, 2.5, tower.getDamage(), enemy, tower.getType()));
				                    }
				                    tower.resetCooldown();
				                    break;
				                }
				            }
				        }
				    }
				    tower.decreaseCooldown(); 
				}
			}
		}
		
		for (Soldier soldier : activeSoldiers) {
			
			soldier.updateTikCounter();
			if(soldier.isDead()) {
				if (soldier.getEnemy() != null) {
					soldier.getEnemy().setBlocked(false); 
				}
				soldiersToRemove.add(soldier);
				continue;
			}
			
			if(soldier.isMoving()) {
				soldier.move();
				continue;
			}
			
			if(!soldier.isBusy()) {
				
				soldier.setTarget(null);
				
				for(Enemy enemy : enemies) {
					if(!enemy.isDead() && !enemy.isBlocked() && !enemiesToRemove.contains(enemy)) {
						double dist = Math.hypot(soldier.getX() - enemy.getX(), soldier.getY() - enemy.getY());
						if (dist < 30 && !soldier.isBusy()) {
							soldier.setTarget(enemy);
							enemy.setBlocked(true);
							break;
						}
					}
				}
			} else if(soldier.isBusy()){
				soldier.fight();
				if (!soldier.getEnemy().isDead()) {
					if (soldier.getEnemy().getCurrentCooldown() <= 0) {
						soldier.takeDamage(soldier.getEnemy().getAttackDamage());
						soldier.getEnemy().resetCooldown();
					} else {
						soldier.getEnemy().decreaseCooldown(); 
					}
					
				}
				
			}
		}
		
		// 5. PROIETTILI IN VOLO
		for (Projectile p : projectiles) {
			p.move(enemies);
			if (p.hasHit()) {
				projectilesToRemove.add(p);
			}
		}
		
		// 6. SPAWN DELLE ONDATE
		if (currentWaveIndex < waves.size()) {
			Wave currentWave = waves.get(currentWaveIndex);
			
			if(spawnTimer < 0) {
				spawnTimer++;
			}else if(!currentWave.isFinished()) {
				for(Integer pathIndex : currentWave.getPathQueues().keySet()) {
					LinkedList<Integer> queue = currentWave.getPathQueues().get(pathIndex);
					
					if(!queue.isEmpty()) {
						int timer = currentWave.getPathTimer().get(pathIndex);
						timer++;
						
						if(timer >= currentWave.getSpawnDelay()) {
							int type = queue.poll();
							EnemyPath assignedPath = enemyPath.get(pathIndex);

							if(type == Enemy.GOBLIN_TYPE) enemiesToAdd.add(new Enemy(40,0.5,assignedPath,5,type,tikCounter,3, 11));
							else if(type == Enemy.SCORPION_TYPE) enemiesToAdd.add(new Enemy(20,0.8,assignedPath,3,type,tikCounter,3 , 8));
							else if(type == Enemy.ORC_TYPE) enemiesToAdd.add(new Enemy(90,0.3,assignedPath,12,type,tikCounter,3 , 15));
							
							currentWave.getPathTimer().put(pathIndex, 0);
						} else {
							currentWave.getPathTimer().put(pathIndex, timer);
						}
					}
				}
			}
			if (currentWave.isFinished() && enemies.isEmpty()) {
				currentWaveIndex++;
				spawnTimer = -90; // Pausa di 90 tick prima della prossima ondata
			}
		}
		
		// --- 7. FASE FINALE: AGGIORNAMENTO DI MASSA ---
		activeSoldiers.addAll(soldiersToAdd);
		activeSoldiers.removeAll(soldiersToRemove);
		
		projectiles.addAll(projectilesToAdd);
		projectiles.removeAll(projectilesToRemove);
		
		enemies.addAll(enemiesToAdd);
		enemies.removeAll(enemiesToRemove);
	}
	
	@Override
	public List<TowerSlot> getAvailableSlots(){
		return slots;
	}
	
	@Override
	public int getPlayerHealth() {
		return playerHealth;
	}
	
	@Override
	public int getGold() {
		return gold;
	}
	
	public void subtractPlayerHealth(int health) {
		playerHealth -= health;
		if (playerHealth <= 0) {
			playerHealth = 0;
			gameOver = true;
		}
	}
	
	public void addGold(int gold) {
		this.gold += gold;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	// 2. Il tuo metodo aggiornato per il Rally Point
	private Point calculateDefaultRallyPoint(int towerX, int towerY) {
		Point closestPoint = new Point(towerX, towerY);
		double minDistance = Double.MAX_VALUE;
		
		// Scorriamo TUTTI i percorsi della mappa (se hai più di una strada)
		for(EnemyPath path : this.enemyPath) {
			
			// Scorriamo i punti (waypoints) dentro il singolo percorso
			for(Point pathPoint : path.getWaypoints()) {
				
				// Il tuo Math.hypot è perfetto qui!
				double dist = Math.hypot(towerX - pathPoint.getX(), towerY - pathPoint.getY());
				
				if(dist < minDistance) {
					minDistance = dist;
					
					// Salviamo le coordinate del punto più vicino
					closestPoint = new Point(pathPoint.getX(), pathPoint.getY());
				}
			}
		} 
		
		return closestPoint;
	}
	
	
}

