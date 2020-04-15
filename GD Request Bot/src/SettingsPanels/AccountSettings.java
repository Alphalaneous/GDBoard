package SettingsPanels;

import Main.Defaults;
import Main.FancyTextArea;
import Main.JButtonUI;
import Main.Settings;
import Main.TwitchAPI;
import Main.CurvedButton;
import Main.CheckboxButton;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.*;

public class AccountSettings {
	private static JLabel channelText;
	private static String channel = Settings.channel.replaceAll("#", "");
	private static JButtonUI defaultUI = new JButtonUI();
	private static JPanel panel = new JPanel();

	public static JPanel createPanel() {
		//TODO update channel when reauthenticating, restart connection to GDBoard servers

		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);


		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		panel.setLayout(null);

		channelText = new JLabel("Connected to: " + channel);
		channelText.setForeground(Defaults.FOREGROUND);
		channelText.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		channelText.setBounds(25,20,channelText.getPreferredSize().width+5,channelText.getPreferredSize().height+5);

		CurvedButton button = new CurvedButton("Refresh Login with Twitch");

		button.setBackground(Defaults.BUTTON);
		button.setBounds(25,50,365,30);
		button.setPreferredSize(new Dimension(365,30));
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Thread thread = new Thread(() -> {
					try {
						TwitchAPI.setOauth();
					}
					catch (Exception ignored){
					}
				});
				thread.start();
			}
		});
		button.refresh();
		panel.add(channelText);
		panel.add(button);
		return panel;

	}
	public static void refreshChannel(){
		channelText.setText("Connected to: " + Settings.channel.replaceAll("#", ""));
	}
	public static void loadSettings(){
		try {
			if(!Settings.getSettings("channel").equalsIgnoreCase("")) {
				channel = Settings.getSettings("channel");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void refreshUI() {
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);

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
