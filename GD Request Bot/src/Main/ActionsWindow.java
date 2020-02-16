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
	private static ResizablePanel window = new InnerWindow("Actions", Settings.getActionsWLoc().x, Settings.getActionsWLoc().y, width, height,
			"\uE7C9").createPanel();
	private static JPanel mainPanel = new JPanel();
	private static JPanel panel = new JPanel();
	private static JButtonUI defaultUI = new JButtonUI();

	//region Create Panel
	static void createPanel() {

		mainPanel.setBounds(1, 31, width, height);

		mainPanel.setLayout(null);

		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);

		panel.setPreferredSize(new Dimension(width, height));
		panel.setBounds(15, 5, width - 30, height - 10);
		if(!Settings.windowedMode) {
			mainPanel.setBackground(Defaults.MAIN);
			panel.setBackground(Defaults.MAIN);
		}
		else{
			mainPanel.setBackground(Defaults.SUB_MAIN);
			panel.setBackground(Defaults.SUB_MAIN);
		}
		panel.setLayout(new GridLayout(1, 4,10,10));

		//TODO make custom Yes/No dialog

		//region Create Skip Button
		JButton skip = createButton("\uE101");
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
		panel.add(skip);
		//endregion

		//region Create Copy Button
		JButton copy = createButton("\uF0E3");
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
		panel.add(copy);
		//endregion

		//region Create Block Button
		JButton block = createButton("\uE8F8");
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
						File file = new File(System.getenv("APPDATA") + "\\GDBoard\\blocked.txt");
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
		panel.add(block);
		//endregion

		//region Create Clear Button
		JButton clear = createButton("\uE107");
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
		panel.add(clear);
		//endregion

		mainPanel.add(panel);
		window.add(mainPanel);
		((InnerWindow) window).refreshListener();
		Overlay.addToFrame(window);
	}
	//endregion

	//region Create Button
	private static JButton createButton(String icon) {
		JButton button = new RoundedJButton(icon);
		button.setPreferredSize(new Dimension(50, 50));
		button.setUI(defaultUI);
		if(!Settings.windowedMode) {
			button.setBackground(Defaults.BUTTON);
		}
		else {
			button.setBackground(Defaults.MAIN);
		}
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 20));
		return button;
	}
	//endregion

	//region RefreshUI
	static void refreshUI() {
		((InnerWindow) window).refreshUI();

		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		if(!Settings.windowedMode) {
			defaultUI.setBackground(Defaults.BUTTON);
			mainPanel.setBackground(Defaults.MAIN);
			panel.setBackground(Defaults.MAIN);
		}
		else{
			defaultUI.setBackground(Defaults.MAIN);
			mainPanel.setBackground(Defaults.SUB_MAIN);
			panel.setBackground(Defaults.SUB_MAIN);
		}
		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				if(!Settings.windowedMode) {
					component.setBackground(Defaults.BUTTON);
				}
				else {
					component.setBackground(Defaults.MAIN);
				}
				component.setForeground(Defaults.FOREGROUND);
			}
		}
	}
	//endregion

	//region Toggle Visible
	static void toggleVisible() {
		((InnerWindow) window).toggle();
	}
	//endregion

	//region SetInvisible
	static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}
	//endregion

	//region SetVisible
	static void setVisible() {
		((InnerWindow) window).setVisible();
	}
	//endregion
}
