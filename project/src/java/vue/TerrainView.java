package vue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import modele.CarteModel;
import modele.TerrainModel;
import ext.GridWorldModelP;
import ext.GridWorldViewPanel;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Vue au sol du convoi et du but. On peut ajouter des adversaires en
 *         cliquant sur cette vue.
 */
public class TerrainView extends GridWorldViewPanel {

	private static final long serialVersionUID = 1L;

	private boolean cache = false;

	public TerrainView(GridWorldModelP model, int windowSize, final CarteModel carte) {
		super(model, windowSize);

		drawArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int x = e.getX() / cellSizeW;
				int y = e.getY() / cellSizeH;
				carte.ajouterAgentAdverse(x, y);
			}
		});

	}

	@Override
	public void drawEmpty(Graphics g, int x, int y) {
		super.drawEmpty(g, x, y);

		Color c = g.getColor();
		if (getModele().estBut(x, y)) {
			g.setColor(Color.YELLOW);
		} else if (!cache) {
			int h = 255 - getModele().getHauteur(x, y);
			g.setColor(new Color(h, h, h));
		} else {
			g.setColor(Color.black);
		}

		g.fillRect(x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 1, cellSizeH - 1);
		g.setColor(c);
	}

	@Override
	public void draw(Graphics g, int x, int y, int object) {
		Color c = g.getColor();
		g.setColor(Color.red);
		g.fillOval(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
		g.setColor(c);
	}

	@Override
	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
		g.setColor(Color.BLUE);
		g.fillOval(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
	}

	public TerrainModel getModele() {
		return (TerrainModel) getModel();
	}

	public void cacherNonDecouvert() {
		cache = !cache;
		update();
	}

}
