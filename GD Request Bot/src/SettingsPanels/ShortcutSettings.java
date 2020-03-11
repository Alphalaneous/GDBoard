package SettingsPanels;

import Main.Defaults;
import Main.FancyTextArea;

import java.awt.*;

import javax.swing.*;

public class ShortcutSettings {
	private static FancyTextArea input = new FancyTextArea(false);
	public static JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		input.setBounds(25,173,365, 32);
		panel.add(input);
		return panel;

	}
}