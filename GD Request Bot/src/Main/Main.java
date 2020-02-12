package Main;

import Chat.Channel;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class Main {
	private static ChatBot bot;

	public static void main(String[] args) throws IOException, IllegalArgumentException, URISyntaxException, AWTException {

		Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.INFO);
		if (!Settings.hasOauth()) {

			Twitch twitch = new Twitch();
			twitch.setClientId("fzwze6vc6d2f7qodgkpq2w8nnsz3rl");

			URI callbackUri = new URI("http://127.0.0.1:23522/authorize.html");

			String authURLString = twitch.auth().getAuthenticationUrl(twitch.getClientId(), callbackUri,
					Scopes.USER_READ) + "chat:edit+chat:read+whispers:read+whispers:edit";
			URI authUrl = new URI(authURLString);

			java.awt.Desktop.getDesktop().browse(authUrl);

			boolean authSuccess = twitch.auth().awaitAccessToken();

			if (authSuccess) {
				String accessToken = twitch.auth().getAccessToken();
				System.out.println("Access Token: " + accessToken);
			} else {
				System.out.println(twitch.auth().getAuthenticationError());
			}
		}

		bot = new ChatBot();

		 /*FileDialog fd = new FileDialog(new JFrame()); fd.setVisible(true); File[] f =
		 fd.getFiles(); if(f.length > 0){
		 System.out.println(fd.getFiles()[0].getAbsolutePath()); }*/

		Overlay.setFrame();
		KeyListener.hook();
		MainBar.createBar();
		Defaults.startMainThread();
		//if (Settings.isRequests()) {
			LevelsWindow.createPanel();
			ActionsWindow.createPanel();
			InfoWindow.createWindow();
			InfoWindow.refreshInfo();
			CommentsWindow.createPanel();
			SongWindow.createPanel();
			//SettingsWindow.createPanel();
			SongWindow.refreshInfo();
		//}

		//Time time = new Time();
		//time.start();
		Overlay.setVisible();

		//if (Settings.isRequests()) {
			bot.connect("irc.chat.twitch.tv", 80);
			Channel channel = bot.joinChannel("#alphalaneous");
			bot.sendMessage("Thank you for using RequestBot by Alphalaneous! Type !help for list of commands!",
					channel);
			while(true) {

				bot.start();
			}

		//}

		/*
		 * SUGGESTIONS Report Button Discord Bot to send reports r/place in GD Custom
		 * Song downloader Discord bot to send GD Messages Periodic messages and tips
		 */

	}

	static void sendMessage(String message) {
		bot.sendMessage(message, Channel.getChannel("#alphalaneous", bot));
	}
}
