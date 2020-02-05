package Main;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RoundedJButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	RoundedJButton(String label) {
		super(label);
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width, size.height);
		setPreferredSize(size);

		setContentAreaFilled(false);
	}

	RoundedJButton(ImageIcon img) {
		super(img);
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width, size.height);
		setPreferredSize(size);

		setContentAreaFilled(false);
	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		if (getModel().isArmed()) {
			g.setColor(Defaults.HOVER);
		} else {
			g.setColor(getBackground());
		}
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(qualityHints);
		g2.fillOval(0, 0, getSize().width, getSize().height);


		super.paintComponent(g);
	}

	/*
	 * protected void paintBorder(Graphics g) { g.setColor(getForeground());
	 * g.drawOval(0, 0, getSize().width-1, getSize().height-1); }
	 */

	private Shape shape;

	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}


		return shape.contains(x, y);
	}
}
