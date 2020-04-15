package SettingsPanels;

import Main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PersonalizationSettings {
	private static CurvedButton windowedButton = new CurvedButton("Switch to Windowed Mode (Requires Restart)");
	private static JButtonUI defaultUI = new JButtonUI();


	public static JPanel createPanel() {
		JPanel panel = new JPanel(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);

		if(Settings.windowedMode){
			windowedButton.setLText("Switch to Overlay Mode (Requires Restart)");
		}
		windowedButton.setBounds(25,25, 365,30);
		windowedButton.setPreferredSize(new Dimension(365,30));
		windowedButton.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		windowedButton.setUI(defaultUI);
		windowedButton.setForeground(Defaults.FOREGROUND);
		windowedButton.setBackground(Defaults.BUTTON);
		windowedButton.refresh();

		windowedButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Object[] options = {"Yes", "No"};
				int n = JOptionPane.showOptionDialog(SettingsWindow.frame,
						"Close GDBoard and Apply Changes?",
						"Close? (Temporary Menu)", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (n == 0) {
					try {
						if (Settings.windowedMode) {
							SettingsWindow.setSettings();
							Windowed.setSettings();
							Settings.writeLocation();
							GeneralSettings.setSettings();
							RequestSettings.setSettings();
							ShortcutSettings.setSettings();
							OutputSettings.setSettings();
							Settings.writeSettings("windowed", "false");
						} else {
							ActionsWindow.setSettings();
							CommentsWindow.setSettings();
							InfoWindow.setSettings();
							LevelsWindow.setSettings();
							SongWindow.setSettings();
							SettingsWindow.setSettings();
							Windowed.setSettings();
							Settings.writeLocation();
							Settings.writeSettings("monitor", String.valueOf(Defaults.screenNum));
							GeneralSettings.setSettings();
							RequestSettings.setSettings();
							ShortcutSettings.setSettings();
							OutputSettings.setSettings();
							Settings.writeSettings("windowed", "true");
						}
						Thread.sleep(100);
					}
					catch (Exception ignored){
					}
					System.exit(0);
				}
			}
		});
		panel.add(windowedButton);
		return panel;
		
	}
}
