package com.alphalaneous;

import com.alphalaneous.GibberishDetector.GibberishDetector;
import com.alphalaneous.GibberishDetector.GibberishDetectorExtended;
import com.alphalaneous.GibberishDetector.GibberishDetectorFactory;
import com.alphalaneous.TwitchBot.ChatMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class Moderation {

	private static final GibberishDetector gibberishDetector;
	static {
		BufferedReader bigEnglishReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Moderation.class.getResourceAsStream("/GibberishResources/bigEnglish.txt"))));
		BufferedReader goodEnglishReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Moderation.class.getResourceAsStream("/GibberishResources/goodEnglish.txt"))));
		BufferedReader badEnglishReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Moderation.class.getResourceAsStream("/GibberishResources/badEnglish.txt"))));

		String bigEnglishString = bigEnglishReader.lines().collect(Collectors.joining());
		String goodEnglishString = goodEnglishReader.lines().collect(Collectors.joining());
		String badEnglishString = badEnglishReader.lines().collect(Collectors.joining());

		GibberishDetectorFactory gibberishDetectorFactory = new GibberishDetectorFactory(GibberishDetectorExtended.class);
		gibberishDetector = gibberishDetectorFactory.createGibberishDetector(Arrays.asList(bigEnglishString.split("\n")), Arrays.asList(goodEnglishString.split("\n")), Arrays.asList(badEnglishString.split("\n")), "abcdefghijklmnopqrstuvwxyz ");

	}

	public static void checkMessage(ChatMessage chatMessage) {
		String emotes = chatMessage.getTag("emotes");
		String messageEmoteless = removeEmotes(emotes, chatMessage.getMessage());
		double emotePercent = checkEmotePercent(emotes, chatMessage.getMessage());
		double capsPercent = checkCapPercent(messageEmoteless);
		double symPercent = checkSymbolPercent(messageEmoteless);
		int emoteCount = getEmoteCount(emotes);
		long capCount = getCapCount(messageEmoteless);
		int symCount = getSymbolCount(messageEmoteless);

		//System.out.println(emoteCount + " : " + emotePercent);
		//System.out.println(capCount + " :: " + capsPercent);
		//System.out.println(symCount + " ::: " + symPercent);

		if(checkIfLink(messageEmoteless)){
			Main.sendMessage("@" + chatMessage.getSender() + ", links are not allowed here!");
			Main.sendMessageWithoutCooldown("/delete " + chatMessage.getTag("id"));
			return;
		}
		if(emotePercent > .5 && emoteCount > 4){
			Main.sendMessage("@" + chatMessage.getSender() + ", please don't spam emotes!");
			Main.sendMessageWithoutCooldown("/delete " + chatMessage.getTag("id"));
			return;
		}
		if(capsPercent > .5 && capCount > 4){
			Main.sendMessage("@" + chatMessage.getSender() + ", please don't spam capital letters!");
			Main.sendMessageWithoutCooldown("/delete " + chatMessage.getTag("id"));
			return;
		}
		if(symPercent > .5 && symCount > 4){
			Main.sendMessage("@" + chatMessage.getSender() + ", please don't spam symbols!");
			Main.sendMessageWithoutCooldown("/delete " + chatMessage.getTag("id"));
			return;
		}

		//if contains no space and is smaller than 16 but greater than 8
		if(((!messageEmoteless.contains(" ") && messageEmoteless.length() <= 16 && messageEmoteless.length() >= 8) || messageEmoteless.length() > 16) && gibberishDetector.isGibberish(messageEmoteless)){
			Main.sendMessage("@" + chatMessage.getSender() + ", please don't send gibberish!");
			Main.sendMessageWithoutCooldown("/delete " + chatMessage.getTag("id"));
			return;
		}
	}

	public static boolean checkIfLink(String message){
		String[] replaceSymbols = { "\\", "|", "{", "}", "\"", "'", ";", "<", ">", ",", "`", "!", "$", "^", "*"};
		String[] endSymbols = {"(", ")", "&", "%", "#", "@", "~", "?", ":", "_", "-", "+", "="};
		String[] messageSplitSpaces = message.split(" ");
		for(String spaceSplit : messageSplitSpaces){
			for(String replace : replaceSymbols){
				spaceSplit = spaceSplit.replace(replace, "");
			}
			spaceSplit = spaceSplit + " ";
			String[] possibleLink = spaceSplit.split("\\.");
			if(possibleLink.length > 1){
				if(possibleLink[possibleLink.length-1].trim().length() >= 2 && possibleLink[possibleLink.length-1].trim().length() <= 6){
					for(String endSymbol : endSymbols) {
						if(possibleLink[possibleLink.length - 1].trim().endsWith(endSymbol)){
							return false;
						}
						else if(!possibleLink[possibleLink.length - 1].trim().endsWith(endSymbol)){
							if(!(possibleLink[0].startsWith("http") || possibleLink[0].startsWith("https"))){
								try {
									URL url = new URL("http://" + spaceSplit.trim());
									URLConnection conn = url.openConnection();
									conn.connect();
									return true;
								} catch (IOException e) {
									return false;
								}
							}
							else{
								try {
									URL url = new URL(spaceSplit.trim());
									URLConnection conn = url.openConnection();
									conn.connect();
									return true;
								} catch (IOException e) {
									return false;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	public static double checkCapPercent(String message) {
		//starting limit then percent after
		message = message.replaceAll(" ", "");
		double upperCase = message.chars().filter(c -> c >= 'A' && c <= 'Z').count();
		double result = upperCase / message.length();
		if(Double.isNaN(result)){
			return 0;
		}
		return result;
	}
	public static long getCapCount(String message){
		return message.chars().filter(c -> c >= 'A' && c <= 'Z').count();
	}
	public static int getEmoteCount(String emoteTag){
		if(emoteTag.equalsIgnoreCase("")){
			return 0;
		}
		String[] emotes = emoteTag.split("/");
		int count = 0;
		for(String emote : emotes){
			String positionsString = emote.split(":")[1];
			String[] positionsEach = positionsString.split(",");
			for(String ignored : positionsEach){
				count++;
			}
		}
		return count;
	}

	public static String removeEmotes(String emoteTag, String message){
		if(emoteTag.equalsIgnoreCase("")){
			return message;
		}
		message = message.replaceAll("\n", "").replaceAll("\r", "");
		String newMessage = message + " ";
		String[] emotes = emoteTag.split("/");
		for(String emote : emotes){
			String positionsString = emote.split(":")[1];
			String[] positionsEach = positionsString.split(",");
			for(String position : positionsEach){
				int start = Integer.parseInt(position.split("-")[0]);
				int stop = Integer.parseInt(position.split("-")[1]) + 1;
				String strToReplace = message.substring(start, stop) + " ";
				newMessage = newMessage.replaceFirst(strToReplace, "");
			}
		}
		return newMessage;
	}


	public static double checkEmotePercent(String emoteTag, String message) {
		if(emoteTag.equalsIgnoreCase("")){
			return 0;
		}
		message = message.replaceAll("\n", "").replaceAll("\r", "");
		String newMessage = message + " ";
		String[] emotes = emoteTag.split("/");
		int emoteCount = 0;
		for(String emote : emotes){
			String positionsString = emote.split(":")[1];
			String[] positionsEach = positionsString.split(",");
			for(String position : positionsEach){
				emoteCount++;
				int start = Integer.parseInt(position.split("-")[0]);
				int stop = Integer.parseInt(position.split("-")[1]) + 1;
				String strToReplace = message.substring(start, stop) + " ";
				newMessage = newMessage.replaceFirst(strToReplace, "");
			}
		}

		double length = newMessage.length() + emoteCount;

		return emoteCount/length;
	}

	public static double checkSymbolPercent(String message) {
		return getSymbolCount(message) / (double) message.length();
	}
	public static int getSymbolCount(String message) {
		int symbolCount = 0;
		for (int i = 0; i < message.length(); i++) {
			if (message.substring(i, i+1).matches("[^A-Za-z0-9 ]")) {
				symbolCount++;
			}
		}
		return symbolCount;
	}
	public static boolean isFollowerBot(String message) {
		return message.matches("\\b(b *i *g *f *o *l *l *o *w *s *([.,]) *c *o *m)+");
	}
}
