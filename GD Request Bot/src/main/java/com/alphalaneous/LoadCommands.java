package com.alphalaneous;

import com.alphalaneous.ChatbotTab.DefaultCommands;
import com.alphalaneous.ChatbotTab.GeometryDashCommands;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoadCommands {

    public static ArrayList<CommandData> defaultCommands;
    public static ArrayList<CommandData> geometryDashCommands;
    public static ArrayList<CommandData> customCommands = new ArrayList<>();

    private static final BufferedReader defaultCommandsReader = new BufferedReader(
            new InputStreamReader(Objects.requireNonNull(
                    Main.class.getClassLoader().getResourceAsStream("Commands/default.json"))));
    private static final BufferedReader geometryCommandsReader = new BufferedReader(
            new InputStreamReader(Objects.requireNonNull(
                    Main.class.getClassLoader().getResourceAsStream("Commands/geometry.json"))));



    public static void loadCommands(){
        defaultCommands = loadJsonToCommandDataArrayList(readIntoString(defaultCommandsReader));
        geometryDashCommands = loadJsonToCommandDataArrayList(readIntoString(geometryCommandsReader));
        GeometryDashCommands.loadCommands();
        DefaultCommands.loadCommands();
    }

    public static void saveCommands(){

    }
    private static ArrayList<CommandData> loadJsonToCommandDataArrayList(String jsonData){
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray commandsArray = jsonObject.getJSONArray("commands");
        ArrayList<CommandData> commandDataArrayList = new ArrayList<>();
        for(int i = 0; i < commandsArray.length(); i++){
            try {
                JSONObject commandDataJson = commandsArray.getJSONObject(i);
                CommandData commandData = new CommandData(commandDataJson.getString("name"));
                commandData.setType(commandDataJson.getString("type"));
                commandData.setDetection(commandDataJson.optString("detection"));
                commandData.setDescription(commandDataJson.optString("description"));
                commandData.setEditable(commandDataJson.optBoolean("editable"));
                commandData.setModOnly(commandDataJson.optBoolean("modOnly"));
                commandData.setMessage(commandDataJson.optString("message"));
                commandData.setCooldown(commandDataJson.optInt("cooldown"));
                commandData.setJsCommand(commandDataJson.optString("command"));
                String level = commandDataJson.optString("level");
                if(level.equalsIgnoreCase("")){
                    level = "$EVERYONE$";
                }
                commandData.setLevel(level);
                JSONArray array = commandDataJson.optJSONArray("aliases");
                if(array != null){
                    commandData.setAliases(array.toList());
                }
                commandDataArrayList.add(commandData);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return commandDataArrayList;
    }

    private static String readIntoString(BufferedReader reader){
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
        }
        catch (IOException ignored){}
        return builder.toString();
    }
}
