package com.alphalaneous.SettingsPanels;

import com.alphalaneous.Components.*;
import com.alphalaneous.Defaults;
import com.alphalaneous.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class ChaosModeSettings {

	private static CheckboxButton enableChaosMode = createButton("$ENABLE_CHAOS_MODE$", 20);
	private static CheckboxButton modOnlyChaos = createButton("$MOD_ONLY_CHAOS$", 50);
	private static CheckboxButton disableKill = createButton("$DISABLE_KILL$", 90);


	private static CheckboxButton minimumX = createButton("$MINIMUM_X$", 120);
	private static CheckboxButton maximumX = createButton("$MAXIMUM_X$", 195);
	private static CheckboxButton minimumY = createButton("$MINIMUM_Y$", 270);
	private static CheckboxButton maximumY = createButton("$MAXIMUM_Y$", 345);
	private static CheckboxButton minimumSize = createButton("$MINIMUM_SIZE$", 420);
	private static CheckboxButton maximumSize = createButton("$MAXIMUM_SIZE$", 495);
	private static CheckboxButton minimumSpeed = createButton("$MINIMUM_SPEED$", 570);
	private static CheckboxButton maximumSpeed = createButton("$MAXIMUM_SPEED$", 645);
	private static FancyTextArea minXInput = new FancyTextArea(true, true);
	private static FancyTextArea maxXInput = new FancyTextArea(true, true);
	private static FancyTextArea minYInput = new FancyTextArea(true, true);
	private static FancyTextArea maxYInput = new FancyTextArea(true, true);
	private static FancyTextArea minSizeInput = new FancyTextArea(true, true);
	private static FancyTextArea maxSizeInput = new FancyTextArea(true, true);
	private static FancyTextArea minSpeedInput = new FancyTextArea(true, true);
	private static FancyTextArea maxSpeedInput = new FancyTextArea(true, true);


	public static boolean enableChaos = false;
	public static boolean modOnly = false;

	public static boolean disableKillOption = false;
	public static int minX = 0;
	public static int maxX = 0;
	public static int minY = 0;
	public static int maxY = 0;
	public static int minSize = 0;
	public static int maxSize = 0;
	public static int minSpeed = 0;
	public static int maxSpeed = 0;

	public static boolean minXOption = false;
	public static boolean maxXOption = false;
	public static boolean minYOption = false;
	public static boolean maxYOption = false;
	public static boolean minSizeOption = false;
	public static boolean maxSizeOption = false;
	public static boolean minSpeedOption = false;
	public static boolean maxSpeedOption = false;

	private static JPanel mainPanel = new JPanel(null);
	private static JPanel panel = new JPanel();
	private static JScrollPane scrollPane = new JScrollPane(panel);


	public static JPanel createPanel() {

		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Defaults.SUB_MAIN);
		scrollPane.setBounds(0, 0, 412, 622);
		scrollPane.setPreferredSize(new Dimension(412, 622));
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());


		mainPanel.setBounds(0, 0, 415, 622);
		mainPanel.setBackground(Defaults.SUB_MAIN);


		panel.setLayout(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 412, 720);
		panel.setPreferredSize(new Dimension(412, 720));
		panel.setBackground(Defaults.SUB_MAIN);

		enableChaosMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				enableChaos = enableChaosMode.getSelectedState();
			}
		});
		modOnlyChaos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				modOnly = modOnlyChaos.getSelectedState();
			}
		});
		disableKill.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				disableKillOption = disableKill.getSelectedState();
			}
		});

		minimumX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				minXOption = minimumX.getSelectedState();
				minXInput.setEditable(minXOption);
			}
		});
		maximumX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				maxXOption = maximumX.getSelectedState();
				maxXInput.setEditable(maxXOption);
			}
		});
		minimumY.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				minYOption = minimumY.getSelectedState();
				minYInput.setEditable(minYOption);
			}
		});
		maximumY.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				maxYOption = maximumY.getSelectedState();
				maxYInput.setEditable(maxYOption);
			}
		});
		minimumSize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				minSizeOption = minimumSize.getSelectedState();
				minSizeInput.setEditable(minSizeOption);
			}
		});
		maximumSize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				maxSizeOption = maximumSize.getSelectedState();
				maxSizeInput.setEditable(maxSizeOption);
			}
		});
		minimumSpeed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				minSpeedOption = minimumSpeed.getSelectedState();
				minSpeedInput.setEditable(minSpeedOption);
			}
		});
		maximumSpeed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				maxSpeedOption = maximumSpeed.getSelectedState();
				maxSpeedInput.setEditable(maxSpeedOption);
			}
		});

		minXInput.setEditable(false);
		minXInput.setBounds(25, 153, 345, 32);
		minXInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		minXInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					minX = Integer.parseInt(minXInput.getText());
				} catch (NumberFormatException f) {
					minX = 0;
				}
			}
		});
		maxXInput.setEditable(false);
		maxXInput.setBounds(25, 228, 345, 32);
		maxXInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		maxXInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					maxX = Integer.parseInt(maxXInput.getText());
				} catch (NumberFormatException f) {
					maxX = 0;
				}
			}
		});
		minYInput.setEditable(false);
		minYInput.setBounds(25, 303, 345, 32);
		minYInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		minYInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					minY = Integer.parseInt(minYInput.getText());
				} catch (NumberFormatException f) {
					minY = 0;
				}
			}
		});
		maxYInput.setEditable(false);
		maxYInput.setBounds(25, 378, 345, 32);
		maxYInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		maxYInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					maxY = Integer.parseInt(maxYInput.getText());
				} catch (NumberFormatException f) {
					maxY = 0;
				}
			}
		});
		minSizeInput.setEditable(false);
		minSizeInput.setBounds(25, 453, 345, 32);
		minSizeInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		minSizeInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					minSize = Integer.parseInt(minSizeInput.getText());
				} catch (NumberFormatException f) {
					minSize = 0;
				}
			}
		});
		maxSizeInput.setEditable(false);
		maxSizeInput.setBounds(25, 528, 345, 32);
		maxSizeInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		maxSizeInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					maxSize = Integer.parseInt(maxSizeInput.getText());
				} catch (NumberFormatException f) {
					maxSize = 0;
				}
			}
		});
		minSpeedInput.setEditable(false);
		minSpeedInput.setBounds(25, 603, 345, 32);
		minSpeedInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		minSpeedInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					minSpeed = Integer.parseInt(minSpeedInput.getText());
				} catch (NumberFormatException f) {
					minSpeed = 0;
				}
			}
		});
		maxSpeedInput.setEditable(false);
		maxSpeedInput.setBounds(25, 678, 345, 32);
		maxSpeedInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		maxSpeedInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					maxSpeed = Integer.parseInt(maxSpeedInput.getText());
				} catch (NumberFormatException f) {
					maxSpeed = 0;
				}
			}
		});

		panel.add(enableChaosMode);
		panel.add(modOnlyChaos);

		panel.add(disableKill);
		panel.add(minimumX);
		panel.add(maximumX);
		panel.add(minimumY);
		panel.add(maximumY);
		panel.add(minimumSize);
		panel.add(maximumSize);
		panel.add(minimumSpeed);
		panel.add(maximumSpeed);

		panel.add(minXInput);
		panel.add(maxXInput);
		panel.add(minYInput);
		panel.add(maxYInput);
		panel.add(minSizeInput);
		panel.add(maxSizeInput);
		panel.add(minSpeedInput);
		panel.add(maxSpeedInput);

		mainPanel.add(scrollPane);
		return mainPanel;

	}

	public static void loadSettings() {


		if (!Settings.getSettings("isChaos").equalsIgnoreCase("")) {
			enableChaos = Boolean.parseBoolean(Settings.getSettings("isChaos"));
			enableChaosMode.setChecked(enableChaos);
		}
		if (!Settings.getSettings("isModChaos").equalsIgnoreCase("")) {
			modOnly = Boolean.parseBoolean(Settings.getSettings("isModChaos"));
			modOnlyChaos.setChecked(modOnly);
		}
		if (!Settings.getSettings("disableKill").equalsIgnoreCase("")) {
			disableKillOption = Boolean.parseBoolean(Settings.getSettings("disableKill"));
			disableKill.setChecked(disableKillOption);
		}
		if (!Settings.getSettings("minXOption").equalsIgnoreCase("")) {
			minXOption = Boolean.parseBoolean(Settings.getSettings("minXOption"));
			minimumX.setChecked(minXOption);
			minXInput.setEditable(minXOption);
		}
		if (!Settings.getSettings("maxXOption").equalsIgnoreCase("")) {
			maxXOption = Boolean.parseBoolean(Settings.getSettings("maxXOption"));
			maximumX.setChecked(maxXOption);
			maxXInput.setEditable(maxXOption);
		}
		if (!Settings.getSettings("minYOption").equalsIgnoreCase("")) {
			minYOption = Boolean.parseBoolean(Settings.getSettings("minYOption"));
			minimumY.setChecked(minYOption);
			minYInput.setEditable(minYOption);
		}
		if (!Settings.getSettings("maxYOption").equalsIgnoreCase("")) {
			maxYOption = Boolean.parseBoolean(Settings.getSettings("maxYOption"));
			maximumY.setChecked(maxYOption);
			maxYInput.setEditable(maxYOption);
		}
		if (!Settings.getSettings("minSizeOption").equalsIgnoreCase("")) {
			minSizeOption = Boolean.parseBoolean(Settings.getSettings("minSizeOption"));
			minimumSize.setChecked(minSizeOption);
			minSizeInput.setEditable(minSizeOption);
		}
		if (!Settings.getSettings("maxSizeOption").equalsIgnoreCase("")) {
			maxSizeOption = Boolean.parseBoolean(Settings.getSettings("maxSizeOption"));
			maximumSize.setChecked(maxSizeOption);
			maxSizeInput.setEditable(maxSizeOption);
		}
		if (!Settings.getSettings("minSpeedOption").equalsIgnoreCase("")) {
			minSpeedOption = Boolean.parseBoolean(Settings.getSettings("minSpeedOption"));
			minimumSpeed.setChecked(minSpeedOption);
			minSpeedInput.setEditable(minSpeedOption);
		}
		if (!Settings.getSettings("maxSpeedOption").equalsIgnoreCase("")) {
			maxSpeedOption = Boolean.parseBoolean(Settings.getSettings("maxSpeedOption"));
			maximumSpeed.setChecked(maxSpeedOption);
			maxSpeedInput.setEditable(maxSpeedOption);
		}
		if (!Settings.getSettings("minX").equalsIgnoreCase("")) {
			minX = Integer.parseInt(Settings.getSettings("minX"));
			minXInput.setText(String.valueOf(minX));
		}
		if (!Settings.getSettings("maxX").equalsIgnoreCase("")) {
			maxX = Integer.parseInt(Settings.getSettings("maxX"));
			maxXInput.setText(String.valueOf(maxX));
		}
		if (!Settings.getSettings("minY").equalsIgnoreCase("")) {
			minY = Integer.parseInt(Settings.getSettings("minY"));
			minYInput.setText(String.valueOf(minY));
		}
		if (!Settings.getSettings("maxY").equalsIgnoreCase("")) {
			maxY = Integer.parseInt(Settings.getSettings("maxY"));
			maxYInput.setText(String.valueOf(maxY));
		}
		if (!Settings.getSettings("minSize").equalsIgnoreCase("")) {
			minSize = Integer.parseInt(Settings.getSettings("minSize"));
			minSizeInput.setText(String.valueOf(minSize));
		}
		if (!Settings.getSettings("maxSize").equalsIgnoreCase("")) {
			maxSize = Integer.parseInt(Settings.getSettings("maxSize"));
			maxSizeInput.setText(String.valueOf(maxSize));
		}
		if (!Settings.getSettings("minSpeed").equalsIgnoreCase("")) {
			minSpeed = Integer.parseInt(Settings.getSettings("minSpeed"));
			minSpeedInput.setText(String.valueOf(minSpeed));
		}
		if (!Settings.getSettings("maxSpeed").equalsIgnoreCase("")) {
			maxSpeed = Integer.parseInt(Settings.getSettings("maxSpeed"));
			maxSpeedInput.setText(String.valueOf(maxSpeed));
		}
	}

	public static void setSettings() {

		Settings.writeSettings("isChaos", String.valueOf(enableChaos));
		Settings.writeSettings("isModChaos", String.valueOf(modOnly));
		Settings.writeSettings("disableKill", String.valueOf(disableKillOption));
		Settings.writeSettings("minXOption", String.valueOf(minXOption));
		Settings.writeSettings("maxXOption", String.valueOf(maxXOption));
		Settings.writeSettings("minYOption", String.valueOf(minYOption));
		Settings.writeSettings("maxYOption", String.valueOf(maxYOption));
		Settings.writeSettings("minSizeOption", String.valueOf(minSizeOption));
		Settings.writeSettings("maxSizeOption", String.valueOf(maxSizeOption));
		Settings.writeSettings("minSpeedOption", String.valueOf(minSpeedOption));
		Settings.writeSettings("maxSpeedOption", String.valueOf(maxSpeedOption));
		Settings.writeSettings("minX", String.valueOf(minX));
		Settings.writeSettings("maxX", String.valueOf(maxX));
		Settings.writeSettings("minY", String.valueOf(minY));
		Settings.writeSettings("maxY", String.valueOf(maxY));
		Settings.writeSettings("minSize", String.valueOf(minSize));
		Settings.writeSettings("maxSize", String.valueOf(maxSize));
		Settings.writeSettings("minSpeed", String.valueOf(minSpeed));
		Settings.writeSettings("maxSpeed", String.valueOf(maxSpeed));
	}

	private static CheckboxButton createButton(String text, int width, int y) {

		CheckboxButton button = new CheckboxButton(text, RequestSettings.class);
		button.setBounds(25, y, width, 30);
		button.setForeground(Defaults.FOREGROUND);
		button.setOpaque(false);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SEGOE.deriveFont(14f));
		button.refresh();
		return button;
	}

	private static CheckboxButton createButton(String text, int y) {
		return createButton(text, 345, y);
	}

	public static void refreshUI() {

		panel.setBackground(Defaults.SUB_MAIN);
		mainPanel.setBackground(Defaults.SUB_MAIN);
		scrollPane.setBackground(Defaults.SUB_MAIN);
		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());


		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);

					}
				}
				component.setBackground(Defaults.BUTTON);
			}
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);

			}
			if (component instanceof JTextArea) {
				((FancyTextArea) component).refreshAll();
			}
			if (component instanceof CheckboxButton) {
				((CheckboxButton) component).refresh();
			}
		}
	}
}
