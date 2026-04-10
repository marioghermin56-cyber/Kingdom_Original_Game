package gioco.model;

import java.util.List;

public class Soldier {

	private double x, y;
	private int health;
	private int maxHealth;
	private int damage;
	private int attackCooldown;
	private int currentCooldown;
	private Enemy target;
	private double destX, destY;
	private double speed = 2.0;
	private boolean isMoving = false;
	private int formationIndex;
	private int tikCounter=0;

	
	public Soldier(double x, double y, int formationIndex) {
		this.x = x;
		this.y = y;
		this.maxHealth = 50;
		this.health = this.maxHealth;
		this.attackCooldown = 30;
		this.damage = 3;
		this.currentCooldown = 0;
		this.target = null;	
		this.destX = x;
		this.destY = y;
		this.formationIndex = formationIndex;
	}
	
	
	public int getFormationIndex() {
		return this.formationIndex;
	}
	
	public int getTikCounter() {
		return this.tikCounter;
	}
	
	public void setDestination(Point destination) {
		
		if (this.target != null) {
	        this.target.setBlocked(false); // Il nemico torna a camminare
	        this.target = null;            // Il soldato non ha più un bersaglio
	    }
		
		this.destX = destination.getX();
		this.destY = destination.getY();
		this.isMoving = true;
		this.target = null;
	}
	
	public boolean isMoving() {
		return this.isMoving;
	}
	
	public void move() {
		if(!isMoving) {
			tikCounter = 0;
			return;
		}
		tikCounter++;
		
		double dx = destX - x;
		double dy = destY - y;
		
		double distance = Math.hypot(dx, dy);
		
		if(distance <= this.speed) {
			this.x = destX;
			this.y = destY;
			this.isMoving = false;
		}else {
			this.x += (dx/distance)*speed;
			this.y += (dy/distance)*speed;
		}		
	}
	
	public double getX() {
		return this.x;
	}
	
	public boolean isBusy() {
        return this.target != null && !this.target.isDead();
    }
	
	public double getY() {
		return this.y;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public int getCurrentCooldown() {
		return this.currentCooldown;
	}
	
	public int getMaxHealth() {
		return maxHealth; 
	}
	
    public boolean isDead() {
    	return health <= 0;
    }
    
    public Enemy getEnemy() {
    	return this.target;
    }
    
    public void setTarget(Enemy enemy) {
    	this.target = enemy;
    }
    
    public void takeDamage(int amount) {
    	this.health -= amount;
    }
    
    public void fight() {
    	if(target != null) {
    		if(currentCooldown <= 0) {
    			target.takeDamage(damage);
    			currentCooldown = attackCooldown;
    		} else {
    			currentCooldown--;
    		}
    	} else {
    		this.target = null;
    	}
    }
}
