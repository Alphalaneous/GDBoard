package Main;

import Main.InnerWindows.LevelsWindow;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.exception.SongNotAllowedForUseException;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.*;

@SuppressWarnings("unused")

public class Board {

	static boolean bwomp = false;

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

	public static void sendAsMain(String message){
		Main.sendMainMessage(message);
	}

	static AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
	public static void playNewgrounds(String songID){
		Sounds.playSound(client.getSongById(Long.parseLong(songID)).block().getDownloadURL(), true, false, false, true);
	}

	private static NashornSandbox sandbox = NashornSandboxes.create();
	public static Object eval(String function){
		sandbox.allow(Requests.class);
		sandbox.allow(GDMod.class);
		sandbox.allow(Board.class);
		sandbox.allow(Variables.class);

		try {
			sandbox.eval("var Levels = Java.type('Main.Requests'); var GD = Java.type('Main.GDMod'); var Board = Java.type('Main.Board'); var Variables = Java.type('Main.Variables'); function command() { " + function + " }");
		}
		catch (Exception e){
			return ("There was an error with the command: " + e).replaceAll(System.getProperty("user.name"), "*****");
		}
		String result = "";
		try {
			Object obj = sandbox.eval("command();");
			if(obj != null) {
				result = obj.toString();
			}
		} catch (Exception e) {
			return ("There was an error with the command: " + e).replaceAll(System.getProperty("user.name"), "*****");
		}
		return result.replaceAll(System.getProperty("user.name"), "*****");
	}

	public static void showPopup(String title, String text){
		DialogBox.showDialogBox(title,text, "", new String[]{"OK"});
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

	public static void rick(){
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
}
