package modele;

import jason.environment.grid.Location;
import modele.percepts.AllPercepts;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Garde les informations du leader en mémoire pour mettre à jour les
 *         percepts
 */
public class Vehicule {

	private final int numero;

	private Location l;

	private static final int OUT = 500;

	public Vehicule(int numero, Location l) {
		this.numero = numero;
		this.l = l;
	}

	public int getNumero() {
		return numero;
	}

	public void initLeader(AllPercepts interpreteur, Location but, int[][] hauteur) {
		interpreteur.ajouterLeader(numero);
		interpreteur.ajouterPositionVehicule(numero, l.x, l.y);
		interpreteur.ajouterPositionButVehicule(numero, but.x, but.y);
		interpreteur.ajouterHeightmap(numero, l.x - 1 >= 0 ? hauteur[l.x - 1][l.y] : OUT, l.y - 1 >= 0 ? hauteur[l.x][l.y - 1] : OUT,
				l.x + 1 < Variables.TAILLE_CARTE_X ? hauteur[l.x + 1][l.y] : OUT,
				l.y + 1 < Variables.TAILLE_CARTE_Y ? hauteur[l.x][l.y + 1] : OUT);
	}

	public void deplacer(AllPercepts interpreteur, Location nl, int[][] hauteur) {
		interpreteur.retirerPositionVehicule(numero, l.x, l.y);
		interpreteur.ajouterPositionVehicule(numero, nl.x, nl.y);

		interpreteur.retirerHeightmap(numero, l.x - 1 >= 0 ? hauteur[l.x - 1][l.y] : OUT, l.y - 1 >= 0 ? hauteur[l.x][l.y - 1] : OUT,
				l.x + 1 < Variables.TAILLE_CARTE_X ? hauteur[l.x + 1][l.y] : OUT,
				l.y + 1 < Variables.TAILLE_CARTE_Y ? hauteur[l.x][l.y + 1] : OUT);
		interpreteur.ajouterHeightmap(numero, nl.x - 1 >= 0 ? hauteur[nl.x - 1][nl.y] : OUT, nl.y - 1 >= 0 ? hauteur[nl.x][nl.y - 1] : OUT,
				nl.x + 1 < Variables.TAILLE_CARTE_X ? hauteur[nl.x + 1][nl.y] : OUT,
				nl.y + 1 < Variables.TAILLE_CARTE_Y ? hauteur[nl.x][nl.y + 1] : OUT);

		l = nl;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vehicule) {
			Vehicule v = (Vehicule) obj;
			return v.numero == numero;
		} else if (obj instanceof Integer) {
			Integer v = (Integer) obj;
			return v == numero;
		}
		return false;
	}
}
