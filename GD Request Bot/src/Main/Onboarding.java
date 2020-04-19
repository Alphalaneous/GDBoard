package Main;

import SettingsPanels.ShortcutSettings;
import com.jidesoft.swing.ResizablePanel;
import org.jnativehook.keyboard.SwingKeyAdapter;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

public class Onboarding {
    static int width = 465;
    static int height = 512;
    private static ResizablePanel window = new InnerWindow("Startup", 0, 0, width - 2, height,
            "\uF137", true).createPanel();
    private static JPanel content = new JPanel(null);
    private static JButtonUI defaultUI = new JButtonUI();
    private static JPanel openPanel = createKeybindButton(110, "Open Keybind", "openKeybind");
    public static int openKeybind = 36;
    static JFrame frame = new JFrame();

    static void createPanel() {
        URL iconURL = Windowed.class.getResource("/Resources/Icons/windowIcon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        Image newIcon = icon.getImage().getScaledInstance(120, 120,  Image.SCALE_SMOOTH);
        frame.setIconImage(newIcon);
        frame.setTitle("GDBoard Startup");
        Onboarding.setLocation(new Point(Defaults.screenSize.width / 2 - width / 2, Defaults.screenSize.height / 2 - height / 2));
        frame.setUndecorated(true);
        frame.setSize(width, 700 + 32);
        frame.setPreferredSize(new Dimension(width, 700 + 32));
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setBackground(new Color(255, 255, 255, 0));
        frame.getContentPane().setSize(width, 700 + 32);
        frame.pack();

        content.setBounds(1, 31, width - 2, height);
        content.setBackground(Defaults.SUB_MAIN);
        content.setLayout(null);

        JTextPane mainText = new JTextPane();
        StyledDocument doc = mainText.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        mainText.setText("Thank You for using GDBoard! Here are a few \nthings to get you started!");
        mainText.setBounds(25, 20, width - 50, mainText.getPreferredSize().height + 15);
        mainText.setOpaque(false);
        mainText.setEditable(false);
        mainText.setForeground(Defaults.FOREGROUND);
        mainText.setBackground(new Color(0, 0, 0, 0));
        mainText.setFont(new Font("bahnschrift", Font.PLAIN, 18));

        JLabel keybindInfo = new JLabel("Set the keybind to open the GDBoard Overlay here!");
        keybindInfo.setFont(new Font("bahnschrift", Font.PLAIN, 12));
        keybindInfo.setBounds(25, 90, width - 50, keybindInfo.getPreferredSize().height + 5);
        keybindInfo.setForeground(Defaults.FOREGROUND);
        defaultUI.setBackground(Defaults.BUTTON);
        defaultUI.setHover(Defaults.BUTTON_HOVER);
        defaultUI.setSelect(Defaults.SELECT);

        JLabel authInfo = new JLabel("Press Next to Log In with Twitch and start GDBoard!");
        authInfo.setFont(new Font("bahnschrift", Font.PLAIN, 12));
        authInfo.setBounds(25, height - 80, width - 50, authInfo.getPreferredSize().height + 5);
        authInfo.setForeground(Defaults.FOREGROUND);
        CurvedButton moveOn = new CurvedButton("Click here if Success and GDBoard hasn't moved on");
        moveOn.setBackground(Defaults.BUTTON);
        moveOn.setBounds(25, height - 140, width - 55, 30);
        moveOn.setPreferredSize(new Dimension(width - 55, 30));
        moveOn.setUI(defaultUI);
        moveOn.setForeground(Defaults.FOREGROUND);
        moveOn.setBorder(BorderFactory.createEmptyBorder());
        moveOn.setFont(new Font("bahnschrift", Font.PLAIN, 14));
        moveOn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Onboarding.setInvisible();
                ShortcutSettings.loadKeybind("Open", openKeybind);
                try {
                    Settings.writeSettings("openKeybind", String.valueOf(openKeybind));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    Settings.writeSettings("onboarding", "false");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Main.starting = false;
            }
        });
        moveOn.refresh();
        moveOn.setVisible(false);
        CurvedButton button = new CurvedButton("Next");

        button.setBackground(Defaults.BUTTON);
        button.setBounds(25, height - 45, width - 55, 30);
        button.setPreferredSize(new Dimension(width - 55, 30));
        button.setUI(defaultUI);
        button.setForeground(Defaults.FOREGROUND);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFont(new Font("bahnschrift", Font.PLAIN, 14));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    moveOn.setVisible(true);
                    TwitchAPI.setOauth();
                    Thread thread = new Thread(() -> {
                        while (!TwitchAPI.success.get()) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                        Onboarding.setInvisible();
                        ShortcutSettings.loadKeybind("Open", openKeybind);
                        try {
                            Settings.writeSettings("openKeybind", String.valueOf(openKeybind));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            Settings.writeSettings("onboarding", "false");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        Main.starting = false;
                    });
                    thread.start();
                } catch (Exception ignored) {
                }
            }
        });
        button.refresh();

        content.add(mainText);
        content.add(openPanel);
        content.add(keybindInfo);
        content.add(authInfo);
        content.add(button);
        content.add(moveOn);
        window.add(content);
        ((InnerWindow) window).setPinVisible();
        ((InnerWindow) window).refreshListener();
        frame.add(window);
    }

    static void toFront() {
        frame.toFront();
    }

    static void refreshUI() {
        ((InnerWindow) window).refreshUI();
        defaultUI.setBackground(Defaults.BUTTON);
        defaultUI.setHover(Defaults.BUTTON_HOVER);
        defaultUI.setSelect(Defaults.SELECT);
        content.setBackground(Defaults.SUB_MAIN);
    }

    static void toggleVisible() {

        ((InnerWindow) window).toggle();
    }

    static void setInvisible() {
        ((InnerWindow) window).setInvisible();
    }

    static void setVisible() {
        ((InnerWindow) window).setVisible();

    }

    private static JButton createButton(String icon) {
        JButton button = new RoundedJButton(icon);
        button.setPreferredSize(new Dimension(50, 50));
        button.setUI(defaultUI);
        if (!Settings.windowedMode) {
            button.setBackground(Defaults.BUTTON);
        } else {
            button.setBackground(Defaults.MAIN);
        }
        button.setForeground(Defaults.FOREGROUND);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 20));
        return button;
    }

    //region SetLocation
    static void setLocation(Point point) {
        frame.setLocation(point);
    }

    //endregion
    private static JPanel createKeybindButton(int y, String text, String setting) {
        JPanel panel = new JPanel(null);
        panel.setBounds(0, y, width, 36);
        panel.setBackground(Defaults.SUB_MAIN);
        FancyTextArea input = new FancyTextArea(false);
        DefaultStyledDocument doc = new DefaultStyledDocument();
        input.setEditable(false);
        input.addKeyListener(new SwingKeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 8 || e.getKeyCode() == 16 || e.getKeyCode() == 17 || e.getKeyCode() == 18) {
                    if (text.equalsIgnoreCase("Open Keybind")) {
                        input.setText("Home");
                        try {
                            Settings.writeSettings(setting, String.valueOf(36));
                            ShortcutSettings.loadKeybind(text.split(" ")[0], 36);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        openKeybind = 36;
                    }
                } else {
                    input.setText(KeyEvent.getKeyText(e.getKeyCode()));
                    try {
                        Settings.writeSettings(setting, String.valueOf(e.getKeyCode()));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    openKeybind = e.getKeyCode();
                }
                panel.requestFocusInWindow();
            }
        });
        input.setBounds(width - 140, 1, 100, 32);

        input.setDocument(doc);

        JLabel keybindButton = new JLabel(text);
        keybindButton.setFont(new Font("bahnschrift", Font.PLAIN, 14));
        keybindButton.setBounds(25, 3, keybindButton.getPreferredSize().width + 5, keybindButton.getPreferredSize().height + 5);
        keybindButton.setForeground(Defaults.FOREGROUND);
        panel.add(keybindButton);
        panel.add(input);
        return panel;
    }

    static void loadSettings() throws IOException {

        if (!Settings.getSettings("openKeybind").equalsIgnoreCase("") && !Settings.getSettings("openKeybind").equalsIgnoreCase("-1")) {
            openKeybind = Integer.parseInt(Settings.getSettings("openKeybind"));
        }
        for (Component component : content.getComponents()) {
            if (component instanceof JPanel) {
                for (Component component1 : ((JPanel) component).getComponents()) {
                    if (component1 instanceof JLabel) {
                        if (((JLabel) component1).getText().equalsIgnoreCase("Open Keybind")) {
                            if (!KeyEvent.getKeyText(ShortcutSettings.openKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(openKeybind));
                            } else {
                                ((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
                            }
                        }
                    }
                }
            }
        }
    }
}
