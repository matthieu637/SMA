package utils;

import jason.environment.grid.Location;

public class DLocation {

	private Location l;

	private int direction;

	public DLocation(int x, int y, int d) {
		l = new Location(x, y);
		this.direction = d;
	}
	
	public Location getLocation(){
		return l;
	}
	
	public int getDirection(){
		return direction;
	}
}
