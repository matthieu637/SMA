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

	public void initLeaderPercept(AllPercepts interpreteur, Location but, int[][] hauteur) {
		interpreteur.ajouterLeader(numero);
		interpreteur.ajouterPositionVehicule(numero, numero, l.x, l.y);
		interpreteur.ajouterPositionButVehicule(numero, but.x, but.y);
		interpreteur.ajouterHeightmap(numero, l.x - 1 >= 0 ? hauteur[l.x - 1][l.y] : OUT, l.y - 1 >= 0 ? hauteur[l.x][l.y - 1] : OUT,
				l.x + 1 < Variables.TAILLE_CARTE_X ? hauteur[l.x + 1][l.y] : OUT, l.y + 1 < Variables.TAILLE_CARTE_Y ? hauteur[l.x][l.y + 1] : OUT);
	}

	public void deplacer(Location nl) {
		l = nl;
	}

	public void majPercept(AllPercepts interpreteur, int[][] hauteur) {
		interpreteur.retirerPositionVehicule(numero, numero);
		interpreteur.ajouterPositionVehicule(numero, numero, l.x, l.y);

		interpreteur.retirerHeightmap(numero);
		interpreteur.ajouterHeightmap(numero, l.x - 1 >= 0 ? hauteur[l.x - 1][l.y] : OUT, l.y - 1 >= 0 ? hauteur[l.x][l.y - 1] : OUT,
				l.x + 1 < Variables.TAILLE_CARTE_X ? hauteur[l.x + 1][l.y] : OUT, l.y + 1 < Variables.TAILLE_CARTE_Y ? hauteur[l.x][l.y + 1] : OUT);
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
