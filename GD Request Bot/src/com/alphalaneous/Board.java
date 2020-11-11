package com.alphalaneous;

import com.alphalaneous.Windows.DialogBox;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.exception.MissingAccessException;
import com.github.alex1304.jdash.util.LevelSearchFilters;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import org.json.JSONObject;

import java.time.ZonedDateTime;

@SuppressWarnings("unused")

public class Board {

	private static boolean bwomp = false;


	public static void playSound(String location){
		Sounds.playSound(location, true, false, true, false);
	}
	public static void playSound(String location, boolean restart, boolean overlap, boolean isURL){
		Sounds.playSound(location, restart, overlap, true, isURL);
	}
	public static void stopSound(String location){
		Sounds.stopSound(location);
	}
	public static void stopAllSounds(){
		Sounds.stopAllSounds();
	}

	public static void sendMessage(String message, boolean whisper, String user){
		Main.sendMessage(message, whisper, user);
	}
	public static void sendMessage(String message){
		Main.sendMessage(message, false, null);
	}

	public static void sendAsMain(String message){
		Main.sendMainMessage(message);
	}

	static AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
	public static void playNewgrounds(String songID){
		Sounds.playSound(client.getSongById(Long.parseLong(songID)).block().getDownloadURL(), true, false, false, true);
	}

	private static NashornSandbox sandbox = NashornSandboxes.create();
	public static String eval(String function){
		return Command.run("function command(){" + function + "}", true);
	}

	public static void showPopup(String title, String text){
		new Thread(() -> {
			DialogBox.showDialogBox(title,"<html>" + text + "</html>", "", new String[]{"OK"});
		}).start();
	}

	public static String getenv(String name){
		return System.getenv(name);
	}

	public static void toggleBwomp(){
		bwomp = !bwomp;
	}

	public static void endGDBoard(){
		Main.close();
	}

	public static void signal(){
		JSONObject messageObj = new JSONObject();
		messageObj.put("request_type", "get_blocked_ids");
		Main.sendBotMessage(messageObj.toString());
	}

	public static void rick(){
			if(Sounds.sounds.containsKey("/Resources/rick.mp3")) {
				Sounds.stopSound("/Resources/rick.mp3");
			}
			Sounds.playSound("/Resources/rick.mp3", true, false, false, false);
	}

	public static void stopRick(){
		Sounds.stopSound("/Resources/rick.mp3");
	}

	public static void knock(){
		Sounds.playSound("/Resources/knock.mp3", true, true, false, false);
	}

	public static void stopKnock(){
		Sounds.stopSound("/Resources/knock.mp3");

	}
	public static void bwomp(){
		Sounds.playSound("/Resources/bwomp.mp3", true, true, false, false);
	}

	public static void stopBwomp(){
		Sounds.stopSound("/Resources/bwomp.mp3");
	}

	public static String testSearchPing(){
		long time = ZonedDateTime.now().toInstant().toEpochMilli();
		try {
			if (LoadGD.isAuth) {
				LoadGD.authClient.searchLevels("", LevelSearchFilters.create(), 0).block();
			} else {
				LoadGD.anonClient.searchLevels("", LevelSearchFilters.create(), 0).block();
			}
			long timeAffter = ZonedDateTime.now().toInstant().toEpochMilli();
			return String.valueOf(timeAffter - time);
		}
		catch (MissingAccessException e){
			return "Servers Down!";
		}
	}
}
