package Main;

import SettingsPanels.BlockedSettings;
import SettingsPanels.BlockedUserSettings;
import SettingsPanels.GeneralSettings;
import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import com.cavariux.twitchirc.Core.TwitchBot;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.GDLevel;
import com.github.alex1304.jdash.exception.MissingAccessException;
import com.github.alex1304.jdash.util.LevelSearchFilters;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.apache.commons.lang3.math.NumberUtils;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class ClientChatBot extends TwitchBot {

    Thread thread = null;
    //region ChatBor Constructor
    ClientChatBot() {
        this.setUsername("chatbot");
        this.setOauth_Key("oauth:" + Settings.oauth);
    }

    //endregion
    @Override
    public void onMessage(User user, Channel channel, String message) {
        String command = message.split(" ")[0];
        String[] arguments = message.split(" ");
        if (command.startsWith("!")) {
        //if (messageCommands.contains(command)) {

            doCommand(String.valueOf(user), command, arguments);

        } else {
            Matcher m = Pattern.compile("\\s*(\\d{6,})\\s*").matcher(message);
            if (m.find()) {
                if (GeneralSettings.followersOption) {
                    if (APIs.isNotFollowing(String.valueOf(user)) && !checkMod(String.valueOf(user))) {
                        if (!(("#" + user).equalsIgnoreCase(String.valueOf(Settings.channel)) || Main.mods.contains(user))) {
                            Main.sendMessage("@" + user + " Please follow to request levels!");
                            return;
                        }
                    }
                }
                try {
                    String[] msgs = message.split(" ");
                    String mention = "";
                    for (String s : msgs) {
                        if (s.contains("@")) {
                            mention = s;
                            break;
                        }
                    }
                    if (!mention.contains(m.group(1))) {
                        Requests.addRequest(m.group(1), String.valueOf(user));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onWhisper(User user, String message) {
        /*String command = message.split(" ")[0];
        String[] arguments = message.split(" ");
        if (whisperCommands.contains(command)) {
            doCommand(String.valueOf(user), command, arguments);
        } else if (whisperRequests) {
            Matcher m = Pattern.compile("\\s*(\\d{6,})\\s*").matcher(message);
            if (m.find()) {
                if (GeneralSettings.followersOption) {
                    if (APIs.isNotFollowing(user)) {
                        if (!(("#" + user).equalsIgnoreCase(String.valueOf(Settings.channel)) || Main.mods.contains(user))) {
                            Main.sendMessage("@" + user + " Please follow to request levels!");
                            return;
                        }
                    }
                }
                try {
                    String[] msgs = message.split(" ");
                    String mention = "";
                    for (String s : msgs) {
                        if (s.contains("@")) {
                            mention = s;
                            break;
                        }
                    }
                    if (!mention.contains(m.group(1))) {
                        Requests.addRequest(m.group(1), String.valueOf(user), false, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    public boolean checkMod(String user) {
        return Main.mods.contains(user) || ("#" + user).equalsIgnoreCase(String.valueOf(Settings.channel));
    }

    public void doCommand(String user, String command, String[] arguments) {
        String response = null;
        boolean isMod = checkMod(user);
        /*if (!isMod) {
            if (!userCommands.contains(command)) {
                return;
            }
        }
        if (disabledCommands.contains(command)) {
            return;
        }*/

        int intArg = 1;
        if (arguments.length > 1 && NumberUtils.isParsable(arguments[1])) {
            intArg = Integer.parseInt(arguments[1]);
            if (intArg <= 0 || intArg > Requests.levels.size() + 1) {
                intArg = 1;
            }

        }

        //region ID/Name/Level CommandSettings
        if (command.equalsIgnoreCase("!ID") ||
                command.equalsIgnoreCase("!name") ||
                command.equalsIgnoreCase("!level")) {
            if (Requests.levels.size() > 0 && intArg <= Requests.levels.size()) {
                response = "@" + user + " The level at position " +
                        intArg + " is " + Requests.levels.get(intArg - 1).getName() +
                        " by " + Requests.levels.get(intArg - 1).getAuthor() + " (" +
                        Requests.levels.get(intArg - 1).getLevelID() + ") Requested by " + Requests.levels.get(intArg - 1).getRequester();
            }
        }
        //endregion

        //region Current Command
        if (command.equalsIgnoreCase("!current")) {
            if (Requests.levels.size() > 0 && intArg <= Requests.levels.size()) {
                response = "@" + user + " The current level is " +
                        Requests.levels.get(0).getName() + " by " +
                        Requests.levels.get(0).getAuthor() + " (" +
                        Requests.levels.get(0).getLevelID() + ") Requested by " + Requests.levels.get(0).getRequester();
            }
        }
        //endregion

        //region Song Command
        if (command.equalsIgnoreCase("!song")) {
            if (Requests.levels.size() > 0 && intArg <= Requests.levels.size()) {
                response = "@" + user + " The song is " +
                        Requests.levels.get(intArg - 1).getSongName() + " by " +
                        Requests.levels.get(intArg - 1).getSongAuthor() +
                        " (" + Requests.levels.get(intArg - 1).getSongID() + ")";
            }
        }
        //endregion

        //region Likes Command
        if (command.equalsIgnoreCase("!likes")) {
            if (Requests.levels.size() > 0 && intArg <= Requests.levels.size()) {
                response = "@" + user + " There are " +
                        Requests.levels.get(intArg - 1).getLikes() + " likes!";
            }
        }
        //endregion

        //region Downloads Command
        if (command.equalsIgnoreCase("!downloads")) {
            if (Requests.levels.size() > 0 && intArg <= Requests.levels.size()) {
                response = "@" + user + " There are " +
                        Requests.levels.get(intArg - 1).getDownloads() + " downloads!";
            }
        }
        //endregion

        //region Difficulty Command
        if (command.equalsIgnoreCase("!difficulty")) {
            if (Requests.levels.size() > 0 && intArg <= Requests.levels.size()) {
                response = "@" + user + " The difficulty is " +
                        Requests.levels.get(intArg - 1).getDifficulty().toLowerCase();
            }
        }
        //endregion

        //region Remove Command
        if (command.equalsIgnoreCase("!remove")) {

            for (int i = 0; i < Requests.levels.size(); i++) {
                try {
                    if (Requests.levels.get(i).getLevelID().equals(Requests.levels.get(intArg - 1).getLevelID())
                            && String.valueOf(user).equalsIgnoreCase(Requests.levels.get(i).getRequester())) {
                        LevelsWindow.removeButton(i);
                        Requests.levels.remove(i);
                        SongWindow.refreshInfo();
                        InfoWindow.refreshInfo();
                        Thread thread = new Thread(() -> {
                            CommentsWindow.unloadComments(true);
                            CommentsWindow.loadComments(0, false);
                        });
                        LevelsWindow.setOneSelect();
                        thread.start();
                        response = "@" + user + ", your level has been removed!";
                    }
                    if (Requests.levels.get(i).getLevelID().equals(Requests.levels.get(intArg - 1).getLevelID())
                            && isMod) {
                        LevelsWindow.removeButton(i);
                        Requests.levels.remove(i);
                        SongWindow.refreshInfo();
                        InfoWindow.refreshInfo();
                        Thread thread = new Thread(() -> {
                            CommentsWindow.unloadComments(true);
                            CommentsWindow.loadComments(0, false);
                        });
                        LevelsWindow.setOneSelect();
                        thread.start();
                        response = "@" + user + ", your level has been removed!";
                    }
                } catch (Exception ignored) {
                }
            }
        }
        //endregion

        //region Clear Command
        if (command.equalsIgnoreCase("!clear") && isMod) {

            for (int i = 0; i < Requests.levels.size(); i++) {
                LevelsWindow.removeButton();
            }
            Requests.levels.clear();
            SongWindow.refreshInfo();
            InfoWindow.refreshInfo();
            CommentsWindow.unloadComments(true);
            response = "@" + user + " Successfully cleared the queue!";
        }
        //endregion

        //region Queue Command
        if (command.equalsIgnoreCase("!q") ||
                command.equalsIgnoreCase("!queue") ||
                command.equalsIgnoreCase("!levelList") ||
                command.equalsIgnoreCase("!list") ||
                command.equalsIgnoreCase("!requests") ||
                command.equalsIgnoreCase("!page")) {
            //if (Main.mods.contains(user) || isBroadcaster) {

            StringBuilder message = new StringBuilder();
            int page = 1;
            try {
                page = Integer.parseInt(arguments[1]);
            } catch (Exception ignored) {
            }
            int pages = (Requests.levels.size() - 1) / 10;
            message.append("Page ").append(page).append(" of ").append(pages + 1).append(" of the queue | ");
            for (int i = (page - 1) * 10; i < page * 10; i++) {
                if (i == Requests.levels.size() - 1 && message.length() >= 2) {
                    message.append(i + 1).append(": ").append(Requests.levels.get(i).getName()).append(" (").append(Requests.levels.get(i).getLevelID()).append("), ");
                    message.delete(message.length() - 2, message.length());
                    break;
                }

                try {
                    message.append(i + 1).append(": ").append(Requests.levels.get(i).getName()).append(" (").append(Requests.levels.get(i).getLevelID()).append("), ");
                } catch (IndexOutOfBoundsException e) {
                    message.delete(0, message.length());
                    response = "@" + user + " No levels on page " + page;
                    break;
                }
            }
            response = message.toString();
            //} else {
            //sendMessage("This command is for mods, use !where or !position to find your position in the queue!", channel);
            //}
        }
        //endregion

        //region Position/Where Command
        if (command.equalsIgnoreCase("!p") ||
                command.equalsIgnoreCase("!where") ||
                command.equalsIgnoreCase("!position")) {

            ArrayList<LevelData> userLevels = new ArrayList<>();
            ArrayList<Integer> userPosition = new ArrayList<>();

            for (int i = 0; i < Requests.levels.size(); i++) {
                int j = i + 1;
                if (Requests.levels.get(i).getRequester().equalsIgnoreCase(String.valueOf(user))) {
                    userLevels.add(Requests.levels.get(i));
                    userPosition.add(j);
                }
            }
            String ordinal;
            String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
            switch (intArg % 100) {
                case 11:
                case 12:
                case 13:
                default:
                    ordinal = (intArg) + suffixes[(intArg) % 10];
            }
            try {
                response = "@" + user + ", " + userLevels.get(intArg - 1).getName()
                        + " is at position " + userPosition.get(intArg - 1) + " in the queue!";
            } catch (IndexOutOfBoundsException e) {
                if (userLevels.size() == 0) {
                    response = "@" + user + " You don't have any levels in the queue!";
                } else {
                    response = "@" + user + " You don't have a " + ordinal + " level in the queue!";
                }
            }
        }
        //endregion

        if (command.equalsIgnoreCase("!rick") && user.equalsIgnoreCase("alphalaneous")) {
            if (thread != null) {
                thread.stop();
            }
            thread = new Thread(() -> {
                try {
                    BufferedInputStream inp = new BufferedInputStream(ServerChatBot.class
                            .getResource("/Resources/rick.mp3").openStream());
                    Player mp3player = new Player(inp);
                    mp3player.play();
                } catch (JavaLayerException | NullPointerException | IOException f) {
                    f.printStackTrace();
                    JOptionPane.showMessageDialog(null, "There was an error playing the music!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            thread.start();

        }
        if (command.equalsIgnoreCase("!stoprick") && user.equalsIgnoreCase("alphalaneous")) {
            if (thread != null && thread.isAlive()) {
                thread.stop();
            }
        }
        //region Help Command
        if (command.equalsIgnoreCase("!help")) {
            if (arguments.length == 1) {
                response = "@" + user + " List of Commands | Type !help <command> for more help. | !request | !position | !ID | !difficulty | !song | !likes | !downloads | !remove | !queue | !block | !blockuser";
            } else if (arguments[1].equalsIgnoreCase("request")) {
                response = "@" + user + " Used to send requests | Usage: \"!request <Level ID>\" to send via level ID | \"!request <Level Name>\" to send via level name | \"!request <Level Name> by <User>\" to send via level name by a user.";
            } else if (arguments[1].equalsIgnoreCase("position")) {
                response = "@" + user + " Used to find your position in the queue | Usage: \"!position\" to get closest in the queue | \"!position <Number>\" to get a specific position";
            } else if (arguments[1].equalsIgnoreCase("ID")) {
                response = "@" + user + " Used to find the current level's ID | Usage: \"!ID\"";
            } else if (arguments[1].equalsIgnoreCase("difficulty")) {
                response = "@" + user + " Used to find the current level's difficulty Usage: \"!difficulty\"";
            } else if (arguments[1].equalsIgnoreCase("song")) {
                response = "@" + user + " Used to find the current level's song information | Usage: \"!song\"";
            } else if (arguments[1].equalsIgnoreCase("likes")) {
                response = "@" + user + " Used to find the current level's like count | Usage: \"!likes\"";
            } else if (arguments[1].equalsIgnoreCase("downloads")) {
                response = "@" + user + " Used to find the current level's download count | Usage: \"!count\"";
            } else if (arguments[1].equalsIgnoreCase("remove")) {
                response = "@" + user + " Used to remove a level from the queue | Usage: \"!remove <Position>\"";
            } else if (arguments[1].equalsIgnoreCase("block")) {
                response = "@" + user + " Used to block a level ID | Usage: \"!block <Level ID>\" to block a specific ID";
            } else if (arguments[1].equalsIgnoreCase("blockuser")) {
                response = "@" + user + " Used to block a user | Usage: \"!blockuser\" to block the current user | \"!blockuser <Username>\" to block a specific user";
            }

        }
        //endregion
        //region Unblock Command
        if (command.equalsIgnoreCase("!unblock") && isMod) {
            String unblocked = arguments[1];

            try {
                boolean exists = false;
                Path file = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\blocked.txt");
                if (Files.exists(file)) {
                    Scanner sc = new Scanner(file);
                    while (sc.hasNextLine()) {
                        if (String.valueOf(unblocked).equals(sc.nextLine())) {
                            exists = true;
                            break;
                        }
                    }
                    sc.close();

                    if (exists) {
                        Path temp = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\_temp_");
                        PrintWriter out = new PrintWriter(new FileWriter(temp.toFile()));
                        Files.lines(file)
                                .filter(line -> !line.contains(unblocked))
                                .forEach(out::println);
                        out.flush();
                        out.close();
                        Files.delete(file);
                        Files.move(temp, temp.resolveSibling(System.getenv("APPDATA") + "\\GDBoard\\blocked.txt"), StandardCopyOption.REPLACE_EXISTING);
                        BlockedSettings.removeID(arguments[1]);
                        response = "@" + user + " Successfully unblocked " + arguments[1];
                        BlockedSettings.removeID(unblocked);
                    } else {
                        response = "@" + user + " That level isn't blocked!";
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                response = "@" + user + " unblock failed!";
            }
        }
        //endregion

        //region Block Command
        if (command.equalsIgnoreCase("!block") && isMod) {
            try {
                int blockedID = Integer.parseInt(arguments[1]);
                boolean goThrough = true;
                Path file = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\blocked.txt");
                if (!Files.exists(file)) {
                    Files.createFile(file);
                }
                Scanner sc = new Scanner(file.toFile());
                while (sc.hasNextLine()) {
                    if (String.valueOf(blockedID).equals(sc.nextLine())) {
                        response = "@" + user + " ID Already Blocked!";
                        sc.close();
                        return;
                    }
                }
                sc.close();

                try {
                    Files.write(file, (blockedID + "\n").getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                response = "@" + user + " Successfully blocked " + arguments[1];
                BlockedSettings.addButton(arguments[1]);
            } catch (Exception e) {
                e.printStackTrace();
                response = "@" + user + " Block failed!";
            }
        }
        //endregion

        if (command.equalsIgnoreCase("!blockUser") && isMod) {
            try {
                String blockedUser = arguments[1];
                boolean goThrough = true;
                Path file = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\blockedUsers.txt");
                if (!Files.exists(file)) {
                    Files.createFile(file);
                }
                Scanner sc = new Scanner(file.toFile());
                while (sc.hasNextLine()) {
                    if (String.valueOf(blockedUser).equals(sc.nextLine())) {
                        response = "@" + user + " User Already Blocked!";
                        sc.close();
                        return;
                    }
                }
                sc.close();
                try {
                    Files.write(file, (blockedUser + "\n").getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                response = "@" + user + " Successfully blocked " + arguments[1];
                BlockedUserSettings.addButton(arguments[1]);

            } catch (Exception e) {
                e.printStackTrace();
                response = "@" + user + " Block failed!";
            }
        }

        if (command.equalsIgnoreCase("!unblockUser") && isMod) {
            String unblocked = arguments[1];

            try {
                boolean exists = false;
                Path file = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\blockedUsers.txt");
                if (Files.exists(file)) {
                    Scanner sc = new Scanner(file);
                    while (sc.hasNextLine()) {
                        if (String.valueOf(unblocked).equals(sc.nextLine())) {
                            exists = true;
                            break;
                        }
                    }
                    sc.close();
                    if (exists) {
                        Path temp = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\_temp_");
                        PrintWriter out = new PrintWriter(new FileWriter(temp.toFile()));
                        Files.lines(file)
                                .filter(line -> !line.contains(unblocked))
                                .forEach(out::println);
                        out.flush();
                        out.close();
                        Files.delete(file);
                        Files.move(temp, temp.resolveSibling(System.getenv("APPDATA") + "\\GDBoard\\blockedUsers.txt"), StandardCopyOption.REPLACE_EXISTING);
                        BlockedSettings.removeID(arguments[1]);
                        response = "@" + user + " Successfully unblocked " + arguments[1];
                        BlockedUserSettings.removeUser(unblocked);
                    } else {
                        response = "@" + user + " That user isn't blocked!";
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                response = "@" + user + " unblock failed!";
            }
        }

        //region Request Command
        if (command.equalsIgnoreCase("!r") ||
                command.equalsIgnoreCase("!request") ||
                command.equalsIgnoreCase("!req") ||
                command.equalsIgnoreCase("!add") ||
                command.equalsIgnoreCase("!send")) {
            Matcher m = null;
            if (GeneralSettings.followersOption) {
                if (APIs.isNotFollowing(user)) {
                    if (!isMod) {
                        response = "@" + user + " Please follow to request levels!";
                        return;
                    }
                }
            }
            if (arguments.length > 1) {
                try {
                    m = Pattern.compile("(\\d+)").matcher(arguments[1]);
                } catch (Exception ignored) {
                }
                assert m != null;
                if (m.matches() && arguments.length <= 2) {
                    try {
                        Requests.addRequest(m.group(1), String.valueOf(user));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    StringBuilder message = new StringBuilder();
                    for (int i = 1; i < arguments.length; i++) {
                        if (arguments.length - 1 == i) {
                            message.append(arguments[i]);
                        } else {
                            message.append(arguments[i]).append(" ");
                        }
                    }
                    if (message.toString().contains(" by ")) {
                        String level1 = message.toString().split("by ")[0].toUpperCase();
                        String username = message.toString().split("by ")[1];
                        AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
                        try {
                            outerLoop:
                            for (int j = 0; j < 10; j++) {
                                Object[] levelPage = Objects.requireNonNull(client.getLevelsByUser(Objects.requireNonNull(client.searchUser(username).block()), j)
                                        .block()).asList().toArray();
                                for (int i = 0; i < 10; i++) {
                                    if (((GDLevel) levelPage[i]).getName().toUpperCase()
                                            .startsWith(level1.substring(0, level1.length() - 1))) {
                                        Requests.addRequest(String.valueOf(((GDLevel) levelPage[i]).getId()),
                                                String.valueOf(user));
                                        break outerLoop;
                                    }
                                }
                            }

                        } catch (IndexOutOfBoundsException ignored) {
                        } catch (MissingAccessException e) {
                            response = "@" + user + " That level or user doesn't exist!";
                            e.printStackTrace();
                        }
                    } else {

                        AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
                        try {
                            Requests.addRequest(String
                                            .valueOf(Objects.requireNonNull(client.searchLevels(message.toString(), LevelSearchFilters.create(), 0)
                                                    .block()).asList().get(0).getId()),
                                    String.valueOf(user));
                        } catch (MissingAccessException e) {
                            response = "@" + user + " That level doesn't exist!";
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                response = "@" + user + " Please specify an ID!!";
            }
        }
        if (response != null) {
            /*if(whisperCommand.contains(command)){
                Main.sendMessage("/w " + user + " " + response);
            }
            else {*/
            Main.sendMessage(response);
            //}
        }
    }
}