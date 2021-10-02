package com.alphalaneous.Tabs;

import com.alphalaneous.Assets;
import com.alphalaneous.ChatbotTab.GeometryDashCommands;
import com.alphalaneous.Components.*;
import com.alphalaneous.Defaults;
import com.alphalaneous.SettingsPanels.*;
import com.alphalaneous.Windows.Window;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

import static com.alphalaneous.Defaults.defaultUI;

public class ChatbotTab {
	public static JPanel window = new JPanel();
	private static final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
	private static final JScrollPane buttonsScroll = new SmoothScrollPane(buttons);
	private static final JPanel content = new JPanel();
	private static final JButtonUI selectUI = new JButtonUI();
	private static final JPanel generalBotPage = ChatbotSettings.createPanel();
	private static final JPanel geometryDashCommandsPage = GeometryDashCommands.createPanel();
	private static final JPanel defaultCommandsPage = CommandSettings.createPanel();
	private static final JPanel pointsPage = ChannelPointSettings.createPanel();
	private static final JPanel spamProtectionPage = SpamProtectionSettings.createPanel();

	private static final JPanel chatbotPanel = new JPanel(null);

	private static final JPanel commandsSection = new TitleSeparator("$COMMANDS_SECTION_TITLE$");

	private static final FunctionButton generalBotButton = createButton("$CHATBOT_SETTINGS$", "\uF130", () -> {
		generalBotPage.setVisible(true);
		return null;
	});
	private static final FunctionButton customCommandsButton = createButton("$CUSTOM_COMMANDS_SETTINGS$", "\uF03C", () -> {
		return null;
	});
	private static final FunctionButton defaultCommandsButton = createButton("$COMMANDS_SETTINGS$", "\uF4EA", () -> {
		defaultCommandsPage.setVisible(true);
		return null;
	});
	private static final FunctionButton geometryDashCommandsButton = createButton("$GEOMETRY_DASH_COMMANDS_SETTINGS$", "\uF26F", () -> {
		geometryDashCommandsPage.setVisible(true);
		return null;
	});
	private static final FunctionButton channelPointsButton = createButton("$CHANNEL_POINTS_SETTINGS$", "\uF52B", () -> {
		pointsPage.setVisible(true);
		return null;
	});
	private static final FunctionButton timersButton = createButton("$TIMERS_SETTINGS$", "\uF210", () -> {
		return null;
	});
	private static final FunctionButton spamProtectionButton = createButton("$SPAM_PROTECTION_SETTINGS$", "\uF048", () -> {
		spamProtectionPage.setVisible(true);
		return null;
	});

	public static void createPanel() {

		int width = 750;
		int height = 662;

		buttonsScroll.setBounds(0, 60, 198, height-98);
		buttonsScroll.getViewport().setBackground(Defaults.COLOR);
		buttonsScroll.setBackground(Defaults.COLOR);

		content.setBounds(208, 0, 524, height);


		buttons.setBackground(Defaults.COLOR);


		content.setBackground(Defaults.COLOR3);
		content.setLayout(null);

		buttons.add(new JPanel(){{
			setOpaque(false);
			setBackground(new Color(0,0,0,0));
			setPreferredSize(new Dimension(100, 10));
		}});

		buttons.add(generalBotButton);
		buttons.add(commandsSection);
		//buttons.add(customCommandsButton);
		buttons.add(defaultCommandsButton);
		//buttons.add(geometryDashCommandsButton);
		buttons.add(createSeparator());
		buttons.add(channelPointsButton);
		//buttons.add(timersButton);
		//buttons.add(spamProtectionButton);

		content.add(generalBotPage);
		//content.add(geometryDashCommandsPage);
		content.add(defaultCommandsPage);
		content.add(pointsPage);
		//content.add(spamProtectionPage);

		buttons.setPreferredSize(new Dimension(208, 400));

		chatbotPanel.setVisible(false);
		chatbotPanel.setBounds(16, 0, width, height);
		chatbotPanel.setPreferredSize(new Dimension(width, height));
		chatbotPanel.add(buttonsScroll);
		chatbotPanel.add(content);

		Window.add(chatbotPanel, Assets.commands, () -> click(generalBotButton));

	}

	public static void refreshUI() {
		buttonsScroll.getVerticalScrollBar().setUI(new ScrollbarUI());
		buttonsScroll.getViewport().setBackground(Defaults.COLOR);
		buttonsScroll.setBackground(Defaults.COLOR);
		chatbotPanel.setBackground(Defaults.COLOR);
		selectUI.setBackground(Defaults.COLOR4);
		selectUI.setHover(Defaults.COLOR5);
		selectUI.setSelect(Defaults.COLOR4);
		buttons.setBackground(Defaults.COLOR);
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
		((TitleSeparator) commandsSection).refreshTextColor();
	}


	public static void resize(int width, int height){

		chatbotPanel.setBounds(16, 0, width, height);
		buttonsScroll.setBounds(0, 0, 198, height-38);

		content.setBounds(198, 0, width, height);

	}

	public static void showSettings() {
		chatbotPanel.setVisible(true);
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
	private static class FunctionButton extends CurvedButtonAlt {

		private final Callable<Void> method;
		private final String text;

		FunctionButton(String text, String icon, Callable<Void> method){
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
				if (component instanceof FunctionButton) {
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
	private static FunctionButton createButton(String text, String icon, Callable<Void> method) {
		return new FunctionButton(text, icon, method);
	}


	static void click(FunctionButton button) {
		for (Component componentA : content.getComponents()) {
			if (componentA instanceof JPanel) {
				componentA.setVisible(false);
			}
		}
		RequestsLog.clear();
		button.runMethod();
	}
}
