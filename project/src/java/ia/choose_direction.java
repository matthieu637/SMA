package ia;

import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;
import jason.environment.grid.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import modele.Variables;
import utils.DLocation;
import utils.Distance;
import utils.DistanceHeightmap;
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


		if (args.length == 5 || args.length == 6 || args.length == 10) {
			boolean random = false;
			if(args.length == 6 || args.length == 10)
				random = generateur.nextFloat() < Variables.PROBA_DEPLACEMENT_ALEATOIRE;
			
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
			
			if(random){
				int indice = generateur.nextInt(loc.size());
				return un.unifies(args[0], new NumberTermImpl(loc.get(indice).getDirection()));
			}

			Location goal = new Location(gx, gy);
			List<DLocation> mins = null;

			if (args.length == 10) {
				int hg = Integer.parseInt(args[i++].toString());
				int hh = Integer.parseInt(args[i++].toString());
				int hd = Integer.parseInt(args[i++].toString());
				int hb = Integer.parseInt(args[i++].toString());
				float comp = Float.parseFloat(args[i++].toString()); 

				mins = UtilList.minList(loc, new DistanceHeightmap(goal, hg, hh, hd, hb, comp));
			} else
				mins = UtilList.minList(loc, new Distance(goal));

			int indice = generateur.nextInt(mins.size());
			return un.unifies(args[0], new NumberTermImpl(mins.get(indice).getDirection()));
		} else
			throw new JasonException("5 ou 9 arguments nécessaires");
		}
	}
}
