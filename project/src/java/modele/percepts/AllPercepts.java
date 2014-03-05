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
	private final static String vehicule = "vehicule(%s, %s)";
	private final static String adversaire = "adversaire(%s, %s)";
	private final static String civil = "civil(%s, %s)";
	private final static String fuel = "fuel(%s)";

	public AllPercepts(Environment env) {
		super(env);
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

	public void ajouterPositionDrone(int drone, int cible, int x, int y) {
		ajouterDrone(drone, position, cible, x, y);
	}

	public void retirerPositionDrone(int agent) {
		retirerDroneUnif(agent, position, "_", "_", "_");
	}

	public void ajouterDroneVoitVehicule(int drone, int x, int y) {
		ajouterDrone(drone, vehicule, x, y);
	}

	public void ajouterDroneVoitAdversaire(int drone, int x, int y) {
		ajouterDrone(drone, adversaire, x, y);
	}

	public void ajouterDroneVoitCivil(int drone, int x, int y) {
		ajouterDrone(drone, civil, x, y);
	}

	public void retirerVisionDrone(int drone) {
		retirerDroneUnif(drone, vehicule, "_", "_");
		retirerDroneUnif(drone, adversaire, "_", "_");
		retirerDroneUnif(drone, civil, "_", "_");
	}

	public void ajouterDroneFuel(int drone, int f) {
		ajouterDrone(drone, fuel, f);
	}

	public void retirerDroneFuel(int drone) {
		retirerDroneUnif(drone, fuel, "_");
	}

}
