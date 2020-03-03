package SettingsPanels;

import Main.CheckboxButton;
import Main.Defaults;
import Main.CurvedButton;
import Main.JButtonUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class GeneralSettings {
	private static JButtonUI defaultUI = new JButtonUI();

	public static JPanel createPanel() {
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		InputStream is = null;
		try {
			is = new FileInputStream(System.getenv("APPDATA") + "\\GDBoard\\version.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assert is != null;
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		JLabel label = null;
		try {
			label = new JLabel("GDBoard version: " + br.readLine().replaceAll("version=", ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert label != null;
		label.setForeground(Defaults.FOREGROUND2);
		label.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		label.setBounds(25,20,label.getPreferredSize().width+5,label.getPreferredSize().height+5);
		CheckboxButton followers = createButton("Followers Only", 50);
		CheckboxButton subs = createButton("Subscribers Only", 80);


		panel.add(followers);
		panel.add(subs);
		panel.add(label);

		return panel;



	}
	public static CheckboxButton createButton(String text, int y){

		CheckboxButton button = new CheckboxButton(text);
		button.setBounds(25,y,365,30);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		button.refresh();
		return button;
	}
}
