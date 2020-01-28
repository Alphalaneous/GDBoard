package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ActionsWindow {

	private static int height = 60;
	private static int width = 200;
	private static JPanel window = new InnerWindow("Actions", 1920 - width - 10, 200, width, height,
			"src/resources/Icons/Actions.png").createPanel();
	private static JPanel mainPanel = new JPanel();
	private static JPanel panel = new JPanel();
	private static JButtonUI defaultUI = new JButtonUI();

	static void createPanel() {

		mainPanel.setBounds(1, 31, width, height);
		mainPanel.setBackground(Defaults.MAIN);
		mainPanel.setLayout(null);

		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);

		panel.setPreferredSize(new Dimension(width, height));
		panel.setBounds(15, 5, width - 30, height - 10);
		panel.setBackground(Defaults.MAIN);
		GridLayout layout = new GridLayout(1, 3);
		layout.setHgap(10);
		layout.setVgap(10);
		panel.setLayout(layout);

		BufferedImage origNext = null;
		try {
			origNext = ImageIO.read(new File("src/resources/Icons/next.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		assert origNext != null;
		Image nextImg = origNext.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon nextNew = new ImageIcon(nextImg);

		JButton block = new RoundedJButton("block");
		block.setPreferredSize(new Dimension(50, 50));
		block.setUI(defaultUI);
		block.setBackground(Defaults.BUTTON);
		block.setForeground(Defaults.FOREGROUND);
		block.setBorder(BorderFactory.createEmptyBorder());
		block.setFont(new Font("bahnschrift", Font.BOLD, 14));
		block.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				if (Requests.levels.size() != 0) {
					Object[] options = { "Yes", "No" };
					int n = JOptionPane.showOptionDialog(Overlay.frame,
							"Block " + Requests.levels.get(LevelsWindow2.getSelectedID()).getLevelID() + "?",
							"Block ID? (Temporary Menu)", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
					if (n == 0) {
						File file = new File("blocked.txt");
						FileWriter fr;
						try {
							fr = new FileWriter(file, true);
							fr.write(Requests.levels.get(LevelsWindow2.getSelectedID()).getLevelID() + "\n");
							fr.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Requests.levels.remove(LevelsWindow2.getSelectedID());
						LevelsWindow2.removeButton();
						
						CommentsWindow.unloadComments();
					}
					SongWindow.refreshInfo();
					InfoWindow.refreshInfo();
				}
			}
		});

		// BufferedImage origSkip = null;
		// try {
		// origSkip = ImageIO.read(new File("src/resources/Icons/skip.png"));
		// } catch (IOException e1) {
		// TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// Image skipImg = origSkip.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		// ImageIcon skipNew = new ImageIcon(skipImg);

		JButton skip = new RoundedJButton(nextNew);
		skip.setPreferredSize(new Dimension(50, 50));
		skip.setUI(defaultUI);
		skip.setBackground(Defaults.BUTTON);
		skip.setForeground(Defaults.FOREGROUND);
		skip.setBorder(BorderFactory.createEmptyBorder());
		skip.setFont(new Font("bahnschrift", Font.BOLD, 14));
		skip.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				if (Requests.levels.size() != 0) {
					CommentsWindow.unloadComments();
					
					if (!(Requests.levels.size() <= 1)) {
						StringSelection selection = new StringSelection(
								Requests.levels.get(LevelsWindow2.getSelectedID()+1).getLevelID());
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, selection);
					}
					if (LevelsWindow2.getSelectedID() == 0 && Requests.levels.size() > 1) {
						Requests.levels.remove(LevelsWindow2.getSelectedID());
						LevelsWindow2.removeButton();
						
						Main.sendMessage("Now Playing " + Requests.levels.get(0).getName() + " ("
								+ Requests.levels.get(0).getLevelID() + "). Requested by "
								+ Requests.levels.get(0).getRequester());
					} else {
						Requests.levels.remove(LevelsWindow2.getSelectedID());
						LevelsWindow2.removeButton();
						
					}

				}
				SongWindow.refreshInfo();
				InfoWindow.refreshInfo();
			}
		});

		// TODO: Add blocking

		BufferedImage origCopy = null;
		try {
			origCopy = ImageIO.read(new File("src/resources/Icons/clipboard.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		assert origCopy != null;
		Image copyImg = origCopy.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon copyNew = new ImageIcon(copyImg);

		JButton copy = new RoundedJButton(copyNew);
		copy.setPreferredSize(new Dimension(50, 50));
		copy.setUI(defaultUI);
		copy.setBackground(Defaults.BUTTON);
		copy.setForeground(Defaults.FOREGROUND);
		copy.setBorder(BorderFactory.createEmptyBorder());
		copy.setFont(new Font("bahnschrift", Font.BOLD, 14));
		copy.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				if (Requests.levels.size() != 0) {
					StringSelection selection = new StringSelection(
							Requests.levels.get(LevelsWindow2.getSelectedID()).getLevelID());
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
				}
			}
		});

		panel.add(skip);
		panel.add(copy);
		panel.add(block);
		mainPanel.add(panel);
		window.add(mainPanel);
		((InnerWindow) window).refreshListener();
		Overlay.addToFrame(window);
	}

	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		mainPanel.setBackground(Defaults.MAIN);
		panel.setBackground(Defaults.MAIN);
		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				component.setBackground(Defaults.BUTTON);
				component.setForeground(Defaults.FOREGROUND);
			}
		}

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
