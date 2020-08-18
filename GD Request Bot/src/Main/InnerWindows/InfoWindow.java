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
	private static JPanel descPanel = new JPanel();
	private static JPanel fullPanel = new JPanel(null);

	private static JTextPane description = new JTextPane();
	private static JPanel window = new InnerWindow("Description", Settings.getInfoWLoc().x, Settings.getInfoWLoc().y, width, height, "\uE946", false).createPanel();

	public static void createPanel() {



		descPanel.setPreferredSize(new Dimension(240, height));
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
		description.setBackground(new Color(0,0,0,0));
		description.setBounds(5,5,width-10, height-10);


		descPanel.add(description);
		fullPanel.setBounds(1,31,400,110);
		fullPanel.add(descPanel);
		window.add(fullPanel);
		((InnerWindow)window).refreshListener();
		if(!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			Overlay.addToFrame(window);
		}
		refreshInfo();
	}
	public static void destroyPanel(){
		try {
			if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
				Windowed.removeFromFrame(fullPanel);
			} else {
				Overlay.removeFromFrame(window);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void resetDimensions(int width, int height){
		descPanel.setBounds(0,0, width, height);
		description.setBounds(5,5,width-10, height-10);

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
		} else {
			description.setText(Requests.levels.get(LevelsWindow.getSelectedID()).getDescription().toString());
		}
	}
	public static void refreshUI() {
		((InnerWindow) window).refreshUI();
		descPanel.setBackground(Defaults.SUB_MAIN);
		description.setForeground(Defaults.FOREGROUND);
	}
	public String getName(){
		return "Description";
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
