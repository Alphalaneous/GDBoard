package com.alphalaneous.Tabs;

import com.alphalaneous.*;
import com.alphalaneous.Components.*;
import com.alphalaneous.SettingsPanels.*;
import com.alphalaneous.Windows.Window;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

import static com.alphalaneous.Defaults.defaultUI;

public class SettingsTab {
	public static JPanel window = new JPanel();
	private static final JPanel buttonsParent = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
	private static final JPanel buttons = new JPanel(new GridBagLayout());
	private static final JScrollPane buttonsScroll = new SmoothScrollPane(buttonsParent);
	private static final JPanel content = new JPanel();
	private static final JButtonUI selectUI = new JButtonUI();
	private static final JPanel generalPage = RequestsSettings.createPanel();
	private static final JPanel messagePage = MessageSettings.createPanel();
	private static final JPanel overlayPage = OutputSettings.createPanel();
	private static final JPanel accountsPage = AccountSettings.createPanel();
	//private static final JPanel commandsPage = CommandSettings.createPanel();
	private static final JPanel requestsPage = FiltersSettings.createPanel();
	private static final JPanel shortcutsPage = ShortcutSettings.createPanel();
	private static final JPanel personalizationPage = PersonalizationSettings.createPanel();
	private static final JPanel blockedPage = BlockedIDSettings.createPanel();
	private static final JPanel blockedUsersPage = BlockedUserSettings.createPanel();
	private static final JPanel blockedCreatorsPage = BlockedCreatorSettings.createPanel();
	private static final JPanel loggedIDsPage = RequestsLog.createPanel();
	private static final JPanel languagePage = LanguageSettings.createPanel();
	private static final LangLabel title = new LangLabel("$SETTINGS_TITLE$");
	private static final JPanel settingsPanel = new JPanel(null);

	private static final JPanel mainPanel = new JPanel();
	private static final JPanel titlePanel = new JPanel();

	private static final JPanel botSection = new TitleSeparator("$BOT_SECTION_TITLE$");
	private static final JPanel GDSection = new TitleSeparator("$GD_SECTION_TITLE$");
	private static final JPanel userSection = new TitleSeparator("$USER_SECTION_TITLE$");

	private static final SettingsButton requests = createButton("$REQUESTS_SETTINGS$", "\uF26F", () -> {
		generalPage.setVisible(true);
		return null;
	});
	private static final SettingsButton messages = createButton("$MESSAGE_SETTINGS$", "\uF26F", () -> {
		messagePage.setVisible(true);
		//TestListView.loadIDs();
		return null;
	});
	private static final SettingsButton outputs = createButton("$OUTPUTS_SETTINGS$", "\uF68D", () -> {
		overlayPage.setVisible(true);
		return null;
	});
	private static final SettingsButton accounts = createButton("$ACCOUNTS_SETTINGS$", "\uF133", () -> {
		accountsPage.setVisible(true);
		return null;
	});
	private static final SettingsButton commands = createButton("$COMMANDS_SETTINGS$", "\uF0B4", () -> {
		//commandsPage.setVisible(true);
		return null;
	});
	private static final SettingsButton cheers = createButton("$CHEERS_SETTINGS$", "\uF157", () -> {
		//cheersPage.setVisible(true);
		return null;
	});
	private static final SettingsButton filters = createButton("$FILTERS_SETTINGS$", "\uF309", () -> {
		requestsPage.setVisible(true);
		return null;
	});
	private static final SettingsButton shortcuts = createButton("$SHORTCUTS_SETTINGS$", "\uF105", () -> {
		shortcutsPage.setVisible(true);
		return null;
	});
	private static final SettingsButton personalization = createButton("$PERSONALIZATION_SETTINGS$", "\uF1B9", () -> {
		personalizationPage.setVisible(true);
		return null;
	});
	private static final SettingsButton blocked = createButton("$BLOCKED_IDS_SETTINGS$", "\uF313", () -> {
		blockedPage.setVisible(true);
		return null;
	});
	private static final SettingsButton blockedUsers = createButton("$BLOCKED_USERS_SETTINGS$", "\uF5D2", () -> {
		blockedUsersPage.setVisible(true);
		return null;
	});
	private static final SettingsButton blockedCreators = createButton("$BLOCKED_CREATORS_SETTINGS$", "\uF5D1", () -> {
		blockedCreatorsPage.setVisible(true);
		return null;
	});
	private static final SettingsButton loggedIDs = createButton("$LOGGED_IDS_SETTINGS$", "\uF0D6", () -> {
		loggedIDsPage.setVisible(true);
		RequestsLog.loadIDs();
		return null;
	});
	private static final SettingsButton language = createButton("$LANGUAGE_SETTINGS$", "\uE12B", () -> {
		languagePage.setVisible(true);
		return null;
	});
	private static final GridBagConstraints gbc = new GridBagConstraints();

	public static void createPanel() {

		int width = 750;
		int height = 662;

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(0, 0, 8, 0);
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.NONE;

		buttonsScroll.setBounds(0, 60, 198, height);
		buttonsScroll.getViewport().setBackground(Defaults.COLOR);
		buttonsScroll.setBackground(Defaults.COLOR);


		content.setBounds(208, 0, 524, height);

		title.setForeground(Defaults.FOREGROUND_A);
		title.setFont(Defaults.SEGOE_FONT.deriveFont(24f));

		mainPanel.setBounds(0, 0, width - 20, height);
		//mainPanel.setBackground(Defaults.TOP);
		mainPanel.setBackground(Defaults.ACCENT);

		mainPanel.setLayout(null);

		titlePanel.setBounds(0, 20, width - 20, height);
		titlePanel.setBackground(Defaults.COLOR6);
		titlePanel.add(title);

		mainPanel.add(titlePanel);

		buttons.setBackground(Defaults.COLOR);
		buttonsParent.setBackground(Defaults.COLOR);

		content.setBackground(Defaults.COLOR3);
		content.setLayout(null);

		content.add(generalPage);
		content.add(messagePage);
		content.add(overlayPage);
		content.add(accountsPage);
		//content.add(commandsPage);
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
		messagePage.setVisible(false);
		accountsPage.setVisible(false);
		//commandsPage.setVisible(false);
		requestsPage.setVisible(false);
		shortcutsPage.setVisible(false);
		personalizationPage.setVisible(false);
		blockedPage.setVisible(false);
		blockedUsersPage.setVisible(false);
		blockedCreatorsPage.setVisible(false);
		loggedIDsPage.setVisible(false);
		languagePage.setVisible(false);

		requests.setBackground(Defaults.COLOR4);
		requests.setUI(selectUI);
		buttons.add(new JPanel(){{
			setOpaque(false);
			setBackground(new Color(0,0,0,0));
			setPreferredSize(new Dimension(100, 10));
		}}, gbc);

		buttons.add(userSection, gbc);
		buttons.add(accounts, gbc);
		buttons.add(personalization, gbc);

		buttons.add(createSeparator(), gbc);
		buttons.add(GDSection, gbc);
		buttons.add(requests, gbc);
		//buttons.add(messages, gbc);
		buttons.add(filters, gbc);
		buttons.add(outputs, gbc);
		buttons.add(shortcuts, gbc);
		buttons.add(blocked, gbc);
		buttons.add(blockedUsers, gbc);
		buttons.add(blockedCreators, gbc);
		buttons.add(loggedIDs, gbc);

		buttonsParent.add(buttons);
		//width 208

		settingsPanel.setVisible(false);
		settingsPanel.setBounds(16, 0, width, height);
		settingsPanel.setPreferredSize(new Dimension(width, height));
		settingsPanel.add(buttonsScroll);
		settingsPanel.add(content);
		Window.add(settingsPanel, Assets.settings, () -> click(accounts));

	}

	public static void refreshUI() {
		mainPanel.setBackground(Defaults.ACCENT);

		titlePanel.setBackground(Defaults.COLOR6);
		title.setForeground(Defaults.FOREGROUND_A);
		buttonsScroll.getVerticalScrollBar().setUI(new ScrollbarUI());
		buttonsScroll.getViewport().setBackground(Defaults.COLOR);
		buttonsScroll.setBackground(Defaults.COLOR);
		settingsPanel.setBackground(Defaults.COLOR);
		selectUI.setBackground(Defaults.COLOR4);
		selectUI.setHover(Defaults.COLOR5);
		selectUI.setSelect(Defaults.COLOR4);
		buttons.setBackground(Defaults.COLOR);
		buttonsParent.setBackground(Defaults.COLOR);
		content.setBackground(Defaults.COLOR3);
		for (Component component : buttons.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND_A);
					}
				}
				if (!((JButton) component).getUI().equals(selectUI)) {
					component.setBackground(Defaults.COLOR);
				} else {
					component.setBackground(Defaults.COLOR4);
				}

			}
		}
		((TitleSeparator) GDSection).refreshTextColor();
		((TitleSeparator) userSection).refreshTextColor();
		((TitleSeparator) botSection).refreshTextColor();
	}


	public static void resize(int width, int height){
		settingsPanel.setBounds(16, 0, width, height);
		buttonsScroll.setBounds(0, 0, 198, height-40);

		content.setBounds(198, 0, width, height);

	}

	public static void showSettings() {
		click(accounts);
		settingsPanel.setVisible(true);
	}

	private static JPanel createSeparator(){

		JPanel panel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g.setColor(Defaults.FOREGROUND_B);
				g2.drawLine(14, 10, 170,10);
				super.paintComponent(g);
			}
		};
		panel.setOpaque(false);
		panel.setBackground(new Color(0,0,0,0));
		panel.setPreferredSize(new Dimension(170,16));

		return panel;
	}

	private static class TitleSeparator extends JPanel {

		private final LangLabel label = new LangLabel("");

		public TitleSeparator(String title) {
			setLayout(null);
			label.setTextLang(title);
			label.setFont(Defaults.MAIN_FONT.deriveFont(12f));
			label.setForeground(Defaults.FOREGROUND_A);
			label.setBounds(14, 0, 170, 30);
			setPreferredSize(new Dimension(170, 30));
			setOpaque(false);
			setBackground(new Color(0, 0, 0, 0));
			add(label);
		}

		public void refreshTextColor(){
			label.setForeground(Defaults.FOREGROUND_A);
		}

	}
	private static class SettingsButton extends CurvedButtonAlt {

		private final Callable<Void> method;
		private final String text;

		SettingsButton(String text, String icon, Callable<Void> method){
			super("");
			this.text = text;
			this.method = method;
			selectUI.setBackground(Defaults.COLOR4);
			selectUI.setHover(Defaults.COLOR5);

			LangLabel label = new LangLabel(text);
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			label.setFont(Defaults.MAIN_FONT.deriveFont(14f));
			label.setBounds(40, 7, 208, 20);
			label.setForeground(Defaults.FOREGROUND_A);

			LangLabel iconLabel = new LangLabel(icon);

			iconLabel.setFont(Defaults.SYMBOLS.deriveFont(14f));
			iconLabel.setBounds(15, 6, 20, 20);
			iconLabel.setForeground(Defaults.FOREGROUND_A);

			setLayout(null);
			add(label);
			add(iconLabel);
			setBackground(Defaults.COLOR);
			setUI(defaultUI);
			setForeground(Defaults.FOREGROUND_A);
			setBorder(BorderFactory.createEmptyBorder());
			setPreferredSize(new Dimension(180, 32));

			addActionListener(e -> runMethod());
		}
		public void runMethod(){
			RequestsLog.clear();
			for (Component componentA : content.getComponents()) {
				if (componentA instanceof JPanel) {
					componentA.setVisible(false);
				}
			}
			try {
				method.call();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			for (Component component : buttons.getComponents()) {
				if (component instanceof SettingsButton) {
					((JButton) component).setUI(defaultUI);
					component.setBackground(Defaults.COLOR);
				}
			}
			if (!text.equalsIgnoreCase("$BACK_BUTTON$")) {
				setUI(selectUI);
				setBackground(Defaults.COLOR4);
			}
		}

	}

	private static SettingsButton createButton(String text, String icon, Callable<Void> method) {
		return new SettingsButton(text, icon, method);
	}


	static void click(SettingsButton button) {
		for (Component componentA : content.getComponents()) {
			if (componentA instanceof JPanel) {
				componentA.setVisible(false);
			}
		}
		RequestsLog.clear();
		button.runMethod();
	}
}
