package Main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

public class CheckBoxButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String buttonText;
	private final int width;
	private final int height;
	
	
	public CheckBoxButton(String buttonText, int width, int height){
		this.buttonText = buttonText;
		this.width = width;
		this.height = height;
	}
	
	public JButton createButton() {
		JButton button = new JButton();
		JButtonUI defaultUI = new JButtonUI();
		button.setBackground(new Color(0,0,0,0));
		defaultUI.setBackground(new Color(0,0,0,0));
		defaultUI.setHover(new Color(0,0,0,0));
		button.setUI(defaultUI);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setPreferredSize(new Dimension(width,height));
		JLabel buttonLabel = new JLabel(buttonText);
		buttonLabel.setForeground(Defaults.FOREGROUND);
		button.add(buttonLabel);
		
		return button;
	}
	
}
