package com.alphalaneous;

import com.alphalaneous.Components.JButtonUI;
import com.alphalaneous.SettingsPanels.PersonalizationSettings;
import com.registry.RegDWORDValue;
import com.registry.RegistryKey;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Defaults {

	public static int screenNum;
	private static String os = (System.getProperty("os.name")).toUpperCase();
	static {
		if(os.contains("WIN")) {
			saveDirectory = System.getenv("APPDATA");
		}
		else{
			saveDirectory = System.getProperty("user.home") + "/Library/Application Support";
		}
	}
	static {
		try {
			screenNum = Integer.parseInt(Settings.getSettings("monitor"));
		} catch (Exception e) {
			screenNum = 0;
		}
	}

	public static Rectangle screenSize = GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getScreenDevices()[screenNum].getDefaultConfiguration().getBounds();
	private static Rectangle prevScreenSize = GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getScreenDevices()[screenNum].getDefaultConfiguration().getBounds();

	public static String saveDirectory;
	public static Color ACCENT = new Color(0, 108, 230);
	public static Color MAIN;
	public static Color MAIN_CLEAR;
	public static Color BUTTON;
	public static Color HOVER;
	public static Color SUB_MAIN;
	public static Color SELECT;
	public static Color SELECT_CLEAR;

	public static Color TOP;
	public static Color FOREGROUND;
	public static Color FOREGROUND2;
	public static JButtonUI defaultUI = new JButtonUI();
	public static JButtonUI settingsButtonUI = new JButtonUI();

	public static Color BUTTON_HOVER;
	public static Color BUTTON_HOVER_CLEAR;

	public static Color TEXT_BOX;
	public static Font SYMBOLS;
	public static Font SEGOE = new Font("Segoe UI", Font.PLAIN, 20);
	public static Font SEGOE_LIGHT = new Font("Segoe UI Light", Font.PLAIN, 20);

	static {
		try {
			SYMBOLS = Font.createFont(Font.TRUETYPE_FONT,
					Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("Resources/Fonts/SegMDL2.ttf")));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	private static AtomicBoolean dark = new AtomicBoolean();
	public static AtomicBoolean programLoaded = new AtomicBoolean();
	private static AtomicBoolean colorsLoaded = new AtomicBoolean();


	public static HashMap<String, Color> colors = new HashMap<>(){{
		put("foreground", Defaults.FOREGROUND);
		put("foreground2", Defaults.FOREGROUND2);
		put("main", Defaults.MAIN);
		put("sub_main", Defaults.SUB_MAIN);
		put("button", Defaults.BUTTON);
		put("button_hover", Defaults.BUTTON_HOVER);
		put("button_hover_clear", Defaults.BUTTON_HOVER_CLEAR);
		put("text_box", Defaults.TEXT_BOX);
		put("top", Defaults.TOP);
		put("select", Defaults.SELECT);
		put("select_clear", Defaults.SELECT_CLEAR);
	}};


	//region Dark Mode
	public static void setDark() {

			MAIN = new Color(31, 31, 31);
			MAIN_CLEAR = new Color(31, 31, 31, 150);

			TEXT_BOX = new Color(58, 58, 58);
			BUTTON = new Color(50, 50, 50);
			HOVER = new Color(60, 60, 60);
			SUB_MAIN = new Color(20, 20, 20);
			SELECT = new Color(70, 70, 70);
			SELECT_CLEAR = new Color(70, 70, 70, 150);

			BUTTON_HOVER = new Color(80, 80, 80);
			BUTTON_HOVER_CLEAR = new Color(80, 80, 80, 150);

			TOP = Color.BLACK;
			FOREGROUND = Color.WHITE;
			FOREGROUND2 = new Color(140, 140, 140);
		colorsLoaded.set(true);
		Themes.refreshUI();
	}
	//endregion

	//region Light Mode
	public static void setLight() {

			MAIN = new Color(230, 230, 230);
			MAIN_CLEAR = new Color(230, 230, 230, 150);
			TEXT_BOX = new Color(205, 205, 205);
			BUTTON = new Color(224, 224, 224);
			HOVER = new Color(211, 211, 211);
			SUB_MAIN = new Color(240, 240, 240);
			SELECT = new Color(215, 215, 215);
			SELECT_CLEAR = new Color(215, 215, 215, 150);

			BUTTON_HOVER = new Color(204, 204, 204);
			BUTTON_HOVER_CLEAR = new Color(204, 204, 204, 150);

			TOP = Color.WHITE;
			FOREGROUND = Color.BLACK;
			FOREGROUND2 = new Color(100, 100, 100);

		colorsLoaded.set(true);
		Themes.refreshUI();
	}
	public static void setSystem(){
		final int[] prevTheme = new int[1];
		RegistryKey personalizeStart = new RegistryKey(
				"Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");
		try {
			prevTheme[0] = ((RegDWORDValue) personalizeStart.getValue("AppsUseLightTheme")).getIntValue();
		} catch (NullPointerException e) {
			prevTheme[0] = 1;
		}
		if(PersonalizationSettings.theme.equalsIgnoreCase("SYSTEM_MODE")) {
			if (prevTheme[0] == 0) {
				Defaults.setDark();
				dark.set(false);
			} else if (prevTheme[0] == 1) {
				Defaults.setLight();
				dark.set(true);
			}
		}
	}
	//endregion

	//region Main Thread
	static void startMainThread() {
		final int[] prevTheme = new int[1];
		final Integer[] prevColor = new Integer[1];
		if(os.contains("WIN")) {
			RegistryKey personalizeStart = new RegistryKey(
					"Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");
			RegistryKey systemColorStart = new RegistryKey(
					"Software\\Microsoft\\Windows\\DWM");
			try {
				prevTheme[0] = ((RegDWORDValue) personalizeStart.getValue("AppsUseLightTheme")).getIntValue();
				prevColor[0] = ((RegDWORDValue) systemColorStart.getValue("ColorizationColor")).getValue();
				ACCENT = Color.decode(String.valueOf(prevColor[0]));
			} catch (NullPointerException e) {
				prevTheme[0] = 1;
				prevColor[0] = 0;
				ACCENT = new Color(0, 108, 230);
			}
			if(PersonalizationSettings.theme.equalsIgnoreCase("SYSTEM_MODE")) {
				if (prevTheme[0] == 0) {
					Defaults.setDark();
					dark.set(false);
				} else if (prevTheme[0] == 1) {
					Defaults.setLight();
					dark.set(true);
				}
			}
		}
		else{
			Defaults.setDark();
			dark.set(false);
		}


		new Thread(() -> {
			RegistryKey personalize = new RegistryKey(
					"Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");
			RegistryKey systemColor = new RegistryKey(
					"Software\\Microsoft\\Windows\\DWM");
			while (true) {
				try {
					if (os.contains("WIN")) {
						int theme = 0;
						Integer color;
						try {
							theme = ((RegDWORDValue) personalize.getValue("AppsUseLightTheme")).getIntValue();
							color = ((RegDWORDValue) systemColor.getValue("ColorizationColor")).getValue();
							if (!ACCENT.equals(Color.decode(String.valueOf(color)))) {
								ACCENT = Color.decode(String.valueOf(color));
								Themes.refreshUI();
							}
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
						if(PersonalizationSettings.theme.equalsIgnoreCase("SYSTEM_MODE")) {
							if (theme == 0 && prevTheme[0] == 1) {
								Defaults.setDark();
								dark.set(false);
								prevTheme[0] = 0;
							} else if (theme == 1 && prevTheme[0] == 0) {
								Defaults.setLight();
								dark.set(true);
								prevTheme[0] = 1;
							}
						}

					} else {
						Defaults.setDark();
						dark.set(false);
						prevTheme[0] = 0;
					}
					try {
						screenSize = GraphicsEnvironment
								.getLocalGraphicsEnvironment()
								.getScreenDevices()[screenNum].getDefaultConfiguration().getBounds();
					} catch (IndexOutOfBoundsException e) {
						screenSize = GraphicsEnvironment
								.getLocalGraphicsEnvironment()
								.getScreenDevices()[0].getDefaultConfiguration().getBounds();
					}
					if (!screenSize.equals(prevScreenSize)) {
						if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
							Themes.refreshUI();
						}
					}
					prevScreenSize = screenSize;

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (!programLoaded.get()) {
						programLoaded.set(true);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
	//endregion
}
