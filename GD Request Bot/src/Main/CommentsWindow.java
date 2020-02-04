package Main;
import com.jidesoft.swing.Resizable;
import com.jidesoft.swing.ResizablePanel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

class CommentsWindow {
	private static JButton top = new JButton("Top");
	private static JButton new1 = new JButton("New");
	private static JButton next = new JButton(">");
	private static JButton prev = new JButton("<");
	private static JPanel panel = new JPanel();
	private static JButtonUI defaultUI = new JButtonUI();
	private static int height = 350;
	private static int width = 300;
	private static ResizablePanel window = new InnerWindow("Comments", 10, 500, width, height,
			"src/resources/Icons/Comments.png"){
		@Override
		protected Resizable createResizable() {

			return new Resizable(this) {
				@Override
				public void resizing(int resizeCorner, int newX, int newY, int newW, int newH) {

					if(!(newH < 100 || newH > 800)) {
						if(newX + width >= 672 && newX <= 1248 && newY <= 93){
							newY = 93;
						}
					setBounds(getX(), newY, getWidth(), newH);
					height = newH;
					resetDimensions(width, newH-32);
						scrollPane.setBounds(1, 31, width + 1, newH - 62);
						buttons.setBounds(1, newH - 31, width, 30);
						scrollPane.updateUI();
					}
				}
			};
		}
	}.createPanel();
	private static JButtonUI newUI = new JButtonUI();
	private static JScrollPane scrollPane = new JScrollPane(panel);
	private static JPanel buttons = new JPanel();
	private static boolean topC = false;
	private static int page = 0;
	static void createPanel() {
		panel.setLayout(null);
		panel.setBounds(0, 0, width, height);
		buttons.setLayout(null);
		buttons.setBounds(1, height + 1 , width, 30);
		buttons.setBackground(Defaults.SUB_MAIN);
		buttons.add(top);
		top.setMargin(new Insets(0, 0, 0, 0));
		top.setBorder(BorderFactory.createEmptyBorder());
		top.setForeground(Defaults.FOREGROUND);
		top.setBackground(Defaults.TOP);
		top.setUI(defaultUI);
		top.setBounds(90,0,30,30);
		top.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				topC = true;
				page = 0;
				try {
					unloadComments(false);
					loadComments(0, true);
				} catch (Exception ignored) {
				}

			}
		});
		buttons.add(new1);
		new1.setMargin(new Insets(0, 0, 0, 0));
		new1.setBorder(BorderFactory.createEmptyBorder());
		new1.setForeground(Defaults.FOREGROUND);
		new1.setBackground(Defaults.TOP);
		new1.setUI(defaultUI);
		new1.setBounds(120,0,30,30);
		new1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				topC = false;
				page = 0;
				try {
					unloadComments(false);
					loadComments(0, false);
				} catch (Exception ignored) {
				}

			}
		});
		buttons.add(prev);
		prev.setMargin(new Insets(0, 0, 0, 0));
		prev.setBorder(BorderFactory.createEmptyBorder());
		prev.setForeground(Defaults.FOREGROUND);
		prev.setBackground(Defaults.TOP);
		prev.setUI(defaultUI);
		prev.setBounds(0,0,30,30);
		prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				if(page != 0){
					page--;
					try {
						unloadComments(false);
						loadComments(page, topC);
					} catch (Exception ignored) {
					}
				}
			}
		});
		buttons.add(next);
		next.setMargin(new Insets(0, 0, 0, 0));
		next.setBorder(BorderFactory.createEmptyBorder());
		next.setForeground(Defaults.FOREGROUND);
		next.setBackground(Defaults.TOP);
		next.setUI(defaultUI);
		next.setBounds(30,0,30,30);
		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				page++;
				try {
					unloadComments(false);
					loadComments(page, topC);
				} catch (Exception ignored) {
					page--;
					try {
						loadComments(page, topC);
					} catch (Exception f) {
						f.printStackTrace();
					}
				}

			}
		});
		newUI.setBackground(Defaults.MAIN);
		newUI.setHover(Defaults.HOVER);
		panel.setBackground(Defaults.SUB_MAIN);
		panel.setPreferredSize(new Dimension(width, height-30));
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setBounds(1,31,width + 1, height-30);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(1, height));
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
		window.add(buttons);
		((InnerWindow) window).refreshListener();
		Overlay.addToFrame(window);
	}


	static void unloadComments(boolean reset) {
		if(reset){
			topC = false;
			page = 0;
		}
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(width, height-30));
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		for(Component component : panel.getComponents()) {
			if(component instanceof JPanel){
				panel.remove(component);
			}
		}
		panel.updateUI();
	}

	static void loadComments(int page, boolean top) {

			panel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 4));

			GetComments getComments = new GetComments();
			ArrayList<String> commentText = null;
			try {
				commentText = getComments.getComments(Requests.levels.get(LevelsWindow2.getSelectedID()).getLevelID(), page, top)
						.get(0);
			} catch (Exception ignored) {

			}
			ArrayList<String> commenterText = null;
			try {
				commenterText = getComments
						.getComments(Requests.levels.get(LevelsWindow2.getSelectedID()).getLevelID(), page, top).get(1);
			} catch (Exception ignored) {
			}
		ArrayList<String> finalCommentText = commentText;
		ArrayList<String> finalCommenterText = commenterText;


		int panelHeight = 0;
			assert finalCommentText != null;
			try {
				for (int i = 0; i < finalCommentText.size() / 2; i++) {
					assert finalCommenterText != null;
					JPanel cmtPanel = new JPanel(null);
					cmtPanel.setBackground(Defaults.MAIN);

					JLabel commenter = new JLabel();
					commenter.setFont(new Font("bahnschrift", Font.BOLD, 14));
					commenter.setBounds(9, 4, (int) (width * 0.5), 18);
					JTextPane comment = new JTextPane();
					comment.setFont(new Font("bahnschrift", Font.PLAIN, 12));
					comment.setBounds(6, 24, width - 6, 60);
					cmtPanel.add(commenter);
					cmtPanel.add(comment);
					commenter.setForeground(Defaults.FOREGROUND);
					comment.setOpaque(false);
					comment.setEditable(false);
					comment.setForeground(Defaults.FOREGROUND);
					comment.setText(finalCommentText.get(i));
					commenter.setText(finalCommenterText.get(i));
					panel.add(cmtPanel);
					panelHeight = panelHeight + 32 + comment.getPreferredSize().height;
					cmtPanel.setPreferredSize(new Dimension(width, 28 + comment.getPreferredSize().height));
				}
			}
			catch (Exception ignored){

			}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		panel.setPreferredSize(new Dimension(width, panelHeight));
			panel.updateUI();
			scrollPane.getVerticalScrollBar().setValue(0);
			SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
	}

	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		newUI.setBackground(Defaults.MAIN);
		newUI.setHover(Defaults.HOVER);
		newUI.setSelect(Defaults.SELECT);
		for(Component component : panel.getComponents()) {
			if(component instanceof JPanel){
				component.setBackground(Defaults.MAIN);
				for(Component component1 : ((JPanel) component).getComponents()){
					if(component1 instanceof JLabel){
						component1.setForeground(Defaults.FOREGROUND);
					}
					if(component1 instanceof JTextPane){
						component1.setForeground(Defaults.FOREGROUND);
					}
				}
			}
		}
		panel.setBackground(Defaults.MAIN);
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
