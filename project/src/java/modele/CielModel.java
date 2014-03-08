package modele;

import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.List;

import modele.percepts.AllPercepts;
import ext.GridWorldModelP;
import utils.Couple;

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
	private List<Adversaire> adversaire;	
	private List<Civil> civil;
	
	private AllPercepts interpreteur;

	protected CielModel(int nbAgent, AllPercepts interpreteur, List<Adversaire> adversaire, List<Civil> civil) {
		super(Variables.TAILLE_CARTE_X, Variables.TAILLE_CARTE_Y, nbAgent, interpreteur);
		int nombre_drone_basse_altitude = (int) (nbAgent * Variables.PROPORTION_DRONE_BASSE_HAUTE_ALTITUDE);
		int nombre_drone_haute_altitude = nbAgent - nombre_drone_basse_altitude;

		drones = new ArrayList<Drone>(nbAgent);
		int i = 0;
		for (; i < nombre_drone_basse_altitude; i++)
			drones.add(new Drone(i + 1, false, getAgPos(i), Variables.CAPACITE_FUEL_DRONE, Variables.CHAMP_VISION_DRONE_BASSE_ALTITUDE, Variables.CHAMP_VISION_DRONE_HAUTE_ALTITUDE));
		for (; i < nombre_drone_haute_altitude + nombre_drone_basse_altitude; i++)
			drones.add(new Drone(i + 1, true, getAgPos(i), Variables.CAPACITE_FUEL_DRONE, Variables.CHAMP_VISION_DRONE_BASSE_ALTITUDE, Variables.CHAMP_VISION_DRONE_HAUTE_ALTITUDE));
		
		this.interpreteur = interpreteur; 
		this.adversaire = adversaire;
		this.civil = civil;
		
		this.majPercepts();
		this.interpreteur.ajouterTourDrones(nbAgent);
	}
	
	public void majPercepts() {
		int nbDrones = drones.size();
		int d = 1;
		for (; d <= nbDrones ; d++) {
			getDrone(d).majPercepts(interpreteur, adversaire, civil);
		}
	}

	@Override
	public Couple<Location, Boolean> deplacerCollision(int agent, int direction) {
		
		Couple<Location, Boolean> c = super.deplacerCollision(agent - 1, direction);  
		if (c.second) {
			boolean outOfFuel = getDrone(agent).deplacer(c.first);
			if (outOfFuel) {
				
				interpreteur.killDrone(this.getAgAtPos(c.first));
				this.remove(GridWorldModelP.AGENT, c.first);
				
			}	
			else {		
				getDrone(agent).majPercepts(interpreteur, adversaire, civil);
			}
		}
		
		return c;
	}

	public Drone getDrone(int id) {
		return drones.get(id-1);
	}
}
