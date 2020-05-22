package Main;

import Main.SettingsPanels.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FloatingInnerWindow {

	private final String title;
	private double x;
	private double y;
	private int width;
	private int height;
	private final String icon;

	public boolean toggleState = true;
	private JButton closeButton = new JButton("\uE894");
	private JButton minimizeButton = new JButton("\uE921");
	private JPanel topBar = new JPanel(null);
	private JLabel windowIcon = new JLabel();
	private JButtonUI defaultUI = new JButtonUI();
	private JDialog frame = new JDialog();
	private JPanel window = new JPanel();


	FloatingInnerWindow(final String title, final int x, final int y, final int width, final int height, final String icon) {
		double y1;
		double x1;
		double ratio = 1920 / Defaults.screenSize.getWidth();
		this.title = title;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.icon = icon;
	}


	JDialog createPanel() {

		window.setDoubleBuffered(true);
		window.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mouseClicked(e);
				moveToFront();
			}
		});
		window.setBackground(new Color(0, 0, 0, 0));
		window.setLayout(null);
		window.setOpaque(false);
		window.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50)));


		MouseInputAdapter mia = new MouseInputAdapter() {
			Point location;
			Point pressed;
			public void mousePressed(MouseEvent me) {
				moveToFront();
				pressed = me.getLocationOnScreen();

			}

			public void mouseDragged(MouseEvent me) {
				Point dragged = me.getLocationOnScreen();
				double x = location.x + dragged.getX() - pressed.getX();
				double y = location.y + dragged.getY() - pressed.getY();
				Point p = new Point();
				p.setLocation(x, y);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		};
		MouseInputAdapter topScreenIA = new MouseInputAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
			}
		};

		topBar.addMouseListener(mia);
		topBar.addMouseMotionListener(mia);


		windowIcon.setForeground(Defaults.FOREGROUND);
		windowIcon.setText(icon);
		windowIcon.setBounds(10, 0, 30, 30);
		windowIcon.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 14));
		topBar.add(windowIcon);


		JLabel titleText = new JLabel(title);
		titleText.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		titleText.setBounds(35, 2, width - 60, 30);
		titleText.setForeground(Defaults.FOREGROUND);
		topBar.add(titleText);


		closeButton.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 14));
		closeButton.setBounds(width - 30, 0, 30, 30);
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setForeground(Defaults.FOREGROUND);
		closeButton.setBorder(BorderFactory.createEmptyBorder());
		closeButton.setBackground(Defaults.TOP);
		closeButton.setUI(defaultUI);
		closeButton.addMouseListener(topScreenIA);
		closeButton.addMouseMotionListener(topScreenIA);

		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

			}
		});

		topBar.add(closeButton);

		minimizeButton.setBounds(width - 60, 0, 30, 30);
		minimizeButton.setMargin(new Insets(0, 0, 0, 0));
		minimizeButton.setBorder(BorderFactory.createEmptyBorder());
		minimizeButton.setBackground(Defaults.TOP);
		minimizeButton.setForeground(Defaults.FOREGROUND);
		minimizeButton.setUI(defaultUI);
		minimizeButton.addMouseListener(topScreenIA);
		minimizeButton.addMouseMotionListener(topScreenIA);
		minimizeButton.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 14));
		minimizeButton.setVisible(false);
		topBar.add(minimizeButton);

		minimizeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					if(!WindowedSettings.onTopOption) {
						Windowed.frame.setState(Frame.ICONIFIED);
					}
				}
			}
		});

		topBar.setBackground(Defaults.TOP);
		topBar.setBounds(1, 1, width, 30);
		window.add(topBar);


		frame.setAlwaysOnTop(true);
		frame.setUndecorated(true);
		frame.setSize(width + 200,height+32 + 200);
		frame.setLayout(null);
		frame.setBackground(new Color(255, 255, 255, 0));
		frame.add(window);
		return frame;
	}


	void refreshListener() {
		for (Component component : window.getComponents()) {
			if(component instanceof JPanel && !component.equals(topBar)) {
				for (Component component1 : ((JPanel) component).getComponents()) {
					if(component1 instanceof JPanel) {
						for (Component component2 : ((JPanel) component1).getComponents()) {
							component2.addMouseListener(new MouseAdapter() {
								public void mousePressed(MouseEvent e) {
									super.mouseClicked(e);
									moveToFront();
								}
							});
						}
					}
					component1.addMouseListener(new MouseAdapter() {

						public void mousePressed(MouseEvent e) {
							super.mouseClicked(e);
							moveToFront();
						}
					});
				}
			}
			component.addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					super.mouseClicked(e);
					moveToFront();
				}
			});
		}
	}


	void refreshUI() {


		defaultUI.setBackground(Defaults.TOP);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		topBar.setBackground(Defaults.TOP);
		closeButton.setForeground(Defaults.FOREGROUND);
		windowIcon.setForeground(Defaults.FOREGROUND);
		for (Component component : topBar.getComponents()) {
			if (component instanceof JButton) {
				component.setBackground(Defaults.TOP);
			}
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);
			}
		}
	}



	void resetDimensions(int width, int height) {
		this.height = height;
		this.width = width;

	}


	void setSettings(){
		Settings.setWindowSettings(title, window.getX() + "," + window.getY() + "," + false + "," + window.isVisible());
	}


	void setVisible() {
		topBar.setVisible(true);
		window.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50)));
		if (toggleState) {

		}
	}


	void setInvisible() {
		topBar.setVisible(false);
		window.setBorder(BorderFactory.createEmptyBorder(-1,-1,-1,-1));
	}


	void toggle() {
		if (toggleState) {

			toggleState = false;
		} else {

			toggleState = true;
		}
	}


	void moveToFront() {
		Overlay.moveToFront(window);
	}


	public void setMinimize(boolean option) {
		minimizeButton.setVisible(option);
	}
}
