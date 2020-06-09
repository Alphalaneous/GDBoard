package Main.SettingsPanels;

import Main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class WindowedSettings {
	private static JButtonUI defaultUI = new JButtonUI();
	public static boolean onTopOption = true;
	private static CheckboxButton onTop = createButton("Always On Top", 20);
 	private static JPanel panel = new JPanel();

	public static JPanel createPanel() {

		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		onTop.setChecked(true);
		onTop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				onTopOption = onTop.getSelectedState();
				Windowed.setOnTop(onTop.getSelectedState());
				((InnerWindow) Windowed.window).setMinimize(!onTopOption);
			}
		});
		panel.setLayout(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		panel.add(onTop);
		return panel;
	}
	public static void loadSettings(){
		try {
			if(!Settings.getSettings("onTop").equalsIgnoreCase("")) {
				onTopOption = Boolean.parseBoolean(Settings.getSettings("onTop"));
				onTop.setChecked(onTopOption);
				Windowed.setOnTop(onTopOption);
				((InnerWindow) Windowed.window).setMinimize(!onTopOption);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void setSettings(){
		try {
			Settings.writeSettings("onTop", String.valueOf(onTopOption));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static CheckboxButton createButton(String text, int y){
		CheckboxButton button = new CheckboxButton(text);
		button.setBounds(25,y,365,30);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		button.refresh();
		return button;
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
			if(component instanceof JTextArea){
				((FancyTextArea) component).refreshAll();
			}
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}

		}
	}
}
