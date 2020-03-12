package SettingsPanels;

import Main.CheckboxButton;
import Main.Defaults;
import Main.FancyTextArea;
import Main.Settings;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OutputSettings {
	public static String outputString = "Currently playing %levelName% (%levelID%) by %author%!";
	public static String noLevelString = "There are no levels in the queue!";
	public static String fileLocation = Paths.get(System.getenv("APPDATA") + "\\GDBoard").toString();
	private static FancyTextArea outputStringInput = new FancyTextArea(false);
	private static FancyTextArea noLevelsStringInput = new FancyTextArea(false);
	private static FancyTextArea fileLocationInput = new FancyTextArea(false);
	public static JPanel createPanel() {
		//TODO Add scrollbar to Text Area?
		JPanel panel = new JPanel();
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Main.Defaults.SUB_MAIN);
		panel.setLayout(null);
		JLabel outputText = new JLabel("Output Text:");
		outputText.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		outputText.setBounds(25,20,outputText.getPreferredSize().width+5,outputText.getPreferredSize().height+5);
		outputText.setForeground(Defaults.FOREGROUND);
		outputStringInput.setBounds(25,45,365, 200);
		outputStringInput.setLineWrap(true);
		outputStringInput.setWrapStyleWord(true);
		outputStringInput.setText(outputString);
		outputStringInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e){
				if(!outputStringInput.getText().equalsIgnoreCase("")){
					outputString = outputStringInput.getText();
				}
				else{
					outputString = "Currently playing %levelName% (%levelID%) by %author%!";
				}
			}
		});

		JLabel noLevelsText = new JLabel("No Levels Text:");
		noLevelsText.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		noLevelsText.setBounds(25,265,noLevelsText.getPreferredSize().width+5,noLevelsText.getPreferredSize().height+5);
		noLevelsText.setForeground(Defaults.FOREGROUND);
		noLevelsStringInput.setBounds(25,290,365, 200);
		noLevelsStringInput.setLineWrap(true);
		noLevelsStringInput.setWrapStyleWord(true);
		noLevelsStringInput.setText(noLevelString);
		noLevelsStringInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e){
				if(!noLevelsStringInput.getText().equalsIgnoreCase("")){
					noLevelString = noLevelsStringInput.getText();
				}
				else{
					noLevelString = "Currently playing %levelName% (%levelID%) by %author%!";
				}
			}
		});

		JLabel fileLocationText = new JLabel("File location:");
		fileLocationText.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		fileLocationText.setBounds(25,510,fileLocationText.getPreferredSize().width+5,fileLocationText.getPreferredSize().height+5);
		fileLocationText.setForeground(Defaults.FOREGROUND);
		fileLocationInput.setBounds(25,535,365, 64);
		fileLocationInput.setLineWrap(true);
		fileLocationInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		fileLocationInput.setText(fileLocation);
		fileLocationInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e){
				fileLocation = fileLocationInput.getText();
			}
		});

		panel.add(outputText);
		panel.add(outputStringInput);
		panel.add(noLevelsText);
		panel.add(noLevelsStringInput);
		panel.add(fileLocationText);
		panel.add(fileLocationInput);
		return panel;
		
	}
	public static void loadSettings(){
		try {
			if(!Settings.getSettings("noLevelsString").equalsIgnoreCase("")) {
				noLevelString = Settings.getSettings("noLevelsString").replace("%n%", "\n");
				noLevelsStringInput.setText(noLevelString);
			}
			if(!Settings.getSettings("outputString").equalsIgnoreCase("")) {
				outputString = Settings.getSettings("outputString").replace("%n%", "\n");
				outputStringInput.setText(outputString);
			}
			if(!Settings.getSettings("outputFileLocation").equalsIgnoreCase("")) {
				fileLocation = Settings.getSettings("outputFileLocation");
				fileLocationInput.setText(fileLocation);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void setSettings(){
		try {
			Settings.writeSettings("outputString", outputString.replace("\n", "%n%"));
			Settings.writeSettings("noLevelsString", noLevelString.replace("\n", "%n%"));
			Settings.writeSettings("outputFileLocation", fileLocation.replace("\\", "\\\\"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void setOutputStringFile(String text){
		Path file = Paths.get(fileLocation + "\\output.txt");
		if(fileLocation.equalsIgnoreCase("")){
			file = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\output.txt");
		}
		try {
			if(!Files.exists(file)) {
				Files.createFile(file);
			}
			Files.write(file, text.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}