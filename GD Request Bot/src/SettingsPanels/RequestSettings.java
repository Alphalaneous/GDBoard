package SettingsPanels;

import Main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class RequestSettings {
    private static JButtonUI defaultUI = new JButtonUI();
    private static CheckboxButton na = createButton("NA", 5);
    private static CheckboxButton auto = createButton("Auto", 35);
    private static CheckboxButton easy = createButton("Easy", 65);
    private static CheckboxButton normal = createButton("Normal", 95);
    private static CheckboxButton hard = createButton("Hard", 125);
    private static CheckboxButton harder = createButton("Harder", 155);
    private static CheckboxButton insane = createButton("Insane", 185);
    private static CheckboxButton easyDemon = createButton("Easy Demon", 215);
    private static CheckboxButton mediumDemon = createButton("Medium Demon", 245);
    private static CheckboxButton hardDemon = createButton("Hard Demon", 275);
    private static CheckboxButton insaneDemon = createButton("Insane Demon", 305);
    private static CheckboxButton extremeDemon = createButton("Extreme Demon", 335);

    public static boolean ratedOption = false;
    public static boolean unratedOption = false;
    public static boolean disableOption = true;

    private static CheckboxButton rated = createButton("Rated Levels Only", 15);
    private static CheckboxButton unrated = createButton("Unrated Levels Only", 45);
    private static CheckboxButton disableDifficulties = createButton("Disable selected difficulties", 75);
    private static JPanel difficultyPanel = new JPanel(null);

    private static JPanel panel = new JPanel();
    public static ArrayList<String> excludedDifficulties = new ArrayList<>();

    public static JPanel createPanel() {

        defaultUI.setBackground(Defaults.BUTTON);
        defaultUI.setHover(Defaults.HOVER);
        defaultUI.setSelect(Defaults.SELECT);

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
        unrated.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                unratedOption = unrated.getSelectedState();
            }
        });
        disableDifficulties.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                disableOption = disableDifficulties.getSelectedState();
            }
        });
        disableDifficulties.setChecked(true);
        na.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (na.getSelectedState()) {
                    excludedDifficulties.add("na");
                    System.out.println(excludedDifficulties.toString());
                } else {
                    excludedDifficulties.remove("na");
                    System.out.println(excludedDifficulties.toString());
                }
            }
        });
        auto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (auto.getSelectedState()) {
                    excludedDifficulties.add("auto");
                    System.out.println(excludedDifficulties.toString());
                } else {
                    excludedDifficulties.remove("auto");
                    System.out.println(excludedDifficulties.toString());
                }
            }
        });
        easy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (easy.getSelectedState()) {
                    excludedDifficulties.add("easy");
                    System.out.println(excludedDifficulties.toString());
                } else {
                    excludedDifficulties.remove("easy");
                    System.out.println(excludedDifficulties.toString());
                }
            }
        });
        normal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (normal.getSelectedState()) {
                    excludedDifficulties.add("normal");
                    System.out.println(excludedDifficulties.toString());
                } else {
                    excludedDifficulties.remove("normal");
                    System.out.println(excludedDifficulties.toString());
                }
            }
        });
        hard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (hard.getSelectedState()) {
                    excludedDifficulties.add("hard");
                    System.out.println(excludedDifficulties.toString());
                } else {
                    excludedDifficulties.remove("hard");
                    System.out.println(excludedDifficulties.toString());
                }
            }
        });
        harder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (harder.getSelectedState()) {
                    excludedDifficulties.add("harder");
                    System.out.println(excludedDifficulties.toString());
                } else {
                    excludedDifficulties.remove("harder");
                    System.out.println(excludedDifficulties.toString());
                }
            }
        });
        insane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (insane.getSelectedState()) {
                    excludedDifficulties.add("insane");
                    System.out.println(excludedDifficulties.toString());
                } else {
                    excludedDifficulties.remove("insane");
                    System.out.println(excludedDifficulties.toString());
                }
            }
        });
        easyDemon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (easyDemon.getSelectedState()) {
                    excludedDifficulties.add("easy demon");
                    System.out.println(excludedDifficulties.toString());
                } else {
                    excludedDifficulties.remove("easy demon");
                    System.out.println(excludedDifficulties.toString());
                }
            }
        });
        mediumDemon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (mediumDemon.getSelectedState()) {
                    excludedDifficulties.add("medium demon");
                    System.out.println(excludedDifficulties.toString());
                } else {
                    excludedDifficulties.remove("medium demon");
                    System.out.println(excludedDifficulties.toString());
                }
            }
        });
        hardDemon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (hardDemon.getSelectedState()) {
                    excludedDifficulties.add("hard demon");
                    System.out.println(excludedDifficulties.toString());
                } else {
                    excludedDifficulties.remove("hard demon");
                    System.out.println(excludedDifficulties.toString());
                }
            }
        });
        insaneDemon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (insaneDemon.getSelectedState()) {
                    excludedDifficulties.add("insane demon");
                    System.out.println(excludedDifficulties.toString());
                } else {
                    excludedDifficulties.remove("insane demon");
                    System.out.println(excludedDifficulties.toString());
                }
            }
        });
        extremeDemon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (extremeDemon.getSelectedState()) {
                    excludedDifficulties.add("extreme demon");
                    System.out.println(excludedDifficulties.toString());
                } else {
                    excludedDifficulties.remove("extreme demon");
                    System.out.println(excludedDifficulties.toString());
                }
            }
        });

        difficultyPanel.setBounds(0,110, 415, 370);
        difficultyPanel.setBackground(Defaults.TOP);
        panel.add(rated);
        panel.add(unrated);
        panel.add(disableDifficulties);
        difficultyPanel.add(na);
        difficultyPanel.add(auto);
        difficultyPanel.add(easy);
        difficultyPanel.add(normal);
        difficultyPanel.add(hard);
        difficultyPanel.add(harder);
        difficultyPanel.add(insane);
        difficultyPanel.add(easyDemon);
        difficultyPanel.add(mediumDemon);
        difficultyPanel.add(hardDemon);
        difficultyPanel.add(insaneDemon);
        difficultyPanel.add(extremeDemon);
        panel.add(difficultyPanel);
        return panel;


    }

    public static void loadSettings() {
        try {
            ratedOption = Boolean.parseBoolean(Settings.getSettings("rated"));
            rated.setChecked(ratedOption);
            unratedOption = Boolean.parseBoolean(Settings.getSettings("unrated"));
            unrated.setChecked(unratedOption);
            if(!Settings.getSettings("disableDifficulties").equalsIgnoreCase("")){
                disableOption = Boolean.parseBoolean(Settings.getSettings("disableDifficulties"));
            }
            disableDifficulties.setChecked(disableOption);
            if(!Settings.getSettings("difficultyFilter").equalsIgnoreCase("")) {
                excludedDifficulties = new ArrayList<>(Arrays.asList(Settings.getSettings("difficultyFilter").split(", ")));
            }
            if(excludedDifficulties.contains("na")){
                na.setChecked(true);
            }
            if(excludedDifficulties.contains("auto")){
                auto.setChecked(true);
            }
            if(excludedDifficulties.contains("easy")){
                easy.setChecked(true);
            }
            if(excludedDifficulties.contains("normal")){
                normal.setChecked(true);
            }
            if(excludedDifficulties.contains("hard")){
                hard.setChecked(true);
            }
            if(excludedDifficulties.contains("harder")){
                harder.setChecked(true);
            }
            if(excludedDifficulties.contains("insane")){
                insane.setChecked(true);
            }
            if(excludedDifficulties.contains("easy demon")){
                easyDemon.setChecked(true);
            }
            if(excludedDifficulties.contains("medium demon")){
                mediumDemon.setChecked(true);
            }
            if(excludedDifficulties.contains("hard demon")){
                hardDemon.setChecked(true);
            }
            if(excludedDifficulties.contains("insane demon")){
                insaneDemon.setChecked(true);
            }
            if(excludedDifficulties.contains("extreme demon")){
                extremeDemon.setChecked(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setSettings() {
        try {
            Settings.writeSettings("rated", String.valueOf(ratedOption));
            Settings.writeSettings("unrated", String.valueOf(unratedOption));
            Settings.writeSettings("disableDifficulties", String.valueOf(disableOption));
            Settings.writeSettings("difficultyFilter", excludedDifficulties.toString().replaceAll("\\[", "").replaceAll("]", "").replaceAll(",{4}", ","));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CheckboxButton createButton(String text, int y) {

        CheckboxButton button = new CheckboxButton(text);
        button.setBounds(25, y, 365, 30);
        button.setUI(defaultUI);
        button.setForeground(Defaults.FOREGROUND);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFont(Defaults.MAIN_FONT.deriveFont(14f));
        button.refresh();
        return button;
    }

    public static void refreshUI() {
        defaultUI.setBackground(Defaults.MAIN);
        defaultUI.setHover(Defaults.BUTTON_HOVER);
        defaultUI.setSelect(Defaults.SELECT);
        difficultyPanel.setBackground(Defaults.TOP);
        panel.setBackground(Defaults.SUB_MAIN);
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton) {
                for (Component component2 : ((JButton) component).getComponents()) {
                    if (component2 instanceof JLabel) {
                        component2.setForeground(Defaults.FOREGROUND);
                    }
                }
                component.setBackground(Defaults.MAIN);
            }
            if (component instanceof JLabel) {
                component.setForeground(Defaults.FOREGROUND);

            }
            if (component instanceof JTextArea) {
                ((FancyTextArea) component).refreshAll();
            }
            if (component instanceof CheckboxButton) {
                ((CheckboxButton) component).refresh();
            }
        }
        for (Component component : difficultyPanel.getComponents()) {
            if (component instanceof JButton) {
                for (Component component2 : ((JButton) component).getComponents()) {
                    if (component2 instanceof JLabel) {
                        component2.setForeground(Defaults.FOREGROUND);
                    }
                }
                component.setBackground(Defaults.MAIN);
            }
            if (component instanceof JLabel) {
                component.setForeground(Defaults.FOREGROUND);

            }
            if (component instanceof JTextArea) {
                ((FancyTextArea) component).refreshAll();
            }
            if (component instanceof CheckboxButton) {
                ((CheckboxButton) component).refresh();
            }
        }
    }
}
