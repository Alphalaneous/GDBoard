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

	static HashMap<String, Thread> threads = new HashMap<>();


	public static void playSound(String location){

		if(threads.containsKey(location)){
			threads.get(location).stop();
			threads.remove(location);
		}
		Thread aThread = new Thread(() -> {
			try {
				FileInputStream is = new FileInputStream(location);
				BufferedInputStream inp = new BufferedInputStream(is);
				Player mp3player = new Player(inp);
				mp3player.play();
			} catch (FileNotFoundException f) {
				f.printStackTrace();
				JOptionPane.showMessageDialog(null, "That file doesn't exist!", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (JavaLayerException f){
				f.printStackTrace();
				JOptionPane.showMessageDialog(null, "That isn't an mp3!", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (NullPointerException f) {
				f.printStackTrace();
				JOptionPane.showMessageDialog(null, "There was an error playing the sound!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		threads.put(location, aThread);
		aThread.start();
	}
	public static void stopSound(String location){
		if(threads.containsKey(location)){
			threads.get(location).stop();
			threads.remove(location);
		}
	}
	public static void stopAllSounds(){
		Iterator hmIterator = threads.entrySet().iterator();

		while (hmIterator.hasNext()) {
			Map.Entry mapElement = (Map.Entry)hmIterator.next();
			((Thread)mapElement.getValue()).stop();
		}
		threads.clear();
	}

	public static void sendAsMain(String message){
		Main.sendMainMessage(message);
	}

	static AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
	public static void playNewgrounds(String songID){
		if(threads.containsKey("ng:" + songID)){
			threads.get("ng:" + songID).stop();
			threads.remove("ng:" + songID);
		}
		Thread aThread = new Thread(() -> {
			try {
				BufferedInputStream inp = new BufferedInputStream(new URL(client.getSongById(Long.parseLong(songID)).block().getDownloadURL()).openStream());
				Player mp3player = new Player(inp);
				mp3player.play();
			} catch (IllegalArgumentException | SongNotAllowedForUseException f){
				f.printStackTrace();
			} catch (FileNotFoundException f) {
				f.printStackTrace();
				JOptionPane.showMessageDialog(null, "That file doesn't exist!", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (JavaLayerException f){
				f.printStackTrace();
				JOptionPane.showMessageDialog(null, "That isn't an mp3!", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception f) {
				f.printStackTrace();
				JOptionPane.showMessageDialog(null, "There was an error playing the sound!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		threads.put("ng:" + songID, aThread);
		aThread.start();
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
			e.printStackTrace();
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

	public static String getenv(String name){
		return System.getenv(name);
	}

	public static String getOAuth(){
		return Settings.oauth;
	}

	public static String getClientID(){
		return "fzwze6vc6d2f7qodgkpq2w8nnsz3rl";
	}

	public static void endGDBoard(){
		Main.close();
	}
	private static Thread rickThread = null;

	public static void rick(){
		System.out.println("ricked");
		if (rickThread != null) {
			rickThread.stop();
		}
		rickThread = new Thread(() -> {
			try {
				BufferedInputStream inp = new BufferedInputStream(ServerChatBot.class
						.getResource("/Resources/rick.mp3").openStream());
				Player mp3player = new Player(inp);
				mp3player.play();
			} catch (JavaLayerException | NullPointerException | IOException f) {
				f.printStackTrace();
				JOptionPane.showMessageDialog(null, "There was an error playing the music!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		rickThread.start();
	}

	public static void stopRick(){
		if (rickThread != null && rickThread.isAlive()) {
			rickThread.stop();
		}
	}
	static boolean bwomp = false;

	public static void toggleBwomp(){
		bwomp = !bwomp;
	}

	private static Thread knockThread = null;

	public static void knock(){
		if (knockThread != null) {
			knockThread.stop();
		}
		knockThread = new Thread(() -> {
			try {
				BufferedInputStream inp = new BufferedInputStream(ServerChatBot.class
						.getResource("/Resources/knock.mp3").openStream());
				Player mp3player = new Player(inp);
				mp3player.play();
			} catch (JavaLayerException | NullPointerException | IOException f) {
				f.printStackTrace();
				JOptionPane.showMessageDialog(null, "There was an error playing the music!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		knockThread.start();
	}

	public static void stopKnock(){
		if (knockThread != null && knockThread.isAlive()) {
			knockThread.stop();
		}
	}
	private static Thread bwompThread = null;

	public static void bwomp(){
		if (bwompThread != null) {
			bwompThread.stop();
		}
		bwompThread = new Thread(() -> {
			try {
				BufferedInputStream inp = new BufferedInputStream(ServerChatBot.class
						.getResource("/Resources/bwomp.mp3").openStream());
				Player mp3player = new Player(inp);
				mp3player.play();
			} catch (JavaLayerException | NullPointerException | IOException f) {
				f.printStackTrace();
				JOptionPane.showMessageDialog(null, "There was an error playing the music!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		bwompThread.start();
	}

	public static void stopBwomp(){
		if (bwompThread != null && bwompThread.isAlive()) {
			bwompThread.stop();
		}
	}


}
