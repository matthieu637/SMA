package modele;

import jason.asSyntax.Literal;
import jason.environment.Environment;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Permet aux modèles de communiquer des percepts à l'environnement plus
 *         facilement
 */
public class Interpreteur {

	private Environment env;

	public Interpreteur(Environment env) {
		this.env = env;
	}

	public void ajouterTous(String string) {
		env.addPercept(Literal.parseLiteral(string));
	}

	public void retirer(String nomAgent, String percept) {
		env.removePercept(nomAgent, Literal.parseLiteral(percept));
	}

	public void ajouter(String nomAgent, String percept) {
		env.addPercept(nomAgent, Literal.parseLiteral(percept));
	}

}
