package modele.entite;

import jason.environment.grid.Location;

public abstract class EntiteDeplacable extends EntiteLocalisable {

	protected Comportement c;

	protected Location but;

	public EntiteDeplacable(Location l, Comportement c) {
		super(l);
		this.c = c;
	}

	public Location getBut(){
		return but;
	}
	
	void setBut(Location l) {
		but = l;
	}

	Comportement getComportement() {
		return c;
	}

	public boolean deplace() {
		return c == Comportement.But || c == Comportement.DeplaceAleatoire;
	}

	public abstract void deplacer();
}
