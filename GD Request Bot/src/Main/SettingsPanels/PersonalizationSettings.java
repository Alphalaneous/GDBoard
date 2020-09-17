package Main.SettingsPanels;

import Main.*;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

import static Main.Defaults.settingsButtonUI;

public class PersonalizationSettings {
	public static boolean onTopOption = false;
	public static boolean disableNotifOption = false;
	private static CurvedButton windowedButton = new CurvedButton("$SWITCH_WINDOWED$");
	private static JPanel panel = new JPanel(null);
	private static RadioPanel themePanel = new RadioPanel(new String[]{"$LIGHT_MODE$", "$DARK_MODE$", "$SYSTEM_MODE$"});
	public static String theme = "SYSTEM_MODE";
	public static LangLabel themeText = new LangLabel("$THEME_TEXT$");
	private static CheckboxButton onTop = createButton("$ALWAYS_ON_TOP$", 150);
	private static CheckboxButton notifications = createButton("$DISABLE_NOTIFICATIONS$", 180);

	public static JPanel createPanel() {

		themeText.setBounds(25, 15, 365, 30);
		themeText.setFont(Defaults.SEGOE.deriveFont(14f));
		themeText.setForeground(Defaults.FOREGROUND2);
		themeText.setOpaque(false);
		themePanel.setBounds(25,50,365,500);
		onTop.setChecked(false);
		onTop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				onTopOption = onTop.getSelectedState();
				Windowed.setOnTop(onTop.getSelectedState());
			}
		});
		notifications.setChecked(false);
		notifications.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				disableNotifOption = notifications.getSelectedState();
			}
		});

		for(RadioButton button : themePanel.buttons){
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {

						System.out.println(themePanel.getSelectedButton());
						theme = themePanel.getSelectedButton();
						if (theme.equalsIgnoreCase("DARK_MODE")) {
							Defaults.setDark();
						} else if (theme.equalsIgnoreCase("LIGHT_MODE")) {
							Defaults.setLight();
						} else {
							Defaults.setSystem();
						}

				}
			});
		}
		themePanel.setChecked("SYSTEM_MODE");
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);

		if(Settings.getSettings("windowed").equalsIgnoreCase("true")){
			windowedButton.setLText("$SWITCH_OVERLAY$");
		}
		windowedButton.setBounds(25,25, 365,30);
		windowedButton.setPreferredSize(new Dimension(365,30));
		windowedButton.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		windowedButton.setUI(settingsButtonUI);
		windowedButton.setForeground(Defaults.FOREGROUND);
		windowedButton.setBackground(Defaults.BUTTON);
		windowedButton.refresh();

		windowedButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				new Thread(()->{
					String option = null;
					if(Settings.getSettings("windowed").equalsIgnoreCase("true")){
						option = DialogBox.showDialogBox("$SWITCH_TO_OVERLAY_TITLE$", "<html> $SWITCH_TO_OVERLAY_INFO$ </html>", "", new String[]{"$YES$", "$NO$"});
					}
					else{
						option = DialogBox.showDialogBox("$SWITCH_TO_WINDOWED_TITLE$", "$SWINTH_TO_WINDOWED_INFO$", "$SWITCH_TO_WINDOWED_SUBINFO$", new String[]{"$YES$", "$NO$"});
					}

					if (option.equalsIgnoreCase("YES")) {
					try {
						if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
							//destroyPanels();
							//Windowed.destroyWindow();
							Settings.writeSettings("windowed", "false");
							//Overlay.createOverlay();
							//SettingsWindow.destroySettings();
							//Reflections innerReflections = new Reflections("Main.InnerWindows", new SubTypesScanner(false));
							//Set<Class<?>> innerClasses =
							//		innerReflections.getSubTypesOf(Object.class);
							//for (Class<?> Class : innerClasses) {
							//	Method method = Class.getMethod("createPanel");
							//	method.invoke(null);
							//}
							//SettingsWindow.createPanel();
							//Reflections settingsReflections = new Reflections("Main.SettingsPanels", new SubTypesScanner(false));
							//Set<Class<?>> settingsClasses =
							//		settingsReflections.getSubTypesOf(Object.class);
							//for (Class<?> Class : settingsClasses) {
							//	try {
							//		Method method = Class.getMethod("loadSettings");
							//		method.invoke(null);
							//	}
							//	catch (NoSuchMethodException ignored){
							//	}
							//}
							//Overlay.setVisible();
						} else {
							//destroyPanels();
							//Overlay.destroyOverlay();
							Settings.writeSettings("windowed", "true");
							//SettingsWindow.destroySettings();
							//SettingsWindow.createPanel();
							//SettingsWindow.setSettings();
							//CommentsWindow.createPanel();
							//Windowed.resetCommentSize();
							//LevelsWindow.createPanel();
							//InfoWindow.createPanel();
							//Windowed.createPanel();
							//Windowed.loadSettings();
							//Reflections settingsReflections = new Reflections("Main.SettingsPanels", new SubTypesScanner(false));
							//Set<Class<?>> settingsClasses =
							//		settingsReflections.getSubTypesOf(Object.class);
							//for (Class<?> Class : settingsClasses) {
							//	try {
							//		Method method = Class.getMethod("loadSettings");
							//		method.invoke(null);
							//	}
							//	catch (NoSuchMethodException ignored){
							//	}
							//}
							//Windowed.frame.setVisible(true);

						}
						//Thread.sleep(100);
						//Main.close();
					}
					catch (Exception f){
						f.printStackTrace();
					}
					Main.close();
					}
				}).start();
			}
		});
		panel.add(onTop);
		panel.add(notifications);
		panel.add(themePanel);
		panel.add(themeText);
		return panel;
		
	}
	private static CheckboxButton createButton(String text, int y){
		CheckboxButton button = new CheckboxButton(text);
		button.setBounds(25,y,365,30);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SEGOE.deriveFont(14f));
		button.refresh();
		return button;
	}
	public static void loadSettings(){
		if(!Settings.getSettings("theme").equalsIgnoreCase("")) {
			theme = Settings.getSettings("theme");
			themePanel.setChecked(theme);
			if(theme.equalsIgnoreCase("DARK_MODE")){
				Defaults.setDark();
			}
			else if(theme.equalsIgnoreCase("LIGHT_MODE")){
				Defaults.setLight();
			}
			else{
				Defaults.setSystem();
			}
		}
		else{
			Defaults.setSystem();
		}
		if(!Settings.getSettings("onTop").equalsIgnoreCase("")) {
			onTopOption = Boolean.parseBoolean(Settings.getSettings("onTop"));
			onTop.setChecked(onTopOption);
			Windowed.setOnTop(onTopOption);
		}
		if(!Settings.getSettings("disableNotifications").equalsIgnoreCase("")) {
			disableNotifOption = Boolean.parseBoolean(Settings.getSettings("disableNotifications"));
			notifications.setChecked(disableNotifOption);
		}
	}
	public static void setSettings(){
		try {
			Settings.writeSettings("theme", themePanel.getSelectedButton());
			Settings.writeSettings("onTop", String.valueOf(onTopOption));
			Settings.writeSettings("disableNotifications", String.valueOf(disableNotifOption));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void refreshUI() {
		themePanel.refreshUI();
		themeText.setForeground(Defaults.FOREGROUND2);
		panel.setBackground(Defaults.SUB_MAIN);
		for (Component component : panel.getComponents()) {
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}
		}
	}
	public static void destroyPanels(){
		try {
			Reflections innerReflections = new Reflections("Main.InnerWindows", new SubTypesScanner(false));
			Set<Class<?>> innerClasses =
					innerReflections.getSubTypesOf(Object.class);
			for (Class<?> Class : innerClasses) {
				Method method = Class.getMethod("destroyPanel");
				method.invoke(null);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
