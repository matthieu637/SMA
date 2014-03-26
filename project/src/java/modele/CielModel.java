package modele;

import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.List;

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

	protected CielModel(int nbAgent, AllPercepts interpreteur, List<EntiteComportement> agentsSupplementaires, Convoi convoi) {
		super(Variables.TAILLE_CARTE_X, Variables.TAILLE_CARTE_Y, nbAgent, interpreteur, false);
		this.agentsSupplementaires = agentsSupplementaires;
		this.convoi = convoi;
		
		drones = new ArrayList<Drone>(nbAgent);
		int i = 0;
		for (; i < nbAgent; i++)
			drones.add(new Drone(i + 1, true, getAgPos(i), Variables.CAPACITE_FUEL_DRONE, Variables.CHAMP_VISION_DRONE_BASSE_ALTITUDE,
					Variables.CHAMP_VISION_DRONE_HAUTE_ALTITUDE));
		this.interpreteur = interpreteur;

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
			boolean outOfFuel = getDrone(agent).deplacer(c.first);
			getDrone(agent).majPercepts(interpreteur, agentsSupplementaires, convoi);
			
			if (outOfFuel) {
				interpreteur.killDrone(this.getAgAtPos(c.first));
				this.remove(GridWorldModelP.AGENT, c.first);
			}
		}

		return c;
	}

	public Drone getDrone(int id) {
		return drones.get(id - 1);
	}

	public boolean droneTermine() {
		for(Drone d : drones)
			if(!d.isAuSol())
				return false;
		return true;
	}
}
