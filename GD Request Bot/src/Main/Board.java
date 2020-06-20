package Main;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
