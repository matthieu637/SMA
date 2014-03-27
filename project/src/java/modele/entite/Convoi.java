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

	private List<Vehicule> morts;

	/**
	 * Pointeurs uniquement pour ne pas avoir à le mettre dans chaque paramètre
	 * de méthode
	 */
	private int[][] hauteur;
	private AllPercepts interpreteur;
	private Location but, but2, but3;
	private Object lock = new Object();
	private Random generateur = new Random();

	public Convoi(int nbAgent, Location location, AllPercepts interpreteur, Location but, int[][] hauteur) {
		morts = new LinkedList<>();
		file = new LinkedList<Vehicule>();
		int a = 1;
		for (; a < nbAgent; a++) {
			Vehicule v = new Vehicule(a, new Location(a - 1, 0), false, a == 1 ? null : file.get(a - 2));
			v.majPercept(interpreteur, hauteur, morts);
			file.add(v);
		}

		Vehicule leader = new Vehicule(nbAgent, location, true, file.get(file.size() - 1));
		leader.setBut(but);
		leader.majPercept(interpreteur, hauteur, morts);
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
		v.majPercept(interpreteur, hauteur, morts);
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

	public boolean remove(int agent, boolean killed) {
		Vehicule mort = getVehicule(agent);
		Vehicule devant = getDevant(mort);

		// but intermediaire atteint
		if (!killed && mort.estLeader() && !mort.getBut().equals(but)) {
			mort.setBut(but);
			mort.majPercept(interpreteur, hauteur, morts);
			return false;
		}

		if (mort.getFollower() != null && devant != null) {
			devant.setFollower(mort.getFollower());
			devant.majPercept(interpreteur, hauteur, morts);
		}

		if (mort.estLeader() && mort.getFollower() != null) {
			Vehicule nouveau_leader = mort.getFollower();
			nouveau_leader.setLeader();
			nouveau_leader.setBut(mort.getBut());
			nouveau_leader.majPercept(interpreteur, hauteur, morts);
		}

		if (killed) {
			morts.add(mort);
			for (Vehicule leader : getLeaders())
				leader.majPercept(interpreteur, hauteur, morts);
		}

		synchronized (lock) {
			file.remove(mort);
		}

		return true;
	}

	public boolean scinder(int agent) {
		synchronized (lock) {
			Vehicule nouveau_leader = getVehicule(agent);
			// retire en tant que follower
			Vehicule devant = getDevant(nouveau_leader);
			if (devant != null)
				devant.setFollower(null);
			Vehicule ancien_leader = getLeaders().get(0);
			nouveau_leader.setLeader();
			definirButIntermediaire(nouveau_leader.getLocation(), but, ancien_leader.getLocation());
			nouveau_leader.setBut(but2);
			ancien_leader.setBut(but3);

			if (devant != null)
				devant.majPercept(interpreteur, hauteur, morts);
			nouveau_leader.majPercept(interpreteur, hauteur, morts);
			return true;
		}
	}

	private void definirButIntermediaire(Location l1, Location but, Location leader) {
		double dist = l1.distanceEuclidean(but);
		double dist2 = leader.distanceEuclidean(but);
		int perturbation = (int) (dist / Variables.FACTEUR_COURBURE_TRAJECTOIRE);
		int perturbation2 = (int) (dist2 / Variables.FACTEUR_COURBURE_TRAJECTOIRE);
		
		float da = l1.y - but.y; 
		float db = but.x - l1.x;
		
		float dc = da * l1.x + db * l1.y;
		float dctest = da * leader.x + db * leader.y;
		
		if(dc < dctest){
			but2 = new Location(but.x - perturbation, l1.y + perturbation);
			but3 = new Location(leader.x + perturbation2, but.y - perturbation2);
		}
		else {
			
			but2 = new Location(l1.x + perturbation, but.y - perturbation);
			but3 = new Location(but.x - perturbation2, leader.y + perturbation2);
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
