package Main;

import Main.SettingsPanels.ChannelPointSettings;
import Main.SettingsPanels.CommandSettings;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

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
				Files.createFile(file);
				Files.write(file, command.getBytes());
				Main.sendMessage(Utilities.format("$COMMAND_ADDED_SUCCESS$", username, newCommandName));
				CommandSettings.refresh();
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
				Files.createFile(file);
				Files.write(file, command.getBytes());
				Main.sendMessage(Utilities.format("$POINTS_ADDED_SUCCESS$", username, newCommandName));
				ChannelPointSettings.refresh();

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
}
