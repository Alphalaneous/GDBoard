package Main;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.metal.MetalScrollBarUI;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ScrollbarUI extends BasicScrollBarUI {


		private Image imageThumb, imageTrack;
		private JButton b = new JButton() {

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, 0);
			}

		};

	public ScrollbarUI() {
			imageThumb = FauxImage.create(32, 32, Defaults.BUTTON_HOVER);
			imageTrack = FauxImage.create(32, 32, Defaults.BUTTON);
		}

		@Override
		protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
			g.setColor(Defaults.BUTTON_HOVER);
			((Graphics2D) g).drawImage(imageThumb,
					r.x, r.y, r.width, r.height, null);
		}

		@Override
		protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
			((Graphics2D) g).drawImage(imageTrack,
					r.x, r.y, r.width, r.height, null);
		}

		@Override
		protected JButton createDecreaseButton(int orientation) {
			return b;
		}

		@Override
		protected JButton createIncreaseButton(int orientation) {
			return b;
		}


	private static class FauxImage {

		static public Image create(int w, int h, Color c) {
			BufferedImage bi = new BufferedImage(
					w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = bi.createGraphics();
			g2d.setPaint(c);
			g2d.fillRect(0, 0, w, h);
			g2d.dispose();
			return bi;
		}
	}
}