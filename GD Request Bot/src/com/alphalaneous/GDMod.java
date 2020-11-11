package com.alphalaneous;

import com.alphalaneous.SettingsPanels.ChaosModeSettings;
import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GDMod {
	public static void run(String... args){
		new Thread(() -> {
		String PID = null;
		try {
			ProcessBuilder pb = new ProcessBuilder("tasklist", "/fi", "\"IMAGENAME eq GeometryDash.exe\"", "/fo", "CSV").redirectErrorStream(true);
			Process process = pb.start();

			try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				while (true) {
					String line = in.readLine();
					if (line == null) {
						break;
					}
					if(line.contains("GeometryDash.exe")){
						PID = line.split(",")[1].replaceAll("\"","");
					}
				}
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
		try {
			String[] cmd = new String[]{Defaults.saveDirectory + "\\GDBoard\\bin\\gdmod.exe", PID};
			String[] fillCmd = ArrayUtils.addAll(cmd, args);
			ProcessBuilder pb = new ProcessBuilder(fillCmd).redirectErrorStream(true);
			pb.start();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		}).start();
	}
	public static void doChaos(String... args){
		System.out.println(args[0]);
		switch(args[0]){
			case "x":
				if (ChaosModeSettings.minXOption && (Double.parseDouble(args[1]) < ChaosModeSettings.minX)) {
					return;
				}
				if (ChaosModeSettings.maxXOption && (Double.parseDouble(args[1]) > ChaosModeSettings.maxX)) {
					return;
				}
				break;
			case "y":
				if (ChaosModeSettings.minYOption && (Double.parseDouble(args[1]) < ChaosModeSettings.minY)) {
					return;
				}
				if (ChaosModeSettings.maxYOption && (Double.parseDouble(args[1]) > ChaosModeSettings.maxY)) {
					return;
				}
				break;
			case "size":
				if (ChaosModeSettings.minSizeOption && (Double.parseDouble(args[1]) < ChaosModeSettings.minSize)) {
					return;
				}
				if (ChaosModeSettings.maxSizeOption && (Double.parseDouble(args[1]) > ChaosModeSettings.maxSize)) {
					return;
				}
				break;
			case "speed":
				if (ChaosModeSettings.minSpeedOption && (Double.parseDouble(args[1]) < ChaosModeSettings.minSpeed)) {
					return;
				}
				if (ChaosModeSettings.maxSpeedOption && (Double.parseDouble(args[1]) > ChaosModeSettings.maxSpeed)) {
					return;
				}
				break;
			case "kill":
				if(ChaosModeSettings.disableKillOption){
					return;
				}
				break;
			default: break;
		}

		new Thread(() -> {
			String PID = null;
			try {
				ProcessBuilder pb = new ProcessBuilder("tasklist", "/fi", "\"IMAGENAME eq GeometryDash.exe\"", "/fo", "CSV").redirectErrorStream(true);
				Process process = pb.start();

				try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					while (true) {
						String line = in.readLine();
						if (line == null) {
							break;
						}
						if(line.contains("GeometryDash.exe")){
							PID = line.split(",")[1].replaceAll("\"","");
						}
					}
				}
			}
			catch (IOException e){
				e.printStackTrace();
			}
			try {
				String[] cmd = new String[]{Defaults.saveDirectory + "\\GDBoard\\bin\\gdmod.exe", PID};
				String[] fillCmd = ArrayUtils.addAll(cmd, args);
				ProcessBuilder pb = new ProcessBuilder(fillCmd).redirectErrorStream(true);
				pb.start();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}).start();
	}
}
