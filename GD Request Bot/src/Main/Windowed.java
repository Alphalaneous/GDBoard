package Main;

import Main.InnerWindows.CommentsWindow;
import Main.InnerWindows.InfoWindow;
import Main.InnerWindows.LevelsWindow;
import Main.SettingsPanels.WindowedSettings;
import com.jhlabs.image.GaussianFilter;
import com.jidesoft.swing.Resizable;
import com.jidesoft.swing.ResizablePanel;
import org.jdesktop.swingx.border.DropShadowBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Windowed {
	private static int width = 765;
	private static int height = 512;
	public static ResizablePanel window = new InnerWindow("GDBoard - 0", Settings.getWindowWLoc().x, Settings.getWindowWLoc().y, width-2, height,
			"\uF137", true){
		@Override
		protected Resizable createResizable() {

			return new Resizable(this) {
				@Override
				public void beginResizing(int var1){
					x = frame.getX();
					y = frame.getY();
				}

				@Override
				public void resizing(int resizeCorner, int newX, int newY, int newW, int newH) {

					if(newW < 775) {
						newW = 775;
						newX = getX();
					}
					if(newH < 552){
						newH = 552;
						newY = getY();
					}
					width = newW;
					height = newH;
					frame.setBounds(x + newX, y + newY, newW+200, newH+200);
					mainFrame.setBounds(20, 20, newW+200, newH+200);
					content.setBounds(5,35,newW-10, newH-40);
					window.setBounds(0,0,newW, newH);
					((InnerWindow) window).resetDimensions(newW-10, newH);
					buttonPanel.setBounds(newW-68, 0, 50, 512);
					LevelsWindow.resizeButtons(newW-375, newH-152);
					levelsWindow.setBounds(0, 0, newW-375, newH-152);
					CommentsWindow.resetDimensions(commentsWindow.getWidth(), newH);
					commentsWindow.setBounds(newW-375, 0, commentsWindow.getWidth(), newH);
					InfoWindow.resetDimensions(levelsWindow.getWidth(), infoWindow.getHeight());
					infoWindow.setBounds(0, levelsWindow.getHeight()+ 1, levelsWindow.getWidth(), infoWindow.getHeight());

				}
			};
		}
	}.createPanel();
	private static JScrollPane levelsWindow = LevelsWindow.getReqWindow();
	private static JPanel infoWindow = InfoWindow.getInfoWindow();
	private static JPanel content = new JPanel(null);
	private static JPanel buttonPanel = new JPanel();
	private static JButtonUI defaultUI = new JButtonUI();
	private static JButtonUI selectUI = new JButtonUI();
	public static JFrame frame = new JFrame();
	private static JLayeredPane mainFrame = new JLayeredPane();
	public static boolean showingMore = true;
	private static RoundedJButton showMore = createButton("\uE00F", "Show More");

	private static JPanel commentsWindow;

	private static int x = frame.getX();
	private static int y = frame.getY();

	public static void setOnTop(boolean onTop){
		frame.setAlwaysOnTop(onTop);
		frame.setFocusableWindowState(!onTop);
	}
	static void createPanel() {
		if(WindowedSettings.onTopOption){
			setOnTop(true);
		}
		try {
			if(Settings.getSettings("windowed").equalsIgnoreCase("true")){
				commentsWindow = CommentsWindow.getComWindow();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.close();
			}
		});
		URL iconURL = Windowed.class.getResource("/Resources/Icons/windowIcon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		Image newIcon = icon.getImage().getScaledInstance(120, 120,  Image.SCALE_SMOOTH);
		frame.setIconImage(newIcon);
		frame.setTitle("GDBoard - 0");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

			}
		});

		frame.setUndecorated(true);
		frame.setSize(width + 200,height+32 + 200);

		frame.setLayout(null);
		frame.setBackground(new Color(255, 255, 255, 0));
		mainFrame.setBounds(20, 20, width + 200, height + 32 + 200);
		mainFrame.setLayout(null);
		mainFrame.setDoubleBuffered(true);
		mainFrame.setBackground(new Color(0, 0, 0));
		content.setBounds(5,35,width-2, height);
		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);
		try {
			if(Settings.getSettings("window").equalsIgnoreCase("") && Settings.getSettings("windowed").equalsIgnoreCase("true")){
				frame.setLocation((int)Defaults.screenSize.getWidth()/2 - width/2, 200);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		levelsWindow.setBounds(0, 0, levelsWindow.getWidth(), levelsWindow.getHeight());
		commentsWindow.setBounds(400, 0, commentsWindow.getWidth(), 512);
		commentsWindow.setVisible(false);

		infoWindow.setBounds(0, levelsWindow.getHeight()+ 1, infoWindow.getWidth(), infoWindow.getHeight());

		buttonPanel.setBounds(width-58, 0, 50, 512);
		buttonPanel.setBackground(Defaults.SUB_MAIN);
		JButton skip = createButton("\uEB9D", "Next/Skip Level");
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

		JButton randNext = createButton("\uF158", "Next Random Level");
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
		JButton copy = createButton("\uF0E3", "Copy to Clipboard");
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
		JButton block = createButton("\uF140", "Block Level");
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
		JButton clear = createButton("\uE107", "Clear Queue");
		clear.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SettingsWindow.run = false;
				((InnerWindow) window).moveToFront();
				Functions.clearFunction();
			}
		});
		buttonPanel.add(clear);
		JButton toggleRequests = createButton("\uE71A", "Toggle Requests");
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
		JButton settings = createButton("\uE713", "Settings");
		settings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SettingsWindow.run = false;
				((InnerWindow) window).moveToFront();
				SettingsWindow.toggleVisible();
			}
		});
		buttonPanel.add(settings);
		CommentsWindow.loadComments(0,false);
		commentsWindow.setVisible(true);


		JButton donate = createButton("\uE006", "Donate");
		donate.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					try {
						Runtime rt = Runtime.getRuntime();
						rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://www.paypal.me/xAlphalaneous");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		buttonPanel.add(donate);
		content.add(levelsWindow);
		content.add(commentsWindow);
		content.add(infoWindow);
		content.add(buttonPanel);
		window.add(content);
		((InnerWindow) window).setPinVisible();
		((InnerWindow) window).setMinimize(true);
		((InnerWindow) window).refreshListener();
		mainFrame.add(window);
		frame.add(mainFrame);
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
	static void resetCommentSize(){
		commentsWindow.setBounds(400, 0, commentsWindow.getWidth(), 512);

	}
	public static void addToFrame(JComponent component) {

		// --------------------
		// Add components to JFrame from elsewhere

		mainFrame.add(component, 0);

		// --------------------
	}
	static void removeFromFrame(JComponent component) {

		// --------------------
		// Add components to JFrame from elsewhere

		mainFrame.remove(component);

		// --------------------
	}
	static void toggleVisible() {

		((InnerWindow) window).toggle();
	}

	static void setVisible() {
		((InnerWindow) window).setVisible();

	}
	private static RoundedJButton createButton(String icon, String tooltip) {
		RoundedJButton button = new RoundedJButton(icon, tooltip);
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
		try {
			Settings.writeSettings("windowSize", width + "," + height);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void loadSettings(){
		try {
			if(!Settings.getSettings("windowSize").equalsIgnoreCase("")){
				String[] dim = Settings.getSettings("windowSize").split(",");
				int newW = Integer.parseInt(dim[0]);
				int newH = Integer.parseInt(dim[1]);
				if(newW < 775) {
					newW = 775;
				}
				if(newH < 552){
					newH = 552;
				}
				width = newW;
				height = newH;
				frame.setSize( newW+200, newH+200);
				mainFrame.setBounds(20, 20, newW+200, newH+200);
				content.setBounds(5,35,newW-10, newH-40);
				window.setBounds(0,0,newW, newH);
				((InnerWindow) window).resetDimensions(newW-10, newH);
				buttonPanel.setBounds(newW-68, 0, 50, 512);
				LevelsWindow.resizeButtons(newW-375, newH-152);
				levelsWindow.setBounds(0, 0, newW-375, newH-152);
				CommentsWindow.resetDimensions(commentsWindow.getWidth(), newH);
				commentsWindow.setBounds(newW-375, 0, commentsWindow.getWidth(), newH);
				InfoWindow.resetDimensions(levelsWindow.getWidth(), infoWindow.getHeight());
				infoWindow.setBounds(0, levelsWindow.getHeight()+ 1, levelsWindow.getWidth(), infoWindow.getHeight());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//endregion
}
