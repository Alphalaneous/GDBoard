package Main.InnerWindows;

import Main.*;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.GDUser;
import com.github.alex1304.jdash.exception.MissingAccessException;
import com.github.alex1304.jdash.exception.SpriteLoadException;
import com.github.alex1304.jdash.graphics.SpriteFactory;
import com.github.alex1304.jdash.util.GDUserIconSet;
import com.jidesoft.swing.Resizable;
import com.jidesoft.swing.ResizablePanel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static Main.Defaults.defaultUI;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class CommentsWindow {
	private static JPanel panel = new JPanel();
	private static JPanel mainPanel = new JPanel(null);
	private static JButtonUI buttonUI = new JButtonUI();
	private static int height = 350;
	private static int width = 300;
	private static JPanel window = new InnerWindow("Comments", Settings.getCommentWLoc().x, Settings.getCommentWLoc().y, width, height,
			"\uEBDB", false).createPanel();
	private static JButtonUI newUI = new JButtonUI();
	private static JScrollPane scrollPane = new JScrollPane(panel);
	private static JPanel buttons = new JPanel();
	private static boolean topC = false;
	private static int page = 0;

	//region Create Panel
	public static void createPanel() {

		//region Panel attributes
		panel.setLayout(null);
		panel.setBounds(0, 0, width, 0);
		mainPanel.setBounds(5, 5, width, height+30);
		panel.setBackground(Defaults.SUB_MAIN);
		panel.setPreferredSize(new Dimension(width, 0));
		//endregion
		buttonUI.setBackground(Defaults.TOP);
		buttonUI.setHover(Defaults.BUTTON_HOVER);
		buttonUI.setSelect(Defaults.SELECT);
		//region ScrollPane attributes
		scrollPane.setBackground(Defaults.SUB_MAIN);
		scrollPane.getViewport().setBackground(Defaults.SUB_MAIN);
		scrollPane.setBackground(Defaults.SUB_MAIN);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setBounds(0, 30, width, height - 30);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
		mainPanel.add(scrollPane);
		//endregion

		//region Buttons Panel attributes
		buttons.setLayout(null);
		buttons.setBounds(0, mainPanel.getHeight()-30, width, 30);
		buttons.setBackground(Defaults.TOP);
		mainPanel.add(buttons);
		//endregion

		//region Create Top Comments Button
		JButton top = createButton("\uE8E1", 90);
		top.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				page = 0;
				try {
					loadComments(0, true);
				} catch (Exception ignored) {
				}
			}
		});
		buttons.add(top);
		//endregion

		//region Create Recent Comments Button
		JButton recent = createButton("\uE823", 120);
		recent.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				topC = false;
				page = 0;
				try {
					loadComments(0, false);
				} catch (Exception ignored) {
				}
			}
		});
		buttons.add(recent);
		//endregion

		//region Create Previous Page Button
		JButton prev = createButton("\uE760", 0);
		prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				if (page != 0) {
					page = page-2;
					try {
						loadComments(page, topC);
					} catch (Exception ignored) {
					}
				}
			}
		});
		buttons.add(prev);
		//endregion

		JButton next = createButton("\uE761", 30);
		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				page = page + 2;
				if(!loadComments(page, topC)){
					page = page - 2;
					try {
						loadComments(page, topC);
					} catch (Exception f) {
						f.printStackTrace();
					}
				}
			}
		});
		buttons.add(next);


		newUI.setBackground(Defaults.MAIN);
		newUI.setHover(Defaults.HOVER);
		panel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 4));
		panel.setVisible(false);
		window.add(mainPanel);
		((InnerWindow) window).refreshListener();
		try {
			if(!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
				Overlay.addToFrame(window);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//endregion
	public static void unloadComments(boolean reset){
		panel.setVisible(false);
		if (reset) {
			topC = false;
			page = 0;
		}
		panel.removeAll();
		panel.setPreferredSize(new Dimension(width, 0));
		scrollPane.updateUI();
	}
	public static JPanel getComWindow(){
		scrollPane.setBounds(0, 0, width, 482);
		buttons.setBounds(0, 482, width, 30);

		return mainPanel;
	}
	public static void resetDimensions(int width, int height){
		scrollPane.setBounds(0, 0, width, height-70);
		buttons.setBounds(0, height-70, width, 30);
	}
	public static boolean loadComments(int page, boolean top){
		topC = top;
		int width = CommentsWindow.width - 15;
		try {
			if(Windowed.showingMore) {
				int panelHeight = 0;
				panel.removeAll();
				panel.setVisible(false);
				URL gdAPI = null;
				String message = null;
				for (int j = 0; j < 2; j++) {
					ArrayList<Comment> commentA = APIs.getGDComments(page + j, top, Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID());
					for (int i = 0; i < commentA.size(); i++) {
						Thread.sleep(10);
						String percent;
						String username = commentA.get(i).getUsername();
						String likes = commentA.get(i).getLikes();
						String date = "";
						String comment = String.format("<html><div WIDTH=%d>%s</div></html>", width - 8, StringEscapeUtils.unescapeHtml4(commentA.get(i).getComment()));
						try {
							percent = StringEscapeUtils.unescapeHtml4(commentA.get(i).getPercent() + "%");
						} catch (Exception e) {
							percent = "";
						}
						if (percent.equalsIgnoreCase("0%")) {
							percent = "";
						}
						JPanel cmtPanel = new JPanel(null);
						cmtPanel.setBackground(Defaults.MAIN);

						JLabel commenter = new JLabel(username);
						commenter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						commenter.setFont(Defaults.MAIN_FONT.deriveFont(14f));
						if (username.equalsIgnoreCase(Requests.levels.get(LevelsWindow.getSelectedID()).getAuthor().toString())) {
							commenter.setForeground(new Color(47, 62, 195));
						} else {
							commenter.setForeground(Defaults.FOREGROUND);
						}
						commenter.setBounds(30, 4, commenter.getPreferredSize().width, 18);
						int finalI = i;
						final ArrayList<Comment>[] finalCommentA = new ArrayList[]{commentA};
						commenter.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								super.mouseClicked(e);
								if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
									try {
										Runtime rt = Runtime.getRuntime();
										rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://www.gdbrowser.com/profile/" + finalCommentA[0].get(finalI).getUsername());
										finalCommentA[0].clear();
										finalCommentA[0] = null;
									} catch (IOException ex) {
										ex.printStackTrace();
									}
								}
							}

							@Override
							public void mouseEntered(MouseEvent e) {
								super.mouseEntered(e);
								commenter.setFont(Defaults.MAIN_FONT.deriveFont(15f));
								commenter.setBounds(28, 4, commenter.getPreferredSize().width + 5, 18);
							}

							@Override
							public void mouseExited(MouseEvent e) {
								super.mouseExited(e);
								commenter.setFont(Defaults.MAIN_FONT.deriveFont(14f));
								commenter.setBounds(30, 4, commenter.getPreferredSize().width, 18);
							}
						});

						JLabel percentLabel = new JLabel(percent);
						percentLabel.setFont(Defaults.MAIN_FONT.deriveFont(14f));
						percentLabel.setForeground(Defaults.FOREGROUND2);
						percentLabel.setBounds(commenter.getPreferredSize().width + 42, 4, percentLabel.getPreferredSize().width + 5, 18);


						JLabel likeIcon = new JLabel();
						if (Integer.parseInt(likes.replaceAll("%", "")) < 0) {
							likeIcon.setText("\uE8E0");
						} else {
							likeIcon.setText("\uE8E1");
						}
						likeIcon.setFont(Defaults.SYMBOLS.deriveFont(14f));
						likeIcon.setForeground(Defaults.FOREGROUND);
						likeIcon.setBounds(width - 20, 4, (int) (width * 0.5), 18);


						JLabel likesLabel = new JLabel(likes);
						likesLabel.setFont(Defaults.MAIN_FONT.deriveFont(10f));
						likesLabel.setForeground(Defaults.FOREGROUND);
						likesLabel.setBounds(width - likesLabel.getPreferredSize().width - 26, 6, likesLabel.getPreferredSize().width + 5, 18);
						JLabel playerIcon = new JLabel();
						new Thread(() -> {
							AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();

							GDUserIconSet iconSet = null;
							GDUser user = null;
							try {
								user = client.searchUser(username).block();

								try {
									iconSet = new GDUserIconSet(user, SpriteFactory.create());
								} catch (SpriteLoadException e) {
									e.printStackTrace();
								}
							} catch (MissingAccessException e) {
								user = client.searchUser("RobTop").block();
								try {
									iconSet = new GDUserIconSet(user, SpriteFactory.create());
								} catch (SpriteLoadException e1) {
									e1.printStackTrace();
								}
							}
							BufferedImage icon = iconSet.generateIcon(user.getMainIconType());
							Image imgScaled = icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
							ImageIcon imgNew = new ImageIcon(imgScaled);
							playerIcon.setIcon(imgNew);
							playerIcon.setBounds(2, -5, imgNew.getIconWidth() + 2, imgNew.getIconHeight() + 2);
							icon = null;
							imgScaled = null;
							imgNew = null;
							iconSet = null;
							user = null;
						}).start();

						JLabel content = new JLabel(comment);
						content.setFont(Defaults.MAIN_FONT.deriveFont(12f));
						content.setForeground(Defaults.FOREGROUND);
						content.setBounds(9, 24, width - 8, content.getPreferredSize().height);
						panelHeight = panelHeight + 32 + content.getPreferredSize().height;

						cmtPanel.add(commenter);
						cmtPanel.add(content);
						cmtPanel.add(percentLabel);
						cmtPanel.add(likesLabel);
						cmtPanel.add(likeIcon);
						cmtPanel.add(playerIcon);

						cmtPanel.setPreferredSize(new Dimension(width, 28 + content.getPreferredSize().height));

						((InnerWindow) window).refreshListener();
						panel.add(cmtPanel);
						panel.setPreferredSize(new Dimension(width, panelHeight));
					}
					commentA = null;
				}
				scrollPane.getViewport().setViewPosition(new Point(0, 0));
				panel.setVisible(true);
				return true;
			}
		}catch (Exception e){
			return false;
		}
		return false;
	}

	public String getName(){
		return "Comments";
	}
	public String getIcon(){
		return "\uEBDB";
	}

	//region Set Pin
	public static void setPin(boolean pin){
		((InnerWindow) window).setPin(pin);
	}
	//endregion

	//region CreateButton
	private static JButton createButton(String icon, int x){
		JButton button = new JButton(icon);
		button.setFont(Defaults.SYMBOLS.deriveFont(20f));
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setForeground(Defaults.FOREGROUND);
		button.setBackground(Defaults.TOP);
		button.setUI(buttonUI);
		button.setBounds(x, 0, 30, 30);
		return button;
	}
	//endregion

	//region RefreshUI
	public static void refreshUI() {
		((InnerWindow) window).refreshUI();
		buttonUI.setBackground(Defaults.TOP);
		buttonUI.setHover(Defaults.BUTTON_HOVER);
		buttonUI.setSelect(Defaults.SELECT);
		newUI.setBackground(Defaults.MAIN);
		newUI.setHover(Defaults.HOVER);
		newUI.setSelect(Defaults.SELECT);
		if(scrollPane != null) {
			scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
			scrollPane.setBackground(Defaults.MAIN);
			scrollPane.getViewport().setBackground(Defaults.SUB_MAIN);

		}
		for (Component component : panel.getComponents()) {
			if (component instanceof JPanel) {
				component.setBackground(Defaults.MAIN);
				for (Component component1 : ((JPanel) component).getComponents()) {
					if (component1 instanceof JLabel) {
						if(((JLabel) component1).getText().contains("%")){
							component1.setForeground(Defaults.FOREGROUND2);
						}
						else if(((JLabel) component1).getText().equalsIgnoreCase(Requests.levels.get(LevelsWindow.getSelectedID()).getAuthor().toString())){
							component1.setForeground(new Color(16, 164,0));
						}
						else {
							component1.setForeground(Defaults.FOREGROUND);
						}
					}
					if (component1 instanceof JTextPane) {
						component1.setForeground(Defaults.FOREGROUND);
					}
				}
			}
		}
		for (Component component : buttons.getComponents()) {
			if (component instanceof JButton) {
				component.setBackground(Defaults.TOP);
				component.setForeground(Defaults.FOREGROUND);
			}
		}
		panel.setBackground(Defaults.SUB_MAIN);
		buttons.setBackground(Defaults.TOP);
	}
	//endregion

	//region ToggleVisible
	public static void toggleVisible() {
		((InnerWindow) window).toggle();
	}
	//endregion

	//region SetInvisible
	public static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}
	//endregion

	//region SetLocation
	public static void setLocation(Point point){
		window.setLocation(point);
	}
	//endregion

	//region SetSettings
	public static void setSettings(){
		((InnerWindow) window).setSettings();
	}
	//endregion

	//region SetVisible
	public static void setVisible() {
		((InnerWindow) window).setVisible();
	}
	//endregion
}

