package vue;

import java.awt.Color;
import java.awt.Graphics;

import ext.GridWorldModelP;
import ext.GridWorldViewPanel;

public class HauteAltitudeView extends GridWorldViewPanel {

	private static final long serialVersionUID = 1L;
	private Color azur_clair = new Color(116, 208, 241);

	public HauteAltitudeView(GridWorldModelP model, int windowSize) {
		super(model, windowSize);
	}

	@Override
	public void drawEmpty(Graphics g, int x, int y) {
		super.drawEmpty(g, x, y);

		Color c = g.getColor();
		g.setColor(azur_clair);
		g.fillRect(x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 1, cellSizeH - 1);
		g.setColor(c);
	}
}
