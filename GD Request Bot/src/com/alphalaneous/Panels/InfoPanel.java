package com.alphalaneous.Panels;

import com.alphalaneous.Defaults;
import com.alphalaneous.Requests;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class InfoPanel {

	private static JPanel descPanel = new JPanel();
	private static JPanel fullPanel = new JPanel(null);

	private static JTextPane description = new JTextPane();
	private static JPanel window = new JPanel();

	public static void createPanel() {


		int height = 110;
		descPanel.setPreferredSize(new Dimension(240, height));
		int width = 400;
		descPanel.setBounds(0, 0, width, height);
		descPanel.setBackground(Defaults.SUB_MAIN);
		descPanel.setLayout(null);
		descPanel.setOpaque(true);


		description.setText("N/A");
		StyledDocument doc = description.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		description.setFont(Defaults.SEGOE.deriveFont(14f));
		description.setSelectionColor(new Color(44, 144, 250));
		description.setOpaque(false);
		description.setEditable(false);
		description.setForeground(Defaults.FOREGROUND);
		description.setBackground(new Color(0, 0, 0, 0));
		description.setBounds(5, 5, width - 10, height - 10);


		descPanel.add(description);
		fullPanel.setBounds(1, 31, 400, 110);
		fullPanel.add(descPanel);
		window.add(fullPanel);
		refreshInfo();
	}

	public static void resetDimensions(int width, int height) {
		descPanel.setBounds(0, 0, width, height);
		description.setBounds(5, 5, width - 10, height - 10);

	}

	public static JPanel getInfoWindow() {
		return fullPanel;
	}

	public static void refreshInfo() {
		if (Requests.levels.size() == 0) {
			description.setText("NA");
		} else {
			description.setText(Requests.levels.get(LevelsPanel.getSelectedID()).getDescription());
		}
	}

	public static void refreshUI() {
		descPanel.setBackground(Defaults.SUB_MAIN);
		description.setForeground(Defaults.FOREGROUND);
	}

	//region SetLocation
	public static void setLocation(Point point) {
		window.setLocation(point);
	}

	public String getName() {
		return "Description";
	}
	//endregion

}
