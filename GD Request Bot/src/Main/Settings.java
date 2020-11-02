package Main;

import Main.InnerWindows.*;

import java.awt.*;
import java.io.*;
import java.util.Properties;

public class Settings {

	private static Properties gfg = new Properties();
	private static FileInputStream in;
	private static BufferedWriter writer;
	private static File file = new File(Defaults.saveDirectory + "\\GDBoard\\config.properties");


	static {
		while (true) {
			try {
				writer = new BufferedWriter(new FileWriter(file, true));
				in = new FileInputStream(file);
				break;
			} catch (IOException e) {
				file.mkdir();
				file.getParentFile().mkdir();
			}
		}
	}

	static void setWindowSettings(String key, String setting) {
		if (key.equalsIgnoreCase("Window")) {
			int x = Integer.parseInt(setting.split(",")[0]);
			int y = Integer.parseInt(setting.split(",")[1]);
			windowWLoc = new Point(x, y);
		}
	}

	static void writeLocation() throws IOException {
		writeSettings("window", windowWLoc.x + "," + windowWLoc.y);
	}

	public static void writeSettings(String key, String setting) throws IOException {
		in.close();
		in = new FileInputStream(file);
		try {
			gfg.load(in);
		}
		catch (IOException e){
			in = new FileInputStream(file);
			gfg.load(in);
		}

		if (gfg.containsKey(key)) {
			BufferedReader file = new BufferedReader(new FileReader(Defaults.saveDirectory + "\\GDBoard\\config.properties"));
			StringBuilder inputBuffer = new StringBuilder();
			String line;
			while ((line = file.readLine()) != null) {
				inputBuffer.append(line);
				inputBuffer.append('\n');
			}
			file.close();

			FileOutputStream fileOut = new FileOutputStream(Defaults.saveDirectory + "\\GDBoard\\config.properties");
			fileOut.write(inputBuffer.toString().replace(key + "=" + gfg.get(key).toString(), key + "=" + setting).getBytes());
			fileOut.close();
			System.out.println(key + ": " + setting);
		} else {
			writer = new BufferedWriter(new FileWriter(file, true));
			writer.newLine();
			writer.write((key + "=" + setting));
			writer.close();
		}

	}

	public static String getSettings(String key) {
		while(true) {
			try {
				in.close();
				in = new FileInputStream(file);
				gfg.load(in);
				if (gfg.containsKey(key)) {
					return gfg.get(key).toString();
				}
				else{
					return "";
				}
			} catch (IOException e) {
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static void loadSettings(boolean start) throws IOException {

		GraphicsDevice[] screens = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getScreenDevices();
		in = new FileInputStream(file);
		gfg.load(in);
		if(gfg.containsKey("oauth") ) {
			oauth = gfg.get("oauth").toString();
		}
		if(gfg.containsKey("channel")) {
			channel = gfg.get("channel").toString().toLowerCase();
		}
		if(gfg.containsKey("windowed")) {
			windowedMode = Boolean.parseBoolean(gfg.get("windowed").toString());
			hasWindowed = true;
		}
		if(gfg.containsKey("onboarding")) {
			onboarding = Boolean.parseBoolean(gfg.get("onboarding").toString());
		}
		if(gfg.containsKey("monitor")) {
			monitor = Integer.parseInt(gfg.get("monitor").toString());
			if (monitor >= screens.length) {
				monitor = 0;
			}
			hasMonitor = true;
		}


		if (!start) {

			if(gfg.containsKey("window")){
				String window = gfg.get("window").toString();
				int x = Integer.parseInt(window.split(",")[0]);
				int y = Integer.parseInt(window.split(",")[1]);
				windowWLoc = new Point(x, y);
				Windowed.setLocation(windowWLoc);
			}
		}
	}

	public static boolean windowedMode = false;
	static String oauth;
	//static String clientID;
	private static Point windowWLoc = new Point(0, 0);
	static int monitor;
	public static String channel;
	static boolean hasMonitor = false;
	public static boolean onboarding = true;
	static boolean hasWindowed = false;



	static Point getWindowWLoc() {
		return windowWLoc;
	}
}
