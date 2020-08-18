package Main.InnerWindows;

import Main.*;
import com.jidesoft.swing.ResizablePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ActionsWindow {

	private static int height = 60;
	private static int width = 300;
	private static JPanel window = new InnerWindow("Actions", Settings.getActionsWLoc().x, Settings.getActionsWLoc().y, width, height,
			"\uE7C9", false).createPanel();
	private static JPanel mainPanel = new JPanel();
	private static JPanel panel = new JPanel();
	private static JButtonUI defaultUI = new JButtonUI();

	//region Create Panel
	public static void createPanel() {
		try {
			mainPanel.setBounds(1, 31, width, height);

			mainPanel.setLayout(null);

			defaultUI.setBackground(Defaults.BUTTON);
			defaultUI.setHover(Defaults.BUTTON_HOVER);
			defaultUI.setSelect(Defaults.SELECT);

			panel.setPreferredSize(new Dimension(width - 25, height));
			panel.setBounds(10, 5, width - 20, height - 10);

			mainPanel.setBackground(Defaults.SUB_MAIN);
			panel.setBackground(Defaults.SUB_MAIN);

			panel.setLayout(new GridLayout(1, 5, 10, 10));

			//region Create Skip Button
			JButton skip = createButton("\uEB9D", "Next/Skip Level");
			skip.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					((InnerWindow) window).moveToFront();
					Functions.skipFunction();
				}
			});
			panel.add(skip);
			//endregion

			//region Create Random Button

			JButton randNext = createButton("\uF158", "Next Random Level");
			randNext.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					((InnerWindow) window).moveToFront();
					Functions.randomFunction();

				}
			});
			panel.add(randNext);
			//endregion

			//region Create Copy Button
			JButton copy = createButton("\uF0E3", "Copy to Clipboard");
			copy.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					((InnerWindow) window).moveToFront();
					super.mousePressed(e);
					Functions.copyFunction();
				}
			});
			panel.add(copy);
			//endregion

			//region Create Block Button
			JButton block = createButton("\uF140", "Block Level");
			block.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					((InnerWindow) window).moveToFront();
					Functions.blockFunction();
				}
			});
			panel.add(block);
			//endregion

			//region Create Clear Button
			JButton clear = createButton("\uE107", "Clear Queue");
			clear.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					SettingsWindow.run = false;
					((InnerWindow) window).moveToFront();
					Functions.clearFunction();
				}
			});
			panel.add(clear);
			//endregion
			mainPanel.add(panel);
			window.add(mainPanel);
			((InnerWindow) window).refreshListener();
			Overlay.addToFrame(window);
		} catch (Exception e) {
			e.printStackTrace();
			DialogBox.showDialogBox("Error!", e.toString(), "Please report to Alphalaneous.", new String[]{"OK"});
		}
	}
	public static void destroyPanel(){
		try {
			if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
				Overlay.removeFromFrame(window);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	//endregion
	public String getName(){
		return "Actions";
	}
	public String getIcon(){
		return "\uE7C9";
	}
	//region Create Button
	private static JButton createButton(String icon, String tooltip) {
		JButton button = new RoundedJButton(icon, tooltip);
		button.setPreferredSize(new Dimension(50, 50));
		button.setUI(defaultUI);
		button.setBackground(Defaults.BUTTON);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SYMBOLS.deriveFont(20f));
		return button;
	}
	//endregion

	//region Set Pin
	public static void setPin(boolean pin) {
		((InnerWindow) window).setPin(pin);
	}
	//endregion

	//region RefreshUI
	public static void refreshUI() {
		((InnerWindow) window).refreshUI();

		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);

		defaultUI.setBackground(Defaults.BUTTON);
		mainPanel.setBackground(Defaults.MAIN);
		panel.setBackground(Defaults.MAIN);
		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				component.setBackground(Defaults.BUTTON);
				component.setForeground(Defaults.FOREGROUND);
			}
		}
	}
	//endregion

	//region Toggle Visible
	public static void toggleVisible() {
		((InnerWindow) window).toggle();
	}
	//endregion

	//region SetInvisible
	public static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}
	//endregion

	//region SetVisible
	public static void setVisible() {
		((InnerWindow) window).setVisible();
	}
	//endregion

	//region SetLocation
	public static void setLocation(Point point) {
		window.setLocation(point);
	}
	//endregion

	//region SetSettings
	public static void setSettings() {
		((InnerWindow) window).setSettings();
	}
	//endregion
}
