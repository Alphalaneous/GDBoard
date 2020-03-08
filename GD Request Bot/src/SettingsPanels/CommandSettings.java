package SettingsPanels;

import Main.CheckboxButton;
import Main.Defaults;
import Main.JButtonUI;

import javax.swing.*;
import java.awt.*;

public class CommandSettings {
    private static JButtonUI defaultUI = new JButtonUI();

    public static JPanel createPanel() {

        defaultUI.setBackground(Defaults.BUTTON);
        defaultUI.setHover(Defaults.HOVER);
        defaultUI.setSelect(Defaults.SELECT);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setDoubleBuffered(true);
        panel.setBounds(0, 0, 415, 622);
        panel.setBackground(Defaults.SUB_MAIN);



        return panel;



    }
    public static void loadSettings(){

    }
    public static void setSettings(){

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
