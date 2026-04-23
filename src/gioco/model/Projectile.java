package gioco.model;

import java.util.List;

public class Projectile {

	private double x, y;
	private double startX, startY;
	private double totalDistance;
	private double projectileSpeed;
	private int damage;
	private Enemy target;
	private boolean hit;
	private int type;
	public static final int ARCHER_PROJECTILE = 1;
	public static final int MAGE_PROJECTILE = 2;
	public static final int CANNON_PROJECTILE = 3;
	private double lastCalculatedAngle = 0.0;
	
	public Projectile(double x, double y, double projectileSpeed, int damage, Enemy target, int type ) {
		this.x = x;
		this.y = y;
		this.projectileSpeed = projectileSpeed;
		this.damage = damage;
		this.target = target;
		this.type = type;
		this.hit = false;
		this.startX = x;
		this.startY = y;
		
		this.totalDistance = Math.hypot(target.getX() - startX, target.getY() - startY);
		
		
	}
	
	public double getDistanceTraveled() {
        return Math.hypot(x - startX, y - startY);
    }
	
	public double getTotalDistanceToTravel() {
		return this.totalDistance;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public boolean hasHit() {
		return this.hit;
	}
	
	public double getAngle() {
		if(target!=null) {
			double distanceX = target.getX() - this.x + 18;
			double distanceY = target.getY() - this.y + 18;
			double dist = Math.hypot(distanceX, distanceY - y);
			if (dist > 10.0) {
		        lastCalculatedAngle = Math.atan2(target.getY() - y + 18, target.getY() - x + 18);
		    }
		    
		    // Ritorna l'angolo (se è vicinissimo, ritornerà l'ultimo angolo "sano" salvato!)
		    return lastCalculatedAngle;
		}
		return 0;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void move(List<Enemy> allEnemies) {
		if(target == null || target.isDead() || hit) {
			hit = true;
			return;
		}
		
		double distanceX = target.getX() - this.x ;
		double distanceY = target.getY() - this.y ;
		double distanceFromEnemy = Math.hypot(distanceX, distanceY);
		//double distanza = Math.sqrt(Math.pow(distanzaX,2) + Math.pow(distanzaY, 2));
		
		if(distanceFromEnemy < this.projectileSpeed) {
			
			hit = true;
			
			if(this.type == CANNON_PROJECTILE) {
				double explosionRadius = 45.0;
				
				for(Enemy enemy : allEnemies) {
					if(!enemy.isDead()) {
						double distFromExplosion = Math.hypot(enemy.getX() - target.getX(), enemy.getY() - target.getY());
						if(distFromExplosion <= explosionRadius) {
							enemy.takeDamage(damage); // Danno aggiuntivo per i nemici vicini all'esplosione
						}
					}
				}
		} else {
			target.takeDamage(damage);
		}
		}else {
		
			double velX = (distanceX/distanceFromEnemy)*projectileSpeed;
			double velY = (distanceY/distanceFromEnemy)*projectileSpeed;		
			this.x += velX;
			this.y += velY;
		}
			
	}
}
