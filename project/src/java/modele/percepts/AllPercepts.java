package modele.percepts;

import jason.environment.Environment;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Regroupe toutes les interactions possibles avec l'environnement pour
 *         une meilleur organisation et retrouver facilement les différents
 *         percept.
 */
public class AllPercepts extends InterpreteurSpl {

	private final static String position = "location(%s, %s, %s)";
	private final static String leader = "leader(%s)";
	private final static String goal = "goal(%s, %s)";
	private final static String heightmap = "heightmap(%s, %s, %s, %s)";
	private final static String follow = "follow(%s)";
	
	//id x y time
	private final static String vehicule = "vehicule(%s, %s, %s, %s)";
	private final static String militaire = "militaire(%s, %s, %s, %s)";
	private final static String civil = "civil(%s, %s, %s, %s)";
	
	//pour tour
	private final static String allie = "allie(%s)";
	
	private final static String fuel = "fuel(%s)";
	private final static String droneAuSol = "droneAuSol(%s)";
	private final static String drone = "drone(%s)";
	private final static String fieldOfView = "fieldOfView(%s)";
	private final static String altitude = "altitude(%s)";
	private final static String dead = "dead(%s)";

	public AllPercepts(Environment env) {
		super(env);
	}

	public void ajouterMort(int leader, int vehicule) {
		ajouterVehicule(leader, dead, "v" + vehicule);
	}

	public void ajouterPositionVehicule(int vehicule, int cible, int x, int y) {
		ajouterVehicule(vehicule, position, "v" + cible, x, y);
	}

	public void ajouterLeader(int numero) {
		ajouterVehicule(numero, leader, "v" + numero);
	}

	public void ajouterPositionButVehicule(int leader, int x, int y) {
		ajouterVehicule(leader, goal, x, y);
	}

	public void ajouterHeightmap(int leader, int i, int j, int k, int l) {
		ajouterVehicule(leader, heightmap, i, j, k, l);
	}

	public void retirerPositionVehicule(int agent) {
		retirerVehiculeUnif(agent, position, "_", "_", "_");
	}

	public void retirerPositionVehicule(int agent, int cible) {
		retirerVehiculeUnif(agent, position, "v" + cible, "_", "_");
	}

	public void retirerHeightmap(int numero) {
		retirerVehiculeUnif(numero, heightmap, "_", "_", "_", "_");
	}

	public void ajouterFollow(int numero, int agent) {
		ajouterVehicule(numero, follow, "v" + agent);
	}

	public void retirerFollow(int follower) {
		retirerVehiculeUnif(follower, follow, "_", "_");
	}

	public void ajouterPositionDrone(int d, int cible, int x, int y) {
		ajouterDrone(d, position, "d" + cible, x, y);
	}

	public void retirerPositionDrone(int agent) {
		retirerDroneUnif(agent, position, "_", "_", "_");
	}

	public void ajouterDroneVoitVehicule(int d, int id, int x, int y, long time) {
		ajouterDrone(d, vehicule, id, x, y, time);
	}

	public void ajouterDroneVoitMilitaire(int d, int id, int x, int y, long time) {
		ajouterDrone(d, militaire, id, x, y, time);
	}

	public void ajouterDroneVoitCivil(int d, int id, int x, int y, long time) {
		ajouterDrone(d, civil, id, x, y, time);
	}

	public void ajouterFieldOfView(int d, int fov) {
		ajouterDrone(d, fieldOfView, fov);
	}

	public void retirerFieldOfView(int d) {
		retirerDroneUnif(d, fieldOfView, "_");
	}

	public void retirerVisionDrone(int d) {
		retirerDroneUnif(d, vehicule, "_", "_", "_", "_");
		retirerDroneUnif(d, militaire, "_", "_", "_", "_");
		retirerDroneUnif(d, civil, "_", "_", "_", "_");
	}

	public void ajouterDroneFuel(int d, int f) {
		ajouterDrone(d, fuel, f);
	}

	public void ajouterAltitude(int d, int f) {
		ajouterDrone(d, altitude, f);
	}

	public void retirerAltitude(int d) {
		retirerDroneUnif(d, altitude, "_");
	}

	public void retirerDroneFuel(int d) {
		retirerDroneUnif(d, fuel, "_");
	}

	public void ajouterTourDroneAuSol(int id) {
		ajouterTour(droneAuSol, "d" + id);
	}

	public void ajouterDrones(int nbDrones, int nbV) {
		for (int d = 0; d < nbDrones; d++) {
			for (int v = 1; v <= nbV; v++) {
				ajouterVehicule(v, drone, "d" + (d + 1));
			}
			for (int d2 = 0; d2 <= nbDrones; d2++) {
				ajouterDrone(d2 + 1, drone, "d" + (d + 1));
			}
		}
		for (int d = 0; d < nbDrones; d++) {
			ajouterTour(drone, "d" + (d + 1));
		}
	}

	public void retirerTourDroneAuSol(int id) {
		retirerTour(droneAuSol, "d" + id);
	}

	public void ajouterTourAllie(int id) {
		ajouterTour(allie, id);
	}

	public void retirerTourAllies() {
		retirerTourUnif(allie, "_");
	}

	public void retirerLeader(int numero) {
		retirerVehiculeUnif(numero, leader, "_");
	}

	public void retirerPositionButVehicule(int leader) {
		retirerVehiculeUnif(leader, goal, "_", "_");
	}

	public void retirerLeaderDrone(int id) {
		retirerDroneUnif(id, leader, "_");
	}

	public void ajouterLeaderDrone(int id, int numero) {
		ajouterDrone(id, leader, "v" + numero);
	}

}
