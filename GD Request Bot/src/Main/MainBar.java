package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class MainBar {

	private static JButtonUI defaultUI = new JButtonUI();
	private static JButtonUI subUI = new JButtonUI();
	private static JLabel time = new JLabel();
	private static JPanel barPanel = new JPanel();
	private static JPanel mainPanel = new JPanel();
	private static JPanel buttonPanel = new JPanel();
	private static JLabel icon =  new JLabel();

	static void createBar() {



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
		JButton close = createSubButton("\uE10A");
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				try {
					Settings.writeLocation();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		buttonPanel.add(toggleComments);
		buttonPanel.add(toggleSong);
		buttonPanel.add(toggleLevels);
		buttonPanel.add(toggleInfo);
		buttonPanel.add(toggleActions);
		buttonPanel.add(close);
		//}
		Map<TextAttribute, Object> attributes = new HashMap<>();
		attributes.put(TextAttribute.TRACKING, 0.02);
		Font font = new Font("bahnschrift", Font.BOLD, 23).deriveFont(attributes);

		time.setFont(font);
		time.setForeground(Defaults.FOREGROUND);

		mainPanel.add(time);
		mainPanel.add(buttonPanel);
		BufferedImage img = null;
		try {
			if(!Defaults.dark.get()) {
				img = ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader()
						.getResource("Resources/Icons/barIconLight.png")));
			}
			else if(Defaults.dark.get()){
				img = ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader()
						.getResource("Resources/Icons/barIconDark.png")));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		assert img != null;
		Image imgScaled = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		ImageIcon imgNew = new ImageIcon(imgScaled);

		icon.setIcon(imgNew);
		icon.setBounds(20, -1, 64, 64);
		icon.updateUI();
		mainPanel.add(icon);

		Overlay.addToFrame(barPanel);

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
	private static JButton createSubButton(String icon) {

		subUI.setBackground(Defaults.TOP);
		JButton button = new JButton(icon);
		button.setPreferredSize(new Dimension(65, 64));
		button.setBackground(Defaults.TOP);
		button.setUI(subUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 20));
		return button;
	}

	static void setTime(String timeValue) {
		time.setText(timeValue);
		time.setBounds(148 - time.getPreferredSize().width, 2, (int) time.getPreferredSize().getWidth(), 64);
		time.updateUI();
	}
	static void refreshUI(boolean color) {
		if(color) {
			BufferedImage img = null;
			try {
				if (Defaults.dark.get()) {
					System.out.println("Dark");
					img = ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader()
							.getResource("Resources/Icons/barIconLight.png")));
				} else if (!Defaults.dark.get()) {
					System.out.println("Light");
					img = ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader()
							.getResource("Resources/Icons/barIconDark.png")));
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
			subUI.setBackground(Defaults.TOP);
			subUI.setHover(Defaults.HOVER);
			subUI.setSelect(Defaults.SELECT);
			time.setForeground(Defaults.FOREGROUND);
			for (Component component : buttonPanel.getComponents()) {
				if (component instanceof JButton) {
					if (((JButton) component).getText().equalsIgnoreCase("\uE10A")) {
						component.setBackground(Defaults.TOP);
					} else {
						component.setBackground(Defaults.MAIN);
					}
					component.setForeground(Defaults.FOREGROUND);
				}
			}
		}
		else {
			int middle = (int) (Defaults.screenSize.getWidth() / 2);
			barPanel.setLocation(middle - 290, 30);
		}
	}

	static void setInvisible() {
		barPanel.setVisible(false);
	}

	static void setVisible() {
		barPanel.setVisible(true);
	}

}
