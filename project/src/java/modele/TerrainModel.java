package modele;
import ext.GridWorldModelP;


public class TerrainModel extends GridWorldModelP{

	protected TerrainModel(int nbAgent) {
		super(Constantes.TAILLE_CARTE_X, Constantes.TAILLE_CARTE_Y, nbAgent);
	}

}
