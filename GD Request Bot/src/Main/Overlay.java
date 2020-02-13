package Main;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Overlay {

	// --------------------
	// Create JFrame Object
	
	static JFrame frame = new JFrame();
	private static JLayeredPane mainFrame = new JLayeredPane();
	static boolean isVisible = true;

	// --------------------

	static void setFrame() {

		//TODO Windowed Mode

		// --------------------
		// default frame stuff
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setSize(Defaults.screenSize);
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		frame.setFocusableWindowState(false);
		frame.setBackground(new Color(0, 0, 0, 100));
		frame.setLayout(null);
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				isVisible = false;
				setWindowsInvisible();

			}
		});
		mainFrame.setDoubleBuffered(true);
		mainFrame.setBounds(0,0,1920,1080);
		mainFrame.setBackground(new Color(0,0,0,0));
		mainFrame.setLayout(null);
		
		frame.add(mainFrame);

		// --------------------
		// Set frame visible

	}

	static void addToFrame(JComponent component) {

		// --------------------
		// Add components to JFrame from elsewhere

		mainFrame.add(component, 0);
		mainFrame.updateUI();

		// --------------------
	}
	
	static void moveToFront(JComponent component) {
		mainFrame.moveToFront(component);
		
	}
	
	static void alwaysFront(JComponent component) {
		mainFrame.setLayer(component, 2);
	}

	static void setVisible() {

		frame.setVisible(true);
	}

	static void refreshUI() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		frame.setSize(Defaults.screenSize);
		frame.invalidate();
		frame.revalidate();
		SettingsWindow.refreshUI();
		LevelsWindow.refreshUI();
		InfoWindow.refreshUI();
		CommentsWindow.refreshUI();
		ActionsWindow.refreshUI();
		SongWindow.refreshUI();
		MainBar.refreshUI();
	}
	
	static void setWindowsInvisible() {
		isVisible = false;
		frame.setBackground(new Color(0, 0, 0, 0));
		CommentsWindow.setInvisible();
		ActionsWindow.setInvisible();
		InfoWindow.setInvisible();
		LevelsWindow.setInvisible();
		SongWindow.setInvisible();
		SettingsWindow.setInvisible();
		MainBar.setInvisible();
		frame.invalidate();
		frame.validate();
		frame.repaint();
	}

	static void setWindowsVisible() {
		isVisible = true;
		frame.setBackground(new Color(0, 0, 0, 100));
		CommentsWindow.setVisible();
		ActionsWindow.setVisible();
		InfoWindow.setVisible();
		LevelsWindow.setVisible();
		SongWindow.setVisible();
		SettingsWindow.setVisible();
		MainBar.setVisible();
		frame.invalidate();
		frame.validate();
		frame.repaint();
	}

}
class MainBar {

	private static JButtonUI defaultUI = new JButtonUI();
	private static JLabel time = new JLabel();
	private static JPanel barPanel = new JPanel();
	private static JPanel mainPanel = new JPanel();
	private static JPanel buttonPanel = new JPanel();
	private static JLabel icon =  new JLabel();

	static void createBar() {
		BufferedImage img = null;
		try {
			if(!Defaults.dark.get()) {
				img = ImageIO.read(new File("src/resources/Icons/barIconLight.png"));
			}
			if(Defaults.dark.get()){
				img = ImageIO.read(new File("src/resources/Icons/barIconDark.png"));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		assert img != null;
		Image imgScaled = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		ImageIcon imgNew = new ImageIcon(imgScaled);

		icon.setIcon(imgNew);
		double ratio = 1920/Defaults.screenSize.getWidth();
		Overlay.alwaysFront(barPanel);
		barPanel.setOpaque(false);
		barPanel.setSize(580, 65);
		barPanel.setLocation((int) (670/ratio), 30);
		barPanel.setLayout(null);
		barPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50)));
		barPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
			}
		});

		mainPanel.setBounds(1, 1, 578, 63);
		mainPanel.setBackground(Defaults.TOP);
		mainPanel.setLayout(null);
		barPanel.add(mainPanel);

		icon.setBounds(20, -1, 64, 64);
		mainPanel.add(icon);

		buttonPanel.setBounds(160, 0, 420, 64);
		buttonPanel.setBackground(Defaults.MAIN);
		buttonPanel.setLayout(new GridLayout(1, 2, 0, 0));

		//if (Settings.isRequests()) {
		JButton toggleSong = createButton("\uEC4F");
		toggleSong.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				SongWindow.toggleVisible();
			}
		});

		JButton toggleComments = createButton("\uEBDB");
		toggleComments.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				CommentsWindow.toggleVisible();
			}
		});

		JButton toggleInfo = createButton("\uE946");
		toggleInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				InfoWindow.toggleVisible();
			}
		});

		JButton toggleLevels = createButton("\uE179");
		toggleLevels.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				LevelsWindow.toggleVisible();
			}
		});

		JButton toggleActions = createButton("\uE7C9");
		toggleActions.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				ActionsWindow.toggleVisible();
			}
		});
		buttonPanel.add(toggleComments);
		buttonPanel.add(toggleSong);
		buttonPanel.add(toggleLevels);
		buttonPanel.add(toggleInfo);
		buttonPanel.add(toggleActions);
		//}
		Map<TextAttribute, Object> attributes = new HashMap<>();
		attributes.put(TextAttribute.TRACKING, 0.02);
		Font font = new Font("bahnschrift", Font.BOLD, 23).deriveFont(attributes);

		time.setFont(font);
		time.setForeground(Defaults.FOREGROUND);



		mainPanel.add(time);
		mainPanel.add(buttonPanel);

		Overlay.addToFrame(barPanel);
		refreshUI();
	}
	static JPanel getMainBar(){
		return barPanel;
	}

	private static JButton createButton(String icon) {

		defaultUI.setBackground(Defaults.MAIN);
		JButton button = new JButton(icon);
		button.setPreferredSize(new Dimension(65, 64));
		button.setBackground(Defaults.MAIN);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 20));
		return button;
	}

	static void setTime(String timeValue) {
		time.setText(timeValue);
		time.setBounds(148 - time.getPreferredSize().width, -1, (int) time.getPreferredSize().getWidth(), 64);
		time.updateUI();
	}
	static void refreshUI() {
		BufferedImage img = null;
		try {
			if(!Defaults.dark.get()) {
				img = ImageIO.read(new File("src/resources/Icons/barIconLight.png"));
			}
			if(Defaults.dark.get()){
				img = ImageIO.read(new File("src/resources/Icons/barIconDark.png"));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		assert img != null;
		Image imgScaled = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		ImageIcon imgNew = new ImageIcon(imgScaled);

		icon.setIcon(imgNew);
		icon.setBounds(20, -1, 64, 64);

		mainPanel.setBackground(Defaults.TOP);
		buttonPanel.setBackground(Defaults.TOP);
		defaultUI.setBackground(Defaults.MAIN);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		time.setForeground(Defaults.FOREGROUND);
		for (Component component : buttonPanel.getComponents()) {
			if (component instanceof JButton) {
				component.setBackground(Defaults.MAIN);
				component.setForeground(Defaults.FOREGROUND);
			}
		}
		int middle = (int) (Defaults.screenSize.getWidth() / 2);
		barPanel.setLocation(middle-290 , 30);
	}

	static void setInvisible() {
		barPanel.setVisible(false);
	}

	static void setVisible() {
		barPanel.setVisible(true);
	}

}

