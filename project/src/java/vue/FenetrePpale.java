package vue;

import java.awt.Container;
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
	private CielView ciel;

	public FenetrePpale(CarteModel modele) {
		setSize(1250, 750);

		Container c = getContentPane();
		c.setLayout(null);
		terrain = new TerrainView(modele.getTerrain(), 600, modele);
		terrain.setLocation(0, 30);
		c.add(terrain);

		ciel = new CielView(modele.getBasseAltitude(), 600);
		ciel.setLocation(625, 30);
		c.add(ciel);

		JLabel l = new JLabel("Vue Au Sol");
		l.setFont(defaultFont);
		l.setBounds(250, 5, 100, 20);
		c.add(l);

		l = new JLabel("Vue du Ciel");
		l.setFont(defaultFont);
		l.setBounds(870, 5, 150, 20);
		c.add(l);

		JCheckBox j = new JCheckBox("Cacher zones non découverte");
		j.setFont(defaultFont);
		j.setBounds(20, 600 + 20 + 20, 250, 20);
		j.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terrain.cacherNonDecouvert();
			}
		});
		c.add(j);

		JSlider v = new JSlider(1, 1000);
		v.setBounds(20, 600 + 20 + 20 + 20, 250, 20);
		v.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider s = (JSlider) e.getSource();
				Variables.getInstance().setVitesse(s.getValue());
			}
		});
		c.add(v);

		setVisible(true);
	}

	public TerrainView getTerrain() {
		return terrain;
	}

	public CielView getCiel() {
		return ciel;
	}
}
