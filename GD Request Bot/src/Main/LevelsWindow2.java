package Main;

import com.jidesoft.swing.Resizable;
import com.jidesoft.swing.ResizablePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class LevelsWindow2 {

	private static int width = 400;
	private static int height = 400;
	private static ResizablePanel window = new InnerWindow("Requests", Settings.getRequestsWLoc().x, Settings.getRequestsWLoc().y, width, height, "src/resources/Icons/Queue.png"){
		@Override
		protected Resizable createResizable() {
			return new Resizable(this) {
				@Override
				public void resizing(int resizeCorner, int newX, int newY, int newW, int newH) {
					if(!(newH < 80 || newH > 800)) {

						if(newX + width >= 672 && newX <= 1248 && newY <= 93){
							newY = 93;
						}
						setBounds(getX(), newY, getWidth(), newH);
						resetDimensions(width, newH - 32);
						scrollPane.setBounds(1, 31, width + 1, newH - 32);
						scrollPane.updateUI();
					}
				}
			};
		}
	}.createPanel();
	private static JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	private static int selectedID = 0;
	private static int prevSelectedID = 0;
	private static JButtonUI defaultUI = new JButtonUI();
	private static JButtonUI selectUI = new JButtonUI();
	private static JButtonUI warningUI = new JButtonUI();
	private static JButtonUI noticeUI = new JButtonUI();
	private static JButtonUI warningSelectUI = new JButtonUI();
	private static JButtonUI noticeSelectUI = new JButtonUI();
	private static int panelHeight = 0;
	private static JScrollPane scrollPane;



	static void createPanel() {
		window.setDoubleBuffered(true);
		mainPanel.setBackground(Defaults.MAIN);
		mainPanel.setBounds(0, 0, width, panelHeight);
		mainPanel.setPreferredSize(new Dimension(400, panelHeight));

		scrollPane = new JScrollPane(mainPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Defaults.MAIN);
		scrollPane.setBounds(1, 31, width + 1, height);
		scrollPane.setPreferredSize(new Dimension(400, height));
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(1, height));
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

			private final Dimension d = new Dimension();
			@Override protected JButton createDecreaseButton(int orientation) {
				return new JButton() {
					@Override public Dimension getPreferredSize() {
						return d;
					}
				};
			}
			@Override protected JButton createIncreaseButton(int orientation) {
				return new JButton() {
					@Override public Dimension getPreferredSize() {
						return d;
					}
				};
			}

			@Override
			protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
				Graphics2D g2 = (Graphics2D) g.create();
				Color color = new Color(0,0,0,0);

				g2.setPaint(color);
				g2.fillRect(r.x, r.y, r.width, r.height);
				g2.dispose();
			}

			@Override
			protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
				Graphics2D g2 = (Graphics2D) g.create();
				Color color = new Color(0,0,0,0);


				g2.setPaint(color);
				g2.fillRect(r.x, r.y, r.width, r.height);
				g2.dispose();
			}
			@Override
			protected void setThumbBounds(int x, int y, int width, int height) {
				super.setThumbBounds(x, y, width, height);
				scrollbar.repaint();
			}
		});
		window.add(scrollPane);
		((InnerWindow) window).refreshListener();
		Overlay.addToFrame(window);
	}

	static void createButton(String name, String author, String ID, String difficulty,
							 boolean epic, boolean featured, int starCount) throws IOException {

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
		JLabel lStarCount = new JLabel(starCount + "*");

		String[] difficulties = { "NA", "easy", "normal", "hard", "harder", "insane", "easy demon", "medium demon",
				"hard demon", "insane demon", "extreme demon" };
		JLabel reqDifficulty = new JLabel();

		for (String difficultyA : difficulties) {
			if (difficulty.equalsIgnoreCase(difficultyA)) {
				if (epic) {
					reqDifficulty.setIcon(new ImageIcon(ImageIO
							.read(Objects.requireNonNull(LevelsWindow2.class.getClassLoader()
									.getResource("DifficultyIcons/Epic/" + difficultyA + ".png")))
							.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
				} else if (featured) {
					reqDifficulty.setIcon(new ImageIcon(ImageIO
							.read(Objects.requireNonNull(LevelsWindow2.class.getClassLoader()
									.getResource("DifficultyIcons/Featured/" + difficultyA + ".png")))
							.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
				} else {
					reqDifficulty.setIcon(new ImageIcon(ImageIO
							.read(Objects.requireNonNull(LevelsWindow2.class.getClassLoader()
									.getResource("DifficultyIcons/Normal/" + difficultyA + ".png")))
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
		System.out.println(starCount);
		if(starCount !=0){
			request.add(lStarCount);
		}
		request.setLayout(null);


		lName.setFont(new Font("bahnschrift", Font.PLAIN, 20));
		lName.setBounds(60, 0, (int) lName.getPreferredSize().getWidth(), 30);
		lID.setFont(new Font("bahnschrift", Font.PLAIN, 12));
		lID.setBounds(60, 26, (int) lID.getPreferredSize().getWidth(), 20);
		lAuthor.setFont(new Font("bahnschrift", Font.PLAIN, 12));
		lAuthor.setBounds((int) (400 - lAuthor.getPreferredSize().getWidth()) - 10, 3,
				(int) lAuthor.getPreferredSize().getWidth(), 20);
		lStarCount.setFont(new Font("bahnschrift", Font.PLAIN, 18));
		lStarCount.setBounds((int) (400 - lStarCount.getPreferredSize().getWidth()) - 10, 26,
				(int) lStarCount.getPreferredSize().getWidth(), 20);
		lAnalyzed.setFont(new Font("bahnschrift", Font.PLAIN, 12));

		lName.setForeground(Defaults.FOREGROUND);
		lAuthor.setForeground(Defaults.FOREGROUND);
		lID.setForeground(Defaults.FOREGROUND);
		lAnalyzed.setForeground(Defaults.FOREGROUND);
		lStarCount.setForeground(Defaults.FOREGROUND);

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
					}
				}
				if(selectedID != prevSelectedID){
					Thread thread = new Thread(() -> {
						CommentsWindow.unloadComments(true);
						CommentsWindow.loadComments(0, false);
					});
					thread.start();

					SongWindow.refreshInfo();
					InfoWindow.refreshInfo();
				}

				prevSelectedID = selectedID;
			}
		});
		if(Requests.levels.size() == 1){
			request.setBackground(Defaults.SELECT);
			request.setUI(selectUI);
			Thread thread = new Thread(() -> {
				CommentsWindow.unloadComments(true);
				CommentsWindow.loadComments(0, false);
			});
			thread.start();
		}
		SongWindow.refreshInfo();
		InfoWindow.refreshInfo();
		panelHeight = panelHeight + 50;
		mainPanel.setBounds(0, 0, width, panelHeight);
		mainPanel.setPreferredSize(new Dimension(width, panelHeight));
		// mainPanel.revalidate();
		scrollPane.updateUI();
		mainPanel.add(request);
		mainPanel.updateUI();
	}

	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		defaultUI.setBackground(Defaults.MAIN);
		selectUI.setBackground(Defaults.SELECT);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);
		mainPanel.setBackground(Defaults.MAIN);
		for(Component component : mainPanel.getComponents()){
			if(component instanceof JButton){
				component.setBackground(Defaults.MAIN);
				for(Component component2 : ((JButton) component).getComponents()){
					if(component2 instanceof JLabel){
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
			}
		}
	}
	static void setOneSelect(){
		for(Component component : mainPanel.getComponents()){
			if(component instanceof JButton){
				component.setBackground(Defaults.SELECT);
				((JButton) component).setUI(selectUI);
				break;
			}
		}
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
								((JButton) component).getComponent(3).setBounds(
										(int) (400 - ((JButton) component).getComponent(3).getPreferredSize()
												.getWidth()) - 10,
										26,
										(int) ((JButton) component).getComponent(3).getPreferredSize().getWidth(),
										20);
								component.setBackground(new Color(150, 0, 0));
								((JButton) component).setUI(warningUI);

							} else if (vulgar) {
								((JLabel) ((JButton) component).getComponent(3)).setText("Analyzed");
								((JButton) component).getComponent(3).setBounds(
										(int) (400 - ((JButton) component).getComponent(3).getPreferredSize()
												.getWidth()) - 10,
										26,
										(int) ((JButton) component).getComponent(3).getPreferredSize().getWidth(),
										20);
								component.setBackground(new Color(150, 150, 0));
								((JButton) component).setUI(noticeUI);
							} else if (analyzed) {
								((JLabel) ((JButton) component).getComponent(3)).setText("Analyzed");
								((JButton) component).getComponent(3).setBounds(
										(int) (400 - ((JButton) component).getComponent(3).getPreferredSize()
												.getWidth()) - 10,
										26,
										(int) ((JButton) component).getComponent(3).getPreferredSize().getWidth(),
										20);
							}
						}
					}
				}
			}
		}
	}
	static void removeButton(int i) {
		mainPanel.remove(i);
		selectedID = 0;
		panelHeight = panelHeight - 50;
        mainPanel.setBounds(0, 0, width, panelHeight);
        mainPanel.setPreferredSize(new Dimension(width, panelHeight));
		mainPanel.updateUI();
	}
	static void removeButton() {
		mainPanel.remove(selectedID);
		selectedID = 0;
        panelHeight = panelHeight - 50;
        mainPanel.setBounds(0, 0, width, panelHeight);
        mainPanel.setPreferredSize(new Dimension(width, panelHeight));
		mainPanel.updateUI();
	}
	static Point getLocationValue() {
		return window.getLocation();
	}
}
