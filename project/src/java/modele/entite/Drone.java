package modele.entite;

import jason.environment.grid.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import modele.percepts.AllPercepts;

public class Drone {

	private int id;
	private boolean haute_altitude;
	private int champ_vision_basse_altitude;
	private int champ_vision_haute_altitude;
	private int fuel;
	private int maxFuel;
	private boolean auSol;
	private Location l;
	private Random generateur;

	/**
	 * Les civils et militaire que j'ai déjà perçu dans le passé
	 */
	private Map<Integer, EntitePercu> entites;

	public Drone(int id, boolean haute_altitude, Location l, int maxFuel, int champ_vision_basse_altitude, int champ_vision_haute_altitude) {
		this.setId(id);
		this.setPos(l);
		this.setHaute_altitude(haute_altitude);
		generateur = new Random();
		this.maxFuel = maxFuel + generateur.nextInt(200);
		this.auSol = true;
		this.champ_vision_basse_altitude = champ_vision_basse_altitude;
		this.champ_vision_haute_altitude = champ_vision_haute_altitude;
		entites = new HashMap<>();
	}

	public boolean isHaute_altitude() {
		return haute_altitude;
	}

	public void setHaute_altitude(boolean haute_altitude) {
		this.haute_altitude = haute_altitude;
	}

	public int changerAltitude() {
		this.haute_altitude = !this.haute_altitude;
		return this.getAltitude();
	}

	public int getAltitude() {
		if (this.haute_altitude)
			return 1;
		else
			return 0;
	}

	public int getMaxFuel() {
		return this.maxFuel;
	}

	public void remplirFuel() {
		this.fuel = this.maxFuel;
	}

	public int getFuel() {
		return this.fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public int getChamp_vision_basse_altitude() {
		return this.champ_vision_basse_altitude;
	}

	public void setChamp_vision_basse_altitude(int champ_vision_basse_altitude) {
		this.champ_vision_basse_altitude = champ_vision_basse_altitude;
	}

	public int getChamp_vision_haute_altitude() {
		return this.champ_vision_haute_altitude;
	}

	public void setChamp_vision_haute_altitude(int champ_vision_haute_altitude) {
		this.champ_vision_haute_altitude = champ_vision_haute_altitude;
	}

	public Location getPos() {
		return this.l;
	}

	public void setPos(Location l) {
		this.l = l;
	}

	public void useFuel(int fuel) {
		this.fuel = this.fuel - fuel;
	}

	public boolean emptyFuel() {
		return this.fuel <= 0;
	}

	public boolean decoller() {
		if (this.auSol == true) {
			this.auSol = false;
			return true;
		} else
			return false;
	}

	public boolean atterir() {
		if (this.auSol == false) {
			this.auSol = true;
			return true;
		} else
			return false;
	}

	public boolean deplacer(Location nl) {
		// System.out.println((int)this.l.distanceEuclidean(nl));
		this.useFuel((int) this.l.distanceEuclidean(nl));
		this.setPos(nl);
		return this.emptyFuel();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void majPercepts(AllPercepts interpreteur, List<EntiteComportement> agentsSupplementaires, Convoi c) {

		// altitude
		interpreteur.retirerAltitude(id);
		interpreteur.ajouterAltitude(id, this.getAltitude());

		// champ de vision
		interpreteur.retirerFieldOfView(id);
		interpreteur.ajouterFieldOfView(id,
				this.getAltitude() == 1 ? this.getChamp_vision_haute_altitude() : this.getChamp_vision_basse_altitude());

		// fuel
		interpreteur.retirerDroneFuel(id);
		interpreteur.ajouterDroneFuel(id, this.getFuel());

		// position
		interpreteur.retirerPositionDrone(id);
		interpreteur.ajouterPositionDrone(id, id, this.l.x, this.l.y);

		// leader
		interpreteur.retirerLeaderDrone(id);
		for (Vehicule v : c.getLeaders())
			interpreteur.ajouterLeaderDrone(id, v.getNumero());

		// vision
		// interpreteur.retirerVisionDrone(id); //ne pas retirer la mémoire

		for (EntiteComportement a : agentsSupplementaires) {
			Location la = a.getLocation();
			EntitePercu ep = new EntitePercu(a);
			EntitePercu actuelConnaissance = entites.get(ep.getId());
			boolean chercheIdentite = false;

			if (this.isHaute_altitude()) {
				if (this.l.distanceEuclidean(la) < this.champ_vision_haute_altitude) {
					// si je ne l'ai jamais vu
					// ou que je l'ai vu, mais pas encore identifié et qu'il a
					// bougé
					if (actuelConnaissance == null || (!actuelConnaissance.isIdentifie() && !ep.positionEgale(actuelConnaissance))) {
						interpreteur.ajouterDroneVoitVehicule(id, a.getID(), la.x, la.y, System.currentTimeMillis());
						entites.put(ep.getId(), ep);
					} // s'il est déjà identifié mais que je suis en haute
						// altitude, je mets juste à jour sa position
					else if (actuelConnaissance != null && actuelConnaissance.isIdentifie())
						chercheIdentite = true;

				}
			} else {// basse altitude
				if (this.l.distanceEuclidean(la) < this.champ_vision_basse_altitude) {
					chercheIdentite = true;
				}
			}

			if (chercheIdentite) {
				// si je ne l'ai jamais vu
				// ou qu'il a bougé
				if (actuelConnaissance == null || !ep.positionEgale(actuelConnaissance))
					if (a instanceof Militaire) {
						interpreteur.ajouterDroneVoitMilitaire(id, a.getID(), la.x, la.y, System.currentTimeMillis());
						ep.setIdentifie(true);
						entites.put(ep.getId(), ep);
					} else if (a instanceof Civil) {
						interpreteur.ajouterDroneVoitCivil(id, a.getID(), la.x, la.y, System.currentTimeMillis());
						ep.setIdentifie(true);
						entites.put(ep.getId(), ep);
					}
			}
		}
	}

}

class EntitePercu {
	private int id, x, y;
	private long temps;
	private boolean identifie;

	public EntitePercu(EntiteComportement e) {
		this.x = e.getLocation().x;
		this.y = e.getLocation().y;
		this.id = e.getID();
		this.temps = System.currentTimeMillis();
		this.identifie = false;
	}

	public EntitePercu(int id, int x, int y, long temps, boolean identifie) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.temps = temps;
		this.identifie = identifie;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public long getTemps() {
		return temps;
	}

	public void setTemps(long temps) {
		this.temps = temps;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public boolean isIdentifie() {
		return identifie;
	}

	public void setIdentifie(boolean identifie) {
		this.identifie = identifie;
	}

	public boolean positionEgale(EntitePercu e) {
		return e.x == x && e.y == y;
	}

}
