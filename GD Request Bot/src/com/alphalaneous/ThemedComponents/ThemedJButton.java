package com.alphalaneous.ThemedComponents;

import com.alphalaneous.Defaults;

import javax.swing.*;
import java.util.ArrayList;

public class ThemedJButton extends JButton {

	private static ArrayList<ThemedJButton> buttons = new ArrayList<>();

	public ThemedJButton(){
		buttons.add(this);
	}

	private String colorChoiceF;
	private String colorChoiceB;


	public ThemedJButton(String text){
		super(text);
		buttons.add(this);
	}

	public void setColorF(String color){
		colorChoiceF = color;
		setForeground(Defaults.colors.get(color));
	}
	public void setColorB(String color){
		colorChoiceB = color;
		setBackground(Defaults.colors.get(color));
	}

	private void refresh(){
		setForeground(Defaults.colors.get(colorChoiceF));
		setBackground(Defaults.colors.get(colorChoiceB));
	}

	public static void refreshAll(){
		for(ThemedJButton button : buttons){
			button.refresh();
		}
	}
}
