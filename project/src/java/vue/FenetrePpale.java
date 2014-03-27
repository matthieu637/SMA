package vue;

import jason.environment.grid.Location;

import java.awt.Container;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modele.CarteModel;
import modele.Variables;
import modele.entite.Civil;
import modele.entite.Comportement;
import modele.entite.Ennemi;
import modele.entite.Militaire;

public class FenetrePpale extends JFrame {

	private static final long serialVersionUID = 1L;

	protected static Font defaultFont = new Font("Arial", Font.PLAIN, 16);

	private TerrainView terrain;
	private CielView ciel;
	private CarteModel modele;

	private JRadioButton type1, type2, type3, fixe, aleatoire, but;
	private Location entite;

	public FenetrePpale(CarteModel modele) {
		setSize(1250, Variables.TAILLE_GRILLE + 180);

		Container c = getContentPane();
		c.setLayout(null);
		terrain = new TerrainView(modele.getTerrain(), Variables.TAILLE_GRILLE, this);
		terrain.setLocation(0, 30);
		c.add(terrain);

		ciel = new CielView(modele.getBasseAltitude(), Variables.TAILLE_GRILLE);
		ciel.setLocation(Variables.TAILLE_GRILLE + 25, 30);
		c.add(ciel);

		JLabel l = new JLabel("Vue Au Sol");
		l.setFont(defaultFont);
		l.setBounds(250, 5, 100, 20);
		c.add(l);

		l = new JLabel("Vue du Ciel");
		l.setFont(defaultFont);
		l.setBounds(Variables.TAILLE_GRILLE + 270, 5, 150, 20);
		c.add(l);

//		JCheckBox j = new JCheckBox("Cacher zones non découverte");
//		j.setFont(defaultFont);
//		j.setBounds(20, 600 + 20 + 20, 270, 20);
//		j.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				terrain.cacherNonDecouvert();
//			}
//		});
//		c.add(j);
		JLabel j = new JLabel("Vitesse :");
		j.setBounds(20, Variables.TAILLE_GRILLE + 20 + 20, 270, 20);
		c.add(j);

		JSlider v = new JSlider(1, 500);
		v.setBounds(20, Variables.TAILLE_GRILLE + 20 + 20 + 30, 250, 20);
		v.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider s = (JSlider) e.getSource();
				Variables.getInstance().setVitesse(s.getValue());
			}
		});
		c.add(v);

		type1 = new JRadioButton("Militaire Ennemi");
		type1.setBounds(20, Variables.TAILLE_GRILLE + 20 + 30 + 40, 150, 25);
		c.add(type1);

		type2 = new JRadioButton("Militaire Allié");
		type2.setBounds(20 + 150, Variables.TAILLE_GRILLE + 20 + 30 + 40, 130, 25);
		c.add(type2);

		type3 = new JRadioButton("Civil");
		type3.setBounds(20 + 150 + 130, Variables.TAILLE_GRILLE + 20 + 30 + 40, 150, 25);
		c.add(type3);

		ButtonGroup types = new ButtonGroup();
		types.add(type1);
		types.add(type2);
		types.add(type3);
		types.setSelected(type3.getModel(), true);

		fixe = new JRadioButton("Fixe");
		fixe.setBounds(20, Variables.TAILLE_GRILLE + 20 + 30 + 40 + 25, 60, 25);
		c.add(fixe);

		aleatoire = new JRadioButton("Aleatoire");
		aleatoire.setBounds(20 + 60, Variables.TAILLE_GRILLE + 20 + 30 + 40 + 25, 100, 25);
		c.add(aleatoire);

		but = new JRadioButton("But");
		but.setBounds(20 + 60 + 100, Variables.TAILLE_GRILLE + 20 + 30 + 40 + 25, 80, 25);
		c.add(but);

		ButtonGroup comportements = new ButtonGroup();
		comportements.add(fixe);
		comportements.add(aleatoire);
		comportements.add(but);
		comportements.setSelected(aleatoire.getModel(), true);

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
		Location l = new Location(x, y);
		if(!modele.getTerrain().inGrid(l))//clic dehors
			return;
		if(!modele.getTerrain().isFree(l)){
			System.out.println("Already something here !");
			return ;
		}

		Comportement c = parseComportement();

		if (!but.isSelected()) {
			if (type1.isSelected())
				modele.ajouterAgent(new Ennemi(l, c));
			else if (type2.isSelected())
				modele.ajouterAgent(new Militaire(l, c));
			else if (type3.isSelected())
				modele.ajouterAgent(new Civil(l, c));
			entite = null;
		} else {
			if (entite == null) {
				entite = l;
			} else {
				Location but = l;
				if (type1.isSelected())
					modele.ajouterAgent(new Ennemi(entite, but));
				else if (type2.isSelected())
					modele.ajouterAgent(new Militaire(entite, but));
				else if (type3.isSelected())
					modele.ajouterAgent(new Civil(entite, but));
				
				entite = null;
			}
		}
	}

	private Comportement parseComportement() {
		if (aleatoire.isSelected())
			return Comportement.DeplaceAleatoire;
		if (fixe.isSelected())
			return Comportement.Fixe;
		if (but.isSelected())
			return Comportement.But;
		throw new Error("impossible");
	}
}
