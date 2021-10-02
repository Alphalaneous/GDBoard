package com.alphalaneous.SettingsPanels;

import com.alphalaneous.Components.CurvedButton;
import com.alphalaneous.Components.ListButton;
import com.alphalaneous.Components.ListView;
import com.alphalaneous.Defaults;
import com.alphalaneous.Windows.DialogBox;
import com.alphalaneous.Windows.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import static com.alphalaneous.Defaults.settingsButtonUI;

public class RequestsLog {

    private static final ListView listView = new ListView("$LOGGED_IDS_SETTINGS$");
    private static final Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");

    public static JPanel createPanel(){

        listView.addButton("\uF0CE", () -> new Thread(() -> {
            Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");
            String option = DialogBox.showDialogBox("$CLEAR_LOGS_DIALOG_TITLE$", "$CLEAR_LOGS_DIALOG_INFO$", "", new String[]{"$YES$", "$NO$"});

            if (option.equalsIgnoreCase("YES")) {
                if (Files.exists(file)) {
                    try {
                        Files.delete(file);
                        listView.clearElements();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }).start());
        return listView;
    }

    public static void clear(){
        listView.clearElements();
    }

    public static void loadIDs(){
        new Thread(() -> {
            File file = new File(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");
            if (file.exists()) {
                Scanner sc = null;
                try {
                    sc = new Scanner(file);
                } catch (FileNotFoundException f) {
                    f.printStackTrace();
                }
                assert sc != null;
                while (sc.hasNextLine()) {
                    listView.addElement(createButton(String.valueOf(Long.parseLong(sc.nextLine().split(",")[0]))));
                }
                sc.close();
            }
        }).start();
    }
    public static CurvedButton createButton(String text){
        ListButton button = new ListButton(text, 80);
        button.addActionListener(e -> new Thread(() -> {

            String option = DialogBox.showDialogBox("$REMOVE_LOG_DIALOG_TITLE$", "$REMOVE_LOG_DIALOG_INFO$", "", new String[]{"$YES$", "$NO$"}, new Object[]{button.getLText()});

            if (option.equalsIgnoreCase("YES")) {
                if (Files.exists(file)) {
                    try {
                        Path temp = Paths.get(Defaults.saveDirectory + "\\GDBoard\\_tempLog_");
                        PrintWriter out = new PrintWriter(new FileWriter(temp.toFile()));
                        Files.lines(file)
                                .filter(line -> !line.contains(button.getLText()))
                                .forEach(out::println);
                        out.flush();
                        out.close();
                        Files.delete(file);
                        Files.move(temp, temp.resolveSibling(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt"), StandardCopyOption.REPLACE_EXISTING);

                    } catch (IOException ex) {

                        JOptionPane.showMessageDialog(Window.getWindow(), "There was an error writing to the file!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                listView.removeElement(button);
            }
        }).start());
        return button;
    }
}
