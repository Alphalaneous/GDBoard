package Main;

import java.awt.*;
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
                writer = new BufferedWriter(new FileWriter(file, true));
                in = new FileInputStream(file);
                break;
            } catch (IOException e) {
                file.mkdir();
                file.getParentFile().mkdir();
            }
        }
    }

    static void setWindowSettings(String key, String setting) {

        if (key.equalsIgnoreCase("Requests")) {
            int x = Integer.parseInt(setting.split(",")[0]);
            int y = Integer.parseInt(setting.split(",")[1]);
            reqPin = Boolean.parseBoolean(setting.split(",")[2]);
            reqX = Boolean.parseBoolean(setting.split(",")[3]);
            requestsWLoc = new Point(x, y);
        }
        if (key.equalsIgnoreCase("Comments")) {
            int x = Integer.parseInt(setting.split(",")[0]);
            int y = Integer.parseInt(setting.split(",")[1]);
            comPin = Boolean.parseBoolean(setting.split(",")[2]);
            comX = Boolean.parseBoolean(setting.split(",")[3]);
            commentWLoc = new Point(x, y);
        }
        if (key.equalsIgnoreCase("Information")) {
            int x = Integer.parseInt(setting.split(",")[0]);
            int y = Integer.parseInt(setting.split(",")[1]);
            infoPin = Boolean.parseBoolean(setting.split(",")[2]);
            infoX = Boolean.parseBoolean(setting.split(",")[3]);
            infoWLoc = new Point(x, y);
        }
        if (key.equalsIgnoreCase("Actions")) {
            int x = Integer.parseInt(setting.split(",")[0]);
            int y = Integer.parseInt(setting.split(",")[1]);
            actPin = Boolean.parseBoolean(setting.split(",")[2]);
            actX = Boolean.parseBoolean(setting.split(",")[3]);
            actionsWLoc = new Point(x, y);
        }
        if (key.equalsIgnoreCase("Music - Newgrounds Audio")) {
            int x = Integer.parseInt(setting.split(",")[0]);
            int y = Integer.parseInt(setting.split(",")[1]);
            songPin = Boolean.parseBoolean(setting.split(",")[2]);
            songX = Boolean.parseBoolean(setting.split(",")[3]);
            songWLoc = new Point(x, y);
        }
    }
    static void writeLocation() throws IOException {
        writeSettings("song", songWLoc.x + "," + songWLoc.y + "," + songPin + "," + songX);
        writeSettings("actions", actionsWLoc.x + "," + actionsWLoc.y + "," + actPin + "," + actX);
        writeSettings("info", infoWLoc.x + "," + infoWLoc.y + "," + infoPin + "," + infoX);
        writeSettings("requests", requestsWLoc.x + "," + requestsWLoc.y + "," + reqPin + "," + reqX);
        writeSettings("comments", commentWLoc.x + "," + commentWLoc.y + "," + comPin + "," + comX);
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

    static void loadSettings(boolean start) throws IOException {
        gfg.load(in);
        try {
            oauth = gfg.get("oauth").toString();
            hasOauth = true;
        } catch (NullPointerException e) {
            hasOauth = false;
        }
        try {
            keybind = Integer.parseInt(gfg.get("keybind").toString());
        } catch (NullPointerException ignored) {
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
        try{
            GraphicsDevice[] screens =  GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getScreenDevices();
            monitor = Integer.parseInt(gfg.get("monitor").toString());
            if(monitor >= screens.length){
                monitor = 0;
            }
            hasMonitor = true;
        }
        catch (NullPointerException e){
            hasMonitor = false;
        }
        if(!start) {
            try {
                int x = Integer.parseInt(gfg.get("requests").toString().split(",")[0]);
                int y = Integer.parseInt(gfg.get("requests").toString().split(",")[1]);
                reqPin = Boolean.parseBoolean(gfg.get("requests").toString().split(",")[2]);
                reqX = Boolean.parseBoolean(gfg.get("requests").toString().split(",")[3]);
                requestsWLoc = new Point(x, y);
                LevelsWindow.setPin(reqPin);
                if(!Settings.windowedMode) {
                    LevelsWindow.setLocation(requestsWLoc);

                    if (!reqX) {
                        LevelsWindow.toggleVisible();
                    }
                }
            } catch (NullPointerException ignored) {
            }
            try {
                int x = Integer.parseInt(gfg.get("actions").toString().split(",")[0]);
                int y = Integer.parseInt(gfg.get("actions").toString().split(",")[1]);
                actPin = Boolean.parseBoolean(gfg.get("actions").toString().split(",")[2]);
                actX = Boolean.parseBoolean(gfg.get("actions").toString().split(",")[3]);
                actionsWLoc = new Point(x, y);
                ActionsWindow.setPin(actPin);
                if(!Settings.windowedMode) {
                    ActionsWindow.setLocation(actionsWLoc);

                    if (!actX) {
                        ActionsWindow.toggleVisible();
                    }
                }
            } catch (NullPointerException ignored) {
            }
            try {
                int x = Integer.parseInt(gfg.get("comments").toString().split(",")[0]);
                int y = Integer.parseInt(gfg.get("comments").toString().split(",")[1]);
                comPin = Boolean.parseBoolean(gfg.get("comments").toString().split(",")[2]);
                comX = Boolean.parseBoolean(gfg.get("comments").toString().split(",")[3]);
                commentWLoc = new Point(x, y);
                CommentsWindow.setPin(comPin);
                if(!Settings.windowedMode) {
                    CommentsWindow.setLocation(commentWLoc);

                    if (!comX) {
                        CommentsWindow.toggleVisible();
                    }
                }
            } catch (NullPointerException ignored) {
            }
            try {
                int x = Integer.parseInt(gfg.get("info").toString().split(",")[0]);
                int y = Integer.parseInt(gfg.get("info").toString().split(",")[1]);
                infoPin = Boolean.parseBoolean(gfg.get("info").toString().split(",")[2]);
                infoX = Boolean.parseBoolean(gfg.get("info").toString().split(",")[3]);
                infoWLoc = new Point(x, y);
                InfoWindow.setPin(infoPin);
                if(!Settings.windowedMode) {
                    InfoWindow.setLocation(infoWLoc);

                    if (!infoX) {
                        InfoWindow.toggleVisible();
                    }
                }
            } catch (NullPointerException ignored) {
            }
            try {
                int x = Integer.parseInt(gfg.get("song").toString().split(",")[0]);
                int y = Integer.parseInt(gfg.get("song").toString().split(",")[1]);
                songPin = Boolean.parseBoolean(gfg.get("song").toString().split(",")[2]);
                songX = Boolean.parseBoolean(gfg.get("song").toString().split(",")[3]);
                songWLoc = new Point(x, y);
                SongWindow.setPin(songPin);
                if(!Settings.windowedMode) {
                    SongWindow.setLocation(songWLoc);
                    if (!songX) {
                        SongWindow.toggleVisible();
                    }
                }
            } catch (NullPointerException ignored) {
            }
        }
    }

    static boolean windowedMode = false;
    static String oauth;
    private static Point requestsWLoc = new Point(10, 10);
    private static Point infoWLoc = new Point(1920 - 400 - 10, 10);
    private static Point commentWLoc = new Point(10, 500);
    private static Point songWLoc = new Point(1920 - 300 - 10, 600);
    private static Point actionsWLoc = new Point(1920 - 260 - 10, 200);
    static int keybind;
    static int monitor;
    static String channel;
    static boolean hasMonitor = false;
    static boolean hasOauth = false;
    static boolean hasChannel = false;
    static boolean hasWindowed = false;
    private static boolean songPin = false;
    private static boolean infoPin = false;
    private static boolean comPin = false;
    private static boolean actPin = false;
    private static boolean reqPin = false;
    private static boolean songX = true;
    private static boolean infoX = true;
    private static boolean comX = true;
    private static boolean actX = true;
    private static boolean reqX = true;

    //TODO Save window sizes
    //TODO Requests off Setting
    //TODO User Request Limits
    //TODO Queue size limits
    //TODO Follower Only Setting
    //TODO Sub Only Setting
    //TODO Cheer Only Setting

    /*static void setWindowedMode(boolean mode) {
        Settings.windowedMode = mode;
    }*/

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
