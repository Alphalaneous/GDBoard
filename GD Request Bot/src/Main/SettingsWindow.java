package Main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SettingsWindow {
	static int width = 622;
	static int height = 622;
	private static JPanel window = new InnerWindow("Settings", 1920 / 2 - 250, 1080 / 2 - 300, width, height,
			"src/resources/Icons/Actions.png").createPanel();
	static JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	static JPanel content = new JPanel();
	static JPanel blankSpace = new JPanel();

	static JButtonUI defaultUI = new JButtonUI();
	static JButtonUI selectUI = new JButtonUI();

	static void createPanel() {

		blankSpace.setBounds(1, 31, 208, 20);
		blankSpace.setBackground(Defaults.MAIN);

		buttons.setBounds(1, 51, 208, height - 20);
		buttons.setBackground(Defaults.MAIN);

		content.setBounds(208, 31, 415, height);
		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);
		content.add(SettingsPanels.GeneralSetttings.createPanel());

		JButton general = createButton("General");
		JButton overlays = createButton("Overlays");
		JButton accounts = createButton("Accounts");
		JButton shortcuts = createButton("Shortcuts");
		JButton personalization = createButton("Personalization");
		JButton blocked = createButton("Blocked IDs");

		buttons.add(general);
		buttons.add(overlays);
		buttons.add(accounts);
		buttons.add(shortcuts);
		buttons.add(personalization);
		buttons.add(blocked);

		window.add(blankSpace);
		window.add(buttons);
		window.add(content);
		((InnerWindow) window).setPinVisible(false);
		((InnerWindow) window).refreshListener();
		Overlay.addToFrame(window);
	}

	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		blankSpace.setBackground(Defaults.MAIN);
		defaultUI.setBackground(Defaults.MAIN);
		defaultUI.setHover(Defaults.HOVER);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);
		buttons.setBackground(Defaults.MAIN);
		content.setBackground(Defaults.SUB_MAIN);
		for (Component component : buttons.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.MAIN);

			}
		}
	}

	static void toggleVisible() {
		((InnerWindow) window).toggle();
	}

	static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

	static void setVisible() {
		((InnerWindow) window).setVisible();
	}

	static JButton createButton(String text) {

		defaultUI.setBackground(Defaults.MAIN);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);

		JButton button = new JButton();
		JLabel label = new JLabel(text);

		label.setBounds(20, 9, 208, 20);
		label.setForeground(Defaults.FOREGROUND);

		button.setLayout(null);
		button.add(label);

		button.setBackground(Defaults.MAIN);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setPreferredSize(new Dimension(208, 38));

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);

				for (Component component2 : button.getComponents()) {
					if (component2 instanceof JLabel) {
						System.out.println(((JLabel) component2).getText());

						if (((JLabel) component2).getText().equals("General")) {
							content.remove(0);
							content.add(SettingsPanels.GeneralSetttings.createPanel());
							content.updateUI();
						}
						else if (((JLabel) component2).getText().equals("Overlays")) {
							content.remove(0);
							content.add(SettingsPanels.OverlaySettings.createPanel());
							content.updateUI();
						}
						else if (((JLabel) component2).getText().equals("Accounts")) {
							content.remove(0);
							content.add(SettingsPanels.AccountSettings.createPanel());
							content.updateUI();
						}
						else if (((JLabel) component2).getText().equals("Shortcuts")) {
							content.remove(0);
							content.add(SettingsPanels.ShortcutSettings.createPanel());
							content.updateUI();
						}
						else if (((JLabel) component2).getText().equals("Personalization")) {
							content.remove(0);
							content.add(SettingsPanels.PersonalizationSettings.createPanel());
							content.updateUI();
						}
						else if (((JLabel) component2).getText().equals("Blocked IDs")) {
							content.remove(0);
							content.add(SettingsPanels.BlockedIDsSettings.createPanel());
							content.updateUI();
						}
						break;
					}
				}
				for (Component component : buttons.getComponents()) {
					if (component instanceof JButton) {
						((JButton) component).setUI(defaultUI);
						component.setBackground(Defaults.MAIN);
						buttons.updateUI();
						
					}
				}
				button.setUI(selectUI);
			}
		});
		return button;
	}
}
