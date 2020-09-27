package Main;

import Main.SettingsPanels.AccountSettings;
import Main.SettingsPanels.GeneralBotSettings;
import Main.SettingsPanels.GeneralSettings;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Json.JsonArray;
import com.cavariux.twitchirc.Json.JsonObject;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

class GDBoardBot {
	static int wait = 2000;
	static int tries = 0;
	static boolean initialConnect = false;
	static AtomicBoolean isConnect = new AtomicBoolean(false);
	static boolean failed = false;
	private static PrintWriter out;
	private static BufferedReader in;
	private static Socket clientSocket;
	static {
		while(true) {
			try {
				clientSocket = new Socket("165.227.53.200", 2963);
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				break;
			} catch (ConnectException | NoRouteToHostException e) {
				System.out.println("failed here");
				try {
					Thread.sleep(wait);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				tries++;
				wait = wait * 2;
				if (Main.programLoaded) {
					wait = 2000;
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private static JButtonUI defaultUI = new JButtonUI();
	private static ChatReader chatReader;
	public static ChannelPointListener channelPointListener;
	public static boolean firstOpen = true;
	static void start() throws IOException {
		start(false);
	}
	static void start(boolean reconnect) throws IOException {
		/*if(clientSocket != null && clientSocket.isConnected() ){
			clientSocket.close();
			out.close();
			in.close();
		}*/

		defaultUI.setBackground(new Color(50, 50, 50));
		defaultUI.setHover( new Color(80, 80, 80));
		defaultUI.setSelect( new Color(70, 70, 70));




		JSONObject authObj = new JSONObject();
		authObj.put("request_type", "connect");
		authObj.put("oauth", Settings.getSettings("oauth"));
		sendMessage(authObj.toString());

		new Thread(() -> {
			isConnect.set(false);
			if(!firstOpen) {
				DialogBox.setUnfocusable();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				firstOpen = false;
			}
			if(!isConnect.get()) {
				if(!reconnect) {
					String choice = DialogBox.showDialogBox("$CONNECTING_GDBOARD$", "$CONNECTING_GDBOARD_INFO$", "$CONNECTING_GDBOARD_SUBINFO$", new String[]{"$RECONNECT$", "$CANCEL$"});
					if (choice.equalsIgnoreCase("CANCEL")) {
						Main.close();
					}
					if (choice.equalsIgnoreCase("RECONNECT")) {
						APIs.success.set(false);
						APIs.setOauth(false);

					}
				}
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
				System.out.println(inputLine);

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
						initialConnect = true;
						isConnect.set(true);
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
						if(GeneralBotSettings.multiOption){
							String finalMessage = message;
							new Thread(() -> {
								try {
									while(ServerChatBot.processing){
										Thread.sleep(50);
									}
									ServerChatBot.onMessage(sender, finalMessage, mod, sub, 0);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}).start();
						}
						else{
							String finalMessage = message;
							try {
								while(ServerChatBot.processing){
									Thread.sleep(50);
								}
								ServerChatBot.onMessage(sender, finalMessage, mod, sub, 0);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					if ((event.equalsIgnoreCase("cheer") && Main.allowRequests)) {
						String sender = object.get("sender").toString().replaceAll("\"", "");
						String message = object.get("message").toString().replaceAll("\"", "");
						int bits = Integer.parseInt(object.get("bits").toString().replaceAll("\"", ""));

						boolean mod = object.get("mod").asBoolean();
						boolean sub = object.get("sub").asBoolean();
						if(GeneralBotSettings.multiOption){
							String finalMessage = message;
							new Thread(() -> {
								try {
									while(ServerChatBot.processing){
										Thread.sleep(50);
									}
									ServerChatBot.onMessage(sender, finalMessage, mod, sub, 0);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}).start();
						}
						else{
							String finalMessage = message;
							try {
								while(ServerChatBot.processing){
									Thread.sleep(50);
								}
								ServerChatBot.onMessage(sender, finalMessage, mod, sub, 0);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					if(event.equalsIgnoreCase("blocked_ids_updated") && GeneralSettings.gdModeOption){
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
				start(true);
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
