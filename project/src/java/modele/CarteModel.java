package modele;

import vue.FenetrePpale;
import ext.GridWorldModelP;


public class CarteModel {

	private TerrainModel terrain;
	private BasseAltitudeModel basse_altitude;
	private HauteAltitudeModel haute_altitude; 
	
	public CarteModel(Interpreteur interpreteur, int nombreVehicule, int nombreDrone) {
		
		//TODO:générer haut/basse drone
		
		terrain = new TerrainModel(nombreVehicule);
		basse_altitude = new BasseAltitudeModel(nombreDrone/2);
		haute_altitude = new HauteAltitudeModel(nombreDrone/2);
	}

	public GridWorldModelP getTerrain() {
		return terrain;
	}

	public GridWorldModelP getHauteAltitude() {
		return haute_altitude;
	}

	public GridWorldModelP getBasseAltitude() {
		return basse_altitude;
	}

	public void update() {
		terrain.update();
		basse_altitude.update();
		haute_altitude.update();
	}

	public void setView(FenetrePpale vue) {
		terrain.setView(vue.getTerrain());
		basse_altitude.setView(vue.getBasse_altitude());
		haute_altitude.setView(vue.getHaute_altitude());
	}

}
