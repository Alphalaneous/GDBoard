//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.alphalaneous.TwitchBot;

public class ChatMessage {
    private final String[] tags;
    private final String sender;
    private final String displayName;
    private final String message;
    private final String[] badges;
    private final boolean isMod;
    private final boolean isSub;
    private final int cheerCount;

    public ChatMessage(String[] tags, String sender, String displayName, String message, String[] badges, boolean isMod, boolean isSub, int cheerCount) {
        this.tags = tags;
        this.sender = sender;
        this.displayName = displayName;
        this.message = message;
        this.badges = badges;
        this.isMod = isMod;
        this.isSub = isSub;
        this.cheerCount = cheerCount;
    }

    public String getTag(String tag) {

        for (String tagA : this.tags) {
            if (tagA.split("=", 2)[0].equals(tag)) {
                return tagA.split("=", 2)[1];
            }
        }

        return null;
    }

    public boolean isMod() {
        return this.isMod;
    }

    public boolean isSub() {
        return this.isSub;
    }

    public int getCheerCount() {
        return this.cheerCount;
    }

    public boolean hasBadge(String badge) {
        for (String badgeA : this.badges) {
            if (badgeA.split("/", 2)[0].equals(badge)) {
                return true;
            }
        }
        return false;
    }

    public String getSender() {
        return this.sender;
    }

    public String getDisplayName() {
        return this.displayName;
    }


    public String getMessage() {
        return this.message;
    }
}
