package Main.SettingsPanels;

import Main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class PersonalizationSettings {
	private static CurvedButton windowedButton = new CurvedButton("Switch to Windowed Mode (Requires Restart)");
	private static JButtonUI defaultUI = new JButtonUI();
	private static JPanel panel = new JPanel(null);


	public static JPanel createPanel() {
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);

		try {
			if(Settings.getSettings("windowed").equalsIgnoreCase("true")){
				windowedButton.setLText("Switch to Overlay Mode (Requires Restart)");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		windowedButton.setBounds(25,25, 365,30);
		windowedButton.setPreferredSize(new Dimension(365,30));
		windowedButton.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		windowedButton.setUI(defaultUI);
		windowedButton.setForeground(Defaults.FOREGROUND);
		windowedButton.setBackground(Defaults.BUTTON);
		windowedButton.refresh();

		windowedButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Object[] options = {"Yes", "No"};
				int n = JOptionPane.showOptionDialog(SettingsWindow.window,
						"Close GDBoard and Apply Changes?",
						"Close? (Temporary Menu)", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (n == 0) {
					try {
						if (Settings.windowedMode) {

							Settings.writeSettings("windowed", "false");
						} else {
							Settings.writeSettings("windowed", "true");
						}
						Thread.sleep(100);
						Main.close();
					}
					catch (Exception ignored){
					}
					Main.close();
				}
			}
		});
		panel.add(windowedButton);
		return panel;
		
	}
	public static void refreshUI() {
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);

		panel.setBackground(Defaults.SUB_MAIN);
		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.BUTTON);
			}
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);
			}
			if(component instanceof JTextArea){
				((FancyTextArea) component).refreshAll();
			}
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}
		}
	}
}
