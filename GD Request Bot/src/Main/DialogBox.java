package Main;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DialogBox {

	private static JButtonUI defaultUI = new JButtonUI();
	private static boolean active = false;
	private static JDialog frame = null;

	public static String showDialogBox(String title, String info, String subInfo, String[] options){
		final String[] value = {null};


		if(!active) {
			active = true;
			defaultUI.setBackground(Defaults.BUTTON);
			defaultUI.setHover(Defaults.BUTTON_HOVER);
			defaultUI.setSelect(Defaults.SELECT);
			frame = new JDialog();
			JPanel textPanel = new JPanel();
			JPanel titlePanel = new JPanel();
			frame.getRootPane().setBorder(new LineBorder(Defaults.ACCENT, 1));
			frame.getRootPane().setBackground(new Color(0, 0, 0));
			textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
			titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
			JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 6, 6));
			frame.setUndecorated(true);
			frame.setLayout(null);
			frame.getContentPane().setBackground(Defaults.TOP);
			JLabel titleLabel = new JLabel(title);
			JLabel infoLabel = new JLabel(info);
			JLabel subInfoLabel = new JLabel(subInfo);

			JDialog finalFrame = frame;
			MouseInputAdapter mia = new MouseInputAdapter() {
				Point location;
				Point pressed;

				public void mousePressed(MouseEvent me) {
					pressed = me.getLocationOnScreen();
					location = finalFrame.getLocation();
				}

				public void mouseDragged(MouseEvent me) {
					Point dragged = me.getLocationOnScreen();
					int x = (int) (location.getX() + dragged.getX() - pressed.getX());
					int y = (int) (location.getY() + dragged.getY() - pressed.getY());
					finalFrame.setLocation(x, y);
				}
			};
			frame.getContentPane().addMouseListener(mia);
			frame.getContentPane().addMouseMotionListener(mia);

			titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			titleLabel.setForeground(Defaults.FOREGROUND);
			infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			infoLabel.setForeground(Defaults.FOREGROUND);
			subInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			subInfoLabel.setForeground(Defaults.FOREGROUND);


			titlePanel.setBounds(30, 25, 340, 110);
			titlePanel.setBackground(new Color(0, 0, 0, 0));

			textPanel.setBounds(30, 70, 340, 110);
			textPanel.setBackground(new Color(0, 0, 0, 0));

			buttonPanel.setBounds(30, 140, 340, 35);
			buttonPanel.setBackground(new Color(0, 0, 0, 0));


			titlePanel.add(titleLabel);
			textPanel.add(infoLabel);
			textPanel.add(subInfoLabel);


			for (int i = 0; i < options.length; i++) {
				JButton button = createButton(options[i]);
				button.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						value[0] = button.getText();
					}
				});
				buttonPanel.add(button);
			}
			frame.setSize(new Dimension(400, 200));
			frame.setPreferredSize(new Dimension(400, 400));
			frame.setLocation((int) (Defaults.screenSize.getX() + Defaults.screenSize.getWidth() / 2 - 200), (int) (Defaults.screenSize.getY() + Defaults.screenSize.getHeight() / 2 - 100));
			frame.setResizable(false);
			frame.add(titlePanel);
			frame.add(textPanel);
			frame.add(buttonPanel);
			frame.setAlwaysOnTop(true);
			frame.setVisible(true);

			while (value[0] == null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			frame.setVisible(false);
			frame.dispose();
			active = false;
			return value[0];
		}
		else {
			frame.requestFocus();
		}
		return "";
	}
	private static JButton createButton(String text) {

		JButton button = new JButton(text);

		button.setForeground(Defaults.FOREGROUND);
		button.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		button.setBackground(Defaults.BUTTON);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());

		return button;
	}
}
