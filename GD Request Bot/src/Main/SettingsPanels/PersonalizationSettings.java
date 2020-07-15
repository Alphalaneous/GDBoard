package Main.SettingsPanels;

import Main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class PersonalizationSettings {
	private static CurvedButton windowedButton = new CurvedButton("Switch to Windowed Mode");
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
				windowedButton.setLText("Switch to Overlay Mode");
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
				new Thread(()->{
					String option = null;
					try {
						if(Settings.getSettings("windowed").equalsIgnoreCase("true")){
							option = DialogBox.showDialogBox("Switch to Overlay?", "<html>If you play GD in fullscreen, this may not work, move it to another monitor by dragging the time to fix. Default is 'Home' key to open. You'll have to reopen GDBoard.", "", new String[]{"Yes", "No"});
						}
						else{
							option = DialogBox.showDialogBox("Switch to Windowed?", "This will close GDboard and set to Windowed mode.", "You will have to reopen GDBoard.", new String[]{"Yes", "No"});
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					if (option.equalsIgnoreCase("Yes")) {
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
				}).start();
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

			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}
		}
	}
}
