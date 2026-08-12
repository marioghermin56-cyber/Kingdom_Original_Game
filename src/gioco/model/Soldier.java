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
	private Tower parentTower;
	private boolean isFacingRight = true;

	
	public Soldier(double x, double y, int formationIndex, Tower parentTower) {
		this.x = x;
		this.y = y;
		
		// --- BILANCIAMENTO TRUPPE ---
		this.maxHealth = 60; // <-- AUMENTATO A 60. Resistono più a lungo ai colpi pesanti.
		this.health = this.maxHealth;
		this.attackCooldown = 30; // Colpiscono velocemente per "distrarre" il nemico
		this.damage = 3; // <-- MANTENUTO A 3. Il loro scopo è bloccare, non uccidere.
		
		this.currentCooldown = 0;
		this.target = null;	
		this.destX = x;
		this.destY = y;
		this.formationIndex = formationIndex;
		this.parentTower = parentTower;
		
		// 1. Leggiamo il livello attuale della torre
		int towerLvl = parentTower.getLvl();
		
		// 2. Impostiamo le statistiche in base al livello
		if (towerLvl == 1) {
			this.maxHealth = 60;
			this.damage = 5;
		} else if (towerLvl == 2) {
			this.maxHealth = 100;
			this.damage = 10;
		} else if (towerLvl == 3) {
			this.maxHealth = 150;
			this.damage = 18;
		}
	}
	
	
	public int getFormationIndex() {
		return this.formationIndex;
	}
	
	public Tower getParentTower() {
		return this.parentTower;
	}
	
	public int getTikCounter() {
		return this.tikCounter;
	}
	
	public boolean isFacingRight() {
		return this.isFacingRight;
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
		
		double dx = destX - x;
		double dy = destY - y;
		
		double distance = Math.hypot(dx, dy);
		
		if (dx != 0) {
		    this.isFacingRight = (dx > 0);
		}

		// 2. Movimento fisico
		if (distance <= this.speed) {
		    this.x = destX;
		    this.y = destY;
		    this.isMoving = false;
		} else {
		    this.x += (dx / distance) * speed;
		    this.y += (dy / distance) * speed;
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
    	if (enemy != null) {
    	    this.isFacingRight = (enemy.getX() > this.x);
    	}
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
    
    public void applyUpgrade() {
		int newLvl = parentTower.getLvl();
		int oldMaxHealth = this.maxHealth;
		
		if (newLvl == 2) {
			this.maxHealth = 100;
			this.damage = 6;
		} else if (newLvl == 3) {
			this.maxHealth = 150;
			this.damage = 10;
		}
		
		// Aumentiamo la vita attuale del soldato dello stesso quantitativo di vita massima guadagnata
		this.health += (this.maxHealth - oldMaxHealth); 
	}
    
    public void updateTikCounter() {
    	tikCounter++;
    }
}
