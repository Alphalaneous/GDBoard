package com.alphalaneous;

import com.alphalaneous.ChatbotTab.GeometryDashCommands;
import com.alphalaneous.Components.*;
import com.alphalaneous.Panels.SettingsTitle;
import com.alphalaneous.SettingsPanels.*;
import com.alphalaneous.Tabs.ChatbotTab;
import com.alphalaneous.ThemedComponents.ThemedCheckbox;
import com.alphalaneous.ThemedComponents.ThemedIconCheckbox;
import com.alphalaneous.Windows.CommandEditor;
import com.alphalaneous.Tabs.SettingsTab;
import com.alphalaneous.Tabs.RequestsTab;
import com.alphalaneous.Windows.OfficerWindow;
import com.alphalaneous.Windows.Window;

import static com.alphalaneous.Defaults.defaultUI;
import static com.alphalaneous.Defaults.settingsButtonUI;

public class Themes {

	public static void refreshUI() {

		defaultUI.setBackground(Defaults.COLOR);
		defaultUI.setHover(Defaults.COLOR5);
		defaultUI.setSelect(Defaults.COLOR4);
		settingsButtonUI.setBackground(Defaults.COLOR2);
		settingsButtonUI.setHover(Defaults.COLOR5);
		settingsButtonUI.setSelect(Defaults.COLOR4);

		Window.refreshUI();

		RequestsTab.refreshUI();
		FancyTooltip.refreshAll();
		SettingsTitle.refreshAll();
		ThemedIconCheckbox.refreshAll();
		ThemedCheckbox.refreshAll();
		FancyTextArea.refreshAll();
		FancyPasswordField.refreshAll();
		RoundedJButton.refreshAll();
		CommandListElement.refreshAll();
		SettingsPage.refreshAll();
		SettingsComponent.refreshAll();
		ListView.refreshAll();
		OfficerWindow.refreshUI();


		SettingsTab.refreshUI();
		ChatbotTab.refreshUI();
		RequestsTab.refreshInfoPanel();
		CommandEditor.refreshUI();
		GeometryDashCommands.refreshUI();
	}
}

