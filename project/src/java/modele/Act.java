package modele;

import java.util.Random;

public class Act {
	private TimeLimit max;
	private long temps;

	private static Random generateur = new Random();
	
	public Act(TimeLimit max) {
		this.max = max;
		temps = System.currentTimeMillis();
	}

	public boolean canAct() {
		if ((System.currentTimeMillis() - temps) >= max.getMax() + generateur.nextInt(5)) {
			temps = System.currentTimeMillis();
			return true;
		}
		
		return false;
	}
}
