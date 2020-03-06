package Main;

import SettingsPanels.BlockedSettings;
import SettingsPanels.GeneralSettings;
import com.jidesoft.swing.ResizablePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Random;

class ActionsWindow {

    private static int height = 60;
    private static int width = 300;
    private static ResizablePanel window = new InnerWindow("Actions", Settings.getActionsWLoc().x, Settings.getActionsWLoc().y, width, height,
            "\uE7C9").createPanel();
    private static JPanel mainPanel = new JPanel();
    private static JPanel panel = new JPanel();
    private static JButtonUI defaultUI = new JButtonUI();
    static boolean requests = true;

    //region Create Panel
    static void createPanel() {
        try {
            mainPanel.setBounds(1, 31, width, height);

            mainPanel.setLayout(null);

            defaultUI.setBackground(Defaults.BUTTON);
            defaultUI.setHover(Defaults.BUTTON_HOVER);

            panel.setPreferredSize(new Dimension(width - 25, height));
            panel.setBounds(10, 5, width - 20, height - 10);
            if (!Settings.windowedMode) {
                mainPanel.setBackground(Defaults.MAIN);
                panel.setBackground(Defaults.MAIN);
            } else {
                mainPanel.setBackground(Defaults.SUB_MAIN);
                panel.setBackground(Defaults.SUB_MAIN);
            }
            panel.setLayout(new GridLayout(1, 5, 10, 10));

            //TODO make custom Yes/No dialog

            //region Create Skip Button
            JButton skip = createButton("\uE101");
            skip.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    ((InnerWindow) window).moveToFront();
                    super.mousePressed(e);
                    if (Requests.levels.size() != 0) {


                        if (!(Requests.levels.size() <= 1) && LevelsWindow.getSelectedID() == 0) {
                            StringSelection selection = new StringSelection(
                                    Requests.levels.get(LevelsWindow.getSelectedID() + 1).getLevelID());
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            clipboard.setContents(selection, selection);
                        }
                        if (LevelsWindow.getSelectedID() == 0 && Requests.levels.size() > 1) {
                            Requests.levels.remove(LevelsWindow.getSelectedID());
                            LevelsWindow.removeButton();
                            if(GeneralSettings.autoDownloadOption) {
                                Path wait = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3.wait");
                                Path song = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3");
                                try {
                                    if (Files.exists(song)) {
                                        Files.move(song, song.resolveSibling(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3.temp"), StandardCopyOption.REPLACE_EXISTING);
                                    }
                                    if (Files.exists(wait)) {
                                        Files.move(wait, wait.resolveSibling(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3"), StandardCopyOption.REPLACE_EXISTING);
                                    }
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            if (!GeneralSettings.nowPlayingOption) {
                                Main.sendMessage("Now Playing " + Requests.levels.get(0).getName() + " ("
                                        + Requests.levels.get(0).getLevelID() + "). Requested by "
                                        + Requests.levels.get(0).getRequester());
                            }
                        } else {
                            if (Requests.levels.get(LevelsWindow.getSelectedID()).getSongName().equalsIgnoreCase("Custom") && !Requests.levels.get(LevelsWindow.getSelectedID()).getPersist()) {
                                if(GeneralSettings.autoDownloadOption) {
                                    Path tempSong = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3.temp");
                                    Path remove = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3");
                                    try {
                                        if (Files.exists(remove)) {
                                            Files.delete(remove);
                                        }
                                        if (Files.exists(tempSong)) {
                                            Files.move(tempSong, tempSong.resolveSibling(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3"), StandardCopyOption.REPLACE_EXISTING);
                                        }
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                            Requests.levels.remove(LevelsWindow.getSelectedID());
                            LevelsWindow.removeButton();

                        }
                        Thread thread = new Thread(() -> {
                            CommentsWindow.unloadComments(true);
                            if (Requests.levels.size() != 0) {
                                CommentsWindow.loadComments(0, false);
                            }
                        });
                        thread.start();

                    }
                    LevelsWindow.setOneSelect();

                    SongWindow.refreshInfo();
                    InfoWindow.refreshInfo();
                }
            });
            panel.add(skip);
            //endregion

            //region Create Random Button
            Random random = new Random();
            JButton randNext = createButton("\uE897");
            randNext.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    ((InnerWindow) window).moveToFront();
                    super.mousePressed(e);
                    int num = 0;
                    try {
                        num = random.nextInt(Requests.levels.size() - 2) + 1;
                    } catch (Exception ignored) {

                    }
                    if (Requests.levels.size() != 0) {

                        if (Requests.levels.get(LevelsWindow.getSelectedID()).getSongName().equalsIgnoreCase("Custom") && !Requests.levels.get(LevelsWindow.getSelectedID()).getPersist()) {
                            if(GeneralSettings.autoDownloadOption) {
                                Path tempSong = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3.temp");
                                Path remove = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3");
                                Path wait = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3.wait");
                                try {
                                    if (Files.exists(remove)) {
                                        Files.delete(remove);
                                    }
                                    if (Files.exists(wait)) {
                                        Files.delete(wait);
                                    }
                                    if (Files.exists(tempSong)) {
                                        Files.move(tempSong, tempSong.resolveSibling(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3"), StandardCopyOption.REPLACE_EXISTING);
                                    }
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                        Requests.levels.remove(LevelsWindow.getSelectedID());
                        LevelsWindow.removeButton();


                        if (Requests.levels.size() == 1) {
                            LevelsWindow.setOneSelect();
                        } else {
                            LevelsWindow.setSelect(num);
                        }
                        Thread thread = new Thread(() -> {
                            CommentsWindow.unloadComments(true);
                            if (Requests.levels.size() != 0) {
                                CommentsWindow.loadComments(0, false);
                            }
                        });
                        thread.start();
                        if (Requests.levels.size() != 0) {
                            System.out.println(num);
                            StringSelection selection = new StringSelection(
                                    Requests.levels.get(num).getLevelID());
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            clipboard.setContents(selection, selection);
                            if(GeneralSettings.autoDownloadOption) {
                                Path wait = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3.wait");
                                Path song = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3");
                                try {
                                    if (Files.exists(song)) {
                                        Files.move(song, song.resolveSibling(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3.temp"), StandardCopyOption.REPLACE_EXISTING);
                                    }
                                    if (Files.exists(wait)) {
                                        Files.move(wait, wait.resolveSibling(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3"), StandardCopyOption.REPLACE_EXISTING);
                                    }
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            if (!GeneralSettings.nowPlayingOption) {
                                Main.sendMessage("Now Playing " + Requests.levels.get(num).getName() + " ("
                                        + Requests.levels.get(num).getLevelID() + "). Requested by "
                                        + Requests.levels.get(num).getRequester());
                            }
                        }
                    }
                    SongWindow.refreshInfo();
                    InfoWindow.refreshInfo();
                }
            });
            panel.add(randNext);
            //endregion

            //region Create Copy Button
            JButton copy = createButton("\uF0E3");
            copy.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    ((InnerWindow) window).moveToFront();
                    super.mousePressed(e);
                    if (Requests.levels.size() != 0) {
                        StringSelection selection = new StringSelection(
                                Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID());
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(selection, selection);
                    }
                }
            });
            panel.add(copy);
            //endregion

            //region Create Block Button
            JButton block = createButton("\uE8F8");
            block.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    ((InnerWindow) window).moveToFront();
                    super.mousePressed(e);
                    if (Requests.levels.size() != 0) {
                        Object[] options = {"Yes", "No"};
                        int n = JOptionPane.showOptionDialog(Overlay.frame,
                                "Block " + Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID() + "?",
                                "Block ID? (Temporary Menu)", JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                        if (n == 0) {
                            BlockedSettings.removeID(Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID());
                            Path file = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\blocked.txt");

                            try {
                                if(!Files.exists(file)){
                                    Files.createFile(file);
                                }
                                Files.write(
                                        file,
                                        (Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID() + "\n").getBytes(),
                                        StandardOpenOption.APPEND);
                            } catch (IOException e1) {
                                JOptionPane.showMessageDialog(null, "There was an error writing to the file!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            Requests.levels.remove(LevelsWindow.getSelectedID());
                            LevelsWindow.removeButton();

                            Thread thread = new Thread(() -> {
                                CommentsWindow.unloadComments(true);
                                if (Requests.levels.size() > 0) {
                                    CommentsWindow.loadComments(0, false);
                                }
                            });
                            thread.start();
                        }
                        SongWindow.refreshInfo();
                        InfoWindow.refreshInfo();
                    }
                    LevelsWindow.setOneSelect();

                }
            });
            panel.add(block);
            //endregion

            //region Create Clear Button
            JButton clear = createButton("\uE107");
            clear.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    ((InnerWindow) window).moveToFront();
                    super.mousePressed(e);
                    Object[] options = {"Yes", "No"};
                    int n = JOptionPane.showOptionDialog(Overlay.frame,
                            "Clear the queue?",
                            "Clear? (Temporary Menu)", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                    if (n == 0) {
                        if (Requests.levels.size() != 0) {
                            for (int i = 0; i < Requests.levels.size(); i++) {
                                if (Requests.levels.get(i).getSongName().equalsIgnoreCase("Custom")) {
                                    if (GeneralSettings.autoDownloadOption) {
                                        Path tempSong = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(i).getSongID() + ".mp3.temp");
                                        Path remove = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(i).getSongID() + ".mp3");
                                        try {
                                            Files.delete(remove);
                                            Files.move(tempSong, tempSong.resolveSibling(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(i).getSongID() + ".mp3"), StandardCopyOption.REPLACE_EXISTING);
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                                LevelsWindow.removeButton();
                            }
                            Requests.levels.clear();
                            SongWindow.refreshInfo();
                            InfoWindow.refreshInfo();
                            CommentsWindow.unloadComments(true);
                        }
                        LevelsWindow.setOneSelect();
                    }
                }
            });
            panel.add(clear);
            //endregion
            mainPanel.add(panel);
            window.add(mainPanel);
            ((InnerWindow) window).refreshListener();
            Overlay.addToFrame(window);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //endregion

    //region Create Button
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
    //endregion

    //region Set Pin
    static void setPin(boolean pin) {
        ((InnerWindow) window).setPin(pin);
    }
    //endregion

    //region RefreshUI
    static void refreshUI() {
        ((InnerWindow) window).refreshUI();

        defaultUI.setHover(Defaults.BUTTON_HOVER);
        defaultUI.setSelect(Defaults.SELECT);
        if (!Settings.windowedMode) {
            defaultUI.setBackground(Defaults.BUTTON);
            mainPanel.setBackground(Defaults.MAIN);
            panel.setBackground(Defaults.MAIN);
        } else {
            defaultUI.setBackground(Defaults.MAIN);
            mainPanel.setBackground(Defaults.SUB_MAIN);
            panel.setBackground(Defaults.SUB_MAIN);
        }
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton) {
                if (!Settings.windowedMode) {
                    component.setBackground(Defaults.BUTTON);
                } else {
                    component.setBackground(Defaults.MAIN);
                }
                component.setForeground(Defaults.FOREGROUND);
            }
        }
    }
    //endregion

    //region Toggle Visible
    static void toggleVisible() {
        ((InnerWindow) window).toggle();
    }
    //endregion

    //region SetInvisible
    static void setInvisible() {
        ((InnerWindow) window).setInvisible();
    }
    //endregion

    //region SetVisible
    static void setVisible() {
        ((InnerWindow) window).setVisible();
    }
    //endregion

    //region SetLocation
    static void setLocation(Point point) {
        window.setLocation(point);
    }
    //endregion

    //region SetSettings
    static void setSettings() {
        ((InnerWindow) window).setSettings();
    }
    //endregion
}
