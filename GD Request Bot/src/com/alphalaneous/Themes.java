package com.alphalaneous;

import com.alphalaneous.Components.FancyTooltip;
import com.alphalaneous.Panels.CommentsPanel;
import com.alphalaneous.Panels.InfoPanel;
import com.alphalaneous.Panels.LevelsPanel;
import com.alphalaneous.SettingsPanels.*;
import com.alphalaneous.Windows.CommandEditor;
import com.alphalaneous.Windows.SettingsWindow;
import com.alphalaneous.Windows.Window;

import static com.alphalaneous.Defaults.defaultUI;
import static com.alphalaneous.Defaults.settingsButtonUI;

public class Themes {

	public static void refreshUI() {

		Window.refreshUI();
		//ThemedJButton.refreshAll();
		//RoundedJButton.refreshAll();
		for (FancyTooltip toolTip : FancyTooltip.tooltips) {
			toolTip.refresh();
		}
		defaultUI.setBackground(Defaults.MAIN);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		settingsButtonUI.setBackground(Defaults.BUTTON);
		settingsButtonUI.setHover(Defaults.BUTTON_HOVER);
		settingsButtonUI.setSelect(Defaults.SELECT);
		CommentsPanel.refreshUI();
		SettingsWindow.refreshUI();
		LevelsPanel.refreshUI();
		InfoPanel.refreshUI();
		AccountSettings.refreshUI();
		PersonalizationSettings.refreshUI();
		GeneralBotSettings.refreshUI();
		BlockedSettings.refreshUI();
		BlockedUserSettings.refreshUI();
		BlockedCreatorSettings.refreshUI();
		GeneralSettings.refreshUI();
		CommandSettings.refreshUI();
		ChannelPointSettings.refreshUI();
		ShortcutSettings.refreshUI();
		RequestSettings.refreshUI();
		ChaosModeSettings.refreshUI();
		CommandEditor.refreshUI();
		RequestsLog.refreshUI();
		WindowedSettings.refreshUI();
		OutputSettings.refreshUI();
	}
}

