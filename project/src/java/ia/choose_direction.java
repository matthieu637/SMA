package ia;

import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;
import jason.environment.grid.Location;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import modele.Variables;
import utils.DLocation;
import utils.UtilList;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Internal Action.
 */
public class choose_direction extends DefaultInternalAction {

	private static final long serialVersionUID = 6650935652929594415L;

	private final Random generateur = new Random();

	private static final Object lock = new Object();
	
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		synchronized (lock) {


		if (args.length == 5 || args.length == 9) {
			int i = 1;
			int x = Integer.parseInt(args[i++].toString());
			int y = Integer.parseInt(args[i++].toString());
			int gx = Integer.parseInt(args[i++].toString());
			int gy = Integer.parseInt(args[i++].toString());

			List<DLocation> loc = new LinkedList<>();
			if (x + 1 < Variables.TAILLE_CARTE_X)
				loc.add(new DLocation(x + 1, y, 2));
			if (y + 1 < Variables.TAILLE_CARTE_Y)
				loc.add(new DLocation(x, y + 1, 3));
			if (x - 1 >= 0)
				loc.add(new DLocation(x - 1, y, 0));
			if (y - 1 >= 0)
				loc.add(new DLocation(x, y - 1, 1));
//			loc.add(new DLocation(x, y, -1));

			Location goal = new Location(gx, gy);
			List<DLocation> mins = null;

			if (args.length == 9) {
				int hg = Integer.parseInt(args[i++].toString());
				int hh = Integer.parseInt(args[i++].toString());
				int hd = Integer.parseInt(args[i++].toString());
				int hb = Integer.parseInt(args[i++].toString());

				mins = UtilList.minList(loc, new DistanceHeightmap(goal, hg, hh, hd, hb));
			} else
				mins = UtilList.minList(loc, new Distance(goal));

			int indice = generateur.nextInt(mins.size());
			return un.unifies(args[0], new NumberTermImpl(mins.get(indice).getDirection()));
		} else
			throw new JasonException("5 ou 9 arguments nécessaires");
		}
	}
}

class Distance implements Comparator<DLocation> {

	private Location goal;

	Distance(Location goal) {
		this.goal = goal;
	}

	@Override
	public int compare(DLocation o1, DLocation o2) {
		double d1 = o1.getLocation().distanceEuclidean(goal);
		double d2 = o2.getLocation().distanceEuclidean(goal);
		return Double.compare(d1, d2);
	}
}

class DistanceHeightmap implements Comparator<DLocation> {

	private Location goal;
	private int hg, hh, hd, hb;
	private static double distMax = new Location(0, 0).distanceEuclidean(new Location(Variables.TAILLE_CARTE_X, Variables.TAILLE_CARTE_Y));

	DistanceHeightmap(Location goal, int hg, int hh, int hd, int hb) {
		this.goal = goal;
		this.hg = hg;
		this.hh = hh;
		this.hd = hd;
		this.hb = hb;
	}

	@Override
	public int compare(DLocation o1, DLocation o2) {
		double epsilon = Variables.DIRECTION_IMPORTANCE_HAUTEUR;

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
