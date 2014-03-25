package modele.entite;

import jason.environment.grid.Location;
import modele.CarteModel;
import modele.percepts.AllPercepts;

public abstract class EntiteComportement extends EntiteDeplacable {

	public EntiteComportement(Location l, Comportement c, int code) {
		super(l, c, code);
	}

	public EntiteComportement(Location l, Location but, int code) {
		super(l, but, code);
	}

	public abstract void majPercept(AllPercepts interpreteur);

	protected abstract void _agir(CarteModel carteModel);
	
	public void agir(CarteModel carteModel) {
		_agir(carteModel);
		if (c == Comportement.But && but != null && getLocation().equals(but))
			carteModel.detruireAgentSupplementaire(this);
	}

}
