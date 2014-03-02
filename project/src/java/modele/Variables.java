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
	 * A l'initialisation combien de drone seront en haute/basse altitude?
	 */
	public static float PROPORTION_DRONE_BASSE_HAUTE_ALTITUDE = 0.6f;

	/**
	 * Facteur de vitesse d'action du MAS
	 */
	public static float VITESSE_ACTION = 1.f;
	/**
	 * Quel pas de temps pour le deplacement des adversaires
	 */
	public static float VITESSE_ACTION_ADVERSAIRE = 1.f;

	/**
	 * Probabilité qu'un adversaire ait un comportement intégrant un déplacement
	 */
	public static float PROBA_ADVERSAIRE_VIRULENT = 0.4f;

	/**
	 * Singleton pour modifier dynamiquement la vitesse
	 */
	private static final Variables singleton = new Variables();

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
		synchronized (lock) {
			return vitesse;
		}
	}

	public void setVitesse(long v) {
		synchronized (lock) {
			vitesse = v;
		}
	}
}
