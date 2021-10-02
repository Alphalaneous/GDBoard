package com.alphalaneous.SettingsPanels;

import com.alphalaneous.Components.SettingsPage;

import javax.swing.*;

public class SpamProtectionSettings {

    public static JPanel createPanel(){
        SettingsPage settingsPage = new SettingsPage("$SPAM_PROTECTION_SETTINGS$");

        settingsPage.addConfigCheckbox("$CAPITAL_LETTER_SPAM$", "$CAPITAL_LETTER_SPAM_DESC$", "capitalLetterFilter");
        settingsPage.addConfigCheckbox("$SYMBOL_SPAM$", "$SYMBOL_SPAM_DESC$", "symbolFilter");
        settingsPage.addConfigCheckbox("$EMOTE_SPAM$", "$EMOTE_SPAM_DESC$", "emoteFilter");
        settingsPage.addConfigCheckbox("$LINK_SPAM$", "$LINK_SPAM_DESC$", "linkFilter");
        settingsPage.addConfigCheckbox("$GIBBERISH_SPAM$", "$GIBBERISH_SPAM_DESC$", "gibberishFilter");

        return settingsPage;
    }

}
