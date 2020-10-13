package Main;

import Main.SettingsPanels.GeneralBotSettings;
import com.alphalaneous.ChatBot;
import com.alphalaneous.ChatMessage;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener extends ChatBot {

	ChatListener(String channel) {
		super(channel);
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {

	}

	@Override
	public void onClose(int i, String s, boolean b) {

	}

	@Override
	public void onMessage(ChatMessage chatMessage) {
		if(!chatMessage.getSender().equalsIgnoreCase("gdboard")) {
			if (GeneralBotSettings.multiOption) {
				new Thread(() -> {
					try {
						while (ServerChatBot.processing) {
							Thread.sleep(50);
						}
						ServerChatBot.onMessage(chatMessage.getSender(), chatMessage.getMessage(), chatMessage.isMod(), chatMessage.isSub(), chatMessage.getCheerCount(), chatMessage.getTag("id"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}).start();
			} else {
				try {
					while (ServerChatBot.processing) {
						Thread.sleep(50);
					}
					ServerChatBot.onMessage(chatMessage.getSender(), chatMessage.getMessage(), chatMessage.isMod(), chatMessage.isSub(), chatMessage.getCheerCount(), chatMessage.getTag("id"));
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
