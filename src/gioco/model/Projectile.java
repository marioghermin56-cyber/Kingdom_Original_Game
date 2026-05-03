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
        
        this.facingRight = (target.getX() + 18) >= x;

        // Velocità adattiva
        double initialDist = Math.hypot((target.getX() + 18) - startX, (target.getY() + 18) - startY);
        if (initialDist < 120) {
            this.currentSpeed = Math.max(1.5, projectileSpeed * (initialDist / 120.0));
        } else {
            this.currentSpeed = projectileSpeed;
        }
    }

    public boolean isFacingRight() { return facingRight; }

    public double getAngle() {
        if (target != null) {
            double distanceX = (target.getX() + 18) - this.x;
            double distanceY = (target.getY() + 18) - this.y;
            if (Math.hypot(distanceX, distanceY) > 5.0) {
                lastCalculatedAngle = Math.atan2(distanceY, distanceX);
            }
        }
        return lastCalculatedAngle;
    }

    public double getDistanceTraveled() { 
        return Math.hypot(x - startX, y - startY); 
    }

    public double getTotalDistanceToTravel() {
        if (target != null && !target.isDead()) {
            return Math.hypot((target.getX() + 18) - startX, (target.getY() + 18) - startY);
        }
        return Math.max(1.0, Math.hypot(x - startX, y - startY));
    }

    public void move(List<Enemy> allEnemies) {
        if (target == null || target.isDead() || hit) {
            hit = true;
            return;
        }

        double distanceX = (target.getX() + 18) - this.x;
        double distanceY = (target.getY() + 18) - this.y;
        double distanceFromCenter = Math.hypot(distanceX, distanceY);

        // IL FIX E' QUI: Raggio ridotto a 3.0
        // Costringe la freccia ad arrivare al cuore matematico prima di sparire.
        if (distanceFromCenter <= 3.0 || distanceFromCenter < this.currentSpeed) {
            this.hit = true;
            processHit(allEnemies);
        } else {
            this.x += (distanceX / distanceFromCenter) * this.currentSpeed;
            this.y += (distanceY / distanceFromCenter) * this.currentSpeed;
        }
    }

    private void processHit(List<Enemy> allEnemies) {
        if (this.type == CANNON_PROJECTILE) {
            double explosionRadius = 45.0;
            for (Enemy enemy : allEnemies) {
                if (!enemy.isDead()) {
                    double dist = Math.hypot(enemy.getX() - target.getX(), enemy.getY() - target.getY());
                    if (dist <= explosionRadius) {
                        enemy.takeDamage(damage);
                    }
                }
            }
        } else {
            target.takeDamage(damage); 
        }
    }

    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public boolean hasHit() { return this.hit; }
    public int getType() { return this.type; }
}