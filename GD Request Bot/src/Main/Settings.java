package Main;

import java.awt.Point;
import java.io.*;
import java.util.Properties;

class Settings {

    private static Properties gfg = new Properties();
    private static FileInputStream in;
    private static BufferedWriter writer;
    private static File file = new File(System.getenv("APPDATA") + "\\GDBoard\\config.properties");


    static {
        while (true) {

            try {
                writer = new BufferedWriter(new FileWriter(

                        file, true));
                in = new FileInputStream(file);
                break;
            } catch (IOException e) {

                file.mkdir();
                file.getParentFile().mkdir();
            }
        }
    }

    static void setLoction(String key, String setting) {
        if (key.equalsIgnoreCase("Requests")) {
            int x = Integer.parseInt(setting.split(",")[0]);
            int y = Integer.parseInt(setting.split(",")[1]);
            reqPin = Boolean.parseBoolean(setting.split(",")[2]);
            requestsWLoc = new Point(x, y);
        }
        if (key.equalsIgnoreCase("Comments")) {
            int x = Integer.parseInt(setting.split(",")[0]);
            int y = Integer.parseInt(setting.split(",")[1]);
            comPin = Boolean.parseBoolean(setting.split(",")[2]);
            commentWLoc = new Point(x, y);
        }
        if (key.equalsIgnoreCase("Information")) {
            int x = Integer.parseInt(setting.split(",")[0]);
            int y = Integer.parseInt(setting.split(",")[1]);
            infoPin = Boolean.parseBoolean(setting.split(",")[2]);
            infoWLoc = new Point(x, y);
        }
        if (key.equalsIgnoreCase("Actions")) {
            int x = Integer.parseInt(setting.split(",")[0]);
            int y = Integer.parseInt(setting.split(",")[1]);
            actPin = Boolean.parseBoolean(setting.split(",")[2]);
            actionsWLoc = new Point(x, y);
        }
        if (key.equalsIgnoreCase("Music - Newgrounds Audio")) {
            int x = Integer.parseInt(setting.split(",")[0]);
            int y = Integer.parseInt(setting.split(",")[1]);
            songPin = Boolean.parseBoolean(setting.split(",")[2]);
            songWLoc = new Point(x, y);
        }
    }
    static void writeLocation() throws IOException {
        writeSettings("song", songWLoc.x + "," + songWLoc.y + "," + songPin);
        writeSettings("actions", actionsWLoc.x + "," + actionsWLoc.y + "," + actPin);
        writeSettings("info", infoWLoc.x + "," + infoWLoc.y + "," + infoPin);
        writeSettings("requests", requestsWLoc.x + "," + requestsWLoc.y + "," + reqPin);
        writeSettings("comments", commentWLoc.x + "," + commentWLoc.y + "," + comPin);
    }
    static void writeSettings(String key, String setting) throws IOException {
        gfg.load(in);
        if (gfg.containsKey(key)) {
            BufferedReader file = new BufferedReader(new FileReader(System.getenv("APPDATA") + "\\GDBoard\\config.properties"));
            StringBuilder inputBuffer = new StringBuilder();
            String line;
            while ((line = file.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();

            FileOutputStream fileOut = new FileOutputStream(System.getenv("APPDATA") + "\\GDBoard\\config.properties");
            fileOut.write(inputBuffer.toString().replace(gfg.get(key).toString(), setting).getBytes());
            fileOut.close();
            System.out.println(key + ": " + setting);
        } else {
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.newLine();
            writer.write((key + "=" + setting));
            writer.close();
        }

    }

    static void loadSettings() throws IOException {
        gfg.load(in);
        try {
            oauth = gfg.get("oauth").toString();
            hasOauth = true;
        } catch (NullPointerException e) {
            hasOauth = false;
        }
        try {
            channel = gfg.get("channel").toString().toLowerCase();
            hasChannel = true;
        } catch (NullPointerException e) {
            hasChannel = false;
        }
        try {
            windowedMode = Boolean.parseBoolean(gfg.get("windowed").toString());
            hasWindowed = true;
        } catch (NullPointerException e) {
            hasWindowed = false;
        }
        try {
            int x = Integer.parseInt(gfg.get("requests").toString().split(",")[0]);
            int y = Integer.parseInt(gfg.get("requests").toString().split(",")[1]);
            reqPin = Boolean.parseBoolean(gfg.get("requests").toString().split(",")[2]);
            requestsWLoc = new Point(x, y);
            LevelsWindow.setPin(reqPin);
        } catch (NullPointerException ignored) {
        }
        try {
            int x = Integer.parseInt(gfg.get("actions").toString().split(",")[0]);
            int y = Integer.parseInt(gfg.get("actions").toString().split(",")[1]);
            actPin = Boolean.parseBoolean(gfg.get("actions").toString().split(",")[2]);
            actionsWLoc = new Point(x, y);
            ActionsWindow.setPin(actPin);
        } catch (NullPointerException ignored) {
        }
        try {
            int x = Integer.parseInt(gfg.get("comments").toString().split(",")[0]);
            int y = Integer.parseInt(gfg.get("comments").toString().split(",")[1]);
            comPin = Boolean.parseBoolean(gfg.get("comments").toString().split(",")[2]);
            commentWLoc = new Point(x, y);
            CommentsWindow.setPin(comPin);
        } catch (NullPointerException ignored) {
        }
        try {
            int x = Integer.parseInt(gfg.get("info").toString().split(",")[0]);
            int y = Integer.parseInt(gfg.get("info").toString().split(",")[1]);
            infoPin = Boolean.parseBoolean(gfg.get("info").toString().split(",")[2]);
            infoWLoc = new Point(x, y);
            InfoWindow.setPin(infoPin);
        } catch (NullPointerException ignored) {
        }
        try {
            int x = Integer.parseInt(gfg.get("song").toString().split(",")[0]);
            int y = Integer.parseInt(gfg.get("song").toString().split(",")[1]);
            songPin = Boolean.parseBoolean(gfg.get("song").toString().split(",")[2]);
            songWLoc = new Point(x, y);
            SongWindow.setPin(songPin);
        } catch (NullPointerException ignored) {
        }
    }

    static boolean windowedMode = false;
    static String oauth;
    private static Point requestsWLoc = new Point(10, 10);
    private static Point infoWLoc = new Point(1920 - 400 - 10, 10);
    private static Point commentWLoc = new Point(10, 500);
    private static Point songWLoc = new Point(1920 - 300 - 10, 600);
    private static Point actionsWLoc = new Point(1920 - 260 - 10, 200);
    static String channel;
    static boolean hasOauth = false;
    static boolean hasChannel = false;
    static boolean hasWindowed = false;
    static boolean songPin = false;
    static boolean infoPin = false;
    static boolean comPin = false;
    static boolean actPin = false;
    static boolean reqPin = false;

    static void setWindowedMode(boolean mode) {
        Settings.windowedMode = mode;
    }

    static void setOAuth(String oauth) {
        Settings.oauth = oauth;
    }

    static void setChannel(String channel) {
        Settings.channel = "#" + channel.toLowerCase();
    }

    static Point getRequestsWLoc() {
        if (Settings.windowedMode) {
            requestsWLoc = new Point(0, 0);
        }
        return requestsWLoc;
    }

    static Point getInfoWLoc() {
        if (Settings.windowedMode) {
            infoWLoc = new Point(0, 402);
        }
        return infoWLoc;
    }

    static Point getCommentWLoc() {
        if (Settings.windowedMode) {
            commentWLoc = new Point(402, 98);
        }
        return commentWLoc;
    }

    static Point getSongWLoc() {
        if (Settings.windowedMode) {
            songWLoc = new Point(402, 422);
        }
        return songWLoc;
    }

    static Point getActionsWLoc() {
        if (Settings.windowedMode) {
            actionsWLoc = new Point(402, 20);
        }
        return actionsWLoc;
    }
}
