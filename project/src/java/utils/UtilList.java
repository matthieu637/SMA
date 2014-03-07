package utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class UtilList {

	/**
	 * Retourne la liste des mins.
	 * 
	 * @param <T>
	 * 
	 * @param coll
	 * @return
	 */
	public static <T> List<T> minList(List<T> coll, Comparator<? super T> comparator) {
		List<T> mins = new LinkedList<>();
		Collections.sort(coll, comparator);

		int i = -1;
		T current;
		do {
			i++;
			current = coll.get(i);
			mins.add(current);
		} while (i + 1 < coll.size() && comparator.compare(current, coll.get(i + 1)) == 0);

		return mins;
	}
}
