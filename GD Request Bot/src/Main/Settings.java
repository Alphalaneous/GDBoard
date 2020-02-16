package Main;

import java.awt.Point;
import java.io.*;
import java.util.Properties;

class Settings {

	private static Properties gfg = new Properties();
	private static FileInputStream in;
	private static BufferedWriter writer;
	private static File file = new File(System.getenv("APPDATA") + "\\GDBoard\\config.properties");
	static {
		while(true) {

			try {
				writer = new BufferedWriter(new FileWriter(

						file, true));
				in = new FileInputStream(file);
				break;
			} catch (IOException e) {

				file.mkdir();
				file.getParentFile().mkdir();
			}
		}
	}

	static void writeSettings(String key, String setting) throws IOException {
		writer = new BufferedWriter(new FileWriter(

				file, true));
		writer.newLine();
		writer.write((key + "=" + setting));
		writer.close();
	}
	static void loadSettings() throws IOException {
		gfg.load(in);
		try {
			oauth = gfg.get("oauth").toString();
			hasOauth = true;
		}
		catch (NullPointerException e){
			hasOauth = false;
		}
		try {
			channel = gfg.get("channel").toString().toLowerCase();
			hasChannel = true;
		}
		catch (NullPointerException e){
			hasChannel = false;
		}
		try {
			windowedMode = Boolean.parseBoolean(gfg.get("windowed").toString());
			hasWindowed = true;
		}
		catch (NullPointerException e){
			hasWindowed = false;
		}
	}

	static boolean windowedMode = false;
	static String oauth;
	private static Point requestsWLoc = new Point(10,10);
	private static Point infoWLoc = new Point(1920 - 400 - 10,10);
	private static Point commentWLoc = new Point(10,500);
	private static Point songWLoc = new Point(1920 - 300 - 10,600);
	private static Point actionsWLoc = new Point(1920 - 260 - 10,200);
	static String channel;
	static boolean hasOauth = false;
	static boolean hasChannel = false;
	static boolean hasWindowed = false;

	static void setWindowedMode(boolean mode){
		Settings.windowedMode = mode;
	}
	static void setOAuth(String oauth) {
		Settings.oauth = oauth;
	}
	static void setChannel (String channel){
		Settings.channel = "#" + channel.toLowerCase();
	}
	static Point getRequestsWLoc() {
		if(Settings.windowedMode) {
			requestsWLoc = new Point(0, 0);
		}
		return requestsWLoc;
	}
	static Point getInfoWLoc() {
		if(Settings.windowedMode) {
			infoWLoc = new Point(0, 402);
		}
		return infoWLoc;
	}
	static Point getCommentWLoc() {
		if(Settings.windowedMode) {
			commentWLoc = new Point(402, 98);
		}
		return commentWLoc;
	}
	static Point getSongWLoc() {
		if(Settings.windowedMode) {
			songWLoc = new Point(402, 422);
		}
		return songWLoc;
	}
	static Point getActionsWLoc() {
		if(Settings.windowedMode) {
			actionsWLoc = new Point(422, 20);
		}
		return actionsWLoc;
	}
}
