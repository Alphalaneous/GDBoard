package Main;

import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.GDLevel;
import com.github.alex1304.jdash.exception.MissingAccessException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

class Requests {

    static ArrayList<LevelData> levels = new ArrayList<>();
    private static AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();

    static void addRequest(String ID, String requester, boolean isCustomSong) throws IOException {
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
            //levelData.setCoins(String.valueOf(level.getCoinCount()));
            //levelData.setVerifiedCoins(level.hasCoinsVerified());
            if (level.getFeaturedScore() > 0) {
                levelData.setFeatured();
            }
            levelData.setEpic(level.isEpic());
            if (isCustomSong) {
                levelData.setSongName("Custom");
                levelData.setSongAuthor("");
            } else {
                levelData.setSongName(Objects.requireNonNull(level.getSong().block()).getSongTitle());
                levelData.setSongAuthor(Objects.requireNonNull(level.getSong().block()).getSongAuthorName());

            }
            levelData.setSongID(String.valueOf(Objects.requireNonNull(level.getSong().block()).getId()));
            //levelData.setSongSize(String.valueOf(level.getSong().block().getSongSize()));
            levelData.setStars(level.getStars());
            GDLevel finalLevel = level;
            parse = new Thread(() -> {
                try {
                    if (!(finalLevel.getStars() > 0)) {
                        parse(Objects.requireNonNull(Objects
                                .requireNonNull(client.getLevelById(Long.parseLong(ID)).block()).download().block())
                                .getData(), ID);
                        //levelData.setAnalyzed();

                        LevelsWindow.updateUI(levelData.getLevelID(), levelData.getContainsVulgar(), levelData.getContainsImage(), levelData.getAnalyzed());
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    System.out.println("Couldn't Analyze Level");
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
            if(file.exists()) {
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
                Main.sendMessage("@" + levelData.getRequester() + " " + levelData.getName() + " ("
                        + levelData.getLevelID() + ") has been added to the queue at position " + levels.size() + "!");
                if (levels.size() == 1) {
                    StringSelection selection = new StringSelection(Requests.levels.get(0).getLevelID());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, selection);
                    Main.sendMessage("Now Playing " + Requests.levels.get(0).getName() + " ("
                            + Requests.levels.get(0).getLevelID() + "). Requested by "
                            + Requests.levels.get(0).getRequester());
                }
                LevelsWindow.createButton(levelData.getName(), levelData.getAuthor(), levelData.getLevelID(), levelData.getDifficulty(), levelData.getEpic(), levelData.getFeatured(), levelData.getStars(), levelData.getRequester());

            }
        }
    }
    private static ArrayList<LevelData> getLevelData() {
        return levels;
    }


    private static void parse(byte[] level, String levelID) {

        ArrayList<GDObject> lvlObject = new ArrayList<>();
        for (int k = 0; k < Requests.getLevelData().size(); k++) {

            if (Requests.getLevelData().get(k).getLevelID().equalsIgnoreCase(levelID)) {
                try {
                    String decompressed = decompress(level);

                    ArrayList<String[]> Objects = new ArrayList<>();

                    String[] values = decompressed.split(";");
                    for (String value : values) {
                        if ((value.split(",")[1].equalsIgnoreCase("1110")) || (value.split(",")[1].equalsIgnoreCase("211"))
                                || (value.split(",")[1].equalsIgnoreCase("914"))) {
                            Objects.add(value.split(","));
                        }
                    }
                    int IDImageCount = 0;

                    outer:
                    for (int i = 0; i < Objects.size(); i++) {
                        lvlObject.add(new GDObject());
                        for (int j = 0; j < Objects.get(i).length; j = j + 2) {
                            if (Objects.get(i)[j].equals("1")) {
                                lvlObject.get(i).ID(Double.parseDouble(Objects.get(i)[j + 1]));
                            }
                            if (Objects.get(i)[j].equals("21")) {
                                lvlObject.get(i).color1(Double.parseDouble(Objects.get(i)[j + 1]));
                            }
                            if (Objects.get(i)[j].equals("31")) {
                                String formatted = Objects.get(i)[j + 1].replace("_", "/").replace("-", "+");
                                // System.out.println(formatted);
                                String text = new String(Base64.getDecoder().decode(formatted));
                                lvlObject.get(i).objectText(text);
                            }
                            if (Objects.get(i)[j].equals("32")) {
                                lvlObject.get(i).scaling(Double.parseDouble(Objects.get(i)[j + 1]));
                            }
                            if (Objects.get(i)[j].equals("41")) {
                                lvlObject.get(i).color1HSVEnabled(Double.parseDouble(Objects.get(i)[j + 1]));
                            }
                            InputStream is = Main.class.getClassLoader()
                                    .getResourceAsStream("Resources/blockedWords.txt");
                            assert is != null;
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);
                           String line;
                            out:
                            while ((line = br.readLine()) != null) {
                                String[] text = lvlObject.get(i).getObjectText().toUpperCase().split(" ");

                                for (String s : text) {
                                    System.out.println(text);
                                    if (s.equalsIgnoreCase(line)) {
                                        System.out.println("Contains Vulgar");
                                        Requests.getLevelData().get(k).setContainsVulgar();
                                        break out;
                                    }
                                }

                            }
                            if (lvlObject.get(i).getID() == 1110 || lvlObject.get(i).getID() == 211
                                    && lvlObject.get(i).getScaling() <= 0.1 && lvlObject.get(i).getScaling() != 0.0
                                    && lvlObject.get(i).getColor1HSVEnabled() == 1) {
                                double color = lvlObject.get(i).getColor1();
                                if (lvlObject.get(i).getColor1() == color) {
                                    IDImageCount++;
                                }
                            }
                            if (IDImageCount >= 3000) {
                                System.out.println("Contains Image Hack");

                                Requests.getLevelData().get(k).setContainsImage();
                                break outer;
                            }
                            isr.close();
                            br.close();

                        }
                    }
                    Requests.getLevelData().get(k).setAnalyzed();
                    LevelsWindow.updateUI(Requests.getLevelData().get(k).getLevelID(), Requests.getLevelData().get(k).getContainsVulgar(), Requests.getLevelData().get(k).getContainsImage(), true);
                    break;


                } catch (Exception e) {
                    e.printStackTrace();
                    for (int m = 0; k < Requests.getLevelData().size(); m++) {
                        if (Requests.getLevelData().get(m).getLevelID().equalsIgnoreCase(levelID)) {
                            LevelsWindow.updateUI(Requests.getLevelData().get(k).getLevelID(), Requests.getLevelData().get(k).getContainsVulgar(), Requests.getLevelData().get(k).getContainsImage(), false);
                            break;
                        }
                    }
                }
            }
        }
        lvlObject.clear();
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
}
