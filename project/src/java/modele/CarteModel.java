package modele;

import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import modele.percepts.AllPercepts;
import utils.Couple;
import vue.FenetrePpale;

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
	 * Liste des adversaires sur les 2 modèles (on peut les voir sur les 2
	 * grilles mais un adversaire ne peut être qu'au sol)
	 */
	private List<Adversaire> adversaire;

	/**
	 * Liste des civils sur les 2 modèles (on peut les voir sur les 2 grilles
	 * mais un civil ne peut être qu'au sol)
	 */
	private List<Civil> civil;

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

		adversaire = new LinkedList<Adversaire>();
		civil = new LinkedList<Civil>();

		terrain = new TerrainModel(nombreVehicule, interpreteur);
		ciel = new CielModel(nombreDrone, interpreteur, adversaire, civil);

		lesGrilles = new ArrayList<Grille>(2);
		lesGrilles.add(terrain);
		lesGrilles.add(ciel);

		generateur = new Random();
		this.interpreteur = interpreteur;

		interpreteur.ajouterTourDroneAuSol(1);
		interpreteur.ajouterTourDroneAuSol(2);
		interpreteur.ajouterTourDroneAuSol(3);
	}

	public TerrainModel getTerrain() {
		return terrain;
	}

	public CielModel getBasseAltitude() {
		return ciel;
	}

	/**
	 * Définie les 2 vues
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
	 *            la direction du deplacement(haut, bas, droite, gauche)
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

	public void remplirFuel(String agName) {
		Couple<Integer, Grille> c = dispatch(agName);
		ciel.getDrone(c.first).remplirFuel();
		ciel.getDrone(c.first).majPercepts(interpreteur, adversaire, civil);
	}

	public boolean decoller(String agName) {
		Couple<Integer, Grille> c = dispatch(agName);
		interpreteur.retirerTourDroneAuSol(c.first);
		return ciel.getDrone(c.first).decoller();
	}

	public boolean atterir(String agName) {
		Couple<Integer, Grille> c = dispatch(agName);
		interpreteur.ajouterTourDroneAuSol(c.first);
		return ciel.getDrone(c.first).atterir();
	}

	public boolean changerAltitude(String agName) {
		Couple<Integer, Grille> c = dispatch(agName);
		interpreteur.retirerAltitude(c.first);
		interpreteur.retirerFieldOfView(c.first);
		int altitude = ciel.getDrone(c.first).changerAltitude();
		interpreteur.ajouterAltitude(c.first, altitude);
		interpreteur.ajouterFieldOfView(c.first, altitude == 1 ? ciel.getDrone(c.first).getChamp_vision_haute_altitude() : ciel.getDrone(c.first)
				.getChamp_vision_basse_altitude());
		return true;
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
	 * Ajoute un civil dans chacune des grilles à la position x,y
	 * 
	 * @param x
	 * @param y
	 */
	public void ajouterAgentCivil(int x, int y) {
		for (Grille g : lesGrilles)
			g.add(Grille.CIVIL_CODE, x, y);
		Location but = this.terrain.getFreePos();
		civil.add(new Civil(x, y, but));
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
			if (terrain.getAgPos(i) != null) {
				double distance = l.distanceEuclidean(this.terrain.getAgPos(i));
				if (distance < md) {
					mn = i;
					md = distance;
				}
			}
		}
		return this.terrain.getAgPos(mn);
	}

	/**
	 * Détruit ce qu'il y a à la position t
	 */
	public void destruction(Location t) {
		if (terrain.getAgAtPos(t) != -1) {
			interpreteur.killVehicule(terrain.getAgAtPos(t));
			terrain.kill(t);
		}
	}

	/**
	 * calcule la visibilité entre 2 points en fonction du relief de façon
	 * approximative résultat entre 0 et 1
	 */
	public double visibilite(Location l1, Location l2) {

		int h1 = this.terrain.getHauteur(l1.x, l1.y);
		int h2 = this.terrain.getHauteur(l2.x, l2.y);

		double v = 0.5 + ((double) (h1 * h2)) / (255 * 255 * 2);

		return v;
	}

	/**
	 * Appeler à chaque iteration pour que les adversaires et civils fassent
	 * leur action
	 */
	public void runEnv() {

		// Adversaires
		for (Adversaire a : adversaire) {

			Location l = a.getLocation();

			Location t = this.find_target(l);

			if ((l.distanceEuclidean(t) < a.vision()) & (this.visibilite(l, t) > generateur.nextDouble())) {
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
					if (trou.isInArea(new Location(0, 0), new Location(this.terrain.getWidth(), this.terrain.getHeight())))
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

		// Civils
		for (Civil c : civil) {

			Location l = c.getLocation();
			Location but = c.getBut();

			int dx = but.x - l.x;
			int dy = but.y - l.y;
			int direction;

			if (but.equals(l)) { // c est arrivé à son but, on le supprime

			} else {
				if (dx == 0) {
					if (dy > 0)
						direction = 3;
					else
						direction = 1;
				} else if (dy == 0) {
					if (dx > 0)
						direction = 2;
					else
						direction = 0;
				} else {

					// on trouve dans quelle direction le civil doit avancer
					// : la direction qui le rapproche le plus de son but
					// tout en restant le plus bas possible

					int rx; // relief de la case d'à coté en x
					if (dx > 0)
						rx = this.terrain.getHauteur(l.x + 1, l.y);
					else
						rx = this.terrain.getHauteur(l.x - 1, l.y);

					int ry; // relief de la case d'à coté en y
					if (dy > 0)
						ry = this.terrain.getHauteur(l.x, l.y + 1);
					else
						ry = this.terrain.getHauteur(l.x, l.y - 1);

					if (rx > ry) {
						if (dy > 0)
							direction = 3;
						else
							direction = 1;
					} else {
						if (dx > 0)
							direction = 2;
						else
							direction = 0;
					}
				}

				for (Grille g : lesGrilles)
					g.remove(Grille.CIVIL_CODE, c.getLocation());
				Grille.deplacer(l, direction);
				c.setLocation(l);
				for (Grille g : lesGrilles)
					g.add(Grille.CIVIL_CODE, c.getLocation());
			}
		}
	}

	public boolean scinder(int agent) {
		return terrain.scinder(agent);
	}

	public boolean tirer(int x, int y) {
		Adversaire killed = null;
		for (Adversaire a : adversaire)
			if (a.getLocation().x == x && a.getLocation().y == y) {
				killed = a;
				break;
			}
		if (killed != null) {
			adversaire.remove(killed);
			for (Grille g : lesGrilles)
				g.remove(Grille.ADVERSAIRE_CODE, killed.getLocation());
		}
		return false;
	}
}
