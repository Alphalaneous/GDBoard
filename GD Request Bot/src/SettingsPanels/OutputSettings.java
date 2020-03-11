package SettingsPanels;

import Main.CheckboxButton;
import Main.Defaults;
import Main.FancyTextArea;
import Main.Settings;

import javax.swing.*;
import javax.swing.text.BadLocationException;
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
	private static FancyTextArea outputStringInput = new FancyTextArea(false);
	private static FancyTextArea noLevelsStringInput = new FancyTextArea(false);
	public static JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Main.Defaults.SUB_MAIN);
		panel.setLayout(null);
		JLabel outputText = new JLabel("Output Text:");
		outputText.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		outputText.setBounds(25,20,outputText.getPreferredSize().width+5,outputText.getPreferredSize().height+5);
		outputText.setForeground(Defaults.FOREGROUND);
		outputStringInput.setBounds(25,45,365, 250);
		outputStringInput.setLineWrap(true);
		outputStringInput.setWrapStyleWord(true);
		outputStringInput.setText(outputString);
		outputStringInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if(outputStringInput.getLineCount() == 13){
					
				}
			}
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
		noLevelsText.setBounds(25,305,noLevelsText.getPreferredSize().width+5,noLevelsText.getPreferredSize().height+5);
		noLevelsText.setForeground(Defaults.FOREGROUND);
		noLevelsStringInput.setBounds(25,330,365, 250);
		noLevelsStringInput.setLineWrap(true);
		noLevelsStringInput.setWrapStyleWord(true);
		noLevelsStringInput.setText(noLevelString);
		noLevelsStringInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if(noLevelsStringInput.getLineCount() == 13){

				}
			}
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
		panel.add(outputText);
		panel.add(outputStringInput);
		panel.add(noLevelsText);
		panel.add(noLevelsStringInput);
		return panel;
		
	}
	public static void loadSettings(){
		try {
			if(!Settings.getSettings("noLevelsString").equalsIgnoreCase("")) {
				noLevelString = Settings.getSettings("noLevelsString");
				noLevelsStringInput.setText(noLevelString);
			}
			if(!Settings.getSettings("outputString").equalsIgnoreCase("")) {
				outputString = Settings.getSettings("outputString");
				outputStringInput.setText(outputString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void setSettings(){
		try {
			Settings.writeSettings("outputString", outputString);
			Settings.writeSettings("noLevelsString", noLevelString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void setOutputStringFile(String text){
		Path file = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\output.txt");
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
