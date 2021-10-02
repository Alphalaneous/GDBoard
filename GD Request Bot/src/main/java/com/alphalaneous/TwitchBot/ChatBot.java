package com.alphalaneous.TwitchBot;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class ChatBot {

	private String channel;
	private WebSocketClient chatReader;

	public ChatBot(String channel) {
		this.channel = channel;
		try {
			chatReader = new WebSocketClient(new URI("wss://irc-ws.chat.twitch.tv:443")) {
				@Override
				public void onOpen(ServerHandshake serverHandshake) {
					ChatBot.this.onOpen(serverHandshake);
				}

				@Override
				public void onMessage(String message) {
					onRawMessage(message);
					message = message.replaceAll("\n", "").replaceAll("\r", "");
					if (message.contains("PRIVMSG")) {

						String tagsPrefix = message.split("@", 3)[1];
						String channelPrefix = message.split("@", 3)[2].replace("\r", "");
						String sentMessage = channelPrefix.split(channel + " :", 2)[1].replace("\n", "").replace("\r", "").trim();
						String sender = channelPrefix.split(".tmi.twitch.tv")[0];
						String displayName = sender;
						String[] badges = {};
						boolean isMod = false;
						boolean isSub = false;
						int cheerCount = 0;
						String[] tags = tagsPrefix.split(";");
						for(String tagA : tags){
							if(tagA.split("=", 2)[0].equals("badges")){
								badges = tagA.split("=", 2)[1].split(",");
							}
							if(tagA.split("=", 2)[0].equals("bits")){
								cheerCount = Integer.parseInt(tagA.split("=", 2)[1]);
							}
							if(tagA.split("=", 2)[0].equals("display-name")){
								displayName = tagA.split("=", 2)[1];
							}
						}
						for(String badgeA : badges){
							if(badgeA.split("/", 2)[0].equals("broadcaster") || badgeA.split("/", 2)[0].equals("moderator")){
								isMod = true;
							}
							if(badgeA.split("/", 2)[0].equals("subscriber") || badgeA.split("/", 2)[0].equals("founder")){
								isSub = true;
							}
						}

						ChatBot.this.onMessage(new ChatMessage(tags, sender, displayName, sentMessage, badges, isMod, isSub, cheerCount));
					}
					if (message.equalsIgnoreCase("PING :tmi.twitch.tv")) {
						send("PONG :tmi.twitch.tv");
					}
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					ChatBot.this.onClose(code, reason, remote);
				}

				@Override
				public void onError(Exception e) {
					ChatBot.this.onError(e);
				}
			};
		}
		catch (URISyntaxException ignored){
		}
	}
	public void connect(String oauth, String nick){
		try {
			chatReader.connectBlocking();
			while(!chatReader.isOpen()){
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		chatReader.send("CAP REQ :twitch.tv/tags");
		chatReader.send("CAP REQ :twitch.tv/commands");
		chatReader.send("CAP REQ :twitch.tv/membership");
		chatReader.send("PASS oauth:" + oauth);
		chatReader.send("NICK " + nick);
		chatReader.send("JOIN #" + channel);
	}
	public void sendMessage(String message){
		chatReader.send("PRIVMSG #" + channel + " :" + message);
	}
	public void sendRawMessage(String message){
		chatReader.send(message);
	}
	public void disconnect(){
		chatReader.close();
	}
	public boolean isClosed(){
		return chatReader.isClosed();
	}
	public abstract void onOpen(ServerHandshake serverHandshake);
	public abstract void onClose(int code, String reason, boolean remote);
	public abstract void onMessage(ChatMessage message);
	public abstract void onRawMessage(String message);
	public abstract void onError(Exception e);


}