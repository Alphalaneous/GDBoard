package Main;

import Main.SettingsPanels.BlockedCreatorSettings;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;
import javax.swing.*;
import Main.SettingsPanels.*;

import static Main.Defaults.defaultUI;

public class SettingsWindow {
	private static int width = 636;
	private static int height = 662;
	public static JPanel window = new InnerWindow("Settings", 0, 0, width-2, height,
			"\uE713", true).createPanel();

	private static JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	private static JPanel content = new JPanel();
	private static JPanel blankSpace = new JPanel();

	private static JButtonUI selectUI = new JButtonUI();
	private static JPanel general = GeneralSettings.createPanel();
	private static JPanel overlay = OutputSettings.createPanel();
	private static JPanel accounts = AccountSettings.createPanel();
	private static JPanel commands = CommandSettings.createPanel();
	private static JPanel points = ChannelPointSettings.createPanel();
	private static JPanel cheers = CheerSettings.createPanel();
	private static JPanel requests = RequestSettings.createPanel();
	private static JPanel shortcuts = ShortcutSettings.createPanel();
	private static JPanel personalization = PersonalizationSettings.createPanel();
	private static JPanel blocked = BlockedSettings.createPanel();
	private static JPanel blockedUsers = BlockedUserSettings.createPanel();
	private static JPanel blockedCreators = BlockedCreatorSettings.createPanel();
	private static JPanel loggedIDs = RequestsLog.createPanel();
	private static JPanel language = LanguageSettings.createPanel();


	public static JFrame frame = new JFrame();

	public static boolean run = true;
	public static void createPanel() {
		frame = new JFrame();
		if(!Settings.getSettings("windowed").equalsIgnoreCase("true")){
			window = new InnerWindow("Settings", 0, 0, width-16, height-40,
					"\uE713", false).createPanel();
		}
		else{
			frame.setSize(width, height);
		}
		URL iconURL = Windowed.class.getResource("/Resources/Icons/windowIcon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		Image newIcon = icon.getImage().getScaledInstance(120, 120,  Image.SCALE_SMOOTH);
		frame.setIconImage(newIcon);
		frame.setResizable(false);
		frame.setTitle("GDBoard - Settings");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				SettingsWindow.toggleVisible();
			}
		});
		if(Settings.getSettings("settings").equalsIgnoreCase("") && Settings.getSettings("windowed").equalsIgnoreCase("true")){
			frame.setLocation((int)Defaults.screenSize.getWidth()/2 - width/2, 200);
		}
		//frame.setUndecorated(true);
		frame.setLayout(null);
		//frame.setBackground(new Color(255, 255, 255, 0));
		if(Settings.getSettings("windowed").equalsIgnoreCase("false")) {
			blankSpace.setBounds(1, 31, 208, 20);
			buttons.setBounds(1, 51, 208, height-20);
			content.setBounds(209, 31, 412, height);
		}
		else{
			blankSpace.setBounds(0, 0, 208, 20);
			buttons.setBounds(0, 20, 208, height-20);
			content.setBounds(208, 0, 412, height);
		}
		blankSpace.setBackground(Defaults.MAIN);

		buttons.setBackground(Defaults.MAIN);

		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);

		content.add(general);
		content.add(overlay);
		content.add(accounts);
		content.add(commands);
		content.add(points);
		content.add(cheers);
		content.add(requests);
		content.add(shortcuts);
		content.add(personalization);
		content.add(blocked);
		content.add(blockedUsers);
		content.add(blockedCreators);
		content.add(loggedIDs);
		content.add(language);

		general.setVisible(true);
		overlay.setVisible(false);
		accounts.setVisible(false);
		commands.setVisible(false);
		points.setVisible(false);
		cheers.setVisible(false);
		requests.setVisible(false);
		shortcuts.setVisible(false);
		personalization.setVisible(false);
		blocked.setVisible(false);
		blockedUsers.setVisible(false);
		blockedCreators.setVisible(false);
		loggedIDs.setVisible(false);
		language.setVisible(false);


		JButton general = createButton("$GENERAL_SETTINGS$");
		general.setBackground(Defaults.SELECT);
		general.setUI(selectUI);
		JButton outputs = createButton("$OUTPUTS_SETTINGS$");
		JButton accounts = createButton("$ACCOUNTS_SETTINGS$");
		JButton commands = createButton("$COMMANDS_SETTINGS$");
		JButton points = createButton("$CHANNEL_POINTS_SETTINGS$");
		JButton cheers = createButton("$CHEERS_SETTINGS$");
		JButton requests = createButton("$REQUESTS_SETTINGS$");
		JButton shortcuts = createButton("$SHORTCUTS_SETTINGS$");
		JButton personalization = createButton("$PERSONALIZATION_SETTINGS$");
		JButton blocked = createButton("$BLOCKED_IDS_SETTINGS$");
		JButton blockedUsers = createButton("$BLOCKED_USERS_SETTINGS$");
		JButton blockedCreators = createButton("$BLOCKED_CREATORS_SETTINGS$");
		JButton loggedIDs = createButton("$LOGGED_IDS_SETTINGS$");
		JButton windowed = createButton("$WINDOWED_SETTINGS$");
		JButton language = createButton("$LANGUAGE_SETTINGS$");



		buttons.add(general);
		buttons.add(outputs);
		buttons.add(accounts);
		buttons.add(commands);
		buttons.add(points);
		//buttons.add(cheers);
		buttons.add(requests);
		buttons.add(shortcuts);
		buttons.add(personalization);
		buttons.add(blocked);
		buttons.add(blockedUsers);
		buttons.add(blockedCreators);
		buttons.add(loggedIDs);
		//buttons.add(language);
		if(Settings.getSettings("windowed").equalsIgnoreCase("false")){
			buttons.add(windowed);
			window.add(blankSpace);
			window.add(buttons);
			window.add(content);
			((InnerWindow) window).setPinVisible();
			((InnerWindow) window).refreshListener();
		}
		frame.setVisible(false);
		toggleVisible();
		if(Settings.getSettings("windowed").equalsIgnoreCase("true")){
			frame.add(blankSpace);
			frame.add(buttons);
			frame.add(content);
		}
		else {
			Overlay.addToFrame(window);
		}

	}
	public static void destroySettings(){
		window.removeAll();
		buttons.removeAll();
		if(!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			frame.setVisible(false);
			frame.dispose();
		}
		else {
			Overlay.removeFromFrame(window);
			frame = new JFrame();
		}
	}
	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		blankSpace.setBackground(Defaults.MAIN);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);
		selectUI.setSelect(Defaults.SELECT);
		buttons.setBackground(Defaults.MAIN);
		content.setBackground(Defaults.SUB_MAIN);
		for (Component component : buttons.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				if(!((JButton) component).getUI().equals(selectUI)) {
					component.setBackground(Defaults.MAIN);
				}
				else{
					component.setBackground(Defaults.SELECT);
				}

			}
		}

	}

	static void toggleVisible() {
		((InnerWindow) window).toggle();
	}

	static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

	static void setVisible() {
		((InnerWindow) window).setVisible();

	}

	private static int clean = 0;

	private static JButton createButton(String text) {

		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);

		JButton button = new JButton();
		LangLabel label = new LangLabel(text);

		label.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		label.setBounds(20, 10, 208, 20);
		label.setForeground(Defaults.FOREGROUND);

		button.setLayout(null);
		button.add(label);
		button.setBackground(Defaults.MAIN);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setPreferredSize(new Dimension(208, 38));

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					((InnerWindow) window).moveToFront();
					super.mousePressed(e);
					for (Component component2 : button.getComponents()) {
						if (component2 instanceof LangLabel) {
							if(!((LangLabel) component2).getIdentifier().equalsIgnoreCase("WINDOWED_SETTINGS")) {
								for (Component componentA : content.getComponents()) {
									if (componentA instanceof JPanel) {
										componentA.setVisible(false);
									}
								}
							}
							RequestsLog.clear();
							switch (((LangLabel) component2).getIdentifier()) {
								case "GENERAL_SETTINGS":
									general.setVisible(true);
									clean++;
									if(clean == 5){
										System.out.println("cleaned");
										System.gc();
										clean = 0;
									}
									break;
								case "OUTPUTS_SETTINGS":
									overlay.setVisible(true);
									clean = 0;
									break;
								case "ACCOUNTS_SETTINGS":
									accounts.setVisible(true);
									clean = 0;
									break;
								case "SHORTCUTS_SETTINGS":
									shortcuts.setVisible(true);
									clean = 0;
									break;
								case "PERSONALIZATION_SETTINGS":
									personalization.setVisible(true);
									clean = 0;
									break;
								case "BLOCKED_IDS_SETTINGS":
									blocked.setVisible(true);
									clean = 0;
									break;
								case "BLOCKED_USERS_SETTINGS":
									blockedUsers.setVisible(true);
									clean = 0;
									break;
								case "BLOCKED_CREATORS_SETTINGS":
									blockedCreators.setVisible(true);
									clean = 0;
									break;
								case "COMMANDS_SETTINGS":
									commands.setVisible(true);
									clean = 0;
									break;
								case "CHANNEL_POINTS_SETTINGS":
									points.setVisible(true);
									clean = 0;
									break;
								case "CHEERS_SETTINGS":
									cheers.setVisible(true);
									break;
								case "REQUESTS_SETTINGS":
									requests.setVisible(true);
									clean = 0;
									break;
								case "LANGUAGE_SETTINGS":
									language.setVisible(true);
									clean = 0;
									break;
								case "WINDOWED_SETTINGS":
									new Thread(()->{
										String option = DialogBox.showDialogBox("$SWITCH_TO_WINDOWED_TITLE$", "$SWINTH_TO_WINDOWED_INFO$", "$SWITCH_TO_WINDOWED_SUBINFO$", new String[]{"$YES$", "$NO$"});
										if (option.equalsIgnoreCase("YES")) {
											try {
												Settings.writeSettings("windowed", "true");
											}
											catch (Exception f){
												f.printStackTrace();
											}
											Main.close();
										}
									}).start();
									clean = 0;
									break;
								case "LOGGED_IDS_SETTINGS":
									loggedIDs.setVisible(true);
									clean = 0;
									new Thread(() -> {
										File file = new File(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");
										if (file.exists()) {
											Scanner sc = null;
											try {
												sc = new Scanner(file);
											} catch (FileNotFoundException f) {
												f.printStackTrace();
											}
											assert sc != null;
											while (sc.hasNextLine()) {
												RequestsLog.addButton(Long.parseLong(sc.nextLine().split(",")[0]));
											}
											sc.close();
										}
									}).start();
									break;
							}
							break;
						}
					}
					for (Component component : buttons.getComponents()) {
						if (component instanceof JButton) {
							((JButton) component).setUI(defaultUI);
							component.setBackground(Defaults.MAIN);
						}
					}

					button.setUI(selectUI);
					button.setBackground(Defaults.SELECT);
				}
			}
		});
		return button;
	}
	public static void addToFrame(JComponent component) {

		// --------------------
		// Add components to JFrame from elsewhere

		frame.add(component, 0);

		// --------------------
	}
	static void removeFromFrame(JComponent component) {

		// --------------------
		// Add components to JFrame from elsewhere

		frame.remove(component);

		// --------------------
	}
	//region SetLocation
	static void setLocation(Point point){
		window.setLocation(point);
	}
	//endregion
	//region SetSettings
	public static void setSettings(){
		if(!Settings.getSettings("windowed").equalsIgnoreCase("true")){
			Settings.setWindowSettings("Settings", window.getX() + "," + window.getY() + "," + false + "," + window.isVisible());
		}

	}
	//endregion
}
