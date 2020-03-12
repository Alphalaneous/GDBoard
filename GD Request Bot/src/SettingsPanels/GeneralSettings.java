package SettingsPanels;

import Main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyListener;
import java.io.*;

public class GeneralSettings {
	private static JButtonUI defaultUI = new JButtonUI();
	public static boolean followersOption = false;
	public static boolean nowPlayingOption = false;
	public static boolean autoDownloadOption = false;
	public static boolean userLimitOption = false;
	private static JLabel versionLabel = new JLabel();
	private static CheckboxButton followers = createButton("Followers Only", 50);
	private static CheckboxButton nowPlaying = createButton("Disable Now Playing Message", 80);
	private static CheckboxButton autoDownload = createButton("Automatically download Music (Experimental)", 110);
	public static CheckboxButton queueLimitText = createButton("Maximum Queue Size: ", 140);
	public static int queueLimit = 0;
	public static int userLimit = 0;
	public static boolean queueLimitBoolean = false;
	private static FancyTextArea queueSizeInput = new FancyTextArea(true);
	public static CheckboxButton userLimitText = createButton("User Limits: ", 215);
	private static FancyTextArea userLimitInput = new FancyTextArea(true);

	public static JPanel createPanel() {

		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);

		InputStream is;
		try {
			is = new FileInputStream(System.getenv("APPDATA") + "\\GDBoard\\version.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			versionLabel.setText("GDBoard version: " + br.readLine().replaceAll("version=", ""));

		} catch (IOException e) {
			versionLabel.setText("GDBoard version: unknown");
		}

		versionLabel.setForeground(Defaults.FOREGROUND2);
		versionLabel.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		versionLabel.setBounds(25,20,versionLabel.getPreferredSize().width+5,versionLabel.getPreferredSize().height+5);

		followers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				followersOption = followers.getSelectedState();
			}
		});

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
		queueSizeInput.setEditable(false);
		queueSizeInput.setBounds(25,173,365, 32);
		queueSizeInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		queueSizeInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
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

		userLimitText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				userLimitOption = userLimitText.getSelectedState();
				if(!userLimitOption){
					userLimitInput.setEditable(false);
				}
				else{
					userLimitInput.setEditable(true);
				}
			}
		});

		userLimitInput.setEditable(false);
		userLimitInput.setBounds(25,245,365, 32);
		userLimitInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		userLimitInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					userLimit = Integer.parseInt(userLimitInput.getText());
				}
				catch (NumberFormatException f){
					userLimit = 0;
				}
				System.out.println(userLimit);
			}
		});

		panel.add(followers);
		panel.add(nowPlaying);
		panel.add(autoDownload);
		panel.add(versionLabel);
		panel.add(queueLimitText);
		panel.add(queueSizeInput);
		panel.add(userLimitText);
		panel.add(userLimitInput);
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
			userLimitOption = Boolean.parseBoolean(Settings.getSettings("userLimitEnabled"));
			if(!Settings.getSettings("userLimit").equalsIgnoreCase("")) {
				userLimit = Integer.parseInt(Settings.getSettings("userLimit"));
				userLimitInput.setText(String.valueOf(userLimit));
			}
			followers.setChecked(followersOption);
			nowPlaying.setChecked(nowPlayingOption);
			autoDownload.setChecked(autoDownloadOption);
			queueLimitText.setChecked(queueLimitBoolean);
			userLimitText.setChecked(userLimitOption);
			if(!queueLimitBoolean){
				queueSizeInput.setEditable(false);
			}
			else{
				queueSizeInput.setEditable(true);
			}
			if(!userLimitOption){
				userLimitInput.setEditable(false);
			}
			else{
				userLimitInput.setEditable(true);
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
			Settings.writeSettings("userLimitEnabled", String.valueOf(userLimitOption));
			Settings.writeSettings("userLimit", String.valueOf(userLimit));
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
