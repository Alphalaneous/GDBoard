package SettingsPanels;

import Main.*;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.SwingKeyAdapter;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ShortcutSettings {
    private static JButtonUI defaultUI = new JButtonUI();
    private static JPanel panel = new JPanel();
    private static JPanel openPanel = createKeybindButton(25, "Open", "openKeybind");
    private static JPanel skipPanel = createKeybindButton(75, "Skip/Next", "skipKeybind");
    private static JPanel randPanel = createKeybindButton(125, "Random", "randomKeybind");
    private static JPanel copyPanel = createKeybindButton(175, "Copy", "copyKeybind");
    private static JPanel blockPanel = createKeybindButton(225, "Block", "blockKeybind");
    private static JPanel clearPanel = createKeybindButton(275, "Clear", "clearKeybind");
    private static JPanel lockPanel = createKeybindButton(325, "Lock Mouse to GD", "lockKeybind");


    public static int openKeybind = 36;
    public static int skipKeybind = 0;
    public static int randKeybind = 0;
    public static int copyKeybind = 0;
    public static int blockKeybind = 0;
    public static int clearKeybind = 0;
    public static int lockKeybind = 0;


    public static JPanel createPanel() {
        defaultUI.setBackground(Defaults.BUTTON);
        defaultUI.setHover(Defaults.HOVER);
        defaultUI.setSelect(Defaults.SELECT);

        panel.setDoubleBuffered(true);
        panel.setBounds(0, 0, 415, 622);
        panel.setBackground(Defaults.SUB_MAIN);
        panel.setLayout(null);

        panel.add(openPanel);
        panel.add(skipPanel);
        panel.add(randPanel);
        panel.add(copyPanel);
        panel.add(blockPanel);
        panel.add(clearPanel);
        //panel.add(lockPanel);

        return panel;

    }

    private static JPanel createKeybindButton(int y, String text, String setting) {
        final boolean[] isFocused = {false};
        JPanel panel = new JPanel(null);
        panel.setBounds(0, y, 415, 36);
        panel.setBackground(Defaults.SUB_MAIN);
        FancyTextArea input = new FancyTextArea(false);
        DefaultStyledDocument doc = new DefaultStyledDocument();
        input.setEditable(false);
        input.addKeyListener(new SwingKeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 8 || e.getKeyCode() == 16 || e.getKeyCode() == 17 || e.getKeyCode() == 18 || e.getKeyCode() == 10) {

                    if (text.equalsIgnoreCase("Open")) {
                        input.setText("Home");
                        try {
                            Settings.writeSettings(setting, String.valueOf(36));
                            loadKeybind(text, 36);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        input.setText("");
                        try {
                            Settings.writeSettings(setting, "-1");
                            loadKeybind(text, -1);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    input.setText(KeyEvent.getKeyText(e.getKeyCode()));
                    try {
                        Settings.writeSettings(setting, String.valueOf(e.getKeyCode()));
                        loadKeybind(text, e.getKeyCode());

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                panel.requestFocusInWindow();
            }
        });
        /*input.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(isFocused[0]){
                    if (!(e.getButton() < 4)) {
                        input.setText("Mouse " + e.getButton());
                    }
                }
            }
        });
        input.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused[0] = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused[0] = false;
            }
        });*/
        input.setBounds(285, 1, 100, 32);

        input.setDocument(doc);

        JLabel keybindButton = new JLabel(text);
        keybindButton.setFont(Defaults.MAIN_FONT.deriveFont(14f));
        keybindButton.setBounds(25, 3, keybindButton.getPreferredSize().width + 5, keybindButton.getPreferredSize().height + 5);
        keybindButton.setForeground(Defaults.FOREGROUND);

        CheckboxButton keybindCtrl = createButton("Control", 90, 1, 100);

        CheckboxButton keybindAlt = createButton("Alt", 110, 1, 100);
        panel.add(keybindButton);
        panel.add(input);
        return panel;
    }

    private static CheckboxButton createButton(String text, int x, int y, int width) {

        CheckboxButton button = new CheckboxButton(text);
        button.setBounds(25, y, width, 30);
        button.setUI(defaultUI);
        button.setForeground(Defaults.FOREGROUND);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFont(Defaults.MAIN_FONT.deriveFont(14f));
        button.refresh();
        return button;
    }

    public static void setSettings() {
        try {
            Settings.writeSettings("openKeybind", String.valueOf(openKeybind));
            Settings.writeSettings("skipKeybind", String.valueOf(skipKeybind));
            Settings.writeSettings("randomKeybind", String.valueOf(randKeybind));
            Settings.writeSettings("copyKeybind", String.valueOf(copyKeybind));
            Settings.writeSettings("blockKeybind", String.valueOf(blockKeybind));
            Settings.writeSettings("clearKeybind", String.valueOf(clearKeybind));
            Settings.writeSettings("lockKeybind", String.valueOf(lockKeybind));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadSettings() throws IOException {
        if(Settings.onboarding){
            openKeybind = Onboarding.openKeybind;
        }
        else if (!Settings.getSettings("openKeybind").equalsIgnoreCase("") && !Settings.getSettings("openKeybind").equalsIgnoreCase("-1")) {
            openKeybind = Integer.parseInt(Settings.getSettings("openKeybind"));
        }
        if (!Settings.getSettings("skipKeybind").equalsIgnoreCase("") && !Settings.getSettings("skipKeybind").equalsIgnoreCase("-1")) {
            skipKeybind = Integer.parseInt(Settings.getSettings("skipKeybind"));
        }
        if (!Settings.getSettings("randomKeybind").equalsIgnoreCase("") && !Settings.getSettings("randomKeybind").equalsIgnoreCase("-1")) {
            randKeybind = Integer.parseInt(Settings.getSettings("randomKeybind"));
        }
        if (!Settings.getSettings("copyKeybind").equalsIgnoreCase("") && !Settings.getSettings("copyKeybind").equalsIgnoreCase("-1")) {
            copyKeybind = Integer.parseInt(Settings.getSettings("copyKeybind"));
        }
        if (!Settings.getSettings("blockKeybind").equalsIgnoreCase("") && !Settings.getSettings("blockKeybind").equalsIgnoreCase("-1")) {
            blockKeybind = Integer.parseInt(Settings.getSettings("blockKeybind"));
        }
        if (!Settings.getSettings("clearKeybind").equalsIgnoreCase("") && !Settings.getSettings("clearKeybind").equalsIgnoreCase("-1")) {
            clearKeybind = Integer.parseInt(Settings.getSettings("clearKeybind"));
        }
        if (!Settings.getSettings("lockKeybind").equalsIgnoreCase("") && !Settings.getSettings("lockKeybind").equalsIgnoreCase("-1")) {
            lockKeybind = Integer.parseInt(Settings.getSettings("lockKeybind"));
        }

        for (Component component : panel.getComponents()) {
            if (component instanceof JPanel) {
                for (Component component1 : ((JPanel) component).getComponents()) {
                    if (component1 instanceof JLabel) {
                        if (((JLabel) component1).getText().equalsIgnoreCase("Open")) {
                            if (!KeyEvent.getKeyText(openKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(openKeybind));
                            } else {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
                            }
                        }
                        if (((JLabel) component1).getText().equalsIgnoreCase("Skip/Next")) {
                            if (!KeyEvent.getKeyText(skipKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(skipKeybind));
                            } else {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
                            }
                        }
                        if (((JLabel) component1).getText().equalsIgnoreCase("Random")) {
                            if (!KeyEvent.getKeyText(randKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(randKeybind));
                            } else {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
                            }
                        }
                        if (((JLabel) component1).getText().equalsIgnoreCase("Copy")) {
                            if (!KeyEvent.getKeyText(copyKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(copyKeybind));
                            } else {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
                            }
                        }
                        if (((JLabel) component1).getText().equalsIgnoreCase("Block")) {
                            if (!KeyEvent.getKeyText(blockKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(blockKeybind));
                            } else {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
                            }
                        }
                        if (((JLabel) component1).getText().equalsIgnoreCase("Clear")) {
                            if (!KeyEvent.getKeyText(clearKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(clearKeybind));
                            } else {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
                            }

                        }
                        if (((JLabel) component1).getText().equalsIgnoreCase("Lock to GD")) {
                            if (!KeyEvent.getKeyText(lockKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(lockKeybind));
                            } else {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
                            }

                        }
                    }
                }
            }
        }
    }

    public static void loadKeybind(String setting, int keybind) {
        if (setting.equalsIgnoreCase("Open")) {
            openKeybind = keybind;
        }
        if (setting.equalsIgnoreCase("Skip/Next")) {
            skipKeybind = keybind;
        }
        if (setting.equalsIgnoreCase("Random")) {
            randKeybind = keybind;
        }
        if (setting.equalsIgnoreCase("Copy")) {
            copyKeybind = keybind;
        }
        if (setting.equalsIgnoreCase("Block")) {
            blockKeybind = keybind;
        }
        if (setting.equalsIgnoreCase("Clear")) {
            clearKeybind = keybind;
        }
        if (setting.equalsIgnoreCase("Lock Mouse to GD")) {
            lockKeybind = keybind;
        }
    }

    public static void refreshUI() {
        defaultUI.setBackground(Defaults.MAIN);
        defaultUI.setHover(Defaults.BUTTON_HOVER);
        defaultUI.setSelect(Defaults.SELECT);

        panel.setBackground(Defaults.SUB_MAIN);
        for (Component component : panel.getComponents()) {
            if (component instanceof JPanel) {
                for (Component component2 : ((JPanel) component).getComponents()) {
                    if (component2 instanceof JButton) {
                        for (Component component3 : ((JButton) component2).getComponents()) {
                            if (component3 instanceof JLabel) {
                                component3.setForeground(Defaults.FOREGROUND);
                            }
                        }
                        component2.setBackground(Defaults.MAIN);
                    }
                    if (component2 instanceof JLabel) {
                        component2.setForeground(Defaults.FOREGROUND);

                    }
                    if (component2 instanceof JTextArea) {
                        ((FancyTextArea) component2).refreshAll();
                    }

                }
                component.setBackground(Defaults.SUB_MAIN);
            }
        }
    }
}