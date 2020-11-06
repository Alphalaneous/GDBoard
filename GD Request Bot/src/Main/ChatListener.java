package Main;

import Main.SettingsPanels.GeneralBotSettings;
import com.alphalaneous.ChatBot;
import com.alphalaneous.ChatMessage;
import org.java_websocket.handshake.ServerHandshake;

public class ChatListener extends ChatBot {

	ChatListener(String channel) {
		super(channel);
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		System.out.println("Connected to Twitch IRC");
	}

	@Override
	public void onClose(int i, String s, boolean b) {

	}

	@Override
	public void onMessage(ChatMessage chatMessage) {
		//TwitchChat.addMessage(chatMessage);
		if(!chatMessage.getSender().equalsIgnoreCase("gdboard")) {
			if (GeneralBotSettings.multiOption) {
				new Thread(() -> {
					try {
						while (BotHandler.processing) {
							Thread.sleep(50);
						}
						BotHandler.onMessage(chatMessage.getSender(), chatMessage.getMessage(), chatMessage.isMod(), chatMessage.isSub(), chatMessage.getCheerCount(), chatMessage.getTag("id"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}).start();
			} else {
				try {
					while (BotHandler.processing) {
						Thread.sleep(50);
					}
					BotHandler.onMessage(chatMessage.getSender(), chatMessage.getMessage(), chatMessage.isMod(), chatMessage.isSub(), chatMessage.getCheerCount(), chatMessage.getTag("id"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onRawMessage(String s) {
	}

	@Override
	public void onError(Exception e) {

	}
}
