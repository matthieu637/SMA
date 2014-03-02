package modele;

import jason.environment.grid.Location;
import java.lang.Math;

/**
 * @author Sébastien Forestier <sforesti@clipper.ens.fr>
 * 
 *         Représente un civil
 */
public class Civil {

	/**
	 * Il connait sa position dans la grille
	 */
	private Location l;
	private Location but;	
	
	public Civil(int x, int y, Location but) {
		this.l = new Location(x, y);
		this.setBut(but);
	}

	public Location getLocation() {
		return l;
	}
	
	public Location getBut() {
		return but;
	}
	
	public void setBut(Location but) {
		this.but = but;
	}

	public void setLocation(Location l) {
		this.l = l; 
	}		

}