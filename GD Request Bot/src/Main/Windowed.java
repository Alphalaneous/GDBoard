package Main;

import SettingsPanels.OutputSettings;
import com.jidesoft.swing.ResizablePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Windowed {
	private static int width = 622;
	private static int height = 622;
	private static ResizablePanel window = new InnerWindow("GDBoard", 0, 0, width-2, height,
			"\uE713", true).createPanel();
	private static JPanel content = new JPanel();

	private static JButtonUI defaultUI = new JButtonUI();
	private static JButtonUI selectUI = new JButtonUI();
	public static boolean run = true;
	static JDialog frame = new JDialog();
	static void createPanel() {
		frame.setAlwaysOnTop(true);
		frame.setFocusableWindowState(false);
		frame.setUndecorated(true);
		frame.setSize(width,height+32);
		frame.setLayout(null);
		frame.setBackground(new Color(255, 255, 255, 0));

		content.setBounds(1, 31, width-2, height);
		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);

		window.add(content);
		((InnerWindow) window).setPinVisible();
		((InnerWindow) window).refreshListener();
		frame.add(window);
		frame.setVisible(true);
	}
	static void toFront(){
		frame.toFront();
	}
	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		defaultUI.setBackground(Defaults.MAIN);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);
		selectUI.setSelect(Defaults.SELECT);
		content.setBackground(Defaults.SUB_MAIN);


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


	//region SetLocation
	static void setLocation(Point point){
		frame.setLocation(point);
	}
	//endregion
	//region SetSettings
	static void setSettings(){
		Settings.setWindowSettings("Window", frame.getX() + "," + frame.getY() + "," + false + "," + frame.isVisible());

	}
	//endregion
}
