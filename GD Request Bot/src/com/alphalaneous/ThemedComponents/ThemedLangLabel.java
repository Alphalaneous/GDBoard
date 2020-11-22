package com.alphalaneous.ThemedComponents;

import com.alphalaneous.Components.LangLabel;
import com.alphalaneous.Defaults;

import java.util.ArrayList;

public class ThemedLangLabel extends LangLabel {

	public static ArrayList<ThemedLangLabel> labels = new ArrayList<>();

	public ThemedLangLabel(String text) {
		super(text);
		labels.add(this);
	}
	public void refresh(){
		setForeground(Defaults.FOREGROUND);
	}
	public static void refreshAll(){
		for(ThemedLangLabel label : labels){
			label.refresh();
		}
	}
}
