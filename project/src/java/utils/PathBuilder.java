package utils;

import jason.environment.grid.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import modele.Variables;

public class PathBuilder {

	private final static Random generateur = new Random();

	private List<Location> path;

	public PathBuilder(Location start, Location goal) {
		path = new LinkedList<>();

		while (!start.equals(goal)) {
			path.add(start);
			start = best(start.x, start.y, goal.x, goal.y).getLocation();
		}
	}
	
	public List<Location> getPath(){
		return path;
	}

	private DLocation best(int x, int y, int gx, int gy) {
		List<DLocation> loc = new LinkedList<>();
		if (x + 1 < Variables.TAILLE_CARTE_X)
			loc.add(new DLocation(x + 1, y, 2));
		if (y + 1 < Variables.TAILLE_CARTE_Y)
			loc.add(new DLocation(x, y + 1, 3));
		if (x - 1 >= 0)
			loc.add(new DLocation(x - 1, y, 0));
		if (y - 1 >= 0)
			loc.add(new DLocation(x, y - 1, 1));

		Location goal = new Location(gx, gy);
		List<DLocation> mins = UtilList.minList(loc, new Distance(goal));
		int indice = generateur.nextInt(mins.size());
		return mins.get(indice);
	}
}
