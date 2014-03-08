package modele;

public class Act {
	private TimeLimit max;
	private long temps;

	public Act(TimeLimit max) {
		this.max = max;
		temps = System.currentTimeMillis();
	}

	public boolean canAct() {
		if ((System.currentTimeMillis() - temps) >= max.getMax()) {
			temps = System.currentTimeMillis();
			return true;
		}
		
		return false;
	}
}
