package gioco.model;

import java.util.List;

public class Projectile {
    private double x, y;
    private double startX, startY;
    private double currentSpeed; 
    private double projectileSpeed;
    private int damage;
    private Enemy target;
    private boolean hit;
    private int type;
    public static final int ARCHER_PROJECTILE = 1;
    public static final int MAGE_PROJECTILE = 2;
    public static final int CANNON_PROJECTILE = 3;
    
    private double actualDistanceTraveled = 0.0; 
    private double dynamicTotalDistance = 0.0;   
    private double lastCalculatedAngle = 0.0;
    private final boolean facingRight;

    public Projectile(double x, double y, double projectileSpeed, int damage, Enemy target, int type) {
        this.x = x;
        this.y = y;
        this.projectileSpeed = projectileSpeed;
        this.damage = damage;
        this.target = target;
        this.type = type;
        this.hit = false;
        this.startX = x;
        this.startY = y;
        
        // RIMOSSO IL +18: e.getX() è già il centro perfetto!
        this.facingRight = target.getX() >= x;

        // RIMOSSI I +18 per calcolare la distanza iniziale corretta
        double initialDist = Math.hypot(target.getX() - startX, target.getY() - startY);
        this.dynamicTotalDistance = initialDist;

        if (initialDist < 120) {
            this.currentSpeed = Math.max(1.5, projectileSpeed * (initialDist / 120.0));
        } else {
            this.currentSpeed = projectileSpeed;
        }
    }


    public boolean isFacingRight() { 
        return facingRight; 
    }

    // RIMUOSSI COMPLETAMENTE getAngle() E lastCalculatedAngle

    public double getDistanceTraveled() { 
        return actualDistanceTraveled; 
    }

    public double getTotalDistanceToTravel() {
        return dynamicTotalDistance;   
    }

    public void move(List<Enemy> allEnemies) {
        if (target == null || target.isDead() || hit) {
            hit = true;
            return;
        }

        double targetX = target.getX();
        double targetY = target.getY();

        double distanceX = targetX - this.x;
        double distanceY = targetY - this.y;
        double distanceFromCenter = Math.hypot(distanceX, distanceY);

        // IL FIX È QUI: Aumentato il raggio di collisione da 3.0 a 15.0
        // Ora la freccia registra l'impatto appena tocca il bordo esterno del nemico,
        // evitando di trapassarlo visivamente.
        if (distanceFromCenter <= 15.0 || distanceFromCenter < this.currentSpeed) {
            this.hit = true;
            processHit(allEnemies);
        } else {
            this.x += (distanceX / distanceFromCenter) * this.currentSpeed;
            this.y += (distanceY / distanceFromCenter) * this.currentSpeed;
            
            this.actualDistanceTraveled += this.currentSpeed;
            this.dynamicTotalDistance = this.actualDistanceTraveled + distanceFromCenter;
        }
    }

    private void processHit(List<Enemy> allEnemies) {
        if (this.type == CANNON_PROJECTILE) {
            double explosionRadius = 45.0;
            for (Enemy enemy : allEnemies) {
                if (!enemy.isDead()) {
                    double dist = Math.hypot(enemy.getX() - target.getX(), enemy.getY() - target.getY());
                    if (dist <= explosionRadius) {
                        enemy.takeDamage(this.damage);
                    }
                }
            }
        } else {
            target.takeDamage(this.damage); 
        }
    }

    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public boolean hasHit() { return this.hit; }
    public int getType() { return this.type; }
}