package SettingsPanels;

import Main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneralSettings {
	private static JButtonUI defaultUI = new JButtonUI();
	public static boolean followersOption = false;
	public static boolean nowPlayingOption = false;
	public static boolean autoDownloadOption = false;
	private static CheckboxButton followers = createButton("Followers Only", 50);
	private static CheckboxButton nowPlaying = createButton("Disable Now Playing Message", 80);
	private static CheckboxButton autoDownload = createButton("Automatically download Music (Experimental)", 110);


	public static CheckboxButton queueLimitText = createButton("Maximum Queue Size: ", 140);
	public static int queueLimit = 0;
	public static boolean queueLimitBoolean = false;

	private static FancyTextArea queueSizeInput = new FancyTextArea(true);

	public static JPanel createPanel() {

		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);

		queueSizeInput.setBounds(25,173,365, 32);
		queueSizeInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);

		queueSizeInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					queueLimit = Integer.parseInt(queueSizeInput.getText());
				}
				catch (NumberFormatException f){
					queueLimit = 0;
				}
				System.out.println(queueLimit);
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		JLabel label;
		InputStream is;
		try {
			is = new FileInputStream(System.getenv("APPDATA") + "\\GDBoard\\version.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			label = new JLabel("GDBoard version: " + br.readLine().replaceAll("version=", ""));

		} catch (IOException e) {
			label = new JLabel("GDBoard version: unknown");
		}

		label.setForeground(Defaults.FOREGROUND2);
		label.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		label.setBounds(25,20,label.getPreferredSize().width+5,label.getPreferredSize().height+5);


		followers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				followersOption = followers.getSelectedState();
			}
		});

		queueSizeInput.setEditable(false);

		nowPlaying.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				nowPlayingOption = nowPlaying.getSelectedState();
			}
		});

		autoDownload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				autoDownloadOption = autoDownload.getSelectedState();
			}
		});
		queueLimitText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				queueLimitBoolean = queueLimitText.getSelectedState();
				if(!queueLimitBoolean){
					queueSizeInput.setEditable(false);
				}
				else{
					queueSizeInput.setEditable(true);
				}
			}
		});

		panel.add(followers);
		panel.add(nowPlaying);
		panel.add(autoDownload);
		panel.add(label);
		panel.add(queueLimitText);
		panel.add(queueSizeInput);
		return panel;



	}

	public static void loadSettings(){
		try {
			followersOption = Boolean.parseBoolean(Settings.getSettings("followers"));
			nowPlayingOption = Boolean.parseBoolean(Settings.getSettings("disableNP"));
			autoDownloadOption = Boolean.parseBoolean(Settings.getSettings("autoDL"));
			queueLimitBoolean = Boolean.parseBoolean(Settings.getSettings("queueLimitEnabled"));
			if(!Settings.getSettings("queueLimit").equalsIgnoreCase("")) {
				queueLimit = Integer.parseInt(Settings.getSettings("queueLimit"));
				queueSizeInput.setText(String.valueOf(queueLimit));
			}
			followers.setChecked(followersOption);

			nowPlaying.setChecked(nowPlayingOption);
			autoDownload.setChecked(autoDownloadOption);
			queueLimitText.setChecked(queueLimitBoolean);
			if(!queueLimitBoolean){
				queueSizeInput.setEditable(false);
			}
			else{
				queueSizeInput.setEditable(true);
			}
		}
		catch (IOException e){
			JOptionPane.showMessageDialog(null, "There was an error reading the config file!", "Error",  JOptionPane.ERROR_MESSAGE);
		}
	}
	public static void setSettings(){
		try {
			Settings.writeSettings("followers", String.valueOf(followersOption));
			Settings.writeSettings("disableNP", String.valueOf(nowPlayingOption));
			Settings.writeSettings("autoDL", String.valueOf(autoDownloadOption));
			Settings.writeSettings("queueLimitEnabled", String.valueOf(queueLimitBoolean));
			Settings.writeSettings("queueLimit", String.valueOf(queueLimit));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There was an error writing to the file!", "Error",  JOptionPane.ERROR_MESSAGE);
		}
	}
	private static JLabel createLabel(String text, int y){
		JLabel label = new JLabel(text);
		label.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		label.setBounds(25,y,label.getPreferredSize().width + 5,30);
		label.setForeground(Defaults.FOREGROUND);

		return label;
	}
	private static CheckboxButton createButton(String text, int y){

		CheckboxButton button = new CheckboxButton(text);
		button.setBounds(25,y,365,30);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		button.refresh();
		return button;
	}

}
