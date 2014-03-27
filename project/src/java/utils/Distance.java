package utils;

import jason.environment.grid.Location;

import java.util.Comparator;

public class Distance implements Comparator<DLocation> {

	private Location goal;

	public Distance(Location goal) {
		this.goal = goal;
	}

	@Override
	public int compare(DLocation o1, DLocation o2) {
		double d1 = o1.getLocation().distanceEuclidean(goal);
		double d2 = o2.getLocation().distanceEuclidean(goal);
		return Double.compare(d1, d2);
	}
}

