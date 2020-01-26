package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class LevelsWindow2 {

	private static int width = 400;
	private static int height = 400;
	private static JPanel window = new InnerWindow("Requests", Settings.getRequestsWLoc().x, Settings.getRequestsWLoc().y, width, height, "src/resources/Icons/Queue.png")
			.createPanel();
	static JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	static int selectedID = 0;
	static JButtonUI defaultUI = new JButtonUI();
	static JButtonUI selectUI = new JButtonUI();
	static JButtonUI warningUI = new JButtonUI();
	static JButtonUI noticeUI = new JButtonUI();
	static JButtonUI warningSelectUI = new JButtonUI();
	static JButtonUI noticeSelectUI = new JButtonUI();
	static int panelHeight = 0;
	static JScrollPane scrollPane;

	static void createPanel() throws IOException, InterruptedException {
		mainPanel.setBackground(Defaults.MAIN);
		mainPanel.setBounds(0, 0, width, panelHeight);
		mainPanel.setPreferredSize(new Dimension(400, panelHeight));

		scrollPane = new JScrollPane(mainPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Defaults.MAIN);
		scrollPane.setBounds(1, 31, width, height);
		scrollPane.setPreferredSize(new Dimension(400, height));
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		window.add(scrollPane);
		((InnerWindow) window).refreshListener();
		Overlay.addToFrame(window);
	}

	static void createButton(String name, String author, String ID, String difficulty, boolean vulgar, boolean image,
			boolean epic, boolean featured, boolean analyzed) throws IOException {
		defaultUI.setBackground(Defaults.MAIN);

		defaultUI.setHover(Defaults.HOVER);

		selectUI.setBackground(Defaults.SELECT);

		selectUI.setHover(Defaults.BUTTON_HOVER);

		warningUI.setBackground(new Color(150, 0, 0));
		warningUI.setHover(new Color(170, 0, 0));

		noticeUI.setBackground(new Color(150, 150, 0));
		noticeUI.setHover(new Color(170, 170, 0));

		warningSelectUI.setBackground(new Color(190, 0, 0));
		warningSelectUI.setHover(new Color(200, 0, 0));
		warningSelectUI.setSelect(new Color(150, 0, 0));

		noticeSelectUI.setBackground(new Color(190, 190, 0));
		noticeSelectUI.setHover(new Color(200, 200, 0));
		noticeSelectUI.setSelect(new Color(150, 150, 0));

		JLabel lName = new JLabel(name);
		JLabel lID = new JLabel(ID);
		JLabel lAuthor = new JLabel(author);
		JLabel lAnalyzed = new JLabel();

		String[] difficulties = { "NA", "easy", "normal", "hard", "harder", "insane", "easy demon", "medium demon",
				"hard demon", "insane demon", "extreme demon" };
		JLabel reqDifficulty = new JLabel();

		for (String difficultyA : difficulties) {
			if (difficulty.equalsIgnoreCase(difficultyA)) {
				if (epic) {
					reqDifficulty.setIcon(new ImageIcon((Image) ImageIO
							.read(LevelsWindow.class.getClassLoader()
									.getResource("DifficultyIcons/Epic/" + difficultyA + ".png"))
							.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
				} else if (featured) {
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

		request.setBackground(Defaults.MAIN);
		request.setUI(defaultUI);
		if (Requests.levels.get(Requests.levels.size() - 1).getStars() > 0) {
			lAnalyzed.setText("");
		} else {
			lAnalyzed.setText("Analyzing...");
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
				Component[] comp = mainPanel.getComponents();
				for (int j = 0; j < comp.length; j++) {
					if (comp[j] instanceof JButton) {
						if (Requests.levels.get(j).getContainsVulgar() && Requests.levels.get(j).getAnalyzed()) {
							comp[j].setBackground(new Color(150, 150, 0));
							((JButton) comp[j]).setUI(noticeUI);
						} else if (Requests.levels.get(j).getContainsImage() && Requests.levels.get(j).getAnalyzed()) {
							comp[j].setBackground(new Color(150, 0, 0));
							((JButton) comp[j]).setUI(warningUI);

						} else {
							((JButton) comp[j]).setUI(defaultUI);
							comp[j].setBackground(Defaults.MAIN);
						}
					}
				}
				if (!lID.getText().equals(Requests.levels.get(selectedID).getLevelID())) {
					CommentsWindow.unloadComments();
				}
				for (int j = 0; j < Requests.levels.size(); j++) {

					if (lID.getText().equals(Requests.levels.get(j).getLevelID())) {
						if (Requests.levels.get(j).getContainsVulgar() && Requests.levels.get(j).getAnalyzed()) {
							request.setUI(noticeSelectUI);
						} else if (Requests.levels.get(j).getContainsImage() && Requests.levels.get(j).getAnalyzed()) {
							request.setUI(warningSelectUI);

						} else {
							request.setUI(selectUI);
						}
						selectedID = j;
						System.out.println(j);
					}
				}
				SongWindow.refreshInfo();
				InfoWindow.refreshInfo();

			}
		});
		SongWindow.refreshInfo();
		InfoWindow.refreshInfo();
		panelHeight = panelHeight + 50;
		mainPanel.setBounds(0, 0, width, panelHeight);
		mainPanel.setPreferredSize(new Dimension(width, panelHeight));
		// mainPanel.revalidate();
		scrollPane.updateUI();
		System.out.println(mainPanel.getHeight());
		mainPanel.add(request);
		mainPanel.updateUI();
	}

	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		defaultUI.setBackground(Defaults.MAIN);
		selectUI.setBackground(Defaults.SELECT);
		defaultUI.setHover(Defaults.HOVER);
		selectUI.setHover(Defaults.BUTTON_HOVER);

	}

	static void toggleVisible() {
		((InnerWindow) window).toggle();
	}

	static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

	static int getSelectedID() {
		return selectedID;
	}

	static void setVisible() {
		((InnerWindow) window).setVisible();
	}

	static void updateUI(String ID, boolean vulgar, boolean image, boolean analyzed) {

		for (Component component : mainPanel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						if (((JLabel) component2).getText().equals(ID)) {
							if (image) {
								((JLabel) ((JButton) component).getComponent(3)).setText("Analyzed");
								((JLabel) ((JButton) component).getComponent(3)).setBounds(
										(int) (400 - ((JLabel) ((JButton) component).getComponent(3)).getPreferredSize()
												.getWidth()) - 10,
										26,
										(int) ((JLabel) ((JButton) component).getComponent(3)).getPreferredSize().getWidth(),
										20);
								((JButton) component).setBackground(new Color(150, 0, 0));
								((JButton) component).setUI(warningUI);

							} else if (vulgar) {
								((JLabel) ((JButton) component).getComponent(3)).setText("Analyzed");
								((JLabel) ((JButton) component).getComponent(3)).setBounds(
										(int) (400 - ((JLabel) ((JButton) component).getComponent(3)).getPreferredSize()
												.getWidth()) - 10,
										26,
										(int) ((JLabel) ((JButton) component).getComponent(3)).getPreferredSize().getWidth(),
										20);
								((JButton) component).setBackground(new Color(150, 150, 0));
								((JButton) component).setUI(noticeUI);
							} else if (analyzed) {
								((JLabel) ((JButton) component).getComponent(3)).setText("Analyzed");
								((JLabel) ((JButton) component).getComponent(3)).setBounds(
										(int) (400 - ((JLabel) ((JButton) component).getComponent(3)).getPreferredSize()
												.getWidth()) - 10,
										26,
										(int) ((JLabel) ((JButton) component).getComponent(3)).getPreferredSize().getWidth(),
										20);
							}
						}
					}
				}
			}
		}
	}
	public static void removeButton(int i) {
		mainPanel.remove(i);
		selectedID = 0;
		mainPanel.updateUI();
	}
	public static void removeButton() {
		mainPanel.remove(selectedID);
		selectedID = 0;
		mainPanel.updateUI();
	}
	static Point getLocationValue() {
		return window.getLocation();
	}
}
