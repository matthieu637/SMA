package modele;

import modele.percepts.AllPercepts;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Contient les informations du modèle pour l'environnement des drônes
 *         en basse altitude
 */
public class BasseAltitudeModel extends Grille {

	protected BasseAltitudeModel(int nbAgent, AllPercepts interpreteur) {
		super(Variables.TAILLE_CARTE_X, Variables.TAILLE_CARTE_Y, nbAgent, interpreteur);
	}

}
