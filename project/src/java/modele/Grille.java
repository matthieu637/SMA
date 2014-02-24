package modele;

import jason.environment.grid.Location;
import ext.GridWorldModelP;

public abstract class Grille extends GridWorldModelP {

	protected Grille(int w, int h, int nbAgs) {
		super(w, h, nbAgs);
		
		for (int a = 0; a < nbAgs; a++)
			setAgPos(a, 0, 0);
	}
	
	public void deplacer(int agent, int position) {
		Location l = getAgPos(agent);
		
		if (l == null)
			return;
		
		l = deplacer(l, position);
		
		setAgPos(agent, l);
	}
	
	public static Location deplacer(Location l, int position) {
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
