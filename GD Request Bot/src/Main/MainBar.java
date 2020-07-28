package Main;


import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

class MainBar {

	private static JButtonUI defaultUI = new JButtonUI();
	private static JButtonUI subUI = new JButtonUI();
	private static JLabel time = new JLabel();
	private static JPanel barPanel = new JPanel();
	private static JPanel mainPanel = new JPanel();
	private static JPanel buttonPanel = new JPanel();
	private static JPanel subPanel = new JPanel();

	private static JLabel icon = new JLabel();
	static boolean requests = true;
	static JButton stopReqs = createSubButton("\uE71A", "Toggle Requests");
	private static JButton toggleSettings = createSubButton("\uE713", "Settings");
	private static JButton close = createSubButton("\uE10A", "Close");
	private static Color alphaBorder = new Color(Defaults.ACCENT.getRed(), Defaults.ACCENT.getGreen(), Defaults.ACCENT.getBlue(), 100);

	static void createBar() {

		//TODO Settings and request toggle buttons in windowed mode
		double ratio = 1920 / Defaults.screenSize.getWidth();
		Overlay.alwaysFront(barPanel);
		barPanel.setOpaque(false);
		barPanel.setSize(580, 65);
		barPanel.setLocation((int) (670 / ratio), 30);
		barPanel.setLayout(null);
		barPanel.setBorder(BorderFactory.createLineBorder(alphaBorder));
		MouseInputAdapter mia = new MouseInputAdapter() {
			Point pressed;

			public void mousePressed(MouseEvent me) {
				pressed = me.getLocationOnScreen();
			}

			public void mouseDragged(MouseEvent me) {

				Point dragged = me.getLocationOnScreen();
				double x = dragged.getX() - pressed.getX();
				double y = dragged.getY() - pressed.getY();
				Point p = new Point();
				p.setLocation(x, y);
				GraphicsDevice[] screens = GraphicsEnvironment
						.getLocalGraphicsEnvironment()
						.getScreenDevices();
				int mouseX = MouseInfo.getPointerInfo().getLocation().x;
				int mouseY = MouseInfo.getPointerInfo().getLocation().y;
				Point mouse = new Point(mouseX, mouseY);
				for (GraphicsDevice screen : screens) {
					if (screen.getDefaultConfiguration().getBounds().contains(mouse)) {
						Defaults.screenNum = Integer.parseInt(screen.getIDstring().replaceAll("Display", "").replace("\\", ""));
					}
				}
			}
		};
		mainPanel.addMouseListener(mia);
		mainPanel.addMouseMotionListener(mia);

		mainPanel.setBounds(1, 1, 578, 63);
		mainPanel.setBackground(Defaults.TOP);
		mainPanel.setLayout(null);
		barPanel.add(mainPanel);

		buttonPanel.setLayout(new GridLayout(1, 1));
		buttonPanel.setBounds(160, 0, 300, 64);
		buttonPanel.setBackground(Defaults.MAIN);

		subPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		subPanel.setBounds(460, 0, 120, 64);
		subPanel.setBackground(Defaults.TOP);


			Reflections innerReflections = new Reflections("Main.InnerWindows", new SubTypesScanner(false));
			Set<Class<?>> innerClasses =
					innerReflections.getSubTypesOf(Object.class);
			for (Class<?> Class : innerClasses) {
				createButton(Class);
			}




		stopReqs.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Functions.requestsToggleFunction();
			}
		});

		toggleSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				SettingsWindow.toggleVisible();
			}
		});

		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				Main.close();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
			}
		});

		subPanel.add(stopReqs);
		subPanel.add(toggleSettings);
		subPanel.add(close);


		Map<TextAttribute, Object> attributes = new HashMap<>();
		attributes.put(TextAttribute.TRACKING, 0.02);
		Font font = Defaults.MAIN_FONT.deriveFont(attributes).deriveFont(23f);

		time.setFont(font);
		time.setForeground(Defaults.FOREGROUND);

		mainPanel.add(time);
		mainPanel.add(buttonPanel);
		mainPanel.add(subPanel);

		BufferedImage img = null;
		try {
			if (!Defaults.dark.get()) {
				img = ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader()
						.getResource("Resources/Icons/barIconLight.png")));
			} else if (Defaults.dark.get()) {
				img = ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader()
						.getResource("Resources/Icons/barIconDark.png")));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		assert img != null;
		Image imgScaled = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		ImageIcon imgNew = new ImageIcon(imgScaled);

		icon.setIcon(imgNew);
		icon.setBounds(20, -1, 64, 64);
		icon.updateUI();
		mainPanel.add(icon);
		Overlay.addToFrame(barPanel);
	}
	static void destroyBar(){
		barPanel.removeAll();
		mainPanel.removeAll();
		subPanel.removeAll();
		buttonPanel.removeAll();
	}
	static JPanel getMainBar() {
		return barPanel;
	}

	private static void createButton(Class<?> InnerWindow) {
		Object t = null;
		try {
			t = InnerWindow.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		Object icon = null;
		Object tooltip = null;
		try {
			Method getIcon = InnerWindow.getMethod("getIcon");
			Method getTooltip = InnerWindow.getMethod("getName");
			icon = getIcon.invoke(t);
			tooltip = getTooltip.invoke(t);

		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		defaultUI.setBackground(Defaults.MAIN);
		JButton button = new JButtonTooltip((String)icon, 64, (String)tooltip, defaultUI);
		//button.setPreferredSize(new Dimension(60, 64));
		button.setBackground(Defaults.MAIN);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SYMBOLS.deriveFont(20f));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					Method method = InnerWindow.getMethod("toggleVisible");
					method.invoke(null);
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(button);
	}

	private static JButton createSubButton(String icon, String tooltip) {

		subUI.setBackground(Defaults.TOP);
		JButton button = new JButtonTooltip(icon, 64, tooltip, subUI);
		button.setPreferredSize(new Dimension(40, 64));
		button.setBackground(Defaults.TOP);
		button.setUI(subUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SYMBOLS.deriveFont(20f));
		return button;
	}

	static void setTime(String timeValue) {
		time.setText(timeValue);
		time.setBounds(148 - time.getPreferredSize().width, 2, (int) time.getPreferredSize().getWidth(), 64);
	}

	static void refreshUI(boolean color) {
		alphaBorder = new Color(Defaults.ACCENT.getRed(), Defaults.ACCENT.getGreen(), Defaults.ACCENT.getBlue(), 100);
		barPanel.setBorder(BorderFactory.createLineBorder(alphaBorder));
		if (color) {
			BufferedImage img = null;
			if(Main.refreshImages) {
				try {
					if (Defaults.dark.get()) {
						System.out.println("Dark");
						img = ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader()
								.getResource("Resources/Icons/barIconLight.png")));
					} else if (!Defaults.dark.get()) {
						System.out.println("Light");
						img = ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader()
								.getResource("Resources/Icons/barIconDark.png")));
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				assert img != null;
				Image imgScaled = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
				ImageIcon imgNew = new ImageIcon(imgScaled);

				icon.setIcon(imgNew);
			}
			icon.setBounds(20, -1, 64, 64);
			mainPanel.setBackground(Defaults.TOP);
			buttonPanel.setBackground(Defaults.TOP);
			defaultUI.setBackground(Defaults.MAIN);
			defaultUI.setHover(Defaults.HOVER);
			defaultUI.setSelect(Defaults.SELECT);
			subUI.setBackground(Defaults.TOP);
			subUI.setHover(Defaults.HOVER);
			subUI.setSelect(Defaults.SELECT);
			time.setForeground(Defaults.FOREGROUND);
			for (Component component : buttonPanel.getComponents()) {
				if (component instanceof JButton) {
					component.setBackground(Defaults.MAIN);
					((JButtonTooltip) component).refreshUI();
					component.setForeground(Defaults.FOREGROUND);
				}
			}
			for (Component component : subPanel.getComponents()) {
				if (component instanceof JButton) {
					component.setBackground(Defaults.TOP);
					((JButtonTooltip) component).refreshUI();
					component.setForeground(Defaults.FOREGROUND);
				}
			}
		} else {
			int middle = (int) (Defaults.screenSize.getWidth() / 2);
			barPanel.setLocation(middle - 290, 30);
		}
	}

	static void setInvisible() {
		barPanel.setVisible(false);
	}

	static void setVisible() {
		barPanel.setVisible(true);
	}

}
