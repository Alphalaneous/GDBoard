package com.alphalaneous;

import com.alphalaneous.Panels.LevelsPanel;
import com.alphalaneous.TwitchBot.ChatMessage;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.regex.Pattern;

public class CommandNew {

    private static final NashornSandbox sandbox = NashornSandboxes.create();

    public static void run(ChatMessage message, boolean isCommand) {
        if(isCommand) {
            Main.sendMessage(replaceBetweenParentheses(message, message.getMessage(), message.getMessage().split(" ", 2)[1].split(" ")));
        }
        else {
            Main.sendMessage(replaceBetweenParentheses(message, message.getMessage(), message.getMessage().split(" ")));
        }
    }

    private static String replaceBetweenParentheses(ChatMessage message, String text, String[] arguments) {

        int pValue = 0;
        int sIndex = 0;
        int eIndex = 0;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '(' && text.charAt(i - 1) != '\\') {
                pValue++;
                sIndex = i;
            }
            if (text.charAt(i) == ')' && text.charAt(i - 1) != '\\') {
                if (pValue != 0) {
                    eIndex = i + 1;
                    break;
                }
            }
        }

        if (pValue != 0) {
            if (text.charAt(sIndex - 1) == '$') {

                String strS = text.substring(0, sIndex-1);
                String strE = text.substring(eIndex);
                String value = text.substring(sIndex + 1, eIndex - 1);
                String data = value.split(" ", 2)[1];
                String identifier = value.split(" ")[0];
                String replacement = "";

                switch (identifier) {
                    case "user": {
                        replacement = message.getSender();
                        break;
                    }

                    case "to_user": {
                        if(arguments.length > 0){
                            replacement = arguments[0];
                        }
                        else{
                            replacement = message.getSender();
                        }
                        break;
                    }
                    case "arg" : {
                        int arg;
                        try{
                            arg = Integer.parseInt(data);
                        }
                        catch (NumberFormatException e){
                            replacement = "Error: Argument is not an Integer.";
                            break;
                        }
                        if(arguments.length >= arg){
                            replacement = arguments[arg];
                        }
                        else{
                            replacement = "Error: Argument doesn't exist.";
                        }
                        break;
                    }
                    case "eval": {
                        try {
                            replacement = String.valueOf(sandbox.eval(data));
                        }
                        catch (Exception e){
                            replacement = e.toString();
                        }
                        break;
                    }
                    case "display_name": {
                        replacement = message.getDisplayName();
                        break;
                    }
                    case "user_id": {
                        replacement = message.getTag("user-id");
                        break;
                    }
                    case "message_id": {
                        replacement = message.getTag("id");
                        break;
                    }
                    case "bwomp" : {
                        Board.bwomp();
                        break;
                    }
                    case "level" : {
                        String[] levelArguments = data.split(",");
                        if(levelArguments.length > 2){
                            replacement = "Error: Too many arguments.";
                            break;
                        }
                        if(levelArguments.length < 2){
                            replacement = "Error: Too few arguments.";
                            break;
                        }
                        int pos;
                        try{
                            pos = Integer.parseInt(levelArguments[0].trim());
                            if(pos < RequestsUtils.getSize()) {
                                replacement = RequestsUtils.getLevel(pos, levelArguments[1].trim().toLowerCase(Locale.ROOT));
                            }
                            else {
                                replacement = "Error: Position to nonexistent level.";
                            }
                        }
                        catch (NumberFormatException e){
                            replacement = "Error: Position is not an Integer.";
                        }
                        break;
                    }
                    case "channel" :
                    case "broadcaster": {
                        replacement = TwitchAccount.login;
                        break;
                    }
                    default: {
                        replacement = "\\(" + value + "\\)";
                        break;
                    }
                }
                String result = strS + replacement + strE;
                return replaceBetweenParentheses(message, result, arguments);
            }
        }
        return text.replace("\\(", "(").replace("\\)", ")");
    }
}
