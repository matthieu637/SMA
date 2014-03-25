package modele.entite;

import jason.environment.grid.Location;
import modele.CarteModel;
import modele.Grille;

public class Ennemie extends Militaire {

	private double vision; // portee de la vision
	private double portee; // portee de tir
	private double imprecision; // tir imprécis: taille du bruit gaussian : si
								// imprecision = 1 -> un tir à 5 cases est dévié
								// en moyenne de 5 cases en x et y

	public Ennemie(Location l, Comportement c) {
		super(l, c, Grille.ADVERSAIRE_CODE);
		init();
	}

	public Ennemie(Location l, Location but) {
		super(l, but, Grille.ADVERSAIRE_CODE);
		init();
	}

	private void init() {
		this.vision = 10.;
		this.portee = 5.;
		this.imprecision = 0.3;
	}

	public double portee() {
		return this.portee;
	}

	public double vision() {
		return this.vision;
	}

	public double imprecision() {
		return this.imprecision;
	}

	@Override
	public void definirBut(CarteModel carteModel) {
		Location target = carteModel.find_target(getLocation());
		if (target != null && (l.distanceEuclidean(target) < vision) && (carteModel.visibilite(l, target) > generateur.nextDouble())) {
			// s'il y a une cible à portee de
			// vue et plutot visible, on s'en approche
			// et/ou on tire
			but = target;
		} else {
			super.definirBut(carteModel);
		}
	}

	@Override
	public void _agir(CarteModel carteModel) {
		if (but != null && l.distanceEuclidean(but) < portee()) { // on tire
			Location trou = tirer(but, generateur.nextGaussian(), generateur.nextGaussian());
			if (trou.isInArea(new Location(0, 0), new Location(carteModel.getTerrain().getWidth(), carteModel.getTerrain().getHeight())))
				carteModel.destruction(trou);
		}
	}

	/**
	 * tire un coup et renvoie le lieu touché
	 */
	public Location tirer(Location t, double rdx, double rdy) {

		double distance = t.distanceEuclidean(this.l);
		int tirx = (int) (t.x + Math.round(distance * this.imprecision * rdx));
		int tiry = (int) (t.y + Math.round(distance * this.imprecision * rdy));
		return new Location(tirx, tiry);
	}
}
