package Main;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

class InfoWindow {

	private static int height = 110;
	private static int width = 400;
	private static JPanel panel = new JPanel();
	private static JLabel likes = new JLabel();
	private static JLabel downloads = new JLabel();
	private static JTextPane description = new JTextPane();
	private static JPanel window = new InnerWindow("Information", 1920 - width - 10, 10, width, height, "src/resources/Icons/Info.png").createPanel();

	static void createWindow() {

		

		panel.setPreferredSize(new Dimension(width, height));
		panel.setBounds(1, 31, width, height);
		panel.setBackground(Defaults.MAIN);

		panel.setLayout(null);
		likes.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		likes.setBounds(10, 10, width, 20);
		likes.setForeground(Defaults.FOREGROUND);
		downloads.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		likes.setText("LIKES: N/A");
		description.setText("N/A");
		downloads.setText("DOWNLOADS: N/A");
		downloads.setBounds((int) (width - downloads.getPreferredSize().getWidth()) - 10, 10, width, 20);
		downloads.setForeground(Defaults.FOREGROUND);
		StyledDocument doc = description.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		description.setFont(new Font("bahnschrift", Font.PLAIN, 16));
		description.setBounds(10, 40, width - 14, 40);
		description.setOpaque(false);
		description.setEditable(false);
		description.setForeground(Defaults.FOREGROUND);
		panel.add(likes);
		panel.add(downloads);
		panel.add(description);

		window.add(panel);
		((InnerWindow)window).refreshListener();
		Overlay.addToFrame(window);
	}

	static void refreshInfo() {
		if (Requests.levels.size() == 0) {
			likes.setText("LIKES: N/A");
			description.setText("N/A");
			downloads.setText("DOWNLOADS: N/A");
			downloads.setBounds((int) (width - downloads.getPreferredSize().getWidth()) - 10, 10, width, 20);
		} else {
			likes.setText("LIKES: " + Requests.levels.get(LevelsWindow2.getSelectedID()).getLikes());
			description.setText(Requests.levels.get(LevelsWindow2.getSelectedID()).getDescription());
			downloads.setText("DOWNLOADS: " + Requests.levels.get(LevelsWindow2.getSelectedID()).getDownloads());
			downloads.setBounds((int) (width - downloads.getPreferredSize().getWidth()) - 10, 10, width, 20);
		}

	}
	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		panel.setBackground(Defaults.MAIN);
		likes.setForeground(Defaults.FOREGROUND);
		downloads.setForeground(Defaults.FOREGROUND);
		description.setForeground(Defaults.FOREGROUND);
	}
	
	static void toggleVisible() {
		((InnerWindow) window).toggle();
	}
	
	static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

	static void setVisible() {
		((InnerWindow) window).setVisible();
	}
}
