package Main;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import com.registry.RegDWORDValue;
import com.registry.RegistryKey;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Defaults {

	static int screenNum = Settings.monitor;
	static Rectangle screenSize =  GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getScreenDevices()[screenNum].getDefaultConfiguration().getBounds();
	static Rectangle prevScreenSize = GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getScreenDevices()[screenNum].getDefaultConfiguration().getBounds();
	static List<User> mods = new ArrayList<>();
	public static Color MAIN;
	public static Color BUTTON;
	public static Color HOVER;
	public static Color SUB_MAIN;
	public static Color SELECT;
	public static Color TOP;
	public static Color FOREGROUND;
	public static Color FOREGROUND2;
	public static Color OUTLINE = new Color(70,70,70);
	public static Color BUTTON_HOVER;
	static AtomicBoolean dark = new AtomicBoolean();
	
	//region Dark Mode
	private static void setDark() {
		MAIN = new Color(31,31,31);
		BUTTON = new Color(50,50,50);
		HOVER = new Color(60,60,60);
		SUB_MAIN = new Color(20,20,20);
		SELECT = new Color(70,70,70);
		BUTTON_HOVER = new Color(80,80,80);
		TOP = Color.BLACK;
		FOREGROUND = Color.WHITE;
		FOREGROUND2 = new Color(140,140,140);

		Overlay.refreshUI(true);
	}
	//endregion

	//region Light Mode
	private static void setLight() {
		MAIN = new Color(230,230,230);
		BUTTON = new Color(210,210,210);
		HOVER = new Color(211,211,211);
		SUB_MAIN = new Color(240,240,240);
		SELECT = new Color(215,215,215);
		BUTTON_HOVER = new Color(200,200,200);
		TOP = Color.WHITE;
		FOREGROUND = Color.BLACK;
		FOREGROUND2 = new Color(100,100,100);
		Overlay.refreshUI(true);
	}
	//endregion

	//region Main Thread
	static void startMainThread(){
		RegistryKey personalizeStart = new RegistryKey(
				"Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");

		final int[] prevTheme = new int[1];
		try {
			 prevTheme[0] = ((RegDWORDValue) personalizeStart.getValue("AppsUseLightTheme")).getIntValue();
		}
		catch (NullPointerException e){
			prevTheme[0] = 1;
		}
		if(prevTheme[0] == 0) {
			Defaults.setDark();
			dark.set(false);
		}
		else if (prevTheme[0] == 1) {
			Defaults.setLight();
			dark.set(true);
		}
		Thread modCheck = new Thread(() ->{
			while(true){
				try{
					mods = Channel.getChannel(Settings.channel, Main.getChatBot()).getMods();
					for(int i = 0; i < mods.size(); i++){
						System.out.println(mods.get(i));
					}

				}
				catch (Exception e){
					e.printStackTrace();
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		modCheck.start();
		Thread thread = new Thread(() -> {
			while(true){

				int minute;
				int hour;
				String half;
				LocalDateTime now = LocalDateTime.now();
				minute = now.getMinute();
				hour = now.getHour();
				half = "AM";
				if(hour >= 12) {
					if(hour != 12) {
						hour = hour - 12;
					}
					half = "PM";
				}
				if(hour == 0) {
					hour = 12;
				}
				MainBar.setTime(hour + ":" + String.format("%02d", minute) + " " + half);
				RegistryKey personalize = new RegistryKey(
							"Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");

				int theme = 0;
				try {
					theme = ((RegDWORDValue) personalize.getValue("AppsUseLightTheme")).getIntValue();
				}
				catch (NullPointerException ignored){
				}
				if(theme == 0 && prevTheme[0] == 1) {
					Defaults.setDark();
					dark.set(false);
					prevTheme[0] = 0;
				}
				else if (theme == 1 && prevTheme[0] == 0) {
					Defaults.setLight();
					dark.set(true);
					prevTheme[0] = 1;
				}
				if(!Settings.windowedMode) {
					screenSize = GraphicsEnvironment
							.getLocalGraphicsEnvironment()
							.getScreenDevices()[screenNum].getDefaultConfiguration().getBounds();
					if (!screenSize.equals(prevScreenSize)) {
						Overlay.refreshUI(false);
					}
					prevScreenSize = screenSize;
				}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

			}
		});
		thread.start();

	}
	//endregion
}
