package gioco.model;

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
	
	// --- NUOVO CONTATORE MORTE ---
	private int deathTickCounter = 0;

	public Soldier(double x, double y, int formationIndex, Tower parentTower) {
		this.x = x;
		this.y = y;
		
		this.maxHealth = 60; 
		this.health = this.maxHealth;
		this.attackCooldown = 30; 
		this.damage = 3; 
		
		this.currentCooldown = 0;
		this.target = null;	
		this.destX = x;
		this.destY = y;
		this.formationIndex = formationIndex;
		this.parentTower = parentTower;
		
		int towerLvl = parentTower.getLvl();
		
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
	
	public int getFormationIndex() { return this.formationIndex; }
	public Tower getParentTower() { return this.parentTower; }
	public int getTikCounter() { return this.tikCounter; }
	public boolean isFacingRight() { return this.isFacingRight; }
	
	public void setDestination(Point destination) {
		if (this.target != null) {
	        this.target.setBlocked(false); 
	        this.target = null;            
	    }
		this.destX = destination.getX();
		this.destY = destination.getY();
		this.isMoving = true;
		this.target = null;
	}
	
	public boolean isMoving() { return this.isMoving; }
	
	public void move() {
		double dx = destX - x;
		double dy = destY - y;
		double distance = Math.hypot(dx, dy);
		
		if (dx != 0) this.isFacingRight = (dx > 0);

		if (distance <= this.speed) {
		    this.x = destX;
		    this.y = destY;
		    this.isMoving = false;
		} else {
		    this.x += (dx / distance) * speed;
		    this.y += (dy / distance) * speed;
		}	
	}
	
	public double getX() { return this.x; }
	public double getY() { return this.y; }
	public int getHealth() { return this.health; }
	public int getCurrentCooldown() { return this.currentCooldown; }
	public int getMaxHealth() { return maxHealth; }
	
	public boolean isBusy() {
        return this.target != null && !this.target.isDead();
    }
	
    public boolean isDead() { return health <= 0; }
    public Enemy getEnemy() { return this.target; }
    
    public void setTarget(Enemy enemy) {
    	this.target = enemy;
    	if (enemy != null) {
    	    this.isFacingRight = (enemy.getX() > this.x);
    	}
    }
    
    public void takeDamage(int amount) { this.health -= amount; }
    
    public void fight() {
    	if(target != null) {
    		this.isFacingRight = (target.getX() > this.x);
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
		this.health += (this.maxHealth - oldMaxHealth); 
	}
    
    public void updateTikCounter() { tikCounter++; }
    
    public int getDeathTickCounter() { return deathTickCounter; }
    public void incrementDeathTick() { this.deathTickCounter++; }
}