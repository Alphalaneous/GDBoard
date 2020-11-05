package Main.SettingsPanels;

import Main.*;
import Main.Components.CheckboxButton;
import Main.Components.FancyTextArea;
import Main.Windows.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class WindowedSettings {
	public static boolean onTopOption = false;
	private static CheckboxButton onTop = createButton("$ALWAYS_ON_TOP$", 20);
 	private static JPanel panel = new JPanel();

	public static JPanel createPanel() {

		onTop.setChecked(false);
		onTop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				onTopOption = onTop.getSelectedState();
				Window.setOnTop(onTop.getSelectedState());
			}
		});
		panel.setLayout(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		panel.add(onTop);
		return panel;
	}

	private static CheckboxButton createButton(String text, int y){
		CheckboxButton button = new CheckboxButton(text, WindowedSettings.class);
		button.setBounds(25,y,365,30);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		button.refresh();
		return button;
	}
	public static void refreshUI() {

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
