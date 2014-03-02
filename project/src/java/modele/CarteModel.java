package modele;

import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import modele.percepts.AllPercepts;
import utils.Couple;
import vue.FenetrePpale;
import ext.GridWorldModelP;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * @author Sébastien Forestier <sforesti@clipper.ens.fr>
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
	 * Drone dans le ciel
	 */
	private CielModel ciel;

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

	private AllPercepts interpreteur;

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
	public CarteModel(AllPercepts interpreteur, int nombreVehicule, int nombreDrone) {
		terrain = new TerrainModel(nombreVehicule, interpreteur);
		ciel = new CielModel(nombreDrone, interpreteur);

		adversaire = new LinkedList<Adversaire>();
		lesGrilles = new ArrayList<Grille>(2);
		lesGrilles.add(terrain);
		lesGrilles.add(ciel);

		generateur = new Random();
		this.interpreteur = interpreteur;
	}

	public TerrainModel getTerrain() {
		return terrain;
	}

	public CielModel getBasseAltitude() {
		return ciel;
	}

	/**
	 * Définie les 3 vues
	 * 
	 * @param vue
	 */
	public void setView(FenetrePpale vue) {
		terrain.setView(vue.getTerrain());
		ciel.setView(vue.getCiel());
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
		c.second.deplacerCollision(c.first, direction);
	}

	/**
	 * Déplace un agent sur une des grilles avec collision
	 * 
	 * @param agName
	 *            le nom de l'agent
	 * @param direction
	 *            la direction du deplacement( haut,bas, droite, gauche)
	 */
	public boolean deplacerCollision(String agName, int direction) {
		Couple<Integer, Grille> c = dispatch(agName);
		Couple<Location, Boolean> c2 = c.second.deplacerCollision(c.first, direction);
		if (c2 != null)
			return c2.second;
		return false;
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
		Couple<Integer, Grille> r = new Couple<Integer, Grille>(index, null);

		switch (agName.charAt(0)) {
		case 'd':
			r.second = ciel;
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
	 * Trouve le véhicule du convoi le plus proche de l
	 */
	public Location find_target(Location l) {

		int n = this.terrain.getNbOfAgs();
		if (n == 0)
			return null;

		int mn = 0;
		double md = 1000.;
		for (int i = 0; i < n; i++) {
			double distance = l.distanceEuclidean(this.terrain.getAgPos(i));
			if (distance < md) {
				mn = i;
				md = distance;
			}
		}
		return this.terrain.getAgPos(mn);
	}

	/**
	 * Détruit ce qu'il y a à la position t
	 */
	public void destruction(Location t) {
		interpreteur.killVehicule(terrain.getAgAtPos(t));
		terrain.remove(GridWorldModelP.AGENT, t);
	}
	
	/**
	* calcule la visibilité entre 2 points en fonction du relief
	* de façon approximative
	* résultat entre 0 et 1
	*/
	public double visibilite(Location l1, Location l2) {
		
		int h1 = this.terrain.getHauteur(l1.x, l1.y);
		int h2 = this.terrain.getHauteur(l2.x, l2.y);
		
		double v = 0.5 + ((double) (h1*h2)) / (255*255*2);
		
		return v;		
	}

	/**
	 * Appeler à chaque iteration pour que les adversaires fassent leur action
	 */
	public void runAdversaire() {
		for (Adversaire a : adversaire) {

			Location l = a.getLocation();

			Location t = this.find_target(l);

			if ((l.distanceEuclidean(t) < a.vision()) & (this.visibilite(l,t) > generateur.nextDouble())) { 
				// s'il y a une cible à portee de
				// vue et plutot visible, on s'en approche
				// et/ou on tire

				if (a.virulent()) { // on s'en approche

					// on trouve dans quelle direction l'adversaire doit avancer
					// : la direction qui le rapproche le plus de la cible
					int dx = t.x - l.x;
					int dy = t.y - l.y;
					int direction;

					if (Math.abs(dx) > Math.abs(dy)) {
						if (dx > 0) {
							direction = 2;
						} else {
							direction = 0;
						}
					} else {
						if (dy > 0) {
							direction = 3;
						} else {
							direction = 1;
						}
					}

					for (Grille g : lesGrilles)
						g.remove(Grille.ADVERSAIRE_CODE, a.getLocation());
					Grille.deplacer(l, direction);
					a.setLocation(l);
					for (Grille g : lesGrilles)
						g.add(Grille.ADVERSAIRE_CODE, a.getLocation());
				}

				if (l.distanceEuclidean(t) < a.portee()) { // on tire
					Location trou = a.tir(t, generateur.nextGaussian(), generateur.nextGaussian());
					this.destruction(trou);
				}
			} else { // sinon, on bouge alétoirement si on est virulent

				if (a.virulent()) {
					for (Grille g : lesGrilles)
						g.remove(Grille.ADVERSAIRE_CODE, a.getLocation());
					Grille.deplacer(l, generateur.nextInt(4));
					a.setLocation(l);
					for (Grille g : lesGrilles)
						g.add(Grille.ADVERSAIRE_CODE, a.getLocation());
				}
			}
		}
	}
}
