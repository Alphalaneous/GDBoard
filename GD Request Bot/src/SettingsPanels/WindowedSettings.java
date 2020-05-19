package SettingsPanels;

import Main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class WindowedSettings {
    private static JButtonUI defaultUI = new JButtonUI();
    public static boolean onTopOption = true;
    private static CheckboxButton onTop = createButton("Always On Top", 20);

    public static JPanel createPanel() {

        defaultUI.setBackground(Defaults.BUTTON);
        defaultUI.setHover(Defaults.HOVER);
        defaultUI.setSelect(Defaults.SELECT);
        onTop.setChecked(true);
        onTop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                onTopOption = onTop.getSelectedState();
                Windowed.setOnTop(onTop.getSelectedState());
                ((InnerWindow) Windowed.window).setMinimize(!onTopOption);
            }
        });
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setDoubleBuffered(true);
        panel.setBounds(0, 0, 415, 622);
        panel.setBackground(Defaults.SUB_MAIN);
        panel.add(onTop);
        return panel;
    }
    public static void loadSettings(){
        try {
            if(!Settings.getSettings("onTop").equalsIgnoreCase("")) {
                onTopOption = Boolean.parseBoolean(Settings.getSettings("onTop"));
                onTop.setChecked(onTopOption);
                Windowed.setOnTop(onTopOption);
                ((InnerWindow) Windowed.window).setMinimize(!onTopOption);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void setSettings(){
        try {
            Settings.writeSettings("onTop", String.valueOf(onTopOption));
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
        button.setFont(Defaults.MAIN_FONT.deriveFont(14f));
        button.refresh();
        return button;
    }

}
