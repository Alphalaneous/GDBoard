package Main;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;

import javax.script.ScriptException;

public class Command {

    private static NashornSandbox sandbox = NashornSandboxes.create();
    public static String run(String user, boolean isMod, boolean isSub, String[] args, String function, int cheer) throws ScriptException {
        sandbox.inject("isMod", isMod);
        sandbox.inject("isSub", isSub);
        sandbox.inject("user", user);
        sandbox.inject("args", args);
        sandbox.inject("cheer", cheer);

        sandbox.allow(Requests.class);
        try {
            sandbox.eval("var Levels = Java.type('Main.Requests');" + function);
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
            e.printStackTrace();
        }
        return result;
    }
}
