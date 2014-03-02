package modele;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Contient toutes les variables modifiables du système
 */
public interface Variables {

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
	 * Vitesse d'action du MAS
	 */
	public static long VITESSE_ACTION = 100;
	/**
	 * Quel pas de temps pour le deplacement des adversaires
	 */
	public static long VITESSE_ACTION_ADVERSAIRE = 500;

	/**
	 * Probabilité qu'un adversaire ait un comportement intégrant un déplacement
	 */
	public static float PROBA_ADVERSAIRE_VIRULENT = 0.4f;
}
