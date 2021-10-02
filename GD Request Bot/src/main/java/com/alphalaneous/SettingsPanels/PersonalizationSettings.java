package com.alphalaneous.SettingsPanels;

import com.alphalaneous.Components.SettingsPage;
import com.alphalaneous.Defaults;
import com.alphalaneous.Settings;
import com.alphalaneous.Windows.Window;

import javax.swing.*;

public class PersonalizationSettings {

    public static JPanel createPanel() {
        SettingsPage settingsPage = new SettingsPage("$PERSONALIZATION_SETTINGS$");
        settingsPage.addRadioOption("$THEME_TEXT$", "", new String[]{"$LIGHT_MODE$", "$DARK_MODE$", "$SYSTEM_MODE$"}, "theme", "SYSTEM_MODE", PersonalizationSettings::setTheme);
        settingsPage.addCheckbox("$ALWAYS_ON_TOP$", "$ON_TOP_DESCRIPTION$", "onTop", PersonalizationSettings::setOnTop);
        settingsPage.addCheckbox("$DISABLE_NOTIFICATIONS$", "$DISABLE_NOTIFICATIONS_DESCRIPTION$","disableNotifications");
        return settingsPage;
    }
    public static void setTheme(){
        String theme = Settings.getSettings("theme").asString();
        if (theme.equalsIgnoreCase("DARK_MODE"))Defaults.setDark();
        else if (theme.equalsIgnoreCase("LIGHT_MODE")) Defaults.setLight();
        else Defaults.setSystem();
    }
    public static void setOnTop(){
        Window.setOnTop(Settings.getSettings("onTop").asBoolean());
    }
}
