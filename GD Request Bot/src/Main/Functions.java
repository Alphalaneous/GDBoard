package Main;

import SettingsPanels.BlockedSettings;
import SettingsPanels.GeneralSettings;
import SettingsPanels.OutputSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.nio.file.*;
import java.util.Random;

public class Functions {

    public static void skipFunction(){

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
                if (Requests.levels.get(LevelsWindow.getSelectedID()).getSongName().equalsIgnoreCase("Custom") && Requests.levels.get(LevelsWindow.getSelectedID()).getNotPersist()) {
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
        OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, 0));
        LevelsWindow.setOneSelect();

        SongWindow.refreshInfo();
        InfoWindow.refreshInfo();
    }

    public static void randomFunction(){
        Random random = new Random();
        int num = 0;
        try {
            num = random.nextInt(Requests.levels.size() - 2) + 1;
        } catch (Exception ignored) {

        }

        if (Requests.levels.size() != 0) {

            if (Requests.levels.get(LevelsWindow.getSelectedID()).getSongName().equalsIgnoreCase("Custom") && Requests.levels.get(LevelsWindow.getSelectedID()).getNotPersist()) {
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
        OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, num));
        SongWindow.refreshInfo();
        InfoWindow.refreshInfo();
    }

    public static void copyFunction(){
        if (Requests.levels.size() != 0) {
            StringSelection selection = new StringSelection(
                    Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }

    public static void blockFunction(){
        if (Requests.levels.size() != 0) {
            SettingsWindow.run = false;
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
            SettingsWindow.run = true;
        }
        LevelsWindow.setOneSelect();
    }

    public static void clearFunction(){
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
            SettingsWindow.run = true;
        }
    }

    public static void requestsToggleFunction(){
        if (MainBar.requests) {
            MainBar.stopReqs.setText("\uE768");
            MainBar.requests = false;
            Main.sendMessage("/me Requests are now off!");

        } else {
            MainBar.stopReqs.setText("\uE71A");
            MainBar.requests = true;
            Main.sendMessage("/me Requests are now on!");


        }
    }

}
