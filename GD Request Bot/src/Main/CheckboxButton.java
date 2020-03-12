package Main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class CheckboxButton extends JButton {

	private JLabel text = new JLabel();
	private JLabel check = new JLabel("\uE739");
	private JLabel checkSymbol = new JLabel("\uE73E");
	private JLabel hover = new JLabel("\uE922");
	private boolean isChecked = false;
	public CheckboxButton(String label) {
		setLayout(null);
		text.setText(label);
		text.setForeground(getForeground());
		check.setForeground(getForeground());
		check.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 16));
		checkSymbol.setForeground(getForeground());
		checkSymbol.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 16));
		hover.setForeground(getForeground());
		hover.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 16));
		checkSymbol.setVisible(false);
		hover.setVisible(false);
		add(hover);
		add(checkSymbol);
		add(check);
		add(text);
		check.setForeground(Color.LIGHT_GRAY);
		setContentAreaFilled(false);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e){
				if (SwingUtilities.isLeftMouseButton(e)) {
					check.setText("\uE73B");
					check.setForeground(Color.LIGHT_GRAY);
					hover.setVisible(false);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (isChecked) {
						check.setText("\uE922");
						check.setForeground(Color.LIGHT_GRAY);
						checkSymbol.setVisible(false);
						isChecked = false;
					} else {
						check.setText("\uE73B");
						check.setForeground(new Color(75, 155, 240));
						checkSymbol.setVisible(true);
						isChecked = true;
					}
				}
				hover.setVisible(true);
			}
			public void mouseEntered(MouseEvent e) {
				hover.setVisible(true);
			}
			public void mouseExited(MouseEvent e) {
				if(!isChecked) {
					check.setText("\uE922");
					check.setForeground(Color.LIGHT_GRAY);
					checkSymbol.setVisible(false);
				}
				else {
					check.setText("\uE73B");
					check.setForeground(new Color(75, 155, 240));
					checkSymbol.setVisible(true);
				}
				hover.setVisible(false);
			}
		});
	}
	public boolean getSelectedState(){
		return isChecked;
	}
	public void setChecked(boolean checked){
		this.isChecked = checked;
		if(!isChecked) {
			check.setText("\uE922");
			check.setForeground(Color.LIGHT_GRAY);
			checkSymbol.setVisible(false);
		}
		else {
			check.setText("\uE73B");
			check.setForeground(new Color(75, 155, 240));
			checkSymbol.setVisible(true);
		}
	}
	/*public void setLText(String text) {
		this.text.setText(text);
		refresh();
	}*/
	public void refresh(){
		text.setForeground(getForeground());
		text.setFont(getFont());
		text.setBounds(0, (getHeight()/2)-(text.getPreferredSize().height/2), text.getPreferredSize().width+5, text.getPreferredSize().height+5);
		check.setForeground(getForeground());
		check.setBounds(getWidth()-20, 0, 30,30);
		checkSymbol.setForeground(getForeground());
		checkSymbol.setBounds(getWidth()-20, 0, 30,30);
		hover.setForeground(getForeground());
		hover.setBounds(getWidth()-20, 0, 30,30);
	}
}
