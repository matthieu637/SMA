package modele;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Contient toutes les variables modifiables du système
 */
public class Variables {

	/**
	 * Taille des grilles en largeur
	 */
	public static int TAILLE_CARTE_X = 60;
	/**
	 * Taille des grilles en hauteur
	 */
	public static int TAILLE_CARTE_Y = 60;

	/**
	 * Paramètres de la fractale pour le terrain au sol
	 */
	public static float FRACTALE_DIMENSION = 1.5f;
	public static int FRACTALE_N_LEVEL = 4;
	/**
	 * Nombre d'iteration de smooth
	 */
	public static int SMOOTH_CARTE = 2;

	/**
	 * Facteur de vitesse d'action / temps de réflexion de chaque agent du MAS
	 * Evite d'avoir le CPU qui tourne à 100%.
	 */
	public static float TEMPS_REFLEXION = 0.2f;

	/**
	 * Quel pas de temps pour le deplacement des vehicule
	 */
	public static final float VITESSE_DEPLACEMENT_VEHICULE = 1.5f;
	/**
	 * Quel pas de temps pour le deplacement des adversaires
	 */
	public static float VITESSE_ACTION_ADVERSAIRE = 1.5f;

	/**
	 * Singleton pour modifier dynamiquement la vitesse
	 */
	private static final Variables singleton = new Variables();

	/**
	 * Capacité en fuel des drones (en nombre de case)
	 */
	public static int CAPACITE_FUEL_DRONE = 500;

	/**
	 * Capacité en fuel des véhicules (en nombre de case)
	 */
	public static int CAPACITE_FUEL_VEHICULE = 100;

	/**
	 * Champ de vision des drones à basse altitude, en nombre de cases
	 */
	public static int CHAMP_VISION_DRONE_BASSE_ALTITUDE = 3;
	
	public static final int VISION_ENNEMI = 5;
	
	public static final int PORTE_ENNEMI = 4;

	/**
	 * Champ de vision des drones à haute altitude, en nombre de cases
	 */
	public static int CHAMP_VISION_DRONE_HAUTE_ALTITUDE = 6;

	public static float DIRECTION_IMPORTANCE_HAUTEUR = 0.05f;
	
	/**
	 * Valeur de hauteur en dehors de la carte (utile smooth)
	 */
	public static int HAUTEUR_HORS_CARTE = 150;

	public static final float PROBA_DEPLACEMENT_ALEATOIRE = 0.01f;
	
	/**
	 * Vitesse d'exécution globale
	 */
	private long vitesse;

	/**
	 * Protège le changement de vitesse
	 */
	private final Object lock = new Object();

	private Variables() {
		synchronized (lock) {
			vitesse = 500;
		}
	}

	public static Variables getInstance() {
		return singleton;
	}

	public long getVitesse() {
		return vitesse;
	}

	public void setVitesse(long v) {
		synchronized (lock) {
			vitesse = v;
		}
	}
}
