package vue;

import java.awt.Color;
import java.awt.Graphics;

import ext.GridWorldModelP;
import ext.GridWorldViewPanel;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Vue des drônes en basse altitude
 */
public class BasseAltitudeView extends GridWorldViewPanel {

	private static final long serialVersionUID = 1L;
	private Color azurin = new Color(169, 234, 254);

	public BasseAltitudeView(GridWorldModelP model, int windowSize) {
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

		g.setColor(Color.GREEN);
		g.fillOval(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
	}

	@Override
	public void draw(Graphics g, int x, int y, int object) {
		Color c = g.getColor();
		g.setColor(Color.red);
		g.fillOval(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
		g.setColor(c);
	}
}
