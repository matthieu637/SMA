package modele.entite;

import jason.environment.grid.Location;

import java.util.List;

import modele.percepts.AllPercepts;
import java.util.Random;

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

	public Drone(int id, boolean haute_altitude, Location l, int maxFuel, int champ_vision_basse_altitude, int champ_vision_haute_altitude) {
		this.setId(id);
		this.setPos(l);
		this.setHaute_altitude(haute_altitude);
		generateur = new Random();
		this.maxFuel = maxFuel + generateur.nextInt(200);
		this.auSol = true;
		this.champ_vision_basse_altitude = champ_vision_basse_altitude;
		this.champ_vision_haute_altitude = champ_vision_haute_altitude;
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

	public void majPercepts(AllPercepts interpreteur, List<EntiteComportement> agentsSupplementaires) {

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

		// vision
		interpreteur.retirerVisionDrone(id);

		if (this.isHaute_altitude()) {
			for (EntiteComportement a : agentsSupplementaires) {
				Location la = a.getLocation();
				if (this.l.distanceEuclidean(la) < this.champ_vision_haute_altitude)
					interpreteur.ajouterDroneVoitVehicule(id, la.x, la.y);
			}
		} else {
			for (EntiteComportement a : agentsSupplementaires) {
				Location la = a.getLocation();
				if (this.l.distanceEuclidean(la) < this.champ_vision_haute_altitude)
					//TODO:check if it really works 
					if (a instanceof Militaire)
						interpreteur.ajouterDroneVoitMilitaire(id, la.x, la.y);
					else if (a instanceof Civil)
						interpreteur.ajouterDroneVoitCivil(id, la.x, la.y);
			}

		}

	}

}
