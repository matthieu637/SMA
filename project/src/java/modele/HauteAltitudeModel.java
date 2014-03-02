package modele;

import modele.percepts.AllPercepts;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Modèle des drônes en haute altitude
 */
public class HauteAltitudeModel extends Grille {

	protected HauteAltitudeModel(int nbAgent, AllPercepts interpreteur) {
		super(Variables.TAILLE_CARTE_X, Variables.TAILLE_CARTE_Y, nbAgent, interpreteur);
	}

}
