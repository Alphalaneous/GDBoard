package com.alphalaneous;

import com.alphalaneous.SettingsPanels.AccountSettings;
import com.alphalaneous.Windows.DialogBox;
import com.alphalaneous.Tabs.RequestsTab;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerBot2 {

	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;

	{
		try {
			clientSocket = new Socket("localhost", 2963); //test
			//clientSocket = new Socket("142.93.12.163", 2963); //new server
			//clientSocket = new Socket("165.227.53.200", 2963); //current server
			
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void connect() {
		JSONObject authObj = new JSONObject();
		authObj.put("request_type", "connect");
		authObj.put("oauth", Settings.getSettings("oauth").asString());

		sendMessage(authObj.toString());

		String inputLine;
		while (true) {
			try {
				if ((inputLine = in.readLine()) == null) break;
			} catch (Exception e) {
				break;
			}
			System.out.println(inputLine);
			String event = "";
			try {
				JSONObject object = new JSONObject(inputLine);
				if (object.get("event") != null) {
					event = object.get("event").toString().replaceAll("\"", "");
				}
				System.out.println(event);
				if (event.equalsIgnoreCase("connected")) {
					System.out.println("> Connected to GDBoard Servers");
					String channel = object.get("username").toString().replaceAll("\"", "").replaceAll("#", "");
					if(object.optBoolean("is_officer")){
						RequestsTab.setOfficerVisible();
					}
					Settings.writeSettings("channel", channel);
					AccountSettings.refreshTwitch(channel);
					APIs.setAllViewers();

				} else if (event.equalsIgnoreCase("connect_failed")) {
					clientSocket.close();
					break;
				}
				if (event.equalsIgnoreCase("blocked_ids_updated") && Settings.getSettings("gdMode").asBoolean()) {
					System.out.println("> Blocked IDs Updated");
					String[] IDs = object.get("ids").toString().replace("\"", "").replace("{", "").replace("}", "").replace("\\", "").split(",");
					for (String ID : IDs) {
						try {
							Requests.globallyBlockedIDs.put(Long.parseLong(ID.split(":", 2)[0]), ID.split(":", 2)[1]);
						}
						catch (NumberFormatException ignored){
						}
					}

				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		System.out.println("dead");
	}

	static void showReconnectDialog() {
		String choice = DialogBox.showDialogBox("$CONNECTING_GDBOARD$", "$CONNECTING_GDBOARD_INFO$", "$CONNECTING_GDBOARD_SUBINFO$", new String[]{"$RECONNECT$", "$CANCEL$"});
		if (choice.equalsIgnoreCase("CANCEL")) {
			Main.close();
		}
		if (choice.equalsIgnoreCase("RECONNECT")) {
			APIs.success.set(false);
			APIs.setOauth();

		}
	}

	public void sendMessage(String message) {
		out.println(message);
	}

	void disconnect() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
