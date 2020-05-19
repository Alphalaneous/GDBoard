package Main;

import SettingsPanels.OutputSettings;
import SettingsPanels.WindowedSettings;
import com.jidesoft.swing.ResizablePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Windowed {
	private static int width = 465;
	private static int height = 512;
	public static ResizablePanel window = new InnerWindow("GDBoard", Settings.getWindowWLoc().x, Settings.getWindowWLoc().y, width-2, height,
			"\uF137", true).createPanel();
	private static JPanel content = new JPanel(null);
	private static JPanel buttonPanel = new JPanel();
	private static JButtonUI defaultUI = new JButtonUI();
	private static JButtonUI selectUI = new JButtonUI();
	public static boolean run = true;
	static JFrame frame = new JFrame();
	public static void setOnTop(boolean onTop){
		frame.setAlwaysOnTop(onTop);
		frame.setFocusableWindowState(!onTop);
	}
	static void createPanel() {
		if(WindowedSettings.onTopOption){
			setOnTop(true);
		}
		URL iconURL = Windowed.class.getResource("/Resources/Icons/windowIcon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		Image newIcon = icon.getImage().getScaledInstance(120, 120,  Image.SCALE_SMOOTH);
		frame.setIconImage(newIcon);
		frame.setTitle("GDBoard");
		frame.setUndecorated(true);
		frame.setSize(width + 200,height+32 + 200);
		frame.setLayout(null);
		frame.setBackground(new Color(255, 255, 255, 0));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		content.setBounds(1, 31, width-2, height);
		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);


		JScrollPane levelsWindow = LevelsWindow.getReqWindow();
		levelsWindow.setBounds(0, 0, levelsWindow.getWidth(), levelsWindow.getHeight());
		JPanel infoWindow = InfoWindow.getInfoWindow();
		infoWindow.setBounds(0, levelsWindow.getHeight()+ 1, infoWindow.getWidth(), infoWindow.getHeight());

		buttonPanel.setBounds(levelsWindow.getWidth()+5, 0, 50, 512);
		buttonPanel.setBackground(Defaults.SUB_MAIN);
		JButton skip = createButton("\uEB9D");
		skip.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				Functions.skipFunction();
			}
		});
		buttonPanel.add(skip);
		//endregion

		//region Create Random Button

		JButton randNext = createButton("\uE897");
		randNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				Functions.randomFunction();

			}
		});
		buttonPanel.add(randNext);
		//endregion

		//region Create Copy Button
		JButton copy = createButton("\uF0E3");
		copy.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				Functions.copyFunction();
			}
		});
		buttonPanel.add(copy);
		//endregion

		//region Create Block Button
		JButton block = createButton("\uE8F8");
		block.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				Functions.blockFunction();
			}
		});
		buttonPanel.add(block);
		//endregion

		//region Create Clear Button
		JButton clear = createButton("\uE107");
		clear.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SettingsWindow.run = false;
				((InnerWindow) window).moveToFront();
				Functions.clearFunction();
			}
		});
		buttonPanel.add(clear);
		JButton toggleRequests = createButton("\uE71A");
		toggleRequests.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SettingsWindow.run = false;
				((InnerWindow) window).moveToFront();
				Functions.requestsToggleFunction();
				toggleRequests.setText(MainBar.stopReqs.getText());
			}
		});
		buttonPanel.add(toggleRequests);
		JButton settings = createButton("\uE713");
		settings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SettingsWindow.run = false;
				((InnerWindow) window).moveToFront();
				SettingsWindow.toggleVisible();
			}
		});
		buttonPanel.add(settings);
		content.add(levelsWindow);
		content.add(infoWindow);
		content.add(buttonPanel);
		window.add(content);
		((InnerWindow) window).setPinVisible();
		((InnerWindow) window).setMinimize(true);
		((InnerWindow) window).refreshListener();
		frame.add(window);
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
		buttonPanel.setBackground(Defaults.SUB_MAIN);
		for (Component component : buttonPanel.getComponents()) {
			if (component instanceof JButton) {
				if (!Settings.windowedMode) {
					component.setBackground(Defaults.BUTTON);
				} else {
					component.setBackground(Defaults.MAIN);
				}
				component.setForeground(Defaults.FOREGROUND);
			}
		}

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
	private static JButton createButton(String icon) {
		JButton button = new RoundedJButton(icon);
		button.setPreferredSize(new Dimension(50, 50));
		button.setUI(defaultUI);
		if (!Settings.windowedMode) {
			button.setBackground(Defaults.BUTTON);
		} else {
			button.setBackground(Defaults.MAIN);
		}
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SYMBOLS.deriveFont(20f));
		return button;
	}

	//region SetLocation
	static void setLocation(Point point){
		frame.setLocation(point);
	}
	//endregion
	//region SetSettings
	public static void setSettings(){
		Settings.setWindowSettings("Window", frame.getX() + "," + frame.getY() + "," + false + "," + true);

	}
	//endregion
}
