package Main;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.*;

public class Language {

	public static String lang = "en_us";
	static Path myPath;

	static {
		try {
			URI uri = Main.class.getResource("/Resources/Languages/").toURI();
			if (uri.getScheme().equals("jar")) {
				myPath = ServerChatBot.fileSystem.getPath("/Resources/Languages/");
			} else {
				myPath = Paths.get(uri);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String setLocale(String text) {
		String newText = text;
		String[] words = newText.split(" ");
		for (int i = 0; i < words.length; i++) {
			if (words[i].startsWith("$") && words[i].endsWith("$")) {
				String newWord = Language.getString(words[i].replace("$", ""));
				newText = newText.replace(words[i], newWord);
			}
		}
		return newText;
	}

	public static String getString(String identifier) {
		try {
			Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/Languages/");
			if (Files.exists(comPath)) {
				Stream<Path> walk1 = Files.walk(comPath, 1);
				for (Iterator<Path> it = walk1.iterator(); it.hasNext(); ) {
					Path path = it.next();
					String[] file = path.toString().split("\\\\");
					String fileName = file[file.length - 1];
					if (fileName.equals(lang + ".lang")) {
						Scanner sc = new Scanner(path.toFile());
						while (sc.hasNextLine()) {
							String line = sc.nextLine();
							if (line.startsWith("//")) {
								continue;
							}
							if (line.split("=", 2)[0].trim().equals(identifier)) {
								sc.close();
								return line.split("=", 2)[1].trim();
							}
						}
						sc.close();
					}
				}
			}
			Stream<Path> walk = Files.walk(myPath, 1);
			for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
				Path path = it.next();

				String[] file;
				if(ServerChatBot.uri.getScheme().equals("jar")) {
					file = path.toString().split("/");
				}
				else {
					file = path.toString().split("\\\\");
				}
				String fileName = file[file.length - 1];
				if (fileName.equals(lang + ".lang")) {
					if(ServerChatBot.uri.getScheme().equals("jar")) {
						InputStream is = Main.class
								.getClassLoader().getResourceAsStream(path.toString().substring(1));
						InputStreamReader isr = new InputStreamReader(is);
						BufferedReader br = new BufferedReader(isr);

						String line;
						while ((line = br.readLine()) != null) {
							if (line.startsWith("//")) {
								continue;
							}
							if (line.split("=", 2)[0].trim().equals(identifier)) {

								is.close();
								isr.close();
								br.close();
								return line.split("=", 2)[1].trim();
							}
						}
						is.close();
						isr.close();
						br.close();
					}
					else {
						Scanner sc = null;
						try {
							sc = new Scanner(path);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						String line;
						while (sc.hasNextLine()) {
							line = sc.nextLine();
							if (line.split("=", 2)[0].trim().equals(identifier)) {
								return line.split("=", 2)[1].trim();
							}
						}
						sc.close();
					}
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return identifier;
	}

	public static void startFileChangeListener() {
		new Thread(() -> {
			try {
				WatchService watcher = FileSystems.getDefault().newWatchService();
				Path dir = Paths.get(Defaults.saveDirectory + "/GDBoard/Languages");
				dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

				while (true) {
					WatchKey key;
					try {
						key = watcher.take();
					} catch (InterruptedException ex) {
						return;
					}

					for (WatchEvent<?> event : key.pollEvents()) {
						WatchEvent.Kind<?> kind = event.kind();

						WatchEvent<Path> ev = (WatchEvent<Path>) event;
						Path fileName = ev.context();

						if (kind == ENTRY_MODIFY && fileName.toString().equals(lang + ".lang")) {
							for(int i = 0; i < LangButton.buttonList.size(); i++){
								LangButton.buttonList.get(i).refreshLocale();
							}
							for(int i = 0; i < LangLabel.labelList.size(); i++){
								LangLabel.labelList.get(i).refreshLocale();
							}
							for(int i = 0; i < RoundedJButton.buttons.size(); i++){
								RoundedJButton.buttons.get(i).refreshLocale();
							}
							for(int i = 0; i < CurvedButtonAlt.buttonList.size(); i++){
								CurvedButtonAlt.buttonList.get(i).refreshLocale();
							}
							Windowed.refreshButtons();
						}
					}

					boolean valid = key.reset();
					if (!valid) {
						break;
					}
				}
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}).start();
	}
}