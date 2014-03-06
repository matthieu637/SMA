package vue;

import java.awt.Color;
import java.awt.Graphics;

import modele.CielModel;
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
	}

	@Override
	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
		super.drawEmpty(g, x, y);

		if (getModele().getDrone(id+1).isHaute_altitude())
			g.setColor(dark_green);
		else
			g.setColor(lawn_green);
		g.fillOval(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
	}

	@Override
	public void draw(Graphics g, int x, int y, int object) {
		Color c = g.getColor();
		g.setColor(Color.red);
		g.fillOval(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
		g.setColor(c);
	}

	public CielModel getModele() {
		return (CielModel) getModel();
	}
}
