package SettingsPanels;

import Main.Defaults;

import java.awt.*;

import javax.swing.*;

public class ShortcutSettings {
	public static JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);

		return panel;

	}
}