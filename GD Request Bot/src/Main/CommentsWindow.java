package Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

class CommentsWindow {
	private static ArrayList<JPanel> commentPanels = new ArrayList<>();
	private static ArrayList<JTextPane> commentContent = new ArrayList<>();
	private static ArrayList<JLabel> commenterContent = new ArrayList<>();
	private static JButton loadCommentsButton = new JButton("Load Comments");
	private static JPanel panel = new JPanel();

	private static int height = 320;
	private static int width = 300;
	private static JPanel window = new InnerWindow("Top Comments", 10, 500, width, height,
			"src/resources/Icons/Comments.png").createPanel();
	private static JButtonUI newUI = new JButtonUI();

	static void createPanel() {

		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panel.setBounds(1, 31, width, height);
		
		newUI.setBackground(Defaults.MAIN);
		newUI.setHover(Defaults.HOVER);
		loadCommentsButton.setBackground(Defaults.MAIN);
		loadCommentsButton.setUI(newUI);
		loadCommentsButton.setBorder(BorderFactory.createEmptyBorder());
		loadCommentsButton.setForeground(Defaults.FOREGROUND);
		loadCommentsButton.setPreferredSize(new Dimension(width, height));
		loadCommentsButton.setFont(new Font("bahnschrift", Font.PLAIN, 20));
		panel.setBackground(Defaults.MAIN);
		panel.add(loadCommentsButton);
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
		window.add(panel);
		((InnerWindow) window).refreshListener();
		Overlay.addToFrame(window);
	}

	static void unloadComments() {
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		loadCommentsButton.setVisible(true);
		for (int i = 0; i < 5; i++) {
			if (commentPanels.size() != 0) {
				try {
				commentPanels.get(i).setVisible(false);
				}
				catch(IndexOutOfBoundsException e) {
					System.out.println("Length below 5");
					break;
				}
			}
		}
		commentPanels.clear();
		panel.updateUI();
	}

	private static void loadComments() throws IOException {
		panel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		GetComments getComments = new GetComments();
		ArrayList<String> commentText = getComments.getComments(Requests.levels.get(LevelsWindow2.getSelectedID()).getLevelID())
				.get(0);
		ArrayList<String> commenterText = getComments
				.getComments(Requests.levels.get(LevelsWindow2.getSelectedID()).getLevelID()).get(1);

		for (int i = 0; i < commentText.size(); i++) {
			JPanel cmtPanel = new JPanel(null);
			cmtPanel.setBackground(Defaults.SUB_MAIN);
			cmtPanel.setPreferredSize(new Dimension(width, 64));
			commentPanels.add(cmtPanel);
			JLabel commenter = new JLabel();
			commenter.setFont(new Font("bahnschrift", Font.BOLD, 14));
			commenter.setBounds(9, 4, (int) (width * 0.5), 18);
			JTextPane comment = new JTextPane();
			comment.setFont(new Font("bahnschrift", Font.PLAIN, 12));
			comment.setBounds(6, 24, width - 6, 46);
			commenterContent.add(commenter);
			commentContent.add(comment);
			cmtPanel.add(commenterContent.get(i));
			cmtPanel.add(commentContent.get(i));
			commenterContent.get(i).setForeground(Defaults.FOREGROUND);
			commentContent.get(i).setOpaque(false);
			commentContent.get(i).setEditable(false);
			commentContent.get(i).setForeground(Defaults.FOREGROUND);
			commentContent.get(i).setText(commentText.get(i));
			commenterContent.get(i).setText(commenterText.get(i));
			panel.add(commentPanels.get(i));
		}
		panel.updateUI();
		loadCommentsButton.setVisible(false);
	}

	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		newUI.setBackground(Defaults.MAIN);
		newUI.setHover(Defaults.HOVER);
		loadCommentsButton.setBackground(Defaults.MAIN);
		loadCommentsButton.setForeground(Defaults.FOREGROUND);
		if (commentPanels.size() != 0) {
			for (int i = 0; i < 5; i++) {
				commentPanels.get(i).setBackground(Defaults.SUB_MAIN);
				for (Component components : commentPanels.get(i).getComponents()) {
					components.setForeground(Defaults.FOREGROUND);
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
