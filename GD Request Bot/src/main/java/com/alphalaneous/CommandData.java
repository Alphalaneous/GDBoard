package com.alphalaneous;

import java.util.List;

public class CommandData {

    private final String commandName;
    private String description;
    private String jsCommand;
    private String message;
    private String type;
    private String detection;
    private String level;
    private List<Object> aliases;
    private int cooldown = 0;
    private boolean modOnly = false;
    private boolean editable = false;

    public CommandData(String commandName){
        this.commandName = commandName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setJsCommand(String jsCommand) {
        this.jsCommand = jsCommand;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDetection(String detection) {
        this.detection = detection;
    }

    public void setLevel(String level){
        this.level = level;
    }

    public void setModOnly(boolean modOnly) {
        this.modOnly = modOnly;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setAliases(List<Object> aliases) {
        this.aliases = aliases;
    }

    public void addAliases(String alias){
        if(this.aliases != null) {
            this.aliases.add(alias);
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public String getDescription() {
        return description;
    }

    public String getJsCommand() {
        return jsCommand;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getDetection() {
        return detection;
    }

    public String getLevel(){
        return level;
    }

    public boolean isModOnly() {
        return modOnly;
    }

    public boolean isEditable() {
        return editable;
    }

    public List<Object> getAliases(){
        return aliases;
    }

    public int getCooldown(){
        return cooldown;
    }
}
