

import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.mas2j.AgentParameters;
import jason.mas2j.MAS2JProject;

import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import modele.CarteModel;
import modele.Variables;
import modele.percepts.AllPercepts;
import vue.FenetrePpale;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Environnement avec un Thread dédié pour le déplacement des
 *         adversaires
 */
public class Env extends Environment implements Runnable {

	private Logger logger = Logger.getLogger("project." + Env.class.getName());

	/**
	 * Le modèle haut niveau
	 */
	private CarteModel modele;

	/**
	 * Called before the MAS execution with the args informed in .mas2j
	 * 
	 * Parse le fichier .mas2j pour récupérer le nom/nombre des agents puis
	 * création du modèle et de la vue à partir de ces informations
	 * */
	@Override
	public void init(String[] args) {
		super.init(args);

		int nombreDrone = 0, nombreVehicule = 0;

		try {
			if (args.length == 0)
				throw new Error("Argument manquant");

			jason.mas2j.parser.mas2j parser = new jason.mas2j.parser.mas2j(new FileInputStream(args[0]));
			MAS2JProject project = parser.mas();

			for (AgentParameters ap : project.getAgents()) {
				if (ap.name.equals("d"))
					nombreDrone = ap.qty;
				else if (ap.name.equals("v"))
					nombreVehicule = ap.qty;
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Impossible de lire le fichier de configuration des agents");
			e.printStackTrace();
		}

		modele = new CarteModel(new AllPercepts(this), nombreVehicule, nombreDrone);
		FenetrePpale vue = new FenetrePpale(modele);
		modele.setView(vue);

		new Thread(this).start();
	}

	@Override
	public boolean executeAction(String agName, Structure action) {
		boolean valide = true;

		if(action.getFunctor().equals("deplacer"))
			modele.deplacer(agName, Integer.parseInt(action.getTerm(0).toString()));
		else valide = false;

		if (!valide)
			logger.info("executing: " + action + ", but not implemented!");

		try {
			Thread.sleep(Variables.VITESSE_ACTION);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return true;
	}

	/** Called before the end of MAS execution */
	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public void run() {
		while (this.isRunning()) {
			try {
				Thread.sleep(Variables.VITESSE_ACTION_ADVERSAIRE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			modele.deplaceAdversaire();
		}
	}
}
