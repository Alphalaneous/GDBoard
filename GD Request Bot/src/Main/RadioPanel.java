package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class RadioPanel extends JPanel {

	public ArrayList<RadioButton> buttons = new ArrayList<RadioButton>();
	public String currentSelect = "";

	public RadioPanel(String[] labels) {
		int pos = 0;
		setBackground(new Color(0, 0, 0, 0));
		setOpaque(false);
		setLayout(null);
		for (String label : labels) {
			RadioButton radioButton = new RadioButton(label);
			radioButton.setBounds(0, pos, 365, 30);
			radioButton.setFont(Defaults.SEGOE.deriveFont(14f));
			radioButton.setBorder(BorderFactory.createEmptyBorder());
			radioButton.refresh();
			pos = pos + 30;

			radioButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					for (RadioButton button : buttons) {
						if (button.getIdentifier().equalsIgnoreCase(radioButton.getIdentifier())) {
							button.setChecked(true);
							currentSelect = radioButton.getIdentifier();
						} else {
							button.setChecked(false);
						}
					}
				}
			});
			buttons.add(radioButton);
			add(radioButton);
		}
		setPreferredSize(new Dimension(getWidth(), pos + 30));
	}

	public void setChecked(String option) {
		for (RadioButton button : buttons) {
			if (button.getIdentifier().equalsIgnoreCase(option)) {
				button.setChecked(true);
				currentSelect = option;
			} else {
				button.setChecked(false);
			}
		}
	}

	public String getSelectedButton() {
		for (RadioButton button : buttons) {
			if (button.getSelectedState()) {
				return button.getIdentifier();
			}
		}
		return null;
	}

	public void refreshUI() {
		for (RadioButton button : buttons) {
			button.refresh();
		}
	}
	public void setWidth(int width){
		for (RadioButton button : buttons) {
			button.setPreferredSize(new Dimension(width, button.getHeight()));
			button.setBounds(button.getX(), button.getY(), width, button.getHeight());
			button.refresh();
		}
	}
}
