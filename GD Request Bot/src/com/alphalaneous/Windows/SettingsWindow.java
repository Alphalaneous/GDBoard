package com.alphalaneous.Windows;

import com.alphalaneous.*;
import com.alphalaneous.Components.JButtonUI;
import com.alphalaneous.Components.LangLabel;
import com.alphalaneous.Components.SettingsButton;
import com.alphalaneous.SettingsPanels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

import static com.alphalaneous.Defaults.defaultUI;

public class SettingsWindow {
	public static JPanel window = new JPanel();
	public static boolean run = true;
	static JFrame frame = new JFrame();
	private static JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	private static JPanel content = new JPanel();
	private static JPanel blankSpace = new JPanel();
	private static JButtonUI selectUI = new JButtonUI();
	private static JPanel generalPage = GeneralSettings.createPanel();
	private static JPanel generalBotPage = GeneralBotSettings.createPanel();
	private static JPanel overlayPage = OutputSettings.createPanel();
	private static JPanel accountsPage = AccountSettings.createPanel();
	private static JPanel commandsPage = CommandSettings.createPanel();
	private static JPanel pointsPage = ChannelPointSettings.createPanel();
	private static JPanel requestsPage = RequestSettings.createPanel();
	private static JPanel shortcutsPage = ShortcutSettings.createPanel();
	private static JPanel personalizationPage = PersonalizationSettings.createPanel();
	private static JPanel blockedPage = BlockedSettings.createPanel();
	private static JPanel blockedUsersPage = BlockedUserSettings.createPanel();
	private static JPanel blockedCreatorsPage = BlockedCreatorSettings.createPanel();
	private static JPanel loggedIDsPage = RequestsLog.createPanel();
	private static JPanel languagePage = LanguageSettings.createPanel();
	private static JPanel chaosModePage = ChaosModeSettings.createPanel();
	private static LangLabel title = new LangLabel("$SETTINGS_TITLE$");
	private static JPanel tempPanel = new JPanel(null);
	private static JPanel mainPanel = new JPanel();
	private static JPanel titlePanel = new JPanel();
	private static JPanel settingsButtons = new JPanel();
	private static int clean = 0;
	private static int refreshHelper = 0;
	private static JButton general = createButton("$GENERAL_SETTINGS$", "\uE115");
	private static JButton generalBot = createButton("$GENERAL_BOT_SETTINGS$", "\uE115");
	private static JButton chaosMode = createButton("$CHAOS_SETTINGS$", "\uE11D");
	private static JButton outputs = createButton("$OUTPUTS_SETTINGS$", "\uE122");
	private static JButton accounts = createButton("$ACCOUNTS_SETTINGS$", "\uE13D");
	private static JButton commands = createButton("$COMMANDS_SETTINGS$", "\uE13E");
	private static JButton points = createButton("$CHANNEL_POINTS_SETTINGS$", "\uF138");
	private static JButton cheers = createButton("$CHEERS_SETTINGS$", "\uF157");
	private static JButton requests = createButton("$REQUESTS_SETTINGS$", "\uED1E");
	private static JButton shortcuts = createButton("$SHORTCUTS_SETTINGS$", "\uE144");
	private static JButton personalization = createButton("$PERSONALIZATION_SETTINGS$", "\uE771");
	private static JButton blocked = createButton("$BLOCKED_IDS_SETTINGS$", "\uF140");
	private static JButton blockedUsers = createButton("$BLOCKED_USERS_SETTINGS$", "\uE1E0");
	private static JButton blockedCreators = createButton("$BLOCKED_CREATORS_SETTINGS$", "\uE25B");
	private static JButton loggedIDs = createButton("$LOGGED_IDS_SETTINGS$", "\uE14C");
	private static JButton windowed = createButton("$WINDOWED_SETTINGS$", "\uE737");
	private static JButton language = createButton("$LANGUAGE_SETTINGS$", "\uE12B");

	public static void createPanel() {
		frame = new JFrame();
		int width = 636;
		int height = 662;
		frame.setSize(width, height);
		URL iconURL = Window.class.getResource("/Resources/Icons/windowIcon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		Image newIcon = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
		frame.setIconImage(newIcon);
		frame.setResizable(false);
		frame.setTitle("GDBoard - Settings");
		frame.getContentPane().setBackground(Defaults.TOP);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mainPanel.setVisible(true);
				tempPanel.setVisible(false);
				frame.setVisible(false);
			}
		});
		if (Settings.getSettings("settings").equalsIgnoreCase("")) {
			frame.setLocation((int) Defaults.screenSize.getWidth() / 2 - width / 2, 200);

		}
		frame.setLayout(null);

		blankSpace.setBounds(0, 0, 208, 60);
		buttons.setBounds(0, 60, 208, height - 20);
		content.setBounds(208, 0, 412, height);

		blankSpace.setBackground(Defaults.MAIN);


		title.setForeground(Defaults.FOREGROUND);
		title.setFont(Defaults.SEGOE_LIGHT.deriveFont(24f));

		mainPanel.setBounds(0, 0, width - 20, height);
		mainPanel.setBackground(Defaults.TOP);
		mainPanel.setLayout(null);

		titlePanel.setBounds(0, 20, width - 20, height);
		titlePanel.setBackground(Defaults.TOP);
		titlePanel.add(title);


		settingsButtons.setBackground(Defaults.TOP);
		settingsButtons.setBounds(50, 80, width - 100, height - 100);

		mainPanel.add(settingsButtons);
		mainPanel.add(titlePanel);

		buttons.setBackground(Defaults.MAIN);

		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);

		content.add(generalPage);
		content.add(chaosModePage);
		content.add(generalBotPage);
		content.add(overlayPage);
		content.add(accountsPage);
		content.add(commandsPage);
		content.add(pointsPage);
		content.add(requestsPage);
		content.add(shortcutsPage);
		content.add(personalizationPage);
		content.add(blockedPage);
		content.add(blockedUsersPage);
		content.add(blockedCreatorsPage);
		content.add(loggedIDsPage);
		content.add(languagePage);

		generalPage.setVisible(true);
		overlayPage.setVisible(false);
		accountsPage.setVisible(false);
		commandsPage.setVisible(false);
		pointsPage.setVisible(false);
		requestsPage.setVisible(false);
		shortcutsPage.setVisible(false);
		personalizationPage.setVisible(false);
		blockedPage.setVisible(false);
		blockedUsersPage.setVisible(false);
		blockedCreatorsPage.setVisible(false);
		loggedIDsPage.setVisible(false);
		languagePage.setVisible(false);
		chaosModePage.setVisible(false);


		JButton home = createButton("$HOME_BUTTON$", "\uE10F");
		blankSpace.add(home);


		general.setBackground(Defaults.SELECT);
		general.setUI(selectUI);


		buttons.add(general);//
		buttons.add(generalBot);
		buttons.add(chaosMode);
		buttons.add(outputs);//
		buttons.add(accounts);
		buttons.add(commands);//
		buttons.add(points);//
		buttons.add(cheers);
		buttons.add(requests);//
		buttons.add(shortcuts);//
		buttons.add(personalization);//
		buttons.add(blocked);//
		buttons.add(blockedUsers);//
		buttons.add(blockedCreators);//
		buttons.add(loggedIDs);//
		buttons.add(language); //


		SettingsButton chatBotButton = new SettingsButton("Chat Bot", "Set up Commands, messages, etc.", "\uE99A"); //TODO Localization
		SettingsButton gdButton = new SettingsButton("Geometry Dash", "Set up Geometry Dash related settings", "\uF16E");
		SettingsButton channelPoints = new SettingsButton("Channel Points", "Activate stuff with Channel Points", "\uF126");
		SettingsButton personalizationTab = new SettingsButton("Personalization", "Personalize GDBoard for you", "\uE771");
		SettingsButton languages = new SettingsButton("Language", "Set your language", "\uE12B");
		SettingsButton accountsTab = new SettingsButton("Accounts", "Manage Accounts", "\uE13D");
		SettingsButton windowedTab = new SettingsButton("Windowed Mode", "Switch to windowed mode, overlay is depreciated", "\uE737");


		chatBotButton.addActionListener(e -> {
			clearButtons();
			generalBot.setVisible(true);
			commands.setVisible(true);
			points.setVisible(true);
			click("GENERAL_BOT_SETTINGS");
			mainPanel.setVisible(false);
			tempPanel.setVisible(true);
		});

		gdButton.addActionListener(e -> {
			clearButtons();
			if(KeyListener.isCtrlPressed()){
				JFrame frame = new JFrame();
				frame.setResizable(false);
				frame.setSize(100,100);
				JButton test = new JButton("test");
				frame.add(test);
				test.addActionListener(a -> {
					APIs.getGDComments(0, false, 128);
					test.setText("success");
				});
				frame.setVisible(true);
			}
			else {
				general.setVisible(true);
				chaosMode.setVisible(true);
				outputs.setVisible(true);
				requests.setVisible(true);
				shortcuts.setVisible(true);
				blocked.setVisible(true);
				blockedUsers.setVisible(true);
				blockedCreators.setVisible(true);
				loggedIDs.setVisible(true);
				click("GENERAL_SETTINGS");
				mainPanel.setVisible(false);
				tempPanel.setVisible(true);
			}
		});
		personalizationTab.addActionListener(e -> {
			clearButtons();
			personalization.setVisible(true);
			click("PERSONALIZATION_SETTINGS");
			mainPanel.setVisible(false);
			tempPanel.setVisible(true);
		});
		accountsTab.addActionListener(e -> {
			clearButtons();
			accounts.setVisible(true);
			click("ACCOUNTS_SETTINGS");
			mainPanel.setVisible(false);
			tempPanel.setVisible(true);
		});
		languages.addActionListener(e -> {
			clearButtons();
			language.setVisible(true);
			click("LANGUAGE_SETTINGS");
			mainPanel.setVisible(false);
			tempPanel.setVisible(true);
		});

		settingsButtons.add(chatBotButton);
		settingsButtons.add(gdButton);
		settingsButtons.add(personalizationTab);
		settingsButtons.add(accountsTab);
		if (Settings.getSettings("windowed").equalsIgnoreCase("false")) {
			settingsButtons.add(windowedTab);
		}

		//settingsButtons.add(languages);


		if (Settings.getSettings("windowed").equalsIgnoreCase("false")) {
			buttons.add(windowed);
			window.add(mainPanel);
			tempPanel.setVisible(false);
			tempPanel.setBounds(0, 0, width, height);
			tempPanel.add(blankSpace);
			tempPanel.add(buttons);
			tempPanel.add(content);
			window.add(tempPanel);
		}
		frame.setVisible(false);

		frame.add(mainPanel);

		tempPanel.setVisible(false);
		tempPanel.setBounds(0, 0, width, height);
		tempPanel.add(blankSpace);
		tempPanel.add(buttons);
		tempPanel.add(content);
		frame.add(tempPanel);

	}

	static void openPage(String page) {
		switch (page) {
			case "chatbot":
				clearButtons();
				generalBot.setVisible(true);
				commands.setVisible(true);
				points.setVisible(true);
				click("GENERAL_BOT_SETTINGS");
				mainPanel.setVisible(false);
				tempPanel.setVisible(true);
				break;
			case "gd":
				clearButtons();
				general.setVisible(true);
				chaosMode.setVisible(true);
				outputs.setVisible(true);
				requests.setVisible(true);
				shortcuts.setVisible(true);
				blocked.setVisible(true);
				blockedUsers.setVisible(true);
				blockedCreators.setVisible(true);
				loggedIDs.setVisible(true);
				click("GENERAL_SETTINGS");
				mainPanel.setVisible(false);
				tempPanel.setVisible(true);
				break;
			case "personalization":
				clearButtons();
				personalization.setVisible(true);
				click("PERSONALIZATION_SETTINGS");
				mainPanel.setVisible(false);
				tempPanel.setVisible(true);
				break;
			case "accounts":
				clearButtons();
				accounts.setVisible(true);
				click("ACCOUNTS_SETTINGS");
				mainPanel.setVisible(false);
				tempPanel.setVisible(true);
				break;
			case "languages":
				clearButtons();
				language.setVisible(true);
				click("LANGUAGE_SETTINGS");
				mainPanel.setVisible(false);
				tempPanel.setVisible(true);
				break;
		}
	}

	private static void clearButtons() {
		for (Component component : buttons.getComponents()) {
			if (component instanceof JButton) {
				component.setVisible(false);
			}
		}
	}

	public static void refreshUI() {
		blankSpace.setBackground(Defaults.MAIN);
		mainPanel.setBackground(Defaults.TOP);
		titlePanel.setBackground(Defaults.TOP);
		title.setForeground(Defaults.FOREGROUND);
		settingsButtons.setBackground(Defaults.TOP);
		for (Component component : settingsButtons.getComponents()) {
			if (component instanceof SettingsButton) {
				((SettingsButton) component).refreshUI();
			}
		}
		frame.getContentPane().setBackground(Defaults.TOP);

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
				if (!((JButton) component).getUI().equals(selectUI)) {
					component.setBackground(Defaults.MAIN);
				} else {
					component.setBackground(Defaults.SELECT);
				}

			}
		}
		for (Component component : blankSpace.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				if (!((JButton) component).getUI().equals(selectUI)) {
					component.setBackground(Defaults.MAIN);
				} else {
					component.setBackground(Defaults.SELECT);
				}

			}
		}
	}

	private static JButton createButton(String text, String icon) {

		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);

		JButton button = new JButton();
		LangLabel label = new LangLabel(text);

		label.setFont(Defaults.SEGOE.deriveFont(14f));
		label.setBounds(40, 8, 208, 20);
		label.setForeground(Defaults.FOREGROUND);

		LangLabel iconLabel = new LangLabel(icon);

		iconLabel.setFont(Defaults.SYMBOLS.deriveFont(14f));
		iconLabel.setBounds(15, 9, 20, 20);
		iconLabel.setForeground(Defaults.FOREGROUND);

		button.setLayout(null);
		button.add(label);
		button.add(iconLabel);
		button.setBackground(Defaults.MAIN);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setPreferredSize(new Dimension(208, 38));


		button.addActionListener(e -> {
			for (Component component2 : button.getComponents()) {
				if (component2 instanceof LangLabel) {
					if (!((LangLabel) component2).getIdentifier().equalsIgnoreCase("WINDOWED_SETTINGS")) {
						for (Component componentA : content.getComponents()) {
							if (componentA instanceof JPanel) {
								componentA.setVisible(false);
							}
						}
					}
					RequestsLog.clear();
					switch (((LangLabel) component2).getIdentifier()) {
						case "GENERAL_SETTINGS":
							generalPage.setVisible(true);
							refreshHelper = 0;
							clean++;
							if (clean == 5) {
								System.out.println("cleaned");
								//noinspection CallToSystemGC
								System.gc();
								clean = 0;
							}
							break;
						case "GENERAL_BOT_SETTINGS":
							generalBotPage.setVisible(true);
							clean = 0;
							refreshHelper = 0;
							break;
						case "CHAOS_SETTINGS":
							chaosModePage.setVisible(true);
							clean = 0;
							refreshHelper++;
							if (refreshHelper == 5) {
								System.out.println("refreshed");
								GDHelper.refresh();
								refreshHelper = 0;
							}
							break;
						case "OUTPUTS_SETTINGS":
							overlayPage.setVisible(true);
							clean = 0;
							refreshHelper = 0;
							break;
						case "ACCOUNTS_SETTINGS":
							accountsPage.setVisible(true);
							clean = 0;
							refreshHelper = 0;
							break;
						case "SHORTCUTS_SETTINGS":
							shortcutsPage.setVisible(true);
							clean = 0;
							break;
						case "PERSONALIZATION_SETTINGS":
							personalizationPage.setVisible(true);
							clean = 0;
							refreshHelper = 0;
							break;
						case "BLOCKED_IDS_SETTINGS":
							blockedPage.setVisible(true);
							clean = 0;
							refreshHelper = 0;
							break;
						case "BLOCKED_USERS_SETTINGS":
							blockedUsersPage.setVisible(true);
							clean = 0;
							refreshHelper = 0;
							break;
						case "BLOCKED_CREATORS_SETTINGS":
							blockedCreatorsPage.setVisible(true);
							clean = 0;
							refreshHelper = 0;
							break;
						case "COMMANDS_SETTINGS":
							commandsPage.setVisible(true);
							clean = 0;
							refreshHelper = 0;
							break;
						case "CHANNEL_POINTS_SETTINGS":
							pointsPage.setVisible(true);
							clean = 0;
							refreshHelper = 0;
							break;
						case "REQUESTS_SETTINGS":
							requestsPage.setVisible(true);
							clean = 0;
							refreshHelper = 0;
							break;
						case "LANGUAGE_SETTINGS":
							languagePage.setVisible(true);
							clean = 0;
							refreshHelper = 0;
							break;
						case "HOME_BUTTON":
							mainPanel.setVisible(true);
							tempPanel.setVisible(false);
							clean = 0;
							refreshHelper = 0;
							break;
						case "LOGGED_IDS_SETTINGS":
							loggedIDsPage.setVisible(true);
							clean = 0;
							refreshHelper = 0;
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

			if (!text.equalsIgnoreCase("$HOME_BUTTON$")) {
				button.setUI(selectUI);
				button.setBackground(Defaults.SELECT);
			}
		});
		return button;
	}

	static void click(String identifier) {
		if (!identifier.equals("WINDOWED_SETTINGS")) {
			for (Component componentA : content.getComponents()) {
				if (componentA instanceof JPanel) {
					componentA.setVisible(false);
				}
			}
		}
		RequestsLog.clear();
		switch (identifier) {
			case "GENERAL_SETTINGS":
				generalPage.setVisible(true);
				break;
			case "CHAOS_SETTINGS":
				chaosModePage.setVisible(true);
				clean = 0;
				break;
			case "OUTPUTS_SETTINGS":
				overlayPage.setVisible(true);
				clean = 0;
				break;
			case "GENERAL_BOT_SETTINGS":
				generalBotPage.setVisible(true);
				clean = 0;
				break;
			case "ACCOUNTS_SETTINGS":
				accountsPage.setVisible(true);
				clean = 0;
				break;
			case "SHORTCUTS_SETTINGS":
				shortcutsPage.setVisible(true);
				clean = 0;
				break;
			case "PERSONALIZATION_SETTINGS":
				personalizationPage.setVisible(true);
				clean = 0;
				break;
			case "BLOCKED_IDS_SETTINGS":
				blockedPage.setVisible(true);
				clean = 0;
				break;
			case "BLOCKED_USERS_SETTINGS":
				blockedUsersPage.setVisible(true);
				clean = 0;
				break;
			case "BLOCKED_CREATORS_SETTINGS":
				blockedCreatorsPage.setVisible(true);
				clean = 0;
				break;
			case "COMMANDS_SETTINGS":
				commandsPage.setVisible(true);
				clean = 0;
				break;
			case "CHANNEL_POINTS_SETTINGS":
				pointsPage.setVisible(true);
				clean = 0;
				break;
			case "REQUESTS_SETTINGS":
				requestsPage.setVisible(true);
				clean = 0;
				break;
			case "LANGUAGE_SETTINGS":
				languagePage.setVisible(true);
				clean = 0;
				break;
			case "WINDOWED_SETTINGS":
				new Thread(() -> {
					String option = DialogBox.showDialogBox("$SWITCH_TO_WINDOWED_TITLE$", "$SWINTH_TO_WINDOWED_INFO$", "$SWITCH_TO_WINDOWED_SUBINFO$", new String[]{"$YES$", "$NO$"});
					if (option.equalsIgnoreCase("YES")) {
						try {
							Settings.writeSettings("windowed", "true");
						} catch (Exception f) {
							f.printStackTrace();
						}
						Main.close();
					}
				}).start();
				clean = 0;
				break;
			case "LOGGED_IDS_SETTINGS":
				loggedIDsPage.setVisible(true);
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

		for (Component component : buttons.getComponents()) {
			if (component instanceof JButton) {
				((JButton) component).setUI(defaultUI);
				component.setBackground(Defaults.MAIN);
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof LangLabel) {
						if (((LangLabel) component2).getIdentifier().equals(identifier)) {
							((JButton) component).setUI(selectUI);
							component.setBackground(Defaults.SELECT);
						}
					}
				}
			}
		}
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
	static void setLocation(Point point) {
		window.setLocation(point);
	}
	//endregion
}
