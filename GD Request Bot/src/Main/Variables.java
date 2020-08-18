package Main;

import Main.InnerWindows.LevelsWindow;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

public class Variables {

	private static Properties variables = new Properties();
	private static FileInputStream in;
	private static BufferedWriter writer;
	private static File file = new File(Defaults.saveDirectory + "\\GDBoard\\vars.board");

	static {
		while (true) {
			try {
				writer = new BufferedWriter(new FileWriter(file, true));
				in = new FileInputStream(file);
				break;
			} catch (IOException e) {
				file.mkdir();
				file.getParentFile().mkdir();
			}
		}
	}

	private static HashMap<String, Object> vars = new HashMap<>();

	static void loadVars() {
		Path path = Paths.get(Defaults.saveDirectory + "/GDBoard/vars.board");
		if (Files.exists(path)) {

			Scanner sc = null;
			try {
				sc = new Scanner(path.toFile());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if(line.contains("=")) {
					vars.put(line.split("=", 2)[0].trim(), line.split("=", 2)[1].trim());
				}
			}
			sc.close();
		}
	}

	static void saveVars() {
		Path file = Paths.get(Defaults.saveDirectory + "/GDBoard/vars.board");

		try {
			if (!Files.exists(file)) {
				Files.createFile(file);
			}
			Iterator it = vars.entrySet().iterator();
			StringBuilder pairs = new StringBuilder();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				pairs.append(pair.getKey() + " = " + pair.getValue() + "\n");
				it.remove();
			}
			if (!Files.exists(file)) {
				Files.createFile(file);
			}
			Files.write(
					file,
					pairs.toString().getBytes(),
					StandardOpenOption.APPEND);
		} catch (IOException e1) {
			DialogBox.showDialogBox("Error!", e1.toString(), "There was an error writing to the file!", new String[]{"OK"});

		}
	}

	public static void setVar(String name, Object object) {
		vars.put(name, object);
	}

	public static String getVar(String name) {
		return String.valueOf(vars.get(name));
	}

	public static void clearVars() throws IOException {
		vars.clear();
	}
}
