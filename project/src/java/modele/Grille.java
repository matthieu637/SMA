package modele;

import jason.environment.grid.Location;
import modele.percepts.AllPercepts;
import utils.Couple;
import ext.GridWorldModelP;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Modèle d'une grille où les agents peuvent se déplacer. Les 3 grilles
 *         descendent de cette classe.
 */
public abstract class Grille extends GridWorldModelP {

	public static final int ADVERSAIRE_CODE = 8;
	public static final int CIVIL_CODE = 16;
	public static final int ALLIE_CODE = 32;

	protected AllPercepts interpreteur;

	protected Grille(int w, int h, int nbAgs, AllPercepts interpreteur) {
		super(w, h, nbAgs);

		for (int a = 0; a < nbAgs; a++)
			setAgPos(a, a, 0);

		this.interpreteur = interpreteur;
	}

	/**
	 * Deplace un agent dans une direction sur la grille
	 * 
	 * @param agent
	 *            index de l'agent
	 * @param direction
	 */
	public Location deplacer(int agent, int direction) {
		Location l = getAgPos(agent);

		if (l == null)
			return null;

		l = deplacer(l, direction);

		setAgPos(agent, l);

		return l;
	}

	/**
	 * Deplace un agent dans une direction sur la grille
	 * 
	 * @param agent
	 *            index de l'agent
	 * @param direction
	 */
	public Couple<Location, Boolean> deplacerCollision(int agent, int direction) {
		Location l = getAgPos(agent);
		Location nl;

		if (l == null)
			return null;

		nl = deplacer(l, direction);

		if (getAgAtPos(nl) == -1) {
			setAgPos(agent, nl);
			return new Couple<Location, Boolean>(nl, true);
		} else
			return new Couple<Location, Boolean>(l, false);
	}

	/**
	 * Applique une direction à une location
	 * 
	 * @param l
	 *            (x,y)
	 * @param direction
	 * @return l+direction
	 */
	public static Location deplacer(Location l, int direction) {
		switch (direction) {
		case 0:
			if (l.x - 1 >= 0)
				l.x--;
			break;
		case 1:
			if (l.y - 1 >= 0)
				l.y--;
			break;
		case 2:
			if (l.x + 1 < Variables.TAILLE_CARTE_X)
				l.x++;
			break;
		case 3:
			if (l.y + 1 < Variables.TAILLE_CARTE_Y)
				l.y++;
			break;

		default:
			break;
		}
		return l;
	}
}
