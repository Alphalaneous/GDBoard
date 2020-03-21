package SettingsPanels;

import Main.Defaults;
import Main.FancyTextArea;
import Main.JButtonUI;
import Main.Settings;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;
import Main.CurvedButton;
import Main.Main;
import Main.CheckboxButton;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.*;

public class AccountSettings {

	private static String channel = "";
	private static FancyTextArea channelInput = new FancyTextArea(false);
	private static JButtonUI defaultUI = new JButtonUI();
	private static JPanel panel = new JPanel();

	public static JPanel createPanel() {


		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);


		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		panel.setLayout(null);

		JLabel channelText = new JLabel("Channel:");
		channelText.setForeground(Defaults.FOREGROUND);
		channelText.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		channelText.setBounds(25,20,channelText.getPreferredSize().width+5,channelText.getPreferredSize().height+5);

		CurvedButton confirmChannel = new CurvedButton("\uE001");
		confirmChannel.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 22));
		confirmChannel.setBounds(330, 46, 60, 30);
		confirmChannel.setPreferredSize(new Dimension(60,30));
		confirmChannel.setUI(defaultUI);
		confirmChannel.setForeground(Defaults.FOREGROUND);
		confirmChannel.setBackground(Defaults.BUTTON);
		confirmChannel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				channel = channelInput.getText();
				Settings.setChannel(channel);
				try {
					Settings.writeSettings("channel", channel);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Thread thread = new Thread(Main::startBot);
				thread.start();
			}
		});
		channelInput.setBounds(25,45,290,32);
		channelInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		CurvedButton button = new CurvedButton("Reauthenticate Twitch");

		button.setBackground(Defaults.BUTTON);
		button.setBounds(25,90,365,30);
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
						Twitch twitch = new Twitch();                                            //Create TwitchAPI object from TwitchAPI
						URI callbackUri = new URI("http://127.0.0.1:23522/authorize.html"); //Set Callback URL to go to when auth success

						twitch.setClientId("fzwze6vc6d2f7qodgkpq2w8nnsz3rl");                    //Set my app client ID to get oauth
						URI authUrl = new URI(twitch.auth().getAuthenticationUrl(            //Create URI to get oauth token from twitch
								twitch.getClientId(), callbackUri, Scopes.USER_READ
						) + "chat:edit+chat:read+whispers:read+whispers:edit&force_verify=true");
						Runtime rt = Runtime.getRuntime();
						rt.exec("rundll32 url.dll,FileProtocolHandler " + authUrl);                //Open in the default browser
						if (twitch.auth().awaitAccessToken()) {                                    //If oauth retrieving succeeds, set oauth in settings
							Settings.setOAuth(twitch.auth().getAccessToken());
							Settings.writeSettings("oauth", twitch.auth().getAccessToken());
							Main.startBot();
						} else {                                                                    //Else print error
							JOptionPane.showMessageDialog(null, "Failed to Authenticate Twitch account", "Error",  JOptionPane.ERROR_MESSAGE);
							System.out.println(twitch.auth().getAuthenticationError());
						}
					}
					catch (Exception ignored){
					}
				});
				thread.start();
			}
		});
		button.refresh();
		panel.add(confirmChannel);
		panel.add(channelText);
		panel.add(channelInput);
		panel.add(button);
		confirmChannel.refresh();
		return panel;

	}
	public static void loadSettings(){
		try {
			if(!Settings.getSettings("channel").equalsIgnoreCase("")) {
				channel = Settings.getSettings("channel");
				channelInput.setText(channel);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void refreshUI() {
		defaultUI.setBackground(Defaults.MAIN);
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
				component.setBackground(Defaults.MAIN);
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
