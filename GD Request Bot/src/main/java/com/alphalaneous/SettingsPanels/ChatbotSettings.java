package com.alphalaneous.SettingsPanels;

import com.alphalaneous.Components.SettingsPage;

import javax.swing.*;

public class ChatbotSettings {

    public static JPanel createPanel() {
        SettingsPage settingsPage = new SettingsPage("$CHATBOT_SETTINGS$");
        settingsPage.addCheckbox("$SILENT_MODE$", "$SILENT_MODE_DESC$", "silentMode");
        settingsPage.addCheckbox("$MULTI_THREAD$", "$MULTI_THREAD_DESC$", "multiMode", true, null);
        settingsPage.addCheckbox("$ANTI_DOX$", "$ANTI_DOX_DESC$", "antiDox", true, null);
        //settingsPage.addInput("$GLOBAL_COOLDOWN$", "$GLOBAL_COOLDOWN_DESC$", 1, "globalCooldown");
        return settingsPage;
    }

}
