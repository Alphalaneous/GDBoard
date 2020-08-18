package Main;

import Main.SettingsPanels.AccountSettings;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Json.JsonObject;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

class GDBoardBot {
	static int wait = 2000;
	static int tries = 0;
	static boolean connected = false;
	static boolean failed = false;
	private static PrintWriter out;
	private static BufferedReader in;
	private static Socket clientSocket;
	private static JButtonUI defaultUI = new JButtonUI();
	private static ChatReader chatReader;
	public static ChannelPointListener channelPointListener;
	public static boolean firstOpen = true;
	static void start() throws IOException {
		if(clientSocket != null && clientSocket.isConnected() ){
			clientSocket.close();
			out.close();
			in.close();
		}

		defaultUI.setBackground(new Color(50, 50, 50));
		defaultUI.setHover( new Color(80, 80, 80));
		defaultUI.setSelect( new Color(70, 70, 70));


		try {
			clientSocket = new Socket("165.227.53.200", 2963);
			//clientSocket = new Socket("localhost", 2963);
		} catch (ConnectException | NoRouteToHostException e) {
			System.out.println("failed here");
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			tries++;
			wait = wait * 2;
			if(Main.programLoaded){
				wait = 2000;
			}
			start();
			return;
		}
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		JSONObject authObj = new JSONObject();
		authObj.put("request_type", "connect");
		authObj.put("oauth", Settings.getSettings("oauth"));
		sendMessage(authObj.toString());

		new Thread(() -> {
			if(!firstOpen) {
				DialogBox.setUnfocusable();
			}
			firstOpen = false;
			String choice = DialogBox.showDialogBox("$CONNECTING_GDBOARD$", "$CONNECTING_GDBOARD_INFO$", "$CONNECTING_GDBOARD_SUBINFO$", new String[]{"$RECONNECT$", "$CANCEL$"});
			if(choice.equalsIgnoreCase("CANCEL")){
				Main.close();
			}
			if(choice.equalsIgnoreCase("RECONNECT")){
				APIs.success.set(false);
				APIs.setOauth(false);

			}
		}).start();


		 new Thread(() -> {
			String inputLine = null;
			while (true) {
				while(clientSocket.isClosed() || !clientSocket.isConnected()){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					if ((inputLine = in.readLine()) == null) break;
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
				String event = "";
				try {
					JsonObject object = JsonObject.readFrom(inputLine);
					if (object.get("event") != null) {
						event = object.get("event").toString().replaceAll("\"", "");
					}
					if (event.equalsIgnoreCase("connected")) {
						DialogBox.closeDialogBox();
						String channel =  object.get("username").toString().replaceAll("\"", "").replaceAll("#", "");
						Settings.channel = channel;
						Settings.writeSettings("channel", channel);
						AccountSettings.refreshTwitch(channel);
						connected = true;
						/**
						 * Reads chat as streamer, reduces load on servers for some actions
						 * such as custom commands that don't use the normal prefix
						 */
						new Thread(() -> {
							if (chatReader != null && chatReader.isRunning()) {
								chatReader.stop();
							}
							chatReader = new ChatReader();
							chatReader.connect();
							chatReader.joinChannel(Settings.getSettings("channel"));
							chatReader.start();
						}).start();
					}
					else if (event.equalsIgnoreCase("connect_failed") || (event.equalsIgnoreCase("error"))) {
						failed = true;
					} if ((event.equalsIgnoreCase("command") || event.equalsIgnoreCase("level_request")) && Main.allowRequests) {
						String sender = object.get("sender").toString().replaceAll("\"", "");
						String message = StringEscapeUtils.unescapeJava(object.get("message").toString());

						message = message.substring(1, message.length()-1);
						boolean mod = object.get("mod").asBoolean();
						boolean sub = object.get("sub").asBoolean();
						String finalMessage = message;
						Thread thread1 = new Thread(() -> {
							try {
								while(ServerChatBot.processing){
									Thread.sleep(50);
								}
								ServerChatBot.onMessage(sender, finalMessage, mod, sub, 0);
							} catch (Exception e) {
								e.printStackTrace();
							}
						});
						thread1.start();
					}
					if ((event.equalsIgnoreCase("cheer") && Main.allowRequests)) {
						String sender = object.get("sender").toString().replaceAll("\"", "");
						String message = object.get("message").toString().replaceAll("\"", "");
						int bits = Integer.parseInt(object.get("bits").toString().replaceAll("\"", ""));

						boolean mod = object.get("mod").asBoolean();
						boolean sub = object.get("sub").asBoolean();
							try {
								while(ServerChatBot.processing){
									Thread.sleep(50);
								}
								ServerChatBot.onMessage(sender, message, mod, sub, bits);
							} catch (Exception e) {
								e.printStackTrace();
							}
					}
				}
				catch (Exception e){
					e.printStackTrace();
					try {
						Thread.sleep(wait);
					} catch (InterruptedException f) {
						f.printStackTrace();
					}
					tries++;
					wait = wait*2;
					if(Main.programLoaded){
						wait = 2000;
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			tries++;
			wait = wait*2;
			if(Main.programLoaded){
				wait = 2000;
			}
		}).start();
	}
	static void sendMessage(String message){
		out.println(message);
	}

	static void sendMainMessage(String message) {
		Channel channel = Channel.getChannel(Settings.getSettings("channel"), chatReader);
		chatReader.sendMessage(message, channel);
	}

}
