package com.alphalaneous.Panels;

import com.alphalaneous.APIs;
import com.alphalaneous.Comment;
import com.alphalaneous.Components.JButtonUI;
import com.alphalaneous.Components.RoundedJButton;
import com.alphalaneous.Components.ScrollbarUI;
import com.alphalaneous.Defaults;
import com.alphalaneous.Requests;
import com.alphalaneous.Windows.Window;

import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.GDUser;
import com.github.alex1304.jdash.exception.MissingAccessException;
import com.github.alex1304.jdash.exception.SpriteLoadException;
import com.github.alex1304.jdash.graphics.SpriteFactory;
import com.github.alex1304.jdash.util.GDUserIconSet;
import org.apache.commons.text.StringEscapeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class CommentsPanel {
	private static JPanel panel = new JPanel();
	private static JPanel mainPanel = new JPanel(null);
	private static JButtonUI buttonUI = new JButtonUI();
	private static int width = 300;
	private static JPanel window = new JPanel();
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
		int height = 350;
		mainPanel.setBounds(1, 1, width, height + 30);
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
		scrollPane.setBounds(0, 30, width, height - 40);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
		mainPanel.add(scrollPane);
		//endregion

		//region Buttons Panel attributes
		buttons.setLayout(null);
		buttons.setBounds(0, mainPanel.getHeight() - 40, width, 40);
		buttons.setBackground(Defaults.TOP);
		mainPanel.add(buttons);
		//endregion


		//region Create Previous Page Button
		JButton prev = createButton("\uE760", 0, "$PREV_PAGE$");
		prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (page != 0) {
					page = page - 2;
					try {
						loadComments(page, topC);
					} catch (Exception ignored) {
					}
				}
			}
		});
		buttons.add(prev);
		//endregion

		JButton next = createButton("\uE761", 35, "$NEXT_PAGE$");
		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				page = page + 2;
				if (!loadComments(page, topC)) {
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

		JButton top = createButton("\uE19F", 90, "$TOP_COMMENTS$");
		top.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				page = 0;
				try {
					loadComments(0, true);
				} catch (Exception ignored) {
				}
			}
		});
		buttons.add(top);

		JButton newest = createButton("\uE823", 125, "$LATEST_COMMENTS$");
		newest.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				topC = false;
				page = 0;
				try {
					loadComments(0, false);
				} catch (Exception ignored) {
				}
			}
		});
		buttons.add(newest);


		newUI.setBackground(Defaults.MAIN);
		newUI.setHover(Defaults.HOVER);
		panel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 4));
		panel.setVisible(false);
		window.add(mainPanel);
	}

	//endregion
	public static void unloadComments(boolean reset) {
		panel.setVisible(false);
		if (reset) {
			topC = false;
			page = 0;
		}
		panel.removeAll();
		panel.setPreferredSize(new Dimension(width, 0));
		scrollPane.updateUI();
	}

	public static JPanel getComWindow() {
		scrollPane.setBounds(0, 0, width, 472);
		buttons.setBounds(0, 472, width, 40);

		return mainPanel;
	}

	public static void resetDimensions(int width, int height) {
		scrollPane.setBounds(0, 0, width, height - 80);
		buttons.setBounds(0, height - 80, width, 40);
	}

	private static AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
	private static SpriteFactory spriteFactory;

	static {
		try {
			spriteFactory = SpriteFactory.create();
		} catch (SpriteLoadException e) {
			e.printStackTrace();
		}
	}

	public static boolean loadComments(int page, boolean top) {
		topC = top;
		int width = CommentsPanel.width - 15;
		if (Requests.levels.size() == 0) {
			return false;
		}
		try {
			if (Window.showingMore) {
				Requests.levels.size();
				int panelHeight = 0;
				panel.removeAll();
				panel.setVisible(false);
				for (int j = 0; j < 2; j++) {

					ArrayList<Comment> commentA = APIs.getGDComments(page + j, top, Requests.levels.get(LevelsPanel.getSelectedID()).getLevelID());
					if (commentA == null || commentA.size() == 0) {
						return false;
					}
					for (int i = 0; i < commentA.size(); i++) {
						String percent;
						String username = commentA.get(i).getUsername();
						String likes = commentA.get(i).getLikes();
						String comment = String.format("<html><div WIDTH=%d>%s</div></html>", width - 15, StringEscapeUtils.unescapeHtml4(commentA.get(i).getComment()));
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
						commenter.setFont(Defaults.SEGOE.deriveFont(12f));
						if (username.equalsIgnoreCase(Requests.levels.get(LevelsPanel.getSelectedID()).getAuthor().toString())) {
							commenter.setForeground(new Color(47, 62, 195));
						} else {
							commenter.setForeground(Defaults.FOREGROUND);
						}
						commenter.setBounds(30, 2, commenter.getPreferredSize().width, 18);
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
									} catch (IOException ex) {
										ex.printStackTrace();
									}
								}
							}

							@Override
							public void mouseEntered(MouseEvent e) {
								super.mouseEntered(e);
								int center = (commenter.getPreferredSize().width) / 2;
								commenter.setFont(Defaults.SEGOE.deriveFont(13f));
								commenter.setBounds(30 + center - (commenter.getPreferredSize().width) / 2, 2, commenter.getPreferredSize().width + 5, 18);
							}

							@Override
							public void mouseExited(MouseEvent e) {
								super.mouseExited(e);
								commenter.setFont(Defaults.SEGOE.deriveFont(12f));
								commenter.setBounds(30, 2, commenter.getPreferredSize().width, 18);
							}
						});

						JLabel percentLabel = new JLabel(percent);
						percentLabel.setFont(Defaults.SEGOE.deriveFont(12f));
						percentLabel.setForeground(Defaults.FOREGROUND2);
						percentLabel.setBounds(commenter.getPreferredSize().width + 42, 2, percentLabel.getPreferredSize().width + 5, 18);


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
						likesLabel.setFont(Defaults.SEGOE.deriveFont(10f));
						likesLabel.setForeground(Defaults.FOREGROUND);
						likesLabel.setBounds(width - likesLabel.getPreferredSize().width - 26, 4, likesLabel.getPreferredSize().width + 5, 18);
						JLabel playerIcon = new JLabel();
						new Thread(() -> {
							GDUserIconSet iconSet;
							GDUser user;
							try {
								user = client.searchUser(username).block();
								assert user != null;
								iconSet = new GDUserIconSet(user, spriteFactory);
							} catch (MissingAccessException e) {
								user = client.searchUser("RobTop").block();
								assert user != null;
								iconSet = new GDUserIconSet(user, spriteFactory);
							}
							BufferedImage icon = iconSet.generateIcon(user.getMainIconType());
							Image imgScaled = icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);

							ImageIcon imgNew = new ImageIcon(imgScaled);
							playerIcon.setIcon(imgNew);
							playerIcon.setBounds(2, -5, 30 + 2, 30 + 2);
						}).start();

						JLabel content = new JLabel(comment);
						content.setFont(Defaults.SEGOE.deriveFont(12f));
						content.setForeground(Defaults.FOREGROUND);
						content.setBounds(9, 24, width - 15, content.getPreferredSize().height);
						panelHeight = panelHeight + 32 + content.getPreferredSize().height;

						cmtPanel.add(commenter);
						cmtPanel.add(content);
						cmtPanel.add(percentLabel);
						cmtPanel.add(likesLabel);
						cmtPanel.add(likeIcon);
						cmtPanel.add(playerIcon);

						cmtPanel.setPreferredSize(new Dimension(width, 28 + content.getPreferredSize().height));

						panel.add(cmtPanel);
						panel.setPreferredSize(new Dimension(width, panelHeight));
					}
				}
				scrollPane.getViewport().setViewPosition(new Point(0, 0));
				panel.setVisible(true);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public String getName() {
		return "Comments";
	}

	//region CreateButton
	private static JButton createButton(String icon, int x, String tooltip) {
		JButton button = new RoundedJButton(icon, tooltip);
		button.setFont(Defaults.SYMBOLS.deriveFont(20f));
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setForeground(Defaults.FOREGROUND);
		button.setBackground(Defaults.TOP);
		button.setUI(buttonUI);
		button.setBounds(x + 5, 5, 30, 30);
		return button;
	}
	//endregion

	//region RefreshUI
	public static void refreshUI() {
		buttonUI.setBackground(Defaults.TOP);
		buttonUI.setHover(Defaults.BUTTON_HOVER);
		buttonUI.setSelect(Defaults.SELECT);
		newUI.setBackground(Defaults.MAIN);
		newUI.setHover(Defaults.HOVER);
		newUI.setSelect(Defaults.SELECT);
		if (scrollPane != null) {
			scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
			scrollPane.setBackground(Defaults.MAIN);
			scrollPane.getViewport().setBackground(Defaults.SUB_MAIN);

		}
		for (Component component : panel.getComponents()) {
			if (component instanceof JPanel) {
				component.setBackground(Defaults.MAIN);
				for (Component component1 : ((JPanel) component).getComponents()) {
					if (component1 instanceof JLabel) {
						if (((JLabel) component1).getText().contains("%")) {
							component1.setForeground(Defaults.FOREGROUND2);
						} else if (((JLabel) component1).getText().equalsIgnoreCase(Requests.levels.get(LevelsPanel.getSelectedID()).getAuthor().toString())) {
							component1.setForeground(new Color(16, 164, 0));
						} else {
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
	//endregion

	//region SetInvisible
	//endregion

	//region SetLocation
	public static void setLocation(Point point) {
		window.setLocation(point);
	}
	//endregion

}

