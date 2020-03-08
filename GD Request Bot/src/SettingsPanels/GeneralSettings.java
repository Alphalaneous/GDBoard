package SettingsPanels;

import Main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class GeneralSettings {
	private static JButtonUI defaultUI = new JButtonUI();
	public static boolean followersOption = false;
	public static boolean subsOption = false;
	public static boolean nowPlayingOption = false;
	public static boolean autoDownloadOption = false;
	private static CheckboxButton followers = createButton("Followers Only", 50);
	//private static CheckboxButton subs = createButton("Subscribers Only", 80);
	private static CheckboxButton nowPlaying = createButton("Disable Now Playing Message", 50);
	private static CheckboxButton autoDownload = createButton("Automatically download Music (Experimental)", 80);
	public static String outputString = "Currently playing %levelName% (%levelID%) by %author%!";
	public static String noLevelString = "There are no levels in the queue!";
	/*private static CheckboxButton auto = createButton("Auto", 110);
	private static CheckboxButton easy = createButton("Easy", 140);
	private static CheckboxButton normal = createButton("Normal", 170);
	private static CheckboxButton hard = createButton("Hard", 200);
	private static CheckboxButton harder = createButton("Harder", 230);
	private static CheckboxButton insane = createButton("Insane", 260);
	private static CheckboxButton demon = createButton("Demon", 290);*/
	public static JPanel createPanel() {

		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
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

		/*subs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				subsOption = subs.getSelectedState();
			}
		});*/



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

		//panel.add(followers);
		//panel.add(subs);

		panel.add(nowPlaying);
		panel.add(autoDownload);
		/*panel.add(auto);
		panel.add(easy);
		panel.add(normal);
		panel.add(hard);
		panel.add(harder);
		panel.add(insane);
		panel.add(demon);*/
		panel.add(label);

		return panel;



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
	public static void loadSettings(){
		try {
			followersOption = Boolean.parseBoolean(Settings.getSettings("followers"));
			//subsOption = Boolean.parseBoolean(Settings.getSettings("subs"));
			if(!Settings.getSettings("outputString").equalsIgnoreCase("")) {
				outputString = Settings.getSettings("outputString");
			}
			if(!Settings.getSettings("noLevelString").equalsIgnoreCase("")) {
				noLevelString = Settings.getSettings("noLevelString");
			}
			nowPlayingOption = Boolean.parseBoolean(Settings.getSettings("disableNP"));
			autoDownloadOption = Boolean.parseBoolean(Settings.getSettings("autoDL"));
			followers.setChecked(followersOption);
			//subs.setChecked(subsOption);

			nowPlaying.setChecked(nowPlayingOption);
			autoDownload.setChecked(autoDownloadOption);
		}
		catch (IOException e){
			JOptionPane.showMessageDialog(null, "There was an error reading the config file!", "Error",  JOptionPane.ERROR_MESSAGE);
		}
	}
	public static void setSettings(){
		try {
			Settings.writeSettings("followers", String.valueOf(followersOption));
			//Settings.writeSettings("subs", String.valueOf(subsOption));

			Settings.writeSettings("disableNP", String.valueOf(nowPlayingOption));
			Settings.writeSettings("autoDL", String.valueOf(autoDownloadOption));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There was an error writing to the file!", "Error",  JOptionPane.ERROR_MESSAGE);
		}
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
