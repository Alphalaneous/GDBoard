package com.alphalaneous;

import java.io.*;

public class GDHelper {

	private static boolean isDead = true;
	private static boolean isInLevel = false;
	private static String levelName = "";
	private static String creator = "";
	private static long levelID = 0;
	private static int attemptCount = 0;
	private static int normalBest = 0;
	private static int practiceBest = 0;
	private static int totalJumps = 0;
	private static int totalAttempts = 0;
	private static float percent = 0;
	private static float posX = 0;
	private static float posY = 0;
	private static int levelLength = 0;
	private static int objects = 0;

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
					if(line != null) {
						if (line.startsWith(">>")) {
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
								if (levelID <= 21 || levelID == 3001) {
									creator = "RobTop";
								} else {
									creator = response;
								}
							}
							if (type.equalsIgnoreCase("ID")) {
								levelID = Long.parseLong(response);
							}
							if (type.equalsIgnoreCase("Current Attempt")) {
								attemptCount = Integer.parseInt(response);
							}
							if (type.equalsIgnoreCase("Total Jumps")) {
								totalJumps = Integer.parseInt(response);
							}
							if (type.equalsIgnoreCase("Total Attempts")) {
								totalAttempts = Integer.parseInt(response);
							}
							if (type.equalsIgnoreCase("Percent")) {
								percent = Float.parseFloat(response);
							}
							if (type.equalsIgnoreCase("X")) {
								posX = Float.parseFloat(response);
							}
							if (type.equalsIgnoreCase("Y")) {
								posY = Float.parseFloat(response);
							}
							if (type.equalsIgnoreCase("Length")) {
								levelLength = Integer.parseInt(response);
							}
							if (type.equalsIgnoreCase("Normal Best")) {
								normalBest = Integer.parseInt(response);
							}
							if (type.equalsIgnoreCase("Practice Best")) {
								practiceBest = Integer.parseInt(response);
							}
							if (type.equalsIgnoreCase("Object Count")) {
								objects = Integer.parseInt(response);
							}
						}
						//System.out.println(line);
					}
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
			pr.destroy();
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
	public static int getCurrentAttempts(){
		return attemptCount;
	}
	public static int getTotalJumps(){
		return totalJumps;
	}
	public static int getTotalAttempts(){
		return totalAttempts;
	}
	public static float getPercent(){
		return percent;
	}
	public static int getNormalBest(){
		return normalBest;
	}
	public static int getPracticeBest(){
		return practiceBest;
	}
	public static int getLength(){
		return levelLength;
	}
	public static int getObjectCount(){
		return objects;
	}
	public static float getX(){
		return posX;
	}
	public static float getY(){
		return posY;
	}
}
