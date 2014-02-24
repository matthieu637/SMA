package modele;

import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import utils.Couple;
import ext.GridWorldModelP;

public class TerrainModel extends GridWorldModelP {

	private static final int ADVERSAIRE_CODE = 8;

	private int hauteur[][];
	private Random generateur = new Random();
	private Couple<Integer, Integer> but;
	private List<Adversaire> adversaire;

	protected TerrainModel(int nbAgent) {
		super(Constantes.TAILLE_CARTE_X, Constantes.TAILLE_CARTE_Y, nbAgent);

		construire_fractale();

		smooth_carte();

		but = definir_position_but();

		for (int a = 0; a < nbAgent; a++)
			setAgPos(a, 0, 0);
		
		adversaire = new LinkedList<>();
	}

	private int par_pixel(float valeur, int n_per_level, float random) {
		double p_per_pixel = (Math.pow(n_per_level, Constantes.FRACTALE_DIMENSION)) / (Math.pow(n_per_level, 2));
		double p = p_per_pixel * (1 - valeur / 255.);
		// System.out.println(p+" "+random);
		if (random < p)
			return 0;
		else
			return 255;
	}

	private void construire_fractale() {
		hauteur = new int[Constantes.TAILLE_CARTE_X][Constantes.TAILLE_CARTE_Y];

		for (int i = 0; i < Constantes.TAILLE_CARTE_X; i++)
			for (int j = 0; j < Constantes.TAILLE_CARTE_Y; j++)
				hauteur[i][j] = par_pixel(hauteur[i][j], Constantes.FRACTALE_N_LEVEL, generateur.nextFloat());
	}

	private void smooth_carte() {
		List<Couple<Integer, Integer>> index = new ArrayList<Couple<Integer, Integer>>(Constantes.TAILLE_CARTE_X
				* Constantes.TAILLE_CARTE_Y);
		for (int i = 0; i < Constantes.TAILLE_CARTE_X; i++)
			for (int j = 0; j < Constantes.TAILLE_CARTE_Y; j++)
				index.add(new Couple<Integer, Integer>(i, j));

		for (int n = 0; n < Constantes.SMOOTH_CARTE; n++) {
			Collections.shuffle(index);

			for (Couple<Integer, Integer> c : index) {
				int i = c.first;
				int j = c.second;
				int sum = 0;

				if (i + 1 < Constantes.TAILLE_CARTE_X)
					sum += hauteur[i + 1][j];
				if (j + 1 < Constantes.TAILLE_CARTE_Y)
					sum += hauteur[i][j + 1];
				if (i - 1 > 0)
					sum += hauteur[i - 1][j];
				if (j - 1 > 0)
					sum += hauteur[i][j - 1];
				hauteur[i][j] = sum / 4;
			}
		}
	}

	private Couple<Integer, Integer> definir_position_but() {
		int x = (int) (Constantes.TAILLE_CARTE_X * (1. / 2.)) + random.nextInt((int) (Constantes.TAILLE_CARTE_X / 3.));
		int y = (int) (Constantes.TAILLE_CARTE_Y * (1. / 2.)) + random.nextInt((int) (Constantes.TAILLE_CARTE_Y / 3.));

		return new Couple<Integer, Integer>(x, y);
	}

	public int getHauteur(int x, int y) {
		return hauteur[x][y];
	}

	public boolean estBut(int x, int y) {
		return but.first == x && but.second == y;
	}

	public void deplacer(int agent, int position) {
		Location l = getAgPos(agent);
		if (l == null)
			return;
		
		l = deplacer(l, position);
		
		setAgPos(agent, l);
	}

	public void ajouterAgentAdverse(int x, int y) {
		add(ADVERSAIRE_CODE, x, y);
		adversaire.add(new Adversaire(x, y));
	}

	public void deplaceAdversaire() {
		for (Adversaire a : adversaire) {
			remove(ADVERSAIRE_CODE, a.getLocation());
			Location l = a.getLocation();
			deplacer(l, generateur.nextInt(4));
			a.setLocation(l);
			add(ADVERSAIRE_CODE, a.getLocation());
		}
	}

	private Location deplacer(Location l, int position) {
		switch (position) {
		case 0:
			if (l.x - 1 >= 0)
				l.x--;
			break;
		case 1:
			if (l.y - 1 >= 0)
				l.y--;
			break;
		case 2:
			if (l.x + 1 < Constantes.TAILLE_CARTE_X)
				l.x++;
			break;
		case 3:
			if (l.y + 1 < Constantes.TAILLE_CARTE_Y)
				l.y++;
			break;

		default:
			System.out.println("Error");
			break;
		}
		return l;
	}

}
