package SettingsPanels;

import java.awt.Color;

import javax.swing.JPanel;

public class BlockedIDsSettings {
	public static JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Color.RED);
		return panel;
		
	}
}
