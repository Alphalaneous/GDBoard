package Main;

import Main.InnerWindows.*;
import Main.SettingsPanels.*;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import org.apache.commons.io.FileUtils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	static boolean starting = true;
	static boolean loaded = false;
	static boolean allowRequests = false;
	static boolean doMessage  = false;
	static boolean doImage  = false;
	private static JDialog dialog = new JDialog();
	private static JPanel panel = new JPanel();
	private static JLabel tf = new JLabel("Loading...");
	private static ChatReader chatReader = new ChatReader();
	public static void main(String[] args) {
		System.setProperty("http.agent", "");
		System.setProperty("http.keepAlive", "false");

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

			dialog.setSize(new Dimension(200,100));
			tf.setForeground(Color.WHITE);
			tf.setFont(Defaults.MAIN_FONT.deriveFont(20f));
			panel.add(tf);
			panel.setBackground(new Color(31, 31, 31));
			panel.setLayout(new GridBagLayout());
			dialog.add(panel);

			dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			for ( WindowListener wl : dialog.getWindowListeners()) {
				dialog.removeWindowListener(wl);
			}
			dialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					close();
				}
			});
			dialog.setResizable(false);
			dialog.setFocusable(false);
			dialog.setFocusableWindowState(false);
			dialog.setTitle("Starting GDBoard");
			dialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2 - dialog.getWidth()/2, Toolkit.getDefaultToolkit().getScreenSize().height/2 - dialog.getHeight()/2);
			dialog.setVisible(true);

			UIManager.setLookAndFeel(new NimbusLookAndFeel() {
				@Override
				public void provideErrorFeedback(Component component) {
				}
			});

			Defaults.startMainThread();        //Starts thread that always checks for changes such as time, resolution, and color scheme
			int i = 0;
			while(!Defaults.loaded.get()){
				System.out.println("Loading... " + i);
				Thread.sleep(10);
				i++;
			}
			Thread.sleep(500);
			i = 0;
			while(!Defaults.colorsLoaded.get()){
				System.out.println("Loading Colors... " + i);
				Thread.sleep(10);
			}
			dialog.setVisible(false);
			Thread.sleep(500);
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


					if (!Settings.hasWindowed) {
						Settings.writeSettings("windowed", "false");
					}
					if (!Settings.hasMonitor) {
						Settings.writeSettings("monitor", "0");
					}
					MouseLock.startLock();
					Thread.sleep(1000);
					JSONObject authObj = new JSONObject();
					authObj.put("request_type", "connect");
					authObj.put("oauth", Settings.oauth);
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

					if (!Settings.windowedMode) {
						MainBar.createBar();            //Creates the main "Game Bar" in the top center
					}

					if(!Settings.windowedMode) {
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
					if (Settings.windowedMode) {
						Windowed.createPanel();
						Windowed.loadSettings();
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

					ControllerListener.hook();         //Starts Controller Listener

					Thread thread1 = new Thread(() -> runKeyboardHook());
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
					if (Settings.windowedMode) {
						Windowed.frame.setVisible(true);
					}
					Overlay.setVisible();

					OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, 0));
					break;
				}
				Thread.sleep(100);
			}
			File file = new File(System.getenv("APPDATA") + "\\GDBoard\\saved.txt");

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
						Requests.addRequest(level[0], level[1]);
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
			Path path = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\bin\\gdmod.exe");
			if(!Files.exists(path)){
				URL inputUrl = Main.class.getResource("/Resources/gdmod.exe");
				FileUtils.copyURLToFile(inputUrl, path.toFile());
			}

			loaded = true;

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(Overlay.frame, e, "Error", JOptionPane.ERROR_MESSAGE);
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
