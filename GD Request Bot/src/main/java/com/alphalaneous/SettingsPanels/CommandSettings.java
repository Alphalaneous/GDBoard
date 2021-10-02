package com.alphalaneous.SettingsPanels;

import com.alphalaneous.BotHandler;
import com.alphalaneous.Components.CurvedButton;
import com.alphalaneous.Components.ListButton;
import com.alphalaneous.Components.ListView;
import com.alphalaneous.Defaults;
import com.alphalaneous.Main;
import com.alphalaneous.Settings;
import com.alphalaneous.Windows.CommandEditor;
import com.alphalaneous.Windows.DialogBox;
import com.alphalaneous.Windows.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;

import static com.alphalaneous.Defaults.settingsButtonUI;

public class CommandSettings {

    private static final ListView listView = new ListView("$COMMANDS_LIST$");
    private static final String[] gdCommands = {"!gdping", "!block", "!blockuser", "!unblock", "!unblockuser", "!clear", "!info", "!move", "!next", "!position", "!queue", "!remove", "!request", "!song", "!toggle", "!top", "!wronglevel"};

    public static JPanel createPanel(){

        listView.addButton("\uF0D1", () ->
            CommandEditor.showEditor("commands", "", false)
        );
        refresh();
        return listView;
    }

    public static void clear(){
        listView.clearElements();
    }

    public static void refresh(){
            listView.clearElements();
            HashMap<String, ButtonInfo> existingCommands = new HashMap<>();
            try {
                URI uri = Main.class.getResource("/Commands/").toURI();
                Path myPath;
                if (uri.getScheme().equals("jar")) {
                    myPath = BotHandler.fileSystem.getPath("/Commands/");
                } else {
                    myPath = Paths.get(uri);
                }
                Stream<Path> walk = Files.walk(myPath, 1);
                Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/commands/");
                if (Files.exists(comPath)) {
                    Stream<Path> walk1 = Files.walk(comPath, 1);
                    for (Iterator<Path> it = walk1.iterator(); it.hasNext(); ) {
                        Path path = it.next();
                        String[] file = path.toString().split("\\\\");
                        String fileName = file[file.length - 1];
                        if (fileName.endsWith(".js")) {
                            existingCommands.put(fileName.substring(0, fileName.length() - 3), new ButtonInfo(path, false));
                        }
                    }
                }
                for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
                    Path path = it.next();
                    String[] file;
                    if (uri.getScheme().equals("jar")) {
                        file = path.toString().split("/");
                    } else {
                        file = path.toString().split("\\\\");
                    }
                    String fileName = file[file.length - 1];
                    if (fileName.endsWith(".js")) {
                        if (!fileName.equalsIgnoreCase("!rick.js") &&
                                !fileName.equalsIgnoreCase("!stoprick.js") &&
                                !fileName.equalsIgnoreCase("!eval.js") &&
                                !fileName.equalsIgnoreCase("!end.js") &&
                                !fileName.equalsIgnoreCase("!popup.js") &&
                                !fileName.equalsIgnoreCase("b!addcom.js") &&
                                !fileName.equalsIgnoreCase("b!editcom.js") &&
                                !fileName.equalsIgnoreCase("b!delcom.js") &&
                                !fileName.equalsIgnoreCase("b!addpoint.js") &&
                                !fileName.equalsIgnoreCase("b!editpoint.js") &&
                                !fileName.equalsIgnoreCase("b!delpoint.js")) {
                            String substring = fileName.substring(0, fileName.length() - 3);
                            if (!existingCommands.containsKey(substring)) {
                                existingCommands.put(substring, new ButtonInfo(path, true));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            TreeMap<String, ButtonInfo> sorted = new TreeMap<>(existingCommands);

            for (Map.Entry<String, ButtonInfo> entry : sorted.entrySet()) {
                boolean exists = false;
                String key = entry.getKey();
                ButtonInfo value = entry.getValue();
                if (!Settings.getSettings("gdMode").asBoolean()) {
                    for (String command : gdCommands) {
                        if (key.equalsIgnoreCase(command)) {
                            exists = true;
                            break;
                        }
                    }
                }
                if (!exists) {
                    listView.addElement(createButton(key, value.isDefault));
                }
            }
    }
    public static CurvedButton createButton(String text, boolean isDefault){
        ListButton button = new ListButton(text, 164);
        button.addActionListener(e -> new Thread(() -> {
            CommandEditor.showEditor("commands", text, isDefault);
        }).start());
        return button;
    }
    public static class ButtonInfo {

        public Path path;
        boolean isDefault;

        ButtonInfo(Path path, boolean isDefault) {
            this.path = path;
            this.isDefault = isDefault;
        }

    }
}
