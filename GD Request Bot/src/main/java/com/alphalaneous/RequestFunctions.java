package com.alphalaneous;

import com.alphalaneous.Panels.*;
import com.alphalaneous.SettingsPanels.BlockedIDSettings;
import com.alphalaneous.SettingsPanels.OutputSettings;
import com.alphalaneous.Windows.DialogBox;
import com.alphalaneous.Tabs.RequestsTab;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class RequestFunctions {

    //todo make spamming the next button only show the last request in chat rather than the first. (Check if spamming, wait until stop and send current selected level)

    private static final LinkedHashMap<LevelButton, Integer> undoQueue = new LinkedHashMap<>(5) {
        protected boolean removeEldestEntry(Map.Entry<LevelButton, Integer> eldest) {
            return size() > 5;
        }
    };
    private static boolean didUndo = false;

    private static boolean onCool = false;

    public static void openGDBrowser(int pos) {
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://www.gdbrowser.com/" + RequestsTab.getRequest(pos).getLevelData().getGDLevel().id());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void skipFunction() {
        skipFunction(LevelButton.selectedID);
    }


    public static void skipFunction(int pos) {
        if (RequestsUtils.bwomp) {
            new Thread(() -> {
                try {
                    BufferedInputStream inp = new BufferedInputStream(Objects.requireNonNull(BotHandler.class
                            .getResource("bwomp.mp3")).openStream());
                    Player mp3player = new Player(inp);
                    mp3player.play();
                } catch (JavaLayerException | NullPointerException | IOException f) {
                    f.printStackTrace();
                    DialogBox.showDialogBox("Error!", f.toString(), "There was an error playing the sound!", new String[]{"OK"});
                }
            }).start();
        }
        if (Main.programLoaded) {
            if (RequestsTab.getQueueSize() != 0) {
                if (didUndo) {
                    undoQueue.clear();
                    didUndo = false;
                }
                if (!Settings.getSettings("basicMode").asBoolean()) {
                    undoQueue.put(RequestsTab.getRequest(pos), pos);
                }
                RequestsTab.removeRequest(pos);
                RequestsTab.setRequestSelect(pos);


                if (RequestsTab.getQueueSize() > 0) {
                    if (!Settings.getSettings("basicMode").asBoolean()) {
                        StringSelection selection = new StringSelection(
                                String.valueOf(RequestsTab.getRequest(0).getLevelData().getGDLevel().id()));
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(selection, selection);
                    } else {
                        StringSelection selection = new StringSelection(
                                String.valueOf(RequestsTab.getRequestBasic(0).getID()));
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(selection, selection);
                    }
                }
                if (pos == 0 && RequestsTab.getQueueSize() > 0) {
                    if (!Settings.getSettings("disableNP").asBoolean()) {
                        new Thread(() -> {
                            if (!Settings.getSettings("basicMode").asBoolean()) {
                                if (RequestsTab.getRequest(0).getLevelData().getContainsImage()) {
                                    Main.sendMessage(Utilities.format("🎮 | $NOW_PLAYING_MESSAGE$",
                                            RequestsTab.getRequest(0).getLevelData().getGDLevel().name(),
                                            RequestsTab.getRequest(0).getLevelData().getGDLevel().id(),
                                            RequestsTab.getRequest(0).getLevelData().getRequester()) + " " + Utilities.format("$IMAGE_HACK$"));
                                } else if (RequestsTab.getRequest(0).getLevelData().getContainsVulgar()) {
                                    Main.sendMessage(Utilities.format("🎮 | $NOW_PLAYING_MESSAGE$",
                                            RequestsTab.getRequest(0).getLevelData().getGDLevel().name(),
                                            RequestsTab.getRequest(0).getLevelData().getGDLevel().id(),
                                            RequestsTab.getRequest(0).getLevelData().getRequester()) + " " + Utilities.format("$VULGAR_LANGUAGE$"));
                                } else {
                                    Main.sendMessage(Utilities.format("🎮 | $NOW_PLAYING_MESSAGE$",
                                            RequestsTab.getRequest(0).getLevelData().getGDLevel().name(),
                                            RequestsTab.getRequest(0).getLevelData().getGDLevel().id(),
                                            RequestsTab.getRequest(0).getLevelData().getRequester()));
                                }
                            } else {
                                Main.sendMessage(Utilities.format("🎮 | $NOW_PLAYING_MESSAGE_BASIC$",
                                        RequestsTab.getRequestBasic(0).getID(),
                                        RequestsTab.getRequestBasic(0).getRequester()));

                            }
                        }).start();
                    }
                }
            }
        }
        if (!Settings.getSettings("basicMode").asBoolean()) {
            OutputSettings.setOutputStringFile(RequestsUtils.parseInfoString(Settings.getSettings("outputString").asString(), 0));
        }
        RequestsTab.unloadComments(true);
        if (RequestsTab.getQueueSize() != 0) {
            if (!Settings.getSettings("basicMode").asBoolean()) {
                if (RequestsTab.getQueueSize() != 0) {
                    new Thread(() -> RequestsTab.loadComments(0, false)).start();
                }
            }
        }
        RequestsTab.getLevelsPanel().setWindowName(RequestsTab.getQueueSize());
        RequestsTab.refreshInfoPanel();
        RequestFunctions.saveFunction();
    }

    static void containsBadStuffCheck() {
        if (RequestsTab.getRequest(0).getLevelData().getContainsImage()) {
            Utilities.notify("Image Hack", RequestsTab.getRequest(0).getLevelData().getGDLevel().name() + " (" + RequestsTab.getRequest(0).getLevelData().getGDLevel().id() + ") possibly contains the image hack!");
        } else if (RequestsTab.getRequest(0).getLevelData().getContainsVulgar()) {
            Utilities.notify("Vulgar Language", RequestsTab.getRequest(0).getLevelData().getGDLevel().name() + " (" + RequestsTab.getRequest(0).getLevelData().getGDLevel().id() + ") contains vulgar language!");
        }
    }

    public static void undoFunction() {
        if (undoQueue.size() != 0) {
            didUndo = true;
            int selectPosition = LevelButton.selectedID;
            LevelButton levelButton = (LevelButton) undoQueue.keySet().toArray()[undoQueue.size() - 1];
            int position = (int) undoQueue.values().toArray()[undoQueue.size() - 1];
            if (position >= RequestsTab.getQueueSize()) {
                position = RequestsTab.getQueueSize();
            }
            RequestsTab.addRequest(levelButton, position);
            //Requests.levels.add(position, data);
            //com.alphalaneous.Tabs.Window.getLevelsPanel().refreshButtons();
            if (RequestsTab.getLevelPosition(levelButton) > selectPosition) {
                RequestsTab.getLevelsPanel().setSelect(selectPosition);
            } else if (RequestsTab.getQueueSize() == 1) {
                RequestsTab.getLevelsPanel().setSelect(selectPosition);
                RequestsTab.refreshInfoPanel();
                new Thread(() -> {
                    RequestsTab.unloadComments(true);
                    RequestsTab.loadComments(0, false);
                }).start();
            } else {
                RequestsTab.getLevelsPanel().setSelect(selectPosition + 1);
                RequestsTab.refreshInfoPanel();
            }
            undoQueue.remove(levelButton);
        }
        RequestFunctions.saveFunction();
    }

    public static void randomFunction() {
        if (Main.programLoaded) {
            Random random = new Random();
            int num = 0;
            if (RequestsTab.getQueueSize() != 0) {
                if (didUndo) {
                    undoQueue.clear();
                    didUndo = false;
                }
                if (!Settings.getSettings("basicMode").asBoolean()) {
                    undoQueue.put(RequestsTab.getRequest(LevelButton.selectedID), LevelButton.selectedID);
                }
                RequestsTab.removeRequest(LevelButton.selectedID);

                RequestFunctions.saveFunction();

                RequestsTab.unloadComments(true);

                if (RequestsTab.getQueueSize() != 0) {
                    while (true) {
                        try {
                            num = random.nextInt(RequestsTab.getQueueSize());
                            break;
                        } catch (Exception ignored) {
                        }
                    }

                    RequestsTab.getLevelsPanel().setSelect(num);
                    if (!Settings.getSettings("basicMode").asBoolean()) {
                        new Thread(() -> RequestsTab.loadComments(0, false)).start();
                        StringSelection selection = new StringSelection(
                                String.valueOf(RequestsTab.getRequest(num).getLevelData().getGDLevel().id()));
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(selection, selection);
                        if (!Settings.getSettings("disableNP").asBoolean()) {
                            int finalNum = num;
                            new Thread(() -> Main.sendMessage(Utilities.format("🎮 | $NOW_PLAYING_MESSAGE$",
                                    RequestsTab.getRequest(finalNum).getLevelData().getGDLevel().name(),
                                    RequestsTab.getRequest(finalNum).getLevelData().getGDLevel().id(),
                                    RequestsTab.getRequest(finalNum).getLevelData().getRequester()))).start();

                        }
                        if (RequestsTab.getRequest(num).getLevelData().getContainsImage()) {
                            Utilities.notify("Image Hack", RequestsTab.getRequest(num).getLevelData().getGDLevel().name() + " (" + RequestsTab.getRequest(num).getLevelData().getGDLevel().id() + ") possibly contains the image hack!");
                        } else if (RequestsTab.getRequest(num).getLevelData().getContainsVulgar()) {
                            Utilities.notify("Vulgar Language", RequestsTab.getRequest(num).getLevelData().getGDLevel().name() + " (" + RequestsTab.getRequest(num).getLevelData().getGDLevel().id() + ") contains vulgar language!");
                        }
                        OutputSettings.setOutputStringFile(RequestsUtils.parseInfoString(Settings.getSettings("outputString").asString(), num));
                        RequestsTab.refreshInfoPanel();
                    } else {
                        StringSelection selection = new StringSelection(
                                String.valueOf(RequestsTab.getRequestBasic(num).getID()));
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(selection, selection);
                        if (!Settings.getSettings("disableNP").asBoolean()) {
                            int finalNum = num;
                            new Thread(() -> Main.sendMessage(Utilities.format("🎮 | $NOW_PLAYING_MESSAGE_BASIC$",
                                    RequestsTab.getRequestBasic(finalNum).getID(),
                                    RequestsTab.getRequestBasic(finalNum).getRequester()))).start();

                        }
                    }
                }
            }
            RequestFunctions.saveFunction();
            RequestsTab.getLevelsPanel().setWindowName(RequestsTab.getQueueSize());
        }
    }

    public static void copyFunction() {
        copyFunction(LevelButton.selectedID);
    }

    public static void copyFunction(int pos) {
        if (RequestsTab.getQueueSize() != 0) {
            StringSelection selection = new StringSelection(
                    String.valueOf(RequestsTab.getRequest(pos).getLevelData().getGDLevel().id()));
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }

    public static void saveFunction() {
        //public static void forceAdd(String name, String author, long levelID, String difficulty, boolean epic, boolean featured, int stars, String requester,
        // int gameVersion, int coins, String description, int likes, int downloads, String length, int levelVersion, int songID, String songName, String songAuthor, int objects, long original){
        if (!Settings.getSettings("basicMode").asBoolean()) {
            try {
                Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\saved.txt");
                if (!Files.exists(file)) {
                    Files.createFile(file);
                }
                FileWriter fileWriter = new FileWriter(file.toFile(), false);
                StringBuilder message = new StringBuilder();


                for (int i = 0; i < RequestsUtils.getSize(); i++) {

                    String creatorName = "Unknown";
                    String songTitle = "Unknown";
                    String songArtist = "Unknown";
                    long originalID = 0;
                    long songID = 0;


                    if (RequestsTab.getRequest(i).getLevelData().getGDLevel().creatorName().isPresent()) {
                        creatorName = RequestsTab.getRequest(i).getLevelData().getGDLevel().creatorName().get();
                    }
                    if (RequestsTab.getRequest(i).getLevelData().getGDLevel().song().isPresent()) {
                        songTitle = RequestsTab.getRequest(i).getLevelData().getGDLevel().song().get().title();
                        songArtist = RequestsTab.getRequest(i).getLevelData().getGDLevel().song().get().artist();
                        songID = RequestsTab.getRequest(i).getLevelData().getGDLevel().song().get().id();
                    }
                    if (RequestsTab.getRequest(i).getLevelData().getGDLevel().originalLevelId().isPresent()) {
                        originalID = RequestsTab.getRequest(i).getLevelData().getGDLevel().originalLevelId().get();
                    }
                    message.append(RequestsTab.getRequest(i).getLevelData().getGDLevel().name())
                            .append(",").append(creatorName)
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().id())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().difficulty())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().demonDifficulty())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().isDemon())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().isAuto())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().isEpic())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().featuredScore())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().stars())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().requestedStars())
                            .append(",").append(RequestsTab.getRequest(i).getRequester())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().gameVersion())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().coinCount())
                            .append(",").append(new String(Base64.getEncoder().encode(RequestsTab.getRequest(i).getLevelData().getGDLevel().description().getBytes())))
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().likes())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().downloads())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().length().toString())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().levelVersion())
                            .append(",").append(songID)
                            .append(",").append(new String(Base64.getEncoder().encode(songTitle.getBytes())))
                            .append(",").append(songArtist)
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().objectCount())
                            .append(",").append(originalID)
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getContainsVulgar())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getContainsImage())
                            .append(",").append(RequestsTab.getRequest(i).getLevelData().getGDLevel().hasCoinsVerified())
                            .append("\n");
                }
                fileWriter.write(message.toString());
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\savedBasic.txt");
                if (!Files.exists(file)) {
                    Files.createFile(file);
                }
                FileWriter fileWriter = new FileWriter(file.toFile(), false);
                StringBuilder message = new StringBuilder();

                for (int i = 0; i < RequestsUtils.getSize(); i++) {
                    message.append(RequestsTab.getRequestBasic(i).getID())
                            .append(",").append(RequestsTab.getRequestBasic(i).getRequester())
                            .append("\n");
                }
                fileWriter.write(message.toString());
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void blockFunction() {
        blockFunction(LevelButton.selectedID);
    }


    public static void blockFunction(int pos) {
        if (Main.programLoaded) {
            if (pos == 0 && RequestsTab.getQueueSize() > 1) {
                StringSelection selection = new StringSelection(
                        String.valueOf(RequestsTab.getRequest(1).getLevelData().getGDLevel().id()));
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }
            if (RequestsTab.getQueueSize() != 0) {

                new Thread(() -> {
                    String option;
                    if (!Settings.getSettings("basicMode").asBoolean()) {
                        option = DialogBox.showDialogBox("$BLOCK_ID_TITLE$", "$BLOCK_ID_INFO$", "$BLOCK_ID_SUBINFO$", new String[]{"$YES$", "$NO$"}, new Object[]{RequestsTab.getRequest(pos).getLevelData().getGDLevel().name(), RequestsTab.getRequest(pos).getLevelData().getGDLevel().id()});
                    } else {
                        option = DialogBox.showDialogBox("$BLOCK_ID_TITLE$", "$BLOCK_ID_INFO$", "$BLOCK_ID_SUBINFO$", new String[]{"$YES$", "$NO$"}, new Object[]{"Unknown", RequestsTab.getRequestBasic(pos).getID()});

                    }
                    if (option.equalsIgnoreCase("YES")) {
                        BlockedIDSettings.addBlockedLevel(String.valueOf(RequestsTab.getRequest(pos).getLevelData().getGDLevel().id()));
                        Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blocked.txt");

                        try {
                            if (!Files.exists(file)) {
                                Files.createFile(file);
                            }
                            Files.write(
                                    file,
                                    (RequestsTab.getRequest(pos).getLevelData().getGDLevel().id() + "\n").getBytes(),
                                    StandardOpenOption.APPEND);
                        } catch (IOException e1) {
                            DialogBox.showDialogBox("Error!", e1.toString(), "There was an error writing to the file!", new String[]{"OK"});

                        }
                        RequestsTab.removeRequest(pos);
                        RequestFunctions.saveFunction();
                        RequestsTab.getLevelsPanel().setSelect(0);
                        new Thread(() -> {
                            RequestsTab.unloadComments(true);
                            if (RequestsTab.getQueueSize() > 0) {
                                RequestsTab.loadComments(0, false);
                            }
                        }).start();
                        RequestsTab.getLevelsPanel().setWindowName(RequestsTab.getQueueSize());

                    }
                    RequestsTab.refreshInfoPanel();
                }).start();
            }
        }
    }

    public static void clearFunction() {
        clearFunction(false);
    }

    public static void clearFunction(boolean basicSetting) {
        if (Main.programLoaded) {
            if (basicSetting) {

                if (RequestsTab.getQueueSize() != 0) {
                    RequestsTab.clearRequests();
                    undoQueue.clear();
                    RequestsTab.refreshInfoPanel();
                    RequestsTab.unloadComments(true);
                }
                RequestsTab.getLevelsPanel().setSelect(0);

            } else {
                new Thread(() -> {
                    String option = DialogBox.showDialogBox("$CLEAR_QUEUE_TITLE$", "$CLEAR_QUEUE_INFO$", "$CLEAR_QUEUE_SUBINFO$", new String[]{"$CLEAR_ALL$", "$CANCEL$"});
                    if (option.equalsIgnoreCase("CLEAR_ALL")) {
                        if (RequestsTab.getQueueSize() != 0) {
                            RequestsTab.clearRequests();
                            undoQueue.clear();
                            RequestFunctions.saveFunction();
                            RequestsTab.refreshInfoPanel();
                            RequestsTab.unloadComments(true);
                        }
                        RequestsTab.getLevelsPanel().setSelect(0);
                    }
                }).start();
            }
        }
    }

    public static void requestsToggleFunction() {
        if (Main.programLoaded) {
            if (Requests.requestsEnabled) {
                Requests.requestsEnabled = false;
                Main.sendMessage(Utilities.format("/me 🟥 | $REQUESTS_OFF_TOGGLE_MESSAGE$"));

            } else {
                Requests.requestsEnabled = true;
                Main.sendMessage(Utilities.format("/me 🟩 | $REQUESTS_ON_TOGGLE_MESSAGE$"));
            }
        }
    }
}
