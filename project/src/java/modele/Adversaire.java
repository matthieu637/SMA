package modele;

import jason.environment.grid.Location;

public class Adversaire {

	private Location l;

	public Adversaire(int x, int y) {
		l = new Location(x, y);
	}

	public Location getLocation() {
		return l;
	}

	public void setLocation(Location l) {
		this.l = l;
	}

	
}
