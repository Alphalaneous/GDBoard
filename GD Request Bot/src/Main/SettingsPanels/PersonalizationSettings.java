package Main.SettingsPanels;

import Main.*;
import Main.InnerWindows.CommentsWindow;
import Main.InnerWindows.InfoWindow;
import Main.InnerWindows.LevelsWindow;
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
	private static CurvedButton windowedButton = new CurvedButton("Switch to Windowed Mode");
	private static JPanel panel = new JPanel(null);


	public static JPanel createPanel() {
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);

		if(Settings.getSettings("windowed").equalsIgnoreCase("true")){
			windowedButton.setLText("Switch to Overlay Mode");
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
						option = DialogBox.showDialogBox("Switch to Overlay?", "<html>If you play GD in fullscreen, this may not work, move it to another monitor by dragging the time to fix. Default is 'Home' key to open. You'll have to reopen GDBoard.", "", new String[]{"Yes", "No"});
					}
					else{
						option = DialogBox.showDialogBox("Switch to Windowed?", "This will close GDboard and set to Windowed mode.", "You will have to reopen GDBoard.", new String[]{"Yes", "No"});
					}

					if (option.equalsIgnoreCase("Yes")) {
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

		panel.add(windowedButton);

		return panel;
		
	}
	public static void refreshUI() {
		panel.setBackground(Defaults.SUB_MAIN);
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
