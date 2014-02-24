package modele;

import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import utils.Couple;
import vue.FenetrePpale;

public class CarteModel {

	private static final int ADVERSAIRE_CODE = 8;

	private TerrainModel terrain;
	private BasseAltitudeModel basse_altitude;
	private HauteAltitudeModel haute_altitude;

	private List<Adversaire> adversaire;
	private List<Grille> lesGrilles;

	private Random generateur;

	public CarteModel(Interpreteur interpreteur, int nombreVehicule, int nombreDrone) {
		int nombre_drone_basse_altitude = (int) (nombreDrone * Constantes.PROPORTION_DRONE_BASSE_HAUTE_ALTITUDE);
		int nombre_drone_haute_altitude = nombreDrone - nombre_drone_basse_altitude;

		terrain = new TerrainModel(nombreVehicule);
		basse_altitude = new BasseAltitudeModel(nombre_drone_basse_altitude);
		haute_altitude = new HauteAltitudeModel(nombre_drone_haute_altitude);

		adversaire = new LinkedList<>();
		lesGrilles = new ArrayList<>(3);
		lesGrilles.add(terrain);
		lesGrilles.add(basse_altitude);
		lesGrilles.add(haute_altitude);

		generateur = new Random();
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
		Couple<Integer, Grille> c = dispatch(agName);
		c.second.deplacer(c.first, position);
	}

	private Couple<Integer, Grille> dispatch(String agName) {
		int index = Integer.parseInt(agName.substring(1));
		Couple<Integer, Grille> r = new Couple<>(index, null);

		switch (agName.charAt(0)) {
		case 'd':
			if (index >= basse_altitude.getNbOfAgs()) {
				r.first = index - basse_altitude.getNbOfAgs();
				r.second = haute_altitude;
			} else
				r.second = basse_altitude;
			break;
		case 'v':
			r.second = terrain;
			break;
		case 't':
			break;
		}

		return r;
	}

	public void ajouterAgentAdverse(int x, int y) {
		for (Grille g : lesGrilles)
			g.add(ADVERSAIRE_CODE, x, y);
		adversaire.add(new Adversaire(x, y));
	}

	public void deplaceAdversaire() {
		for (Adversaire a : adversaire) {
			for (Grille g : lesGrilles)
				g.remove(ADVERSAIRE_CODE, a.getLocation());
			Location l = a.getLocation();
			Grille.deplacer(l, generateur.nextInt(4));
			a.setLocation(l);
			for (Grille g : lesGrilles)
				g.add(ADVERSAIRE_CODE, a.getLocation());
		}
	}
}
