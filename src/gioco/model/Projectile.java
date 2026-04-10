package gioco.model;


public class Projectile {

	private double x;
	private double y;
	private double projectileSpeed;
	private int damage;
	private Enemy target;
	private boolean hit;
	private int type;
	public static final int ARCHER_PROJECTILE = 1;
	public static final int MAGE_PROJECTILE = 2;
	public static final int CANNON_PROJECTILE = 3;
	
	public Projectile(double x, double y, double projectileSpeed, int damage, Enemy target, int type ) {
		this.x = x;
		this.y = y;
		this.projectileSpeed = projectileSpeed;
		this.damage = damage;
		this.target = target;
		this.type = type;
		this.hit = false;
		
		
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
			double distanceX = target.getX() - this.x;
			double distanceY = target.getY() - this.y;
			return Math.atan2(distanceY, distanceX);
		}
		return 0;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void move() {
		if(target == null || target.isDead() || hit) {
			hit = true;
			return;
		}
		
		double distanceX = this.x - target.getX();
		double distanceY = this.y - target.getY();
		double distanceFromEnemy = Math.hypot(distanceX, distanceY);
		//double distanza = Math.sqrt(Math.pow(distanzaX,2) + Math.pow(distanzaY, 2));
		
		if(distanceFromEnemy < 3) {
			target.takeDamage(damage);
			hit = true;
			return;
		} 
		
		double velX = (distanceX/distanceFromEnemy)*projectileSpeed;
		double velY = (distanceY/distanceFromEnemy)*projectileSpeed;		
		this.x -= velX;
		this.y -= velY;
			
	}
}
