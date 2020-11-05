package Main;

import Main.Panels.*;
import Main.SettingsPanels.*;
import Main.Windows.CommandEditor;
import Main.Windows.DialogBox;
import Main.Windows.SettingsWindow;
import Main.Windows.Window;
import org.apache.commons.io.FileUtils;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.io.*;
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
	static boolean sendMessages = false;
	private static ChatListener chatReader2;
	static boolean allowRequests = false;

	private static ChannelPointListener channelPointListener;

	public static void main(String[] args) {
		/*
		  Saves defaults of UI Elements before switching to Nimbus
		  Sets to Nimbus, then sets defaults back
		 */
		setUI();
		Settings.loadSettings();
		PersonalizationSettings.loadSettings();
		/*
		  Places config files in JRE folder in the GDBoard AppData as I forgot to
		  include them in the bundled JRE
		 */
		createConfFiles();

		if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			Settings.writeSettings("windowed", "true");
		}

		Defaults.programLoaded.set(false);
		try {

			/* Disables logging used with JDash */
			Language.startFileChangeListener();
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);
			logger.setUseParentHandlers(false);


			LoadGD.load();

			Assets.loadAssets();
			addMissingFiles();
			Defaults.startMainThread();

			/*
			  If first time launch, the user has to go through onboarding
			  Show it and wait until finished
			 */
			if (Settings.getSettings("onboarding").equalsIgnoreCase("")) {
				Onboarding.createPanel();
				Onboarding.loadSettings();
				Onboarding.refreshUI();
				Onboarding.frame.setVisible(true);
				Onboarding.isLoading = true;
			} else {
				programStarting = false;
			}

			while (Onboarding.isLoading) {
				Thread.sleep(100);
			}
			Variables.loadVars();
			new Thread(() -> {
				while (true) {
					try {
						if (chatReader2 != null) {
							try {
								chatReader2.disconnect();
							} catch (WebsocketNotConnectedException ignored) {
							}
						}
						chatReader2 = new ChatListener(Settings.getSettings("channel"));
						chatReader2.connect(Settings.getSettings("oauth"), Settings.getSettings("channel"));
						while (!chatReader2.isClosed()) {
							Thread.sleep(100);
						}
					} catch (Exception ignored) {
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
			GDBoardBot.start();


			/* Wait for GDBoard to connect before proceeding */
			while (!GDBoardBot.initialConnect) {
				Thread.sleep(100);
			}
			while (true) {
				if (!programStarting) {

					/* If there is no monitor setting, default to 0 */
					if (!Settings.getSettings("monitor").equalsIgnoreCase("")) {
						Settings.writeSettings("monitor", "0");
					}

					CommentsPanel.createPanel();
					LevelsPanel.createPanel();
					InfoPanel.createPanel();
					Window.createPanel();


					/* Create the settings pane; */
					CommandEditor.createPanel();
					SettingsWindow.createPanel();

					/*
					  Load Settings panels and Settings
					  Uses reflection to easily loop when more are added
					 */

					GeneralBotSettings.loadSettings();
					GeneralSettings.loadSettings();
					OutputSettings.loadSettings();
					RequestSettings.loadSettings();
					ShortcutSettings.loadSettings();


					/*
					  Runs keyboard and Controller hook for global keybinds
					  Runs on separate Threads
					 */
					new Thread(() -> runKeyboardHook()).start();


					/* Reads channel point redemptions for channel point triggers */
					try {
						channelPointListener = new ChannelPointListener(new URI("wss://pubsub-edge.twitch.tv"));
						channelPointListener.connect();
					} catch (URISyntaxException e) {
						e.printStackTrace();
						/* Should never fail */
					}

					/* Refresh GD Username in Account Settings */
					AccountSettings.refreshGD(LoadGD.username);

					Themes.refreshUI();

					Window.resetCommentSize();
					Window.loadSettings();
					Window.frame.setVisible(true);

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
					Thread.sleep(100);
					String[] level = sc.nextLine().split(",");
					try {
						if (level.length < 26) {
							Requests.forceAdd(level[0], level[1], Long.parseLong(level[2]), level[3], Boolean.parseBoolean(level[4]),
									Boolean.parseBoolean(level[5]), Integer.parseInt(level[6]), level[7], Integer.parseInt(level[8]),
									Integer.parseInt(level[9]), new String(Base64.getDecoder().decode(level[10])), Integer.parseInt(level[11]), Integer.parseInt(level[12]),
									level[13], Integer.parseInt(level[14]), Integer.parseInt(level[15]), new String(Base64.getDecoder().decode(level[16])), level[17],
									Integer.parseInt(level[18]), Long.parseLong(level[19]), Boolean.parseBoolean(level[20]), Boolean.parseBoolean(level[21]),
									-1, null, null, false);
						}
						if (level.length == 26) {
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
			allowRequests = true;
			Functions.saveFunction();
			LevelsPanel.setOneSelect();
			APIs.getViewers();
			CommentsPanel.loadComments(0, false);
			Board.signal();
			while (!LoadGD.loaded) {
				Thread.sleep(10);
			}
			sendMessages = true;

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
			programLoaded = true;
		} catch (Exception e) {
			e.printStackTrace();
			DialogBox.showDialogBox("Error!", "<html>" + e.toString() + ": " + e.getStackTrace()[0], "Please report to Alphalaneous#9687 on Discord.", new String[]{"Close"});
			close(true, false);
		}
	}


	static void refreshBot() {
		try {
			GDBoardBot.start(true);
			if (channelPointListener != null) {
				channelPointListener.disconnectBot();
			}
			channelPointListener = new ChannelPointListener(new URI("wss://pubsub-edge.twitch.tv"));
			channelPointListener.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static void sendMainMessage(String message) {

		chatReader2.sendMessage(message);
	}

	private static boolean onCool = false;

	private static void addMissingFiles() {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean cooldown = false;

	static void sendMessage(String message, boolean whisper, String user) {
		if (cooldown) {
			return;
		}
		cooldown = true;
		new Thread(() -> {
			try {
				Thread.sleep(GeneralBotSettings.cooldown);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cooldown = false;
		}).start();
		if (!GeneralBotSettings.silentOption || message.equalsIgnoreCase(" ")) {
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
				if (GeneralBotSettings.antiDox) {
					message = message.replaceAll(System.getProperty("user.name"), "*****");
				}
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
		} else if (whisper) {
			if (!message.equalsIgnoreCase("")) {
				JSONObject messageObj = new JSONObject();
				messageObj.put("request_type", "send_message");
				if (GeneralBotSettings.antiDox) {
					message = message.replaceAll(System.getProperty("user.name"), "*****");
				}
				messageObj.put("message", "/w " + user + " " + message);
				GDBoardBot.sendMessage(messageObj.toString());
			}
		}
	}

	public static void sendMessage(String message) {
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

			if (Settings.getSettings("onboarding").equalsIgnoreCase("false") && loaded) {

				Window.frame.setVisible(false);
				Window.setSettings();
				Settings.writeLocation();

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
				Settings.saveSettings();

			}
		try {
			GlobalScreen.unregisterNativeHook();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}


	public static void setUI() {
		HashMap<Object, Object> defaults = new HashMap<>();
		for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
			if (entry.getKey().getClass() == String.class && ((String) entry.getKey()).startsWith("ProgressBar")) {
				defaults.put(entry.getKey(), entry.getValue());
			}
		}
		for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
			if (entry.getKey().getClass() == String.class && ((String) entry.getKey()).startsWith("ToolTip")) {
				defaults.put(entry.getKey(), entry.getValue());
			}
		}
		for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
			if (entry.getKey().getClass() == String.class && ((String) entry.getKey()).startsWith("MenuItem")) {
				defaults.put(entry.getKey(), entry.getValue());
			}
		}
		for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
			if (entry.getKey().getClass() == String.class && ((String) entry.getKey()).startsWith("ScrollBar")) {
				defaults.put(entry.getKey(), entry.getValue());
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

		for (Map.Entry<Object, Object> entry : defaults.entrySet()) {
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

	private static void createConfFiles() {
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
