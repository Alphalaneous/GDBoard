package Main.SettingsPanels;

import Main.*;
import Main.Components.*;
import Main.Windows.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class PersonalizationSettings {
	public static boolean onTopOption = false;
	public static boolean disableNotifOption = false;
	private static JPanel panel = new JPanel(null);
	private static RadioPanel themePanel = new RadioPanel(new String[]{"$LIGHT_MODE$", "$DARK_MODE$", "$SYSTEM_MODE$"});
	public static String theme = "SYSTEM_MODE";
	private static LangLabel themeText = new LangLabel("$THEME_TEXT$");
	private static CheckboxButton onTop = createButton("$ALWAYS_ON_TOP$", 150);
	private static CheckboxButton notifications = createButton("$DISABLE_NOTIFICATIONS$", 180);

	public static JPanel createPanel() {

		themeText.setBounds(25, 15, 365, 30);
		themeText.setFont(Defaults.SEGOE.deriveFont(14f));
		themeText.setForeground(Defaults.FOREGROUND2);
		themeText.setOpaque(false);
		themePanel.setBounds(25, 50, 365, 500);
		onTop.setChecked(false);
		onTop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				onTopOption = onTop.getSelectedState();
				Window.setOnTop(onTop.getSelectedState());
				Settings.writeSettings("onTop", String.valueOf(onTopOption));
			}
		});
		notifications.setChecked(false);
		notifications.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				disableNotifOption = notifications.getSelectedState();
				Settings.writeSettings("disableNotifications", String.valueOf(disableNotifOption));
			}
		});

		for (RadioButton button : themePanel.buttons) {
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
					Settings.writeSettings("theme", themePanel.getSelectedButton());

				}
			});
		}
		themePanel.setChecked("SYSTEM_MODE");
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);

		panel.add(onTop);
		panel.add(notifications);
		panel.add(themePanel);
		panel.add(themeText);
		return panel;

	}

	private static CheckboxButton createButton(String text, int y) {
		CheckboxButton button = new CheckboxButton(text, PersonalizationSettings.class);
		button.setBounds(25, y, 365, 30);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SEGOE.deriveFont(14f));
		button.refresh();
		return button;
	}

	public static void loadSettings() {
		if (!Settings.getSettings("theme").equalsIgnoreCase("")) {
			theme = Settings.getSettings("theme");
			themePanel.setChecked(theme);
			if (theme.equalsIgnoreCase("DARK_MODE")) {
				Defaults.setDark();
			} else if (theme.equalsIgnoreCase("LIGHT_MODE")) {
				Defaults.setLight();
			} else {
				Defaults.setSystem();
			}
		} else {
			Defaults.setSystem();
		}
		if (!Settings.getSettings("onTop").equalsIgnoreCase("")) {
			onTopOption = Boolean.parseBoolean(Settings.getSettings("onTop"));
			onTop.setChecked(onTopOption);
			Window.setOnTop(onTopOption);
		}
		if (!Settings.getSettings("disableNotifications").equalsIgnoreCase("")) {
			disableNotifOption = Boolean.parseBoolean(Settings.getSettings("disableNotifications"));
			notifications.setChecked(disableNotifOption);
		}
	}

	public static void refreshUI() {
		themePanel.refreshUI();
		themeText.setForeground(Defaults.FOREGROUND2);
		panel.setBackground(Defaults.SUB_MAIN);
		for (Component component : panel.getComponents()) {
			if (component instanceof CheckboxButton) {
				((CheckboxButton) component).refresh();
			}
		}
	}
}
