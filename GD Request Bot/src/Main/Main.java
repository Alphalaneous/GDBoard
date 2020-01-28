package Main;

import com.cavariux.twitchirc.Chat.Channel;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;

import javax.swing.*;
import java.awt.AWTException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Main {
	private static ChatBot bot;

	public static void main(String[] args) throws IOException, IllegalArgumentException, URISyntaxException, AWTException {
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
				//Settings.setOAuth(accessToken);
			} else {
				System.out.println(twitch.auth().getAuthenticationError());
			}
		}

		bot = new ChatBot();
		GetTheme theme = new GetTheme();
		theme.start();

		/*
		 * FileDialog fd = new FileDialog(new JFrame()); fd.setVisible(true); File[] f =
		 * fd.getFiles(); if(f.length > 0){
		 * System.out.println(fd.getFiles()[0].getAbsolutePath()); } 
		 */

		Overlay.setFrame();
		KeyListener.hook();
		MainBar.createBar();

		//if (Settings.isRequests()) {
			LevelsWindow2.createPanel();
			ActionsWindow.createPanel();
			InfoWindow.createWindow();
			InfoWindow.refreshInfo();
			CommentsWindow.createPanel();
			SongWindow.createPanel();
			SettingsWindow.createPanel();
			SongWindow.refreshInfo();
		//}

		Time time = new Time();
		time.start();
		Overlay.setVisible();

		//if (Settings.isRequests()) {
			bot.connect("irc.chat.twitch.tv", 80);
			Channel channel = bot.joinChannel("#alphalaneous");
			bot.sendMessage("Thank you for using RequestBot by Alphalaneous! Type !help for list of commands!",
					channel);
			bot.start();
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
