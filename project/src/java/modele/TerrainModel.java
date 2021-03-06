package modele;

import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import modele.entite.Convoi;
import modele.entite.Vehicule;
import modele.percepts.AllPercepts;
import utils.Couple;
import utils.PathBuilder;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Modèle des véhicule au sol
 * 
 */
public class TerrainModel extends Grille {

	/**
	 * Les hauteurs de la grille
	 */
	private int hauteur[][];
	/**
	 * Position du but à atteindre par le convoi
	 */
	private Location but;

	private Random generateur = new Random();

	private Convoi convoi;

	protected TerrainModel(int nbAgent, AllPercepts interpreteur) {
		super(Variables.TAILLE_CARTE_X, Variables.TAILLE_CARTE_Y, nbAgent, interpreteur, true);

		construire_fractale();

		smooth_carte();

		but = definir_position_but();

		// percept initiaux
		convoi = new Convoi(nbAgent, getAgPos(nbAgent - 1), interpreteur, but, hauteur);
	}

	/**
	 * Fractale pour une case
	 * 
	 * @param valeur
	 * @param n_per_level
	 * @param random
	 * @return
	 */
	private int par_pixel(float valeur, int n_per_level, float random) {
		double p_per_pixel = (Math.pow(n_per_level, Variables.FRACTALE_DIMENSION)) / (Math.pow(n_per_level, 2));
		double p = p_per_pixel * (1 - valeur / 255.);

		if (random < p)
			return 0;
		else
			return 255;
	}

	/**
	 * Construit la hauteur pour chaque case
	 */
	private void construire_fractale() {
		hauteur = new int[Variables.TAILLE_CARTE_X][Variables.TAILLE_CARTE_Y];

		for (int i = 0; i < Variables.TAILLE_CARTE_X; i++)
			for (int j = 0; j < Variables.TAILLE_CARTE_Y; j++)
				hauteur[i][j] = par_pixel(hauteur[i][j], Variables.FRACTALE_N_LEVEL, generateur.nextFloat());
	}

	/**
	 * Regarde les voisins de chaque case tirée aléatoirement pour ajuster son
	 * niveau. Plus les voisins sont haut, plus la case est haute.
	 */
	private void smooth_carte() {
		List<Couple<Integer, Integer>> index = new ArrayList<Couple<Integer, Integer>>(Variables.TAILLE_CARTE_X * Variables.TAILLE_CARTE_Y);
		for (int i = 0; i < Variables.TAILLE_CARTE_X; i++)
			for (int j = 0; j < Variables.TAILLE_CARTE_Y; j++)
				index.add(new Couple<Integer, Integer>(i, j));

		for (int n = 0; n < Variables.SMOOTH_CARTE; n++) {
			Collections.shuffle(index);

			for (Couple<Integer, Integer> c : index) {
				int i = c.first;
				int j = c.second;
				int sum = 0;

				if (i + 1 < Variables.TAILLE_CARTE_X)
					sum += hauteur[i + 1][j];
				else
					sum += Variables.HAUTEUR_HORS_CARTE;

				if (j + 1 < Variables.TAILLE_CARTE_Y)
					sum += hauteur[i][j + 1];
				else
					sum += Variables.HAUTEUR_HORS_CARTE;

				if (i - 1 > 0)
					sum += hauteur[i - 1][j];
				else
					sum += Variables.HAUTEUR_HORS_CARTE;

				if (j - 1 > 0)
					sum += hauteur[i][j - 1];
				else
					sum += Variables.HAUTEUR_HORS_CARTE;
				hauteur[i][j] = sum / 4;
			}
		}
	}

	/**
	 * Défini un but dans la zone en bas à droite de la carte
	 * 
	 * @return
	 */
	private Location definir_position_but() {
		int x = (int) (Variables.TAILLE_CARTE_X * (1. / 2.)) + random.nextInt((int) (Variables.TAILLE_CARTE_X / 3.));
		int y = (int) (Variables.TAILLE_CARTE_Y * (1. / 2.)) + random.nextInt((int) (Variables.TAILLE_CARTE_Y / 3.));

		return new Location(x, y);
	}

	public int getHauteur(int x, int y) {
		return hauteur[x][y];
	}

	public boolean estBut(int x, int y) {
		return but.x == x && but.y == y;
	}

	@Override
	public Location deplacer(int agent, int direction) {
		Location l = super.deplacer(agent - 1, direction);

		convoi.majPercepts(agent, l);

		return l;
	}

	@Override
	public Couple<Location, Boolean> deplacerCollision(int agent, int direction) {
		Couple<Location, Boolean> c = null;
		if (convoi.canAct(agent)) {
			c = super.deplacerCollision(agent - 1, direction);
			convoi.majPercepts(agent, c.first);
		} else
			c = new Couple<Location, Boolean>(null, false);

		if (c.second && convoi.getVehicule(agent).butAtteint()) {
			if (retirerAgent(c.first, false, null)) {
				System.out.println("Congratz!");
				interpreteur.killVehicule(agent);
			}
		}

		return c;
	}

	public boolean retirerAgent(Location t, boolean killed, Location from) {
		int pos = getAgAtPos(t);
		if (killed)
			animate(from, t, convoi.getVehicule(pos + 1));
		if (convoi.remove(pos + 1, killed)) {
			removeAgent(pos);
			return true;
		}
		return false;
	}

	private void animate(final Location t, final Location goal, final Vehicule a) {
		new Thread() {
			public void run() {
				try {
					PathBuilder pb = new PathBuilder(t, goal);
					for (Location l : pb.getPath()) {
						view.update(l.x, l.y, true);
						Thread.sleep(150);
						view.update(l.x, l.y, false);
						Thread.sleep(150);
					}
					for (int i = 0; i < 4; i++) {
						remove(Grille.AGENT, a.getLocation());
						Thread.sleep(150);
						add(Grille.AGENT, a.getLocation());
						Thread.sleep(150);
					}
					remove(Grille.AGENT, a.getLocation());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	public boolean scinder(int agent) {
		return convoi.scinder(agent);
	}

	public int[][] getHauteurs() {
		return hauteur;
	}

	public Convoi getConvoi() {
		return convoi;
	}

	public boolean convoiArrive() {
		return convoi.arriver();
	}
}
