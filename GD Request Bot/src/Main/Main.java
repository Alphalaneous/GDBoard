package Main;

import Chat.Channel;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URI;


public class Main {

	private static ChatBot bot;

	//region Main
	public static void main(String[] args) throws IllegalArgumentException {
		//TODO Keybinds



		try {

			Settings.writeSettings("keybind", String.valueOf(KeyEvent.VK_HOME));
			Settings.keybind = KeyEvent.VK_HOME;
			Settings.loadSettings(true);

			//region Get Channel
			if (!Settings.hasChannel) {
				String channel = JOptionPane.showInputDialog("Enter your channel");
				Settings.setChannel(channel);
				Settings.writeSettings("channel", channel);
			}
			if (!Settings.hasWindowed) {
				Settings.writeSettings("windowed", "false");
			}
			if(!Settings.hasMonitor){
				Settings.writeSettings("monitor", "0");
			}
			//endregion

			//region Get Twitch oauth
			if (!Settings.hasOauth) {                                                    //If the oauth token is not acquired, get one from twitch

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


			//endregion

			//region Create ChatBot and send starting message
			bot = new ChatBot();                                                        //Create ChatBot
			bot.connect();                                                                //Connect the bot
			bot.sendMessage("Thank you for using GDBoard by Alphalaneous! Type !help for list of commands!", bot.joinChannel(Settings.channel));
			//endregion

			//region Start and Create Everything
			Defaults.startMainThread();        //Starts thread that always checks for changes such as time, resolution, and color scheme
			Overlay.setFrame();                //Creates the JFrame that contains everything
			KeyListener.hook();                    //Starts a Keyboard and Controller Listener
			if (!Settings.windowedMode) {
				MainBar.createBar();            //Creates the main "Game Bar" in the top center
			}
			LevelsWindow.createPanel();            //Creates the Levels Window containing all the requests in the level queue
			ActionsWindow.createPanel();            //Creates the Action Window containing buttons that do specific actions
			InfoWindow.createPanel();            //Creates the Info Window containing the information of the selected level
			CommentsWindow.createPanel();            //Creates the Comment Window containing the comments of the selected level
			SongWindow.createPanel();            //Creates the Song Window allowing you to play the song of the selected level

			InfoWindow.refreshInfo();            //Refreshes the information shown on the Info Window for the first time
			SongWindow.refreshInfo();            //Refreshes the information shown on the Song Window for the first time

			if (Settings.windowedMode) {
				Overlay.refreshUI(true);
			}
			Settings.loadSettings(false);
			Overlay.setVisible();

			//endregion
			//region Start ChatBot and keep trying if it fails
			while (true) {                            //If the Chat Bot fails, try again
				try {
					bot.start();
				} catch (Exception e) {
					bot = new ChatBot();                                                //Create ChatBot
					bot.connect();                                                        //Connect the bot
				}
				//Start the Chat Bot
				Thread.sleep(100);            //Sleep before trying again

			}
		}
		catch (Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e, "Error",  JOptionPane.ERROR_MESSAGE);
		}
		//endregion
	}
	//endregion

	//region SendMessage to send messages as created ChatBot
	static void sendMessage(String message) {	//Send message as created static ChatBot
		bot.sendMessage(message, Channel.getChannel(Settings.channel, bot));
	}
	static ChatBot getChatBot(){
		return bot;
	}
	//endregion
}
