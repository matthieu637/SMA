package modele.entite;

import jason.environment.grid.Location;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import modele.Variables;
import modele.percepts.AllPercepts;

public class Convoi {

	private List<Vehicule> file;

	/**
	 * Pointeurs uniquement pour ne pas avoir à le mettre dans chaque paramètre
	 * de méthode
	 */
	private int[][] hauteur;
	private AllPercepts interpreteur;
	private Location but;
	private Object lock = new Object();
	private Random generateur = new Random();

	public Convoi(int nbAgent, Location location, AllPercepts interpreteur, Location but, int[][] hauteur) {

		file = new LinkedList<Vehicule>();
		int a = 1;
		for (; a < nbAgent; a++) {
			Vehicule v = new Vehicule(a, new Location(a - 1, 0), false, a == 1 ? null : file.get(a - 2));
			v.majPercept(interpreteur, hauteur);
			file.add(v);
		}

		Vehicule leader = new Vehicule(nbAgent, location, true, file.get(file.size() - 1));
		leader.setBut(but);
		leader.majPercept(interpreteur, hauteur);
		file.add(leader);

		for (Vehicule v : file)
			interpreteur.ajouterComportement(v.getNumero(), Variables.DIRECTION_IMPORTANCE_HAUTEUR + generateur.nextDouble()
					* Variables.DIRECTION_IMPORTANCE_VARIATION * 2. - Variables.DIRECTION_IMPORTANCE_VARIATION);

		this.interpreteur = interpreteur;
		this.hauteur = hauteur;
		this.but = but;
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

	public void remove(int agent, boolean killed) {
		Vehicule mort = getVehicule(agent);
		Vehicule devant = getDevant(mort);

		if (mort.getFollower() != null && devant != null) {
			devant.setFollower(mort.getFollower());
			devant.majPercept(interpreteur, hauteur);
		}

		if (mort.estLeader() && mort.getFollower() != null) {
			Vehicule nouveau_leader = mort.getFollower();
			nouveau_leader.setLeader();
			nouveau_leader.setBut(but);
			nouveau_leader.majPercept(interpreteur, hauteur);
		}

		if (killed)
			for (Vehicule leader : getLeaders())
				interpreteur.ajouterMort(leader.getNumero(), mort.getNumero());

		synchronized (lock) {
			file.remove(mort);
		}
	}

	public boolean scinder(int agent) {
		synchronized (lock) {
			Vehicule nouveau_leader = getVehicule(agent);
			// retire en tant que follower
			Vehicule devant = getDevant(nouveau_leader);
			if (devant != null)
				devant.setFollower(null);
			nouveau_leader.setLeader();
			nouveau_leader.setBut(but);

			if (devant != null)
				devant.majPercept(interpreteur, hauteur);
			nouveau_leader.majPercept(interpreteur, hauteur);
			return true;
		}
	}

	public Vehicule getDevant(Vehicule v) {
		Vehicule devant = null;
		for (Vehicule candidat : file) {
			if (candidat.getFollower() != null && candidat.getFollower().equals(v)) {
				devant = candidat;
				break;
			}
		}

		return devant;
	}

	public boolean arriver() {
		return file.size() == 0;
	}
}
