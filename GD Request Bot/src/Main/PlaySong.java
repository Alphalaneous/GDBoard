package Main;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class PlaySong extends Thread {
	
	private String ID;
	
	void setSong(String ID){
		this.ID = ID;
	}
	
	public void run() {

		// Set URL
		URL url;
		try {
			//bug https://www.newgrounds.com/audio/listen/659469
			//bug https://www.newgrounds.com/audio/listen/700642
			url = new URL("https://www.newgrounds.com/audio/listen/" + ID);
			URLConnection spoof;

			spoof = url.openConnection();

			spoof.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
			BufferedReader in;

			in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));

			String strLine;
			String audioURL = null;

			while ((strLine = in.readLine()) != null) {

				if (strLine.contains("embedController")) {
					String[] strings = strLine.split("\"");
					audioURL = strings[3].replaceAll("\\\\", "");
				}
			}

			Player mp3player;
			BufferedInputStream inp;

			assert audioURL != null;
			inp = new BufferedInputStream(new URL(audioURL).openStream());
			mp3player = new Player(inp);
			mp3player.play();
		} catch (IOException | JavaLayerException | NullPointerException e) {
			e.printStackTrace();
		}
		
	}
}
