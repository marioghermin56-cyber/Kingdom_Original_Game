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
	
	private int x, y;
	private int health;
	private double speed;
	private List<Point> path;
	private int targetWayPointIndex;
	private int value;
	private int maxHealth;
	private int type;
	private int tikCounter;
	private boolean isBlocked;
	private int attackDamage;
	private int attackCooldown;
	private int currentCooldown;
	
	public Enemy(int health, double speed, List<Point> path, int value, int type, int tikCounter, int attackDamage, int attackCooldown) {
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
		
		if(!path.isEmpty()) {
			this.x = path.get(0).getX();
			this.y = path.get(0).getY();
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
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getHealth() {
		return health;
	}
	
	public boolean hasReachedEnd() {
		return targetWayPointIndex >= path.size();
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
		
		if(isBlocked) {
			return;
		}
		
		if(hasReachedEnd()) {
			return;
		}
		
		
		Point target = path.get(targetWayPointIndex);
		
		if(this.x < target.getX()) {
			this.x += Math.min(speed,target.getX() - this.x ); 
		} else if (this.x > target.getX()) {
			this.x -= Math.min(speed, this.x - target.getX());
		} 
		
		if(this.y < target.getY()) {
			this.y += Math.min(speed, target.getY() - this.y);
		} else if(this.y > target.getY()) {
			this.y -= Math.min(speed, this.y - target.getY());
		}
		
		if (this.x == target.getX() && this.y == target.getY()) {
            targetWayPointIndex++;
        }
		
		this.tikCounter++;
	}
}
