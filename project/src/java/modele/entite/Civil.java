package modele.entite;

import jason.environment.grid.Location;

/**
 * @author Sébastien Forestier <sforesti@clipper.ens.fr>
 * 
 *         Représente un civil
 */
public class Civil extends EntiteDeplacable {

	public Civil(Location l, Comportement c, Location but) {
		super(l, c);
		this.setBut(but);
	}

	@Override
	public void deplacer() {
		// TODO Auto-generated method stub
		
	}

}