package Main;

import Main.InnerWindows.*;
import Main.SettingsPanels.*;
import com.cavariux.twitchirc.Chat.Channel;
import org.apache.commons.io.FileUtils;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

	static boolean programStarting = true;
	public static boolean programLoaded = false;
	static boolean allowRequests = false;
	static boolean sendMessages = false;
	static boolean refreshImages = false;
	private static ChatReader chatReader;
	private static boolean finishedLoading = false;

	private static ChannelPointListener channelPointListener;

	public static void main(String[] args) throws IOException {

		/**
		 * Saves defaults of UI Elements before switching to Nimbus
		 * Sets to Nimbus, then sets defaults back
		 */
		setUI();
		/**
		 * Places config files in JRE folder in the GDBoard AppData as I forgot to
		 * include them in the bundled JRE
		 */
		createConfFiles();
		/**
		 * Sets Windowed to true as it is default and previously wasn't, easiest fix for all
		 * checks of windowed mode
		 */
		if(Settings.getSettings("windowed").equalsIgnoreCase("")){
			Settings.writeSettings("windowed", "true");
		}

		Defaults.programLoaded.set(false);

		try {

			/**
			 * Disables logging used with JDash
			 */

			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);
			logger.setUseParentHandlers(false);

			/**
			 * ---Lot's of bad multithreading with Swing, works though---
			 * Don't do this at home kids
			 *
			 * Shows loading screen
			 */
			new Thread(() -> {
				String choice = DialogBox.showDialogBox("Loading GDBoard...", "This may take a few seconds", "", new String[]{"Cancel"}, true);
				if(choice.equalsIgnoreCase("Cancel")){
					close();
				}
			}).start();

			/**
			 * Sets loading bar progress on loading screen
			 */
			new Thread(() -> {
				for(int i = 0; i < 90; i++){
					if(finishedLoading){
						break;
					}
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					DialogBox.setProgress(i);
				}
			}).start();

			/**
			 * Loads Geometry Dash data, if it fails to load and times out, continue anyways
			 */
			new Thread(() -> {
				try {
					LoadGD.load();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}).start();

			while(!LoadGD.loaded){
				if(LoadGD.timeout){
					break;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Assets.loadAssets();

			/**
			 * Starts thread that always checks for changes such as time, resolution, and color scheme
			 */
			Defaults.startMainThread();

			/**
			 * Wait until loaded
			 * I load colors separately due to dynamic color changing with windows
			 */
			while(!Defaults.programLoaded.get()){
				Thread.sleep(10);
			}
			Thread.sleep(500);
			while(!Defaults.colorsLoaded.get()){
				Thread.sleep(10);
			}
			finishedLoading = true;

			/**
			 * Finishes Progress bar
			 */
			new Thread(() -> {
				for(int i = 90; i < 100; i++){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					DialogBox.setProgress(i);
				}
			}).start();
			Thread.sleep(500);
			DialogBox.closeDialogBox();

			/**
			 * If first time launch, the user has to go through onboarding
			 * Show it and wait until finished
			 */
			if(Settings.getSettings("onboarding").equalsIgnoreCase("")){
				Onboarding.createPanel();
				Onboarding.loadSettings();
				Onboarding.refreshUI();
				Onboarding.frame.setVisible(true);
			}
			else{
				programStarting = false;
			}

			while(true) {
				if (!programStarting) {



					Settings.loadSettings(true);
					GDBoardBot.start();

					while(!GDBoardBot.connected){
						Thread.sleep(10);
					}

					if (!Settings.hasMonitor) {
						Settings.writeSettings("monitor", "0");
					}
					Thread.sleep(1000);

					if(!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
						Overlay.createOverlay();
						Reflections innerReflections = new Reflections("Main.InnerWindows", new SubTypesScanner(false));
						Set<Class<?>> innerClasses =
								innerReflections.getSubTypesOf(Object.class);
						for (Class<?> Class : innerClasses) {
							Method method = Class.getMethod("createPanel");
							method.invoke(null);
						}
					}
					else{
						CommentsWindow.createPanel();
						LevelsWindow.createPanel();
						InfoWindow.createPanel();
					}
					SettingsWindow.createPanel();
					if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
						Windowed.createPanel();
					}
					Settings.loadSettings(false);

					Reflections settingsReflections = new Reflections("Main.SettingsPanels", new SubTypesScanner(false));
					Set<Class<?>> settingsClasses =
							settingsReflections.getSubTypesOf(Object.class);
					for (Class<?> Class : settingsClasses) {
						try {
							Method method = Class.getMethod("loadSettings");
							method.invoke(null);
						}
						catch (NoSuchMethodException ignored){
						}
					}

					new Thread(() -> runKeyboardHook()).start();
					ControllerListener.hook();
					chatReader = new ChatReader();
					new Thread(() -> {
						chatReader.connect();
						chatReader.joinChannel(Settings.getSettings("channel"));
						chatReader.start();
					}).start();

					try {
						channelPointListener = new ChannelPointListener(new URI("wss://pubsub-edge.twitch.tv"));
						channelPointListener.connect();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					System.out.println(LoadGD.username);
					AccountSettings.refreshGD(LoadGD.username);

					Overlay.refreshUI(true);
					if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
						Windowed.resetCommentSize();
						Windowed.loadSettings();
						Windowed.frame.setVisible(true);

						Windowed.frame.setAlwaysOnTop(true);
						Windowed.frame.setAlwaysOnTop(false);

						Windowed.frame.toFront();
						Windowed.frame.requestFocus();
						Windowed.refresh();

					} else {
						Overlay.setVisible();
					}

					OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, 0));
					break;
				}
				Thread.sleep(100);
			}
			File file = new File(Defaults.saveDirectory + "\\GDBoard\\saved.txt");

			if (file.exists()) {
				Scanner sc = null;
				try {
					sc = new Scanner(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				assert sc != null;
				while (sc.hasNextLine()) {
					String[] level = sc.nextLine().split(",");
					try {
						Requests.addRequest(Long.parseLong(level[0]),level[1]);

					}
					catch (Exception e){
						Requests.levels.clear();
					}
				}
				sc.close();
			}
			Requests.addedLevels.clear();

			APIs.getViewers();

			sendMessages = true;
			allowRequests = true;
			refreshImages = true;

			Main.sendMessage("Thank you for using GDBoard by Alphalaneous and TreehouseFalcon! It is suggested to VIP or Mod GDBoard to prevent chat limits from occurring.");

			new Thread(() -> {
				while(true){
					try {
						Thread.sleep(120000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Main.sendMessage(" ");
				}
			}).start();

			Path path = Paths.get(Defaults.saveDirectory + "\\GDBoard\\bin\\gdmod.exe");
			if(!Files.exists(path)){
				URL inputUrl = Main.class.getResource("/Resources/gdmod.exe");
				FileUtils.copyURLToFile(inputUrl, path.toFile());
			}
			programLoaded = true;
		} catch (Exception e) {
			e.printStackTrace();
			String option = DialogBox.showDialogBox("Error!", "<html>" + e.toString() + ": " + e.getStackTrace()[0], "Please report to Alphalaneous#9687 on Discord.", new String[]{"Close"});
			close(true, false);
		}
	}
	static Channel channel;

	static {
		channel = Channel.getChannel(Settings.getSettings("channel"), chatReader);
	}

	static void sendMainMessage(String message){
		chatReader.sendMessage(message, channel);
	}
	static void sendMessage(String message, boolean whisper, String user) {
		if(!message.equalsIgnoreCase("")) {
			JSONObject messageObj = new JSONObject();
			messageObj.put("request_type", "send_message");
			if(whisper){
				messageObj.put("message", "/w " + user + " " + message);
			}
			else {
				messageObj.put("message", message);
			}
			GDBoardBot.sendMessage(messageObj.toString());
		}
	}
	static void sendMessage(String message) {
		if(!message.equalsIgnoreCase("")) {
			JSONObject messageObj = new JSONObject();
			messageObj.put("request_type", "send_message");
			messageObj.put("message", message);
			GDBoardBot.sendMessage(messageObj.toString());
		}
	}
	private static boolean failed = false;
	static Thread thread;
	private static void runKeyboardHook() {
		if(thread != null) {
			if (thread.isAlive()) {
				thread.stop();
			}
		}
		try {
			if (GlobalScreen.isNativeHookRegistered()) {
				GlobalScreen.unregisterNativeHook();
			}
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(new KeyListener());
			while (GlobalScreen.isNativeHookRegistered()) {
				Thread.sleep(100);
			}
		}
		catch (Exception e){
			try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
			failed = true;
		}
		thread = new Thread(() -> {
			while(true){
				if (failed){
					runKeyboardHook();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	public static void close(boolean forceLoaded, boolean load){
		boolean loaded = Main.programLoaded;
		if(forceLoaded){
			loaded = load;
		}
		try {
			if(Settings.getSettings("onboarding").equalsIgnoreCase("false") && loaded) {
				if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
					ActionsWindow.setSettings();
					CommentsWindow.setSettings();
					InfoWindow.setSettings();
					LevelsWindow.setSettings();
					SongWindow.setSettings();
					SettingsWindow.setSettings();
					Windowed.setSettings();
					Settings.writeLocation();
					Settings.writeSettings("monitor", String.valueOf(Defaults.screenNum));
				} else {
					Windowed.frame.setVisible(false);
					SettingsWindow.setSettings();
					Windowed.setSettings();
					Settings.writeLocation();
					WindowedSettings.setSettings();
				}
				try {
					channelPointListener.disconnectBot();
				}
				catch (WebsocketNotConnectedException ignored){}
				GeneralSettings.setSettings();
				RequestSettings.setSettings();
				ShortcutSettings.setSettings();
				OutputSettings.setSettings();
				try {
					GlobalScreen.unregisterNativeHook();
				} catch (NativeHookException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}


	public static void setUI(){
		HashMap<Object, Object> progressDefaults = new HashMap<>();
		for(Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()){
			if(entry.getKey().getClass() == String.class && ((String)entry.getKey()).startsWith("ProgressBar")){
				progressDefaults.put(entry.getKey(), entry.getValue());
			}
		}
		HashMap<Object, Object> tooltipDefaults = new HashMap<>();
		for(Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()){
			if(entry.getKey().getClass() == String.class && ((String)entry.getKey()).startsWith("ToolTip")){
				progressDefaults.put(entry.getKey(), entry.getValue());
			}
		}

		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel() {
				@Override
				public void provideErrorFeedback(Component component) {
				}
			});
		} catch (UnsupportedLookAndFeelException ignored) { }

		for(Map.Entry<Object, Object> entry : progressDefaults.entrySet()){
			UIManager.getDefaults().put(entry.getKey(), entry.getValue());
		}
		for(Map.Entry<Object, Object> entry : tooltipDefaults.entrySet()){
			UIManager.getDefaults().put(entry.getKey(), entry.getValue());
		}
		System.setProperty("sun.awt.noerasebackground", "true");
	}

	public static void createConfFiles(){
		Path conf = Paths.get(Defaults.saveDirectory + "\\GDBoard\\jre\\conf");
		Path confzip = Paths.get(Defaults.saveDirectory + "\\GDBoard\\jre\\conf.zip");

		if(!Files.exists(conf)){
			URL inputUrl = Main.class.getResource("/Resources/conf.zip");
			try {
				FileUtils.copyURLToFile(inputUrl, confzip.toFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(Files.exists(Paths.get(Defaults.saveDirectory + "\\GDBoard\\jre\\conf.zip"))){
				Path decryptTo = Paths.get(Defaults.saveDirectory + "\\GDBoard\\jre\\conf");
				try {
					Files.createDirectory(decryptTo);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Paths.get(Defaults.saveDirectory + "\\GDBoard\\jre\\conf.zip")))) {
					ZipEntry entry;
					while ((entry = zipInputStream.getNextEntry()) != null) {

						final Path toPath = decryptTo.resolve(entry.getName());
						if (entry.isDirectory()) {
							Files.createDirectory(toPath);
						} else {
							Files.copy(zipInputStream, toPath);
						}
					}
					Files.delete(confzip);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public static void close(){
		close(false, false);
	}
}
