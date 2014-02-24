package modele;

import vue.FenetrePpale;

public class CarteModel {

	private TerrainModel terrain;
	private BasseAltitudeModel basse_altitude;
	private HauteAltitudeModel haute_altitude;

	public CarteModel(Interpreteur interpreteur, int nombreVehicule, int nombreDrone) {

		// TODO:générer haut/basse drone

		int nombre_drone_basse_altitude = (int) (nombreDrone * Constantes.PROPORTION_DRONE_BASSE_HAUTE_ALTITUDE);
		int nombre_drone_haute_altitude = nombreDrone - nombre_drone_basse_altitude;

		terrain = new TerrainModel(nombreVehicule);
		basse_altitude = new BasseAltitudeModel(nombre_drone_basse_altitude);
		haute_altitude = new HauteAltitudeModel(nombre_drone_haute_altitude);
	}

	public TerrainModel getTerrain() {
		return terrain;
	}

	public HauteAltitudeModel getHauteAltitude() {
		return haute_altitude;
	}

	public BasseAltitudeModel getBasseAltitude() {
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

	public void deplacer(String agName, int position) {
		terrain.deplacer(toAgent(agName), position);
	}

	private int toAgent(String agName) {
		switch (agName.charAt(0)) {
		case 'd':
			int index = Integer.parseInt(agName.substring(1));
			return index >= basse_altitude.getNbOfAgs() ? index - basse_altitude.getNbOfAgs() : index;
		case 't':
			return Integer.parseInt(agName.substring(1));
		case 'v':
			return Integer.parseInt(agName.substring(1));
		default:
			return -1;
		}
	}
}
