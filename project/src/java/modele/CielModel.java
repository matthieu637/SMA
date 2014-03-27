package modele;

import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modele.entite.Convoi;
import modele.entite.Drone;
import modele.entite.EntiteComportement;
import modele.percepts.AllPercepts;
import utils.Couple;
import ext.GridWorldModelP;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Contient les informations du modèle pour l'environnement des drônes
 *         en basse altitude
 */
public class CielModel extends Grille {

	private List<Drone> drones;

	/*
	 * Pointeurs pour récupérer les adversaires et civils
	 */
	private List<EntiteComportement> agentsSupplementaires;
	/**
	 * Pointeur pour récupérer les vehicules du convoi
	 */
	private Convoi convoi;

	private AllPercepts interpreteur;

	private Map<Location, List<Integer>> zones;

	protected CielModel(int nbAgent, AllPercepts interpreteur, List<EntiteComportement> agentsSupplementaires, Convoi convoi) {
		super(Variables.TAILLE_CARTE_X, Variables.TAILLE_CARTE_Y, nbAgent, interpreteur, false);
		this.agentsSupplementaires = agentsSupplementaires;
		this.convoi = convoi;

		drones = new ArrayList<Drone>(nbAgent);

		for (int i = 0; i < nbAgent; i++)
			drones.add(new Drone(i + 1, true, getAgPos(i), Variables.CAPACITE_FUEL_DRONE, Variables.CHAMP_VISION_DRONE_BASSE_ALTITUDE,
					Variables.CHAMP_VISION_DRONE_HAUTE_ALTITUDE));
		this.interpreteur = interpreteur;

		zones = new HashMap<Location, List<Integer>>();
		for (int i = 0; i < Variables.TAILLE_CARTE_X; i++)
			for (int j = 0; j < Variables.TAILLE_CARTE_Y; j++)
				zones.put(new Location(i, j), new ArrayList<Integer>(nbAgent));

		this.majPercepts();
	}

	public void majPercepts() {
		int nbDrones = drones.size();
		int d = 1;
		for (; d <= nbDrones; d++) {
			getDrone(d).majPercepts(interpreteur, agentsSupplementaires, convoi);
		}
	}

	@Override
	public Couple<Location, Boolean> deplacerCollision(int agent, int direction) {

		Couple<Location, Boolean> c = super.deplacerCollision(agent - 1, direction);

		if (c.second) {
			Drone d = getDrone(agent);
			boolean outOfFuel = d.deplacer(c.first);
			d.majPercepts(interpreteur, agentsSupplementaires, convoi);

			if (outOfFuel) {
				interpreteur.killDrone(this.getAgAtPos(c.first));
				this.remove(GridWorldModelP.AGENT, c.first);
			}

			updateZone(d);
			// view.update();
		}
		return c;
	}

	// private Object lock = new Object();

	private void updateZone(Drone d) {
		// synchronized (lock) {

		int champ = d.isHaute_altitude() ? d.getChamp_vision_haute_altitude() : d.getChamp_vision_basse_altitude();

		int xstart = d.getPos().x - champ - 4;
		int xfin = d.getPos().x + champ + 4;

		int ystart = d.getPos().y - champ - 4;
		int yfin = d.getPos().y + champ + 4;

		for (int i = xstart; i <= xfin; i++)
			for (int j = ystart; j <= yfin; j++) {
				Location candidat = new Location(i, j);
				if (inGrid(i, j)) {
					List<Integer> actives = zones.get(candidat);
					boolean changed = false;
					if (d.getPos().distanceEuclidean(candidat) <= champ) {
						if (!actives.contains(d.getId())) {
							actives.add(d.getId());
							changed = true;
						}
					} else {
						if (actives.contains(d.getId())) {
							actives.remove((Integer) d.getId());
							changed = true;
						}
					}
					if (changed)
						view.update(i, j);
				}
			}
		// }
	}

	public Integer getZone(int x, int y) {
		Location cle = new Location(x, y);
		return zones.get(cle).size();
	}

	public Drone getDrone(int id) {
		return drones.get(id - 1);
	}

	public boolean droneTermine() {
		for (Drone d : drones)
			if (!d.isAuSol())
				return false;
		return true;
	}

	public List<Drone> getDrones() {
		return drones;
	}
}

class ColorPoint {
	private Location l;
	private boolean c;

	public ColorPoint(Location l, boolean c) {
		super();
		this.l = l;
		this.c = c;
	}

	public Location getL() {
		return l;
	}

	public void setL(Location l) {
		this.l = l;
	}

	public boolean getC() {
		return c;
	}

	public void setC(boolean c) {
		this.c = c;
	}

}
