/*package Main;

import Main.SettingsPanels.AccountSettings;
import com.cavariux.twitchirc.Json.JsonObject;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.http.WebSocket;

class ChannelPointListener {
	static int wait = 2000;
	static int tries = 0;
	static boolean connected = false;
	static boolean failed = false;
	private static PrintWriter out;
	private static BufferedReader in;
	private static WebSocketClient clientSocket;
	public static boolean pong = false;
	static void start() throws IOException {
		try {
			clientSocket = new Socket("wss://pubsub-edge.twitch.tv", 443);
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
				String type = "";
				System.out.println(inputLine);
				try {
					JsonObject object = JsonObject.readFrom(inputLine);
					if (object.get("type") != null) {
						type = object.get("type").toString().replaceAll("\"", "");
					}
					if (type.equalsIgnoreCase("PONG")) {

					}
					if (type.equalsIgnoreCase("RESPONSE")){

					}
					if (type.equalsIgnoreCase("reward-redeemed")){
						String user  = object.get("data").asObject().get("redemption").asObject().get("user").asObject().get("login").toString();
						String title = object.get("data").asObject().get("redemption").asObject().get("reward").asObject().get("title").toString();

					}

				}
				catch (Exception e){
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
			System.out.println("failed here");
			try {
				start();
				Thread.sleep(1000);
				//todo

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
		clientSocket = new Socket("wss://pubsub-edge.twitch.tv", 443);
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		start();
		//todo
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
*/