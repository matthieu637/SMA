package modele;

import jason.environment.grid.Location;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Représente un adversaire
 */
public class Adversaire {

	/**
	 * Il connait sa position dans la grille
	 */
	private Location l;
	/**
	 * Certain adversaire ne sont là que pour "monter la garde" et ne se déplace
	 * pas
	 */
	private boolean virulent;

	public Adversaire(int x, int y, boolean virulent) {
		l = new Location(x, y);
		this.virulent = virulent;
	}

	public Location getLocation() {
		return l;
	}

	public void setLocation(Location l) {
		this.l = l;
	}

	public boolean virulent() {
		return virulent;
	}

}
