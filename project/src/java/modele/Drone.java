package modele;

import jason.environment.grid.Location;
import modele.percepts.AllPercepts;

import java.util.List;
import jason.environment.grid.Location;

public class Drone {

	private int id;
	private boolean haute_altitude;
	private int champ_vision_basse_altitude;
	private int champ_vision_haute_altitude;
	private int fuel;
	private Location l;

	public Drone(int id, boolean haute_altitude, int fuel, int champ_vision_basse_altitude, int champ_vision_haute_altitude) {
		this.setId(id);
		this.setHaute_altitude(haute_altitude);
		this.fuel = fuel;
		this.champ_vision_basse_altitude = champ_vision_basse_altitude;
		this.champ_vision_haute_altitude = champ_vision_haute_altitude;
	}

	public boolean isHaute_altitude() {
		return haute_altitude;
	}

	public void setHaute_altitude(boolean haute_altitude) {
		this.haute_altitude = haute_altitude;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void majPercepts(AllPercepts interpreteur, Location nl, List<Adversaire> adversaire, List<Civil> civil) {
		interpreteur.retirerPositionDrone(id);
		interpreteur.ajouterPositionDrone(id, nl.x, nl.y);

		interpreteur.retirerVisionDrone(id);
		
		if (this.isHaute_altitude()) {				
			for (Adversaire a : adversaire) {
				Location la = a.getLocation();
				if (this.l.distanceEuclidean(la) < this.champ_vision_haute_altitude)
					interpreteur.ajouterDroneVoitVehicule(id, la.x, la.y);
			}
			for (Civil c : civil) {
				Location lc = c.getLocation();
				if (this.l.distanceEuclidean(lc) < this.champ_vision_haute_altitude)
					interpreteur.ajouterDroneVoitVehicule(id, lc.x, lc.y);
			}
		}
		else {	
			for (Adversaire a : adversaire) {
				Location la = a.getLocation();
				if (this.l.distanceEuclidean(la) < this.champ_vision_basse_altitude)
					interpreteur.ajouterDroneVoitAdversaire(id, la.x, la.y);
			}
			for (Civil c : civil) {
				Location lc = c.getLocation();
				if (this.l.distanceEuclidean(lc) < this.champ_vision_basse_altitude)
					interpreteur.ajouterDroneVoitCivil(id, lc.x, lc.y);
			}			
		}
				
		l = nl;
	}	
	
}
