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
	public static boolean queueLimitBoolean = false;
	public static boolean userLimitOption = false;
	public static boolean userLimitStreamOption = false;
	private static JLabel versionLabel = new JLabel();
	private static CheckboxButton followers = createButton("Followers Only", 50);
	private static CheckboxButton nowPlaying = createButton("Disable Now Playing Message", 80);
	private static CheckboxButton autoDownload = createButton("Automatically download Music (Experimental)", 110);
	private static CheckboxButton queueLimitText = createButton("Maximum Queue Size: ", 140);
	private static CheckboxButton userLimitText = createButton("In Queue Request Limit: ", 215);
	private static CheckboxButton userLimitStreamText = createButton("All Stream Request Limit: ", 290);

	public static int queueLimit = 0;
	public static int userLimit = 0;
	public static int userLimitStream = 0;

	private static FancyTextArea queueSizeInput = new FancyTextArea(true);
	private static FancyTextArea userLimitInput = new FancyTextArea(true);
	private static FancyTextArea userLimitStreamInput = new FancyTextArea(true);
	private static JPanel panel = new JPanel();

	public static JPanel createPanel() {

		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);


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

		userLimitStreamText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				userLimitStreamOption = userLimitStreamText.getSelectedState();
				if(!userLimitStreamOption){
					userLimitStreamInput.setEditable(false);
				}
				else{
					userLimitStreamInput.setEditable(true);
				}
			}
		});

		userLimitStreamInput.setEditable(false);
		userLimitStreamInput.setBounds(25,320,365, 32);
		userLimitStreamInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		userLimitStreamInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					userLimitStream = Integer.parseInt(userLimitStreamInput.getText());
				}
				catch (NumberFormatException f){
					userLimitStream = 0;
				}
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
		panel.add(userLimitStreamText);
		panel.add(userLimitStreamInput);
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
			userLimitStreamOption = Boolean.parseBoolean(Settings.getSettings("userLimitStreamEnabled"));
			if(!Settings.getSettings("userLimitStream").equalsIgnoreCase("")) {
				userLimitStream = Integer.parseInt(Settings.getSettings("userLimitStream"));
				userLimitStreamInput.setText(String.valueOf(userLimitStream));
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
			if(!userLimitStreamOption){
				userLimitStreamInput.setEditable(false);
			}
			else{
				userLimitStreamInput.setEditable(true);
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
			Settings.writeSettings("userLimitStreamEnaabled", String.valueOf(userLimitStreamOption));
			Settings.writeSettings("userLimitStream", String.valueOf(userLimitStream));
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
	public static void refreshUI() {
		defaultUI.setBackground(Defaults.MAIN);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);

		panel.setBackground(Defaults.SUB_MAIN);
		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.MAIN);
			}
			if (component instanceof JLabel && !(((JLabel) component).getText().contains("GDBoard"))) {
				component.setForeground(Defaults.FOREGROUND);

			}
			if(component instanceof JTextArea){
				((FancyTextArea) component).refreshAll();
			}
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}

		}
	}
}
