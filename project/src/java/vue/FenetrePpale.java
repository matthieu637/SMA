package vue;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
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
	private CarteModel modele;

	public FenetrePpale(CarteModel modele) {
		setSize(1250, 780);

		Container c = getContentPane();
		c.setLayout(null);
		terrain = new TerrainView(modele.getTerrain(), 600, this);
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
		j.setBounds(20, 600 + 20 + 20, 270, 20);
		j.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terrain.cacherNonDecouvert();
			}
		});
		c.add(j);

		JSlider v = new JSlider(1, 1000);
		v.setBounds(20, 600 + 20 + 20 + 30, 250, 20);
		v.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider s = (JSlider) e.getSource();
				Variables.getInstance().setVitesse(s.getValue());
			}
		});
		c.add(v);

		JRadioButton type1 = new JRadioButton("Militaire Ennemi");
		type1.setBounds(20, 600 + 20 + 30 + 40, 150, 25);
		c.add(type1);

		JRadioButton type2 = new JRadioButton("Militaire Allié");
		type2.setBounds(20 + 150, 600 + 20 + 30 + 40, 130, 25);
		c.add(type2);

		JRadioButton type3 = new JRadioButton("Civil");
		type3.setEnabled(true);
		type3.setBounds(20 + 150 + 130, 600 + 20 + 30 + 40, 150, 25);
		c.add(type3);

		ButtonGroup types = new ButtonGroup();
		types.add(type1);
		types.add(type2);
		types.add(type3);

		JRadioButton fixe = new JRadioButton("Fixe");
		fixe.setBounds(20, 600 + 20 + 30 + 40 + 25, 60, 25);
		c.add(fixe);

		JRadioButton aleatoire = new JRadioButton("Aleatoire");
		aleatoire.setBounds(20 + 60, 600 + 20 + 30 + 40 + 25, 100, 25);
		aleatoire.setEnabled(true);
		c.add(aleatoire);

		JRadioButton but = new JRadioButton("But");
		but.setBounds(20 + 60 + 100, 600 + 20 + 30 + 40 + 25, 80, 25);
		c.add(but);

		ButtonGroup comportements = new ButtonGroup();
		comportements.add(fixe);
		comportements.add(aleatoire);
		comportements.add(but);

		setVisible(true);
		this.modele = modele;
	}

	public TerrainView getTerrain() {
		return terrain;
	}

	public CielView getCiel() {
		return ciel;
	}

	public void ajouterAgent(int x, int y) {
		modele.ajouterAgentAdverse(x, y);
	}
}
