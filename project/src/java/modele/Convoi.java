package modele;

import jason.environment.grid.Location;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import modele.percepts.AllPercepts;

public class Convoi {

	private List<Vehicule> file;

	/**
	 * Pointeurs uniquement pour ne pas avoir à le mettre dans chaque paramètre
	 * de méthode
	 */
	private int[][] hauteur;
	private AllPercepts interpreteur;

	public Convoi(int nbAgent, Location location, AllPercepts interpreteur, Location but, int[][] hauteur) {

		file = new LinkedList<Vehicule>();
		int a = 1;
		for (; a < nbAgent; a++) {
			Vehicule v = new Vehicule(a, new Location(a - 1, 0), false, a == 1 ? null : file.get(a - 2));
			v.initPercept(interpreteur, hauteur);
			v.majPercept(interpreteur, hauteur);
			file.add(v);
		}

		Vehicule leader = new Vehicule(nbAgent, location, true, file.get(file.size() - 1));
		leader.setBut(but);
		leader.initPercept(interpreteur, hauteur);
		leader.majPercept(interpreteur, hauteur);
		file.add(leader);

		this.interpreteur = interpreteur;
		this.hauteur = hauteur;
	}

	public void majPercepts(int agent, Location l) {
		Vehicule v = getVehicule(agent);

		v.deplacer(l);
		v.majPercept(interpreteur, hauteur);
	}

	public Vehicule getVehicule(int agent) {
		int pos = Collections.binarySearch(file, agent);
		if (pos < 0)
			return null;
		return file.get(pos);
	}

	public boolean canAct(int agent) {
		if (getVehicule(agent) != null)
			return getVehicule(agent).canAct();
		return false;
	}

	public List<Vehicule> getLeaders() {
		List<Vehicule> leads = new LinkedList<>();
		for (Vehicule v : file)
			if (v.estLeader())
				leads.add(v);
		return leads;
	}

	public void remove(int agent) {
		Vehicule mort = getVehicule(agent);
		Vehicule devant = null;
		for (Vehicule candidat : file) {
			if (candidat.getFollower() != null && candidat.getFollower().equals(mort)) {
				devant = candidat;
				break;
			}
		}
		if (mort.getFollower() != null)
			devant.setFollower(mort.getFollower());
		devant.majPercept(interpreteur, hauteur);

		for (Vehicule leader : getLeaders())
			interpreteur.ajouterMort(leader.getNumero(), mort.getNumero());
		synchronized (file) {
			file.remove(mort);
		}
	}
}
