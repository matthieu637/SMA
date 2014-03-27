package modele;

import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import modele.entite.Drone;
import modele.entite.EntiteComportement;
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
	 * Liste des civils sur les 2 modèles (on peut les voir sur les 2 grilles
	 * mais un civil ne peut être qu'au sol)
	 */
	private List<EntiteComportement> agentsSupplementaires;

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

	private Object lock = new Object();

	public CarteModel(AllPercepts interpreteur, int nombreVehicule, int nombreDrone) {
		agentsSupplementaires = new LinkedList<>();

		terrain = new TerrainModel(nombreVehicule, interpreteur);
		ciel = new CielModel(nombreDrone, interpreteur, agentsSupplementaires, terrain.getConvoi());

		lesGrilles = new ArrayList<Grille>(2);
		lesGrilles.add(terrain);
		lesGrilles.add(ciel);

		this.interpreteur = interpreteur;

		for (int i = 1; i <= nombreDrone; i++)
			interpreteur.ajouterTourDroneAuSol(i);
		interpreteur.ajouterDrones(nombreDrone, nombreVehicule);
		for (int i = 1; i <= nombreVehicule; i++)
			for (int j = 1; j <= nombreVehicule; j++)
				interpreteur.ajouterVehicule(i, j);
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
		ciel.getDrone(c.first).majPercepts(interpreteur, agentsSupplementaires, terrain.getConvoi());
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
		Drone d = ciel.getDrone(c.first);
		d.changerAltitude();
		d.majPercepts(interpreteur, agentsSupplementaires, terrain.getConvoi());
		ciel.updatePosition(d);
		// interpreteur.retirerAltitude(c.first);
		// interpreteur.retirerFieldOfView(c.first);
		// int altitude = ciel.getDrone(c.first).changerAltitude();
		// interpreteur.ajouterAltitude(c.first, altitude);
		// interpreteur.ajouterFieldOfView(c.first, altitude == 1 ?
		// ciel.getDrone(c.first).getChamp_vision_haute_altitude() :
		// ciel.getDrone(c.first)
		// .getChamp_vision_basse_altitude());
		return true;
	}

	public boolean tirer(String agName, int x, int y) {
		Couple<Integer, Grille> c = dispatch(agName);
		Drone d = ciel.getDrone(c.first);
		d.majPercepts(interpreteur, agentsSupplementaires, terrain.getConvoi());

		EntiteComportement killed = null;
		for (EntiteComportement a : agentsSupplementaires)
			if (a.getLocation().x == x && a.getLocation().y == y) {
				ciel.anime(d.getPos(), a.getLocation(), a, lesGrilles);
				killed = a;
				break;
			}
		if (killed != null) {
			System.out.println("target killed");
			detruireAgentSupplementaire(killed, false);
			return true;
		}
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
	 * Ajoute un agent dans chacune des grilles
	 * 
	 * @param x
	 * @param y
	 * @param ennemie
	 */
	public void ajouterAgent(EntiteComportement a) {
		for (Grille g : lesGrilles)
			g.add(a.getCode(), a.getLocation());

		agentsSupplementaires.add(a);
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
	public void destruction(Location from, Location t) {
		if (terrain.getAgAtPos(t) != -1) {
			int ag = terrain.getAgAtPos(t);
			if (terrain.retirerAgent(t, true, from))
				interpreteur.killVehicule(ag + 1);

			ciel.majPercepts();
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

		synchronized (lock) {
			List<EntiteComportement> mort = new LinkedList<>();

			for (EntiteComportement e : agentsSupplementaires) {
				if (e.canAct()) {
					e.definirBut(this);
					if (e.seDeplace()) {
						for (Grille g : lesGrilles)
							g.remove(e.getCode(), e.getLocation());

						e.deplacer(terrain);

						for (Grille g : lesGrilles)
							g.add(e.getCode(), e.getLocation());
					}
					if (e.agir(this))
						mort.add(e);
				}

			}

			for (EntiteComportement m : mort)
				detruireAgentSupplementaire(m, true);
		}

		interpreteur.retirerTourAllies();
		for (EntiteComportement e : agentsSupplementaires)
			e.majPercept(interpreteur);
	}

	public boolean scinder(String agent) {
		Couple<Integer, Grille> c = dispatch(agent);
		return terrain.scinder(c.first);
	}

	private void detruireAgentSupplementaire(EntiteComportement e, boolean removeGrid) {
		agentsSupplementaires.remove(e);
		if (removeGrid)
			for (Grille g : lesGrilles)
				g.remove(e.getCode(), e.getLocation());
	}

	public boolean termine() {
		return terrain.convoiArrive() && ciel.droneTermine();
	}
}
