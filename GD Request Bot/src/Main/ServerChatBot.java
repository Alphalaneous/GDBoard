package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ServerChatBot {
    static boolean processing = false;
    private static URI uri;
    static Path myPath;
    static {
        try {
            uri = Main.class.getResource("/Resources/Commands/").toURI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileSystem fileSystem;

    static {
        try {
            fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uri.getScheme().equals("jar")) {
            myPath = fileSystem.getPath("/Resources/Commands/");
        } else {
            myPath = Paths.get(uri);
        }

    }

    private static ArrayList<String> comCooldown = new ArrayList<>();
    static void onMessage(String user, String message, boolean isMod, boolean isSub, int cheer) {
        boolean whisper = false;
        processing = true;
        String com = message.split(" ")[0];
        String[] arguments = message.split(" ");
        String response = "";
        Matcher m = Pattern.compile("\\s*(\\d{6,})\\s*").matcher(message);
        if (m.find() && !message.startsWith("!")) {
            try {
                String[] msgs = message.split(" ");
                String mention = "";
                for (String s : msgs) {
                    if (s.contains("@")) {
                        mention = s;
                        break;
                    }
                }
                if (!mention.contains(m.group(1))) {
                    Requests.addRequest(m.group(1), String.valueOf(user));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                if (com.equalsIgnoreCase("!sudo") && (isMod || user.equalsIgnoreCase("Alphalaneous"))) {
                    if (arguments[2].startsWith("!")) {
                        user = arguments[1].toLowerCase();
                        com = arguments[2];
                        arguments = Arrays.copyOfRange(arguments, 2, arguments.length);
                        isMod = true;
                        isSub = true;
                    }
                }
                if (Files.exists(Paths.get(System.getenv("APPDATA") + "/GDBoard/disable.txt"))) {
                    Scanner sc2 = new Scanner(Paths.get(System.getenv("APPDATA") + "/GDBoard/disable.txt").toFile());
                    while (sc2.hasNextLine()) {
                        String line = sc2.nextLine();
                        if (line.equalsIgnoreCase(com)) {
							sc2.close();
                            return;
                        }
                    }
                    sc2.close();
                }
                if (Files.exists(Paths.get(System.getenv("APPDATA") + "/GDBoard/mod.txt"))) {
                    Scanner sc2 = new Scanner(Paths.get(System.getenv("APPDATA") + "/GDBoard/mod.txt").toFile());
                    while (sc2.hasNextLine()) {
                        String line = sc2.nextLine();
                        if (line.equalsIgnoreCase(com) && !isMod) {
							sc2.close();
							return;
                        }
                    }
                    sc2.close();
                }
                else {
                    InputStream is = Main.class
                            .getClassLoader().getResourceAsStream("Resources/Commands/mod.txt");
                    assert is != null;
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.equalsIgnoreCase(com) && !isMod) {
							is.close();
							isr.close();
							br.close();
                            return;
                        }
                    }
					is.close();
					isr.close();
					br.close();
                }
                boolean whisperExists = false;
                if (Files.exists(Paths.get(System.getenv("APPDATA") + "/GDBoard/whisper.txt"))) {
                    Scanner sc2 = new Scanner(Paths.get(System.getenv("APPDATA") + "/GDBoard/whisper.txt").toFile());
                    while (sc2.hasNextLine()) {
                        String line = sc2.nextLine();
                        if (line.equalsIgnoreCase(com)) {
                            whisperExists = true;
                            whisper = true;
                            break;
                        }
                    }
                    sc2.close();
                }
                if (!whisperExists) {
                    InputStream is = Main.class
                            .getClassLoader().getResourceAsStream("Resources/Commands/whisper.txt");
                    assert is != null;
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.equalsIgnoreCase(com)) {
                            whisper = true;
                            break;
                        }
                    }
                    is.close();
                    isr.close();
                    br.close();
                }
                boolean aliasesExist = false;
                if (Files.exists(Paths.get(System.getenv("APPDATA") + "/GDBoard/commands/aliases.txt"))) {
                    Scanner sc2 = new Scanner(Paths.get(System.getenv("APPDATA") + "/GDBoard/commands/aliases.txt").toFile());
                    while (sc2.hasNextLine()) {
                        String line = sc2.nextLine();
                        if (line.split("=")[0].replace(" ", "").equalsIgnoreCase(com)) {
                            aliasesExist = true;
                            com = line.split("=")[1].replace(" ", "");
                            break;
                        }
                    }
                    sc2.close();
                }
                if (!aliasesExist) {
                    InputStream is = Main.class
                            .getClassLoader().getResourceAsStream("Resources/Commands/aliases.txt");
                    assert is != null;
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.split("=")[0].replace(" ", "").equalsIgnoreCase(com)) {
                            com = line.split("=")[1].replace(" ", "");
                            break;
                        }
                    }
                    is.close();
                    isr.close();
                    br.close();
                }
                if (comCooldown.contains(com)) {
                    System.out.println("cooldown");
                    processing = false;
                    return;
                }
                int cooldown = 0;
                boolean coolExists = false;
                if (Files.exists(Paths.get(System.getenv("APPDATA") + "/GDBoard/commands/cooldown.txt"))) {
                    Scanner sc3 = new Scanner(Paths.get(System.getenv("APPDATA") + "/GDBoard/commands/cooldown.txt").toFile());
                    while (sc3.hasNextLine()) {
                        String line = sc3.nextLine();
                        if (line.split("=")[0].replace(" ", "").equalsIgnoreCase(com)) {
                            coolExists = true;
                            cooldown = Integer.parseInt(line.split("=")[1].replace(" ", ""));
                            break;
                        }
                    }
                    sc3.close();
                }
                if (!coolExists) {
                    InputStream is = Main.class
                            .getClassLoader().getResourceAsStream("Resources/Commands/cooldown.txt");
                    assert is != null;
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.split("=")[0].replace(" ", "").equalsIgnoreCase(com)) {
                            cooldown = Integer.parseInt(line.split("=")[1].replace(" ", ""));
                            break;
                        }
                    }
                    is.close();
                    isr.close();
                    br.close();
                }
                if (cooldown > 0) {
                    String finalCom = com;
                    int finalCooldown = cooldown;
                    Thread thread = new Thread(() -> {
                        comCooldown.add(finalCom);
                        try {
                            Thread.sleep(finalCooldown);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        comCooldown.remove(finalCom);
                    });
                    thread.start();
                }
                boolean comExists = false;


                Path comPath = Paths.get(System.getenv("APPDATA") + "/GDBoard/commands/");
                if (Files.exists(comPath)) {
                    Stream<Path> walk1 = Files.walk(comPath, 1);
                    for (Iterator<Path> it = walk1.iterator(); it.hasNext(); ) {
                        Path path = it.next();
                        String[] file = path.toString().split("\\\\");
                        String fileName = file[file.length - 1];
                        if (fileName.equalsIgnoreCase(com + ".js")) {
                            comExists = true;
                            response = Command.run(user, isMod, isSub, arguments, Files.readString(path, StandardCharsets.UTF_8), cheer);

                        }
                    }
                }

                if (!comExists) {

                    Stream<Path> walk = Files.walk(myPath, 1);
                    for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
                        Path path = it.next();
                        String[] file = path.toString().split("/");
                        String fileName = file[file.length - 1];
                        System.out.println(path.toString());
                        if (fileName.equalsIgnoreCase(com + ".js")) {

                            InputStream is = Main.class
                                    .getClassLoader().getResourceAsStream(path.toString().substring(1));
                            assert is != null;
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);
                            StringBuilder function = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null) {
                                function.append(line);
                            }
                            is.close();
                            isr.close();
                            br.close();
                            response = Command.run(user, isMod, isSub, arguments, function.toString(), cheer);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (response != null) {
                Main.sendMessage(response, whisper, user);
            }


        }
        processing = false;
    }
}
