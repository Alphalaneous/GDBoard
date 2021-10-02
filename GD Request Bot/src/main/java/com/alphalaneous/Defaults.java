package com.alphalaneous;

import com.alphalaneous.Components.JButtonUI;
import com.registry.RegDWORDValue;
import com.registry.RegistryKey;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Defaults {

	public static int screenNum;
	public static String saveDirectory;
	public static boolean isLight = false;
	public static Color ACCENT = new Color(0, 108, 230);
	public static Color COLOR;
	public static Color COLOR1;
	public static Color COLOR2;
	public static Color COLOR3;
	public static Color COLOR4;
	public static Color COLOR6;
	public static Color FOREGROUND_A;
	public static Color FOREGROUND_B;
	public static JButtonUI defaultUI = new JButtonUI();
	public static JButtonUI settingsButtonUI = new JButtonUI();
	public static Color COLOR5;
	public static Font SYMBOLS;
	public static Font SYMBOLSalt;
	public static Font MAIN_FONT;
	public static Font SEGOE_FONT = new Font("Segoe UI", Font.PLAIN, 20);
	public static HashMap<String, Color> colors = new HashMap<>() {{
		put("foreground", Defaults.FOREGROUND_A);
		put("foreground2", Defaults.FOREGROUND_B);
		put("main", Defaults.COLOR);
		put("sub_main", Defaults.COLOR3);
		put("button", Defaults.COLOR2);
		put("button_hover", Defaults.COLOR5);
		put("top", Defaults.COLOR6);
		put("select", Defaults.COLOR4);
	}};
	private static final String os = (System.getProperty("os.name")).toUpperCase();

	static {
		if (os.contains("WIN")) {
			saveDirectory = System.getenv("APPDATA");
		} else {
			saveDirectory = System.getProperty("user.home") + "/Library/Application Support";
		}
	}

	static {
		try {
			screenNum = Settings.getSettings("monitor").asInteger();
		} catch (Exception e) {
			screenNum = 0;
		}
	}

	static {
		try {
			SYMBOLS = Font.createFont(Font.TRUETYPE_FONT,
					Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("Fonts/webhostinghub-glyphs.ttf")));
			MAIN_FONT = Font.createFont(Font.TRUETYPE_FONT,
					Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("Fonts/Poppins-Regular.ttf")));
			SYMBOLSalt = Font.createFont(Font.TRUETYPE_FONT,
					Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("Fonts/SegMDL2.ttf")));
		} catch (FontFormatException | IOException | NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static void setDark() {
		isLight = false;
		COLOR = new Color(31, 29, 46);
		COLOR1 = new Color(47, 44, 66);
		COLOR2 = new Color(39, 38, 59);
		COLOR3 = new Color(23, 22, 35);
		COLOR4 = new Color(45, 42, 66);
		COLOR5 = new Color(58, 56, 80);
		COLOR6 = new Color(8, 7, 20);
		FOREGROUND_A = Color.WHITE;
		FOREGROUND_B = new Color(165, 165, 165);
		Themes.refreshUI();
	}

	public static void setLight() {
		isLight = true;
		COLOR = new Color(230, 230, 230);
		COLOR1 = new Color(205, 205, 205);
		COLOR2 = new Color(224, 224, 224);
		COLOR3 = new Color(240, 240, 240);
		COLOR4 = new Color(215, 215, 215);
		COLOR5 = new Color(204, 204, 204);
		COLOR6 = Color.WHITE;
		FOREGROUND_A = Color.BLACK;
		FOREGROUND_B = new Color(132, 132, 132);
		Themes.refreshUI();
	}

	public static void setSystem() {
		final int[] prevTheme = new int[1];
		RegistryKey personalizeStart = new RegistryKey(
				"Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");
		try {
			prevTheme[0] = ((RegDWORDValue) personalizeStart.getValue("AppsUseLightTheme")).getIntValue();
		} catch (NullPointerException e) {
			prevTheme[0] = 1;
		}
		if(Settings.getSettings("theme").asString().equalsIgnoreCase("SYSTEM_MODE")) {
			if (prevTheme[0] == 0) {
				Defaults.setDark();
			} else if (prevTheme[0] == 1) {
				Defaults.setLight();
			}
		}
		else{
			setDark();
		}
	}
	private static final int[] prevTheme = new int[1];
	private static final RegistryKey personalize = new RegistryKey(
			"Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");
	private static final RegistryKey systemColor = new RegistryKey(
			"Software\\Microsoft\\Windows\\DWM");

	static void initializeThemeInfo(){

		if(Settings.getSettings("theme").asString().equalsIgnoreCase("LIGHT_MODE")) setLight();
		else if(Settings.getSettings("theme").asString().equalsIgnoreCase("DARK_MODE")) setDark();
		else {
			if (os.contains("WIN")) Defaults.setSystem();
			else Defaults.setDark();
		}
	}

	static void startMainThread() {
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
						if(Settings.getSettings("theme").asString().equalsIgnoreCase("SYSTEM_MODE")) {
							if (theme == 0 && prevTheme[0] == 1) {
								Defaults.setDark();
								prevTheme[0] = 0;
							} else if (theme == 1 && prevTheme[0] == 0) {
								Defaults.setLight();
								prevTheme[0] = 1;
							}
						}

					} else {
						Defaults.setDark();
						prevTheme[0] = 0;
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

	}
}
