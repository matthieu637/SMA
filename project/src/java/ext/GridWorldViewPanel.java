package ext;

import jason.environment.grid.GridWorldModel;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Adaptation de jason.environment.grid.GridWorldView en remplacant
 *         JFrame par JPanel pour en gérer plusieurs à la fois
 */
public class GridWorldViewPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	protected int cellSizeW = 0;
	protected int cellSizeH = 0;

	protected GridCanvas drawArea;
	protected GridWorldModelP model;

	protected static Font defaultFont = new Font("Arial", Font.BOLD, 10);

	public GridWorldViewPanel(GridWorldModelP model, int windowSize) {
		this.model = model;
		initComponents(windowSize);
		model.setView(this);
	}

	/** sets the size of the frame and adds the components */
	public void initComponents(int width) {
		setSize(width, width);
		setPreferredSize(new Dimension(width, width));
		setLayout(new BorderLayout());
		drawArea = new GridCanvas();
		add(BorderLayout.CENTER, drawArea);
	}

	@Override
	public void repaint() {
		if (drawArea != null && model != null) {
			cellSizeW = drawArea.getWidth() / model.getWidth();
			cellSizeH = drawArea.getHeight() / model.getHeight();
			super.repaint();
			drawArea.repaint();
		}
	}

	/** updates all the frame */
	public void update() {
		repaint();
	}

	/** updates only one position of the grid */
	public void update(int x, int y) {
		Graphics g = drawArea.getGraphics();
		if (g == null)
			return;
		// drawEmpty(g, x, y);
		draw(g, x, y);
	}

	public void drawObstacle(Graphics g, int x, int y) {
		g.setColor(Color.darkGray);
		g.fillRect(x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 1, cellSizeH - 1);
		g.setColor(Color.black);
		g.drawRect(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
	}

	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
		g.setColor(c);
		g.fillOval(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
		if (id >= 0) {
			g.setColor(Color.black);
			drawString(g, x, y, defaultFont, String.valueOf(id + 1));
		}
	}

	public void drawString(Graphics g, int x, int y, Font f, String s) {
		g.setFont(f);
		FontMetrics metrics = g.getFontMetrics();
		int width = metrics.stringWidth(s);
		int height = metrics.getHeight();
		g.drawString(s, x * cellSizeW + (cellSizeW / 2 - width / 2), y * cellSizeH + (cellSizeH / 2 + height / 2));
	}

	public void drawStringOffset(Graphics g, int x, int y, Font f, String s) {
		g.setFont(f);
		FontMetrics metrics = g.getFontMetrics();
		int width = metrics.stringWidth(s);
		int height = metrics.getHeight();
		g.drawString(s, x * cellSizeW + (cellSizeW / 2 - width / 2) - 1, y * cellSizeH + (cellSizeH / 2 + height / 2) - 2);
	}

	public void drawEmpty(Graphics g, int x, int y) {
		g.setColor(Color.white);
		g.fillRect(x * cellSizeW + 1, y * cellSizeH + 1, cellSizeW - 1, cellSizeH - 1);
		g.setColor(Color.lightGray);
		g.drawRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
	}

	/**
	 * method to draw unknown object, probably overridden by the user viewer
	 * class
	 */
	public void draw(Graphics g, int x, int y, int object) {
		// g.setColor(Color.black);
		// drawString(g,x,y,defaultFont,String.valueOf(object));
	}

	private static int limit = (int) Math.pow(2, 6);

	private void draw(Graphics g, int x, int y) {
		boolean drawSomething = false;

		if ((model.data[x][y] & GridWorldModel.OBSTACLE) != 0) {
			drawObstacle(g, x, y);
			drawSomething = true;
		}

		int vl = GridWorldModel.OBSTACLE * 2;
		while (vl < limit) {
			if ((model.data[x][y] & vl) != 0) {
				draw(g, x, y, vl);
				drawSomething = true;
			}
			vl *= 2;
		}

		if ((model.data[x][y] & GridWorldModel.AGENT) != 0) {
			drawAgent(drawArea.getGraphics(), x, y, Color.blue, model.getAgAtPos(x, y));
			drawSomething = true;
		}

		if (!drawSomething)
			drawEmpty(g, x, y);
	}

	public Canvas getCanvas() {
		return drawArea;
	}

	public GridWorldModelP getModel() {
		return model;
	}

	public class GridCanvas extends Canvas {

		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			cellSizeW = drawArea.getWidth() / model.getWidth();
			cellSizeH = drawArea.getHeight() / model.getHeight();
			int mwidth = model.getWidth();
			int mheight = model.getHeight();

			g.setColor(Color.lightGray);
			for (int l = 1; l <= mheight; l++) {
				g.drawLine(0, l * cellSizeH, mwidth * cellSizeW, l * cellSizeH);
			}
			for (int c = 1; c <= mwidth; c++) {
				g.drawLine(c * cellSizeW, 0, c * cellSizeW, mheight * cellSizeH);
			}

			for (int x = 0; x < mwidth; x++) {
				for (int y = 0; y < mheight; y++) {
					draw(g, x, y);
				}
			}
		}
	}

	public void update(int x, int y, boolean b) {
		Graphics g = drawArea.getGraphics();
		if (g == null)
			return;
		// drawEmpty(g, x, y);
		draw(g, x, y);

		if (b) {
			g.setColor(Color.BLACK);
			g.fillRect(x * cellSizeW + 3, y * cellSizeH + 3, cellSizeW - 6, cellSizeH - 6);
		}
	}
}
