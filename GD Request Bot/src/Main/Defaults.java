package Main;

import com.registry.RegDWORDValue;
import com.registry.RegistryKey;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class Defaults {

    public static int screenNum = Settings.monitor;
    static Rectangle screenSize = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getScreenDevices()[screenNum].getDefaultConfiguration().getBounds();
    static Rectangle prevScreenSize = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getScreenDevices()[screenNum].getDefaultConfiguration().getBounds();

    public static Color MAIN;
    public static Color BUTTON;
    public static Color HOVER;
    public static Color SUB_MAIN;
    public static Color SELECT;
    public static Color TOP;
    public static Color FOREGROUND;
    public static Color FOREGROUND2;
    public static Color OUTLINE = new Color(70, 70, 70);
    public static Color BUTTON_HOVER;
    public static Color TEXT_BOX;
    static AtomicBoolean dark = new AtomicBoolean();

    //region Dark Mode
    private static void setDark() {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("MM.dd");
        if (!ft.format(date).equalsIgnoreCase("04.01")) {
            MAIN = new Color(31, 31, 31);
            TEXT_BOX = new Color(58, 58, 58);
            BUTTON = new Color(50, 50, 50);
            HOVER = new Color(60, 60, 60);
            SUB_MAIN = new Color(20, 20, 20);
            SELECT = new Color(70, 70, 70);
            BUTTON_HOVER = new Color(80, 80, 80);
            TOP = Color.BLACK;
            FOREGROUND = Color.WHITE;
            FOREGROUND2 = new Color(140, 140, 140);
        } else {
            MAIN = new Color(137, 0, 96);
            TEXT_BOX = new Color(203, 0, 154);
            BUTTON = new Color(107, 0, 80);
            HOVER = new Color(88, 0, 64);
            SUB_MAIN = new Color(111, 0, 80);
            SELECT = new Color(163, 34, 121);
            BUTTON_HOVER = new Color(203, 0, 154);
            TOP = new Color(70, 14, 52);
            FOREGROUND = Color.WHITE;
            FOREGROUND2 = new Color(159, 0, 114);
        }
        Overlay.refreshUI(true);
    }
    //endregion

    //region Light Mode
    private static void setLight() {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("MM.dd");
        if (!ft.format(date).equalsIgnoreCase("04.01")) {
            MAIN = new Color(230, 230, 230);
            TEXT_BOX = new Color(205, 205, 205);
            BUTTON = new Color(210, 210, 210);
            HOVER = new Color(211, 211, 211);
            SUB_MAIN = new Color(240, 240, 240);
            SELECT = new Color(215, 215, 215);
            BUTTON_HOVER = new Color(200, 200, 200);
            TOP = Color.WHITE;
            FOREGROUND = Color.BLACK;
            FOREGROUND2 = new Color(100, 100, 100);
        } else {
            MAIN = new Color(137, 0, 96);
            TEXT_BOX = new Color(203, 0, 154);
            BUTTON = new Color(107, 0, 80);
            HOVER = new Color(88, 0, 64);
            SUB_MAIN = new Color(111, 0, 80);
            SELECT = new Color(163, 34, 121);
            BUTTON_HOVER = new Color(203, 0, 154);
            TOP = new Color(70, 14, 52);
            FOREGROUND = Color.WHITE;
            FOREGROUND2 = new Color(159, 0, 114);
            Overlay.refreshUI(true);
        }
        Overlay.refreshUI(true);
    }
    //endregion

    //region Main Thread
    static void startMainThread() {
        RegistryKey personalizeStart = new RegistryKey(
                "Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");

        final int[] prevTheme = new int[1];
        try {
            prevTheme[0] = ((RegDWORDValue) personalizeStart.getValue("AppsUseLightTheme")).getIntValue();
        } catch (NullPointerException e) {
            prevTheme[0] = 1;
        }
        if (prevTheme[0] == 0) {
            Defaults.setDark();
            dark.set(false);
        } else if (prevTheme[0] == 1) {
            Defaults.setLight();
            dark.set(true);
        }

        Thread thread = new Thread(() -> {
            while (true) {

                int minute;
                int hour;
                String half;
                LocalDateTime now = LocalDateTime.now();
                minute = now.getMinute();
                hour = now.getHour();
                half = "AM";
                if (hour >= 12) {
                    if (hour != 12) {
                        hour = hour - 12;
                    }
                    half = "PM";
                }
                if (hour == 0) {
                    hour = 12;
                }
                MainBar.setTime(hour + ":" + String.format("%02d", minute) + " " + half);
                RegistryKey personalize = new RegistryKey(
                        "Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize");

                int theme = 0;
                try {
                    theme = ((RegDWORDValue) personalize.getValue("AppsUseLightTheme")).getIntValue();
                } catch (NullPointerException ignored) {
                }
                if (theme == 0 && prevTheme[0] == 1) {
                    Defaults.setDark();
                    dark.set(false);
                    prevTheme[0] = 0;
                } else if (theme == 1 && prevTheme[0] == 0) {
                    Defaults.setLight();
                    dark.set(true);
                    prevTheme[0] = 1;
                }
                if (!Settings.windowedMode) {
                    screenSize = GraphicsEnvironment
                            .getLocalGraphicsEnvironment()
                            .getScreenDevices()[screenNum].getDefaultConfiguration().getBounds();
                    if (!screenSize.equals(prevScreenSize)) {
                        Overlay.refreshUI(false);
                    }
                    prevScreenSize = screenSize;
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
    //endregion
}
