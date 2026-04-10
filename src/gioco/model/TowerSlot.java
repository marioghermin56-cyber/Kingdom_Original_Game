package gioco.model;

public class TowerSlot {
	
	private int x;
	private int y;
	private Tower tower;
	
	public TowerSlot(int x, int y) {
		this.x = x;
		this.y = y;
		this.tower = null;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
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
