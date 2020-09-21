package Main;

import Main.SettingsPanels.GeneralSettings;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;

import java.util.Arrays;

public class Command {

    private static NashornSandbox sandbox = NashornSandboxes.create();
    public static String run(String user, boolean isMod, boolean isSub, String[] args, String function, int cheer, boolean sayError) {
        sandbox.inject("isMod", isMod);
        sandbox.inject("isChaos", GeneralSettings.isChaos);
        sandbox.inject("isChaosChaos", GeneralSettings.isChaosChaos);
        sandbox.inject("queueLength", GeneralSettings.queueLevelLength);
        sandbox.inject("isSub", isSub);
        sandbox.inject("user", user);
        sandbox.inject("args", args);
        sandbox.inject("cheer", cheer);
        String[] xArgs = Arrays.copyOfRange(args, 1, args.length);
        sandbox.inject("xArgs", xArgs);
        String message = "";
        for(String msg : xArgs){
            message = message + " " + msg;
        }
        sandbox.inject("message", message);

        sandbox.allow(Requests.class);
        sandbox.allow(GDMod.class);
        sandbox.allow(Board.class);
        sandbox.allow(Variables.class);
        sandbox.allow(Utilities.class);
        sandbox.allow(Twitch.class);


        try {
            sandbox.eval("" +
                    "var Twitch = Java.type('Main.Twitch'); " +
                    "var Levels = Java.type('Main.Requests'); " +
                    "var GD = Java.type('Main.GDMod'); " +
                    "var Board = Java.type('Main.Board'); " +
                    "var Variables = Java.type('Main.Variables'); " +
                    "var Utilities = Java.type('Main.Utilities');" + function);
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
            //}
        }
        String spacelessResult = result.replaceAll(" ", "").toLowerCase();
        if(spacelessResult.startsWith("/color") || spacelessResult.startsWith("/block") || spacelessResult.startsWith("/unblock")){
            return "Use of that command is prohibited, nice try :)";
        }
        String[] words = result.split(" ");
        for(int i = 0; i < words.length; i++){
            if(words[i].startsWith("$") && words[i].endsWith("$")){
                String newWord = Language.getString(words[i].replaceAll("\\$", ""));
                result = result.replace(words[i], newWord);
            }
        }
        return result;
    }
    public static String run(String user, String message, String function){
        return run(user, false, false, message.split(" "), function, 0, true);
    }
    public static String run(String function, boolean sayError){
        return run("", false, false, new String[]{null, null}, function, 0, sayError);
    }
}
