package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Overlay {

    // --------------------
    // Create JFrame Object

    public static JFrame frame = new JFrame();
    private static JLayeredPane mainFrame = new JLayeredPane();
    static boolean isVisible = true;

    static void setFrame() {

        // --------------------
        // default frame stuff


        frame.setFocusableWindowState(false);


        if (!Settings.windowedMode) {
            frame.setUndecorated(true);

            frame.setBackground(new Color(0, 0, 0, 100));
            frame.setBounds(Defaults.screenSize);
            frame.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    isVisible = false;
                    setWindowsInvisible();

                }
            });
        } else {
            frame.setTitle("GDBoard (Beta)");
            BufferedImage img = null;
            try {
                    img = ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader()
                        .getResource("Resources/Icons/windowIcon.png")));

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setIconImage(img);
            frame.setSize(new Dimension(720, 554));
            frame.setResizable(false);
            frame.getContentPane().setBackground(Defaults.SUB_MAIN);
        }
        frame.setLayout(null);

        mainFrame.setDoubleBuffered(true);
        mainFrame.setBounds(0, 0, Defaults.screenSize.width, Defaults.screenSize.height);
        mainFrame.setBackground(new Color(0, 0, 0, 0));
        mainFrame.setLayout(null);
        frame.toBack();
        frame.add(mainFrame);

    }

    static void addToFrame(JComponent component) {

        // --------------------
        // Add components to JFrame from elsewhere

        mainFrame.add(component, 0);
        mainFrame.updateUI();

        // --------------------
    }

    static void moveToFront(JComponent component) {
        mainFrame.moveToFront(component);

    }

    static void alwaysFront(JComponent component) {
        mainFrame.setLayer(component, 2);
    }

    static void setVisible() {
        frame.setVisible(true);
        MainBar.setTooltips();
        frame.setAlwaysOnTop(true);
    }

    static void refreshUI(boolean color) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Settings.windowedMode) {

            frame.getContentPane().setBackground(Defaults.SUB_MAIN);
        } else {
            mainFrame.setBounds(0, 0, Defaults.screenSize.width, Defaults.screenSize.height);
            frame.setBounds(Defaults.screenSize);
        }
        frame.invalidate();
        frame.revalidate();
        SettingsWindow.refreshUI();
        LevelsWindow.refreshUI();
        InfoWindow.refreshUI();
        CommentsWindow.refreshUI();
        ActionsWindow.refreshUI();
        SongWindow.refreshUI();
        MainBar.refreshUI(color);

    }
    public static JFrame getWindow(){
        return frame;
    }
    static void setWindowsInvisible() {
        isVisible = false;
        frame.setBackground(new Color(0, 0, 0, 0));
        CommentsWindow.setInvisible();
        ActionsWindow.setInvisible();
        InfoWindow.setInvisible();
        LevelsWindow.setInvisible();
        SongWindow.setInvisible();
        SettingsWindow.setInvisible();
        MainBar.setInvisible();
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    static void setWindowsVisible() {
        isVisible = true;
        frame.setBackground(new Color(0, 0, 0, 100));
        CommentsWindow.setVisible();
        ActionsWindow.setVisible();
        InfoWindow.setVisible();
        LevelsWindow.setVisible();
        SongWindow.setVisible();
        SettingsWindow.setVisible();
        MainBar.setVisible();
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

}

