package com.alphalaneous;

import com.alphalaneous.SettingsPanels.*;
import com.alphalaneous.Tabs.*;
import com.alphalaneous.Windows.*;
import com.alphalaneous.Windows.Window;
import org.apache.commons.io.FileUtils;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {


    public static boolean programLoaded = false;
    public static boolean sendMessages = false;
    public static boolean allowRequests = false;
    public static Thread keyboardHookThread;

    private static ChannelPointListener channelPointListener;
    private static ChatListener chatReader;
    private static ServerBot serverBot = null;
    private static boolean keepConnecting = true;
    private static boolean onCool = false;
    private static boolean cooldown = false;
    private static boolean failed = false;
    private static final ArrayList<Image> iconImages = new ArrayList<>();
    private static final ImageIcon icon = Assets.loquibot;
    private static final Image newIcon16 = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
    private static final Image newIcon32 = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);

    public static void main(String[] args) {

        iconImages.add(newIcon16);
        iconImages.add(newIcon32);

        JFrame starting = new JFrame("loquibot");
        starting.setSize(200, 200);
        starting.setResizable(false);
        starting.setLocationRelativeTo(null);
        starting.setUndecorated(true);
        starting.setBackground(new Color(0, 0, 0, 0));
        starting.add(new JLabel(Assets.loquibotLarge));
        starting.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        starting.setIconImages(iconImages);
        starting.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        starting.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Main.close();
            }
        });
        starting.setVisible(true);

        System.out.println("> Start");

        //Saves defaults of UI Elements before switching to Nimbus
        //Sets to Nimbus, then sets defaults back

        setUI();
        Settings.loadSettings();
        System.out.println("> Settings Loaded");

		//Places config files in JRE folder in the GDBoard AppData as I forgot to
		//include them in the bundled JRE

        createConfFiles();
        System.out.println("> Config Files Created");

        if (Settings.getSettings("onboarding").exists()) {
            try {
                TwitchAccount.setInfo();
                new Thread(ChannelPointSettings::refresh).start();
            } catch (Exception e) {
                e.printStackTrace();
                APIs.setOauth();
                TwitchAccount.setInfo();
                new Thread(ChannelPointSettings::refresh).start();
            }
        }
        System.out.println("> Twitch Loaded");

        try {
            Language.startFileChangeListener();
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            logger.setUseParentHandlers(false);

            new Thread(Assets::loadAssets).start();
            Defaults.initializeThemeInfo();
            new Thread(Defaults::startMainThread).start();

            System.out.println("> Main Threads Started");


            //If first time launch, the user has to go through onboarding
            //Show it and wait until finished

            if (!Settings.getSettings("onboarding").exists()) {
                Onboarding.createPanel();
                Onboarding.refreshUI();
                Onboarding.frame.setVisible(true);
                Onboarding.isLoading = true;
                while (Onboarding.isLoading) {
                    Thread.sleep(100);
                }
                TwitchAccount.setInfo();
                new Thread(ChannelPointSettings::refresh).start();
            }

            LoadGD.load();
            System.out.println("> LoadGD Loaded");

            new Thread(Variables::loadVars).start();
            System.out.println("> Variables Loaded");

            new Thread(() -> {
                while (keepConnecting) {
                    try {
                        if (chatReader != null) {
                            try {
                                chatReader.disconnect();
                            } catch (WebsocketNotConnectedException ignored) {
                            }
                        }
                        chatReader = new ChatListener(TwitchAccount.login);
                        chatReader.connect(Settings.getSettings("oauth").asString(), TwitchAccount.login);
                        while (!chatReader.isClosed()) {
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
            new Thread(() -> {
                while (keepConnecting) {
                    serverBot = new ServerBot();
                    serverBot.connect();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }).start();


            //If there is no monitor setting, default to 0

            if (!Settings.getSettings("monitor").asString().equalsIgnoreCase("")) {
                Settings.writeSettings("monitor", "0");
            }

            Window.initFrame();
            CommandEditor.createPanel();
            RequestsTab.createPanel();
            //ActionsTab.createPanel();
            ChatbotTab.createPanel();
            SettingsTab.createPanel();
            OfficerWindow.create();
            Window.loadTopComponent();
            LoadCommands.loadCommands();

            System.out.println("> Panels Created");

            //Runs keyboard hook for global keybinds

            new Thread(Main::runKeyboardHook).start();

            //Reads channel point redemptions for channel point triggers

            new Thread(() -> {
                try {
                    while (true) {
                        channelPointListener = new ChannelPointListener(new URI("wss://pubsub-edge.twitch.tv"));
                        channelPointListener.connectBlocking();
                        while (channelPointListener.isOpen()) {
                            Thread.sleep(10);
                        }
                        Thread.sleep(2000);
                    }
                } catch (URISyntaxException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            //Refresh GD Username in Account Settings
            //AccountSettings.refreshGD(LoadGD.username);

            Window.loadSettings();
            Themes.refreshUI();
            starting.setVisible(false);
            Window.setVisible(true);
            System.out.println("> Window Visible");
            Window.setOnTop(Settings.getSettings("onTop").asBoolean());
            OutputSettings.setOutputStringFile(RequestsUtils.parseInfoString(Settings.getSettings("outputString").asString(), 0));

            Path initialJS = Paths.get(Defaults.saveDirectory + "\\GDBoard\\initial.js");

            if (Files.exists(initialJS)) {
                new Thread(() -> {
                    try {
                        if (!Files.readString(initialJS, StandardCharsets.UTF_8).equalsIgnoreCase("")) {
                            Command.run(TwitchAccount.display_name, true, true, new String[]{"dummy"}, Files.readString(initialJS, StandardCharsets.UTF_8), 0, null, -1);
                        }
                    } catch (Exception ignored) {
                    }
                }).start();
            } else {
                Files.createFile(initialJS);
            }

            if(!Settings.getSettings("basicMode").asBoolean()) {
                Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\saved.txt");
                if (Files.exists(file)) {
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
                            RequestsUtils.forceAdd(level[0], //name
                                    level[1], //creator
                                    Long.parseLong(level[2]), //ID
                                    level[3], //difficulty
                                    level[4], //demonDifficulty
                                    Boolean.parseBoolean(level[5]), //isDemon
                                    Boolean.parseBoolean(level[6]), //isAuto
                                    Boolean.parseBoolean(level[7]), //isEpic
                                    Integer.parseInt(level[8]), //featuredScore
                                    Integer.parseInt(level[9]), //stars
                                    Integer.parseInt(level[10]), //requestedStars
                                    level[11], //requester
                                    Integer.parseInt(level[12]), //gameVersion
                                    Integer.parseInt(level[13]), //coinCount
                                    new String(Base64.getDecoder().decode(level[14].getBytes())), //description
                                    Integer.parseInt(level[15]), //likes
                                    Integer.parseInt(level[16]), //downloads
                                    level[17], //length
                                    Integer.parseInt(level[18]), //levelVersion
                                    Long.parseLong(level[19]), //songID
                                    new String(Base64.getDecoder().decode(level[20].getBytes())), //songTitle
                                    level[21], //songArtist
                                    Integer.parseInt(level[22]), //objectCount
                                    Long.parseLong(level[23]), //originalLevelID
                                    Boolean.parseBoolean(level[24]), //containsVulgar
                                    Boolean.parseBoolean(level[25]), //containsImage
                                    Boolean.parseBoolean(level[26]) //coinsVerified
                            );

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    sc.close();
                }
            }
            else {
                RequestsTab.setBasicMode(Settings.getSettings("basicMode").asBoolean(), false);

                Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\savedBasic.txt");
                if (Files.exists(file)) {
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
                            RequestsUtils.forceAddBasic(
                                    Long.parseLong(level[0]), //ID
                                    level[1] //requester
                            );

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    sc.close();
                }
            }
            allowRequests = true;
            RequestFunctions.saveFunction();
            RequestsTab.getLevelsPanel().setSelect(0);
            new Thread(APIs::checkViewers).start();
            if(!Settings.getSettings("basicMode").asBoolean()) {
                try {
                    if(RequestsTab.getQueueSize() != 0) {
                        new Thread(() -> RequestsTab.loadComments(0, false)).start();
                    }
                }
                catch (Exception ignored) {}
            }

            sendMessages = true;

            //if (Settings.getSettings("isHigher").asBoolean()) sendMessage(Utilities.format("🔷 | $STARTUP_MESSAGE_MOD_VIP$"));

            /*Reflections reflections =
                    new Reflections(new ConfigurationBuilder()
                            .filterInputsBy(new FilterBuilder().includePackage("com.alphalaneous"))
                            .setUrls(ClasspathHelper.forPackage("com.alphalaneous"))
                            .setScanners(new SubTypesScanner(false)));

            Set<String> typeList = reflections.getAllTypes();

            for(String str : typeList){
                if(str.equals("com.alphalaneous.Board")){
                    Class.forName(str).getMethod("bwomp").invoke(null);
                }
                System.out.println(Class.forName(str).getName());
            }*/

            APIs.setAllViewers();

            if(!Settings.getSettings("isHigher").exists()) {
                if (APIs.allMods.contains("gdboard") || APIs.allVIPs.contains("gdboard")) Settings.writeSettings("isHigher", "true");
                else Settings.writeSettings("isHigher", "false");
            }
            Board.testSearchPing();
            programLoaded = true;

        } catch (Exception e) {
            e.printStackTrace();
            DialogBox.showDialogBox("Error!", "<html>" + e + ": " + e.getStackTrace()[0], "Please report to Alphalaneous#9687 on Discord.", new String[]{"Close"});
            close(true, false);
        }
    }


    public static ArrayList<Image> getIconImages() {
        return iconImages;
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

        chatReader.sendMessage(message);
    }

    public static void sendToServer(String message) {
        try {
            serverBot.sendMessage(message);
        } catch (Exception ignored) {
        }
    }

    public static void sendMessageWithoutCooldown(String message){
        JSONObject messageObj = new JSONObject();
        messageObj.put("request_type", "send_message");
        messageObj.put("message", message);
        serverBot.sendMessage(messageObj.toString());
    }

    static void sendMessage(String message, boolean whisper, String user) {
        if (!Settings.getSettings("silentMode").asBoolean() || message.equalsIgnoreCase(" ")) {
            if (!message.equalsIgnoreCase("")) {

                JSONObject messageObj = new JSONObject();
                messageObj.put("request_type", "send_message");
                if (Settings.getSettings("antiDox").asBoolean()) {
                    message = message.replaceAll(System.getProperty("user.name"), "*****");
                }
                if (whisper) {
                    messageObj.put("message", "/w " + user + " " + message);
                } else {
                    messageObj.put("message", message);
                }
                try {
                    serverBot.sendMessage(messageObj.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (whisper) {
            if (!message.equalsIgnoreCase("")) {
                JSONObject messageObj = new JSONObject();
                messageObj.put("request_type", "send_message");
                if (Settings.getSettings("antiDox").asBoolean()) {
                    message = message.replaceAll(System.getProperty("user.name"), "*****");
                }
                messageObj.put("message", "/w " + user + " " + message);
                try {
                    serverBot.sendMessage(messageObj.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendMessage(String message) {
        sendMessage(message, false, null);
    }

    private static void runKeyboardHook() {
        var runHookRef = new Object() {
            boolean runHook = true;
        };
        if (keyboardHookThread != null) {
            if (keyboardHookThread.isAlive()) {
                runHookRef.runHook = false;
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
        keyboardHookThread = new Thread(() -> {
            while (runHookRef.runHook) {
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
        keyboardHookThread.start();
    }

    public static void close(boolean forceLoaded, boolean load) {
        boolean loaded = Main.programLoaded;
        if (forceLoaded) {
            loaded = load;
        }
        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Variables.saveVars();
            Settings.saveSettings();
            System.exit(0);
        }).start();
        Utilities.disposeTray();
        if (!Settings.getSettings("onboarding").asBoolean() && loaded) {
            Window.setVisible(false);
            Window.setSettings();
            keepConnecting = false;
            try {
                channelPointListener.disconnectBot();
                chatReader.disconnect();
                serverBot.disconnect();
                GlobalScreen.unregisterNativeHook();
            } catch (Exception ignored) {
            }
            Variables.saveVars();
            Settings.saveSettings();

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
            URL inputUrl = Main.class.getResource("/conf.zip");
            try {
                assert inputUrl != null;
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
