package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class LevelsWindow {

	private static JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	private static JScrollPane scrollPane = new JScrollPane();
	private static int width = 400;
	private static int height = 400;
	private static int panelHeight = 0;
	private static int ID;
	private static JPanel window = new InnerWindow("Requests", 10, 10, width, height, "src/resources/Icons/Queue.png")
			.createPanel();
	static JButtonUI defaultUI = new JButtonUI();
	static JButtonUI selectUI = new JButtonUI();

	static void createPanel() throws IOException, InterruptedException {

		panel.setPreferredSize(new Dimension(400, panelHeight));
		panel.setBackground(Defaults.MAIN);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setBackground(Defaults.MAIN);
		scrollPane.setBounds(1, 31, 400, height);
		scrollPane.setPreferredSize(new Dimension(400, height));
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(panel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		// window.add(buttonPanel);
		window.add(scrollPane);
		((InnerWindow) window).refreshListener();
		Overlay.addToFrame(window);
	}

	static void refreshRequests() throws IOException {
		ID = 0;
		panelHeight = 0;
		panel.removeAll();
		for (int i = 0; i < Requests.levels.size(); i++) {

			defaultUI.setBackground(Defaults.MAIN);

			defaultUI.setHover(Defaults.HOVER);

			selectUI.setBackground(Defaults.SELECT);

			selectUI.setHover(Defaults.BUTTON_HOVER);

			JButtonUI warningUI = new JButtonUI();

			warningUI.setBackground(new Color(150, 0, 0));
			warningUI.setHover(new Color(170, 0, 0));

			JButtonUI noticeUI = new JButtonUI();

			noticeUI.setBackground(new Color(150, 150, 0));
			noticeUI.setHover(new Color(170, 170, 0));

			JButtonUI warningSelectUI = new JButtonUI();

			warningSelectUI.setBackground(new Color(190, 0, 0));
			warningSelectUI.setHover(new Color(200, 0, 0));
			warningSelectUI.setSelect(new Color(150, 0, 0));

			JButtonUI noticeSelectUI = new JButtonUI();

			noticeSelectUI.setBackground(new Color(190, 190, 0));
			noticeSelectUI.setHover(new Color(200, 200, 0));
			noticeSelectUI.setSelect(new Color(150, 150, 0));

			JLabel lName = new JLabel(Requests.levels.get(i).getName());
			JLabel lID = new JLabel(Requests.levels.get(i).getLevelID());
			JLabel lAuthor = new JLabel(Requests.levels.get(i).getAuthor());
			JLabel lAnalyzed = new JLabel();

			String[] difficulties = { "NA", "easy", "normal", "hard", "harder", "insane", "easy demon", "medium demon",
					"hard demon", "insane demon", "extreme demon" };
			JLabel reqDifficulty = new JLabel();
			

			for (String difficultyA : difficulties) {
				if (Requests.levels.get(i).getDifficulty().equalsIgnoreCase(difficultyA)) {
					if (Requests.levels.get(i).getEpic()) {
						reqDifficulty.setIcon(new ImageIcon((Image) ImageIO
								.read(LevelsWindow.class.getClassLoader()
										.getResource("DifficultyIcons/Epic/" + difficultyA + ".png"))
								.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
					} else if (Requests.levels.get(i).getFeatured()) {
						reqDifficulty.setIcon(new ImageIcon((Image) ImageIO
								.read(LevelsWindow.class.getClassLoader()
										.getResource("DifficultyIcons/Featured/" + difficultyA + ".png"))
								.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
					} else {
						reqDifficulty.setIcon(new ImageIcon((Image) ImageIO
								.read(LevelsWindow.class.getClassLoader()
										.getResource("DifficultyIcons/Normal/" + difficultyA + ".png"))
								.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
					}
				}
			}
			reqDifficulty.setBounds(10, 0, 50, 50);
			JButton request = new JButton();

			request.add(lName);
			request.add(lID);
			request.add(lAuthor);
			request.add(lAnalyzed);
			request.add(reqDifficulty);
			request.setLayout(null);

			lName.setFont(new Font("bahnschrift", Font.PLAIN, 20));
			lName.setBounds(60, 0, (int) lName.getPreferredSize().getWidth(), 30);
			lID.setFont(new Font("bahnschrift", Font.PLAIN, 12));
			lID.setBounds(60, 26, (int) lID.getPreferredSize().getWidth(), 20);
			lAuthor.setFont(new Font("bahnschrift", Font.PLAIN, 12));
			lAuthor.setBounds((int) (400 - lAuthor.getPreferredSize().getWidth()) - 10, 3,
					(int) lAuthor.getPreferredSize().getWidth(), 20);
			lAnalyzed.setFont(new Font("bahnschrift", Font.PLAIN, 12));

			lName.setForeground(Defaults.FOREGROUND);
			lAuthor.setForeground(Defaults.FOREGROUND);
			lID.setForeground(Defaults.FOREGROUND);
			lAnalyzed.setForeground(Defaults.FOREGROUND);
			panelHeight = panelHeight + 50;
			panel.setPreferredSize(new Dimension(width, panelHeight));

			if (Requests.levels.get(i).getContainsVulgar()) {
				request.setBackground(new Color(150, 150, 0));
				request.setUI(noticeUI);
				lAnalyzed.setText("Analyzed");
			} else if (Requests.levels.get(i).getContainsImage()) {
				request.setBackground(new Color(150, 0, 0));
				request.setUI(warningUI);
				lAnalyzed.setText("Analyzed");
			} else if (Requests.levels.get(i).getAnalyzed()) {
				request.setBackground(Defaults.MAIN);
				request.setUI(defaultUI);
				lAnalyzed.setText("Analyzed");
			}

			else {
				request.setBackground(Defaults.MAIN);
				request.setUI(defaultUI);
				if (Requests.levels.get(i).getStars() > 0) {
					lAnalyzed.setText("");
				} else {
					lAnalyzed.setText("Analyzing...");
				}
			}
			lAnalyzed.setBounds((int) (400 - lAnalyzed.getPreferredSize().getWidth()) - 10, 26,
					(int) lAnalyzed.getPreferredSize().getWidth(), 20);

			request.setBorder(BorderFactory.createEmptyBorder());
			request.setPreferredSize(new Dimension(width, 50));

			request.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					((InnerWindow) window).moveToFront();
					super.mousePressed(e);
					Component[] comp = panel.getComponents();
					for (int j = 0; j < comp.length; j++) {
						if (comp[j] instanceof JButton) {
							if (Requests.levels.get(j).getContainsVulgar()) {
								comp[j].setBackground(new Color(150, 150, 0));
								((JButton) comp[j]).setUI(noticeUI);
							} else if (Requests.levels.get(j).getContainsImage()) {
								comp[j].setBackground(new Color(150, 0, 0));
								((JButton) comp[j]).setUI(warningUI);

							} else {
								((JButton) comp[j]).setUI(defaultUI);
								comp[j].setBackground(Defaults.MAIN);
							}

							panel.updateUI();

						}
					}
					if (!lID.getText().equals(Requests.levels.get(ID).getLevelID())) {
						CommentsWindow.unloadComments();
					}
					for (int j = 0; j < Requests.levels.size(); j++) {

						if (lID.getText().equals(Requests.levels.get(j).getLevelID())) {
							if (Requests.levels.get(j).getContainsVulgar()) {
								request.setUI(noticeSelectUI);
							} else if (Requests.levels.get(j).getContainsImage()) {
								request.setUI(warningSelectUI);

							} else {
								request.setUI(selectUI);
							}
							ID = j;
						}
					}
					SongWindow.refreshInfo();
					InfoWindow.refreshInfo();

				}
			});
			SongWindow.refreshInfo();
			InfoWindow.refreshInfo();
			panel.add(request);
		}
		scrollPane.updateUI();
		panel.updateUI();

	}

	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		defaultUI.setBackground(Defaults.MAIN);
		selectUI.setBackground(Defaults.SELECT);
		defaultUI.setHover(Defaults.HOVER);
		selectUI.setHover(Defaults.BUTTON_HOVER);
		panel.setBackground(Defaults.MAIN);
		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.MAIN);

			}
		}
	}

	static int getID() {
		return ID;
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
