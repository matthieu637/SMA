package vue;

import jason.environment.grid.Location;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import modele.CielModel;
import modele.Grille;
import modele.entite.Drone;
import ext.GridWorldModelP;
import ext.GridWorldViewPanel;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Vue des drônes en basse altitude
 */
public class CielView extends GridWorldViewPanel {

	private static final long serialVersionUID = 1L;
	private static final Color azurin = new Color(169, 234, 254);

	private static final Color lawn_green = new Color(124, 252, 0);

	private static final Color dark_green = new Color(0, 100, 0);

	public CielView(GridWorldModelP model, int windowSize) {
		super(model, windowSize);
	}

	@Override
	public void drawEmpty(Graphics g, int x, int y) {
		super.drawEmpty(g, x, y);

		Color c = g.getColor();
		g.setColor(azurin);
		g.fillRect(x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 1, cellSizeH - 1);
		g.setColor(c);

		if (x < 3 && y == 0) {
			g.setColor(Color.white);
			super.drawStringOffset(g, x, y, defaultFont, "P");
			g.setColor(c);
		}

		int color = getModele().getZone(x, y);
		if (color > 0) {
			float alpha = 0.10f;
			int type = AlphaComposite.SRC_OVER;
			AlphaComposite composite = AlphaComposite.getInstance(type, alpha);
			Graphics2D g2d = (Graphics2D) g;
			Composite originalComposite = g2d.getComposite();
			g2d.setComposite(composite);
			g.setColor(lawn_green);
			g.fillRect(x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 1, cellSizeH - 1);

			g2d.setComposite(originalComposite);
		}
	}

	@Override
	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
		super.drawEmpty(g, x, y);

		Drone d = getModele().getDrone(id + 1);

		if (d.isHaute_altitude())
			g.setColor(dark_green);
		else
			g.setColor(lawn_green);

		g.fillOval(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
	}

	@Override
	public void draw(Graphics g, int x, int y, int object) {
		Color c = g.getColor();
		if ((object & Grille.ADVERSAIRE_CODE) != 0)
			g.setColor(Color.red);
		else if ((object & Grille.ALLIE_CODE) != 0)
			g.setColor(Color.green);
		else if ((object & Grille.CIVIL_CODE) != 0)
			g.setColor(Color.PINK);
		g.fillOval(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
		g.setColor(c);
	}

	public CielModel getModele() {
		return (CielModel) getModel();
	}

}
