package SettingsPanels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OverlaySettings {

	public static JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Main.Defaults.SUB_MAIN);
		
		JButton toggleRequests = new Main.CheckBoxButton("Show request overlays (Requires Restart)", 415,30).createButton();
		
		
		panel.add(toggleRequests);
		return panel;
		
	}
	
}
