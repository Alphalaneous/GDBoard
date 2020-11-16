package com.alphalaneous;

import java.awt.*;
import java.net.URL;

public class ChannelPointReward {

	private long cost;
	private String title;
	private Color bgColor;
	private String prompt;
	private URL imgURL;

	ChannelPointReward(String title, String prompt, long cost, Color bgColor, URL imgURL){
		this.title = title;
		this.prompt = prompt;
		this.cost = cost;
		this.bgColor = bgColor;
		this.imgURL = imgURL;
	}
	public long getCost() {
		return cost;
	}

	public String getTitle() {
		return title;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public String getPrompt() {
		return prompt;
	}

	public URL getImgURL() {
		return imgURL;
	}
}
