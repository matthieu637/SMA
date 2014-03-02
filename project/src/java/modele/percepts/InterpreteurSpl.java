package modele.percepts;

import jason.environment.Environment;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Interface entre AllPercepts et Interpreteur, ne contient pas
 *         directement les percepts et n'interagit pas directement avec
 *         l'environnement. Doit contenir tout les String.format
 */
public abstract class InterpreteurSpl extends Interpreteur {

	protected InterpreteurSpl(Environment env) {
		super(env);
	}

	protected void ajouterVehicule(int numero, String percept, Object... args) {
		ajouter("v" + numero, String.format(percept, args));
	}

	protected void retirerVehicule(int numero, String percept, Object... args) {
		retirer("v" + numero, String.format(percept, args));
	}

	protected void ajouterDrone(int numero, String percept, Object... args) {
		ajouter("d" + numero, String.format(percept, args));
	}

	protected void retirerDrone(int numero, String percept, Object... args) {
		retirer("d" + numero, String.format(percept, args));
	}

	protected void retirerVehiculeUnif(int numero, String percept, Object... args) {
		retirerUnif("v" + numero, String.format(percept, args));
	}
	
	protected void retirerDroneUnif(int numero, String percept, Object... args) {
		retirerUnif("d" + numero, String.format(percept, args));
	}

	/**
	 * Tue un véhicule du système
	 * 
	 */
	public void killVehicule(int vehicule) {
		kill("v" + (vehicule + 1));
	}
}
