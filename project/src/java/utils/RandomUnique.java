package utils;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Matthieu Zimmer
 * 
 *         Générateur aléatoire sans doublon
 * 
 * @param <T>
 */
public abstract class RandomUnique<T> {

	private static final Random GENERATEUR = new Random();
	private static final int NOMBRE_LIMIT_BOUCLE = 50;

	private final List<T> dejaEnregistre = new LinkedList<T>();

	public T creer() {
		int nombreDeBoucle = 0;

		while (nombreDeBoucle < NOMBRE_LIMIT_BOUCLE) {
			T object = creationGenerique();

			if (!dejaEnregistre.contains(object)) {
				dejaEnregistre.add(object);
				return object;
			}
		}

		if (nombreDeBoucle >= NOMBRE_LIMIT_BOUCLE)
			throw new Error(RandomUnique.class.getName() + " Boucle infinie");
		return null;
	}

	protected abstract T creationGenerique();

	public static Random getGenerateur() {
		return GENERATEUR;
	}

	public void reset() {
		dejaEnregistre.clear();
	}

	public void update(T a, T b) {
		dejaEnregistre.remove((Object) a);
		dejaEnregistre.add(b);
	}

	public void remove(T a) {
		dejaEnregistre.remove((Object) a);
	}

	public void display() {
		for (T a : dejaEnregistre)
			System.out.println(a);
	}

	public T creer(T o) {
		dejaEnregistre.add(o);
		return o;
	}
}
