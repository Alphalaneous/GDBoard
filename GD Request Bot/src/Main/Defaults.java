package Main;

import com.registry.RegDWORDValue;
import com.registry.RegistryKey;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Defaults {

	static int screenNum;
	static String os = (System.getProperty("os.name")).toUpperCase();
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

	static Rectangle screenSize = GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getScreenDevices()[screenNum].getDefaultConfiguration().getBounds();
	private static Rectangle prevScreenSize = GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getScreenDevices()[screenNum].getDefaultConfiguration().getBounds();

	public static String saveDirectory;
	public static Color ACCENT = new Color(0, 108, 230);
	public static Color MAIN;
	public static Color BUTTON;
	public static Color HOVER;
	public static Color SUB_MAIN;
	public static Color SELECT;
	public static Color TOP;
	public static Color FOREGROUND;
	public static Color FOREGROUND2;
	static Color OUTLINE = new Color(70, 70, 70);
	public static Color BUTTON_HOVER;
	static Color TEXT_BOX;
	public static Font MAIN_FONT;
	public static Font SYMBOLS;



	static {
		try {
			MAIN_FONT = Font.createFont(Font.TRUETYPE_FONT,
					Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("Resources/Fonts/bahnschrift.ttf")));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	static {
		try {
			SYMBOLS = Font.createFont(Font.TRUETYPE_FONT,
					Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("Resources/Fonts/SegMDL2.ttf")));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	static AtomicBoolean dark = new AtomicBoolean();
	static AtomicBoolean loaded = new AtomicBoolean();
	static AtomicBoolean colorsLoaded = new AtomicBoolean();


	//region Dark Mode
	private static void setDark() {
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("MM.dd");
		if (!ft.format(date).equalsIgnoreCase("04.01")) {
			MAIN = new Color(31, 31, 31);
			TEXT_BOX = new Color(58, 58, 58);
			BUTTON = new Color(50, 50, 50);
			HOVER = new Color(60, 60, 60);
			SUB_MAIN = new Color(20, 20, 20);
			SELECT = new Color(70, 70, 70);
			BUTTON_HOVER = new Color(80, 80, 80);
			TOP = Color.BLACK;
			FOREGROUND = Color.WHITE;
			FOREGROUND2 = new Color(140, 140, 140);
		} else {
			MAIN = new Color(137, 0, 96);
			TEXT_BOX = new Color(203, 0, 154);
			BUTTON = new Color(107, 0, 80);
			HOVER = new Color(88, 0, 64);
			SUB_MAIN = new Color(111, 0, 80);
			SELECT = new Color(163, 34, 121);
			BUTTON_HOVER = new Color(203, 0, 154);
			TOP = new Color(70, 14, 52);
			FOREGROUND = Color.WHITE;
			FOREGROUND2 = new Color(159, 0, 114);
		}
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		colorsLoaded.set(true);
		Overlay.refreshUI(true);
	}
	//endregion

	//region Light Mode
	private static void setLight() {
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("MM.dd");
		if (!ft.format(date).equalsIgnoreCase("04.01")) {
			MAIN = new Color(230, 230, 230);
			TEXT_BOX = new Color(205, 205, 205);
			BUTTON = new Color(224, 224, 224);
			HOVER = new Color(211, 211, 211);
			SUB_MAIN = new Color(240, 240, 240);
			SELECT = new Color(215, 215, 215);
			BUTTON_HOVER = new Color(204, 204, 204);
			TOP = Color.WHITE;
			FOREGROUND = Color.BLACK;
			FOREGROUND2 = new Color(100, 100, 100);
		} else {
			MAIN = new Color(137, 0, 96);
			TEXT_BOX = new Color(203, 0, 154);
			BUTTON = new Color(107, 0, 80);
			HOVER = new Color(88, 0, 64);
			SUB_MAIN = new Color(111, 0, 80);
			SELECT = new Color(163, 34, 121);
			BUTTON_HOVER = new Color(203, 0, 154);
			TOP = new Color(70, 14, 52);
			FOREGROUND = Color.WHITE;
			FOREGROUND2 = new Color(159, 0, 114);
			Overlay.refreshUI(true);
		}
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		colorsLoaded.set(true);
		Overlay.refreshUI(true);
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

			if (prevTheme[0] == 0) {
				Defaults.setDark();
				dark.set(false);
			} else if (prevTheme[0] == 1) {
				Defaults.setLight();
				dark.set(true);
			}
		}
		else{
			Defaults.setDark();
			dark.set(false);
		}


		Thread thread = new Thread(() -> {
			while (true) {

				int minute;
				int hour;
				String half;
				LocalDateTime now = LocalDateTime.now();
				minute = now.getMinute();
				hour = now.getHour();
				half = "AM";
				if (hour >= 12) {
					if (hour != 12) {
						hour = hour - 12;
					}
					half = "PM";
				}
				if (hour == 0) {
					hour = 12;
				}
				MainBar.setTime(hour + ":" + String.format("%02d", minute) + " " + half);
				if(os.contains("WIN")) {
					RegistryKey personalize = new RegistryKey(
							"Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");
					RegistryKey systemColor = new RegistryKey(
							"Software\\Microsoft\\Windows\\DWM");

					int theme = 0;
					Integer color;
					try {
						theme = ((RegDWORDValue) personalize.getValue("AppsUseLightTheme")).getIntValue();
						color = ((RegDWORDValue) systemColor.getValue("ColorizationColor")).getValue();
						if (!ACCENT.equals(Color.decode(String.valueOf(color)))) {
							ACCENT = Color.decode(String.valueOf(color));
							Overlay.refreshUI(false);
						}
					} catch (NullPointerException ignored) {
					}

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
				else{
					Defaults.setDark();
					dark.set(false);
					prevTheme[0] = 0;
				}
				try {
					screenSize = GraphicsEnvironment
							.getLocalGraphicsEnvironment()
							.getScreenDevices()[screenNum].getDefaultConfiguration().getBounds();
				}
				catch (IndexOutOfBoundsException e){
					screenSize = GraphicsEnvironment
							.getLocalGraphicsEnvironment()
							.getScreenDevices()[0].getDefaultConfiguration().getBounds();
				}
					if (!screenSize.equals(prevScreenSize)) {
						try {
							if(!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
								Overlay.refreshUI(false);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					prevScreenSize = screenSize;


				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(!loaded.get()){
					loaded.set(true);
				}
			}
		});
		thread.start();

	}
	//endregion
}
