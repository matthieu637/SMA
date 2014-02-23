package vue;

import java.awt.Color;
import java.awt.Graphics;

import modele.TerrainModel;
import ext.GridWorldModelP;
import ext.GridWorldViewPanel;

public class TerrainView extends GridWorldViewPanel {

	private static final long serialVersionUID = 1L;

	public TerrainView(GridWorldModelP model, int windowSize) {
		super(model, windowSize);
	}

	@Override
	public void drawEmpty(Graphics g, int x, int y) {
		super.drawEmpty(g, x, y);

		Color c = g.getColor();
		if (!getModele().estBut(x, y)) {
			int h = 255 - getModele().getHauteur(x, y);
			g.setColor(new Color(h, h, h));
		} else
			g.setColor(Color.RED);

		g.fillRect(x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 1, cellSizeH - 1);
		g.setColor(c);
	}

	public TerrainModel getModele() {
		return (TerrainModel) getModel();
	}

}
