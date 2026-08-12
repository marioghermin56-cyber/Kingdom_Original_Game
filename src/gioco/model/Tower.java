package gioco.model;

public class Tower {

	private int damage;
	private int range;
	private int cooldown;
	private int currentCooldown;
	public static final int ARCHER_TYPE = 1;
	public static final int MAGE_TYPE = 2;
	public static final int BARRACKS_TYPE = 4;
	public static final int CANNON_TYPE = 3;
	public static final int ARCHER_BASE_COST = 70;
    public static final int MAGE_BASE_COST = 100;
    public static final int BARRACKS_BASE_COST = 70;
    public static final int CANNON_BASE_COST = 125;
	private int type;
	private Point rallyPoint;
	private boolean initialTrainingDone = false;
	private int lvl;
	private int maxLvl = 3;
	
	public Tower(int damage, int range, int cooldown, int type) {
		this.damage = damage;
		this.range = range;
		this.cooldown = cooldown;
		this.currentCooldown = 0;
		this.type = type;
		this.lvl  = 1;
	}
	
	public int getLvl() {
		return this.lvl;
	}
	
	public int getBaseCost(int type) {
        if (type == ARCHER_TYPE) return ARCHER_BASE_COST;
        if (type == MAGE_TYPE) return MAGE_BASE_COST;
        if (type == BARRACKS_TYPE) return BARRACKS_BASE_COST;
        if (type == CANNON_TYPE) return CANNON_BASE_COST;
        return 100;
    }
	
	public boolean canUpgrade() {
		return lvl < maxLvl;
	}
	
	public int getUpgradeCost() {
        int baseCost = getBaseCost(this.type);
        return baseCost * (this.lvl + 1);         
    }
	
	public void upgradeTower() {
		if(!canUpgrade()) {
			return;
		}
		
		lvl++;
		
		if (this.type == ARCHER_TYPE) {
            this.damage += 12;      
            this.cooldown -= 3;     
            this.range += 25;       // Raggio upgrade ORIGINALE
        } 
        else if (this.type == MAGE_TYPE) {
            this.damage += 55;      
            this.cooldown -= 5;
            this.range += 15;       // Raggio upgrade ORIGINALE
        } 
        else if (this.type == CANNON_TYPE) {
            this.damage += 28;      
            this.cooldown -= 10;    
            this.range += 10;       // Raggio upgrade ORIGINALE 
        }
        else if (this.type == BARRACKS_TYPE) {
            this.cooldown = Math.max(120, this.cooldown - 120); 
        }
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public int getCooldown() {
		return this.cooldown;
	}
	
	public Point  getRallyPoint() {
		return this.rallyPoint;
	}
	
	public void setRallyPoint(Point rallyPoint) {
		this.rallyPoint = rallyPoint;
	}
	
	public int getRange() {
		return this.range;
	}
	
	public int getType() {
		return this.type;
	}
	
	public boolean canShoot() {
		return currentCooldown <= 0;
	}
	
	public void resetCooldown() {
		this.currentCooldown = Tower.this.getCooldown();
	}
	
	public void decreaseCooldown() {
		if(currentCooldown > 0) {
			currentCooldown--;
		}
	}
	
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	
	public boolean isInitialTrainingDone() {
        return initialTrainingDone;
    }

    public void setInitialTrainingDone(boolean state) {
        this.initialTrainingDone = state;
    }
}
