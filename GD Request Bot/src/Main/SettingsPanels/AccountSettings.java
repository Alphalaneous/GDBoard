package Main.SettingsPanels;

import Main.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.*;

import static Main.Defaults.settingsButtonUI;

public class AccountSettings {
	private static LangLabel channelText = new LangLabel("");
	private static LangLabel geometryText = new LangLabel("");
	private static RoundedJButton refreshTwitch = new RoundedJButton("\uE149", "$REFRESH_TWITCH$");
	private static RoundedJButton refreshGD = new RoundedJButton("\uE149", "$REFRESH_GD$");

	private static JPanel panel = new JPanel();

	public static JPanel createPanel() {

		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		panel.setLayout(null);

		channelText.setTextLangFormat("$TWITCH$", Settings.getSettings("channel"));
		geometryText.setTextLangFormat("$GEOMETRY_DASH$","NA");
		channelText.setForeground(Defaults.FOREGROUND);
		channelText.setFont(Defaults.SEGOE.deriveFont(14f));
		channelText.setBounds(25,50,channelText.getPreferredSize().width+5,channelText.getPreferredSize().height+5);
		geometryText.setForeground(Defaults.FOREGROUND);
		geometryText.setFont(Defaults.SEGOE.deriveFont(14f));
		geometryText.setBounds(25,20,geometryText.getPreferredSize().width+5,geometryText.getPreferredSize().height+5);


		refreshTwitch.setBackground(Defaults.BUTTON);
		refreshTwitch.setBounds(365,48,25,25);
		refreshTwitch.setPreferredSize(new Dimension(25,25));
		refreshTwitch.setUI(settingsButtonUI);
		refreshTwitch.setForeground(Defaults.FOREGROUND);
		refreshTwitch.setBorder(BorderFactory.createEmptyBorder());
		refreshTwitch.setFont(Defaults.SYMBOLS.deriveFont(14f));
		refreshTwitch.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Thread thread = new Thread(() -> {
					try {
						APIs.setOauth(false);
					}
					catch (Exception ignored){
					}
				});
				thread.start();
			}
		});

		refreshGD.setBackground(Defaults.BUTTON);
		refreshGD.setBounds(365,18,25,25);
		refreshGD.setPreferredSize(new Dimension(25,25));
		refreshGD.setUI(settingsButtonUI);
		refreshGD.setForeground(Defaults.FOREGROUND);
		refreshGD.setBorder(BorderFactory.createEmptyBorder());
		refreshGD.setFont(Defaults.SYMBOLS.deriveFont(14f));
		refreshGD.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Thread thread = new Thread(() -> {
					try {
						LoadGD.load(true);
					} catch (IOException ignored) {
					}
				});
				thread.start();
			}
		});
		
		panel.add(channelText);
		panel.add(geometryText);
		panel.add(refreshTwitch);
		panel.add(refreshGD);
		return panel;

	}
	public static void refreshTwitch(String channel){
		channelText.setTextLangFormat("$TWITCH$", channel);

		channelText.setBounds(25,50,channelText.getPreferredSize().width+5,channelText.getPreferredSize().height+5);

	}
	public static void refreshGD(String username){
		if(LoadGD.isAuth) {
			geometryText.setTextLangFormat("$GEOMETRY_DASH$", username);
			geometryText.setBounds(25, 20, geometryText.getPreferredSize().width + 5, geometryText.getPreferredSize().height + 5);
		}

	}
	public static void refreshUI() {
		refreshTwitch.setBackground(Defaults.BUTTON);
		refreshTwitch.setForeground(Defaults.FOREGROUND);
		refreshGD.setBackground(Defaults.BUTTON);
		refreshGD.setForeground(Defaults.FOREGROUND);
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
