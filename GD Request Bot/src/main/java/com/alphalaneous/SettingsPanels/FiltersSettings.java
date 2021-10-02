package com.alphalaneous.SettingsPanels;

import com.alphalaneous.Assets;
import com.alphalaneous.Components.SettingsComponent;
import com.alphalaneous.Components.SettingsPage;
import com.alphalaneous.Components.SmoothScrollPane;
import com.alphalaneous.Defaults;
import com.alphalaneous.Settings;
import com.alphalaneous.ThemedComponents.ThemedIconCheckbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class FiltersSettings {

    public static ArrayList<String> excludedDifficulties = new ArrayList<>();
    public static ArrayList<String> excludedLengths = new ArrayList<>();

    private static final JPanel difficultyPanel = new JPanel();
    private static final JPanel lengthPanel = new JPanel();

    private static final JPanel difficultyPanelWithScroll = new JPanel(new GridLayout(1,1,0,0)){
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            g.setColor(getBackground());

            RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHints(qualityHints);
            g2.fillRoundRect(0, 0, getSize().width, getSize().height, 20, 20);


            super.paintComponent(g);
        }
    };

    private static final JPanel lengthPanelWithScroll = new JPanel(){
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g.setColor(getBackground());
            RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHints(qualityHints);
            g2.fillRoundRect(0, 0, getSize().width, getSize().height, 20, 20);
            super.paintComponent(g);
        }
    };


    private static final SmoothScrollPane difficultyPanelScroll = new SmoothScrollPane(difficultyPanel);
    private static final SmoothScrollPane lengthPanelScroll = new SmoothScrollPane(lengthPanel);


    private static final ThemedIconCheckbox naIcon = createDifficultyButton("NA");
    private static final ThemedIconCheckbox autoIcon = createDifficultyButton("auto");
    private static final ThemedIconCheckbox easyIcon = createDifficultyButton("easy");
    private static final ThemedIconCheckbox normalIcon = createDifficultyButton("normal");
    private static final ThemedIconCheckbox hardIcon = createDifficultyButton("hard");
    private static final ThemedIconCheckbox harderIcon = createDifficultyButton("harder");
    private static final ThemedIconCheckbox insaneIcon = createDifficultyButton("insane");
    private static final ThemedIconCheckbox easyDemonIcon = createDifficultyButton("easy demon");
    private static final ThemedIconCheckbox mediumDemonIcon = createDifficultyButton("medium demon");
    private static final ThemedIconCheckbox hardDemonIcon = createDifficultyButton("hard demon");
    private static final ThemedIconCheckbox insaneDemonIcon = createDifficultyButton("insane demon");
    private static final ThemedIconCheckbox extremeDemonIcon = createDifficultyButton("extreme demon");

    private static final ThemedIconCheckbox tinyIcon = createLengthButton("Tiny");
    private static final ThemedIconCheckbox shortIcon = createLengthButton("Short");
    private static final ThemedIconCheckbox mediumIcon = createLengthButton("Medium");
    private static final ThemedIconCheckbox longIcon = createLengthButton("Long");
    private static final ThemedIconCheckbox XLIcon = createLengthButton("XL");


    public static JPanel createPanel() {
        SettingsPage settingsPage = new SettingsPage("$FILTERS_SETTINGS$");
        settingsPage.addCheckbox("$RATED_LEVELS_ONLY$", "", "rated");
        settingsPage.addCheckbox("$UNRATED_LEVELS_ONLY$", "", "unrated");
        settingsPage.addCheckbox("$DISABLE_SELECTED_DIFFICULTIES$", "", "disableDifficulties");

        difficultyPanel.setBackground(Defaults.COLOR6);

        difficultyPanel.add(naIcon);
        difficultyPanel.add(autoIcon);
        difficultyPanel.add(easyIcon);
        difficultyPanel.add(normalIcon);
        difficultyPanel.add(hardIcon);
        difficultyPanel.add(harderIcon);
        difficultyPanel.add(insaneIcon);
        difficultyPanel.add(easyDemonIcon);
        difficultyPanel.add(mediumDemonIcon);
        difficultyPanel.add(hardDemonIcon);
        difficultyPanel.add(insaneDemonIcon);
        difficultyPanel.add(extremeDemonIcon);

        difficultyPanelScroll.setVerticalScrollEnabled(false);
        difficultyPanelScroll.setHorizontalScrollEnabled(true);


        difficultyPanelWithScroll.setPreferredSize(new Dimension(500,80));
        difficultyPanelWithScroll.setBackground(Defaults.COLOR6);
        difficultyPanelWithScroll.add(difficultyPanelScroll);


        SettingsComponent difficultyComponent = new SettingsComponent(difficultyPanelWithScroll, new Dimension(700,80)){
            @Override
            protected void resizeComponent(Dimension dimension){
                setPreferredSize(new Dimension(dimension.width-300,getPreferredSize().height));
                difficultyPanelScroll.setPreferredSize(new Dimension(dimension.width-340,difficultyPanelWithScroll.getPreferredSize().height));
                difficultyPanelWithScroll.setPreferredSize(new Dimension(dimension.width-340,difficultyPanelWithScroll.getPreferredSize().height));
                difficultyPanelWithScroll.setBounds(30,0,difficultyPanelWithScroll.getPreferredSize().width, difficultyPanelWithScroll.getPreferredSize().height);
            }
            @Override
            protected void refreshUI(){
                difficultyPanelWithScroll.setBackground(Defaults.COLOR6);
            }
        };
        difficultyPanelScroll.setOpaque(false);
        difficultyPanel.setOpaque(false);
        difficultyPanelWithScroll.setOpaque(false);
        difficultyPanelScroll.getViewport().setOpaque(false);
        difficultyComponent.setOpaque(false);
        settingsPage.addComponent(difficultyComponent);

        lengthPanel.setBackground(Defaults.COLOR6);

        lengthPanel.add(tinyIcon);
        lengthPanel.add(shortIcon);
        lengthPanel.add(mediumIcon);
        lengthPanel.add(longIcon);
        lengthPanel.add(XLIcon);


        lengthPanelWithScroll.setPreferredSize(new Dimension(500,80));
        lengthPanelWithScroll.setBackground(Defaults.COLOR6);
        lengthPanelWithScroll.add(lengthPanelScroll);
        lengthPanelScroll.setVerticalScrollEnabled(false);
        lengthPanelScroll.setHorizontalScrollEnabled(true);

        SettingsComponent lengthComponent = new SettingsComponent(lengthPanelWithScroll, new Dimension(700,80)){
            @Override
            protected void resizeComponent(Dimension dimension){
                setPreferredSize(new Dimension(dimension.width-300,getPreferredSize().height));
                lengthPanelScroll.setPreferredSize(new Dimension(dimension.width-340,lengthPanelScroll.getPreferredSize().height));
                lengthPanelWithScroll.setPreferredSize(new Dimension(dimension.width-340,lengthPanelScroll.getPreferredSize().height));
                lengthPanelWithScroll.setBounds(30,0,lengthPanelScroll.getPreferredSize().width, lengthPanelScroll.getPreferredSize().height);
            }
            @Override
            protected void refreshUI(){
                lengthPanelWithScroll.setBackground(Defaults.COLOR6);
            }
        };

        lengthPanelScroll.setOpaque(false);
        lengthPanel.setOpaque(false);
        lengthComponent.setOpaque(false);
        lengthPanelWithScroll.setOpaque(false);
        lengthPanelScroll.getViewport().setOpaque(false);

        settingsPage.addCheckbox("$DISABLE_SELECTED_LENGTHS$", "", "disableLengths");

        settingsPage.addComponent(lengthComponent);

        settingsPage.addCheckedInput("$MINIMUM_LIKES$", "", 1, true, true, false, "minLikesOption", "minLikes");
        settingsPage.addCheckedInput("$MAXIMUM_LIKES$", "", 1, true, true, false, "maxLikesOption", "maxLikes");
        settingsPage.addCheckedInput("$MINIMUM_OBJECTS$", "", 1, true, false, false, "minObjectsOption", "minObjects");
        settingsPage.addCheckedInput("$MAXIMUM_OBJECTS$", "", 1, true, false, false, "maxObjectsOption", "maxObjects");
        settingsPage.addCheckedInput("$MINIMUM_ID$", "", 1, true, false, false, "minIDOption", "minID");
        settingsPage.addCheckedInput("$MAXIMUM_ID$", "", 1, true, false, false, "maxIDOption", "maxID");

        loadDifficultiesAndLengths();
        return settingsPage;
    }
    private static ThemedIconCheckbox createDifficultyButton(String difficulty) {
        ImageIcon imageIcon = Assets.difficultyIconsNormal.get(difficulty);

        ThemedIconCheckbox checkbox = createIconCheckbox(imageIcon,30);
        checkbox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (checkbox.getSelectedState()) excludedDifficulties.add(difficulty.toLowerCase());
                else excludedDifficulties.remove(difficulty.toLowerCase());
                Settings.writeSettings("difficultyFilter", excludedDifficulties.toString());
            }
        });
        return checkbox;
    }

    private static ThemedIconCheckbox createLengthButton(String length) {
        ImageIcon imageIcon = Assets.lengthIcons.get(length);

        ThemedIconCheckbox checkbox = createIconCheckbox(imageIcon,16);
        checkbox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (checkbox.getSelectedState()) excludedLengths.add(length.toLowerCase());
                else excludedLengths.remove(length.toLowerCase());
                Settings.writeSettings("lengthFilter", excludedLengths.toString());
            }
        });
        return checkbox;
    }

    private static ThemedIconCheckbox createIconCheckbox(ImageIcon imageIcon, int height) {

        double ratio = imageIcon.getIconWidth()/(double)imageIcon.getIconHeight();
        if(ratio < 0){
            ratio = imageIcon.getIconHeight()/(double)imageIcon.getIconWidth();
        }
        int newWidth = (int) (ratio * height);

        Icon icon = new ImageIcon(imageIcon.getImage().getScaledInstance(newWidth, height, Image.SCALE_SMOOTH));
        //Icon icon = new ImageIcon(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

        ThemedIconCheckbox button = new ThemedIconCheckbox(icon);
        button.setForeground(Defaults.FOREGROUND_A);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFont(Defaults.MAIN_FONT.deriveFont(14f));
        button.setPreferredSize(new Dimension(icon.getIconWidth()+6,height+50));
        button.refresh();
        return button;
    }
    public static void loadDifficultiesAndLengths() {
        String excludedDifficultiesString = Settings.getSettings("difficultyFilter").asString();
        String excludedLengthsString = Settings.getSettings("lengthFilter").asString();
        if(excludedDifficultiesString.length() != 0) {
            excludedDifficulties = new ArrayList<>(Arrays.asList(excludedDifficultiesString.substring(1, excludedDifficultiesString.length() - 1).split(", ")));
        }
        if(excludedLengthsString.length() != 0) {
            excludedLengths = new ArrayList<>(Arrays.asList(excludedLengthsString.substring(1, excludedLengthsString.length() - 1).split(", ")));
        }

        if (excludedLengths.contains("tiny")) {
            tinyIcon.setChecked(true);
        }
        if (excludedLengths.contains("short")) {
            shortIcon.setChecked(true);
        }
        if (excludedLengths.contains("medium")) {
            mediumIcon.setChecked(true);
        }
        if (excludedLengths.contains("long")) {
            longIcon.setChecked(true);
        }
        if (excludedLengths.contains("xl")) {
            XLIcon.setChecked(true);
        }
        if (excludedDifficulties.contains("na")) {
            naIcon.setChecked(true);
        }
        if (excludedDifficulties.contains("auto")) {
            autoIcon.setChecked(true);
        }
        if (excludedDifficulties.contains("easy")) {
            easyIcon.setChecked(true);
        }
        if (excludedDifficulties.contains("normal")) {
            normalIcon.setChecked(true);
        }
        if (excludedDifficulties.contains("hard")) {
            hardIcon.setChecked(true);
        }
        if (excludedDifficulties.contains("harder")) {
            harderIcon.setChecked(true);
        }
        if (excludedDifficulties.contains("insane")) {
            insaneIcon.setChecked(true);
        }
        if (excludedDifficulties.contains("easy demon")) {
            easyDemonIcon.setChecked(true);
        }
        if (excludedDifficulties.contains("medium demon")) {
            mediumDemonIcon.setChecked(true);
        }
        if (excludedDifficulties.contains("hard demon")) {
            hardDemonIcon.setChecked(true);
        }
        if (excludedDifficulties.contains("insane demon")) {
            insaneDemonIcon.setChecked(true);
        }
        if (excludedDifficulties.contains("extreme demon")) {
            extremeDemonIcon.setChecked(true);
        }
    }

}
