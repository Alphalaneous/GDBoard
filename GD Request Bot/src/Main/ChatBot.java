package Main;

import Chat.Channel;
import Chat.User;
import Core.TwitchBot;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.GDLevel;
import com.github.alex1304.jdash.exception.MissingAccessException;
import com.github.alex1304.jdash.util.LevelSearchFilters;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatBot extends TwitchBot {

    ChatBot() {
        this.setUsername("chatbot");
        this.setOauth_Key("oauth:" + Settings.oauth);
    }

    @Override
    public void onMessage(User user, Channel channel, String msg) {

        if (msg.startsWith("!")) {

            String command = msg.split(" ")[0];
            command.replaceAll("!", "");
            String[] arguments = msg.split(" ");
            boolean isBroadcaster = ("#" + user).equalsIgnoreCase(String.valueOf(channel));

            int level = 1;
            if (arguments.length > 1) {
                try {
                    level = Integer.parseInt(arguments[1]);
                    if (level <= 0 || level > Requests.levels.size() + 1) {
                        level = 1;
                    }
                } catch (Exception ignored) {
                }
            }
            if (command.equalsIgnoreCase("ID") ||
                    command.equalsIgnoreCase("name") ||
                    command.equalsIgnoreCase("level")) {

                Main.sendMessage("@" + user + " The level at position " +
                        level + " is " + Requests.levels.get(level - 1).getName() +
                        " by " + Requests.levels.get(level - 1).getAuthor() + " (" +
                        Requests.levels.get(level - 1).getLevelID() + ")");
            }
            if (command.equalsIgnoreCase("current")) {

                Main.sendMessage("@" + user + " The current level is " +
                        Requests.levels.get(0).getName() + " by " +
                        Requests.levels.get(level - 1).getAuthor() + " (" +
                        Requests.levels.get(0).getLevelID() + ")");
            }
            if (command.equalsIgnoreCase("song")) {

                Main.sendMessage("@" + user + " The song is " +
                        Requests.levels.get(level - 1).getSongName() + " by " +
                        Requests.levels.get(level - 1).getSongAuthor() +
                        " (" + Requests.levels.get(level - 1).getSongID() + ")");
            }
            if (command.equalsIgnoreCase("likes")) {

                Main.sendMessage("@" + user + " There are " +
                        Requests.levels.get(level - 1).getLikes() + " likes!");
            }
            if (command.equalsIgnoreCase("downloads")) {

                Main.sendMessage("@" + user + " There are " +
                        Requests.levels.get(level - 1).getDownloads() + " downloads!");
            }
            if (command.equalsIgnoreCase("difficulty")) {

                Main.sendMessage("@" + user + " The difficulty is " +
                        Requests.levels.get(level - 1).getDifficulty().toLowerCase());
            }
            if (command.equalsIgnoreCase("remove")) {

                for (int i = 0; i < Requests.levels.size(); i++) {
                    try {
                        if (Requests.levels.get(i).getLevelID().equals(Requests.levels.get(level - 1).getLevelID())
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
                            sendMessage("@" + user + ", your level has been removed!", channel);
                        }
                        if (Requests.levels.get(i).getLevelID().equals(Requests.levels.get(level - 1).getLevelID())
                                && (user.isMod(channel) || isBroadcaster)) {
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
                            sendMessage("@" + user + ", your level has been removed!", channel);
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
            if (command.equalsIgnoreCase("clear") && (user.isMod(channel) || isBroadcaster)) {

                for (int i = 0; i < Requests.levels.size(); i++) {
                    LevelsWindow.removeButton();
                }
                Requests.levels.clear();
                SongWindow.refreshInfo();
                InfoWindow.refreshInfo();
                CommentsWindow.unloadComments(true);
                sendMessage("@" + user + " Successfully cleared the queue!", channel);
            }
            if (command.equalsIgnoreCase("q") ||
                    command.equalsIgnoreCase("queue") ||
                    command.equalsIgnoreCase("levelList") ||
                    command.equalsIgnoreCase("list") ||
                    command.equalsIgnoreCase("requests") ||
                    command.equalsIgnoreCase("page")) {

                if (user.isMod(channel) || isBroadcaster) {

                    StringBuilder message = new StringBuilder();
                    int page = 1;
                    try {
                        page = Integer.parseInt(arguments[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
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
                            e.printStackTrace();
                            message.delete(0, message.length());
                            sendMessage("@" + user + " No levels on page " + page, channel);
                            break;
                        }
                    }
                    sendMessage(message, channel);
                } else {
                    sendMessage("This command is for mods, use !where or !position to find your position in the queue!", channel);
                }
            }


            if (command.equalsIgnoreCase("p") ||
                    command.equalsIgnoreCase("where") ||
                    command.equalsIgnoreCase("position")) {

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
                switch (level % 100) {
                    case 11:
                    case 12:
                    case 13:
                    default:
                        ordinal = (level) + suffixes[(level) % 10];
                }
                try {
                    sendMessage("@" + user + ", " + userLevels.get(level - 1).getName()
                            + " is at position " + userPosition.get(level - 1) + " in the queue!", channel);
                } catch (IndexOutOfBoundsException e) {

                    sendMessage("@" + user + " You don't have a " + ordinal + " level in the queue!", channel);
                }
            }
            if (command.equalsIgnoreCase("help")) {
                if (user.isMod(channel) || isBroadcaster) {
                    if (arguments.length == 1) {
                        sendMessage("@" + user + " List of Commands | Type !help <command> for more help. | !request | !position | !ID | !difficulty | !song | !likes | !downloads | !remove | !queue | !block | !blockuser", channel);
                    } else if (arguments[1].contains("request")) {
                        sendMessage("@" + user + " Used to send requests | Usage: \"!request <Level ID>\" to send via level ID | \"!request <Level Name>\" to send via level name | \"!request <Level Name> by <User>\" to send via level name by a user.", channel);
                    } else if (arguments[1].contains("position")) {
                        sendMessage("@" + user + " Used to find your position in the queue | Usage: \"!position\" to get closest in the queue | \"!position <Number>\" to get a specific position", channel);
                    } else if (arguments[1].contains("ID")) {
                        sendMessage("@" + user + " Used to find the current level's ID | Usage: \"!ID\"", channel);
                    } else if (arguments[1].contains("difficulty")) {
                        sendMessage("@" + user + " Used to find the current level's difficulty Usage: \"!difficulty\"", channel);
                    } else if (arguments[1].contains("song")) {
                        sendMessage("@" + user + " Used to find the current level's song information | Usage: \"!song\"", channel);
                    } else if (arguments[1].contains("likes")) {
                        sendMessage("@" + user + " Used to find the current level's like count | Usage: \"!likes\"", channel);
                    } else if (arguments[1].contains("downloads")) {
                        sendMessage("@" + user + " Used to find the current level's download count | Usage: \"!count\"", channel);
                    } else if (arguments[1].contains("remove")) {
                        sendMessage("@" + user + " Used to remove a level from the queue | Usage: \"!remove <Position>\"", channel);
                    } else if (arguments[1].contains("block")) {
                        sendMessage("@" + user + " Used to block a level ID | Usage: \"!block <Level ID>\" to block a specific ID", channel);
                    } else if (arguments[1].contains("blockuser")) {
                        sendMessage("@" + user + " Used to block a user | Usage: \"!blockuser\" to block the current user | \"!blockuser <Username>\" to block a specific user", channel);
                    }
                } else {
                    if (arguments.length == 1) {
                        sendMessage("@" + user + " List of Commands | Type !help <command> for more help. | !request | !position | !ID | !difficulty | !song | !likes | !downloads | !remove", channel);
                    } else if (arguments[1].contains("request")) {
                        sendMessage("@" + user + " Used to send requests | Usage: \"!request <Level ID>\" to send via level ID | \"!request <Level Name>\" to send via level name | \"!request <Level Name> by <User>\" to send via level name by a user.", channel);
                    } else if (arguments[1].contains("position")) {
                        sendMessage("@" + user + " Used to find your position in the queue | Usage: \"!position <Number>\"", channel);
                    } else if (arguments[1].contains("ID")) {
                        sendMessage("@" + user + " Used to find the current level's ID | Usage: \"!ID\"", channel);
                    } else if (arguments[1].contains("difficulty")) {
                        sendMessage("@" + user + " Used to find the current level's difficulty Usage: \"!difficulty\"", channel);
                    } else if (arguments[1].contains("song")) {
                        sendMessage("@" + user + " Used to find the current level's song information | Usage: \"!song\"", channel);
                    } else if (arguments[1].contains("likes")) {
                        sendMessage("@" + user + " Used to find the current level's like count | Usage: \"!likes\"", channel);
                    } else if (arguments[1].contains("downloads")) {
                        sendMessage("@" + user + " Used to find the current level's download count | Usage: \"!count\"", channel);
                    } else if (arguments[1].contains("remove")) {
                        sendMessage("@" + user + " Used to remove your own levels from the queue | Usage: \"!remove <Position>\" (To find position, use \"!position\"", channel);
                    }
                }
            }
            if (command.equalsIgnoreCase("unblock") && (user.isMod(channel) || isBroadcaster)) {
                String unblocked = arguments[1];
                try {
                    boolean exists = false;
                    File file = new File("blocked.txt");

                    Scanner sc = new Scanner(file);
                    while (sc.hasNextLine()) {
                        if (String.valueOf(unblocked).equals(sc.nextLine())) {
                            System.out.println("Blocked ID");
                            exists = true;
                            break;
                        }
                    }
                    sc.close();
                    if (exists) {
                        File temp = new File("_temp_");
                        PrintWriter out = new PrintWriter(new FileWriter(temp));
                        Files.lines(file.toPath())
                                .filter(line -> !line.contains(unblocked))
                                .forEach(out::println);
                        out.flush();
                        out.close();
                        file.delete();
                        temp.renameTo(file);
                        sendMessage("@" + user + " Successfully unblocked " + arguments[1], channel);
                    } else {
                        sendMessage("@" + user + " That level isn't blocked!", channel);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    sendMessage("@" + user + " unblock failed!", channel);
                }
            }
            if (command.equalsIgnoreCase("block") && (user.isMod(channel) || isBroadcaster)) {
                try {
                    int blockedID = Integer.parseInt(arguments[1]);
                    boolean goThrough = true;
                    File file = new File("blocked.txt");
                    Scanner sc = new Scanner(file);

                    while (sc.hasNextLine()) {
                        if (String.valueOf(blockedID).equals(sc.nextLine())) {
                            System.out.println("Blocked ID");
                            goThrough = false;
                            break;
                        }
                    }
                    sc.close();
                    if (goThrough) {
                        FileWriter fr;
                        try {
                            fr = new FileWriter(file, true);
                            fr.write(blockedID + "\n");
                            fr.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        sendMessage("@" + user + " Successfully blocked " + arguments[1], channel);
                    } else {
                        sendMessage("@" + user + " ID Already Blocked!", channel);
                    }
                } catch (Exception e) {
                    sendMessage("@" + user + " Invalid ID", channel);
                }

            }
            if (command.equalsIgnoreCase("blockUser") && (user.isMod(channel) || isBroadcaster)) {
                sendMessage("Soon...", channel); //TODO Blocking User
            }


            if (command.equalsIgnoreCase("r") ||
                    command.equalsIgnoreCase("request") ||
                    command.equalsIgnoreCase("req") ||
                    command.equalsIgnoreCase("send")) {
                Matcher m = null;
                try {
                    m = Pattern.compile("(\\d+)").matcher(arguments[1]);
                } catch (Exception ignored) {
                }
                assert m != null;
                if (m.matches() && arguments.length <= 2) {
                    try {
                        Requests.addRequest(m.group(1), String.valueOf(user), false);
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
                    if (message.toString().contains("by")) {
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
                                                String.valueOf(user), false);
                                        break outerLoop;
                                    }
                                }
                            }

                        } catch (IndexOutOfBoundsException ignored) {
                        } catch (MissingAccessException | IOException e) {
                            sendMessage("@" + user + " That level or user doesn't exist!", channel);
                            e.printStackTrace();
                        }

                    } else if (message.toString().contains("with")) {
                        String level1 = message.toString().split("with ")[0].toUpperCase();
                        String songUrl = message.toString().split("with ")[1];
                        try {

                            new URL(songUrl).toURI();
                            try {
                                Requests.addRequest(m.group(1), String.valueOf(user), true);
                                //TODO: figure out custom music somehow...

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            sendMessage("@" + user + " Not a valid link!", channel);
                        }
                        System.out.println("Level ID: " + level1 + " Song Link: " + songUrl);
                    } else {

                        AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
                        try {
                            Requests.addRequest(String
                                            .valueOf(Objects.requireNonNull(client.searchLevels(message.toString(), LevelSearchFilters.create(), 0)
                                                    .block()).asList().get(0).getId()),
                                    String.valueOf(user), false);
                        } catch (MissingAccessException | IOException e) {
                            sendMessage("@" + user + " That level doesn't exist!", channel);
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if (!msg.startsWith("!")) {

            // --------------------
            // Get request from chat without commands

            Matcher m = Pattern.compile("\\s*(\\d{6,})\\s*").matcher(msg);
            if (m.find()) {
                try {
                    String[] msgs = msg.split(" ");
                    String mention = "";
                    for (String s : msgs) {
                        if (s.contains("@")) {
                            mention = s;
                            break;
                        }
                    }
                    if (!mention.contains(m.group(1))) {
                        Requests.addRequest(m.group(1), String.valueOf(user), false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

