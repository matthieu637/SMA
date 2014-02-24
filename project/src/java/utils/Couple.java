package utils;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Permet de faciliter quelques écritures. Similaire à std::pair
 * 
 * @param <E>
 * @param <K>
 */
public class Couple<E, K> {

	public E first;
	public K second;

	public Couple(E first, K second) {
		super();
		this.first = first;
		this.second = second;
	}

}
