package modele.entite;

import jason.environment.grid.Location;

import java.util.Random;

import modele.CarteModel;
import modele.Grille;
import modele.TerrainModel;
import modele.Variables;

public abstract class EntiteDeplacable extends EntiteLocalisable {

	protected Comportement c;

	protected Location but;

	protected Random generateur = new Random();

	public EntiteDeplacable(Location l, Comportement c, int code) {
		super(l, code);
		this.c = c;
	}

	public EntiteDeplacable(Location l, Location but, int code) {
		super(l, code);
		this.c = Comportement.But;
		this.but = but;
	}

	public Location getBut() {
		return but;
	}

	void setBut(Location l) {
		but = l;
	}

	Comportement getComportement() {
		return c;
	}

	public boolean seDeplace() {
		return c == Comportement.But || c == Comportement.DeplaceAleatoire;
	}

	public void deplacer(TerrainModel terrain) {
		int dx = but.x - l.x;
		int dy = but.y - l.y;

		if (but.equals(l)) { // c est arrivé à son but, on le supprime
			if (c == Comportement.DeplaceAleatoire)
				but = null;
		} else {
			int direction;
			if (dx == 0) {
				if (dy > 0)
					direction = 3;
				else
					direction = 1;
			} else if (dy == 0) {
				if (dx > 0)
					direction = 2;
				else
					direction = 0;
			} else {

				// on trouve dans quelle direction le civil doit avancer
				// : la direction qui le rapproche le plus de son but
				// tout en restant le plus bas possible

				int rx; // relief de la case d'à coté en x
				if (dx > 0)
					rx = terrain.getHauteur(l.x + 1, l.y);
				else
					rx = terrain.getHauteur(l.x - 1, l.y);

				int ry; // relief de la case d'à coté en y
				if (dy > 0)
					ry = terrain.getHauteur(l.x, l.y + 1);
				else
					ry = terrain.getHauteur(l.x, l.y - 1);

				if (rx > ry) {
					if (dy > 0)
						direction = 3;
					else
						direction = 1;
				} else {
					if (dx > 0)
						direction = 2;
					else
						direction = 0;
				}
			}

			Location nl = Grille.deplacer(getLocation(), direction);
			if (terrain.isFree(nl))
				setLocation(nl);
		}
	}

	public void definirBut(CarteModel carteModel) {
		if (c == Comportement.DeplaceAleatoire) {
			if (but == null)
				setBut(new Location(generateur.nextInt(Variables.TAILLE_CARTE_X), generateur.nextInt(Variables.TAILLE_CARTE_Y)));
		} else if (c == Comportement.Fixe)
			but = null;
	}
}
