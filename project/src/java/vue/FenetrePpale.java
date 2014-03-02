package vue;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modele.CarteModel;
import modele.Variables;

public class FenetrePpale extends JFrame {

	private static final long serialVersionUID = 1L;

	protected static Font defaultFont = new Font("Arial", Font.PLAIN, 16);

	private TerrainView terrain;
	private BasseAltitudeView basse_altitude;
	private HauteAltitudeView haute_altitude;

	public FenetrePpale(CarteModel modele) {
		setSize(1870, 750);

		getContentPane().setLayout(null);
		terrain = new TerrainView(modele.getTerrain(), 600, modele);
		terrain.setLocation(0, 30);
		getContentPane().add(terrain);

		basse_altitude = new BasseAltitudeView(modele.getBasseAltitude(), 600);
		basse_altitude.setLocation(625, 30);
		getContentPane().add(basse_altitude);

		haute_altitude = new HauteAltitudeView(modele.getHauteAltitude(), 600);
		haute_altitude.setLocation(1250, 30);
		getContentPane().add(haute_altitude);

		JLabel l = new JLabel("Vue Au Sol");
		l.setFont(defaultFont);
		l.setBounds(250, 5, 100, 20);
		getContentPane().add(l);

		l = new JLabel("Vue Basse Altitude");
		l.setFont(defaultFont);
		l.setBounds(870, 5, 150, 20);
		getContentPane().add(l);

		l = new JLabel("Vue Haute Altitude");
		l.setFont(defaultFont);
		l.setBounds(1500, 5, 150, 20);
		getContentPane().add(l);

		JCheckBox j = new JCheckBox("Cacher zones non découverte");
		j.setFont(defaultFont);
		j.setBounds(20, 600 + 20 + 20, 250, 20);
		j.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terrain.cacherNonDecouvert();
			}
		});
		getContentPane().add(j);

		JSlider v = new JSlider(1, 1000);
		v.setBounds(20, 600 + 20 + 20 + 20, 250, 20);
		v.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider s = (JSlider) e.getSource();
				Variables.getInstance().setVitesse(s.getValue());
			}
		});
		getContentPane().add(v);

		setVisible(true);
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
