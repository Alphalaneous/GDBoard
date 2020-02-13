package Main;

import Chat.Channel;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;


public class Main {

	private static ChatBot bot;

	public static void main(String[] args) throws IOException, IllegalArgumentException, URISyntaxException, AWTException, InterruptedException {

		Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);		//Create root logger object
		root.setLevel(Level.INFO);													//Disable all logging except for INFO

		Settings.setOAuth("meuwul05bn5t246c9tmg490z735oqv");						//Temporary Setting initialization
		Settings.setChannel("Alphalaneous");										//TODO Make actual settings file to initialize from

		if (!Settings.hasOauth()) {													//If the oauth token is not acquired, get one from twitch

			Twitch twitch = new Twitch();											//Create Twitch object from TwitchAPI
			URI callbackUri = new URI("http://127.0.0.1:23522/authorize.html"); //Set Callback URL to go to when auth success

			twitch.setClientId("fzwze6vc6d2f7qodgkpq2w8nnsz3rl");					//Set my app client ID to get oauth
			URI authUrl = new URI(twitch.auth().getAuthenticationUrl(			//Create URI to get oauth token from twitch
					twitch.getClientId(), callbackUri, Scopes.USER_READ
			) + "chat:edit+chat:read+whispers:read+whispers:edit&force_verify=true");

			Desktop.getDesktop().browse(authUrl);									//Open in the default browser
			if (twitch.auth().awaitAccessToken()) {									//If oauth retrieving succeeds, set oauth in settings
				Settings.setOAuth(twitch.auth().getAccessToken());
			}
			else {																	//Else print error
				System.out.println(twitch.auth().getAuthenticationError());
			}
		}

		bot = new ChatBot();														//Create ChatBot
		bot.connect();																//Connect the bot
		bot.sendMessage("Thank you for using GDBoard by Alphalaneous! Type !help for list of commands!", bot.joinChannel(Settings.channel));

		Defaults.       startMainThread();		//Starts thread that always checks for changes such as time, resolution, and color scheme
		Overlay.        setFrame();				//Creates the JFrame that contains everything
		KeyListener.    hook();					//Starts a Keyboard and Controller Listener
		MainBar.        createBar();			//Creates the main "Game Bar" in the top center

		LevelsWindow.   createPanel();			//Creates the Levels Window containing all the requests in the level queue
		ActionsWindow.  createPanel();			//Creates the Action Window containing buttons that do specific actions
		InfoWindow.     createPanel();			//Creates the Info Window containing the information of the selected level
		CommentsWindow. createPanel();			//Creates the Comment Window containing the comments of the selected level
		SongWindow.     createPanel();			//Creates the Song Window allowing you to play the song of the selected level

		InfoWindow.     refreshInfo();			//Refreshes the information shown on the Info Window for the first time
		SongWindow.     refreshInfo();			//Refreshes the information shown on the Song Window for the first time

		Overlay.        setVisible();

		while(true) {							//If the Chat Bot fails, try again
			try {
				bot.start();
			}
			catch (Exception e){
				bot = new ChatBot();												//Create ChatBot
				bot.connect();														//Connect the bot
			}
												//Start the Chat Bot
			Thread.sleep(100);			//Sleep before trying again

		}
	}

	static void sendMessage(String message) {	//Send message as created static ChatBot
		bot.sendMessage(message, Channel.getChannel(Settings.channel, bot));
	}
}
