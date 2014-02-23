package vue;

import java.awt.Color;
import java.awt.Graphics;

import modele.TerrainModel;
import ext.GridWorldModelP;
import ext.GridWorldViewPanel;

public class TerrainView extends GridWorldViewPanel {

	private static final long serialVersionUID = 1L;

	private boolean cache = false;

	public TerrainView(GridWorldModelP model, int windowSize) {
		super(model, windowSize);
	}

	@Override
	public void drawEmpty(Graphics g, int x, int y) {
		super.drawEmpty(g, x, y);

		Color c = g.getColor();
		if (getModele().estBut(x, y)) {
			g.setColor(Color.RED);
		} else if(!cache) {
			int h = 255 - getModele().getHauteur(x, y);
			g.setColor(new Color(h, h, h));
		} else {
			g.setColor(Color.black);
		}

		g.fillRect(x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 1, cellSizeH - 1);
		g.setColor(c);
	}

	public TerrainModel getModele() {
		return (TerrainModel) getModel();
	}

	public void cacherNonDecouvert() {
		cache = !cache;
		update();
	}

}
