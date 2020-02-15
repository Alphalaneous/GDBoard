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
			channel = gfg.get("channel").toString();
			hasChannel = true;
		}
		catch (NullPointerException e){
			hasChannel = false;
		}
	}


	static String oauth;
	private static Point requestsWLoc = new Point(10,10);
	static String channel;
	static boolean hasOauth = false;
	static boolean hasChannel = false;

	static void setOAuth(String oauth) {
		Settings.oauth = oauth;
	}
	static void setChannel (String channel){
		Settings.channel = "#" + channel.toLowerCase();
	}
	static Point getRequestsWLoc() {
		return requestsWLoc;
	}
	
}
