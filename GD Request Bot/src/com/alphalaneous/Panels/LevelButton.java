package com.alphalaneous.Panels;

import com.alphalaneous.*;
import com.alphalaneous.Components.JButtonUI;
import com.alphalaneous.Components.RoundedJButton;
import com.alphalaneous.Windows.DialogBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.alphalaneous.Defaults.defaultUI;

public class LevelButton extends JButton {

	private static JButtonUI selectUI = new JButtonUI();
	private static JButtonUI warningUI = new JButtonUI();
	private static JButtonUI noticeUI = new JButtonUI();
	private static JButtonUI warningSelectUI = new JButtonUI();
	private static JButtonUI noticeSelectUI = new JButtonUI();


	private JButtonUI clear = new JButtonUI();

	private String name;
	public long ID;
	private String author;
	private String difficulty;
	private boolean epic;
	private boolean featured;
	private int starCount;
	private String requester;
	private double version;
	private boolean analyzed;
	private boolean image;
	private boolean vulgar;
	public boolean selected;
	private int coins;
	private ImageIcon playerIcon;
	private boolean expanded = false;

	private RoundedJButton analyzeButton = new RoundedJButton("\uE7BA", "WARNING");
	private JButton moveUp = new JButton("\uE010");
	private JButton moveDown = new JButton("\uE011");


	private JLabel lName = new JLabel();
	private JLabel lAuthorID = new JLabel();
	private JLabel lRequester = new JLabel();
	private JLabel lAnalyzed = new JLabel();
	private JLabel lStarCount = new JLabel();
	private JLabel lPlayerIcon = new JLabel();
	private JLabel lStar = new JLabel("\uE24A");
	private JPanel info = new JPanel(new GridLayout(0, 2, 1, 1));
	private boolean viewership = false;
	private int gonePoints = 3;

	LevelButton(LevelData data) {
		this.name = data.getName();
		this.ID = data.getLevelID();
		this.author = data.getAuthor();
		this.difficulty = data.getDifficulty();
		this.epic = data.getEpic();
		this.featured = data.getFeatured();
		this.starCount = data.getStars();
		this.requester = data.getRequester();
		this.version = data.getVersion();
		this.playerIcon = data.getPlayerIcon();
		this.coins = data.getCoins();
		try {
			if (LevelsPanel.getSize() == 0) {
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


			String[] difficulties = {"NA", "auto", "easy", "normal", "hard", "harder", "insane", "easy demon", "medium demon",
					"hard demon", "insane demon", "extreme demon"};
			JLabel reqDifficulty = new JLabel();

			for (String difficultyA : difficulties) {
				if (difficulty.equalsIgnoreCase(difficultyA)) {
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


			lName.setFont(Defaults.SEGOE.deriveFont(18f));
			lName.setBounds(50, -3, (int) lName.getPreferredSize().getWidth() + 5, 30);

			int pos = 0;

			for (int i = 0; i < coins; i++) {
				JLabel coin;
				if (data.getVerifiedCoins()) {
					coin = new JLabel(Assets.verifiedCoin);
				} else {
					coin = new JLabel(Assets.unverifiedCoin);
				}
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
			info.setBounds(50, 62, LevelsPanel.getButtonWidth() - 100, 100);
			info.setOpaque(false);
			info.setVisible(false);
			add(info);

			moveUp.setFont(Defaults.SYMBOLS.deriveFont(15f));
			moveUp.setUI(clear);
			moveUp.setForeground(Defaults.FOREGROUND);
			moveUp.setBackground(new Color(0, 0, 0, 0));
			moveUp.setOpaque(false);
			moveUp.setBounds(LevelsPanel.getButtonWidth() - 30, 0, 25, 30);
			moveUp.addActionListener(e -> {
				if (Main.programLoaded) {
					if (Requests.getPosFromID(ID) != 0) {
						LevelsPanel.movePosition(Requests.getPosFromID(ID), Requests.getPosFromID(ID) - 1);
					}
				}
			});
			moveUp.addMouseListener(new MouseAdapter() {
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
			moveDown.setBounds(LevelsPanel.getButtonWidth() - 30, 30, 25, 30);
			moveDown.addActionListener(e -> {
				if (Main.programLoaded) {
					if (Requests.getPosFromID(ID) != Requests.levels.size() - 1) {
						LevelsPanel.movePosition(Requests.getPosFromID(ID), Requests.getPosFromID(ID) + 1);
					}
				}
			});
			moveDown.addMouseListener(new MouseAdapter() {
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

			lAuthorID.setFont(Defaults.SEGOE.deriveFont(12f));
			lAuthorID.setBounds(50, 22, (int) lAuthorID.getPreferredSize().getWidth() + 5, 20);
			lPlayerIcon.setIcon(playerIcon);
			lPlayerIcon.setBounds(50 + lAuthorID.getPreferredSize().width + 2, 13, 40, 40);


			lRequester.setFont(Defaults.SEGOE.deriveFont(12f));
			lRequester.setBounds(50, 38, (int) lRequester.getPreferredSize().getWidth() + 5, 20);
			lStarCount.setFont(Defaults.SEGOE.deriveFont(12f));
			lStarCount.setBounds(25 - (lStarCount.getPreferredSize().width + lStar.getPreferredSize().width) / 2, 36,
					(int) lStarCount.getPreferredSize().getWidth() + 5, 20);
			lStar.setFont(Defaults.SYMBOLS.deriveFont(12f));
			lStar.setBounds(1 + lStarCount.getPreferredSize().width + lStarCount.getX(), 36,
					(int) lStar.getPreferredSize().getWidth() + 5, 20);
			lAnalyzed.setFont(Defaults.SEGOE.deriveFont(12f));

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

			lAnalyzed.setBounds((int) (LevelsPanel.getButtonWidth() - lAnalyzed.getPreferredSize().getWidth()) - 10, 28,
					(int) lAnalyzed.getPreferredSize().getWidth(), 20);

			setBorder(BorderFactory.createEmptyBorder());
			setPreferredSize(new Dimension(LevelsPanel.getButtonWidth(), 60));
			//setMaximumSize(new Dimension(LevelsPanel2.getButtonWidth(), 60));
			//setMinimumSize(new Dimension(LevelsPanel2.getButtonWidth(), 60));

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
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
				}
			});

			addActionListener(e -> {
				LevelsPanel.deselectAll();
				select();
				if (LevelsPanel.findButton(this) != LevelsPanel.getPrevSelectedID()) {
					if (Requests.levels.size() != 0) {
						new Thread(() -> {
							CommentsPanel.unloadComments(true);
							CommentsPanel.loadComments(0, false);
						}).start();
					}
					SongPanel.refreshInfo();
					InfoPanel.refreshInfo();
				}
				//System.out.println(average(reqDifficulty.getIcon()).brighter().toString());

			});
			if (Requests.levels.size() == 1) {
				setBackground(Defaults.SELECT);
				setUI(selectUI);
				Thread thread = new Thread(() -> {
					CommentsPanel.unloadComments(true);
					CommentsPanel.loadComments(0, false);
				});
				thread.start();
			}
			if (selected) {
				select();
			}
			SongPanel.refreshInfo();
			InfoPanel.refreshInfo();

		} catch (Exception e) {
			e.printStackTrace();
			DialogBox.showDialogBox("Error!", e.toString(), "Please report to Alphalaneous.", new String[]{"OK"});
		}
	}

	public long getID() {
		return ID;
	}

	public String getUsername() {
		return name;
	}

	public String getRequester() {
		return requester;
	}

	public boolean getImage() {
		return image;
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

	void setAnalyzed(boolean analyzed, boolean image, boolean vulgar) {
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
		lAnalyzed.setBounds((int) (LevelsPanel.getButtonWidth() - lAnalyzed.getPreferredSize().getWidth()) - 10, 28,
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
		if (Requests.getPosFromID(ID) != -1) {
			JLabel likes = createLabel("Likes: " + Requests.levels.get(Requests.getPosFromID(ID)).getLikes());
			JLabel downloads = createLabel("Downloads: " + Requests.levels.get(Requests.getPosFromID(ID)).getDownloads());
			JLabel length = createLabel("Length: " + Requests.levels.get(Requests.getPosFromID(ID)).getLength());
			JLabel password = createLabel("Password: " + Requests.levels.get(Requests.getPosFromID(ID)).getPassword());

			long pass;
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

			JLabel songID = createLabel("Song ID: " + Requests.levels.get(Requests.getPosFromID(ID)).getSongID());
			JLabel songName = createLabel("Song: " + Requests.levels.get(Requests.getPosFromID(ID)).getSongName());
			JLabel songAuthor = createLabel("Song Artist: " + Requests.levels.get(Requests.getPosFromID(ID)).getSongAuthor());


			info.add(likes);
			info.add(downloads);
			info.add(length);
			info.add(password);
			info.add(objects);
			info.add(original);
			info.add(upload);
			info.add(update);
			info.add(version);
			info.add(songID);
			info.add(songName);
			info.add(songAuthor);
		}
		info.setVisible(true);
		expanded = true;
		setPreferredSize(new Dimension(LevelsPanel.getButtonWidth(), 170));

	}

	void deselect() {
		if (this.selected) {
			info.setVisible(false);
			info.removeAll();
			expanded = false;
			setPreferredSize(new Dimension(LevelsPanel.getButtonWidth(), 60));
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

	void resizeButton(int width) {
		moveUp.setBounds(LevelsPanel.getButtonWidth() - 30, 0, 25, 30);
		moveDown.setBounds(LevelsPanel.getButtonWidth() - 30, 30, 25, 30);
		info.setBounds(50, 60, LevelsPanel.getButtonWidth() - 100, 100);
		setPreferredSize(new Dimension(LevelsPanel.getButtonWidth(), getHeight()));
	}

	public void refresh(boolean image, boolean vulgar) {
		for (Component component : getComponents()) {
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);
			}
		}
		moveDown.setForeground(Defaults.FOREGROUND);
		moveUp.setForeground(Defaults.FOREGROUND);

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
		if (selected) {
			select();
		}
	}

	public void refresh() {
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);
		selectUI.setSelect(Defaults.SELECT);
		for (Component component : getComponents()) {
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);
			}
		}
		moveDown.setForeground(Defaults.FOREGROUND);
		moveUp.setForeground(Defaults.FOREGROUND);
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
		if (selected) {
			select();
		}
	}

	JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(Defaults.FOREGROUND2);
		label.setFont(Defaults.SEGOE.deriveFont(11f));
		return label;

	}
	private static Color average(Icon icon){

		int width = icon.getIconWidth();
		int height = icon.getIconHeight();
		BufferedImage bi = new BufferedImage(
				icon.getIconWidth(),
				icon.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		icon.paintIcon(null, g, 0,0);
		g.dispose();
		int total = 0;

		int avrR = 0;
		int avrG = 0;
		int avrB = 0;
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(!(new Color(bi.getRGB(x, y)).equals(new Color(0,0,0)) || new Color(bi.getRGB(x, y)).getAlpha() == 0)) {
					avrR = avrR + new Color(bi.getRGB(x, y)).getRed();
					avrG = avrG + new Color(bi.getRGB(x, y)).getGreen();
					avrB = avrB + new Color(bi.getRGB(x, y)).getBlue();
					total++;
				}
			}
		}
		avrR = avrR / total;
		avrG = avrG / total;
		avrB = avrB / total;
		return new Color(avrR, avrG, avrB);
	}
}