package modele.entite;

import jason.environment.grid.Location;
import modele.Act;
import modele.Grille;
import modele.TimeLimit;
import modele.Variables;
import modele.percepts.AllPercepts;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Garde les informations du leader en mémoire pour mettre à jour les
 *         percepts
 */
public class Vehicule extends EntiteLocalisable implements Comparable<Integer> {

	private final int numero;

	private Location but;
	
	private boolean estLeader;

	private static final int OUT = 500;

	private Vehicule follower;

	private Act deplacement;

	public Vehicule(int numero, Location l, boolean leader, Vehicule follower) {
		super(l, Grille.AGENT);
		this.numero = numero;
		this.estLeader = leader;
		this.follower = follower;
		this.deplacement = new Act(new TimeLimit() {

			@Override
			public long getMax() {
				return (long) (Variables.getInstance().getVitesse() * Variables.VITESSE_DEPLACEMENT_VEHICULE);
			}
		});
	}

	public boolean canAct() {
		return deplacement.canAct();
	}

	public int getNumero() {
		return numero;
	}

	public boolean estLeader() {
		return estLeader;
	}

	public void deplacer(Location nl) {
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

	public void majPercept(AllPercepts interpreteur, int[][] hauteur) {
		interpreteur.retirerPositionVehicule(numero, numero);
		interpreteur.ajouterPositionVehicule(numero, numero, l.x, l.y);

		if (follower != null) {
			interpreteur.retirerFollow(follower.getNumero());
			interpreteur.ajouterFollow(follower.getNumero(), numero);
			
			interpreteur.retirerPositionVehicule(follower.getNumero(), numero);
			interpreteur.ajouterPositionVehicule(follower.getNumero(), numero, l.x, l.y);
		}

		if (estLeader) {
			interpreteur.retirerLeader(numero);
			interpreteur.ajouterLeader(numero);
			
			interpreteur.retirerHeightmap(numero);
			interpreteur.ajouterHeightmap(numero, l.x - 1 >= 0 ? hauteur[l.x - 1][l.y] : OUT, l.y - 1 >= 0 ? hauteur[l.x][l.y - 1] : OUT,
					l.x + 1 < Variables.TAILLE_CARTE_X ? hauteur[l.x + 1][l.y] : OUT,
					l.y + 1 < Variables.TAILLE_CARTE_Y ? hauteur[l.x][l.y + 1] : OUT);
		}
		
		if(but != null){
			interpreteur.retirerPositionButVehicule(numero);
			interpreteur.ajouterPositionButVehicule(numero, but.x, but.y);
		}
	}

	public void setBut(Location but) {
		this.but = but;
	}

	@Override
	public int compareTo(Integer o) {
		return Integer.compare(this.numero, o);
	}

	public Vehicule getFollower() {
		return follower;
	}

	public void setFollower(Vehicule devant) {
		follower = devant;
	}

	public void setLeader() {
		estLeader = true; 
	}

}
