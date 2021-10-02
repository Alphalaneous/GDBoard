package com.alphalaneous;

import com.alphalaneous.TwitchBot.ChatBot;
import com.alphalaneous.TwitchBot.ChatMessage;
import org.java_websocket.handshake.ServerHandshake;

public class ChatListener extends ChatBot {

	ChatListener(String channel) {
		super(channel);
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		System.out.println("> Connected to Twitch IRC");
		Main.sendMessage(Utilities.format("🔷 | $STARTUP_MESSAGE$"));
	}

	@Override
	public void onClose(int i, String s, boolean b) {

	}

	@Override
	public void onMessage(ChatMessage chatMessage) {
		//TwitchChat.addMessage(chatMessage);
		if (!chatMessage.getSender().equalsIgnoreCase("gdboard")) {
			if (Settings.getSettings("multiMode").asBoolean()) {
				new Thread(() -> {
					waitOnMessage(chatMessage);
				}).start();
			} else {
				waitOnMessage(chatMessage);
			}
			//Moderation.checkMessage(chatMessage);
			//CommandNew.run(chatMessage, false);
		}
	}

	private void waitOnMessage(ChatMessage chatMessage) {
		try {
			while (BotHandler.processing) {
				Thread.sleep(50);
			}
			BotHandler.onMessage(chatMessage.getSender(), chatMessage.getMessage(), chatMessage.isMod(), chatMessage.isSub(), chatMessage.getCheerCount(), chatMessage.getTag("id"), Long.parseLong(chatMessage.getTag("user-id")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRawMessage(String s) {
	}

	@Override
	public void onError(Exception e) {

	}
}
