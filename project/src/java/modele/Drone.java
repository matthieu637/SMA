package modele;

public class Drone {

	private boolean haute_altitude;
	private int id;

	public Drone(int id, boolean haute_altitude) {
		this.setId(id);
		this.setHaute_altitude(haute_altitude);
	}

	public boolean isHaute_altitude() {
		return haute_altitude;
	}

	public void setHaute_altitude(boolean haute_altitude) {
		this.haute_altitude = haute_altitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
