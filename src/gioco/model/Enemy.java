package gioco.model;

import java.util.List;

public class Enemy {
	
	public static final int GOBLIN_TYPE = 1;
	public static final int ORC_TYPE = 7;
	public static final int SCORPION_TYPE = 8;
	public static final int YETI_TYPE = 9;
	
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
	private boolean isFacingRight = true;
	private int goldReward;

	private boolean isAttacking = false;
	private boolean isDying = false;
	private int deathTickCounter = 0;
	
	public Enemy(int health, double speed, EnemyPath path, int value, int type, int tikCounter, int attackDamage, int attackCooldown, int goldReward) {
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
		this.goldReward = goldReward;
		
		if(path != null && !path.getWaypoints().isEmpty()) {
			gioco.model.Point spawnPoint = path.getWaypoints().get(0);
			this.x = spawnPoint.getX();
			this.y = spawnPoint.getY();
		}
	}
	
	public boolean isFacingRight() {
		return this.isFacingRight;
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
	
	public int getGoldReward() {
		return this.goldReward;
	}
	
	public boolean hasReachedEnd() {
		return targetWayPointIndex >= path.getWaypoints().size();
	}
	
	public void takeDamage(int amount) {
		this.health -= amount;
        
        if (this.health <= 0 && !this.isDying) {
            this.health = 0;
            this.isDying = true;
        }
	}
	
	public boolean isDead() {
		return this.health <= 0;
	}
	
	public void setBlocked(boolean blocked) {
		this.isBlocked = blocked;
	}
	
   
    public boolean isAttacking() { return isAttacking; }
    public void setAttacking(boolean attacking) { this.isAttacking = attacking; }

    public boolean isDying() { return isDying; }
    
    public int getDeathTickCounter() { return deathTickCounter; }
    public void incrementDeathTick() { this.deathTickCounter++; }
	
    public void move() {
        
        if (isDying) {
            incrementDeathTick();
            return;
        }

        
        this.tikCounter++;

		if(isBlocked || hasReachedEnd()) {
			return; 
		}
		
		Point target = path.getWaypoints().get(targetWayPointIndex);
		
		double dx = target.getX() - this.x;
		double dy = target.getY() - this.y;
		
		double distance = Math.sqrt((dx * dx) + (dy * dy));
		
		if (dx != 0) {
		    this.isFacingRight = (dx > 0);
		}
		
		if (distance <= speed) {
			this.x = target.getX();
			this.y = target.getY();
			targetWayPointIndex++; 
		} else {
			this.x += (dx / distance) * speed;
			this.y += (dy / distance) * speed;
		}
	}
    public void setY(double y) {
        this.y = y;
    }
    public void setFacingRight(boolean facingRight) {
        this.isFacingRight = facingRight;
    }
}