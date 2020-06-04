package Main;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;

import java.util.Arrays;

public class Command {

    private static NashornSandbox sandbox = NashornSandboxes.create();
    public static String run(String user, boolean isMod, boolean isSub, String[] args, String function, int cheer) {
        sandbox.inject("isMod", isMod);
        sandbox.inject("isSub", isSub);
        sandbox.inject("user", user);
        sandbox.inject("args", args);
        sandbox.inject("cheer", cheer);
        String[] xArgs = Arrays.copyOfRange(args, 1, args.length);
        sandbox.inject("xArgs", xArgs);
        sandbox.allow(Requests.class);
        sandbox.allow(GDMod.class);
        try {
            sandbox.eval("var Levels = Java.type('Main.Requests'); var GD = Java.type('Main.GDMod');" + function);
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
            Main.sendMessage("There was an error with the command: " + e);
        }
        return result;
    }
}
