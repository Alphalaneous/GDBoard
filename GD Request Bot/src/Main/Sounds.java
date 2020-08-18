package Main;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Sounds {

	public static HashMap<String, Sound> sounds = new HashMap<String, Sound>();


	public static void playSound(String location, boolean restart, boolean overlap){
		if(sounds.size() <= 5 && (!sounds.containsKey(location) || overlap)) {
			new Sound(location, true, false).playSound();
		}
		else if(sounds.containsKey(location) && restart){
			sounds.get(location).stopSound();
			new Sound(location, true, false).playSound();
		}
	}
	public static void playSound(String location, boolean restart, boolean overlap, boolean isFile, boolean isURL){


		if(sounds.size() <= 5 && (!sounds.containsKey(location) || overlap)) {
			new Sound(location, isFile, isURL).playSound();
		}
		else if(sounds.containsKey(location) && restart){

			sounds.get(location).stopSound();
			new Sound(location, isFile, isURL).playSound();
		}
	}
	public static void stopSound(String location){
		sounds.get(location).stopSound();
	}
	public static void stopAllSounds(){
		Iterator hmIterator = sounds.entrySet().iterator();

		while (hmIterator.hasNext()) {
			Map.Entry mapElement = (Map.Entry)hmIterator.next();
			((Sound)mapElement.getValue()).stopSound();
		}
	}
	private static class Sound {

		String location;
		boolean complete = false;
		boolean isFile = true;
		boolean isURL = false;
		Player mp3player;

		Sound(String location, boolean isFile, boolean isURL){
			this.location = location;
			this.isFile = isFile;
			this.isURL = isURL;
			Sounds.sounds.put(location, this);
		}
		public void playSound() {
			new Thread(() -> {
				try {
					BufferedInputStream inp = null;
					if(isURL){
						inp = new BufferedInputStream(new URL(location).openStream());
					}
					else if(isFile){
						inp = new BufferedInputStream(new FileInputStream(location));
					}
					else if(!isFile){
						inp = new BufferedInputStream(ServerChatBot.class
								.getResource(location).openStream());
					}
					mp3player = new Player(inp);

					mp3player.play();

				} catch (Exception f) {
					f.printStackTrace();
					DialogBox.showDialogBox("Error!", f.toString(), "There was an error playing the sound!", new String[]{"OK"});

				}
				complete = true;
				Sounds.sounds.remove(location, this);
			}).start();

		}
		public void stopSound(){
			mp3player.close();
			complete = true;
		}
	}
}

