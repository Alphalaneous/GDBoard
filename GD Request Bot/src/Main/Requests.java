package Main;

import SettingsPanels.GeneralSettings;
import SettingsPanels.OutputSettings;
import SettingsPanels.RequestSettings;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.GDLevel;
import com.github.alex1304.jdash.exception.MissingAccessException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.GZIPInputStream;

class Requests {

    static ArrayList<LevelData> levels = new ArrayList<>();
    private static HashMap<String, Integer> userStreamLimitMap = new HashMap<>();

    static void addRequest(String ID, String requester, boolean isCustomSong, String customUrl) {
        if (MainBar.requests) {
            if (GeneralSettings.queueLimitBoolean && (levels.size() >= GeneralSettings.queueLimit)) {
                System.out.println(GeneralSettings.queueLimit + ", " + (levels.size()));
                Main.sendMessage("@" + requester + " The queue is full!");
                return;
            }
            if (GeneralSettings.userLimitOption) {
                int size = 0;
                for (LevelData level : levels) {
                    if (level.getRequester().equalsIgnoreCase(requester)) {
                        size++;
                    }
                }
                if (size >= GeneralSettings.userLimit) {
                    Main.sendMessage("@" + requester + " You have the maximum amount of levels in the queue!");
                    return;
                }
            }

            if (GeneralSettings.userLimitStreamOption) {
                if (userStreamLimitMap.containsKey(requester)) {
                    if (userStreamLimitMap.get(requester) >= GeneralSettings.userLimitStream) {
                        Main.sendMessage("@" + requester + " You've reached the maximum amount of levels for the stream!");
                        return;
                    }
                }
            }
            if (userStreamLimitMap.containsKey(requester)) {
                userStreamLimitMap.put(requester, userStreamLimitMap.get(requester) + 1);
            } else {
                userStreamLimitMap.put(requester, 1);
            }
            System.out.println(userStreamLimitMap.get(requester));
            try {
                AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
                GDLevel level = null;
                boolean goThrough = true;
                boolean valid = true;
                try {
                    level = client.getLevelById(Integer.parseInt(ID)).block();
                } catch (MissingAccessException | NumberFormatException e) {
                    Main.sendMessage("@" + requester + " That level ID doesn't exist!");
                    goThrough = false;
                } catch (Exception e) {
                    Main.sendMessage("@" + requester + " Level search failed... (Servers down?)");
                    goThrough = false;
                }
                LevelData levelData = new LevelData();
                // --------------------
                Thread parse;
                if (goThrough) {
                    if (level != null && RequestSettings.ratedOption && !(level.getStars() > 0)) {
                        Main.sendMessage("@" + requester + " Please send star rated levels only!");
                        return;
                    }
                    levelData.setRequester(requester);
                    assert level != null;
                    levelData.setAuthor(level.getCreatorName());
                    levelData.setName(level.getName());
                    levelData.setDifficulty(level.getDifficulty().toString());
                    levelData.setDescription(level.getDescription());
                    levelData.setLikes(String.valueOf(level.getLikes()));
                    levelData.setDownloads(String.valueOf(level.getDownloads()));
                    levelData.setSongURL(Objects.requireNonNull(level.getSong().block()).getDownloadURL());
                    levelData.setLength(level.getLength().toString());
                    //levelData.setLength(level.getLength().toString());
                    if (level.isDemon()) {
                        if (level.getDifficulty().toString().equalsIgnoreCase("EASY")) {
                            levelData.setDifficulty("easy demon");
                        } else if (level.getDifficulty().toString().equalsIgnoreCase("NORMAL")) {
                            levelData.setDifficulty("medium demon");
                        } else if (level.getDifficulty().toString().equalsIgnoreCase("HARD")) {
                            levelData.setDifficulty("hard demon");
                        } else if (level.getDifficulty().toString().equalsIgnoreCase("HARDER")) {
                            levelData.setDifficulty("insane demon");
                        } else if (level.getDifficulty().toString().equalsIgnoreCase("INSANE")) {
                            levelData.setDifficulty("extreme demon");
                        }
                    }
                    levelData.setLevelID(ID);
                    levelData.setVersion(level.getGameVersion());
                    //levelData.setCoins(String.valueOf(level.getCoinCount()));
                    //levelData.setVerifiedCoins(level.hasCoinsVerified());
                    if (level.getFeaturedScore() > 0) {
                        levelData.setFeatured();
                    }
                    levelData.setEpic(level.isEpic());

                    levelData.setSongID(String.valueOf(Objects.requireNonNull(level.getSong().block()).getId()));
                    //levelData.setSongSize(String.valueOf(level.getSong().block().getSongSize()));
                    levelData.setStars(level.getStars());
                    GDLevel finalLevel = level;
                    parse = new Thread(() -> {
                        try {
                            if (!(finalLevel.getStars() > 0) && finalLevel.getGameVersion()/10 >= 2) {
                                parse(Objects.requireNonNull(Objects
                                        .requireNonNull(client.getLevelById(Long.parseLong(ID)).block()).download().block())
                                        .getData(), ID
                                );
                                LevelsWindow.updateUI(levelData.getLevelID(), levelData.getContainsVulgar(), levelData.getContainsImage(), levelData.getAnalyzed());
                            }

                        } catch (IllegalArgumentException e) {
                            JOptionPane.showMessageDialog(null, "There was an error analyzing " + levelData.getLevelID() + "!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    parse.start();
                    // --------------------
                    // Loop through levels to check if level is already in queue

                    for (int k = 0; k < levels.size(); k++) {

                        if (levelData.getLevelID().equals(levels.get(k).getLevelID())) {
                            int j = k + 1;
                            Main.sendMessage(
                                    "@" + levelData.getRequester() + " Level is already in the queue at position " + j + "!");
                            System.out.println("Level Already Exists");
                            valid = false;
                            break;
                        }
                    }

                    File file = new File(System.getenv("APPDATA") + "\\GDBoard\\blocked.txt");
                    if (file.exists()) {
                        Scanner sc = new Scanner(file);

                        while (sc.hasNextLine()) {
                            if (levelData.getLevelID().equals(sc.nextLine())) {
                                Main.sendMessage("@" + levelData.getRequester() + " That Level is Blocked!");
                                System.out.println("Blocked ID");
                                valid = false;
                                break;
                            }
                        }
                        sc.close();
                    }
                    if (valid) {
                        // --------------------
                        // Adds level to queue array "levels" and refreshes LevelsWindow
                        levels.add(levelData);
                        if (GeneralSettings.autoDownloadOption) {
                            URL url1;
                            String fileName = null;
                            if (customUrl != null) {
                                url1 = new URL(customUrl);
                                fileName = FilenameUtils.getName(url1.getPath());
                                System.out.println(fileName);
                            }
                            String finalFileName = fileName;
                            Thread thread = new Thread(() -> {
                                System.out.println(levelData.getSongID());
                                try {
                                    File folder = new File(System.getenv("LOCALAPPDATA") + "\\GeometryDash");
                                    boolean download = true;
                                    String[] files = folder.list();
                                    if (files != null) {
                                        if (files.length > 0) {
                                            for (String file2 : files) {
                                                if (file2.equalsIgnoreCase(levelData.getSongID() + ".mp3")) {
                                                    if (!isCustomSong) {
                                                        download = false;
                                                    } else if (Requests.levels.size() == 1) {

                                                        Path song = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3");
                                                        Files.move(song, song.resolveSibling(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ".mp3.temp"), StandardCopyOption.REPLACE_EXISTING);
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (download && !levelData.getSongID().equalsIgnoreCase("0")) {
                                        File file2;
                                        if (isCustomSong) {
                                            if (Requests.levels.size() == 1) {
                                                file2 = new File(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + levelData.getSongID() + ".mp3");
                                            } else {
                                                file2 = new File(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + levelData.getSongID() + ".mp3.wait");
                                            }
                                        } else {
                                            file2 = new File(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + levelData.getSongID() + ".mp3");
                                        }
                                        URL url;
                                        assert finalFileName != null;
                                        if (isCustomSong && !finalFileName.endsWith(".mp3")) {
                                            url = new URL(levelData.getSongURL());
                                            Main.sendMessage("@" + requester + ", Invalid song! (requires .mp3 format)");
                                        } else if (isCustomSong && finalFileName.endsWith(".mp3")) {
                                            try {
                                                url = new URL(customUrl);
                                            } catch (MalformedURLException e) {
                                                url = new URL(levelData.getSongURL());
                                                Main.sendMessage("@" + requester + " Invalid URL!");
                                            }
                                        } else {
                                            url = new URL(levelData.getSongURL());
                                        }

                                        FileUtils.copyURLToFile(url, file2);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            thread.start();
                        }
                        Main.sendMessage("@" + levelData.getRequester() + " " + levelData.getName() + " ("
                                + levelData.getLevelID() + ") has been added to the queue at position " + levels.size() + "!");
                        if (levels.size() == 1) {
                            StringSelection selection = new StringSelection(Requests.levels.get(0).getLevelID());
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            clipboard.setContents(selection, selection);
                            if (!GeneralSettings.nowPlayingOption) {
                                Main.sendMessage("Now Playing " + Requests.levels.get(0).getName() + " ("
                                        + Requests.levels.get(0).getLevelID() + "). Requested by "
                                        + Requests.levels.get(0).getRequester());
                            }
                        }
                        if (isCustomSong && GeneralSettings.autoDownloadOption) {
                            levelData.setSongName("Custom");
                            levelData.setSongAuthor("");
                        } else {
                            levelData.setSongName(Objects.requireNonNull(level.getSong().block()).getSongTitle());
                            System.out.println(Objects.requireNonNull(level.getSong().block()).getSongTitle());
                            levelData.setSongAuthor(Objects.requireNonNull(level.getSong().block()).getSongAuthorName());

                        }
                        OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, 0));
                        LevelsWindow.createButton(levelData.getName(), levelData.getAuthor(), levelData.getLevelID(), levelData.getDifficulty(), levelData.getEpic(), levelData.getFeatured(), levelData.getStars(), levelData.getRequester(), levelData.getVersion());

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(Overlay.frame, e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            Main.sendMessage("@" + requester + " Requests are off!");
        }
    }

    private static ArrayList<LevelData> getLevelData() {
        return levels;
    }

    private static void parse(byte[] level, String levelID) {
        all : for (int k = 0; k < Requests.getLevelData().size(); k++) {
            if (Requests.getLevelData().get(k).getLevelID().equalsIgnoreCase(levelID)) {
                String decompressed = null;
                try {
                    decompressed = decompress(level);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert decompressed != null;
                int imageIDCount = 0;
                String color = "";
                String[] values = decompressed.split(";");
                for (String value1 : values) {
                    if (value1.startsWith("1,1110") || value1.startsWith("1,211") || value1.startsWith("1,914")) {
                        String value = value1.replaceAll("(,[^,]*),", "$1;");

                        String[] attributes = value.split(";");
                        double scale = 0;
                        boolean hsv = false;
                        String tempColor = "";
                        String text = "";
                        for (String attribute : attributes) {

                            if (attribute.startsWith("32")) {
                                scale = Double.parseDouble(attribute.split(",")[1]);
                            }
                            if (attribute.startsWith("41")) {
                                hsv = true;
                            }
                            if (attribute.startsWith("21")) {
                                tempColor = attribute.split(",")[1];
                            }
                            if (attribute.startsWith("31")) {
                                String formatted = attribute.split(",")[1].replace("_", "/").replace("-", "+");
                                text = new String(Base64.getDecoder().decode(formatted));
                            }
                        }
                        InputStream is = Main.class.getClassLoader()
                                .getResourceAsStream("Resources/blockedWords.txt");
                        assert is != null;
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String line;

                        try {
                            out: while ((line = br.readLine()) != null) {
                                String[] text1 = text.toUpperCase().split(" ");

                                for (String s : text1) {
                                    if (s.equalsIgnoreCase(line)) {
                                        System.out.println("Contains Vulgar");
                                        Requests.getLevelData().get(k).setContainsVulgar();
                                        break out;
                                    }
                                }
                            }
                            if (scale != 0.0 && hsv) {
                                if (tempColor.equalsIgnoreCase(color)) {
                                    imageIDCount++;
                                }
                            }
                            if (imageIDCount >= 3000) {

                                Requests.getLevelData().get(k).setContainsImage();
                            }
                           color = tempColor;
                        } catch (IOException e) {
                            e.printStackTrace();
                            LevelsWindow.updateUI(Requests.getLevelData().get(k).getLevelID(), Requests.getLevelData().get(k).getContainsVulgar(), Requests.getLevelData().get(k).getContainsImage(), false);
                            break all;
                        }
                    }
                }
                Requests.getLevelData().get(k).setAnalyzed();
                LevelsWindow.updateUI(Requests.getLevelData().get(k).getLevelID(), Requests.getLevelData().get(k).getContainsVulgar(), Requests.getLevelData().get(k).getContainsImage(), true);
            }
        }
    }

    private static String decompress(byte[] compressed) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(bis);
        BufferedReader br = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        gis.close();
        bis.close();
        return sb.toString();
    }

    static String parseInfoString(String text, int level) {
        if (Requests.levels.size() != 0) {
            text = text.replaceAll("(?i)%levelName%", levels.get(level).getName())
                    .replaceAll("(?i)%levelID%", levels.get(level).getLevelID())
                    .replaceAll("(?i)%levelAuthor%", levels.get(level).getAuthor())
                    .replaceAll("(?i)%requester%", levels.get(level).getRequester())
                    .replaceAll("(?i)%songName%", levels.get(level).getSongName())
                    .replaceAll("(?i)%songID%", levels.get(level).getSongID())
                    .replaceAll("(?i)%songArtist%", levels.get(level).getSongAuthor())
                    .replaceAll("(?i)%likes%", levels.get(level).getLikes())
                    .replaceAll("(?i)%downloads%", levels.get(level).getDownloads())
                    .replaceAll("(?i)%description%", levels.get(level).getDescription());
            return text;
        } else {
            return OutputSettings.noLevelString;
        }
    }
}
