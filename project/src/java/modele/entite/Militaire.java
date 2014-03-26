package modele.entite;

import jason.environment.grid.Location;
import modele.CarteModel;
import modele.Grille;
import modele.percepts.AllPercepts;


/**
 * @author Sébastien Forestier <sforesti@clipper.ens.fr>
 * 
 *         Représente un adversaire
 */
public class Militaire extends EntiteComportement {


	public Militaire(Location l, Comportement c, int code) {
		super(l, c, code);
	}

	public Militaire(Location l, Location but, int code) {
		super(l, but, code);
	}

	public Militaire(Location l, Comportement c) {
		super(l, c, Grille.ALLIE_CODE);
	}

	public Militaire(Location l, Location but) {
		super(l, but, Grille.ALLIE_CODE);
	}

	@Override
	public void majPercept(AllPercepts interpreteur) {
		interpreteur.ajouterTourAllie(getID());
	}

	@Override
	public void _agir(CarteModel carteModel) {
		
	}

}
