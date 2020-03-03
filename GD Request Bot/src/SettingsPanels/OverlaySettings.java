package SettingsPanels;

import Main.CheckboxButton;

import javax.swing.JButton;
import javax.swing.JPanel;

public class OverlaySettings {

	public static JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Main.Defaults.SUB_MAIN);

		return panel;
		
	}
	
}
