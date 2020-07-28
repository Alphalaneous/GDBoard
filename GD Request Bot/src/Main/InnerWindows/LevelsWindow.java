package Main.InnerWindows;

import Main.*;

import javax.swing.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static Main.Defaults.defaultUI;
import static javax.swing.ScrollPaneConstants.*;

public class LevelsWindow {

	private static int width = 400;
	private static int buttonWidth = 385;

	private static int height = 400;
	private static JPanel window = new InnerWindow("Requests - 0", Settings.getRequestsWLoc().x, Settings.getRequestsWLoc().y, width, height, "\uE179", false).createPanel();
	private static JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	private static int selectedID = 0;
	private static int prevSelectedID = 0;
	private static JButtonUI selectUI = new JButtonUI();
	private static JButtonUI warningUI = new JButtonUI();
	private static JButtonUI noticeUI = new JButtonUI();
	private static JButtonUI warningSelectUI = new JButtonUI();
	private static JButtonUI noticeSelectUI = new JButtonUI();
	private static int panelHeight = 90;
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
		scrollPane.setBackground(Defaults.MAIN);

		scrollPane.setBounds(1, 31, width, height);
		scrollPane.setPreferredSize(new Dimension(400, height));
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
		window.add(scrollPane);
		((InnerWindow) window).refreshListener();
		if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			Overlay.addToFrame(window);
		}
	}
	public static void destroyPanel(){
		try {
			if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
				Windowed.removeFromFrame(scrollPane);
			} else {
				Overlay.removeFromFrame(window);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void resizeButtons(int width, int height){
		buttonWidth = width - 15;
		Component[] comp = mainPanel.getComponents();
		for (int j = 0; j < comp.length; j++) {
			if (comp[j] instanceof LevelButton) {
				((LevelButton) comp[j]).resizeButton(width, height);
			}
		}

	}

	public static JScrollPane getReqWindow() {
		return scrollPane;
	}

	public static void createButton(String name, String author, long ID, String difficulty, boolean epic, boolean featured, int starCount, String requester, double version, ImageIcon playerIcon, int coins) {
		Point saved = scrollPane.getViewport().getViewPosition();
		scrollPane.getViewport().setViewPosition(saved);
		mainPanel.add(new LevelButton(name, author, ID, difficulty, epic, featured, starCount, requester, version, playerIcon, coins));
		if (Requests.levels.size() == 1) {
			setOneSelect();
		}
		scrollPane.getViewport().setViewPosition(saved);
	}

	public static void setName(int count) {
		if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			//((InnerWindow) Windowed.window).setTitle("GDBoard - " + count);
			Windowed.frame.setTitle("GDBoard - " + count);
		} else {
			((InnerWindow) window).setTitle("Requests - " + count);
		}
	}

	public static LevelButton getButton(int i) {
		return ((LevelButton) mainPanel.getComponent(i));
	}

	public static int getSize() {
		return mainPanel.getComponents().length;
	}

	public static void movePosition(int position, int newPosition) {
		long selectID = -1;
		if (newPosition >= Requests.levels.size()) {
			newPosition = Requests.levels.size() - 1;
		}
		for (int i = 0; i < Requests.levels.size(); i++) {
			if (getButton(i).selected) {
				selectID = Requests.levels.get(i).getLevelID();
			}
		}

		mainPanel.add(getButton(position), newPosition);
		LevelData data = Requests.levels.get(position);
		Requests.levels.remove(position);
		Requests.levels.add(newPosition, data);
		for (int i = 0; i < Requests.levels.size(); i++) {
			if (selectID == Requests.levels.get(i).getLevelID()) {
				LevelsWindow.setSelect(i);
			}
		}
		Functions.saveFunction();
	}

	public static class LevelButton extends JButton {

		JButtonUI clear = new JButtonUI();
		JButtonUI semiClear = new JButtonUI();

		String name;
		long ID;
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
		int coins;
		ImageIcon playerIcon;
		boolean expanded = false;

		RoundedJButton analyzeButton = new RoundedJButton("\uE7BA", "WARNING");
		JButton moveUp = new JButton("\uE010");
		JButton moveDown = new JButton("\uE011");


		JLabel lName = new JLabel();
		JLabel lAuthorID = new JLabel();
		JLabel lRequester = new JLabel();
		JLabel lAnalyzed = new JLabel();
		JLabel lStarCount = new JLabel();
		JLabel lPlayerIcon = new JLabel();
		JLabel lLikes = new JLabel();
		JLabel lDownloads = new JLabel();
		JLabel lLength = new JLabel();
		JLabel lStar = new JLabel("\uE24A");
		JPanel info = new JPanel(new GridLayout(0, 2, 1, 1));

		LevelButton(String name, String author, long ID, String difficulty, boolean epic, boolean featured, int starCount, String requester, double version, ImageIcon playerIcon, int coins) {
			this.name = name;
			this.ID = ID;
			this.author = author;
			this.difficulty = difficulty;
			this.epic = epic;
			this.featured = featured;
			this.starCount = starCount;
			this.requester = requester;
			this.version = version;
			this.playerIcon = playerIcon;
			this.coins = coins;
			try {
				if(LevelsWindow.getSize() == 0){
					this.selected = true;
				}

				clear.setBackground(new Color(0, 0, 0, 0));
				clear.setHover(new Color(0, 0, 0, 0));
				clear.setSelect(new Color(0, 0, 0, 0));

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
				lRequester.setText("Sent by " + requester);
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
							reqDifficulty.setIcon(Assets.difficultyIconsEpic.get(difficultyA));
						} else if (featured) {
							reqDifficulty.setIcon(Assets.difficultyIconsFeature.get(difficultyA));
						} else if (starCount != 0) {
							reqDifficulty.setIcon(Assets.difficultyIconsNormal.get(difficultyA));
						} else {
							reqDifficulty.setIcon(new ImageIcon(Assets.difficultyIconsNormal.get(difficultyA).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
						}

					}
				}
				if (starCount != 0) {
					reqDifficulty.setBounds(10, 5, 30, 30);
				} else {
					reqDifficulty.setBounds(10, 15, 30, 30);
				}

				add(lName);
				add(lAuthorID);
				add(lRequester);
				add(lPlayerIcon);
				add(reqDifficulty);
				if (starCount != 0) {
					add(lStarCount);
					add(lStar);
				}
				setLayout(null);


				lName.setFont(Defaults.MAIN_FONT.deriveFont(18f));
				lName.setBounds(50, 2, (int) lName.getPreferredSize().getWidth() + 5, 30);

				int pos = 0;

				for (int i = 0; i < coins; i++) {
					JLabel coin = new JLabel(Assets.verifiedCoin);
					coin.setBounds((int) lName.getPreferredSize().getWidth() + lName.getX() + 5 + pos, 7, 15, 15);
					pos = pos + 10;
					add(coin);
				}

				if (coins != 0) {
					analyzeButton.setBounds((int) lName.getPreferredSize().getWidth() + lName.getX() + 15 + pos, 3, 20, 20);
				} else {
					analyzeButton.setBounds((int) lName.getPreferredSize().getWidth() + lName.getX() + 5 + pos, 3, 20, 20);
				}
				analyzeButton.setFont(Defaults.SYMBOLS.deriveFont(18f));
				analyzeButton.setUI(clear);
				analyzeButton.setForeground(Defaults.FOREGROUND);
				analyzeButton.setBackground(new Color(0, 0, 0, 0));
				analyzeButton.setVisible(false);
				add(analyzeButton);


				info.setBackground(new Color(0, 0, 0, 0));
				info.setBounds(50, 60, buttonWidth - 100, 80);
				info.setOpaque(false);
				info.setVisible(false);
				add(info);

				moveUp.setFont(Defaults.SYMBOLS.deriveFont(15f));
				moveUp.setUI(clear);
				moveUp.setForeground(Defaults.FOREGROUND);
				moveUp.setBackground(new Color(0, 0, 0, 0));
				moveUp.setOpaque(false);
				moveUp.setBounds(buttonWidth - 30, 0, 25, 30);
				moveUp.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if(Main.programLoaded) {
							if (Requests.getPosFromID(ID) != 0) {
								LevelsWindow.movePosition(Requests.getPosFromID(ID), Requests.getPosFromID(ID) - 1);
							}
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						moveUp.setForeground(Defaults.FOREGROUND2);
					}

					@Override
					public void mouseExited(MouseEvent e) {
						moveUp.setForeground(Defaults.FOREGROUND);
					}
				});
				add(moveUp);


				moveDown.setFont(Defaults.SYMBOLS.deriveFont(15f));
				moveDown.setUI(clear);
				moveDown.setForeground(Defaults.FOREGROUND);
				moveDown.setBackground(new Color(0, 0, 0, 0));
				moveDown.setOpaque(false);
				moveDown.setBounds(buttonWidth - 30, 30, 25, 30);
				moveDown.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if(Main.programLoaded) {
							if (Requests.getPosFromID(ID) != Requests.levels.size() - 1) {
								LevelsWindow.movePosition(Requests.getPosFromID(ID), Requests.getPosFromID(ID) + 1);
							}
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						moveDown.setForeground(Defaults.FOREGROUND2);
					}

					@Override
					public void mouseExited(MouseEvent e) {
						moveDown.setForeground(Defaults.FOREGROUND);
					}
				});
				add(moveDown);

				lAuthorID.setFont(Defaults.MAIN_FONT.deriveFont(12f));
				lAuthorID.setBounds(50, 24, (int) lAuthorID.getPreferredSize().getWidth() + 5, 20);
				lPlayerIcon.setIcon(playerIcon);
				lPlayerIcon.setBounds(50 + lAuthorID.getPreferredSize().width + 2, 13, 40, 40);


				lRequester.setFont(Defaults.MAIN_FONT.deriveFont(12f));
				lRequester.setBounds(50, 40, (int) lRequester.getPreferredSize().getWidth() + 5, 20);
				lStarCount.setFont(Defaults.MAIN_FONT.deriveFont(12f));
				lStarCount.setBounds(25 - (int) (lStarCount.getPreferredSize().width + lStar.getPreferredSize().width) / 2, 38,
						(int) lStarCount.getPreferredSize().getWidth() + 5, 20);
				lStar.setFont(Defaults.SYMBOLS.deriveFont(12f));
				lStar.setBounds(1 + lStarCount.getPreferredSize().width + lStarCount.getX(), 36,
						(int) lStar.getPreferredSize().getWidth() + 5, 20);
				lAnalyzed.setFont(Defaults.MAIN_FONT.deriveFont(12f));

				lName.setForeground(Defaults.FOREGROUND);
				lRequester.setForeground(Defaults.FOREGROUND2);
				lAuthorID.setForeground(Defaults.FOREGROUND2);
				lAnalyzed.setForeground(Defaults.FOREGROUND);
				lStarCount.setForeground(Defaults.FOREGROUND);
				lStar.setForeground(Defaults.FOREGROUND);

				setBackground(Defaults.MAIN);
				setUI(defaultUI);
				if (starCount > 0) {
					lAnalyzed.setText("");
				} else if (version / 10 < 2) {
					lAnalyzed.setText("Old Level");
				} else {
					lAnalyzed.setText("Analyzing...");
				}

				lAnalyzed.setBounds((int) (buttonWidth - lAnalyzed.getPreferredSize().getWidth()) - 10, 28,
						(int) lAnalyzed.getPreferredSize().getWidth(), 20);

				setBorder(BorderFactory.createEmptyBorder());
				setPreferredSize(new Dimension(buttonWidth, 60));

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
									String.valueOf(ID));
							Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
							clipboard.setContents(selection, selection);
						}
						((InnerWindow) window).moveToFront();
						super.mousePressed(e);

						Component[] comp = mainPanel.getComponents();
						for (int j = 0; j < comp.length; j++) {
							if (comp[j] instanceof LevelButton) {
								((LevelButton) comp[j]).deselect();
							}
						}

						for (int j = 0; j < Requests.levels.size(); j++) {
							if (ID == Requests.levels.get(j).getLevelID()) {
								((LevelButton) comp[j]).select();
								selectedID = j;
							}
						}
						if (selectedID != prevSelectedID) {
							new Thread(() -> {
								CommentsWindow.unloadComments(true);
								CommentsWindow.loadComments(0, false);
							}).start();

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
				if(selected){
					select();
				}
				SongWindow.refreshInfo();
				InfoWindow.refreshInfo();
				panelHeight = panelHeight + 60;
				mainPanel.setBounds(0, 0, width, panelHeight);
				mainPanel.setPreferredSize(new Dimension(width, panelHeight));
				((InnerWindow) window).refreshListener();

			} catch (Exception e) {
				e.printStackTrace();
				DialogBox.showDialogBox("Error!", e.toString(), "Please report to Alphalaneous.", new String[]{"OK"});
			}
		}

		public boolean viewership = false;
		public int gonePoints = 3;

		public long getID() {
			return ID;
		}

		public String getUsername() {
			return name;
		}

		public String getRequester() {
			return requester;
		}

		public boolean getAnalyzed() {
			return analyzed;
		}

		public boolean getImage() {
			return image;
		}

		public boolean getVulgar() {
			return vulgar;
		}

		public void setViewership(boolean viewer) {
			if (viewer) {
				lRequester.setForeground(Defaults.FOREGROUND2);
				viewership = true;
				gonePoints = 3;
			} else {
				gonePoints = gonePoints - 1;
				if (gonePoints == 0) {
					lRequester.setForeground(Color.RED);
					viewership = false;
					gonePoints = 0;
				}
			}
			Requests.levels.get(Requests.getPosFromID(ID)).setViewership(viewership);
		}

		public void setAnalyzed(boolean analyzed, boolean image, boolean vulgar) {
			this.analyzed = analyzed;
			this.image = image;
			this.vulgar = vulgar;

			if (image) {
				analyzeButton.setTooltip("IMAGE HACK");
				analyzeButton.setVisible(true);
				setBackground(new Color(150, 0, 0));
				setUI(warningUI);
			} else if (vulgar) {
				analyzeButton.setTooltip("VULGAR LANGUAGE");
				analyzeButton.setVisible(true);
				setBackground(new Color(150, 150, 0));
				setUI(noticeUI);
			} else if (analyzed) {
				lAnalyzed.setText("Analyzed");
			} else {
				lAnalyzed.setText("Failed Analyzing");
			}
			lAnalyzed.setBounds((int) (buttonWidth - lAnalyzed.getPreferredSize().getWidth()) - 10, 28,
					(int) lAnalyzed.getPreferredSize().getWidth(), 20);

		}

		public void select() {
			this.selected = true;
			if (image) {
				setUI(warningSelectUI);
				setBackground(new Color(200, 0, 0));
			} else if (vulgar) {
				setUI(noticeSelectUI);
				setBackground(new Color(200, 150, 0));

			} else {
				setBackground(Defaults.SELECT);
				setUI(selectUI);

			}
			info.setVisible(false);

			info.removeAll();
			if(Requests.getPosFromID(ID) != -1) {
				JLabel likes = createLabel("Likes: " + Requests.levels.get(Requests.getPosFromID(ID)).getLikes());
				JLabel downloads = createLabel("Downloads: " + Requests.levels.get(Requests.getPosFromID(ID)).getDownloads());
				JLabel length = createLabel("Length: " + Requests.levels.get(Requests.getPosFromID(ID)).getLength());
				JLabel password = createLabel("Password: " + Requests.levels.get(Requests.getPosFromID(ID)).getPassword());

				long pass = 0;
				if ((pass = Requests.levels.get(Requests.getPosFromID(ID)).getPassword()) != 0) {
					if (pass == -2) {
						password.setText("Free copy");
					} else if (pass == -1) {
						password.setText("No copy");
					} else {
						password.setText("Password: " + pass);
					}
				} else {
					password.setText("Pass: NA");

				}

				long obj = Requests.levels.get(Requests.getPosFromID(ID)).getObjects();
				JLabel objects;
				if (obj == 0) {
					objects = createLabel("Objects: NA");
				} else {
					objects = createLabel("Objects: " + obj);
				}

				long orig = Requests.levels.get(Requests.getPosFromID(ID)).getOriginal();
				JLabel original;
				if (orig == 0) {
					original = createLabel("Original");
				} else {
					original = createLabel("Original: " + orig);
				}
				JLabel upload = createLabel("Upload: " + Requests.levels.get(Requests.getPosFromID(ID)).getUpload());
				JLabel update = createLabel("Update: " + Requests.levels.get(Requests.getPosFromID(ID)).getUpdate());
				JLabel version = createLabel("Version: " + Requests.levels.get(Requests.getPosFromID(ID)).getLevelVersion());


				info.add(likes);
				info.add(downloads);
				info.add(length);
				info.add(password);
				info.add(objects);
				info.add(original);
				info.add(upload);
				info.add(update);
				info.add(version);
			}
			info.setVisible(true);
			expanded = true;
			setPreferredSize(new Dimension(buttonWidth, 150));

		}

		public void deselect() {
			if (this.selected) {
				info.setVisible(false);
				info.removeAll();
				expanded = false;
				setPreferredSize(new Dimension(buttonWidth, 60));
			}
			this.selected = false;
			if (image) {
				setUI(warningUI);
				setBackground(new Color(150, 0, 0));
			} else if (vulgar) {
				setUI(noticeUI);
				setBackground(new Color(150, 150, 0));
			} else {
				setBackground(Defaults.MAIN);
				setUI(defaultUI);
			}

		}

		public void resizeButton(int width, int height){
			buttonWidth = width-15;
			moveUp.setBounds(buttonWidth - 30, 0, 25, 30);
			moveDown.setBounds(buttonWidth - 30, 30, 25, 30);
			info.setBounds(50, 60, buttonWidth - 100, 80);
			setPreferredSize(new Dimension(buttonWidth, getHeight()));
		}

		public void refresh(boolean image, boolean vulgar) {
			for (Component component : getComponents()) {
				if (component instanceof JLabel) {
					component.setForeground(Defaults.FOREGROUND);
				}
			}
			analyzeButton.setForeground(Defaults.FOREGROUND);
			lRequester.setForeground(Defaults.FOREGROUND2);
			lAuthorID.setForeground(Defaults.FOREGROUND2);
			if (selected) {
				if (image) {
					setBackground(new Color(200, 0, 0));
					setUI(warningSelectUI);
				} else if (vulgar) {
					setBackground(new Color(200, 150, 0));
					setUI(noticeSelectUI);
				} else {
					setBackground(Defaults.MAIN);
					setUI(selectUI);
				}
				select();
			} else {
				if (image) {
					setBackground(new Color(150, 0, 0));
					setUI(warningUI);
				} else if (vulgar) {
					setBackground(new Color(150, 150, 0));
					setUI(noticeUI);
				} else {
					setBackground(Defaults.MAIN);
					setUI(defaultUI);
				}
			}
			if(selected){
				System.out.println("jksdfhgkyeusfghd");
				select();
			}
		}

		public void refresh() {
			for (Component component : getComponents()) {
				if (component instanceof JLabel) {
					component.setForeground(Defaults.FOREGROUND);
				}
			}
			analyzeButton.setForeground(Defaults.FOREGROUND);
			lRequester.setForeground(Defaults.FOREGROUND2);
			lAuthorID.setForeground(Defaults.FOREGROUND2);
			if (selected) {
				if (image) {
					setBackground(new Color(200, 0, 0));
					setUI(warningSelectUI);
				} else if (vulgar) {
					setBackground(new Color(200, 150, 0));
					setUI(noticeSelectUI);
				} else {
					setBackground(Defaults.MAIN);
					setUI(selectUI);
				}
				select();
			} else {
				if (image) {
					setBackground(new Color(150, 0, 0));
					setUI(warningUI);
				} else if (vulgar) {
					setBackground(new Color(150, 150, 0));
					setUI(noticeUI);
				} else {
					setBackground(Defaults.MAIN);
					setUI(defaultUI);
				}
			}
			if(selected){
				System.out.println("jksdfhgkyeusfghd");
				select();
			}
		}

		public JLabel createLabel(String text) {


					JLabel label = new JLabel(text);
					label.setForeground(Defaults.FOREGROUND2);
					label.setFont(Defaults.MAIN_FONT.deriveFont(11f));
					return label;

		}
	}

	public static void setPin(boolean pin) {
		((InnerWindow) window).setPin(pin);
	}

	public static void refreshUI() {
		((InnerWindow) window).refreshUI();
		selectUI.setBackground(Defaults.SELECT);
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
		if (scrollPane != null) {
			scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
			scrollPane.setBackground(Defaults.MAIN);
			scrollPane.getViewport().setBackground(Defaults.MAIN);

		}
	}

	public static void setOneSelect() {
		if (mainPanel.getComponents().length != 0) {
			((LevelButton) mainPanel.getComponent(0)).select();
			selectedID = 0;
		}
	}

	public static void setSelect(int i) {
		int j = 0;
		for (Component component : mainPanel.getComponents()) {
			if (component instanceof LevelButton) {
				if (j == i) {
					((LevelButton) component).select();
					selectedID = i;
					break;
				}
				j++;
			}
		}
	}

	public String getName() {
		return "Requests";
	}

	public String getIcon() {
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

	public static void updateUI(long ID, boolean vulgar, boolean image, boolean analyzed) {
		out:
		while (true) {
			for (Component component : mainPanel.getComponents()) {
				if (component instanceof LevelButton) {
					if (((LevelButton) component).ID == ID) {
						((LevelButton) component).setAnalyzed(analyzed, image, vulgar);
						((LevelButton) component).refresh(image, vulgar);
						break out;
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
