package gioco.model;

public class TowerSlot {
	
	private int x, y;
	private int width, height;
	private Tower tower;
	
	public TowerSlot(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tower = null;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public boolean isOccupied() {
		return tower != null;
	}
	
	public Tower getTower() {
		return tower;
	}
	
	public void setTower(Tower tower) {
		this.tower = tower;
	}
}
