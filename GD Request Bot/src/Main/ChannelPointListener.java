package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import Main.APIs;
import Main.Settings;
import com.cavariux.twitchirc.Json.JsonObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class ChannelPointListener extends WebSocketClient {

	private boolean pingSuccess = false;
	private static URI uri;
	static Path myPath;
	static {
		try {
			uri = Main.class.getResource("/Resources/points/").toURI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static {

		if (uri.getScheme().equals("jar")) {
			myPath = ServerChatBot.fileSystem.getPath("/Resources/points/");
		} else {
			myPath = Paths.get(uri);
		}

	}
	public ChannelPointListener(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public ChannelPointListener(URI serverURI) {
		super(serverURI);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		while(true) {
			try {
				send("{\n" +
						"  \"type\": \"LISTEN\",\n" +
						"  \"data\": {\n" +
						"    \"topics\": [\"channel-points-channel-v1." + APIs.getIDs(Settings.getSettings("channel")) + "\"],\n" +
						"    \"auth_token\": \"" + Settings.getSettings("oauth") + "\"\n" +
						"  }\n" +
						"}");
				break;
			}
			catch (NullPointerException e){

			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		new Thread(() -> {
			while(true){
				send("{\n" +
						"  \"type\": \"PING\"\n" +
						"}");
				pingSuccess = false;
				try {
					Thread.sleep(300000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(!pingSuccess){
					send("{\n" +
							"  \"type\": \"RECONNECT\"\n" +
							"}");
				}
			}
		}).start();
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(String message) {

		JsonObject object = JsonObject.readFrom(message);
		String event = object.get("type").toString().replaceAll("\"", "");
		if(event.equalsIgnoreCase("PONG")){
			pingSuccess = true;
		}
		if(event.equalsIgnoreCase("MESSAGE")){
			String topic = object.get("data").asObject().get("topic").toString().replaceAll("\"", "");

			if(topic.startsWith("channel-points-channel-v1")){
				System.out.println(message);
				String redemptionA = object.get("data").asObject().get("message").toString().replaceAll("\\\\\"", "\"");
				String redemption = JsonObject.readFrom(redemptionA.substring(1, redemptionA.length()-1)).get("data").asObject().get("redemption").asObject().get("reward").asObject().get("title").toString().replaceAll("\"", "");
				String username = JsonObject.readFrom(redemptionA.substring(1, redemptionA.length()-1)).get("data").asObject().get("redemption").asObject().get("user").asObject().get("login").toString().replaceAll("\"", "");

				boolean isUserinput = JsonObject.readFrom(redemptionA.substring(1, redemptionA.length()-1)).get("data").asObject().get("redemption").asObject().get("reward").asObject().get("is_user_input_required").asBoolean();
				String userInput = "";
				if(isUserinput) {
					userInput = JsonObject.readFrom(redemptionA.substring(1, redemptionA.length() - 1)).get("data").asObject().get("redemption").asObject().get("user_input").toString().replaceAll("\"", "");
					System.out.println(redemption + " redeemed by " + username + " with " + userInput);
				}
				else{
					System.out.println(redemption + " redeemed by " + username);
				}
				String finalUserInput = userInput;
				try {
					boolean comExists = false;
					Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/points/");
					if (Files.exists(comPath)) {
						Stream<Path> walk1 = Files.walk(comPath, 1);
						for (Iterator<Path> it = walk1.iterator(); it.hasNext(); ) {
							Path path = it.next();
							String[] file = path.toString().split("\\\\");
							String fileName = file[file.length - 1];
							if (fileName.equalsIgnoreCase(redemption + ".js")) {
								comExists = true;
								new Thread(() -> {
									try {
										while (ServerChatBot.processing) {
											Thread.sleep(50);
										}
										Main.sendMessage(Command.run(username, finalUserInput, Files.readString(path, StandardCharsets.UTF_8)));
									} catch (Exception e) {
										e.printStackTrace();
									}
								}).start();
							}
						}
					}

					if (!comExists) {

						Stream<Path> walk = Files.walk(myPath, 1);
						for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
							Path path = it.next();
							String[] file = path.toString().split("/");
							String fileName = file[file.length - 1];
							System.out.println(path.toString());
							if (fileName.equalsIgnoreCase(redemption + ".js")) {

								InputStream is = Main.class
										.getClassLoader().getResourceAsStream(path.toString().substring(1));
								assert is != null;
								InputStreamReader isr = new InputStreamReader(is);
								BufferedReader br = new BufferedReader(isr);
								StringBuilder function = new StringBuilder();
								String line;

								while ((line = br.readLine()) != null) {
									function.append(line);
								}
								is.close();
								isr.close();
								br.close();


								Main.sendMessage(Command.run(username, finalUserInput, function.toString()));
								break;
							}
						}
					}
				}
				catch (Exception ignored){
				}

			}
		}
	}

	@Override
	public void onMessage(ByteBuffer message) {
		System.out.println("received ByteBuffer");
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
	}

	public void disconnectBot(){
		send("{\n" +
				"  \"type\": \"UNLISTEN\"\n" +
				"}");
	}
}