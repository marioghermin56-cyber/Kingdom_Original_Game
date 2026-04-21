package gioco.model;

import java.util.List;

public class Enemy {

	
	public static final int GOBLIN_TYPE = 1;
	public static final int BLACKWIZARD_TYPE = 2;
	public static final int DARKGIANT_TYPE = 3;
	public static final int BLADESWORDSMAN_TYPE = 4;
	public static final int GHOST_TYPE = 5;
	public static final int LITTLEDEVIL_TYPE = 6;
	public static final int ORC_TYPE = 7;
	public static final int SCORPION_TYPE = 8;
	public static final int SKINHEAD_TYPE = 9;
	public static final int DEADSWORDSMAN_TYPE = 10;
	
	private double x, y;
	private int health;
	private double speed;
	
	private EnemyPath path;
	private int targetWayPointIndex;
	private int value;
	private int maxHealth;
	private int type;
	private int tikCounter;
	private boolean isBlocked;
	private int attackDamage;
	private int attackCooldown;
	private int currentCooldown;
	
	public Enemy(int health, double speed, EnemyPath path, int value, int type, int tikCounter, int attackDamage, int attackCooldown) {
		this.health = health;
		this.maxHealth = health;
		this.speed = speed;
		this.path = path;
		this.targetWayPointIndex = 1;
		this.value = value;
		this.type = type;
		this.tikCounter = tikCounter;
		this.isBlocked = false;
		this.attackDamage = attackDamage;
		this.attackCooldown = attackCooldown;
		this.currentCooldown = attackCooldown;
		
		if(path != null && !path.getWaypoints().isEmpty()) {
			gioco.model.Point spawnPoint = path.getWaypoints().get(0);
			this.x = spawnPoint.getX();
			this.y = spawnPoint.getY();
		}
		
	}
	
	public int getCurrentCooldown() {
		return this.currentCooldown;
	}
	
	public void decreaseCooldown() {
		this.currentCooldown--;
	}
	
	public void resetCooldown() {
		this.currentCooldown = attackCooldown;
	}
	
	public boolean isBlocked() {
		return this.isBlocked;
	}
	
	public int getAttackDamage() {
		return this.attackDamage;
	}
	
	public int getTikCounter() {
		return this.tikCounter;
	}
	
	public int getType() {
		return this.type;
	}
	
	public int getMaxHealth() {
		return this.maxHealth;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getHealth() {
		return health;
	}
	
	public boolean hasReachedEnd() {
		return targetWayPointIndex >= path.getWaypoints().size();
	}
	
	public void takeDamage(int amount) {
		this.health -= amount;
	}
	
	public boolean isDead() {
		return this.health <= 0;
	}
	
	public void setBlocked(boolean blocked) {
		this.isBlocked = blocked;
	}
	
public void move() {
		
		if(isBlocked || hasReachedEnd()) {
			return; // Se è bloccato o arrivato alla fine, non fa nulla
		}
		
		// Peschiamo il prossimo punto da raggiungere
		Point target = path.getWaypoints().get(targetWayPointIndex);
		
		// Calcoliamo le distanze su asse X e Y
		double dx = target.getX() - this.x;
		double dy = target.getY() - this.y;
		
		// Teorema di Pitagora per trovare la distanza totale in linea retta
		double distance = Math.sqrt((dx * dx) + (dy * dy));
		
		// Se la distanza rimanente è minore della nostra velocità, siamo arrivati al waypoint!
		if (distance <= speed) {
			this.x = target.getX();
			this.y = target.getY();
			targetWayPointIndex++; // Puntiamo al prossimo waypoint!
		} else {
			// Altrimenti, facciamo un passo verso il waypoint.
			// (dx / distance) e (dy / distance) creano il vettore normalizzato
			this.x += (dx / distance) * speed;
			this.y += (dy / distance) * speed;
		}
		
		this.tikCounter++;
	}
}
