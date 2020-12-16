package com.alphalaneous;

import com.alphalaneous.SettingsPanels.ChaosModeSettings;
import com.alphalaneous.SettingsPanels.GeneralSettings;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;

import java.util.Arrays;

public class Command {

    private static NashornSandbox sandbox = NashornSandboxes.create();
    public static String run(String user, boolean isMod, boolean isSub, String[] args, String function, int cheer, boolean sayError, String messageID) {
        sandbox.inject("isMod", isMod);
        sandbox.inject("isChaos", ChaosModeSettings.enableChaos);
        sandbox.inject("isModChaos", ChaosModeSettings.modOnly);
        sandbox.inject("queueLength", GeneralSettings.queueLevelLength);
        sandbox.inject("isSub", isSub);
        sandbox.inject("user", user);
        sandbox.inject("args", args);
        sandbox.inject("cheer", cheer);
        sandbox.inject("messageID", messageID);

        String[] xArgs = Arrays.copyOfRange(args, 1, args.length);
        sandbox.inject("xArgs", xArgs);
        StringBuilder message = new StringBuilder();
        for(String msg : xArgs){
            message.append(" ").append(msg);
        }
        sandbox.inject("message", message.toString());

        sandbox.allow(com.alphalaneous.Requests.class);
        sandbox.allow(com.alphalaneous.GDMod.class);
        sandbox.allow(com.alphalaneous.Board.class);
        sandbox.allow(com.alphalaneous.Variables.class);
        sandbox.allow(com.alphalaneous.Utilities.class);
        sandbox.allow(com.alphalaneous.Twitch.class);
        sandbox.allow(com.alphalaneous.GDHelper.class);


        try {
            sandbox.eval("" +
                    "var Twitch = Java.type('com.alphalaneous.Twitch'); " +
                    "var Levels = Java.type('com.alphalaneous.Requests'); " +
                    "var GD = Java.type('com.alphalaneous.GDMod'); " +
                    "var Board = Java.type('com.alphalaneous.Board'); " +
                    "var Variables = Java.type('com.alphalaneous.Variables'); " +
                    "var GDHelper = Java.type('com.alphalaneous.GDHelper'); " +
                    "var Utilities = Java.type('com.alphalaneous.Utilities');" + function);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        String result = "";
        try {
            Object obj = sandbox.eval("command();");
            if(obj != null) {
                result = obj.toString();
            }
        } catch (Exception e) {
            //if(sayError) {
                Main.sendMessage(("There was an error with the command: " + e).replaceAll(System.getProperty("user.name"), "*****"));
                e.printStackTrace();
            //}
        }
        String spacelessResult = result.replaceAll(" ", "").toLowerCase();
        if(spacelessResult.startsWith("/color") || spacelessResult.startsWith("/block") || spacelessResult.startsWith("/unblock")){
            return "Use of that command is prohibited, nice try :)";
        }
        String[] words = result.split(" ");
        for (String word : words) {
            if (word.startsWith("$") && word.endsWith("$")) {
                String newWord = Language.getString(word.replaceAll("\\$", ""));
                result = result.replace(word, newWord);
            }
        }
        return result;
    }
    public static String run(String user, String message, String function){
        return run(user, false, false, message.split(" "), function, 0, true, null);
    }
    public static String run(String function, boolean sayError){
        return run("", false, false, new String[]{null, null}, function, 0, sayError, null);
    }
}
