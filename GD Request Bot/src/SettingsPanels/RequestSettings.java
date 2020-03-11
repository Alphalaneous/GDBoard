package SettingsPanels;

import Main.CheckboxButton;
import Main.Defaults;
import Main.JButtonUI;
import Main.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class RequestSettings {
    private static JButtonUI defaultUI = new JButtonUI();
    /*private static CheckboxButton auto = createButton("Auto", 110);
        private static CheckboxButton easy = createButton("Easy", 140);
        private static CheckboxButton normal = createButton("Normal", 170);
        private static CheckboxButton hard = createButton("Hard", 200);
        private static CheckboxButton harder = createButton("Harder", 230);
        private static CheckboxButton insane = createButton("Insane", 260);
        private static CheckboxButton demon = createButton("Demon", 290);*/
    public static boolean ratedOption = false;
    private static CheckboxButton rated = createButton("Rated Levels Only", 15);

    public static JPanel createPanel() {

        defaultUI.setBackground(Defaults.BUTTON);
        defaultUI.setHover(Defaults.HOVER);
        defaultUI.setSelect(Defaults.SELECT);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setDoubleBuffered(true);
        panel.setBounds(0, 0, 415, 622);
        panel.setBackground(Defaults.SUB_MAIN);

        rated.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                ratedOption = rated.getSelectedState();
            }
        });
        panel.add(rated);
        return panel;



    }
    public static void loadSettings(){
        try {
            ratedOption = Boolean.parseBoolean(Settings.getSettings("rated"));
            rated.setChecked(ratedOption);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void setSettings(){
        try {
            Settings.writeSettings("rated", String.valueOf(ratedOption));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CheckboxButton createButton(String text, int y){

        CheckboxButton button = new CheckboxButton(text);
        button.setBounds(25,y,365,30);
        button.setUI(defaultUI);
        button.setForeground(Defaults.FOREGROUND);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFont(new Font("bahnschrift", Font.PLAIN, 14));
        button.refresh();
        return button;
    }
}
