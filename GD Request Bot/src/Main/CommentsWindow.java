package Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

class CommentsWindow {
	private static JButton loadCommentsButton = new JButton("Load Comments");
	private static JPanel panel = new JPanel();

	private static int height = 350;
	private static int width = 300;
	private static JPanel window = new InnerWindow("Top Comments", 10, 500, width, height,
			"src/resources/Icons/Comments.png").createPanel();
	private static JButtonUI newUI = new JButtonUI();

	static void createPanel() {

		panel.setLayout(null);
		panel.setBounds(0, 0, width, height);
		
		newUI.setBackground(Defaults.MAIN);
		newUI.setHover(Defaults.HOVER);
		loadCommentsButton.setBackground(Defaults.MAIN);
		loadCommentsButton.setUI(newUI);
		loadCommentsButton.setBorder(BorderFactory.createEmptyBorder());
		loadCommentsButton.setForeground(Defaults.FOREGROUND);
		loadCommentsButton.setPreferredSize(new Dimension(width, height-30));
		loadCommentsButton.setFont(new Font("bahnschrift", Font.PLAIN, 20));
		loadCommentsButton.setBounds(0,0,width, height-30);
		panel.setBackground(Defaults.MAIN);
		panel.add(loadCommentsButton);
		JScrollPane scrollPane = new JScrollPane(panel);
		loadCommentsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				try {
					loadComments();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		panel.setPreferredSize(new Dimension(width, height-30));
		scrollPane.setBounds(1,31,width, height-30);
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(1, height));
		window.add(scrollPane);
		((InnerWindow) window).refreshListener();
		Overlay.addToFrame(window);
	}

	static void unloadComments() {
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(width, height-30));
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		loadCommentsButton.setVisible(true);
		for(Component component : panel.getComponents()) {
			if(component instanceof JPanel){
				panel.remove(component);
			}
		}
		panel.updateUI();
	}

	private static void loadComments() throws IOException {
		panel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		GetComments getComments = new GetComments();
		ArrayList<String> commentText = getComments.getComments(Requests.levels.get(LevelsWindow2.getSelectedID()).getLevelID())
				.get(0);
		System.out.println(Requests.levels.get(LevelsWindow2.getSelectedID()).getLevelID());
		ArrayList<String> commenterText = getComments
				.getComments(Requests.levels.get(LevelsWindow2.getSelectedID()).getLevelID()).get(1);
		int panelHeight = 0;
		for (int i = 0; i < commentText.size(); i++) {
			System.out.println(commentText.get(i));
			JPanel cmtPanel = new JPanel(null);
			cmtPanel.setBackground(Defaults.SUB_MAIN);
			cmtPanel.setPreferredSize(new Dimension(width, 64));
			JLabel commenter = new JLabel();
			commenter.setFont(new Font("bahnschrift", Font.BOLD, 14));
			commenter.setBounds(9, 4, (int) (width * 0.5), 18);
			JTextPane comment = new JTextPane();
			comment.setFont(new Font("bahnschrift", Font.PLAIN, 12));
			comment.setBounds(6, 24, width - 6, 46);
			cmtPanel.add(commenter);
			cmtPanel.add(comment);
			commenter.setForeground(Defaults.FOREGROUND);
			comment.setOpaque(false);
			comment.setEditable(false);
			comment.setForeground(Defaults.FOREGROUND);
			comment.setText(commentText.get(i));
			commenter.setText(commenterText.get(i));
			panel.add(cmtPanel);
			panelHeight = panelHeight + 64;
		}
		panel.setPreferredSize(new Dimension(width, panelHeight));
		panel.updateUI();
		loadCommentsButton.setVisible(false);
	}

	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		newUI.setBackground(Defaults.MAIN);
		newUI.setHover(Defaults.HOVER);
		newUI.setSelect(Defaults.SELECT);
		loadCommentsButton.setBackground(Defaults.MAIN);
		loadCommentsButton.setForeground(Defaults.FOREGROUND);
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
		/*if (commentPanels.size() != 0) {
			for (int i = 0; i < 5; i++) {
				commentPanels.get(i).setBackground(Defaults.SUB_MAIN);
				for (Component components : commentPanels.get(i).getComponents()) {
					components.setForeground(Defaults.FOREGROUND);
				}
			}
		}*/
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
