package modele;

import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.List;

import modele.percepts.AllPercepts;
import utils.Couple;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Contient les informations du modèle pour l'environnement des drônes
 *         en basse altitude
 */
public class CielModel extends Grille {

	private List<Drone> drones;

	protected CielModel(int nbAgent, AllPercepts interpreteur) {
		super(Variables.TAILLE_CARTE_X, Variables.TAILLE_CARTE_Y, nbAgent, interpreteur);
		int nombre_drone_basse_altitude = (int) (nbAgent * Variables.PROPORTION_DRONE_BASSE_HAUTE_ALTITUDE);
		int nombre_drone_haute_altitude = nbAgent - nombre_drone_basse_altitude;

		drones = new ArrayList<Drone>(nbAgent);
		int i = 0;
		for (; i < nombre_drone_basse_altitude; i++)
			drones.add(new Drone(i + 1, false));
		for (; i < nombre_drone_haute_altitude + nombre_drone_basse_altitude; i++)
			drones.add(new Drone(i + 1, true));
	}

	@Override
	public Couple<Location, Boolean> deplacerCollision(int agent, int direction) {
		return super.deplacerCollision(agent - 1, direction);
	}

	public Drone getDrone(int id) {
		return drones.get(id);
	}
}
