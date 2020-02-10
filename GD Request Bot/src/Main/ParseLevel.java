package Main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

class ParseLevel {

    static void parse(byte[] level, String levelID) {

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
                            File file = new File("blockedWords.txt");
                            Scanner sc = new Scanner(file);


                            out:
                            while (sc.hasNextLine()) {
                                String[] text = lvlObject.get(i).getObjectText().toUpperCase().split(" ");
                                String word = sc.nextLine();

                                for (String s : text) {

                                    if (s.equalsIgnoreCase(word)) {
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

                            sc.close();

                        }
                    }
                    System.out.println("Analyzed");
                    Requests.getLevelData().get(k).setAnalyzed();
                    LevelsWindow.updateUI(Requests.getLevelData().get(k).getLevelID(), Requests.getLevelData().get(k).getContainsVulgar(), Requests.getLevelData().get(k).getContainsImage(), true);
                    break;


                } catch (Exception e) {
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
