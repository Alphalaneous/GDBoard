package com.alphalaneous.SettingsPanels;

import com.alphalaneous.APIs;
import com.alphalaneous.ChannelPointReward;
import com.alphalaneous.Components.*;
import com.alphalaneous.Defaults;
import com.alphalaneous.TwitchAccount;
import com.alphalaneous.Windows.CommandEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.*;
import java.util.*;

import static com.alphalaneous.Defaults.settingsButtonUI;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class ChannelPointSettings {
	private static int i = 0;
	private static double height = 0;
	private static JLabel commandLabel = new JLabel();
	private static JPanel commandsPanel = new JPanel();
	private static JScrollPane scrollPane = new JScrollPane(commandsPanel);
	private static JPanel panel = new JPanel();
	private static JPanel titlePanel = new JPanel();
	private static RoundedJButton refreshPoints = new RoundedJButton("\uE149", "$REFRESH_CHANNEL_POINTS_TOOLTIP$");
	private static LangLabel notAvailable = new LangLabel("$CHANNEL_POINTS_UNAVAILABLE$");

	public static JPanel createPanel() {

		LangLabel label = new LangLabel("$POINTS_LIST$");
		label.setForeground(Defaults.FOREGROUND);
		label.setFont(Defaults.SEGOE.deriveFont(14f));
		label.setBounds(25, 20, label.getPreferredSize().width + 5, label.getPreferredSize().height + 5);

		panel.add(label);

		refreshPoints.setBackground(Defaults.BUTTON);
		refreshPoints.setBounds(370, 16, 30, 30);
		refreshPoints.setFont(Defaults.SYMBOLS.deriveFont(14f));
		refreshPoints.setForeground(Defaults.FOREGROUND);
		refreshPoints.setUI(settingsButtonUI);
		refreshPoints.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//CommandEditor.showEditor("points", "", false);
				refresh();
			}
		});

		panel.add(refreshPoints);


		notAvailable.setForeground(Defaults.FOREGROUND);
		notAvailable.setFont(Defaults.SEGOE.deriveFont(16f));

		titlePanel.setBounds(0, 0, 415, 50);
		titlePanel.setLayout(null);
		titlePanel.setDoubleBuffered(true);
		titlePanel.setBackground(Defaults.TOP);

		commandLabel.setFont(Defaults.SEGOE.deriveFont(14f));
		commandLabel.setForeground(Defaults.FOREGROUND);
		commandLabel.setBounds(50,17,commandLabel.getPreferredSize().width+5, commandLabel.getPreferredSize().height + 5);
		titlePanel.add(commandLabel);


		panel.setLayout(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		commandsPanel.setDoubleBuffered(true);
		commandsPanel.setBounds(0, 0, 400, 0);
		commandsPanel.setPreferredSize(new Dimension(400, 0));
		commandsPanel.setBackground(Defaults.SUB_MAIN);
		commandsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
		scrollPane.setBounds(0, 60, 412, 562);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Defaults.SUB_MAIN);
		scrollPane.setPreferredSize(new Dimension(412, 562));
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
		/*
		HashMap<String, ButtonInfo> existingCommands = new HashMap<>();
		try {
			Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/points/");
			if (Files.exists(comPath)) {
				Stream<Path> walk1 = Files.walk(comPath, 1);
				for (Iterator<Path> it = walk1.iterator(); it.hasNext(); ) {
					Path path = it.next();
					String[] file = path.toString().split("\\\\");
					String fileName = file[file.length - 1];
					if (fileName.endsWith(".js")) {
						existingCommands.put(fileName.substring(0, fileName.length()-3), new ButtonInfo( path));
					}
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		TreeMap<String, ButtonInfo> sorted = new TreeMap<>(existingCommands);

		for(Map.Entry<String,ButtonInfo> entry : sorted.entrySet()) {
			String key = entry.getKey();
			addButton(key);
		}*/
		panel.setBounds(0, 0, 415, 622);
		panel.add(scrollPane);
		return panel;
	}

	public static void refresh(){

		commandsPanel.removeAll();
		height = 0;
		commandsPanel.setBounds(0, 0, 400, (int) (height + 4));
		commandsPanel.setPreferredSize(new Dimension(400, (int) (height + 4)));

		/*HashMap<String, ButtonInfo> existingCommands = new HashMap<>();
		try {
			Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/points/");
			if (Files.exists(comPath)) {
				Stream<Path> walk1 = Files.walk(comPath, 1);
				for (Iterator<Path> it = walk1.iterator(); it.hasNext(); ) {
					Path path = it.next();
					String[] file = path.toString().split("\\\\");
					String fileName = file[file.length - 1];
					if (fileName.endsWith(".js")) {
						existingCommands.put(fileName.substring(0, fileName.length()-3), new ButtonInfo( path));
					}
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		TreeMap<String, ButtonInfo> sorted = new TreeMap<>(existingCommands);

		for(Map.Entry<String,ButtonInfo> entry : sorted.entrySet()) {
			String key = entry.getKey();
			addButton(key);
		}*/
		if(TwitchAccount.broadcaster_type.equalsIgnoreCase("affiliate")
				|| TwitchAccount.broadcaster_type.equalsIgnoreCase("partner")) {
			commandsPanel.remove(notAvailable);
			ArrayList<ChannelPointReward> rewards = APIs.getChannelPoints();
			for (ChannelPointReward reward : rewards) {
				addButton(reward.getTitle(), reward.getBgColor());
			}
		}
		else{
			commandsPanel.add(notAvailable);
		}

	}
	public static class ButtonInfo{

		public Path path;

		ButtonInfo(Path path){
			this.path = path;
		}

	}



	public static void addButton(String command, Color color) {
		i++;
		if ((i-1) % 2 == 0) {
			height = height + 39;

			commandsPanel.setBounds(0, 0, 400, (int) (height + 4));
			commandsPanel.setPreferredSize(new Dimension(400, (int) (height + 4)));
			if(i > 0) {
				scrollPane.updateUI();
			}
		}
		JButtonUI colorUI = new JButtonUI();
		colorUI.setBackground(color);
		colorUI.setHover(color);
		colorUI.setSelect(color.darker());
		CurvedButton button = new CurvedButton(command);
		button.setBackground(color);
		button.setUI(colorUI);
		double brightness = Math.sqrt(color.getRed()* color.getRed() * .241 +
						color.getGreen() * color.getGreen() * .691 +
						color.getBlue() * color.getBlue() * .068);

		if(brightness > 130){
			button.setForeground(Color.BLACK);
		}
		else{
			button.setForeground(Color.WHITE);
		}
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SEGOE.deriveFont(14f));
		button.setPreferredSize(new Dimension(170, 35));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				CommandEditor.showEditor("points", command, false);
			}
		});
		button.refresh();
		commandsPanel.add(button);
	}


	public static void refreshUI(){
		panel.setBackground(Defaults.TOP);
		titlePanel.setBackground(Defaults.TOP);
		commandLabel.setForeground(Defaults.FOREGROUND);
		commandsPanel.setBackground(Defaults.SUB_MAIN);
		notAvailable.setForeground(Defaults.FOREGROUND);


		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());

		for (Component component : commandsPanel.getComponents()) {
			/*if (component instanceof CurvedButton) {

				if(component.getForeground().equals(Defaults.FOREGROUND2)) {
					component.setForeground(Defaults.FOREGROUND2);
				}
				else{
					component.setForeground(Defaults.FOREGROUND);
				}
				component.setBackground(Defaults.BUTTON);
				((CurvedButton) component).refresh();
			}*/
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);

			}
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}
		}

		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				component.setForeground(Defaults.FOREGROUND);
				component.setBackground(Defaults.BUTTON);
			}
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);
			}
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}
		}
	}
}
