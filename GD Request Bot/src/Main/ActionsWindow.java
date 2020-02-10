package Main;

import com.jidesoft.swing.ResizablePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ActionsWindow {

	private static int height = 60;
	private static int width = 260;
	private static ResizablePanel window = new InnerWindow("Actions", 1920 - width - 10, 200, width, height,
			"\uE7C9").createPanel();
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
		GridLayout layout = new GridLayout(1, 4);
		layout.setHgap(10);
		layout.setVgap(10);
		panel.setLayout(layout);

		//TODO make custom Yes/No dialog
		JButton block = new RoundedJButton("\uE8F8");
		//TODO Block level, Block requester, Block author
		block.setPreferredSize(new Dimension(50, 50));
		block.setUI(defaultUI);
		block.setBackground(Defaults.BUTTON);
		block.setForeground(Defaults.FOREGROUND);
		block.setBorder(BorderFactory.createEmptyBorder());
		block.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 20));
		block.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				if (Requests.levels.size() != 0) {
					Object[] options = { "Yes", "No" };
					int n = JOptionPane.showOptionDialog(Overlay.frame,
							"Block " + Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID() + "?",
							"Block ID? (Temporary Menu)", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
					if (n == 0) {
						File file = new File("blocked.txt");
						FileWriter fr;
						try {
							fr = new FileWriter(file, true);
							fr.write(Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID() + "\n");
							fr.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						Requests.levels.remove(LevelsWindow.getSelectedID());
						LevelsWindow.removeButton();

						Thread thread = new Thread(() -> {
							CommentsWindow.unloadComments(true);
							CommentsWindow.loadComments(0, false);
						});
						thread.start();
					}
					SongWindow.refreshInfo();
					InfoWindow.refreshInfo();
				}
				LevelsWindow.setOneSelect();
			}
		});
		JButton clear = new RoundedJButton("\uE107");
		clear.setPreferredSize(new Dimension(50, 50));
		clear.setUI(defaultUI);
		clear.setBackground(Defaults.BUTTON);
		clear.setForeground(Defaults.FOREGROUND);
		clear.setBorder(BorderFactory.createEmptyBorder());
		clear.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 20));
		clear.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				Object[] options = { "Yes", "No" };
				int n = JOptionPane.showOptionDialog(Overlay.frame,
						"Clear the queue?",
						"Clear? (Temporary Menu)", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (n == 0) {
					if (Requests.levels.size() != 0) {
						for (int i = 0; i < Requests.levels.size(); i++) {
							LevelsWindow.removeButton();
						}
						Requests.levels.clear();
						SongWindow.refreshInfo();
						InfoWindow.refreshInfo();
						CommentsWindow.unloadComments(true);
					}
					LevelsWindow.setOneSelect();
				}
			}
		});


		JButton skip = new RoundedJButton("\uE101");
		skip.setPreferredSize(new Dimension(50, 50));
		skip.setUI(defaultUI);
		skip.setBackground(Defaults.BUTTON);
		skip.setForeground(Defaults.FOREGROUND);
		skip.setBorder(BorderFactory.createEmptyBorder());
		skip.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 20));
		skip.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				if (Requests.levels.size() != 0) {


					if (!(Requests.levels.size() <= 1) && LevelsWindow.getSelectedID() == 0) {
						StringSelection selection = new StringSelection(
								Requests.levels.get(LevelsWindow.getSelectedID()+1).getLevelID());
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, selection);
					}
					if (LevelsWindow.getSelectedID() == 0 && Requests.levels.size() > 1) {
						Requests.levels.remove(LevelsWindow.getSelectedID());
						LevelsWindow.removeButton();
						
						Main.sendMessage("Now Playing " + Requests.levels.get(0).getName() + " ("
								+ Requests.levels.get(0).getLevelID() + "). Requested by "
								+ Requests.levels.get(0).getRequester());
					} else {
						Requests.levels.remove(LevelsWindow.getSelectedID());
						LevelsWindow.removeButton();
						
					}
					Thread thread = new Thread(() -> {
						CommentsWindow.unloadComments(true);
						CommentsWindow.loadComments(0, false);
					});
					thread.start();

				}
				LevelsWindow.setOneSelect();

				SongWindow.refreshInfo();
				InfoWindow.refreshInfo();
			}
		});

		JButton copy = new RoundedJButton("\uF0E3");
		copy.setPreferredSize(new Dimension(50, 50));
		copy.setUI(defaultUI);
		copy.setBackground(Defaults.BUTTON);
		copy.setForeground(Defaults.FOREGROUND);
		copy.setBorder(BorderFactory.createEmptyBorder());
		copy.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 20));
		copy.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				if (Requests.levels.size() != 0) {
					StringSelection selection = new StringSelection(
							Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID());
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
				}
			}
		});

		panel.add(skip);
		panel.add(copy);
		panel.add(block);
		panel.add(clear);
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
