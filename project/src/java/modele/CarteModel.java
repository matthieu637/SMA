package modele;

import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import utils.Couple;
import vue.FenetrePpale;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Modèle haut niveau, il contient les 3 différents grille et les
 *         adversaires
 * 
 */
public class CarteModel {

	/**
	 * Le modèle au sol, contenant le but et les véhicules
	 */
	private TerrainModel terrain;
	/**
	 * Drone en basse altitude
	 */
	private BasseAltitudeModel basse_altitude;
	/**
	 * Drone haute altitude
	 */
	private HauteAltitudeModel haute_altitude;

	/**
	 * Contient les 3 modèles précedent pour faciliter le polymorphisme
	 */
	private List<Grille> lesGrilles;

	/**
	 * Liste des adversaires sur les 3 modèles (on peut les voir sur les 3
	 * grilles mais un adversaire ne peut être qu'au sol)
	 */
	private List<Adversaire> adversaire;

	private Random generateur;

	/**
	 * Initialise les modèles, puis dispatche le nombre de drône en haute
	 * altitude ou basse altitude
	 * 
	 * @param interpreteur
	 *            permet de communiquer des percepts à l'environnement de façon
	 *            plus propre
	 * @param nombreVehicule
	 *            nombre de véhicule au sol (lu dans le .mas2j)
	 * @param nombreDrone
	 *            nombre de drone
	 */
	public CarteModel(Interpreteur interpreteur, int nombreVehicule, int nombreDrone) {
		int nombre_drone_basse_altitude = (int) (nombreDrone * Variables.PROPORTION_DRONE_BASSE_HAUTE_ALTITUDE);
		int nombre_drone_haute_altitude = nombreDrone - nombre_drone_basse_altitude;

		terrain = new TerrainModel(nombreVehicule);
		basse_altitude = new BasseAltitudeModel(nombre_drone_basse_altitude);
		haute_altitude = new HauteAltitudeModel(nombre_drone_haute_altitude);

		adversaire = new LinkedList();
		lesGrilles = new ArrayList(3);
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

	/**
	 * Définie les 3 vues
	 * 
	 * @param vue
	 */
	public void setView(FenetrePpale vue) {
		terrain.setView(vue.getTerrain());
		basse_altitude.setView(vue.getBasse_altitude());
		haute_altitude.setView(vue.getHaute_altitude());
	}

	/**
	 * Déplace un agent sur une des grilles
	 * 
	 * @param agName
	 *            le nom de l'agent
	 * @param direction
	 *            la direction du deplacement( haut,bas, droite, gauche)
	 */
	public void deplacer(String agName, int direction) {
		Couple<Integer, Grille> c = dispatch(agName);
		c.second.deplacer(c.first, direction);
	}

	/**
	 * Permet de récuperer la grille sur laquelle est l'agent à partir de son
	 * nom et son index dans cette grille
	 * 
	 * @param agName
	 *            nom de l'agent
	 * @return l'index de l'agent dans sa grille, sa grille
	 */
	private Couple<Integer, Grille> dispatch(String agName) {
		int index = Integer.parseInt(agName.substring(1));
		Couple<Integer, Grille> r = new Couple(index, null);

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

	/**
	 * Ajoute un agent adversaire dans chacune des grilles à la position x,y
	 * 
	 * @param x
	 * @param y
	 */
	public void ajouterAgentAdverse(int x, int y) {
		for (Grille g : lesGrilles)
			g.add(Grille.ADVERSAIRE_CODE, x, y);
		adversaire.add(new Adversaire(x, y, generateur.nextFloat() < Variables.PROBA_ADVERSAIRE_VIRULENT));
	}

	/**
	 * Appeler à chaque iteration pour faire se déplacer les adversaires
	 */
	public void deplaceAdversaire() {
		for (Adversaire a : adversaire) {
			if (a.virulent()) {
				for (Grille g : lesGrilles)
					g.remove(Grille.ADVERSAIRE_CODE, a.getLocation());
				Location l = a.getLocation();
				Grille.deplacer(l, generateur.nextInt(4));
				a.setLocation(l);
				for (Grille g : lesGrilles)
					g.add(Grille.ADVERSAIRE_CODE, a.getLocation());
			}
		}
	}
}
