package com.alphalaneous;

import java.io.*;

public class GDHelper {

	private static boolean isDead = true;
	private static boolean isInLevel = false;
	private static String levelName;
	private static String creator;
	private static long levelID;

	private static Runtime rt = Runtime.getRuntime();

	static String command = Defaults.saveDirectory + "\\GDBoard\\bin\\ChaosMode.exe";
	private static BufferedReader processOutput;
	private static BufferedWriter processInput;
	private static Process pr;
	static {
		try {
			pr = rt.exec(command);
			processOutput = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			processInput = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void start() {
		new Thread(() -> {
			while (true) {
				try {
					String line = processOutput.readLine();
					if(line.startsWith(">>")) {
						String type = line.split(": ", 2)[0].replace(">> ", "");
						String response = line.split(": ", 2)[1];


						if (type.equalsIgnoreCase("IsDead")) {
							if (response.equalsIgnoreCase("true")) {
								isDead = true;
								//System.out.println("IsDead: true");
							} else if (response.equalsIgnoreCase("false")) {
								isDead = false;
								//System.out.println("IsDead: false");
							}
						}
						if (type.equalsIgnoreCase("InLevel")) {
							if (response.equalsIgnoreCase("true")) {
								isInLevel = true;
								//System.out.println("InLevel: true");
							} else if (response.equalsIgnoreCase("false")) {
								isInLevel = false;
								//System.out.println("InLevel: false");
							}
						}
						if (type.equalsIgnoreCase("Name")) {
							levelName = response;
						}
						if (type.equalsIgnoreCase("Creator")) {
							if(levelID <= 21 || levelID == 3001){
								creator = "RobTop";
							}
							else {
								creator = response;
							}
						}
						if (type.equalsIgnoreCase("ID")) {
							levelID = Long.parseLong(response);

						}
					}
					//System.out.println(line);

				}
				catch (Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}

	static void send(String command) {
		new Thread(() -> {
			try {
				//System.out.println(command);
				while(!(!isDead && isInLevel)){
					Thread.sleep(100);
				}
				processInput.write(command + "\n");
				processInput.flush();
			} catch (Exception ignored) {
			}
		}).start();
	}
	static void close(){
		try {
			processInput.write("exit 0\n");
			processInput.flush();
			pr.destroyForcibly();
		}
		catch (Exception ignored){
		}
	}
	public static String getCurrentLevelName(){
		return levelName;
	}
	public static String getCurrentLevelCreator(){
		return creator;
	}
	public static long getCurrentLevelID(){
		return levelID;
	}
	public static boolean getCurrentDeathStatus(){
		return isDead;
	}
	public static boolean getCurrentInLevelStatus(){
		return isInLevel;
	}
}
