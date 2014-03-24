package modele.entite;

import jason.environment.grid.Location;


/**
 * @author Sébastien Forestier <sforesti@clipper.ens.fr>
 * 
 *         Représente un adversaire
 */
public class Militaire extends EntiteDeplacable {


	public Militaire(Location l, Comportement c) {
		super(l, c);
	}

	public Militaire(Location l, Comportement c, Location but) {
		super(l, c);
		this.setBut(but);
	}

	@Override
	public void deplacer() {
		// TODO Auto-generated method stub
		
	}
}
