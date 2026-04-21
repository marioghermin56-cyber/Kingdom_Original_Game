package gioco.model;

import java.util.ArrayList;
import java.util.List;



public class EnemyPath {
	 private List<Point> waypoints;

	 public EnemyPath() {
	    this.waypoints = new ArrayList<>();
	 }

	 public void addPoint(double x, double y) {
	    waypoints.add(new Point((int)x, (int)y));
	 }

	 public List<Point> getWaypoints() {
	     return waypoints;
	 }
}