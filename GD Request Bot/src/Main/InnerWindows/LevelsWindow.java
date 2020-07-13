package Main.InnerWindows;

import com.jidesoft.swing.Resizable;
import com.jidesoft.swing.ResizablePanel;
import Main.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

import static javax.swing.ScrollPaneConstants.*;

public class LevelsWindow {

	private static int width = 400;
	private static int buttonWidth = 385;

	private static int height = 400;
	private static ResizablePanel window = new InnerWindow("Requests - 0", Settings.getRequestsWLoc().x, Settings.getRequestsWLoc().y, width, height, "\uE179", false) {
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
						scrollPane.setBounds(1, 31, width, newH - 32);
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
		scrollPane.setBounds(1, 31, width , height);
		scrollPane.setPreferredSize(new Dimension(400, height));
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
		window.add(scrollPane);
		((InnerWindow) window).refreshListener();
		if(!Settings.windowedMode) {
			Overlay.addToFrame(window);
		}
	}
	public static JScrollPane getReqWindow(){
		return scrollPane;
	}

	public static void createButton(String name, String author, long ID, String difficulty, boolean epic, boolean featured, int starCount, String requester, double version, ImageIcon playerIcon){
		Point saved = scrollPane.getViewport().getViewPosition();
		scrollPane.getViewport().setViewPosition( saved );
		mainPanel.add(new LevelButton(name, author, ID, difficulty, epic, featured, starCount, requester, version, playerIcon));
		if(Requests.levels.size() == 1){
			setOneSelect();
		}
		scrollPane.getViewport().setViewPosition(saved);
	}
	public static void setName(int count){
		if(Settings.windowedMode){
			((InnerWindow) Windowed.window).setTitle("GDBoard - " + count);
			Windowed.frame.setTitle("GDBoard - " + count);
		}
		else {
			((InnerWindow) window).setTitle("Requests - " + count);
		}
	}

	public static LevelButton getButton(int i) {
		return ((LevelButton) mainPanel.getComponent(i));
	}
	public static int getSize() {
		return mainPanel.getComponents().length;
	}

	public static void movePosition(int position, int newPosition){
		long selectID = -1;
		if(newPosition >= Requests.levels.size()){
			newPosition = Requests.levels.size()-1;
		}
		for(int  i = 0; i < Requests.levels.size(); i++){
			if(getButton(i).selected){
				selectID = Requests.levels.get(i).getLevelID();
			}
		}

		mainPanel.add(getButton(position), newPosition);
		LevelData data = Requests.levels.get(position);
		Requests.levels.remove(position);
		Requests.levels.add(newPosition, data);
		for(int  i = 0; i < Requests.levels.size(); i++){
			if(selectID == Requests.levels.get(i).getLevelID()){
				LevelsWindow.setSelect(i);
			}
		}
		Functions.saveFunction();
		mainPanel.invalidate();
		mainPanel.validate();
	}

	public static class LevelButton extends JButton{

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
		ImageIcon playerIcon;

		JLabel lName = new JLabel();
		JLabel lAuthorID = new JLabel();
		JLabel lRequester = new JLabel();
		JLabel lAnalyzed = new JLabel();
		JLabel lStarCount = new JLabel();
		JLabel lPlayerIcon = new JLabel();
		JLabel lStar = new JLabel("\uE24A");

		LevelButton(String name, String author, long ID, String difficulty, boolean epic, boolean featured, int starCount, String requester, double version, ImageIcon playerIcon){
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
							reqDifficulty.setIcon(Assets.difficultyIconsEpic.get(difficultyA));
						} else if (featured) {
							reqDifficulty.setIcon(Assets.difficultyIconsFeature.get(difficultyA));
						} else {
							reqDifficulty.setIcon(Assets.difficultyIconsNormal.get(difficultyA));
						}
					}
				}
				reqDifficulty.setBounds(10, 0, 50, 50);

				add(lName);
				add(lAuthorID);
				add(lRequester);
				add(lAnalyzed);
				add(lPlayerIcon);
				add(reqDifficulty);
				if (starCount != 0) {
					add(lStarCount);
					add(lStar);
				}
				setLayout(null);


				lName.setFont(Defaults.MAIN_FONT.deriveFont(20f));
				lName.setBounds(60, 2, (int) lName.getPreferredSize().getWidth() + 5, 30);
				lAuthorID.setFont(Defaults.MAIN_FONT.deriveFont(12f));
				lAuthorID.setBounds(60, 28, (int) lAuthorID.getPreferredSize().getWidth() + 5, 20);
				lPlayerIcon.setIcon(playerIcon);
				lPlayerIcon.setBounds(60 + lAuthorID.getPreferredSize().width + 2, 16, 40, 40);


				lRequester.setFont(Defaults.MAIN_FONT.deriveFont(12f));
				lRequester.setBounds((int) (buttonWidth - lRequester.getPreferredSize().getWidth()) - 10, 3,
						(int) lRequester.getPreferredSize().getWidth() + 5, 20);
				lStarCount.setFont(Defaults.MAIN_FONT.deriveFont(18f));
				lStarCount.setBounds(((int) (buttonWidth - lStarCount.getPreferredSize().getWidth()) - 30), 28,
						(int) lStarCount.getPreferredSize().getWidth() + 5, 20);
				lStar.setFont(Defaults.SYMBOLS.deriveFont(16f));
				lStar.setBounds((int) (buttonWidth - lStar.getPreferredSize().getWidth()) - 10, 25,
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
				setPreferredSize(new Dimension(buttonWidth, 50));

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
								((LevelButton)comp[j]).deselect();
							}
						}

						for (int j = 0; j < Requests.levels.size(); j++) {
							if (ID == Requests.levels.get(j).getLevelID()) {
								((LevelButton)comp[j]).select();
								selectedID = j;
							}
						}
						if (selectedID != prevSelectedID) {
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
				mainPanel.updateUI();
				((InnerWindow) window).refreshListener();

			} catch (Exception e) {
				e.printStackTrace();
				DialogBox.showDialogBox("Error!", e.toString(), "Please report to Alphalaneous.", new String[]{"OK"});
			}
		}
		public boolean viewership = false;
		public int gonePoints = 3;
		public long getID(){
			return ID;
		}
		public String getUsername(){
			return name;
		}
		public String getRequester(){
			return requester;
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
		public void setViewership(boolean viewer){
			if(viewer){
				lRequester.setForeground(Defaults.FOREGROUND);
				viewership = true;
				gonePoints = 3;
			}
			else{
				gonePoints = gonePoints - 1;
				if(gonePoints == 0){
					lRequester.setForeground(Color.RED);
					viewership = false;
					gonePoints = 0;
				}
			}
			Requests.levels.get(Requests.getPosFromID(ID)).setViewership(viewership);
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
			else{
				lAnalyzed.setText("Failed Analyzing");
			}
			lAnalyzed.setBounds((int) (buttonWidth - lAnalyzed.getPreferredSize().getWidth()) - 10, 28,
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
		public void refresh(boolean image, boolean vulgar){
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
				select();
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
				select();
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
		if(scrollPane != null) {
			scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
			scrollPane.setBackground(Defaults.MAIN);
			scrollPane.getViewport().setBackground(Defaults.MAIN);

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

	public static void updateUI(long ID, boolean vulgar, boolean image, boolean analyzed) {
		out: while(true){
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
