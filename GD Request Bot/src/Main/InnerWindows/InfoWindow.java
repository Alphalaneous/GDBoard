package Main.InnerWindows;

import Main.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class InfoWindow {

	private static int height = 110;
	private static int width = 400;
	private static JPanel panel = new JPanel();
	private static JScrollPane scrollPane = new JScrollPane(panel);
	private static JPanel descPanel = new JPanel();
	private static JPanel fullPanel = new JPanel(null);
	private static JLabel likes = new JLabel();
	private static JLabel downloads = new JLabel();
	private static JLabel length = new JLabel();
	private static JLabel password = new JLabel();
	private static JLabel objects = new JLabel();
	private static JLabel coins = new JLabel();
	private static JLabel original = new JLabel();
	private static JLabel upload = new JLabel();
	private static JLabel update = new JLabel();
	private static JLabel version = new JLabel();



	private static JTextPane description = new JTextPane();
	private static JPanel window = new InnerWindow("Information", Settings.getInfoWLoc().x, Settings.getInfoWLoc().y, width, height, "\uE946", false).createPanel();

	public static void createPanel() {
		scrollPane.setPreferredSize(new Dimension(180, height));
		scrollPane.setBounds(0, 0, 180, height);
		scrollPane.setBackground(Defaults.MAIN);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.getHorizontalScrollBar().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);

		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
		scrollPane.getHorizontalScrollBar().setUI(new ScrollbarUI());

		panel.setPreferredSize(new Dimension(180, 230));
		panel.setBounds(0, 0, 180, 230);
		panel.setBackground(Defaults.MAIN);

		descPanel.setPreferredSize(new Dimension(240, height));
		descPanel.setBounds(180, 0, 220, height);
		descPanel.setBackground(Defaults.SUB_MAIN);
		descPanel.setLayout(null);
		descPanel.setOpaque(true);

		panel.setLayout(null);
		likes = createLabel("LIKES: NA", 10, 160);
		downloads = createLabel("DOWNLOADS: NA", 32, 160);
		length = createLabel("LENGTH: NA", 54, 160);
		password = createLabel("PASSWORD: NA", 76, 160);
		objects = createLabel("OBJECTS: NA", 98, 160);
		coins = createLabel("COINS: NA", 120, 160);
		original = createLabel("ORIGINAL: NA", 142, 160);
		upload = createLabel("UPLOAD: NA", 164, 160);
		update = createLabel("UPDATE: NA", 186, 160);
		version = createLabel("VERSION: NA", 208, 160);



		description.setText("N/A");
		StyledDocument doc = description.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		description.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		description.setOpaque(false);
		description.setEditable(false);
		description.setForeground(Defaults.FOREGROUND);
		description.setBackground(new Color(0,0,0,0));
		description.setBounds(5,5,210, height-10);

		panel.add(likes);
		panel.add(downloads);
		panel.add(length);
		panel.add(password);
		panel.add(objects);
		panel.add(coins);
		panel.add(original);
		panel.add(upload);
		panel.add(update);
		panel.add(version);
		descPanel.add(description);
		fullPanel.setBounds(1,31,400,110);
		fullPanel.add(scrollPane);
		fullPanel.add(descPanel);
		window.add(fullPanel);
		((InnerWindow)window).refreshListener();
		if(!Settings.windowedMode) {
			Overlay.addToFrame(window);
		}
		refreshInfo();
	}
	public static JPanel getInfoWindow(){
		return fullPanel;
	}
	public static void setPin(boolean pin){
		((InnerWindow) window).setPin(pin);
	}
	public static void refreshInfo() {
		if (Requests.levels.size() == 0) {
			description.setText("NA");

			likes.setText("LIKES: NA");
			length.setText("LENGTH: NA");
			password.setText("PASSWORD: NA");
			downloads.setText("DOWNLOADS: NA");
			objects.setText("OBJECTS: NA");
			coins.setText("COINS: NA");
			original.setText("ORIGINAL: NA");
			upload.setText("UPLOAD: NA");
			update.setText("UPDATE: NA");
			version.setText("VERSION: NA");




		} else {
			String pass;
			if((pass = Requests.levels.get(LevelsWindow.getSelectedID()).getPassword()) != null) {
				if (pass.equalsIgnoreCase("-2")) {
					password.setText("FREE COPY");
				}
				else if (pass.equalsIgnoreCase("-1")) {
					password.setText("NO COPY");
				}
				else {
					password.setText("PASSWORD: " + pass);
				}
			}
			else {
				password.setText("NO COPY");

			}
			likes.setText("LIKES: " + Requests.levels.get(LevelsWindow.getSelectedID()).getLikes());
			description.setText(Requests.levels.get(LevelsWindow.getSelectedID()).getDescription());
			length.setText("LENGTH: " + Requests.levels.get(LevelsWindow.getSelectedID()).getLength());
			downloads.setText("DOWNLOADS: " + Requests.levels.get(LevelsWindow.getSelectedID()).getDownloads());
			if(Requests.levels.get(LevelsWindow.getSelectedID()).getObjects() == 0){
				objects.setText("OBJECTS: NA");

			}
			else{
				objects.setText("OBJECTS: "  + Requests.levels.get(LevelsWindow.getSelectedID()).getObjects());

			}
			coins.setText("COINS: " + Requests.levels.get(LevelsWindow.getSelectedID()).getCoins());
			if(Requests.levels.get(LevelsWindow.getSelectedID()).getOriginal() == 0){
				original.setText("ORIGINAL");

			}
			else{
				original.setText("ORIGINAL: " + Requests.levels.get(LevelsWindow.getSelectedID()).getOriginal());
			}
			upload.setText("UPLOAD: " + Requests.levels.get(LevelsWindow.getSelectedID()).getUpload() + " ago");
			update.setText("UPDATE: " + Requests.levels.get(LevelsWindow.getSelectedID()).getUpdate() + " ago");
			version.setText("VERSION: " + Requests.levels.get(LevelsWindow.getSelectedID()).getLevelVersion());
		}
	}
	public static void refreshUI() {
		((InnerWindow) window).refreshUI();
		panel.setBackground(Defaults.MAIN);
		descPanel.setBackground(Defaults.SUB_MAIN);
		likes.setForeground(Defaults.FOREGROUND);
		length.setForeground(Defaults.FOREGROUND);
		password.setForeground(Defaults.FOREGROUND);
		downloads.setForeground(Defaults.FOREGROUND);
		description.setForeground(Defaults.FOREGROUND);
	}
	private static JLabel createLabel(String text, int y, int width){
		JLabel label = new JLabel(text);
		label.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		label.setBounds(10, y, width, 20);
		label.setForeground(Defaults.FOREGROUND);
		return label;
	}
	public String getName(){
		return "Information";
	}
	public String getIcon(){
		return "\uE946";
	}
	public static void toggleVisible() {
		((InnerWindow) window).toggle();
	}
	
	public static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

	//region SetLocation
	public static void setLocation(Point point){
		window.setLocation(point);
	}
	//endregion

	//region SetSettings
	public static void setSettings(){
		((InnerWindow) window).setSettings();
	}
	//endregion

	public static void setVisible() {
		((InnerWindow) window).setVisible();
	}
}
