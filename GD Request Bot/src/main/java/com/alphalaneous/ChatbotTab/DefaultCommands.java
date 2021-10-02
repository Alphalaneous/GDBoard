package com.alphalaneous.ChatbotTab;

import com.alphalaneous.CommandData;
import com.alphalaneous.Components.CommandListElement;
import com.alphalaneous.Components.SmoothScrollPane;
import com.alphalaneous.Defaults;
import com.alphalaneous.LoadCommands;
import com.alphalaneous.Panels.SettingsTitle;

import javax.swing.*;
import java.awt.*;

public class DefaultCommands {
	private static final JPanel panel = new JPanel(null);
	private static final JPanel listPanel = new JPanel();
	private static final JPanel borderPanel = new JPanel(new BorderLayout());
	private static final SmoothScrollPane scrollPane = new SmoothScrollPane(borderPanel);
	private static final GridBagConstraints gbc = new GridBagConstraints();
	private static final JPanel titlePanel = new JPanel(null);

	public static JPanel createPanel() {
		listPanel.setLayout(new GridBagLayout());
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(6, 9, 0, 2);
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		borderPanel.add(listPanel, BorderLayout.NORTH);
		borderPanel.setOpaque(false);
		borderPanel.setBackground(Defaults.COLOR);
		titlePanel.setBounds(0,0,524,80);
		titlePanel.setBackground(Defaults.COLOR6);
		listPanel.setBackground(Defaults.COLOR3);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 524, 622);
		panel.setBackground(Defaults.COLOR3);
		titlePanel.add(new SettingsTitle("$DEFAULT_COMMANDS_SETTINGS$"));
		scrollPane.setBounds(0, 80, 524, 542);
		scrollPane.setOpaque(false);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		listPanel.setOpaque(false);
		panel.setOpaque(false);
		panel.add(titlePanel);
		panel.add(scrollPane);
		return panel;

	}

	public static void loadCommands(){
		for(CommandData commandData : LoadCommands.defaultCommands){
			listPanel.add(new CommandListElement(commandData), gbc);
		}
	}

	public static void refreshUI() {
		panel.setBackground(Defaults.COLOR3);
		titlePanel.setBackground(Defaults.COLOR6);
		listPanel.setBackground(Defaults.COLOR3);
		borderPanel.setBackground(Defaults.COLOR);
	}
	public static void resizeHeight(int height){
		panel.setBounds(0, 0, 524, height);
		scrollPane.setBounds(0, 80, 524, height-80);
	}
}
