package modele;

import jason.environment.grid.Location;
import java.lang.Math;

/**
 * @author Sébastien Forestier <sforesti@clipper.ens.fr>
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
	
	private double vision; // portee de la vision
	private double portee; // portee de tir
	private double imprecision; // tir imprécis: taille du bruit gaussian : si imprecision = 1 -> un tir à 5 cases est dévié en moyenne de 5 cases en x et y

	public Adversaire(int x, int y, boolean virulent) {
		l = new Location(x, y);
		this.virulent = virulent;
		this.vision = 10.;
		this.portee = 5.;
		this.imprecision = 0.3;
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
		
	public double portee() {
		return this.portee;
	}
	
	public double vision() {
		return this.vision;
	}
	
	public double imprecision() {
		return this.imprecision;
	}
	
	/**
	* tire un coup et renvoie le lieu touché
	*/
	public Location tir(Location t, double rdx, double rdy) {
		
		double distance = t.distanceEuclidean(this.l);
		int tirx = (int) (t.x + Math.round(distance * this.imprecision * rdx));
		int tiry = (int) (t.y + Math.round(distance * this.imprecision * rdy));
		return new Location(tirx, tiry); 		
	}

}
