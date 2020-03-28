package Main;

import SettingsPanels.*;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.jnativehook.GlobalScreen;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static ChatBot2 bot;
    static List<User> mods = new ArrayList<>();

    //region Main
    public static void main(String[] args) throws IllegalArgumentException {
        //TODO Use nio everywhere
        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);

            logger.setUseParentHandlers(false);
            try {
                UIManager.setLookAndFeel(new MyLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            Settings.writeSettings("keybind", String.valueOf(KeyEvent.VK_HOME));
            Settings.loadSettings(true);

            //region Get Channel
            if (!Settings.hasChannel) {
                String channel = JOptionPane.showInputDialog("Enter your channel");
                Settings.setChannel(channel);
                Settings.writeSettings("channel", channel);
            }
            if (!Settings.hasWindowed) {
                Settings.writeSettings("windowed", "false");
            }
            if (!Settings.hasMonitor) {
                Settings.writeSettings("monitor", "0");
            }
            //endregion

            //region Get Twitch oauth
            if (!Settings.hasOauth) {                                                    //If the oauth token is not acquired, get one from twitch

                Twitch twitch = new Twitch();                                            //Create TwitchAPI object from TwitchAPI
                URI callbackUri = new URI("http://127.0.0.1:23522/authorize.html"); //Set Callback URL to go to when auth success

                twitch.setClientId("fzwze6vc6d2f7qodgkpq2w8nnsz3rl");                    //Set my app client ID to get oauth
                URI authUrl = new URI(twitch.auth().getAuthenticationUrl(            //Create URI to get oauth token from twitch
                        twitch.getClientId(), callbackUri, Scopes.USER_READ
                ) + "chat:edit+chat:read+whispers:read+whispers:edit&force_verify=true");

                //Desktop.getDesktop().browse(authUrl);                                    //Open in the default browser
                Runtime rt = Runtime.getRuntime();
                rt.exec("rundll32 url.dll,FileProtocolHandler " + authUrl);
                if (twitch.auth().awaitAccessToken()) {                                    //If oauth retrieving succeeds, set oauth in settings
                    Settings.setOAuth(twitch.auth().getAccessToken());
                    Settings.writeSettings("oauth", twitch.auth().getAccessToken());
                } else {                                                                    //Else print error
                    JOptionPane.showMessageDialog(null, "Failed to Authenticate Twitch account", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println(twitch.auth().getAuthenticationError());
                }
            }
            //endregion

            Date date = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("MM.dd");
            if(ft.format(date).equalsIgnoreCase("04.01")) {
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(1800000);
                        BufferedInputStream inp = new BufferedInputStream(Main.class
                                .getResource("../Resources/rick.mp3").openStream());
                        Player mp3player = new Player(inp);
                        mp3player.play();
                    } catch (JavaLayerException | NullPointerException | InterruptedException | IOException f) {
                        JOptionPane.showMessageDialog(null, "There was an error playing the music!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                thread.start();
            }

            //region Start and Create Everything
            Defaults.startMainThread();        //Starts thread that always checks for changes such as time, resolution, and color scheme
            Overlay.setFrame();                //Creates the JFrame that contains everything
            ControllerListener.hook();                    //Starts Controller Listener

            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new KeyListener());

            if (!Settings.windowedMode) {
                MainBar.createBar();            //Creates the main "Game Bar" in the top center
            }
            LevelsWindow.createPanel();            //Creates the Levels Window containing all the requests in the level queue
            ActionsWindow.createPanel();            //Creates the Action Window containing buttons that do specific actions
            InfoWindow.createPanel();            //Creates the Info Window containing the information of the selected level
            CommentsWindow.createPanel();            //Creates the Comment Window containing the comments of the selected level
            SongWindow.createPanel();            //Creates the Song Window allowing you to play the song of the selected level
            SettingsWindow.createPanel();
            if(Settings.windowedMode) {
                Windowed.createPanel();
            }
            InfoWindow.refreshInfo();            //Refreshes the information shown on the Info Window for the first time
            SongWindow.refreshInfo();            //Refreshes the information shown on the Song Window for the first time

            if (Settings.windowedMode) {
                Overlay.refreshUI(true);
            }
            Settings.loadSettings(false);
            GeneralSettings.loadSettings();
            OutputSettings.loadSettings();
            RequestSettings.loadSettings();
            AccountSettings.loadSettings();
            ShortcutSettings.loadSettings();
            Overlay.setVisible();
            SettingsWindow.toFront();
            //endregion
            OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, 0));
            //region Create ChatBot and send starting message

            while (!startBot()) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //endregion

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(Overlay.frame, e, "Error", JOptionPane.ERROR_MESSAGE);

        }
        //endregion
    }

    //endregion
    //region SendMessage to send messages as created ChatBot
    static void sendMessage(String message) {    //Send message as created static ChatBot
        bot.sendMessage(message, Channel.getChannel(Settings.channel, bot));
    }

    public static boolean startBot() {
        try {
            if (bot != null) {
                bot.stop();
            }
            bot = new ChatBot2();
            Thread modCheck = new Thread(() -> {
                while (true) {
                    try {
                        mods = Channel.getChannel(Settings.channel, bot).getMods();
                        for (User mod : mods) {
                            System.out.println(mod);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            modCheck.start();
            bot.connect();
            bot.joinChannel(Settings.channel);
            bot.setClientID("fzwze6vc6d2f7qodgkpq2w8nnsz3rl");
            Thread thread = new Thread(() -> {
                bot.start();        //Start the Chat Bot
            });
            thread.start();
            while (true) {
                if (bot != null) {
                    Main.sendMessage("Thank you for using GDBoard by Alphalaneous! Type !help for list of commands!");
                    break;
                }
            }

            return true;
        } catch (Exception e) {
            bot = null;
            return false;
        }

    }

    public static class MyLookAndFeel extends NimbusLookAndFeel {
        @Override
        public void provideErrorFeedback(Component component) {
        }
    }
    //endregion
}
