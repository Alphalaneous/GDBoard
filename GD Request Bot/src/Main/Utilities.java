package Main;

import Main.SettingsPanels.ChannelPointSettings;
import Main.SettingsPanels.CommandSettings;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Scanner;

public class Utilities {


	public static void addCommand(String username, String... args){
		String newCommandName = args[0].toString();
		String message = "";

		for(String msg : args){
			message = message + " " + msg;
		}
		message = message.replaceFirst(args[0], "").trim();
		String command;
		if(message.startsWith("eval:")){
			command = "function command(){" + message.replace("eval:", "").trim() + "}";
		}
		else{
			command = "function command() { return \"" + message.toString() + "\";}";
		}
		Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\Commands\\" + newCommandName +".js");
		if (!Files.exists(file)) {
			try {
				if(!Files.exists(Paths.get(Defaults.saveDirectory + "\\GDBoard\\Commands\\"))) {
					Files.createDirectory(Paths.get(Defaults.saveDirectory + "\\GDBoard\\Commands\\"));
				}
				Files.createFile(file);
				Files.write(file, command.getBytes());
				Main.sendMessage(Utilities.format("$COMMAND_ADDED_SUCCESS$", username, newCommandName));
				CommandSettings.refresh();
				saveOption(newCommandName, "commands", "ADVANCED_EDITOR");
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		else{
			Main.sendMessage(Utilities.format("$COMMAND_EXISTS$", username));
		}
	}
	public static void editCommand(String username, String... args){
		String newCommandName = args[0].toString();
		String message = "";

		for(String msg : args){
			message = message + " " + msg;
		}
		message = message.replaceFirst(args[0], "").trim();
		String command;
		if(message.startsWith("eval:")){
			command = "function command(){" + message.replace("eval:", "").trim() + "}";
		}
		else{
			command = "function command() { return \"" + message.toString() + "\";}";
		}
		Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\Commands\\" + newCommandName +".js");
		if (Files.exists(file)) {
			try {
				Files.write(file, command.getBytes());
				Main.sendMessage(Utilities.format("$COMMAND_EDIT_SUCCESS$", username, newCommandName));
				saveOption(newCommandName, "commands", "ADVANCED_EDITOR");

			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		else{
			Main.sendMessage(Utilities.format("$COMMAND_DOESNT_EXIST$", username));
		}
	}
	public static void deleteCommand(String username, String command){
		Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\Commands\\" + command +".js");
		if (Files.exists(file)) {
			try {
				Files.delete(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Main.sendMessage(Utilities.format("$COMMAND_DELETE_SUCCESS$", username, command));
			CommandSettings.refresh();
			deleteCommandA(command, "commands", "SEND_MESSAGE");
		}
		else{
			Main.sendMessage(Utilities.format("$COMMAND_DOESNT_EXIST$", username, command));

		}
	}
	public static void addPoints(String username, String... args){
		String newCommandName = args[0].toString();
		String message = "";

		for(String msg : args){
			message = message + " " + msg;
		}
		message = message.replaceFirst(args[0], "").trim();
		String command;
		if(message.startsWith("eval:")){
			command = "function command(){" + message.replace("eval:", "").trim() + "}";
		}
		else{
			command = "function command() { return \"" + message.toString() + "\";}";
		}
		Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\Points\\" + newCommandName +".js");
		if (!Files.exists(file)) {
			try {
				if(!Files.exists(Paths.get(Defaults.saveDirectory + "\\GDBoard\\Points\\"))) {
					Files.createDirectory(Paths.get(Defaults.saveDirectory + "\\GDBoard\\Points\\"));
				}
				Files.createFile(file);
				Files.write(file, command.getBytes());
				Main.sendMessage(Utilities.format("$POINTS_ADDED_SUCCESS$", username, newCommandName));
				ChannelPointSettings.refresh();
				saveOption(newCommandName, "points", "ADVANCED_EDITOR");

			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		else{
			Main.sendMessage(Utilities.format("$POINTS_EXISTS$", username));
		}
	}
	public static void editPoints(String username, String... args){
		String newCommandName = args[0].toString();
		String message = "";

		for(String msg : args){
			message = message + " " + msg;
		}
		message = message.replaceFirst(args[0], "").trim();
		String command;
		if(message.startsWith("eval:")){
			command = "function command(){" + message.replace("eval:", "").trim() + "}";
		}
		else{
			command = "function command() { return \"" + message.toString() + "\";}";
		}
		Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\Points\\" + newCommandName +".js");
		if (Files.exists(file)) {
			try {
				Files.write(file, command.getBytes());
				Main.sendMessage(Utilities.format("$POINTS_EDIT_SUCCESS$", username, newCommandName));
				saveOption(newCommandName, "points", "ADVANCED_EDITOR");

			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		else{
			Main.sendMessage(Utilities.format("$POINTS_DOESNT_EXIST$", username));
		}
	}
	public static void deletePoints(String username, String command){
		Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\Points\\" + command +".js");
		if (Files.exists(file)) {
			try {
				Files.delete(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Main.sendMessage(Utilities.format("$POINTS_DELETE_SUCCESS$", username, command));
			ChannelPointSettings.refresh();
			deleteCommandA(command, "points", "SEND_MESSAGE");
		}
		else{
			Main.sendMessage(Utilities.format("$POINTS_DOESNT_EXIST$", username, command));

		}
	}
	public static String fetchURL(String url){
		return APIs.fetchURL(url);
	}
	public static String format(String format, Object... args){
		String[] words = format.split(" ");
		for(int i = 0; i < words.length; i++){
			if(words[i].startsWith("$") && words[i].endsWith("$")){
				String newWord = Language.getString(words[i].replaceAll("\\$", ""));
				format = format.replace(words[i], newWord);
			}
		}
		return String.format(format, args);
	}
	public static void openLink(String link){
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec("rundll32 url.dll,FileProtocolHandler " + link);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void openSteamApp(int id){
		try {
			Desktop.getDesktop().browse(new URI("steam://rungameid/" + id));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	public static void runProgram(String location){

		String[] loc = location.split("\\\\");
		StringBuilder dir = new StringBuilder();
		for(int i = 0; i < loc.length-1; i++ ) {
			dir.append(loc[i] + "\\\\");
		}
		try {
			Runtime.getRuntime().exec(new String[]{location}, null, new File(dir.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void runCommand(String... cmdArray){
		try {
			Runtime.getRuntime().exec(cmdArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void runCommand(String[] args, String[] envp, String fileDirectory){
		try {
			Runtime.getRuntime().exec(args, envp, new File(fileDirectory));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String findProgram(String search){
		try {
			Process getInstalled = Runtime.getRuntime().exec(Defaults.saveDirectory + "\\GDBoard\\bin\\getInstalledPrograms.bat " + search);
			BufferedReader installed = new BufferedReader(new InputStreamReader(getInstalled.getInputStream()));
			String install;
			String exe = null;
			int count = 0;
			while(true) {
				install = installed.readLine();
				if(install == null) {
					break;
				}
				install = install.trim();
				if(count < 3 || install.equals("")) {
					count++;
					continue;
				}
				Process getExes = Runtime.getRuntime().exec(Defaults.saveDirectory + "\\GDBoard\\bin\\getProgram.bat " + "\"" + install + "\"");
				BufferedReader exes = new BufferedReader(new InputStreamReader(getExes.getInputStream()));
				while(true) {
					exe = exes.readLine();
					if(exe == null) {
						break;
					}
					exe = exe.trim();
				}
			}
			if(exe != null) {
				return exe;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void delStuff(String command, String identifier) {

			boolean exists = false;
			Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\" + identifier + ".txt");
			try {
				if (Files.exists(file)) {
					Scanner sc = new Scanner(file);
					while (sc.hasNextLine()) {
						if (String.valueOf(command).equals(sc.nextLine())) {
							exists = true;
							break;
						}
					}
					sc.close();
					if (exists) {
						Path temp = Paths.get(Defaults.saveDirectory + "\\GDBoard\\_temp" + identifier + "_");
						PrintWriter out = new PrintWriter(new FileWriter(temp.toFile()));
						Files.lines(file)
								.filter(line -> !line.contains(command))
								.forEach(out::println);
						out.flush();
						out.close();
						Files.delete(file);
						Files.move(temp, temp.resolveSibling(Defaults.saveDirectory + "\\GDBoard\\" + identifier + ".txt"), StandardCopyOption.REPLACE_EXISTING);
					}
				}
			} catch (Exception f) {
				f.printStackTrace();
			}

	}

	public static void saveOption(String command, String type, String optionType) {
		try {
			String typeA = null;
			if (Files.exists(Paths.get(Defaults.saveDirectory + "/GDBoard/" + type + "/options.txt"))) {
				Scanner sc3 = new Scanner(Paths.get(Defaults.saveDirectory + "/GDBoard/" + type + "/options.txt").toFile());
				while (sc3.hasNextLine()) {
					String line = sc3.nextLine();
					if(line.split("=").length > 1) {
						if (line.split("=")[0].trim().equalsIgnoreCase(command)) {
							typeA = line.split("=")[1].trim();
							break;
						}
					}
				}
				sc3.close();
			} else {
				Files.createFile(Paths.get(Defaults.saveDirectory + "/GDBoard/" + type + "/options.txt"));
			}
			if (typeA != null) {
				BufferedReader file = new BufferedReader(new FileReader(Defaults.saveDirectory + "/GDBoard/" + type + "/options.txt"));
				StringBuilder inputBuffer = new StringBuilder();
				String line;
				while ((line = file.readLine()) != null) {
					inputBuffer.append(line);
					inputBuffer.append('\n');
				}
				file.close();

				FileOutputStream fileOut = new FileOutputStream(Defaults.saveDirectory + "/GDBoard/" + type + "/options.txt");
				fileOut.write(inputBuffer.toString().replace(command + " = " + typeA, command + " = " + optionType).getBytes());
				fileOut.close();
			} else {
				BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(Defaults.saveDirectory + "/GDBoard/" + type + "/options.txt").toFile(), true));
				writer.newLine();
				writer.write(command + " = " + optionType);
				writer.close();
			}
		} catch (Exception f) {
			f.printStackTrace();
		}
	}
	public static void delCooldown(String command, String type) {
		try {
			int cooldown = -1;
			if (Files.exists(Paths.get(Defaults.saveDirectory + "/GDBoard/cooldown.txt"))) {
				Scanner sc3 = new Scanner(Paths.get(Defaults.saveDirectory + "/GDBoard/cooldown.txt").toFile());
				while (sc3.hasNextLine()) {
					String line = sc3.nextLine();
					if(line.split("=").length > 1) {
						if (line.split("=")[0].trim().equalsIgnoreCase(command)) {
							cooldown = Integer.parseInt(line.split("=")[1].trim());
							break;
						}
					}
				}
				sc3.close();
			} else {
				Files.createFile(Paths.get(Defaults.saveDirectory + "/GDBoard/cooldown.txt"));
			}
			if (cooldown != -1) {
				BufferedReader file = new BufferedReader(new FileReader(Defaults.saveDirectory + "/GDBoard/cooldown.txt"));
				StringBuilder inputBuffer = new StringBuilder();
				String line;
				while ((line = file.readLine()) != null) {
					inputBuffer.append(line);
					inputBuffer.append('\n');
				}
				file.close();

				FileOutputStream fileOut = new FileOutputStream(Defaults.saveDirectory + "/GDBoard/cooldown.txt");
				fileOut.write(inputBuffer.toString().replace(command + " = " + cooldown, command + " = " + 0).getBytes());
				fileOut.close();
			} else {
				BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(Defaults.saveDirectory + "/GDBoard/cooldown.txt").toFile(), true));
				writer.newLine();
				writer.write(command + " = " + 0);
				writer.close();

			}
		} catch (Exception f) {
			f.printStackTrace();
		}
	}
	public static void deleteCommandA(String command, String type, String optionType){
		if(optionType.equalsIgnoreCase("command")) {
			delStuff(command, "mod");
			delStuff(command, "disable");
			delStuff(command, "whisper");
			delCooldown(command, type);
		}
		saveOption(command, type, optionType);
	}
}
