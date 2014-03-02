package modele;

import jason.environment.grid.Location;

import java.util.LinkedList;
import java.util.List;

import modele.percepts.AllPercepts;

public class Convoi {

	private List<Vehicule> leaders;
	private List<Integer> file;

	/**
	 * Pointeurs uniquement pour ne pas avoir à le mettre dans chaque paramètre
	 * de méthode
	 */
	private int[][] hauteur;
	private AllPercepts interpreteur;

	public Convoi(int nbAgent, Location location, AllPercepts interpreteur, Location but, int[][] hauteur) {
		Vehicule leader = new Vehicule(nbAgent, location);
		leader.initLeader(interpreteur, but, hauteur);

		leaders = new LinkedList<Vehicule>();
		leaders.add(leader);

		file = new LinkedList<Integer>();
		int a = 1;
		for (; a < nbAgent; a++) {
			file.add(a);
			interpreteur.ajouterPositionVehicule(a, a, 0);
			interpreteur.ajouterFollow(a, a + 1, 0);
		}
		file.add(a);

		this.interpreteur = interpreteur;
		this.hauteur = hauteur;
	}

	public void majPercepts(int agent, Location l) {
		boolean isLeader = false;
		for (Vehicule leader : leaders)
			if (leader.equals(agent)) {
				leader.deplacer(interpreteur, l, hauteur);
				isLeader = true;
				break;
			}

		if (!isLeader) {// si je ne suis pas un leader je met également à jour
						// ma position
			interpreteur.retirerPositionVehicule(agent);
			interpreteur.ajouterPositionVehicule(agent, l.x, l.y);
		}

		// je met à jour le but de mon follower
		int pos = file.indexOf(agent);
		if (pos != 0) {
			int follower = file.get(pos - 1);
			interpreteur.retirerFollow(follower);
			interpreteur.ajouterFollow(follower, l.x, l.y);
		}
	}
}
