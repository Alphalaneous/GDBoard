package Main;

import Main.InnerWindows.*;
import Main.SettingsPanels.*;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import javazoom.jl.player.JavaSoundAudioDevice;
import org.apache.commons.io.FileUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.multi.MultiLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {


	static boolean starting = true;
	static boolean loaded = false;
	static boolean allowRequests = false;
	static boolean doMessage  = false;
	static boolean doImage  = false;
	private static ChatReader chatReader = new ChatReader();
	private static ChannelPointListener client;
	private static boolean passed = false;
	static {
		try {
			client = new ChannelPointListener(new URI("wss://pubsub-edge.twitch.tv"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		HashMap<Object, Object> progressDefaults = new HashMap<>();
		for(Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()){
			if(entry.getKey().getClass() == String.class && ((String)entry.getKey()).startsWith("ProgressBar")){
				progressDefaults.put(entry.getKey(), entry.getValue());
			}
		}

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
		try {
			if(Settings.getSettings("windowed").equalsIgnoreCase("")){
				Settings.writeSettings("windowed", "true");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*try {
			System.setOut(new PrintStream(new FileOutputStream(new File(System.getenv("APPDATA") + "\\GDBoard\\clOutput.txt"))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/

		Defaults.loaded.set(false);
		try {
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);
			logger.setUseParentHandlers(false);

			new Thread(() -> {
				String choice = DialogBox.showDialogBox("Loading GDBoard...", "This may take a few seconds", "", new String[]{"Cancel"}, true);
				if(choice.equalsIgnoreCase("Cancel")){
					close();
				}
			}).start();

			//dialog.setVisible(true);

			UIManager.setLookAndFeel(new NimbusLookAndFeel() {
				@Override
				public void provideErrorFeedback(Component component) {
				}
			});
			for(Map.Entry<Object, Object> entry : progressDefaults.entrySet()){
				UIManager.getDefaults().put(entry.getKey(), entry.getValue());
			}

			new Thread(() -> {
				for(int i = 0; i < 90; i++){
					if(passed){
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

			LoadGD.load();
			Assets.loadAssets();

			client.connect();
			Defaults.startMainThread();        //Starts thread that always checks for changes such as time, resolution, and color scheme

			while(!Defaults.loaded.get()){
				Thread.sleep(10);
			}
			Thread.sleep(500);



			while(!Defaults.colorsLoaded.get()){
				Thread.sleep(10);
			}
			passed = true;
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
			if(Settings.getSettings("onboarding").equalsIgnoreCase("")){
				Onboarding.createPanel();
				Onboarding.loadSettings();
				Onboarding.refreshUI();
				Onboarding.frame.setVisible(true);
			}
			else{
				starting = false;
			}


			while(true) {
				if (!starting) {
					Settings.loadSettings(true);
					GDBoardBot.start();

					if (!Settings.hasMonitor) {
						Settings.writeSettings("monitor", "0");
					}
					Thread.sleep(1000);
					JSONObject authObj = new JSONObject();
					authObj.put("request_type", "connect");
					authObj.put("oauth", Settings.getSettings("oauth"));
					GDBoardBot.sendMessage(authObj.toString());
					Thread.sleep(1000);
					while(!GDBoardBot.connected){
						GDBoardBot.sendMessage(authObj.toString());
						Thread.sleep(15000);
					}
					if (GDBoardBot.failed) {
						APIs.setOauth();
					}
					Overlay.setFrame();                //Creates the JFrame that contains everything

					if(!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
						MainBar.createBar();            //Creates the main "Game Bar" in the top center
					}

					if(!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
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
						Windowed.loadSettings();
					}
					Settings.loadSettings(false);
					AccountSettings.refreshGD(LoadGD.username);

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

					Thread thread1 = new Thread(() -> runKeyboardHook());
					ControllerListener.hook();
					thread1.start();

					Thread thread = new Thread(() -> {
						chatReader.connect();
						try {
							chatReader.joinChannel(Settings.getSettings("channel"));
						} catch (IOException e) {
							e.printStackTrace();
						}
						chatReader.start();
					});
					thread.start();
					Overlay.refreshUI(true);
					if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
						Windowed.resetCommentSize();
						Windowed.frame.setVisible(true);

					}
					Overlay.setVisible();

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
						Requests.addRequest(Long.parseLong(level[0]), level[1]);

					}
					catch (Exception e){
						Requests.levels.clear();
					}
				}
				sc.close();
			}
			Requests.addedLevels.clear();

			APIs.getViewers();

			doMessage = true;
			allowRequests = true;
			doImage = true;

			Main.sendMessage("Thank you for using GDBoard by Alphalaneous and TreehouseFalcon! It is suggested to VIP or Mod GDBoard to prevent chat limits from occurring.");

			Thread threada = new Thread(() -> {
				while(true){
					try {
						Thread.sleep(120000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Main.sendMessage(" ");
				}
			});
			threada.start();

			/*Thread ping = new Thread(() -> {
				while(true){
					ChannelPointListener.pong = false;
					ChannelPointListener.sendMessage("{\"type\": \"PING\"}");

					try {
						Thread.sleep(240000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!ChannelPointListener.pong){
						try {
							ChannelPointListener.restart();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			ping.start();*/
			Path path = Paths.get(Defaults.saveDirectory + "\\GDBoard\\bin\\gdmod.exe");
			if(!Files.exists(path)){
				URL inputUrl = Main.class.getResource("/Resources/gdmod.exe");
				FileUtils.copyURLToFile(inputUrl, path.toFile());
			}
			loaded = true;

		} catch (Exception e) {
			e.printStackTrace();
			String option = DialogBox.showDialogBox("Error!", e.toString(), "Please report to Alphalaneous.", new String[]{"OK"});
		}
	}
	static Channel channel;

	static {
		try {
			channel = Channel.getChannel(Settings.getSettings("channel"), chatReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public static void close(){
		if(!Settings.onboarding && loaded) {
			if (!Settings.windowedMode) {
				ActionsWindow.setSettings();
				CommentsWindow.setSettings();
				InfoWindow.setSettings();
				LevelsWindow.setSettings();
				SongWindow.setSettings();
				SettingsWindow.setSettings();
				Windowed.setSettings();

				try {
					Settings.writeLocation();
					Settings.writeSettings("monitor", String.valueOf(Defaults.screenNum));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else {
				Windowed.frame.setVisible(false);
				SettingsWindow.setSettings();
				Windowed.setSettings();
				try {
					Settings.writeLocation();
				} catch (IOException e) {
					e.printStackTrace();
				}
				WindowedSettings.setSettings();
			}
			try {
				client.disconnectBot();
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
		System.exit(0);
	}
}
