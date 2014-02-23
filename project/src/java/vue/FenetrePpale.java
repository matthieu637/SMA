package vue;

import javax.swing.JFrame;

import modele.CarteModel;

public class FenetrePpale extends JFrame {

	private static final long serialVersionUID = 1L;

	private TerrainView terrain;
	private BasseAltitudeView basse_altitude;
	private HauteAltitudeView haute_altitude;

	public FenetrePpale(CarteModel modele) {
		setSize(1870, 750);
		setVisible(true);

		getContentPane().setLayout(null);
		terrain = new TerrainView(modele.getTerrain(), 600);
		terrain.setLocation(0, 3);
		getContentPane().add(terrain);

		basse_altitude = new BasseAltitudeView(modele.getBasseAltitude(), 600);
		basse_altitude.setLocation(625, 3);
		getContentPane().add(basse_altitude);

		haute_altitude = new HauteAltitudeView(modele.getHauteAltitude(), 600);
		haute_altitude.setLocation(1250, 3);
		getContentPane().add(haute_altitude);
	}

	public TerrainView getTerrain() {
		return terrain;
	}

	public BasseAltitudeView getBasse_altitude() {
		return basse_altitude;
	}

	public HauteAltitudeView getHaute_altitude() {
		return haute_altitude;
	}
}
