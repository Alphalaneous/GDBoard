package Main;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class PlaySong extends Thread {
	
	private String URL;
	
	void setSong(String URL){
		this.URL = URL;
	}
	
	public void run() {

		try {
			Player mp3player;
			BufferedInputStream inp;
			assert URL != null;
			inp = new BufferedInputStream(new URL(URL).openStream());
			mp3player = new Player(inp);
			mp3player.play();
		} catch (IOException | JavaLayerException | NullPointerException e) {
			e.printStackTrace();
		}
	}
}
