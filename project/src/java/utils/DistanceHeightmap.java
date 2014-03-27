package utils;

import jason.environment.grid.Location;

import java.util.Comparator;

import modele.Variables;

public class DistanceHeightmap implements Comparator<DLocation> {

	private Location goal;
	private int hg, hh, hd, hb;
	private static double distMax = new Location(0, 0).distanceEuclidean(new Location(Variables.TAILLE_CARTE_X, Variables.TAILLE_CARTE_Y));
	private float comp;

	public DistanceHeightmap(Location goal, int hg, int hh, int hd, int hb, float comp) {
		this.goal = goal;
		this.hg = hg;
		this.hh = hh;
		this.hd = hd;
		this.hb = hb;
		this.comp = comp;
	}

	@Override
	public int compare(DLocation o1, DLocation o2) {
		double epsilon = comp;

		double h1 = getHeightmap(o1) / 255f;
		double h2 = getHeightmap(o2) / 255f;

		double d1 = o1.getLocation().distanceEuclidean(goal) / distMax;
		double d2 = o2.getLocation().distanceEuclidean(goal) / distMax;

		double c1 = (1f - epsilon) * d1 + epsilon * h1;
		double c2 = (1f - epsilon) * d2 + epsilon * h2;

		return Double.compare(c1, c2);
	}

	private int getHeightmap(DLocation l) {
		switch (l.getDirection()) {
		case 0:
			return hg;
		case 1:
			return hh;
		case 2:
			return hd;
		case 3:
			return hb;
		default:
			return 256;
		}
	}
}