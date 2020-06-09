package Main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.swing.*;

public class RoundedJButton extends JButton {

	private static final long serialVersionUID = 1L;

	public RoundedJButton(String label, String tooltip) {
		super(label);
		final boolean[] exited = {false};
		Dimension size = getPreferredSize();
		JPanel tooltipPanel = new JPanel();
		tooltipPanel.setFocusable(false);
		JLabel tooltipLabel = new JLabel(tooltip);
		tooltipPanel.add(tooltipLabel);
		tooltipLabel.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		size.width = size.height = Math.max(size.width, size.height);
		setPreferredSize(size);
		setContentAreaFilled(false);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				exited[0] = false;
				Thread thread = new Thread(() -> {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					if(!exited[0]) {
						tooltipPanel.setBackground(Defaults.TOP);
						tooltipLabel.setForeground(Defaults.FOREGROUND);
						if(Settings.windowedMode){
							tooltipPanel.setBounds(MouseInfo.getPointerInfo().getLocation().x - (tooltipLabel.getPreferredSize().width + 10)/2 - Windowed.frame.getX(), MouseInfo.getPointerInfo().getLocation().y+20 - Windowed.frame.getY(), tooltipLabel.getPreferredSize().width + 10, tooltipLabel.getPreferredSize().height + 5);
							Windowed.addToFrame(tooltipPanel);
						}
						else{
							tooltipPanel.setBounds(MouseInfo.getPointerInfo().getLocation().x - (tooltipLabel.getPreferredSize().width + 10)/2 - Defaults.screenSize.x, MouseInfo.getPointerInfo().getLocation().y+20 - Defaults.screenSize.y, tooltipLabel.getPreferredSize().width + 10, tooltipLabel.getPreferredSize().height + 5);
							Overlay.addToFrame(tooltipPanel);

						}
						tooltipPanel.setVisible(true);
					}
				});
				thread.start();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exited[0] = true;
				tooltipPanel.setVisible(false);
				Overlay.removeFromFrame(tooltipPanel);
			}
		});
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
