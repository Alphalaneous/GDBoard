package Main.SettingsPanels;

import Main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class RequestSettings {
	private static JButtonUI defaultUI = new JButtonUI();
	private static CheckboxButton na = createButton("NA", 5);
	private static CheckboxButton auto = createButton("Auto", 35);
	private static CheckboxButton easy = createButton("Easy", 65);
	private static CheckboxButton normal = createButton("Normal", 95);
	private static CheckboxButton hard = createButton("Hard", 125);
	private static CheckboxButton harder = createButton("Harder", 155);
	private static CheckboxButton insane = createButton("Insane", 185);
	private static CheckboxButton easyDemon = createButton("Easy Demon", 215);
	private static CheckboxButton mediumDemon = createButton("Medium Demon", 245);
	private static CheckboxButton hardDemon = createButton("Hard Demon", 275);
	private static CheckboxButton insaneDemon = createButton("Insane Demon", 305);
	private static CheckboxButton extremeDemon = createButton("Extreme Demon", 335);

	private static CheckboxButton tiny = createButton("Tiny", 5);
	private static CheckboxButton shortL = createButton("Short", 35);
	private static CheckboxButton medium = createButton("Medium", 65);
	private static CheckboxButton longL = createButton("Long", 95);
	private static CheckboxButton XL = createButton("XL", 125);


	private static CheckboxButton minimumLikes = createButton("Minimum Likes: ", 690);
	private static CheckboxButton maximumLikes = createButton("Maximum Likes: ", 765);

	private static CheckboxButton minimumObjects = createButton("Minimum Objects: ", 840);
	private static CheckboxButton maximumObjects = createButton("Maximum Objects: ", 915);

	private static FancyTextArea minLikesInput = new FancyTextArea(true, true);
	private static FancyTextArea maxLikesInput = new FancyTextArea(true, true);
	private static FancyTextArea minObjectsInput = new FancyTextArea(true, false);
	private static FancyTextArea maxObjectsInput = new FancyTextArea(true, false);

	public static int minLikes = 0;
	public static int maxLikes = 0;
	public static int minObjects = 0;
	public static int maxObjects = 0;

	public static boolean minLikesOption = false;
	public static boolean maxLikesOption = false;
	public static boolean minObjectsOption = false;
	public static boolean maxObjectsOption = false;

	public static boolean ratedOption = false;
	public static boolean unratedOption = false;
	public static boolean disableOption = true;
	public static boolean disableLengthOption = true;

	private static CheckboxButton rated = createButton("Rated Levels Only", 15);
	private static CheckboxButton unrated = createButton("Unrated Levels Only", 45);
	private static CheckboxButton disableDifficulties = createButton("Disable selected difficulties", 75);
	private static CheckboxButton disableLengths = createButton("Disable selected lengths", 490);

	private static JPanel difficultyPanel = new JPanel(null);
	private static JPanel lengthPanel = new JPanel(null);

	private static JPanel mainPanel = new JPanel(null);
	private static JPanel panel = new JPanel();
	private static JScrollPane scrollPane = new JScrollPane(panel);

	public static ArrayList<String> excludedDifficulties = new ArrayList<>();
	public static ArrayList<String> excludedLengths = new ArrayList<>();


	public static JPanel createPanel() {
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);

		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Defaults.SUB_MAIN);
		scrollPane.setBounds(0, 0, 412 , 622);
		scrollPane.setPreferredSize(new Dimension(412, 622));
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());


		mainPanel.setBounds(0, 0, 415 , 622);
		mainPanel.setBackground(Defaults.SUB_MAIN);


		panel.setLayout(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 412, 1000);
		panel.setPreferredSize(new Dimension(412,1000));
		panel.setBackground(Defaults.SUB_MAIN);

		rated.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				ratedOption = rated.getSelectedState();
			}
		});
		unrated.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				unratedOption = unrated.getSelectedState();
			}
		});
		disableDifficulties.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				disableOption = disableDifficulties.getSelectedState();
			}
		});
		disableLengths.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				disableLengthOption = disableLengths.getSelectedState();
			}
		});
		disableDifficulties.setChecked(true);
		disableLengths.setChecked(true);
		na.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (na.getSelectedState()) {
					excludedDifficulties.add("na");
					System.out.println(excludedDifficulties.toString());
				} else {
					excludedDifficulties.remove("na");
					System.out.println(excludedDifficulties.toString());
				}
			}
		});
		auto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (auto.getSelectedState()) {
					excludedDifficulties.add("auto");
					System.out.println(excludedDifficulties.toString());
				} else {
					excludedDifficulties.remove("auto");
					System.out.println(excludedDifficulties.toString());
				}
			}
		});
		easy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (easy.getSelectedState()) {
					excludedDifficulties.add("easy");
					System.out.println(excludedDifficulties.toString());
				} else {
					excludedDifficulties.remove("easy");
					System.out.println(excludedDifficulties.toString());
				}
			}
		});
		normal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (normal.getSelectedState()) {
					excludedDifficulties.add("normal");
					System.out.println(excludedDifficulties.toString());
				} else {
					excludedDifficulties.remove("normal");
					System.out.println(excludedDifficulties.toString());
				}
			}
		});
		hard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (hard.getSelectedState()) {
					excludedDifficulties.add("hard");
					System.out.println(excludedDifficulties.toString());
				} else {
					excludedDifficulties.remove("hard");
					System.out.println(excludedDifficulties.toString());
				}
			}
		});
		harder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (harder.getSelectedState()) {
					excludedDifficulties.add("harder");
					System.out.println(excludedDifficulties.toString());
				} else {
					excludedDifficulties.remove("harder");
					System.out.println(excludedDifficulties.toString());
				}
			}
		});
		insane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (insane.getSelectedState()) {
					excludedDifficulties.add("insane");
					System.out.println(excludedDifficulties.toString());
				} else {
					excludedDifficulties.remove("insane");
					System.out.println(excludedDifficulties.toString());
				}
			}
		});
		easyDemon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (easyDemon.getSelectedState()) {
					excludedDifficulties.add("easy demon");
					System.out.println(excludedDifficulties.toString());
				} else {
					excludedDifficulties.remove("easy demon");
					System.out.println(excludedDifficulties.toString());
				}
			}
		});
		mediumDemon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (mediumDemon.getSelectedState()) {
					excludedDifficulties.add("medium demon");
					System.out.println(excludedDifficulties.toString());
				} else {
					excludedDifficulties.remove("medium demon");
					System.out.println(excludedDifficulties.toString());
				}
			}
		});
		hardDemon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (hardDemon.getSelectedState()) {
					excludedDifficulties.add("hard demon");
					System.out.println(excludedDifficulties.toString());
				} else {
					excludedDifficulties.remove("hard demon");
					System.out.println(excludedDifficulties.toString());
				}
			}
		});
		insaneDemon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (insaneDemon.getSelectedState()) {
					excludedDifficulties.add("insane demon");
					System.out.println(excludedDifficulties.toString());
				} else {
					excludedDifficulties.remove("insane demon");
					System.out.println(excludedDifficulties.toString());
				}
			}
		});
		extremeDemon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (extremeDemon.getSelectedState()) {
					excludedDifficulties.add("extreme demon");
					System.out.println(excludedDifficulties.toString());
				} else {
					excludedDifficulties.remove("extreme demon");
					System.out.println(excludedDifficulties.toString());
				}
			}
		});


		tiny.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (tiny.getSelectedState()) {
					excludedLengths.add("tiny");
					System.out.println(excludedLengths.toString());
				} else {
					excludedLengths.remove("tiny");
					System.out.println(excludedLengths.toString());
				}
			}
		});
		shortL.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (shortL.getSelectedState()) {
					excludedLengths.add("short");
					System.out.println(excludedLengths.toString());
				} else {
					excludedLengths.remove("short");
					System.out.println(excludedLengths.toString());
				}
			}
		});
		medium.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (medium.getSelectedState()) {
					excludedLengths.add("medium");
					System.out.println(excludedLengths.toString());
				} else {
					excludedLengths.remove("medium");
					System.out.println(excludedLengths.toString());
				}
			}
		});
		longL.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (longL.getSelectedState()) {
					excludedLengths.add("long");
					System.out.println(excludedLengths.toString());
				} else {
					excludedLengths.remove("long");
					System.out.println(excludedLengths.toString());
				}
			}
		});
		XL.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (XL.getSelectedState()) {
					excludedLengths.add("xl");
					System.out.println(excludedLengths.toString());
				} else {
					excludedLengths.remove("xl");
					System.out.println(excludedLengths.toString());
				}
			}
		});

		minimumLikes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				minLikesOption = minimumLikes.getSelectedState();
				minLikesInput.setEditable(minLikesOption);
			}
		});
		maximumLikes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				maxLikesOption = maximumLikes.getSelectedState();
				maxLikesInput.setEditable(maxLikesOption);
			}
		});
		minimumObjects.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				minObjectsOption = minimumObjects.getSelectedState();
				minObjectsInput.setEditable(minObjectsOption);
			}
		});
		maximumObjects.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				maxObjectsOption = maximumObjects.getSelectedState();
				maxObjectsInput.setEditable(maxObjectsOption);
			}
		});

		minLikesInput.setEditable(false);
		minLikesInput.setBounds(25,723,345, 32);
		minLikesInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		minLikesInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					minLikes = Integer.parseInt(minLikesInput.getText());
				}
				catch (NumberFormatException f){
					minLikes = 0;
				}
			}
		});
		maxLikesInput.setEditable(false);
		maxLikesInput.setBounds(25,798,345, 32);
		maxLikesInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		maxLikesInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					maxLikes = Integer.parseInt(maxLikesInput.getText());
				}
				catch (NumberFormatException f){
					maxLikes = 0;
				}
			}
		});
		minObjectsInput.setEditable(false);
		minObjectsInput.setBounds(25,873,345, 32);
		minObjectsInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		minObjectsInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					minObjects = Integer.parseInt(minObjectsInput.getText());
				}
				catch (NumberFormatException f){
					minObjects = 0;
				}
			}
		});
		maxObjectsInput.setEditable(false);
		maxObjectsInput.setBounds(25,948,345, 32);
		maxObjectsInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		maxObjectsInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					maxObjects = Integer.parseInt(maxObjectsInput.getText());
				}
				catch (NumberFormatException f){
					maxObjects = 0;
				}
			}
		});



		difficultyPanel.setBounds(0,110, 415, 370);
		difficultyPanel.setBackground(Defaults.TOP);


		lengthPanel.setBounds(0,525, 415, 160);
		lengthPanel.setBackground(Defaults.TOP);

		lengthPanel.add(tiny);
		lengthPanel.add(shortL);
		lengthPanel.add(medium);
		lengthPanel.add(longL);
		lengthPanel.add(XL);

		panel.add(rated);
		panel.add(unrated);
		panel.add(disableDifficulties);
		panel.add(disableLengths);
		difficultyPanel.add(na);
		difficultyPanel.add(auto);
		difficultyPanel.add(easy);
		difficultyPanel.add(normal);
		difficultyPanel.add(hard);
		difficultyPanel.add(harder);
		difficultyPanel.add(insane);
		difficultyPanel.add(easyDemon);
		difficultyPanel.add(mediumDemon);
		difficultyPanel.add(hardDemon);
		difficultyPanel.add(insaneDemon);
		difficultyPanel.add(extremeDemon);

		panel.add(difficultyPanel);
		panel.add(lengthPanel);

		panel.add(minimumLikes);
		panel.add(maximumLikes);
		panel.add(minimumObjects);
		panel.add(maximumObjects);

		panel.add(minLikesInput);
		panel.add(maxLikesInput);
		panel.add(minObjectsInput);
		panel.add(maxObjectsInput);


		mainPanel.add(scrollPane);
		return mainPanel;


	}

	public static void loadSettings() {
		try {
			ratedOption = Boolean.parseBoolean(Settings.getSettings("rated"));
			rated.setChecked(ratedOption);
			unratedOption = Boolean.parseBoolean(Settings.getSettings("unrated"));
			unrated.setChecked(unratedOption);
			if(!Settings.getSettings("disableDifficulties").equalsIgnoreCase("")){
				disableOption = Boolean.parseBoolean(Settings.getSettings("disableDifficulties"));
			}
			if(!Settings.getSettings("disableLengths").equalsIgnoreCase("")){
				disableLengthOption = Boolean.parseBoolean(Settings.getSettings("disableLengths"));
			}
			disableDifficulties.setChecked(disableOption);
			if(!Settings.getSettings("difficultyFilter").equalsIgnoreCase("")) {
				excludedDifficulties = new ArrayList<>(Arrays.asList(Settings.getSettings("difficultyFilter").split(", ")));
			}
			if(!Settings.getSettings("lengthFilter").equalsIgnoreCase("")) {
				excludedLengths = new ArrayList<>(Arrays.asList(Settings.getSettings("lengthFilter").split(", ")));
			}

			if(!Settings.getSettings("minLikesOption").equalsIgnoreCase("")) {
				minLikesOption = Boolean.parseBoolean(Settings.getSettings("minLikesOption"));
				minimumLikes.setChecked(minLikesOption);
				minLikesInput.setEditable(minLikesOption);
			}
			if(!Settings.getSettings("maxLikesOption").equalsIgnoreCase("")) {
				maxLikesOption = Boolean.parseBoolean(Settings.getSettings("maxLikesOption"));
				maximumLikes.setChecked(maxLikesOption);
				maxLikesInput.setEditable(maxLikesOption);
			}
			if(!Settings.getSettings("minObjectsOption").equalsIgnoreCase("")) {
				minObjectsOption = Boolean.parseBoolean(Settings.getSettings("minObjectsOption"));
				minimumObjects.setChecked(minObjectsOption);
				minObjectsInput.setEditable(minObjectsOption);
			}
			if(!Settings.getSettings("maxObjectsOption").equalsIgnoreCase("")) {
				maxObjectsOption = Boolean.parseBoolean(Settings.getSettings("maxObjectsOption"));
				maximumObjects.setChecked(maxObjectsOption);
				maxObjectsInput.setEditable(maxObjectsOption);
			}
			if(excludedLengths.contains("tiny")){
				tiny.setChecked(true);
			}
			if(excludedLengths.contains("short")){
				shortL.setChecked(true);
			}
			if(excludedLengths.contains("medium")){
				medium.setChecked(true);
			}
			if(excludedLengths.contains("long")){
				longL.setChecked(true);
			}
			if(excludedLengths.contains("xl")){
				XL.setChecked(true);
			}

			if(excludedDifficulties.contains("na")){
				na.setChecked(true);
			}
			if(excludedDifficulties.contains("auto")){
				auto.setChecked(true);
			}
			if(excludedDifficulties.contains("easy")){
				easy.setChecked(true);
			}
			if(excludedDifficulties.contains("normal")){
				normal.setChecked(true);
			}
			if(excludedDifficulties.contains("hard")){
				hard.setChecked(true);
			}
			if(excludedDifficulties.contains("harder")){
				harder.setChecked(true);
			}
			if(excludedDifficulties.contains("insane")){
				insane.setChecked(true);
			}
			if(excludedDifficulties.contains("easy demon")){
				easyDemon.setChecked(true);
			}
			if(excludedDifficulties.contains("medium demon")){
				mediumDemon.setChecked(true);
			}
			if(excludedDifficulties.contains("hard demon")){
				hardDemon.setChecked(true);
			}
			if(excludedDifficulties.contains("insane demon")){
				insaneDemon.setChecked(true);
			}
			if(excludedDifficulties.contains("extreme demon")){
				extremeDemon.setChecked(true);
			}
			if(!Settings.getSettings("minLikes").equalsIgnoreCase("")) {
				minLikes = Integer.parseInt(Settings.getSettings("minLikes"));
				minLikesInput.setText(String.valueOf(minLikes));
			}
			if(!Settings.getSettings("maxLikes").equalsIgnoreCase("")) {
				maxLikes = Integer.parseInt(Settings.getSettings("maxLikes"));
				maxLikesInput.setText(String.valueOf(maxLikes));
			}
			if(!Settings.getSettings("minObjects").equalsIgnoreCase("")) {
				minObjects = Integer.parseInt(Settings.getSettings("minObjects"));
				minObjectsInput.setText(String.valueOf(minObjects));
			}
			if(!Settings.getSettings("maxObjects").equalsIgnoreCase("")) {
				maxObjects = Integer.parseInt(Settings.getSettings("maxObjects"));
				maxObjectsInput.setText(String.valueOf(maxObjects));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setSettings() {
		try {
			Settings.writeSettings("rated", String.valueOf(ratedOption));
			Settings.writeSettings("unrated", String.valueOf(unratedOption));
			Settings.writeSettings("disableDifficulties", String.valueOf(disableOption));
			Settings.writeSettings("minLikesOption", String.valueOf(minLikesOption));
			Settings.writeSettings("maxLikesOption", String.valueOf(maxLikesOption));
			Settings.writeSettings("minObjectsOption", String.valueOf(minObjectsOption));
			Settings.writeSettings("maxObjectsOption", String.valueOf(maxObjectsOption));
			Settings.writeSettings("disableLengths", String.valueOf(disableOption));
			Settings.writeSettings("minLikes", String.valueOf(minLikes));
			Settings.writeSettings("maxLikes", String.valueOf(maxLikes));
			Settings.writeSettings("minObjects", String.valueOf(minObjects));
			Settings.writeSettings("maxObjects", String.valueOf(maxObjects));
			Settings.writeSettings("difficultyFilter", excludedDifficulties.toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(",{4}", ","));
			Settings.writeSettings("lengthFilter", excludedLengths.toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(",{4}", ","));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static CheckboxButton createButton(String text, int y) {

		CheckboxButton button = new CheckboxButton(text);
		button.setBounds(25, y, 345, 30);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		button.refresh();
		return button;
	}

	public static void refreshUI() {
		defaultUI.setBackground(Defaults.MAIN);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		difficultyPanel.setBackground(Defaults.TOP);
		lengthPanel.setBackground(Defaults.TOP);
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
				component.setBackground(Defaults.MAIN);
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
		for (Component component : difficultyPanel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.MAIN);
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
		for (Component component : lengthPanel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.MAIN);
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
