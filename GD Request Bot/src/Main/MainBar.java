package Main;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class MainBar {

	private static JButtonUI defaultUI = new JButtonUI();
	private static JLabel time = new JLabel();
	private static JPanel barPanel = new JPanel();
	private static JPanel mainPanel = new JPanel();
	private static JPanel buttonPanel = new JPanel();

	static void createBar() {

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("src/resources/Icons/barIcon.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		assert img != null;
		Image imgScaled = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
		ImageIcon imgNew = new ImageIcon(imgScaled);

		JLabel icon = new JLabel(imgNew);
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

		icon.setBounds(-2, -1, 64, 64);
		mainPanel.add(icon);

		buttonPanel.setBounds(160, 0, 420, 64);
		buttonPanel.setBackground(Defaults.MAIN);
		buttonPanel.setLayout(new GridLayout(1, 2, 0, 0));

		//if (Settings.isRequests()) {
			JButton toggleSong = createButton("src/resources/Icons/note.png");
			toggleSong.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					SongWindow.toggleVisible();
				}
			});

			JButton toggleComments = createButton("src/resources/Icons/comments.png");
			toggleComments.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					CommentsWindow.toggleVisible();
				}
			});

			JButton toggleInfo = createButton("src/resources/Icons/info.png");
			toggleInfo.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					InfoWindow.toggleVisible();
				}
			});

			JButton toggleLevels = createButton("src/resources/Icons/queue.png");
			toggleLevels.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					LevelsWindow2.toggleVisible();
				}
			});

			JButton toggleActions = createButton("src/resources/Icons/actions.png");
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
	}

	private static JButton createButton(String location) {
		BufferedImage origImg = null;
		try {
			origImg = ImageIO.read(new File(location));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		assert origImg != null;
		Image scaledImg = origImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon imgNew = new ImageIcon(scaledImg);

		defaultUI.setBackground(Defaults.MAIN);
		JButton button = new JButton(imgNew);
		button.setPreferredSize(new Dimension(65, 64));
		button.setBackground(Defaults.MAIN);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(new Font("bahnschrift", Font.BOLD, 14));
		return button;
	}

	static void setTime(String timeValue) {
		time.setText(timeValue);
		time.setBounds(148 - time.getPreferredSize().width, -1, (int) time.getPreferredSize().getWidth(), 64);
		time.updateUI();
	}

	static void refreshUI() {
		mainPanel.setBackground(Defaults.TOP);
		buttonPanel.setBackground(Defaults.TOP);
		defaultUI.setBackground(Defaults.MAIN);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		time.setForeground(Defaults.FOREGROUND);
		for (Component component : buttonPanel.getComponents()) {
			if (component instanceof JButton) {
				component.setBackground(Defaults.MAIN);
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
