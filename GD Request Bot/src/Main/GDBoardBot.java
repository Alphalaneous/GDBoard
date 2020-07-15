package Main;

import Main.SettingsPanels.AccountSettings;
import com.cavariux.twitchirc.Json.JsonObject;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.Socket;

class GDBoardBot {
	static int wait = 2000;
	static int tries = 0;
	static boolean connected = false;
	static boolean failed = false;
	private static PrintWriter out;
	private static BufferedReader in;
	private static Socket clientSocket;
	private static JButtonUI defaultUI = new JButtonUI();

	static void start() throws IOException {
		defaultUI.setBackground(new Color(50, 50, 50));
		defaultUI.setHover( new Color(80, 80, 80));
		defaultUI.setSelect( new Color(70, 70, 70));

		new Thread(() -> {
			String choice = DialogBox.showDialogBox("Connecting to Servers...", "This may take a few seconds", "If stuck here, try pressing reconnect", new String[]{"Reconnect", "Cancel"});
			if(choice.equalsIgnoreCase("Cancel")){
				Main.close();
			}
			if(choice.equalsIgnoreCase("Reconnect")){
				APIs.setOauth();
			}
		}).start();
		try {
			clientSocket = new Socket("165.227.53.200", 2963);
			//clientSocket = new Socket("localhost", 2963);
		} catch (ConnectException | NoRouteToHostException e) {
			System.out.println("failed");
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			tries++;
			wait = wait * 2;
			if(tries >= 10){
				APIs.setOauth();
				tries = 0;
			}
			start();
			return;
		}

		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		Thread thread = new Thread(() -> {
			String inputLine;
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
					break;
				}
				String event = "";
				//System.out.println(inputLine);
				try {
					JsonObject object = JsonObject.readFrom(inputLine);
					if (object.get("event") != null) {
						event = object.get("event").toString().replaceAll("\"", "");
					}
					if (event.equalsIgnoreCase("connected")) {
						connected = true;
						String channel =  object.get("username").toString().replaceAll("\"", "").replaceAll("#", "");
						Settings.channel = channel;
						Settings.writeSettings("channel", channel);
						AccountSettings.refreshTwitch(channel);
						DialogBox.closeDialogBox();
					}
					else if (event.equalsIgnoreCase("connect_failed")) {
						System.out.println(object.get("error").toString().replaceAll("\"", ""));
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
						Thread thread1 = new Thread(() -> {
							try {
								while(ServerChatBot.processing){
									Thread.sleep(50);
								}
								ServerChatBot.onMessage(sender, message, mod, sub, bits);
							} catch (Exception e) {
								e.printStackTrace();
							}
						});
						thread1.start();
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
					if(tries >= 10){
						APIs.setOauth();
						tries = 0;
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			new Thread(() -> {
				String choice = DialogBox.showDialogBox("Loading GDBoard...", "This may take a few seconds", "", new String[]{"Reconnect", "Cancel"});
				if(choice.equalsIgnoreCase("Cancel")){
					Main.close();
				}
				if(choice.equalsIgnoreCase("Reconnect")){
					APIs.setOauth();
				}
			}).start();
			try {
				start();
				Thread.sleep(1000);
				JSONObject authObj = new JSONObject();
				authObj.put("request_type", "connect");
				authObj.put("oauth", Settings.getSettings("oauth"));
				GDBoardBot.sendMessage(authObj.toString());
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tries++;
			wait = wait*2;
			if(tries >= 10){
				APIs.setOauth();
				tries = 0;
			}
		});
		thread.start();
	}
	static void sendMessage(String message){
		out.println(message);
	}
	static void restart() throws IOException {
		if(clientSocket != null) {
			clientSocket.close();
		}
		clientSocket = new Socket("165.227.53.200", 2963);
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JSONObject authObj = new JSONObject();
		authObj.put("request_type", "connect");
		authObj.put("oauth", Settings.getSettings("oauth"));
		GDBoardBot.sendMessage(authObj.toString());
	}
}
