package com.alphalaneous;

import java.io.*;

class GDHelper {

	private static boolean isDead = true;
	private static boolean isInLevel = false;

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
					if(line.startsWith(">>")){
						String[] words = line.split(" ");
						if(words[1].equalsIgnoreCase("IsDead:")){
							if(words[2].equalsIgnoreCase("true")){
								isDead = true;
								//System.out.println("IsDead: true");
							}
							else if(words[2].equalsIgnoreCase("false")){
								isDead = false;
								//System.out.println("IsDead: false");
							}
						}
						if(words[1].equalsIgnoreCase("InLevel:")){
							if(words[2].equalsIgnoreCase("true")){
								isInLevel = true;
								//System.out.println("InLevel: true");
							}
							else if(words[2].equalsIgnoreCase("false")){
								isInLevel = false;
								//System.out.println("InLevel: false");
							}
						}
					}
					//System.out.println(line);
				}
				catch (Exception ignored){
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
}
