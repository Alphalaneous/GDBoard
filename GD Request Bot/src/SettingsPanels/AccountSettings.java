package SettingsPanels;

import Main.Defaults;
import Main.Main;
import Main.Overlay;
import Main.Settings;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.URI;

import javax.swing.*;

public class AccountSettings {

	public static JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		JButton button = new JButton("Reauthenticate (Requires Restart)");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Thread thread = new Thread(() -> {
					try {
						Twitch twitch = new Twitch();                                            //Create Twitch object from TwitchAPI
						URI callbackUri = new URI("http://127.0.0.1:23522/authorize.html"); //Set Callback URL to go to when auth success

						twitch.setClientId("fzwze6vc6d2f7qodgkpq2w8nnsz3rl");                    //Set my app client ID to get oauth
						URI authUrl = new URI(twitch.auth().getAuthenticationUrl(            //Create URI to get oauth token from twitch
								twitch.getClientId(), callbackUri, Scopes.USER_READ
						) + "chat:edit+chat:read+whispers:read+whispers:edit&force_verify=true");

						Desktop.getDesktop().browse(authUrl);                                    //Open in the default browser
						if (twitch.auth().awaitAccessToken()) {                                    //If oauth retrieving succeeds, set oauth in settings
							Settings.setOAuth(twitch.auth().getAccessToken());
							Settings.writeSettings("oauth", twitch.auth().getAccessToken());
						} else {                                                                    //Else print error
							System.out.println(twitch.auth().getAuthenticationError());
						}
					}
					catch (Exception ignored){
					}
				});
				thread.start();
			}
		});
		panel.add(button);
		return panel;
		
	}
}
