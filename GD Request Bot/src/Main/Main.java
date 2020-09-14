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
	private static boolean finishedLoading = false;

	private static ChannelPointListener channelPointListener;

	public static void main(String[] args) throws IOException {

		/**
		 * Saves defaults of UI Elements before switching to Nimbus
		 * Sets to Nimbus, then sets defaults back
		 */
		setUI();
		PersonalizationSettings.loadSettings();
		/**
		 * Places config files in JRE folder in the GDBoard AppData as I forgot to
		 * include them in the bundled JRE
		 */
		createConfFiles();
		/**
		 * Sets Windowed to true as it is default and previously wasn't, easiest fix for all
		 * checks of windowed mode
		 */
		if (Settings.getSettings("windowed").equalsIgnoreCase("")) {
			Settings.writeSettings("windowed", "true");
		}

		Defaults.programLoaded.set(false);
		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		genv.registerFont(Defaults.SYMBOLS);
		genv.registerFont(Defaults.MAIN_FONT);
		genv.registerFont(Defaults.SEGOE);
		try {

			/** Disables logging used with JDash */
			Language.startFileChangeListener();
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
				String choice = DialogBox.showDialogBox("$LOADING_GDBOARD$", "$LOADING_GDBOARD_INFO$", "", new String[]{"$CANCEL$"}, true, new Object[]{});
				if (choice.equalsIgnoreCase("CANCEL")) {
					close();
				}
			}).start();

			/** Sets loading bar progress on loading screen */
			new Thread(() -> {
				for (int i = 0; i < 90; i++) {
					if (finishedLoading) {
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

			/** Loads Geometry Dash data, if it fails to load and times out, continue anyways */
			new Thread(() -> {
				try {
					LoadGD.load(false);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}).start();

			while (!LoadGD.loaded) {
				if (LoadGD.timeout) {
					break;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Assets.loadAssets();
			addMissingFiles();
			/** Starts thread that always checks for changes such as time, resolution, and color scheme */
			Defaults.startMainThread();

			/**
			 * Wait until loaded
			 * I load colors separately due to dynamic color changing with windows
			 */
			while (!Defaults.programLoaded.get()) {
				Thread.sleep(10);
			}
			while (!Defaults.colorsLoaded.get()) {
				Thread.sleep(10);
			}
			finishedLoading = true;

			/** Finishes Progress bar */
			new Thread(() -> {
				for (int i = 90; i < 100; i++) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					DialogBox.setProgress(i);
				}
			}).start();
			DialogBox.closeDialogBox();

			/**
			 * If first time launch, the user has to go through onboarding
			 * Show it and wait until finished
			 */
			if (Settings.getSettings("onboarding").equalsIgnoreCase("")) {
				Onboarding.createPanel();
				Onboarding.loadSettings();
				Onboarding.refreshUI();
				Onboarding.frame.setVisible(true);
			} else {
				programStarting = false;
			}

			while (true) {
				if (!programStarting) {

					Settings.loadSettings(true);
					Variables.loadVars();
					GDBoardBot.start();

					/** Wait for GDBoard to connect before proceeding */
					while (!GDBoardBot.initialConnect) {
						Thread.sleep(100);
					}

					/** If there is no monitor setting, default to 0 */
					if (!Settings.hasMonitor) {
						Settings.writeSettings("monitor", "0");
					}

					/**
					 * If not windowed mode, create all panels
					 * Uses reflection to easily loop when more are added
					 */
					if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
						Overlay.createOverlay();
						Reflections innerReflections = new Reflections("Main.InnerWindows", new SubTypesScanner(false));
						Set<Class<?>> innerClasses =
								innerReflections.getSubTypesOf(Object.class);
						for (Class<?> Class : innerClasses) {
							Method method = Class.getMethod("createPanel");
							method.invoke(null);
						}
					}
					/** Else create these 3 for the windowed frame */
					else {
						CommentsWindow.createPanel();
						LevelsWindow.createPanel();
						InfoWindow.createPanel();
						Windowed.createPanel();
					}

					/** Create the settings pane; */
					CommandEditor.createPanel();
					SettingsWindow.createPanel();

					/** Load GDBoard Settings */
					while(true) {
						try {
							Settings.loadSettings(false);
							break;
						}
						catch (Exception ignored){
						}
						Thread.sleep(100);
					}
					/**
					 * Load Settings panels and Settings
					 * Uses reflection to easily loop when more are added
					 */
					Reflections settingsReflections = new Reflections("Main.SettingsPanels", new SubTypesScanner(false));
					Set<Class<?>> settingsClasses =
							settingsReflections.getSubTypesOf(Object.class);
					for (Class<?> Class : settingsClasses) {
						for (Method method : Class.getMethods()) {
							if (method.getName().equalsIgnoreCase("loadSettings")) {
								while(true) {
									try {
										method.invoke(null);
										break;
									} catch (Exception e) {
									}
								}
							}
						}
					}

					/**
					 * Runs keyboard and Controller hook for global keybinds
					 * Runs on separate Threads
					 */
					new Thread(() -> runKeyboardHook()).start();
					new Thread(() -> ControllerListener.hook());

					/**
					 * Reads chat as streamer, reduces load on servers for some actions
					 * such as custom commands that don't use the normal prefix
					 */



					/** Reads channel point redemptions for channel point triggers */
					try {
						channelPointListener = new ChannelPointListener(new URI("wss://pubsub-edge.twitch.tv"));
						channelPointListener.connect();
					} catch (URISyntaxException e) {
						e.printStackTrace();
						/** Should never fail */
					}

					/** Refresh GD Username in Account Settings */
					AccountSettings.refreshGD(LoadGD.username);


					Overlay.refreshUI(true);

					if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
						Windowed.resetCommentSize();
						Windowed.loadSettings();
						Windowed.frame.setVisible(true);

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
						if(level.length < 26){
							Requests.forceAdd(level[0], level[1], Long.parseLong(level[2]), level[3], Boolean.parseBoolean(level[4]),
									Boolean.parseBoolean(level[5]), Integer.parseInt(level[6]), level[7], Integer.parseInt(level[8]),
									Integer.parseInt(level[9]), new String(Base64.getDecoder().decode(level[10])), Integer.parseInt(level[11]), Integer.parseInt(level[12]),
									level[13], Integer.parseInt(level[14]), Integer.parseInt(level[15]), new String(Base64.getDecoder().decode(level[16])), level[17],
									Integer.parseInt(level[18]), Long.parseLong(level[19]), Boolean.parseBoolean(level[20]), Boolean.parseBoolean(level[21]),
									-1, null, null, false);
						}
						if(level.length == 26) {
							Requests.forceAdd(level[0], level[1], Long.parseLong(level[2]), level[3], Boolean.parseBoolean(level[4]),
									Boolean.parseBoolean(level[5]), Integer.parseInt(level[6]), level[7], Integer.parseInt(level[8]),
									Integer.parseInt(level[9]), new String(Base64.getDecoder().decode(level[10])), Integer.parseInt(level[11]), Integer.parseInt(level[12]),
									level[13], Integer.parseInt(level[14]), Integer.parseInt(level[15]), new String(Base64.getDecoder().decode(level[16])), level[17],
									Integer.parseInt(level[18]), Long.parseLong(level[19]), Boolean.parseBoolean(level[20]), Boolean.parseBoolean(level[21]),
									Integer.parseInt(level[22]), level[23], level[24], Boolean.parseBoolean(level[25]));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				sc.close();
			}

			Functions.saveFunction();
			LevelsWindow.setOneSelect();
			APIs.getViewers();
			CommentsWindow.loadComments(0, false);

			sendMessages = true;
			allowRequests = true;
			refreshImages = true;

			Main.sendMessage(Utilities.format("$STARTUP_MESSAGE$"));

			new Thread(() -> {
				while (true) {
					try {
						Thread.sleep(120000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Main.sendMessage(" ");
				}
			}).start();
			APIs.setAllViewers();


			programLoaded = true;
		} catch (Exception e) {
			e.printStackTrace();
			String option = DialogBox.showDialogBox("Error!", "<html>" + e.toString() + ": " + e.getStackTrace()[0], "Please report to Alphalaneous#9687 on Discord.", new String[]{"Close"});
			close(true, false);
		}
	}


	public static void refreshBot() {
		try {
			GDBoardBot.start();
			channelPointListener.disconnectBot();
			channelPointListener.reconnectBot();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static void sendMainMessage(String message) {
		GDBoardBot.sendMainMessage(message);
	}

	static boolean onCool = false;

	static void addMissingFiles(){
		try {
			Path path = Paths.get(Defaults.saveDirectory + "\\GDBoard\\bin\\gdmod.exe");
			if (!Files.exists(path)) {
				URL inputUrl = Main.class.getResource("/Resources/gdmod.exe");
				FileUtils.copyURLToFile(inputUrl, path.toFile());
			}
			Path pathA = Paths.get(Defaults.saveDirectory + "\\GDBoard\\bin\\getProgram.bat");
			if (!Files.exists(pathA)) {
				URL inputUrl = Main.class.getResource("/Resources/getProgram.bat");
				FileUtils.copyURLToFile(inputUrl, pathA.toFile());
			}
			Path pathB = Paths.get(Defaults.saveDirectory + "\\GDBoard\\bin\\getInstalledPrograms.bat");
			if (!Files.exists(pathB)) {
				URL inputUrl = Main.class.getResource("/Resources/getInstalledPrograms.bat");
				FileUtils.copyURLToFile(inputUrl, pathB.toFile());
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	static void sendMessage(String message, boolean whisper, String user) {
		if(!GeneralBotSettings.silentOption || message.equalsIgnoreCase(" ")) {
			if (!message.equalsIgnoreCase("")) {

				while (onCool) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				onCool = true;
				JSONObject messageObj = new JSONObject();
				messageObj.put("request_type", "send_message");
				if (whisper) {
					messageObj.put("message", "/w " + user + " " + message);
				} else {
					messageObj.put("message", message);
				}
				GDBoardBot.sendMessage(messageObj.toString());
				new Thread(() -> {
					try {
						Thread.sleep(500);
						onCool = false;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}).start();
			}
		}
	}

	static void sendMessage(String message) {
		sendMessage(message, false, null);
	}

	private static boolean failed = false;
	static Thread thread;

	private static void runKeyboardHook() {
		if (thread != null) {
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
		} catch (Exception e) {
			try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
			failed = true;
		}
		thread = new Thread(() -> {
			while (true) {
				if (failed) {
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

	public static void close(boolean forceLoaded, boolean load) {
		boolean loaded = Main.programLoaded;
		if (forceLoaded) {
			loaded = load;
		}
		try {
			if (Settings.getSettings("onboarding").equalsIgnoreCase("false") && loaded) {
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
				}
				try {
					channelPointListener.disconnectBot();
				} catch (WebsocketNotConnectedException ignored) {
				}
				Variables.saveVars();
				GeneralSettings.setSettings();
				GeneralBotSettings.setSettings();
				RequestSettings.setSettings();
				ShortcutSettings.setSettings();
				OutputSettings.setSettings();
				PersonalizationSettings.setSettings();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			GlobalScreen.unregisterNativeHook();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}


	public static void setUI() {
		HashMap<Object, Object> progressDefaults = new HashMap<>();
		for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
			if (entry.getKey().getClass() == String.class && ((String) entry.getKey()).startsWith("ProgressBar")) {
				progressDefaults.put(entry.getKey(), entry.getValue());
			}
		}
		HashMap<Object, Object> tooltipDefaults = new HashMap<>();
		for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
			if (entry.getKey().getClass() == String.class && ((String) entry.getKey()).startsWith("ToolTip")) {
				progressDefaults.put(entry.getKey(), entry.getValue());
			}
		}
		HashMap<Object, Object> menuItemDefaults = new HashMap<>();
		for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
			if (entry.getKey().getClass() == String.class && ((String) entry.getKey()).startsWith("MenuItem")) {
				progressDefaults.put(entry.getKey(), entry.getValue());
			}
		}
		HashMap<Object, Object> scrollBarDefaults = new HashMap<>();
		for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
			if (entry.getKey().getClass() == String.class && ((String) entry.getKey()).startsWith("ScrollBar")) {
				progressDefaults.put(entry.getKey(), entry.getValue());
			}
		}

		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel() {
				@Override
				public void provideErrorFeedback(Component component) {
				}
			});
		} catch (UnsupportedLookAndFeelException ignored) {
		}

		for (Map.Entry<Object, Object> entry : progressDefaults.entrySet()) {
			UIManager.getDefaults().put(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<Object, Object> entry : tooltipDefaults.entrySet()) {
			UIManager.getDefaults().put(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<Object, Object> entry : menuItemDefaults.entrySet()) {
			UIManager.getDefaults().put(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<Object, Object> entry : scrollBarDefaults.entrySet()) {
			UIManager.getDefaults().put(entry.getKey(), entry.getValue());
		}

		System.setProperty("sun.awt.noerasebackground", "true");
		UIManager.put("Menu.selectionBackground", Color.RED);
		UIManager.put("Menu.selectionForeground", Color.WHITE);
		UIManager.put("Menu.background", Color.WHITE);
		UIManager.put("Menu.foreground", Color.BLACK);
		UIManager.put("Menu.opaque", false);
		UIManager.put("ToolTipManager.enableToolTipMode", "allWindows");

	}

	public static void createConfFiles() {
		Path conf = Paths.get(Defaults.saveDirectory + "\\GDBoard\\jre\\conf");
		Path confzip = Paths.get(Defaults.saveDirectory + "\\GDBoard\\jre\\conf.zip");

		if (!Files.exists(conf)) {
			URL inputUrl = Main.class.getResource("/Resources/conf.zip");
			try {
				FileUtils.copyURLToFile(inputUrl, confzip.toFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (Files.exists(Paths.get(Defaults.saveDirectory + "\\GDBoard\\jre\\conf.zip"))) {
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

	public static void close() {
		close(false, false);
	}
}
