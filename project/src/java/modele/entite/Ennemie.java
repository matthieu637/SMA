package modele.entite;

import jason.environment.grid.Location;

public class Ennemie extends Militaire {

	private double vision; // portee de la vision
	private double portee; // portee de tir
	private double imprecision; // tir imprécis: taille du bruit gaussian : si
								// imprecision = 1 -> un tir à 5 cases est dévié
								// en moyenne de 5 cases en x et y

	public Ennemie(Location l, Comportement c) {
		super(l, c);
		this.vision = 10.;
		this.portee = 5.;
		this.imprecision = 0.3;
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
