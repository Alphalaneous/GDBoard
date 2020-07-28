package Main.SettingsPanels;

import Main.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.*;

import static Main.Defaults.settingsButtonUI;

public class AccountSettings {
	private static JLabel channelText;
	private static JLabel geometryText;

	private static JPanel panel = new JPanel();

	public static JPanel createPanel() {

		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		panel.setLayout(null);

		channelText = new JLabel("Twitch: " + Settings.getSettings("channel"));
		geometryText = new JLabel("Geometry Dash: NA");
		channelText.setForeground(Defaults.FOREGROUND);
		channelText.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		channelText.setBounds(25,50,channelText.getPreferredSize().width+5,channelText.getPreferredSize().height+5);
		geometryText.setForeground(Defaults.FOREGROUND);
		geometryText.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		geometryText.setBounds(25,20,geometryText.getPreferredSize().width+5,geometryText.getPreferredSize().height+5);

		RoundedJButton button = new RoundedJButton("\uE149", "Refresh Login");
		button.asSettings();
		button.setBackground(Defaults.BUTTON);
		button.setBounds(365,45,30,30);
		button.setPreferredSize(new Dimension(365,30));
		button.setUI(settingsButtonUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SYMBOLS.deriveFont(14f));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Thread thread = new Thread(() -> {
					try {
						APIs.setOauth();
					}
					catch (Exception ignored){
					}
				});
				thread.start();
			}
		});
		panel.add(channelText);
		panel.add(geometryText);
		panel.add(button);
		return panel;

	}
	public static void refreshTwitch(String channel){
		channelText.setText("Twitch: " + channel);

		channelText.setBounds(25,50,channelText.getPreferredSize().width+5,channelText.getPreferredSize().height+5);

	}
	public static void refreshGD(String username){
		if(LoadGD.isAuth) {
			geometryText.setText("Geometry Dash: " + username);
			geometryText.setBounds(25, 20, geometryText.getPreferredSize().width + 5, geometryText.getPreferredSize().height + 5);
		}

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
			if(component instanceof JTextArea){
				((FancyTextArea) component).refreshAll();
			}
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}
		}
	}
}
