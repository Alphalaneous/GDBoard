package Main;

import Main.Components.FancyTooltip;
import Main.Panels.*;
import Main.SettingsPanels.*;
import Main.Windows.CommandEditor;
import Main.Windows.SettingsWindow;
import Main.Windows.Window;

import static Main.Defaults.defaultUI;
import static Main.Defaults.settingsButtonUI;

public class Themes {

	public static void refreshUI() {

		Window.refreshUI();
		for(FancyTooltip toolTip : FancyTooltip.tooltips){
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
		CommandEditor.refreshUI();
		RequestsLog.refreshUI();
		WindowedSettings.refreshUI();
		OutputSettings.refreshUI();
	}
}

