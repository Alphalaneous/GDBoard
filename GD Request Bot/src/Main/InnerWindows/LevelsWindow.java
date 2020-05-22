package Main.InnerWindows;

import com.jidesoft.swing.Resizable;
import com.jidesoft.swing.ResizablePanel;
import Main.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class LevelsWindow {

	private static int width = 400;
	private static int height = 400;
	private static ResizablePanel window = new InnerWindow("Requests", Settings.getRequestsWLoc().x, Settings.getRequestsWLoc().y, width, height, "\uE179", false) {
		@Override
		protected Resizable createResizable() {
			return new Resizable(this) {
				@Override
				public void resizing(int resizeCorner, int newX, int newY, int newW, int newH) {
					if (!(newH < 80 || newH > 800)) {

						if (newX + width >= 672 && newX <= 1248 && newY <= 93) {
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


	public static void createPanel() {
		window.setDoubleBuffered(true);
		mainPanel.setBackground(Defaults.MAIN);
		mainPanel.setBounds(0, 0, width, panelHeight);
		mainPanel.setPreferredSize(new Dimension(400, panelHeight));

		//TODO Queue organization

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

			@Override
			protected JButton createDecreaseButton(int orientation) {
				return new JButton() {
					@Override
					public Dimension getPreferredSize() {
						return d;
					}
				};
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return new JButton() {
					@Override
					public Dimension getPreferredSize() {
						return d;
					}
				};
			}

			@Override
			protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
				Graphics2D g2 = (Graphics2D) g.create();
				Color color = new Color(0, 0, 0, 0);

				g2.setPaint(color);
				g2.fillRect(r.x, r.y, r.width, r.height);
				g2.dispose();
			}

			@Override
			protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
				Graphics2D g2 = (Graphics2D) g.create();
				Color color = new Color(0, 0, 0, 0);


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
	public static JScrollPane getReqWindow(){
		return scrollPane;
	}

	public static void createButton(String name, String author, String ID, String difficulty, boolean epic, boolean featured, int starCount, String requester, double version){
		mainPanel.add(new LevelButton(name, author, ID, difficulty, epic, featured, starCount, requester, version));
		if(Requests.levels.size() == 1){
			setOneSelect();
		}
	}
	public static class LevelButton extends JButton{

		String name;
		String ID;
		String author;
		String difficulty;
		boolean epic;
		boolean featured;
		int starCount;
		String requester;
		double version;
		boolean analyzed;
		boolean image;
		boolean vulgar;
		boolean selected;

		JLabel lName = new JLabel();
		JLabel lAuthorID = new JLabel();
		JLabel lRequester = new JLabel();
		JLabel lAnalyzed = new JLabel();
		JLabel lStarCount = new JLabel();
		JLabel lStar = new JLabel("\uE24A");

		LevelButton(String name, String author, String ID, String difficulty, boolean epic, boolean featured, int starCount, String requester, double version){
			this.name = name;
			this.ID = ID;
			this.author = author;
			this.difficulty = difficulty;
			this.epic = epic;
			this.featured = featured;
			this.starCount = starCount;
			this.requester = requester;
			this.version = version;
			try {
				defaultUI.setBackground(Defaults.MAIN);
				defaultUI.setHover(Defaults.HOVER);

				selectUI.setBackground(Defaults.SELECT);
				selectUI.setHover(Defaults.BUTTON_HOVER);

				warningUI.setBackground(new Color(150, 0, 0));
				warningUI.setHover(new Color(170, 0, 0));
				warningUI.setSelect(new Color(150, 0, 0));

				noticeUI.setBackground(new Color(150, 150, 0));
				noticeUI.setHover(new Color(170, 170, 0));
				noticeUI.setSelect(new Color(150, 150, 0));

				warningSelectUI.setBackground(new Color(190, 0, 0));
				warningSelectUI.setHover(new Color(200, 0, 0));
				warningSelectUI.setSelect(new Color(150, 0, 0));

				noticeSelectUI.setBackground(new Color(190, 190, 0));
				noticeSelectUI.setHover(new Color(200, 200, 0));
				noticeSelectUI.setSelect(new Color(150, 150, 0));

				lName.setText(name);
				lAuthorID.setText("By " + author + " (" + ID + ")");
				lRequester.setText(requester);
				lStarCount.setText(String.valueOf(starCount));


				String[] difficulties = {"NA", "easy", "normal", "hard", "harder", "insane", "easy demon", "medium demon",
						"hard demon", "insane demon", "extreme demon"};
				JLabel reqDifficulty = new JLabel();

				for (String difficultyA : difficulties) {
					if (difficulty.equalsIgnoreCase(difficultyA)) {
						if (difficulty.equalsIgnoreCase("insane") && starCount == 1) {
							difficultyA = "auto";
						}
						if (epic) {
							reqDifficulty.setIcon(new ImageIcon(ImageIO
									.read(Objects.requireNonNull(LevelsWindow.class.getClassLoader()
											.getResource("Resources/DifficultyIcons/Epic/" + difficultyA + ".png")))
									.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
						} else if (featured) {
							reqDifficulty.setIcon(new ImageIcon(ImageIO
									.read(Objects.requireNonNull(LevelsWindow.class.getClassLoader()
											.getResource("Resources/DifficultyIcons/Featured/" + difficultyA + ".png")))
									.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
						} else {
							reqDifficulty.setIcon(new ImageIcon(ImageIO
									.read(Objects.requireNonNull(LevelsWindow.class.getClassLoader()
											.getResource("Resources/DifficultyIcons/Normal/" + difficultyA + ".png")))
									.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
						}
					}
				}
				reqDifficulty.setBounds(10, 0, 50, 50);

				add(lName);
				add(lAuthorID);
				add(lRequester);
				add(lAnalyzed);
				add(reqDifficulty);
				System.out.println(starCount);
				if (starCount != 0) {
					add(lStarCount);
					add(lStar);
				}
				setLayout(null);


				lName.setFont(Defaults.MAIN_FONT.deriveFont(20f));
				lName.setBounds(60, 2, (int) lName.getPreferredSize().getWidth() + 5, 30);
				lAuthorID.setFont(Defaults.MAIN_FONT.deriveFont(12f));
				lAuthorID.setBounds(60, 28, (int) lAuthorID.getPreferredSize().getWidth() + 5, 20);
				lRequester.setFont(Defaults.MAIN_FONT.deriveFont(12f));
				lRequester.setBounds((int) (400 - lRequester.getPreferredSize().getWidth()) - 10, 3,
						(int) lRequester.getPreferredSize().getWidth() + 5, 20);
				lStarCount.setFont(Defaults.MAIN_FONT.deriveFont(18f));
				lStarCount.setBounds(((int) (400 - lStarCount.getPreferredSize().getWidth()) - 30), 28,
						(int) lStarCount.getPreferredSize().getWidth() + 5, 20);
				lStar.setFont(Defaults.SYMBOLS.deriveFont(16f));
				lStar.setBounds((int) (400 - lStar.getPreferredSize().getWidth()) - 10, 25,
						(int) lStar.getPreferredSize().getWidth() + 5, 20);
				lAnalyzed.setFont(Defaults.MAIN_FONT.deriveFont(12f));

				lName.setForeground(Defaults.FOREGROUND);
				lRequester.setForeground(Defaults.FOREGROUND);
				lAuthorID.setForeground(Defaults.FOREGROUND);
				lAnalyzed.setForeground(Defaults.FOREGROUND);
				lStarCount.setForeground(Defaults.FOREGROUND);
				lStar.setForeground(Defaults.FOREGROUND);

				setBackground(Defaults.MAIN);
				setUI(defaultUI);
				System.out.println(version);
				if (starCount > 0) {
					lAnalyzed.setText("");
				} else if (version / 10 < 2) {
					lAnalyzed.setText("Old Level");
				} else {
					lAnalyzed.setText("Analyzing...");
				}

				lAnalyzed.setBounds((int) (400 - lAnalyzed.getPreferredSize().getWidth()) - 10, 28,
						(int) lAnalyzed.getPreferredSize().getWidth(), 20);

				setBorder(BorderFactory.createEmptyBorder());
				setPreferredSize(new Dimension(width, 50));

				addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (SwingUtilities.isMiddleMouseButton(e)) {
							if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
								try {
									Runtime rt = Runtime.getRuntime();
									rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://www.gdbrowser.com/" + ID);
								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}
						}
						if (SwingUtilities.isRightMouseButton(e)) {
							StringSelection selection = new StringSelection(
									ID);
							Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
							clipboard.setContents(selection, selection);
						}
						((InnerWindow) window).moveToFront();
						super.mousePressed(e);

						Component[] comp = mainPanel.getComponents();
						for (int j = 0; j < comp.length; j++) {
							if (comp[j] instanceof LevelButton) {
								((LevelButton)comp[j]).deselect();
							}
						}

						for (int j = 0; j < Requests.levels.size(); j++) {
							if (ID.equalsIgnoreCase(Requests.levels.get(j).getLevelID())) {
								((LevelButton)comp[j]).select();
								selectedID = j;
							}
						}
						if (selectedID != prevSelectedID) {
							Thread thread = new Thread(() -> {
								while(true) {
									try {
										CommentsWindow.unloadComments(true);
										CommentsWindow.loadComments(0, false);
										break;
									} catch (Exception f) {
										f.printStackTrace();
									}
									try {
										Thread.sleep(50);
									} catch (InterruptedException e1) {
										e1.printStackTrace();
									}
								}
							});
							thread.start();

							SongWindow.refreshInfo();
							InfoWindow.refreshInfo();
						}

						prevSelectedID = selectedID;
					}
				});
				if (Requests.levels.size() == 1) {
					setBackground(Defaults.SELECT);
					setUI(selectUI);
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
				scrollPane.updateUI();
				mainPanel.updateUI();
				((InnerWindow) window).refreshListener();

			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		public String getID(){
			return ID;
		}
		public String getUsername(){
			return name;
		}
		public String getRequester(){
			return name;
		}
		public boolean getAnalyzed(){
			return analyzed;
		}
		public boolean getImage(){
			return image;
		}
		public boolean getVulgar(){
			return vulgar;
		}
		public void setAnalyzed(boolean analyzed, boolean image, boolean vulgar){
			this.analyzed = analyzed;
			this.image = image;
			this.vulgar = vulgar;

			if(image){
				setBackground(new Color(150, 0, 0));
				setUI(warningUI);
				lAnalyzed.setText("Image Hack");
			}
			else if(vulgar){
				setBackground(new Color(150, 150, 0));
				setUI(noticeUI);
				lAnalyzed.setText("Vulgar Language");
			}
			else if(analyzed) {
				lAnalyzed.setText("Analyzed");
			}
			else {
				lAnalyzed.setText("Failed Analyzing");
			}
			lAnalyzed.setBounds((int) (400 - lAnalyzed.getPreferredSize().getWidth()) - 10, 28,
					(int) lAnalyzed.getPreferredSize().getWidth(), 20);
		}
		public void select(){
			this.selected = true;
			if(image) {
				setUI(warningSelectUI);
				setBackground(new Color(200, 0, 0));
			}
			else if(vulgar) {
				setUI(noticeSelectUI);
				setBackground(new Color(200, 150, 0));
			}
			else{
				setBackground(Defaults.SELECT);
				setUI(selectUI);
			}
		}
		public void deselect(){
			this.selected = false;
			if(image) {
				setUI(warningUI);
				setBackground(new Color(150, 0, 0));
			}
			else if(vulgar) {
				setUI(noticeUI);
				setBackground(new Color(150, 150, 0));
			}
			else{
				setBackground(Defaults.MAIN);
				setUI(defaultUI);
			}
		}
		public void refresh(boolean analyzed, boolean image, boolean vulgar){
			for (Component component : getComponents()) {
				if (component instanceof JLabel) {
					component.setForeground(Defaults.FOREGROUND);
				}
			}
			if(selected){
				if (image) {
					setBackground(new Color(200, 0, 0));
					setUI(warningSelectUI);
				}
				else if (vulgar) {
					setBackground(new Color(200, 150, 0));
					setUI(noticeSelectUI);
				} else {
					setBackground(Defaults.MAIN);
					setUI(selectUI);
				}
			}
			else {
				if (image) {
					setBackground(new Color(150, 0, 0));
					setUI(warningUI);
				}
				else if (vulgar) {
					setBackground(new Color(150, 150, 0));
					setUI(noticeUI);
				} else {
					setBackground(Defaults.MAIN);
					setUI(defaultUI);
				}
			}
		}
		public void refresh(){
			for (Component component : getComponents()) {
				if (component instanceof JLabel) {
					component.setForeground(Defaults.FOREGROUND);
				}
			}
			if(selected){
				if (image) {
					setBackground(new Color(200, 0, 0));
					setUI(warningSelectUI);
				}
				else if (vulgar) {
					setBackground(new Color(200, 150, 0));
					setUI(noticeSelectUI);
				} else {
					setBackground(Defaults.MAIN);
					setUI(selectUI);
				}
			}
			else {
				if (image) {
					setBackground(new Color(150, 0, 0));
					setUI(warningUI);
				}
				else if (vulgar) {
					setBackground(new Color(150, 150, 0));
					setUI(noticeUI);
				} else {
					setBackground(Defaults.MAIN);
					setUI(defaultUI);
				}
			}
		}
	}

	public static void setPin(boolean pin) {
		((InnerWindow) window).setPin(pin);
	}

	public static void refreshUI() {
		((InnerWindow) window).refreshUI();
		defaultUI.setBackground(Defaults.MAIN);
		selectUI.setBackground(Defaults.SELECT);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);
		selectUI.setSelect(Defaults.SELECT);
		mainPanel.setBackground(Defaults.MAIN);
		int i = 0;
		for (Component component : mainPanel.getComponents()) {
			if (component instanceof LevelButton) {
				if (selectedID == i) {
					((LevelButton) component).select();
				} else {
					component.setBackground(Defaults.MAIN);
				}
				((LevelButton) component).refresh();
			}
			i++;
		}
	}

	public static void setOneSelect() {
		if(mainPanel.getComponents().length != 0) {
			((LevelButton) mainPanel.getComponent(0)).select();
			selectedID = 0;
		}
	}

	public static void setSelect(int i) {
		int j = 0;
		for (Component component : mainPanel.getComponents()) {
			if (component instanceof LevelButton) {
				if (j == i) {
					((LevelButton)component).select();
					selectedID = i;
					break;
				}
				j++;
			}
		}
	}
	public String getName(){
		return "Requests";
	}
	public String getIcon(){
		return "\uE179";
	}
	public static void toggleVisible() {
		((InnerWindow) window).toggle();
	}

	public static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

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

	public static int getSelectedID() {
		return selectedID;
	}

	public static void setVisible() {
		((InnerWindow) window).setVisible();
	}

	public static void updateUI(String ID, boolean vulgar, boolean image, boolean analyzed) {
		for(int i = 0; i < 10; i++) {
			for (Component component : mainPanel.getComponents()) {
				if (component instanceof LevelButton) {
					if (((LevelButton) component).ID.equalsIgnoreCase(ID)) {
						((LevelButton) component).setAnalyzed(analyzed, image, vulgar);
						((LevelButton) component).refresh(analyzed, image, vulgar);
						break;
					}
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeButton(int i) {
		mainPanel.remove(i);
		selectedID = 0;
		panelHeight = panelHeight - 50;
		mainPanel.setBounds(0, 0, width, panelHeight);
		mainPanel.setPreferredSize(new Dimension(width, panelHeight));
	}

	public static void removeButton() {
		mainPanel.remove(selectedID);
		selectedID = 0;
		panelHeight = panelHeight - 50;
		mainPanel.setBounds(0, 0, width, panelHeight);
		mainPanel.setPreferredSize(new Dimension(width, panelHeight));
	}
}
