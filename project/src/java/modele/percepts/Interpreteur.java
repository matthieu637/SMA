package modele.percepts;

import jason.asSyntax.Literal;
import jason.environment.Environment;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Permet aux modèles de communiquer des percepts à l'environnement plus
 *         facilement. Abstract uniquement pour un meilleur découpage.
 */
public abstract class Interpreteur {

	private Environment env;

	protected Interpreteur(Environment env) {
		this.env = env;
	}

	/**
	 * Ajoute un percept à tout les agent
	 * 
	 * @param string
	 */
	protected void ajouterTous(String string) {
		env.addPercept(Literal.parseLiteral(string));
	}

	/**
	 * Retire un percet à un agent
	 * 
	 * @param nomAgent
	 * @param percept
	 */
	protected void retirer(String nomAgent, String percept) {
		env.removePercept(nomAgent, Literal.parseLiteral(percept));
	}
	
	/**
	 * Retire un percept par unification
	 * 
	 * @param nomAgent
	 * @param percept
	 */
	protected void retirerUnif(String nomAgent, String percept){
		env.removePerceptsByUnif(nomAgent, Literal.parseLiteral(percept));
	}

	/**
	 * Ajoute un percept à un agent
	 * 
	 * @param nomAgent
	 * @param percept
	 */
	protected void ajouter(String nomAgent, String percept) {
		env.addPercept(nomAgent, Literal.parseLiteral(percept));
	}

	/**
	 * Tue un agent du système
	 * @param name
	 */
	protected void kill(final String name) {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1500);
					env.getEnvironmentInfraTier().getRuntimeServices().killAgent(name, null);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
