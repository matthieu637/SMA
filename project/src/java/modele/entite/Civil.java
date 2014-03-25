package modele.entite;

import jason.environment.grid.Location;
import modele.CarteModel;
import modele.Grille;
import modele.percepts.AllPercepts;

/**
 * @author Sébastien Forestier <sforesti@clipper.ens.fr>
 * 
 *         Représente un civil
 */
public class Civil extends EntiteComportement {

	public Civil(Location l, Location but) {
		super(l, but, Grille.CIVIL_CODE);
	}

	public Civil(Location l, Comportement c) {
		super(l, c, Grille.CIVIL_CODE);
	}

	@Override
	public void majPercept(AllPercepts interpreteur) {

	}

	@Override
	public void _agir(CarteModel carteModel) {

	}

}