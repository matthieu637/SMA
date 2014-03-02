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

	private final static String position = "location(%d, %d)";
	private final static String leader = "leader(%s)";
	private final static String goal = "goal(%d, %d)";
	private final static String heightmap = "heightmap(%d, %d, %d, %d)";

	public AllPercepts(Environment env) {
		super(env);
	}

	public void ajouterPositionVehicule(int vehicule, int x, int y) {
		ajouterVehicule(vehicule, position, x, y);
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

	public void retirerPositionVehicule(int leader, int x, int y) {
		retirerVehicule(leader, position, x, y);
	}

	public void retirerHeightmap(int numero, int i, int j, int k, int l) {
		retirerVehicule(numero, heightmap, i, j, k, l);
	}
}
