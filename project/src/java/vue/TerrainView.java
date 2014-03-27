package vue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import modele.Grille;
import modele.TerrainModel;
import ext.GridWorldModelP;
import ext.GridWorldViewPanel;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Vue au sol du convoi et du but. On peut ajouter des adversaires en
 *         cliquant sur cette vue.
 */
public class TerrainView extends GridWorldViewPanel{

	private static final long serialVersionUID = 1L;

	private boolean cache = false;

	public TerrainView(GridWorldModelP model, int windowSize, final FenetrePpale ppale) {
		super(model, windowSize);

		drawArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int x = e.getX() / cellSizeW;
				int y = e.getY() / cellSizeH;
				ppale.ajouterAgent(x, y);
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
		
		if(x < 3 && y == 0){
			g.setColor(Color.white);
			super.drawStringOffset(g, x, y, defaultFont, "P");
			g.setColor(c);
		}
	}

	@Override
	public void draw(Graphics g, int x, int y, int object) {
		Color c = g.getColor();
		if((object & Grille.ADVERSAIRE_CODE) != 0)
			g.setColor(Color.red);
		else if((object & Grille.ALLIE_CODE) != 0)
			g.setColor(Color.MAGENTA);
		else if((object & Grille.CIVIL_CODE) != 0)
			g.setColor(Color.PINK);
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
