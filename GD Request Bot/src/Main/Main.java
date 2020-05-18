package Main;

import SettingsPanels.*;
import com.cavariux.twitchirc.Chat.User;
import org.apache.commons.io.FileUtils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    static List<User> mods = new ArrayList<>();
    static boolean starting = true;
    static boolean loaded = false;
    static boolean allowRequests = false;
    static boolean doMessage  = false;
    static boolean doImage  = false;
    private static JDialog dialog = new JDialog();
    private static JPanel panel = new JPanel();
    private static JLabel tf = new JLabel("Loading...");
    public static void main(String[] args) {
        Defaults.loaded.set(false);
        //TODO Use nio everywhere
        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            logger.setUseParentHandlers(false);

            dialog.setSize(new Dimension(200,100));
            tf.setForeground(Color.WHITE);
            tf.setFont(new Font("bahnschrift", Font.BOLD, 20));
            panel.add(tf);
            panel.setBackground(new Color(31, 31, 31));
            panel.setLayout(new GridBagLayout());
            dialog.add(panel);

            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            for ( WindowListener wl : dialog.getWindowListeners())
                dialog.removeWindowListener(wl);
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
            int j = 0;
            while(!Defaults.colorsLoaded.get()){
                System.out.println("Loading Colors... " + i);
                Thread.sleep(10);
                j++;
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
                    if (!Settings.windowedMode) {
                        Overlay.setFrame();                //Creates the JFrame that contains everything
                        MainBar.createBar();            //Creates the main "Game Bar" in the top center
                    }
                    ChatReader chatReader = new ChatReader();
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
                    LevelsWindow.createPanel();         //Creates the Levels Window containing all the requests in the level queue
                    ActionsWindow.createPanel();        //Creates the Action Window containing buttons that do specific actions
                    InfoWindow.createPanel();           //Creates the Info Window containing the information of the selected level
                    CommentsWindow.createPanel();       //Creates the Comment Window containing the comments of the selected level
                    SongWindow.createPanel();           //Creates the Song Window allowing you to play the song of the selected level
                    SettingsWindow.createPanel();
                    //Randomizer.createPanel();
                    if (Settings.windowedMode) {
                        Windowed.createPanel();
                    }
                    InfoWindow.refreshInfo();            //Refreshes the information shown on the Info Window for the first time
                    SongWindow.refreshInfo();            //Refreshes the information shown on the Song Window for the first time
                    Settings.loadSettings(false);
                    GeneralSettings.loadSettings();
                    WindowedSettings.loadSettings();
                    OutputSettings.loadSettings();
                    RequestSettings.loadSettings();
                    ShortcutSettings.loadSettings();
                    ControllerListener.hook();         //Starts Controller Listener

                    GlobalScreen.registerNativeHook();
                    GlobalScreen.addNativeKeyListener(new KeyListener());

                    Overlay.refreshUI(true);
                    if (Settings.windowedMode) {
                        Windowed.frame.setVisible(true);
                    }
                    else {
                        Overlay.setVisible();
                    }
                    SettingsWindow.toFront();
                    SettingsWindow.toggleVisible();
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
            doMessage = true;
            allowRequests = true;
            doImage = true;
            Main.sendMessage("Thank you for using GDBoard by Alphalaneous and TreeHouseFalcon! Type !help for list of commands!");
            Thread thread = new Thread(() -> {
                while(true){
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Main.sendMessage(" ");
                }
            });
            thread.start();
            Path path = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\bin\\gdkill.exe");
            if(!Files.exists(path)){
                URL inputUrl = Main.class.getResource("/Resources/gdkill.exe");
                FileUtils.copyURLToFile(inputUrl, path.toFile());
            }

            loaded = true;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(Overlay.frame, e, "Error", JOptionPane.ERROR_MESSAGE);
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
